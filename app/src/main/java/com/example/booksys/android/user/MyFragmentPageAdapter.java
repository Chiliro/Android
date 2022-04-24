package com.example.booksys.android.user;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyFragmentPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments; //Fragment集合

    public MyFragmentPageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    //当前显示的是第几个
    @Override
    public Fragment getItem(int arg0) {
        return fragments.get(arg0);
    }

    //计算需要几个item
    @Override
    public int getCount() {
        return fragments.size();
    }
}
