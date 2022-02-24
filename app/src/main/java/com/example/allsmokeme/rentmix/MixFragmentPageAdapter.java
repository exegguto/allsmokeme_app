package com.example.allsmokeme.rentmix;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.allsmokeme.firm.NewFirmFragment;

class MixFragmentPageAdapter extends FragmentPagerAdapter { //Кнопки нижнего меню

  public MixFragmentPageAdapter(FragmentManager fm) {
    super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    return NewFirmFragment.Companion.newInstance("MixActivity");
  }

  @Override
  public int getCount() {
    return 1;
  }
}
