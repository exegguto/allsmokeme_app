package com.example.allsmokeme.cart

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allsmokeme.BaseFragment
import com.example.allsmokeme.R
import com.example.allsmokeme.cart.CartHelper.cart
import com.example.allsmokeme.cart.CartHelper.cartItems


class CartFragment : BaseFragment(), CartRecyclerAdapter.OnItemClickListener {
//    @BindView(R.id.recyclerView)
    private var recyclerView: RecyclerView? = null
    private var productRecyclerAdapter: CartRecyclerAdapter? = null
    private var toast: Toast? = null

    override val viewId: Int = R.layout.fragment_cart

    override fun onViewCreated(view: View?) {

        onUpdateList() //обновляет данные по клику
        val buyButton = view?.findViewById<Button>(R.id.buyButton)
        buyButton?.setOnClickListener(View.OnClickListener {//это типа оформление покупки
            toast?.cancel()
            toast = Toast.makeText(context, "", Toast.LENGTH_LONG)
            toast?.setText(String.format(
                getString(R.string.cart_success_message),
                cart!!.totalQuantity,
                cart!!.totalPrice
            ))
            toast?.show()
            cart?.clear()
            activity?.finish()
        })
    }
    override fun onItemClick(cartItemsEntityModel: CartItemsEntityModel?) {
        // open details of product
    }

    override fun onItemPlusClicked(position: Int, cartItemsEntityModel: CartItemsEntityModel?) {
        var quantity = cartItemsEntityModel?.quantity?.toInt()
        val cartModel = CartItemsEntityModel()
        cartModel.product = cartItemsEntityModel?.product
        quantity = quantity!! + 1
        cartModel.quantity = quantity.toLong()
        productRecyclerAdapter!!.updateItem(position, cartModel)
    }

    override fun onItemMinusClicked(position: Int, cartItemsEntityModel: CartItemsEntityModel?) {
        var quantity = cartItemsEntityModel?.quantity?.toInt()
        val cartModel = CartItemsEntityModel()
        cartModel.product = cartItemsEntityModel?.product
        quantity = quantity!! - 1;
        cartModel.quantity = quantity.toLong()
        productRecyclerAdapter?.updateItem(position, cartModel)
    }

    override fun onUpdateList() {
        productRecyclerAdapter = context?.let { CartRecyclerAdapter(
            it,
            cartItems as MutableList<CartItemsEntityModel>
        ) }
        productRecyclerAdapter!!.setOnItemClickListener(this)
        recyclerView = context?.findViewById(R.id.recyclerView)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        recyclerView!!.adapter = productRecyclerAdapter
        context?.invalidateOptionsMenu()
    }

    companion object {

        fun newInstance(): CartFragment {
            val bundle = Bundle()
            val cartFragment = CartFragment()
            cartFragment.arguments = bundle
            return cartFragment
        }
    }
}