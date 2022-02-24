package com.example.allsmokeme.tobacco

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.allsmokeme.BaseFragment
import com.example.allsmokeme.IOnBackPressed
import com.example.allsmokeme.R
import com.example.allsmokeme.cart.CartHelper
import com.example.allsmokeme.cart.ProductEntityModel
import com.example.allsmokeme.rentmix.MixActivity
import com.example.allsmokeme.product.ProductModel
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import java.math.BigDecimal


class TobaccoFragment : BaseFragment(), IOnBackPressed {
    var tobaccoRatingBar: RatingBar? = null
    var price: CollectionReference? = null
    var priceDoc: DocumentReference? = null
    var ratingAll: Float = 0F
    var ratingSum: Float = 0F
    var mixResult: String = ""
    var tobaccoPriseMix: Int = 0
    private var activity: Activity? = null

    override val viewId: Int = R.layout.fragment_tobacco

    override fun onViewCreated(view: View?) {

        val bundle: Bundle? = arguments
        val activ = bundle?.getString("Activity")

        val productModel = bundle?.getParcelableArrayList<ProductModel>("productModel")

//        val tobaccoPhoto = view?.findViewById<ImageView>(R.id.tobaccoPhoto)
        val tobaccoPrice  =  view?.findViewById<TextView>(R.id.tobaccoPrice)
        tobaccoRatingBar  =  view?.findViewById<RatingBar>(R.id.tobaccoRatingBar)
        val tobaccoName  =  view?.findViewById<TextView>(R.id.tobaccoName)
        val tobaccoAvailability  =  view?.findViewById<TextView>(R.id.tobaccoAvailability)
        val tobaccocompositionValue  =  view?.findViewById<TextView>(R.id.tobaccoСompositionValue)
        val tobaccoFortressValue  =  view?.findViewById<TextView>(R.id.tobaccoFortressValue)
        val tobaccoDescriptionValue  =  view?.findViewById<TextView>(R.id.tobaccoDescriptionValue)
        val tobaccoCountryValue  =  view?.findViewById<TextView>(R.id.tobaccoCountryValue)
        val tobaccoNicotineValue  =  view?.findViewById<TextView>(R.id.tobaccoNicotineValue)
        val tobaccobuttonRent  =  view?.findViewById<Button>(R.id.buttonRent)

        priceDoc = price?.document(productModel?.get(0)?.id.toString())

        if(activ == "MainActivity") {
            tobaccoPrice!!.text = productModel?.get(0)?.price.toString()
        }else if(activ == "MixActivity"){
            tobaccoPrice!!.text = "+" + productModel?.get(0)?.priceMix.toString()
        }

        tobaccoPriseMix = productModel?.get(0)?.priceMix!!.toInt()
        mixResult = productModel.get(0)?.firm + " " + productModel.get(0)?.name_eng + " " + productModel.get(0)?.name_ru
        tobaccoName!!.text = mixResult + " " + productModel.get(0)?.weight
        tobaccoAvailability!!.text = productModel.get(0)?.availability.toString()
        tobaccocompositionValue!!.text = productModel.get(0)?.composition.toString()
        tobaccoFortressValue!!.text = productModel.get(0)?.fortress.toString()
        tobaccoDescriptionValue!!.text = productModel.get(0)?.description.toString()
        tobaccoCountryValue!!.text = productModel.get(0)?.country.toString()
        tobaccoNicotineValue!!.text = productModel.get(0)?.nicotine.toString()

        if(productModel.get(0)?.rating != null && productModel.get(0)?.ratingSum?.toFloat() != null) {
            ratingAll = productModel.get(0)?.rating!!
            ratingSum = productModel.get(0)?.ratingSum?.toFloat()!!
            tobaccoRatingBar!!.rating = ratingAll/ratingSum
        }

        tobaccoRatingBar!!.setOnRatingBarChangeListener(RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->
            priceDoc?.update(
                mapOf(
                    "rating" to rating + ratingAll,
                    "ratingSum" to ratingSum + 1
                )
            )
        })

        var toast: Toast? = null

        tobaccobuttonRent?.setOnClickListener(View.OnClickListener {
            val product = ProductEntityModel()
            if(activ == "MainActivity") {
                product.id = productModel.get(0)?.id
                product.name_eng = productModel.get(0)?.name_eng
                product.composition = productModel.get(0)?.composition
                product.price = productModel.get(0)?.price?.let { BigDecimal.valueOf(it) }
//                product.photo = productModel.get(0)?.photo
                val cart = CartHelper.cart
                cart!!.add(product, 1)
                toast?.cancel()
                toast = Toast.makeText(context, "", Toast.LENGTH_LONG)
                toast?.setText(String.format(getString(R.string.cart_add_message)))
                toast?.show()
                getActivity()?.invalidateOptionsMenu()
            }else if(activ == "MixActivity"){
                if (context is Activity) activity = context as Activity // передаем значения массива в активити
                (activity as MixActivity).fragmentMixResul(mixResult, tobaccoPriseMix)
            }
        })

    }
    override fun onBackPressed(): Boolean {
        return false
    }
}
