package com.example.allsmokeme.firm

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allsmokeme.BaseFragment
import com.example.allsmokeme.R
import com.example.allsmokeme.product.ProductFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source


class FirmFragment : BaseFragment(), FirmRecyclerAdapter.OnItemClickListener {

    private var recyclerView: RecyclerView? = null
    private val listFirm: ArrayList<FirmModel> = ArrayList()
    private var listFirmTemp: ArrayList<String> = ArrayList()

    override val viewId: Int = R.layout.fragment_firm

    override fun onViewCreated(view: View?) {
        val db = FirebaseFirestore.getInstance()
        // Source can be CACHE, SERVER, or DEFAULT.
        val source = Source.CACHE
        listFirmTemp.clear()
        listFirm.clear()
        db.collection("tobacco") //получаем список фирм
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
//                    listFirmTemp.add(document.id)
                    val tobacco = document.toObject<FirmModel>(
                        FirmModel::class.java
                    )
                    tobacco.firmName = document.id
                    listFirm.add(tobacco)
                }
                val firmRecyclerAdapter = FirmRecyclerAdapter(
                    requireContext(),
                    listFirm
                ).also {
                    it.setOnItemClickListener(this)
                }
                recyclerView = view?.findViewById(R.id.recyclerView)
                recyclerView!!.layoutManager = GridLayoutManager(context, 1)
                recyclerView!!.adapter = firmRecyclerAdapter
                recyclerView!!.adapter?.notifyDataSetChanged()


/*
                for (num in 0 until listFirmTemp.size) {
                    db.collection("tobacco") //получаем фото фирмы
                        .document(listFirmTemp[num])
                        .get()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val document1 = task.result
                                if (document1!!.exists()) {
                                    val tobacco = document1.toObject<FirmModel>(
                                        FirmModel::class.java
                                    )
                                    tobacco?.firmName = listFirmTemp[num]
                                    tobacco?.firmPhoto = document1["photo"].toString()
                                    listFirm.add(tobacco!!)
                                }
                            }
                            if (num == listFirmTemp.size-1){
                                val firmRecyclerAdapter = FirmRecyclerAdapter(
                                    requireContext(),
                                    listFirm
                                ).also {
                                    it.setOnItemClickListener(this)
                                }
                                recyclerView = view?.findViewById(R.id.recyclerView)
                                recyclerView!!.layoutManager = GridLayoutManager(context, 2)
                                recyclerView!!.adapter = firmRecyclerAdapter
                                recyclerView!!.adapter?.notifyDataSetChanged()

                            }
                        }
                        .addOnFailureListener { //exception -> textView.setText("Error getting documents: "
                        }
                }
*/
            }
            .addOnFailureListener { //exception -> textView.setText("Error getting documents: ")
            }
    }


    override fun onItemClick(firmModel: FirmModel?) {
        val productFragment = ProductFragment()
        val bundle: Bundle? = arguments
        bundle?.putString("firmName", firmModel?.firmName)

        productFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.container, productFragment,"ProductFragment")
            .addToBackStack(bundle?.getString("Activity")+"_FirmFragment")
            .commit()

//        Переходит в корзину после добавдения товара
//        val intent = Intent(context, CartActivity::class.java)
//        context?.startActivity(intent)
//        activity?.invalidateOptionsMenu()
    }
}