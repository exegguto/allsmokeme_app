package com.example.allsmokeme.rentlogistic

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.allsmokeme.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class AddressCompleteAdapter(
    private val context: Context,
    private val addressList: MutableList<RentAddressModel>,
    private val activ: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_address, parent, false)
        return ReceiveViewHolder(view)
    }

    override fun onBindViewHolder( holder: RecyclerView.ViewHolder, position: Int ) {
        val viewHolder = holder as ReceiveViewHolder
        var tobaccoPrice = ""

        if(activ == "RentLogisticActivity") {
            tobaccoPrice = addressList[position].price.toString()
        }else if(activ == "MixActivity"){
            tobaccoPrice = addressList[position].price.toString()
        }

        viewHolder.textSitySeach.text = addressList[position].Sity + ", ул. " + addressList[position].Street + ", д. " +  addressList[position].House + ", кв. " + addressList[position].Apartment
        viewHolder.price.text = addressList[position].price.toString()
    }

    override fun getItemCount(): Int { return addressList.size }

    inner class ReceiveViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnLongClickListener {

        var textSitySeach: TextView =  itemView.findViewById(R.id.textSitySeach)
        var price: TextView =  itemView.findViewById(R.id.price)
        var textTable: TableLayout =  itemView.findViewById(R.id.textTable)
//        var del: Button = itemView.findViewById(R.id.del)

        init {
            textTable.setOnClickListener {   //применить выбранный адрес
                onItemClickListener?.onItemClick(addressList[adapterPosition])
            }
/*
            del.setOnClickListener {
                addressList.removeAt(position)
                notifyDataSetChanged()
            }
*/
            textTable.setOnLongClickListener(this)
        }

        override fun onLongClick(view: View): Boolean {
            val popupMenu = PopupMenu(context, textTable)
            popupMenu.inflate(R.menu.options_address)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.delete -> {
                        val db = FirebaseFirestore.getInstance()
                        db.collection("users")
                            .document(FirebaseAuth.getInstance().currentUser!!.uid)
                            .collection("address")
                            .document(addressList[adapterPosition].addressID)
                            .update("Visibility",false)

                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
            Toast.makeText(view.context, "long click", Toast.LENGTH_SHORT).show()

            return true
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(rentAddressModel: RentAddressModel)
        //fun onItemClickProduct(rentAddressModel: RentAddressModel)
    }
}