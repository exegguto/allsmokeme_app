package com.example.allsmokeme.rentset

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allsmokeme.BaseFragment
import com.example.allsmokeme.OnHomeFragmentDataListener
import com.example.allsmokeme.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source


class RentSetFragment : BaseFragment(), Rent2SetRecyclerAdapter.OnItemClickListener {

    var rentView1: RecyclerView? = null
    var rentView2: RecyclerView? = null
    private var rent2SetRecyclerAdapter: Rent2SetRecyclerAdapter? = null
    private val listTempSetModel: ArrayList<RentSetEndModel?>? = ArrayList()
    private val listTempSet2Model: ArrayList<RentSetEndModel?>? = ArrayList()
    private var mListener: OnHomeFragmentDataListener? = null

    override val viewId: Int = R.layout.fragment_set

    override fun onViewCreated(view: View?) {
        if (listTempSetModel!!.size > 0 && listTempSet2Model!!.size > 0) onUpdateList() //обновляет данные по клику
        else {
            // listTempSet.clear()
            //работа с базой данных
            val db = FirebaseFirestore.getInstance()
            // Source can be CACHE, SERVER, or DEFAULT.
            val source = Source.CACHE

            db.collection("rent_set")
                .get()
                .addOnSuccessListener { result ->

                    var num = 0
                    for (document in result) {
                        val rent1Set = document.toObject<RentSetModel>(RentSetModel::class.java)

                        if (rent1Set.mass1 != null && rent1Set.mass1!![3] as Boolean) {
                            val temp = RentSetEndModel()
                            temp.service = rent1Set.mass1?.get(0) as String
                            temp.prise = (rent1Set.mass1?.get(1) as Long).toInt()
                            temp.addMix = rent1Set.mass1?.get(2) as Boolean
                            if (num == 0) listTempSetModel.add(temp) else listTempSet2Model?.add(
                                temp
                            )
                        }
                        if (rent1Set.mass2 != null && rent1Set.mass2!![3] as Boolean) {
                            val temp = RentSetEndModel()
                            temp.prise = (rent1Set.mass2?.get(1) as Long).toInt()
                            temp.service = rent1Set.mass2?.get(0) as String
                            temp.addMix = rent1Set.mass2?.get(2) as Boolean
                            if (num == 0) listTempSetModel.add(temp) else listTempSet2Model?.add(
                                temp
                            )
                        }
                        if (rent1Set.mass3 != null && rent1Set.mass3!![3] as Boolean) {
                            val temp = RentSetEndModel()
                            temp.prise = (rent1Set.mass3?.get(1) as Long).toInt()
                            temp.service = rent1Set.mass3?.get(0) as String
                            temp.addMix = rent1Set.mass3?.get(2) as Boolean
                            if (num == 0) listTempSetModel.add(temp) else listTempSet2Model?.add(
                                temp
                            )
                        }
                        if (rent1Set.mass4 != null && rent1Set.mass4!![3] as Boolean) {
                            val temp = RentSetEndModel()
                            temp.prise = (rent1Set.mass4?.get(1) as Long).toInt()
                            temp.service = rent1Set.mass4?.get(0) as String
                            temp.addMix = rent1Set.mass4?.get(2) as Boolean
                            if (num == 0) listTempSetModel.add(temp) else listTempSet2Model?.add(
                                temp
                            )
                        }
                        if (rent1Set.mass5 != null && rent1Set.mass5!![3] as Boolean) {
                            val temp = RentSetEndModel()
                            temp.prise = (rent1Set.mass5?.get(1) as Long).toInt()
                            temp.service = rent1Set.mass5?.get(0) as String
                            temp.addMix = rent1Set.mass5?.get(2) as Boolean
                            if (num == 0) listTempSetModel.add(temp) else listTempSet2Model?.add(
                                temp
                            )
                        }
                        if (rent1Set.mass6 != null && rent1Set.mass6!![3] as Boolean) {
                            val temp = RentSetEndModel()
                            temp.prise = (rent1Set.mass6?.get(1) as Long).toInt()
                            temp.service = rent1Set.mass6?.get(0) as String
                            temp.addMix = rent1Set.mass6?.get(2) as Boolean
                            if (num == 0) listTempSetModel.add(temp) else listTempSet2Model?.add(
                                temp
                            )
                        }
                        if (rent1Set.mass7 != null && rent1Set.mass7!![3] as Boolean) {
                            val temp = RentSetEndModel()
                            temp.prise = (rent1Set.mass7?.get(1) as Long).toInt()
                            temp.service = rent1Set.mass7?.get(0) as String
                            temp.addMix = rent1Set.mass7?.get(2) as Boolean
                            if (num == 0) listTempSetModel.add(temp) else listTempSet2Model?.add(
                                temp
                            )
                        }
                        if (rent1Set.mass8 != null && rent1Set.mass8!![3] as Boolean) {
                            val temp = RentSetEndModel()
                            temp.prise = (rent1Set.mass8?.get(1) as Long).toInt()
                            temp.service = rent1Set.mass8?.get(0) as String
                            temp.addMix = rent1Set.mass8?.get(2) as Boolean
                            if (num == 0) listTempSetModel.add(temp) else listTempSet2Model?.add(
                                temp
                            )
                        }
                        if (rent1Set.mass9 != null && rent1Set.mass9!![3] as Boolean) {
                            val temp = RentSetEndModel()
                            temp.prise = (rent1Set.mass9?.get(1) as Long).toInt()
                            temp.service = rent1Set.mass9?.get(0) as String
                            temp.addMix = rent1Set.mass9?.get(2) as Boolean
                            if (num == 0) listTempSetModel.add(temp) else listTempSet2Model?.add(
                                temp
                            )
                        }
                        if (rent1Set.mass10 != null && rent1Set.mass10!![3] as Boolean) {
                            val temp = RentSetEndModel()
                            temp.prise = (rent1Set.mass10?.get(1) as Long).toInt()
                            temp.service = rent1Set.mass10?.get(0) as String
                            temp.addMix = rent1Set.mass10?.get(2) as Boolean
                            if (num == 0) listTempSetModel.add(temp) else listTempSet2Model?.add(
                                temp
                            )
                        }
                        num++
                    }
                    onUpdateList() //обновляет данные по клику
                }
                .addOnFailureListener { //exception -> textView.setText("Error getting documents: ")
                }
        }
    }

    override fun onItemPlusClicked(position: Int, rentSetEndModel: RentSetEndModel?) {
        var quantity = rentSetEndModel?.quantity?.toInt()
        val rentOrder: RentSetEndModel = rentSetEndModel!!
        quantity = quantity!! + 1
        rentOrder.quantity = quantity.toInt()
        rent2SetRecyclerAdapter!!.updateItem(position, rentOrder)
//        if(rentOrder.addMix) mListener?.addMix()
    }

    override fun onItemClick(rentSetEndModel: RentSetEndModel?) {
        // open details of product
    }
    override fun onItemMinusClicked(position: Int, rentSetEndModel: RentSetEndModel?) {
        var quantity = rentSetEndModel?.quantity?.toInt()
        if (quantity!! > 0) {
            val rentOrder: RentSetEndModel = rentSetEndModel!!
            quantity -= 1
            rentOrder.quantity = quantity.toInt()
            rent2SetRecyclerAdapter?.updateItem(position, rentOrder)
        }
    }

    override fun onUpdateList( ) {
        val rent1SetRecyclerAdapter1 = context?.let { Rent1SetRecyclerAdapter(
            it,
            listTempSetModel,
            mListener
        )}

//        rent1SetRecyclerAdapter!!.setOnItemClickListener(this)
        rentView1 = view?.findViewById(R.id.recyclerView1)
        rentView1!!.layoutManager = GridLayoutManager(context, 1)
        rentView1!!.itemAnimator = DefaultItemAnimator()
        rentView1!!.adapter = rent1SetRecyclerAdapter1

        rent2SetRecyclerAdapter = context?.let {Rent2SetRecyclerAdapter(
            it,
            listTempSet2Model,
            mListener
        )}

        rent2SetRecyclerAdapter!!.setOnItemClickListener(this)
        rentView2 = view?.findViewById(R.id.recyclerView2)
        rentView2!!.layoutManager = GridLayoutManager(context, 1)
        rentView2!!.itemAnimator = DefaultItemAnimator()
        rentView2!!.adapter = rent2SetRecyclerAdapter


 /*       productRecyclerAdapter = context?.let { Rent2SetRecyclerAdapter(
            it,
            CartHelper.cartItems as ArrayList<RentSetEnd?>
        ) }

        productRecyclerAdapter!!.setOnItemClickListener(this)
        rentView2 = view?.findViewById(R.id.recyclerView2)
        rentView2!!.layoutManager = GridLayoutManager(context, 1)
        rentView2!!.itemAnimator = DefaultItemAnimator()
        rentView2!!.adapter = productRecyclerAdapter*/
//        context?.invalidateOptionsMenu() обновить итого
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnHomeFragmentDataListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                    .toString() + " must implement OnFragment1DataListener"
            )
        }
    }
}
