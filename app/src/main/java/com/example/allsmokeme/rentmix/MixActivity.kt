package com.example.allsmokeme.rentmix

import android.content.Intent
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.allsmokeme.BaseActivity
import com.example.allsmokeme.R


class MixActivity : BaseActivity() {
    override fun getViewId(): Int { return R.layout.activity_mix }
    var mix: String = ""
    private var priseMix: Int = 0
    private var toolbar: Toolbar? = null

    override fun onCreateView() {
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        val adapter1 = MixFragmentPageAdapter(supportFragmentManager)
        viewPager.adapter = adapter1

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //установка кнопки наверх
//        supportActionBar?.setHomeButtonEnabled(true) //установка кнопки домой
    }
    fun fragmentMixResul(MixResul: String, prise: Int){mix = MixResul; priseMix = prise; onClickMix()}

    fun onClickMix() {
        val intent = Intent()
        intent.putExtra("mixResult", mix)
        intent.putExtra("priseMix", priseMix)
        setResult(RESULT_OK, intent)
        finish()
    }

    fun mixEnd(view: View) { //отмена добавления микса
        setResult(RESULT_CANCELED, null)
        finish()}
}