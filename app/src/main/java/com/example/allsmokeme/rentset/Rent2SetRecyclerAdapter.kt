package com.example.allsmokeme.rentset;

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.allsmokeme.OnHomeFragmentDataListener
import com.example.allsmokeme.R


//Реакции на действия с миксами

@Suppress("DEPRECATION")
class Rent2SetRecyclerAdapter(
  private val context: Context,
  private val rentOrder: ArrayList<RentSetEndModel?>?,
  private val mListener: OnHomeFragmentDataListener?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val view: View =
      LayoutInflater.from(parent.context).inflate(R.layout.item_set2, parent, false)
    return ReceiveViewHolder(view)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val viewHolder = holder as ReceiveViewHolder
    viewHolder.service.text = rentOrder?.get(position)?.service
    viewHolder.price.text = rentOrder?.get(position)?.prise.toString()
    viewHolder.quantity.text = rentOrder?.get(position)?.quantity.toString()

  }

  override fun getItemCount(): Int { return rentOrder!!.size }

  private var onItemClickListener: OnItemClickListener? = null

  inner class ReceiveViewHolder(itemView: View?) :
    RecyclerView.ViewHolder(itemView!!) {

    var service: TextView =  itemView!!.findViewById(R.id.service)
    var price: TextView =  itemView!!.findViewById(R.id.price)
    var quantity: TextView =  itemView!!.findViewById(R.id.quantity)
    var plus: Button =  itemView!!.findViewById(R.id.plus)
    var minus: Button =  itemView!!.findViewById(R.id.minus)

    init {
/*
      quantity.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
        if (event.action == KeyEvent.ACTION_DOWN &&
          keyCode == KeyEvent.KEYCODE_ENTER
        ) {
          // сохраняем текст, введенный до нажатия Enter в переменную
          var temp = quantity.text.toString()
          rentOrder?.get(adapterPosition)?.quantity = Integer.parseInt(temp)
          updateItem(adapterPosition, rentOrder?.get(adapterPosition)!!)
          return@OnKeyListener true
        }
        false
      }
      )


      quantity.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
        if (event.action == KeyEvent.ACTION_DOWN) {
              // сохраняем текст, введенный до нажатия Enter в переменную
              val temp = quantity.text.toString()
              rentOrder?.get(adapterPosition)?.quantity = Integer.parseInt(temp)
              updateItem(adapterPosition, rentOrder?.get(adapterPosition)!!)
              return@OnEditorActionListener true
         }
        false
      })
*/

      quantity.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
        if (!hasFocus) {
          var temp = quantity.text.toString()
          if(temp == "") temp = "0"
          rentOrder?.get(adapterPosition)?.quantity = Integer.parseInt(temp)
          updateItem(adapterPosition, rentOrder?.get(adapterPosition)!!)
        }
      }

      minus.setOnClickListener {
        onItemClickListener!!.onItemMinusClicked(
          adapterPosition,
          rentOrder?.get(adapterPosition)
        )
      }
      plus.setOnClickListener {
        if (onItemClickListener != null) {
          onItemClickListener!!.onItemPlusClicked(
            adapterPosition,
            rentOrder?.get(adapterPosition)
          )
        }
      }
    }
  }

  fun updateItem(position: Int, rentSetEndModel: RentSetEndModel) {//сюда добавлять изменение заказа в корзине аренды
    if (rentSetEndModel.quantity > 0) {
      rentOrder?.set(position, rentSetEndModel)
    } else {
      onItemClickListener!!.onUpdateList()
    }
    mListener?.reCalcRent2Set(rentOrder!!)
    notifyDataSetChanged()
  }

  fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
    this.onItemClickListener = onItemClickListener
  }

  interface OnItemClickListener {
    fun onItemClick(rentSetEndModel: RentSetEndModel?)
    fun onItemPlusClicked(position: Int, rentSetEndModel: RentSetEndModel?)
    fun onItemMinusClicked(position: Int, rentSetEndModel: RentSetEndModel?)
    fun onUpdateList()
  }
}