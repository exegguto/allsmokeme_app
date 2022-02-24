package com.example.allsmokeme

import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavItemSelectedListener     //    this.toolbar = toolbar;
    (  //  private final Toolbar toolbar;
    private val viewPager: ViewPager //, private val toolbar: Toolbar
) :
    BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//    toolbar.setTitle(item.getTitle()); //добавление надписей в тулбар
        val itemId = item.itemId
        if (itemId == R.id.menu_rent) {
            viewPager.currentItem = 0
            return true
        } else if (itemId == R.id.menu_shop) {
            viewPager.currentItem = 1
            return true
        } else if (itemId == R.id.menu_restaurant) {
            viewPager.currentItem = 2
            return true
        } else if (itemId == R.id.menu_mixology) {
            viewPager.currentItem = 3
            return true
        } else if (itemId == R.id.menu_chat) {
            viewPager.currentItem = 4
            return true
        }
        return false
    }
}