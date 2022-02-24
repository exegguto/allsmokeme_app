package com.example.allsmokeme.cart

import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.allsmokeme.BaseActivity
import com.example.allsmokeme.R

class CartActivity : BaseActivity() {
    private var toolbar: Toolbar? = null
    override fun getViewId(): Int { return R.layout.activity_cart }
    private var toast: Toast? = null

    override fun onCreateView() {
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        val adapter1 = CartFragmentPageAdapter(supportFragmentManager)
        viewPager.adapter = adapter1

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //установка кнопки наверх
    }

    fun onClickCartClear(view: View){
        toast?.cancel()
        toast = Toast.makeText(this, "", Toast.LENGTH_LONG)
        toast?.setText(String.format(
            getString(R.string.cart_dont_message)
        ))
        toast?.show()
        CartHelper.cart?.clear()
        this.finish()
    }

}