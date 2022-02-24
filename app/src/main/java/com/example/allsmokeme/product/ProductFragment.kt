package com.example.allsmokeme.product

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allsmokeme.BaseFragment
import com.example.allsmokeme.IOnBackPressed
import com.example.allsmokeme.R
import com.example.allsmokeme.cart.CartHelper
import com.example.allsmokeme.cart.ProductEntityModel
import com.example.allsmokeme.rentmix.MixActivity
import com.example.allsmokeme.tobacco.TobaccoFragment
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import java.math.BigDecimal

class ProductFragment : BaseFragment(), ProductRecyclerAdapter.OnItemClickListener, IOnBackPressed {
    private val listTempMix: MutableList<ProductModel> = ArrayList()
    var price: CollectionReference? = null
    internal var activity: Activity? = null

    override val viewId: Int = R.layout.fragment_product

    override fun onViewCreated(view: View?) {
        
        //работа с базой данных
        val db = FirebaseFirestore.getInstance()
        // Source can be CACHE, SERVER, or DEFAULT.
        val source = Source.CACHE
        var recyclerView: RecyclerView? = null
        val bundle: Bundle? = arguments
        val activ = bundle?.getString("Activity")

        if (bundle != null) {}

        val firmName = bundle?.getString("firmName");

        listTempMix.clear()
        db.collection("tobacco") //получаем список коллекций (крепости)
            .document(firmName!!)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document!!.exists()) {
                        val list = document["collection"] as ArrayList<*>?
                        for (num in 0 until list?.size!!) {

                            price = db.collection("tobacco")
                                .document(firmName)
                                .collection(list[num].toString())

                            price!!
                                .get()
                                .addOnSuccessListener { result ->
                                    for (document1 in result) {
                                        val tobacco = document1.toObject<ProductModel>(
                                            ProductModel::class.java
                                        )
                                        tobacco.id = document1.id
                                        tobacco.firm = firmName
                                        tobacco.fortress  = list[num].toString()
                                        listTempMix.add(tobacco)

                                    }
                                    if(num == list.size-1){
                                        val productRecyclerAdapter = ProductRecyclerAdapter(
                                            requireContext(),
                                            listTempMix,
                                            activ!!
                                        ).also {
                                            it.setOnItemClickListener(this)
                                        }
                                        recyclerView = view?.findViewById(R.id.recyclerView)
                                        recyclerView!!.layoutManager = GridLayoutManager(context, 1)
                                        recyclerView!!.adapter = productRecyclerAdapter
                                        recyclerView!!.adapter?.notifyDataSetChanged()
                                    }
                                }
                                .addOnFailureListener {}
                        }
                    }
                }
            }
            .addOnFailureListener { //exception -> textView.setText("Error getting documents: "
            }
    }
    var toast: Toast? = null

    override fun onItemClick(productModel: ProductModel) {//добавить в корзину
        val bundle: Bundle? = arguments
        val activ = bundle?.getString("Activity")
        if(activ == "MainActivity") {
            val product = ProductEntityModel()
            product.id = productModel.id
            product.name_eng = productModel.name_eng
            product.name_ru = productModel.name_ru
            product.weight = productModel.weight
            product.composition = productModel.composition
            product.price = productModel.price.let { BigDecimal.valueOf(it) }
//            product.photo = productModel.photo
            val cart = CartHelper.cart
            cart!!.add(product, 1)
            toast?.cancel()
            toast = Toast.makeText(context, "", Toast.LENGTH_LONG)
            toast?.setText(String.format(getString(R.string.cart_add_message)))
            toast?.show()
            getActivity()?.invalidateOptionsMenu()
        }else if(activ == "MixActivity"){
            val mixResult = productModel.firm + " " + productModel.name_eng + " " + productModel.name_ru
            val tobaccoPriseMix = productModel.priceMix
            if (context is Activity) activity = context as Activity // передаем значения массива в активити
            (activity as MixActivity).fragmentMixResul(mixResult, tobaccoPriseMix.toInt())

        }
//        Переходит в корзину после добавдения товара
//        val intent = Intent(context, CartActivity::class.java)
//        context?.startActivity(intent)
    }

    override fun onItemClickProduct(productModel: ProductModel) {//посмотреть карточку товара
        val tobaccoFragment = TobaccoFragment()

        val listTemp: MutableList<ProductModel> = ArrayList()
        listTemp.add(productModel)
        val bundle: Bundle? = arguments
        bundle?.putParcelableArrayList("productModel", ArrayList<ProductModel>(listTemp))

        tobaccoFragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, tobaccoFragment, "TobaccoFragment")
            .addToBackStack(bundle?.getString("Activity")+"ProductFragment")
            .commit()
    }

    override fun onBackPressed(): Boolean {
        return false
    }

}
