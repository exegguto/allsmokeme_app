package com.example.allsmokeme.rentmix;

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.allsmokeme.OnHomeFragmentDataListener
import com.example.allsmokeme.R
import com.google.android.material.switchmaterial.SwitchMaterial

//Реакции на действия с миксами

@Suppress("DEPRECATION")
class MixRecyclerAdapter(
  private val context: Context,
  private val rentOrder: ArrayList<RentMixModel?>?,
  private val mListener: OnHomeFragmentDataListener?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private var onItemClickListener: OnItemClickListener? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val view: View =
      LayoutInflater.from(parent.context).inflate(R.layout.item_rent_mix, parent, false)
    return ReceiveViewHolder(view)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val viewHolder = holder as ReceiveViewHolder

    var priseMix: Int = 0
    val pos = position + 1
    viewHolder.name.text = "Смесь №$pos"
    viewHolder.addMixOnClick1.text = rentOrder?.get(position)?.mix1
    viewHolder.addMixOnClick2.text = rentOrder?.get(position)?.mix2
    viewHolder.addMixOnClick3.text = rentOrder?.get(position)?.mix3
    viewHolder.switch1.isChecked = rentOrder?.get(position)?.switch1!!
    viewHolder.switch2.isChecked = rentOrder[position]?.switch2!!
    viewHolder.switch3.isChecked = rentOrder[position]?.score!!
    viewHolder.switch4.isChecked = rentOrder[position]?.scorePineaple!!
    viewHolder.priseScore.text = rentOrder.get(position)?.priseScore.toString()
    viewHolder.priseScorePineaple.text = rentOrder.get(position)?.priseScorePineaple.toString()
    viewHolder.addMixOnClick1.tag = position
    viewHolder.addMixOnClick2.tag = position
    viewHolder.addMixOnClick3.tag = position

    if(rentOrder.get(position)?.switch1!!)priseMix += rentOrder[position]?.prise1!!
    if (rentOrder[position]?.switch2!!) priseMix += rentOrder[position]?.prise2!!
    if (rentOrder[position]?.score!!) priseMix += rentOrder[position]?.priseScore!!
    if (rentOrder[position]?.scorePineaple!!) priseMix += rentOrder[position]?.priseScorePineaple!!

    if(rentOrder[position]?.mix1 != context.getString(R.string.chooseTaste)) {
      viewHolder.delMixOnClick1.isVisible = true
      viewHolder.addMixOnClick2.isVisible = true

      var priseMixtemp = -1
      if (priseMixtemp < rentOrder[position]?.priseMix1!!) priseMixtemp =
        rentOrder[position]?.priseMix1!!
      if (priseMixtemp < rentOrder[position]?.priseMix2!!) priseMixtemp =
        rentOrder[position]?.priseMix2!!
      if (priseMixtemp < rentOrder[position]?.priseMix3!!) priseMixtemp =
        rentOrder[position]?.priseMix3!!
      priseMix += priseMixtemp
    }else{
      viewHolder.addMixOnClick2.visibility = GONE
      viewHolder.delMixOnClick1.visibility = GONE
    }
    if(rentOrder.get(position)?.mix2 != context.getString(R.string.chooseTaste)) {
      viewHolder.delMixOnClick2.isVisible = true
      viewHolder.addMixOnClick3.isVisible = true
    }else{
      viewHolder.addMixOnClick3.visibility = GONE
      viewHolder.delMixOnClick2.visibility = GONE
    }
    if(rentOrder.get(position)?.mix3 != context.getString(R.string.chooseTaste)) viewHolder.delMixOnClick3.isVisible = true
    else viewHolder.delMixOnClick3.visibility = GONE

    viewHolder.priseMix.text = priseMix.toString()
  }

  override fun getItemCount(): Int { return rentOrder!!.size }

  inner class ReceiveViewHolder(itemView: View?) :
    RecyclerView.ViewHolder(itemView!!) {

    var name: TextView =  itemView!!.findViewById(R.id.addMixName)

    var switch1: SwitchMaterial = itemView!!.findViewById(R.id.switch1)
    var switch2: SwitchMaterial = itemView!!.findViewById(R.id.switch2)
    var switch3: SwitchMaterial = itemView!!.findViewById(R.id.switch3)
    var switch4: SwitchMaterial = itemView!!.findViewById(R.id.switch4)
    var priseScore: TextView =  itemView!!.findViewById(R.id.priseScore)
    var priseScorePineaple: TextView =  itemView!!.findViewById(R.id.priseScorePineaple)

    var addMixOnClick1: TextView =  itemView!!.findViewById(R.id.addMixOnClick1)
    var addMixOnClick2: TextView =  itemView!!.findViewById(R.id.addMixOnClick2)
    var addMixOnClick3: TextView =  itemView!!.findViewById(R.id.addMixOnClick3)
    var priseMix: TextView =  itemView!!.findViewById(R.id.priseMix)

    var delMixOnClick1: Button = itemView!!.findViewById(R.id.delMixOnClick1)
    var delMixOnClick2: Button = itemView!!.findViewById(R.id.delMixOnClick2)
    var delMixOnClick3: Button = itemView!!.findViewById(R.id.delMixOnClick3)
    var delMixOnClickAll: Button = itemView!!.findViewById(R.id.delMixOnClickAll)

    init {
      switch1.setOnClickListener {
        if(switch1.isChecked) {
          val switch3 = rentOrder?.get(position)?.score
          val switch4 = rentOrder?.get(position)?.scorePineaple
          rentOrder?.set(position, RentMixModel())
          rentOrder?.get(position)?.switch1 = true
          rentOrder?.get(position)?.switch2 = false
          rentOrder?.get(position)?.score = switch3!!
          rentOrder?.get(position)?.scorePineaple = switch4!!
          mListener?.rentListMix(rentOrder!!)
          notifyItemChanged(position)
        }
      }
      switch2.setOnClickListener{
        if(switch2.isChecked){
          val switch3 = rentOrder?.get(position)?.score
          val switch4 = rentOrder?.get(position)?.scorePineaple
          rentOrder?.set(position, RentMixModel())
          rentOrder?.get(position)?.switch1 = false
          rentOrder?.get(position)?.switch2 = true
          rentOrder?.get(position)?.score = switch3!!
          rentOrder?.get(position)?.scorePineaple = switch4!!
          mListener?.rentListMix(rentOrder!!)
          notifyItemChanged(position)
        }
      }
      switch3.setOnClickListener {
        if(switch3.isChecked) {
          rentOrder?.get(position)?.score = true
          rentOrder?.get(position)?.scorePineaple = false
          mListener?.rentListMix(rentOrder!!)
          notifyItemChanged(position)
        }
      }
      switch4.setOnClickListener{
        if(switch4.isChecked){
          rentOrder?.get(position)?.score = false
          rentOrder?.get(position)?.scorePineaple = true
          mListener?.rentListMix(rentOrder!!)
          notifyItemChanged(position)
        }
      }
      delMixOnClick1.setOnClickListener {
        if(addMixOnClick1.text != context.getString(R.string.chooseTaste)){
          rentOrder?.get(position)?.mix1 =  context.getString(R.string.chooseTaste)
          rentOrder?.get(position)?.priseMix1 = 0
          mListener?.delMixOnClick(rentOrder!!, position)
          notifyItemChanged(position)
        }
      }
      delMixOnClick2.setOnClickListener {
        if(addMixOnClick2.text != context.getString(R.string.chooseTaste)){
          rentOrder?.get(position)?.mix2 =  context.getString(R.string.chooseTaste)
          rentOrder?.get(position)?.priseMix2 = 0
          mListener?.delMixOnClick(rentOrder!!, position)
          notifyItemChanged(position)
        }
      }
      delMixOnClick3.setOnClickListener {
        if(addMixOnClick3.text != context.getString(R.string.chooseTaste)){
          rentOrder?.get(position)?.mix3 =  context.getString(R.string.chooseTaste)
          rentOrder?.get(position)?.priseMix3 = 0
          mListener?.delMixOnClick(rentOrder!!, position)
          notifyItemChanged(position)
        }
      }
      delMixOnClickAll.setOnClickListener {
        rentOrder?.removeAt(position)
        mListener?.delMixOnClick(rentOrder!!, -1)
        delMixOnClick1.visibility = GONE
        delMixOnClick2.visibility = GONE
        delMixOnClick3.visibility = GONE
        addMixOnClick2.visibility = GONE
        addMixOnClick3.visibility = GONE
//        notifyItemRemoved(position)
        notifyDataSetChanged()
      }
    }
  }

  fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
    this.onItemClickListener = onItemClickListener
  }

  interface OnItemClickListener { fun onItemClick(productModel: RentMixModel?) }

}