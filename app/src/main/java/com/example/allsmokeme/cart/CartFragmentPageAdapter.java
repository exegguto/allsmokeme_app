package com.example.allsmokeme.cart;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class CartFragmentPageAdapter extends FragmentPagerAdapter { //Кнопки нижнего меню

  public CartFragmentPageAdapter(FragmentManager fm) {
    super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    return CartFragment.Companion.newInstance();
  }

  @Override
  public int getCount() {
    return 1;
  }
}
