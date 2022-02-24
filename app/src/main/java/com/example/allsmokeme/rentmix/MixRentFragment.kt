package com.example.allsmokeme.rentmix

import android.content.Context
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allsmokeme.BaseFragment
import com.example.allsmokeme.OnHomeFragmentDataListener
import com.example.allsmokeme.R

//выводит выбор миксов

class MixRentFragment : BaseFragment(){
    var rentView: RecyclerView? = null
    var listTemp: ArrayList<RentMixModel?>? = ArrayList()
    private var mListener: OnHomeFragmentDataListener? = null

    override val viewId: Int = R.layout.fragment_rent_mix

    override fun onViewCreated(view: View?) {


        val mixRecyclerAdapter = MixRecyclerAdapter(requireContext(), listTemp, mListener)
        rentView = view?.findViewById(R.id.recyclerViewMix)
        rentView!!.layoutManager = GridLayoutManager(context, 1)
        rentView!!.adapter = mixRecyclerAdapter

        val addMixClick: Button = view?.findViewById(R.id.addMix)!!
        addMixClick.setOnClickListener {
            listTemp?.add(RentMixModel())
            mListener?.rentListMix(listTemp!!)
            rentView!!.adapter?.notifyDataSetChanged()
        }
        mListener?.fragmentListMix(listTemp!!)
    }

    override fun onResume(){
        super.onResume()
        rentView!!.adapter?.notifyDataSetChanged()
    } //нужна что бы обновлялось, после добавления микса в смесь

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