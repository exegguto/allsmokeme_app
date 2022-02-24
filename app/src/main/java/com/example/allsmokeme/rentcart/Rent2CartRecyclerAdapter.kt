package com.example.allsmokeme.rentcart;

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.allsmokeme.OnHomeFragmentDataListener
import com.example.allsmokeme.OnRentCartFragmentDataListener
import com.example.allsmokeme.R
import com.example.allsmokeme.rentmix.RentMixModel
import com.example.allsmokeme.rentset.RentSetEndModel

//Реакции на действия с миксами

@Suppress("DEPRECATION")
class Rent2CartRecyclerAdapter(
  private val context: Context,
  private val catalogMix: ArrayList<RentSetEndModel?>,
  private val mListener: OnRentCartFragmentDataListener?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private var onItemClickListener: OnItemClickListener? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val view: View =
      LayoutInflater.from(parent.context).inflate(R.layout.item_setcart2, parent, false)
    return ReceiveViewHolder(view)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val viewHolder = holder as ReceiveViewHolder

    if(catalogMix.get(position)?.quantity!! > 0) {
      viewHolder.text.text = catalogMix.get(position)?.service
      viewHolder.price.text = catalogMix.get(position)?.prise.toString()
    }
  }

  override fun getItemCount(): Int { return catalogMix.size }

  inner class ReceiveViewHolder(itemView: View?) :
    RecyclerView.ViewHolder(itemView!!) {

    var text: TextView =  itemView!!.findViewById(R.id.text)
    var price: TextView =  itemView!!.findViewById(R.id.price)
    var del: Button = itemView!!.findViewById(R.id.del)

    init {
      del.setOnClickListener {
        catalogMix.removeAt(position)
        notifyDataSetChanged()
      }
    }
  }

  fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
    this.onItemClickListener = onItemClickListener
  }

  interface OnItemClickListener { fun onItemClick(productModel: RentSetEndModel?) }

}