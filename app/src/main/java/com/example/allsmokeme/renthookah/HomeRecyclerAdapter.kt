package com.example.allsmokeme.renthookah;

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.allsmokeme.OnHomeFragmentDataListener
import com.example.allsmokeme.R
import com.google.firebase.storage.FirebaseStorage


@Suppress("DEPRECATION")
class HomeRecyclerAdapter(
  private val context: Context,
  private val catalogModels: MutableList<RentHookahModel?>?,
  private val mListener: OnHomeFragmentDataListener?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private var onItemClickListener: OnItemClickListener? = null
  var num: Boolean = true
  var selectedPosition: Int = -1

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val view: View =
      LayoutInflater.from(parent.context).inflate(R.layout.item_rent, parent, false)
    return ReceiveViewHolder(view)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val viewHolder = holder as ReceiveViewHolder

    if(num){
      selectedPosition = position
      viewHolder.buyButton.text = String.format(context.getString(R.string.buybutton_one))
    } else viewHolder.buyButton.text = String.format(context.getString(R.string.buybutton_all))

    viewHolder.userTipe.text = catalogModels?.get(selectedPosition)?.tipe
    viewHolder.userId.text = catalogModels?.get(selectedPosition)?.time
    viewHolder.userPrice_empty.text = String.format(
      context.getString(R.string.dollars_format),
      catalogModels?.get(selectedPosition)?.price_empty
    )
    if (catalogModels?.get(selectedPosition)!!.foto.toString() != "null") {
      val url: String = catalogModels.get(selectedPosition)!!.foto.toString()
      val storage = FirebaseStorage.getInstance()
      val gsReference = storage.getReferenceFromUrl(url)

      val ONE_MEGABYTE: Long = 1024 * 1024
      gsReference.getBytes(ONE_MEGABYTE)
        .addOnSuccessListener { bytes ->
          val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
          viewHolder.userFoto.setImageBitmap(bmp)
          // Data for "images/island.jpg" is returned, use this as needed
        }.addOnFailureListener {
          // Handle any errors
        }
    }
  }

  override fun getItemCount(): Int { return if (num) { catalogModels!!.size } else { 1 } }

  inner class ReceiveViewHolder(itemView: View?) :
    RecyclerView.ViewHolder(itemView!!) {

    var userTipe: TextView =  itemView!!.findViewById(R.id.userTipe)
    var userId: TextView =  itemView!!.findViewById(R.id.userId)
    var userFoto: ImageView =  itemView!!.findViewById(R.id.userFoto)
    var userPrice_empty: TextView =  itemView!!.findViewById(R.id.userPrice_empty)
    var buyButton: Button = itemView!!.findViewById(R.id.buttonRent)

     init {
      buyButton.setOnClickListener { view: View? ->
        if(num) {
          selectedPosition = position
          num = false
          mListener?.onOpenMixRentFragment(catalogModels?.get(selectedPosition))
        }else {
          num = true
          mListener?.onOpenMixRentFragment(null)
        }
        notifyDataSetChanged()
      }
    }
  }

  fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
    this.onItemClickListener = onItemClickListener
  }

  interface OnItemClickListener { fun onItemClick(productModel: RentHookahModel?) }
}