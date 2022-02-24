package com.example.allsmokeme

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar


abstract class BaseActivity : AppCompatActivity() {
    private var textCartItemCount: TextView? = null
    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getViewId())
        onCreateView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { onBackPressed(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

/*
    private fun setupBadge() {
        if (textCartItemCount != null) {
            if (cartItems.size == 0) {
                if (textCartItemCount!!.visibility != View.GONE) {
                    textCartItemCount!!.visibility = View.GONE
                }
            } else {
                textCartItemCount!!.text = Math.min(cartItems.size, 99).toString()
                if (textCartItemCount!!.visibility != View.VISIBLE) {
                    textCartItemCount!!.visibility = View.VISIBLE
                }
            }
        }
    }
*/

/*    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.help_call, menu)
        val menuItem = menu.findItem(R.id.menu_cart)
        val actionView = menuItem?.actionView
        textCartItemCount = actionView?.findViewById(R.id.cart_badge)
        setupBadge()
        actionView?.setOnClickListener { v: View? -> onOptionsItemSelected(menuItem) }

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        return true
    }*/

    abstract fun getViewId(): Int
    abstract fun onCreateView()
}