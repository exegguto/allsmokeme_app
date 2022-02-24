package com.example.allsmokeme.rentset;

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.allsmokeme.OnHomeFragmentDataListener
import com.example.allsmokeme.R
import com.example.allsmokeme.rentmix.RentMixModel
import kotlinx.android.synthetic.main.item_set2.view.*

//Реакции на действия с миксами

@Suppress("DEPRECATION")
class Rent1SetRecyclerAdapter(
  private val context: Context,
  private val catalogMixes: ArrayList<RentSetEndModel?>?,
  private val mListener: OnHomeFragmentDataListener?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private var onItemClickListener: Rent1SetRecyclerAdapter.OnItemClickListener? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val view: View =
      LayoutInflater.from(parent.context).inflate(R.layout.item_set1, parent, false)
    return ReceiveViewHolder(view)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val viewHolder = holder as ReceiveViewHolder
    viewHolder.checkBox.text = catalogMixes?.get(position)?.service.toString()
    when (catalogMixes?.get(position)!!.quantity) {
      1 -> viewHolder.checkBox.isChecked = true
      0 -> viewHolder.checkBox.isChecked = false
    }
    viewHolder.price.text = catalogMixes.get(position)?.prise.toString()
  }

  override fun getItemCount(): Int { return catalogMixes!!.size }

  inner class ReceiveViewHolder(itemView: View?) :
    RecyclerView.ViewHolder(itemView!!) {

    var checkBox: CheckBox =  itemView!!.findViewById(R.id.checkBox)
    var price: TextView =  itemView!!.findViewById(R.id.price)

    init {
      checkBox.setOnCheckedChangeListener { compoundButton, check ->
        if(check) catalogMixes?.get(position)!!.quantity = 1
        else catalogMixes?.get(position)!!.quantity = 0
        mListener?.reCalcRent1Set(catalogMixes)
      }
    }
  }

  fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
    this.onItemClickListener = onItemClickListener
  }

  interface OnItemClickListener { fun onItemClick(productModel: RentMixModel?) }

}