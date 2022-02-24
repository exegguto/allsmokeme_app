package com.example.allsmokeme.product;

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.allsmokeme.R
import com.google.firebase.storage.FirebaseStorage

class ProductRecyclerAdapter(
  private val context: Context,
  private val catalogProduct: MutableList<ProductModel>,
  private val activ: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private var onItemClickListener: OnItemClickListener? = null

  override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val view: View =
      LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
    return ReceiveViewHolder(view)
  }

  override fun onBindViewHolder( holder: RecyclerView.ViewHolder, position: Int ) {
    val viewHolder = holder as ReceiveViewHolder
    var tobaccoPrice = ""

    if(activ == "MainActivity") {
      tobaccoPrice = catalogProduct.get(position).price.toString()
    }else if(activ == "MixActivity"){
      tobaccoPrice = catalogProduct.get(position).priceMix.toString()
    }

    viewHolder.name_eng.text = catalogProduct[position].name_eng + " / " + catalogProduct[position].name_ru
//    viewHolder.titleru.text = catalogProduct[position].name_ru
    viewHolder.weight.text = catalogProduct[position].weight
    viewHolder.composition.text = "Вкусы: " + catalogProduct[position].composition
    viewHolder.buyButton.text = String.format(
      context.getString(R.string.dollars_format),
      tobaccoPrice
    )
/*
    if (catalogProduct.get(position).photo != null) {
      val url: String = catalogProduct.get(position).photo
      val storage = FirebaseStorage.getInstance()
      val gsReference = storage.getReferenceFromUrl(url)

      val ONE_MEGABYTE: Long = 1024 * 1024
      gsReference.getBytes(ONE_MEGABYTE)
        .addOnSuccessListener { bytes ->
          val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
          viewHolder.photo.setImageBitmap(bmp)
          // Data for "images/island.jpg" is returned, use this as needed
        }.addOnFailureListener {
          // Handle any errors
        }
    }
*/
//    Picasso.get().load(catalogProduct[position].photo).into(viewHolder.photo)
  }

  override fun getItemCount(): Int { return catalogProduct.size }

  inner class ReceiveViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    var name_eng: TextView =  itemView.findViewById(R.id.name_eng)
//    var titleru: TextView =  itemView.findViewById(R.id.titleru)
    var weight: TextView =  itemView.findViewById(R.id.weight)
    var composition: TextView =  itemView.findViewById(R.id.composition)
//    var photo: ImageView =  itemView.findViewById(R.id.photo)
    var buyButton: Button = itemView.findViewById(R.id.buyButton)
    var product: RelativeLayout =  itemView.findViewById(R.id.product)

    init {
      buyButton.setOnClickListener { view: View -> //добавить в корзину
        onItemClickListener?.onItemClick(catalogProduct[adapterPosition])
      }
      product.setOnClickListener { view: View -> //посмотреть карточку товара
        onItemClickListener?.onItemClickProduct(catalogProduct[adapterPosition])
      }
    }
  }

  fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
    this.onItemClickListener = onItemClickListener
  }

  interface OnItemClickListener {
    fun onItemClick(productModel: ProductModel)
    fun onItemClickProduct(productModel: ProductModel)
  }
}