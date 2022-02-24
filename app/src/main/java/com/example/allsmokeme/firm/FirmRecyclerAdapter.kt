package com.example.allsmokeme.firm;

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.allsmokeme.R
import com.google.firebase.storage.FirebaseStorage

class FirmRecyclerAdapter (
  private val context: Context,
  private val catalogFirm: ArrayList<FirmModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private var onItemClickListener: OnItemClickListener? = null

  override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val view: View =
      LayoutInflater.from(parent.context).inflate(R.layout.item_firm, parent, false)
    return ReceiveViewHolder(view)
  }

  override fun onBindViewHolder( holder: RecyclerView.ViewHolder, position: Int ) {
    val viewHolder = holder as ReceiveViewHolder

    viewHolder.nameFirm.text = catalogFirm[position].firmName

/*    if (catalogFirm[position].firmPhoto != null) {
      val url: String = catalogFirm[position].firmPhoto!!
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
    }*/

  }

  override fun getItemCount(): Int { return catalogFirm.size }

  inner class ReceiveViewHolder(itemView: View?) :
    RecyclerView.ViewHolder(itemView!!) {
//      var photo: ImageView =  itemView!!.findViewById(R.id.imageFirm)
      var nameFirm: TextView =  itemView!!.findViewById(R.id.nameFirm)

    init {
      nameFirm.setOnClickListener { view: View? ->
        onItemClickListener!!.onItemClick(catalogFirm.get(adapterPosition))
      }
    }
    }

  fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
    this.onItemClickListener = onItemClickListener
  }

  interface OnItemClickListener { fun onItemClick(firmModel: FirmModel?) }
}