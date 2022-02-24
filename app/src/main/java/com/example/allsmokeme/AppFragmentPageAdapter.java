package com.example.allsmokeme;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.allsmokeme.chat.NewChatFragment;
import com.example.allsmokeme.firm.NewFirmFragment;
import com.example.allsmokeme.renthookah.NewHomeFragment;

class AppFragmentPageAdapter extends FragmentPagerAdapter { //Кнопки нижнего меню

  public AppFragmentPageAdapter(FragmentManager fm) {
    super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case 0:
        return NewHomeFragment.Companion.newInstance();
      case 1:
        return NewFirmFragment.Companion.newInstance("MainActivity");
      case 2:
        return NotificationFragment.newInstance();
      case 3:
        return NotificationFragment.newInstance();
      case 4:
        return NewChatFragment.Companion.newInstance();
      default:
        throw new RuntimeException("Not supported");
    }
  }

  @Override
  public int getCount() {
    return 5;
  }
}
