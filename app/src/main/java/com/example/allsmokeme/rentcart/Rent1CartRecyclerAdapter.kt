package com.example.allsmokeme.rentcart;

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.allsmokeme.OnRentCartFragmentDataListener
import com.example.allsmokeme.R
import com.example.allsmokeme.rentmix.RentMixModel

//Реакции на действия с миксами

@Suppress("DEPRECATION")
class Rent1CartRecyclerAdapter(
  private val context: Context,
  private val rentOrder: ArrayList<RentMixModel?>,
  private val mListener: OnRentCartFragmentDataListener?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private var onItemClickListener: OnItemClickListener? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val view: View =
      LayoutInflater.from(parent.context).inflate(R.layout.item_setcart1, parent, false)
    return ReceiveViewHolder(view)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val viewHolder = holder as ReceiveViewHolder
    val pos = position + 1
    viewHolder.addMixName.text = "Смесь №$pos"

    if(rentOrder.get(position)?.switch1!!){
      viewHolder.price.text = rentOrder[position]?.prise1.toString()
      viewHolder.mix1.text = "Микс на усмотрение кальянщика из простого табака"
      viewHolder.mix2.visibility = GONE
      viewHolder.mix3.visibility = GONE
    }else if (rentOrder.get(position)?.switch2!!) {
      viewHolder.price.text = rentOrder[position]?.prise2.toString()
      viewHolder.mix1.text = "Микс на усмотрение кальянщика из премиального табака"
      viewHolder.mix2.visibility = GONE
      viewHolder.mix3.visibility = GONE
    } else {
      if(rentOrder[position]?.priseMix1!! >= rentOrder[position]?.priseMix2!!) {
        if (rentOrder[position]?.priseMix1!! >= rentOrder[position]?.priseMix3!!)
          viewHolder.price.text = rentOrder[position]?.priseMix1.toString()
        else
          viewHolder.price.text = rentOrder[position]?.priseMix3.toString()
      }else
        if (rentOrder[position]?.priseMix2!! >= rentOrder[position]?.priseMix3!!)
          viewHolder.price.text = rentOrder[position]?.priseMix2.toString()
        else
          viewHolder.price.text = rentOrder[position]?.priseMix3.toString()

      viewHolder.mix1.text = rentOrder[position]?.mix1
      if(rentOrder[position]?.mix2 != context.resources.getString(R.string.chooseTaste))viewHolder.mix2.text = rentOrder[position]?.mix2
      else viewHolder.mix2.visibility = GONE
      if(rentOrder[position]?.mix3 != context.resources.getString(R.string.chooseTaste))viewHolder.mix3.text = rentOrder[position]?.mix3
      else viewHolder.mix3.visibility = GONE

    }
    if(rentOrder[position]?.score!! || rentOrder[position]?.scorePineaple!!){
      viewHolder.switch1.visibility = VISIBLE
      viewHolder.price1.visibility = VISIBLE
      viewHolder.del1.visibility = VISIBLE
    }
    if(rentOrder[position]?.score!!){
      viewHolder.switch1.text = context.resources.getString(R.string.score)
      viewHolder.price1.text = rentOrder[position]?.priseScore.toString()
    }
    if(rentOrder[position]?.scorePineaple!!){
      viewHolder.switch1.text = context.resources.getString(R.string.scorePineaple)
      viewHolder.price1.text = rentOrder[position]?.priseScorePineaple.toString()
    }
    if(!rentOrder[position]?.score!! && !rentOrder[position]?.scorePineaple!!){
      viewHolder.switch1.visibility = GONE
      viewHolder.price1.visibility = GONE
      viewHolder.del1.visibility = GONE
    }

  }

  override fun getItemCount(): Int { return rentOrder.size }

  inner class ReceiveViewHolder(itemView: View?) :
    RecyclerView.ViewHolder(itemView!!) {

    var addMixName: TextView =  itemView!!.findViewById(R.id.addMixName)
    var mix1: TextView =  itemView!!.findViewById(R.id.mix1)
    var mix2: TextView =  itemView!!.findViewById(R.id.mix2)
    var mix3: TextView =  itemView!!.findViewById(R.id.mix3)
    var price: TextView =  itemView!!.findViewById(R.id.price)
    var del: Button = itemView!!.findViewById(R.id.del)
    var switch1: TextView =  itemView!!.findViewById(R.id.switch1)
    var price1: TextView =  itemView!!.findViewById(R.id.price1)
    var del1: Button = itemView!!.findViewById(R.id.del1)

    init {
      del.setOnClickListener {
        rentOrder.removeAt(position)
        notifyDataSetChanged()
      }
      del1.setOnClickListener {
        if(rentOrder[position]?.score!!) rentOrder[position]?.score = false
        if(rentOrder[position]?.scorePineaple!!) rentOrder[position]?.scorePineaple = false
        notifyDataSetChanged()
      }
    }
  }

  fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
    this.onItemClickListener = onItemClickListener
  }

  interface OnItemClickListener { fun onItemClick(productModel: RentMixModel?) }

}