package com.example.allsmokeme.rentcart

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allsmokeme.BaseFragment
import com.example.allsmokeme.OnRentCartFragmentDataListener
import com.example.allsmokeme.R

//выводит выбор миксов

class RentCartFragment : BaseFragment(){
    private var rentView1: RecyclerView? = null
    private var rentView2: RecyclerView? = null
    private var rentView3: RecyclerView? = null
    private lateinit var rentOrder: RentCartModel
    private var mListener: OnRentCartFragmentDataListener? = null

    override val viewId: Int = R.layout.fragment_rent_cart

    override fun onViewCreated(view: View?) {
        rentOrder = mListener!!.getMass()
        
        if(rentOrder.mix.size > 0) {
            val rent1CartRecyclerAdapter = Rent1CartRecyclerAdapter(
                requireContext(),
                rentOrder.mix, mListener
            )

            rentView1 = view?.findViewById(R.id.recyclerView1)
            rentView1!!.layoutManager = GridLayoutManager(context, 1)
            rentView1!!.adapter = rent1CartRecyclerAdapter
        }
        if(rentOrder.rent1SetEndModel.size > 0) {
            val rent2CartRecyclerAdapter =
                Rent2CartRecyclerAdapter(requireContext(), rentOrder.rent1SetEndModel, mListener)

            rentView2 = view?.findViewById(R.id.recyclerView2)
            rentView2!!.layoutManager = GridLayoutManager(context, 1)
            rentView2!!.adapter = rent2CartRecyclerAdapter
        }
        if(rentOrder.rent2SetEndModel.size > 0) {
            val rent3CartRecyclerAdapter =
                Rent3CartRecyclerAdapter(requireContext(), rentOrder.rent2SetEndModel, mListener)

            rentView3 = view?.findViewById(R.id.recyclerView3)
            rentView3!!.layoutManager = GridLayoutManager(context, 1)
            rentView3!!.adapter = rent3CartRecyclerAdapter
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnRentCartFragmentDataListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                    .toString() + " must implement OnFragment1DataListener"
            )
        }
    }
}