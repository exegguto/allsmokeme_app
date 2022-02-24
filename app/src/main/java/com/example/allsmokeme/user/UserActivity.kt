package com.example.allsmokeme.user

import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.allsmokeme.BaseActivity
import com.example.allsmokeme.R

class UserActivity : BaseActivity() {
    private var toolbar: Toolbar? = null
    override fun getViewId(): Int { return R.layout.activity_user }
    private var toast: Toast? = null

    override fun onCreateView() {

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //установка кнопки наверх
    }

}