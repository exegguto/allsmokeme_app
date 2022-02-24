package com.example.allsmokeme.cart

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.allsmokeme.R
import com.example.allsmokeme.cart.CartHelper.cart
import com.google.firebase.storage.FirebaseStorage


class CartRecyclerAdapter(
    private val context: Context,
    private val productEntityModel: MutableList<CartItemsEntityModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    fun updateItem(position: Int, cartItemsEntityModel: CartItemsEntityModel) {
        if (cartItemsEntityModel.quantity > 0) {
            productEntityModel[position] = cartItemsEntityModel
            cart!!.update(cartItemsEntityModel.product!!, cartItemsEntityModel.quantity)
        } else {
            cart!!.remove(productEntityModel[position].product!!)
            onItemClickListener!!.onUpdateList()
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return ReceiveViewHolder(view)
    }

    override fun onBindViewHolder( holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ReceiveViewHolder

        viewHolder.name_eng.text = productEntityModel[position].product?.name_eng
        viewHolder.titleru.text = productEntityModel[position].product?.name_ru
        viewHolder.weight.text = productEntityModel[position].product?.weight
        viewHolder.composition.text = productEntityModel[position].product?.composition
        viewHolder.price!!.text = String.format(
            context.getString(R.string.dollars_format),
            productEntityModel[position].product?.price.toString()
        )
        if (productEntityModel[position].product?.photo != null) {
            val url: String? = productEntityModel[position].product?.photo
            val storage = FirebaseStorage.getInstance()
            val gsReference = storage.getReferenceFromUrl(url!!)

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
        viewHolder.quantity!!.text = productEntityModel[position].quantity.toString()
    }

    override fun getItemCount(): Int { return productEntityModel.size }

    inner class ReceiveViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var name_eng: TextView =  itemView.findViewById(R.id.name_eng)
        var titleru: TextView =  itemView.findViewById(R.id.titleru)
        var weight: TextView =  itemView.findViewById(R.id.weight)
        var composition: TextView =  itemView.findViewById(R.id.composition)
        var photo: ImageView =  itemView.findViewById(R.id.image)
        var product: RelativeLayout =  itemView.findViewById(R.id.product)

        var price  =  itemView.findViewById<TextView>(R.id.price)
        var quantity  =  itemView.findViewById<TextView>(R.id.quantity)
        var plus =  itemView.findViewById<Button>(R.id.plus)
        var minus =  itemView.findViewById<Button>(R.id.minus)

        init {
            itemView.setOnClickListener { view: View? ->
                onItemClickListener!!.onItemClick(productEntityModel[adapterPosition])
            }
            minus!!.setOnClickListener { view: View? ->
                onItemClickListener!!.onItemMinusClicked(
                    adapterPosition,
                    productEntityModel[adapterPosition])
            }
            plus!!.setOnClickListener { view: View? ->
                onItemClickListener!!.onItemPlusClicked(
                    adapterPosition,
                    productEntityModel[adapterPosition]
                )
            }
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(cartItemsEntityModel: CartItemsEntityModel?)
        fun onItemPlusClicked( position: Int, cartItemsEntityModel: CartItemsEntityModel?)
        fun onItemMinusClicked(position: Int, cartItemsEntityModel: CartItemsEntityModel?)
        fun onUpdateList()
    }

}