package com.example.allsmokeme.renthookah

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allsmokeme.BaseFragment
import com.example.allsmokeme.OnHomeFragmentDataListener
import com.example.allsmokeme.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source


class HomeFragment : BaseFragment(), HomeRecyclerAdapter.OnItemClickListener{ //Выводит список вариантов кальянов

//    var radioGroup: RadioGroup? = null
    var rentView: RecyclerView? = null
    private var mListener: OnHomeFragmentDataListener? = null

    override val viewId: Int = R.layout.fragment_home

    override fun onViewCreated(view: View?) {
        //работа с базой данных
        val db = FirebaseFirestore.getInstance()
        val listTemp: MutableList<RentHookahModel?>? = ArrayList()
        // Source can be CACHE, SERVER, or DEFAULT.
        val source = Source.CACHE

        db.collection("rent")
            .get()
            .addOnSuccessListener { result ->
                if (listTemp!!.size > 0) listTemp.clear()
                for (document in result) {
                    val user = document.toObject<RentHookahModel>(RentHookahModel::class.java)
                    user.id = document.id
                    listTemp.add(user)
                    val productRecyclerAdapter = HomeRecyclerAdapter(requireContext(), listTemp, mListener).also {
                        it.setOnItemClickListener(this)
                    }
                    rentView = view?.findViewById(R.id.recyclerView)
                    rentView!!.layoutManager = GridLayoutManager(context, 1)
                    rentView!!.adapter = productRecyclerAdapter
                }
            }
            .addOnFailureListener { //exception -> textView.setText("Error getting documents: ")
            }
    }

    override fun onItemClick(productModel: RentHookahModel?) {}

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