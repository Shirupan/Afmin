package com.example.example.adpter;


import com.example.example.base.BaseFragment;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;


/**
 * Stone
 * 2019/4/22
 * FragmentPagerAdapter在saveState()和restoreState()不保存Fragment状态，而FragmentStatePagerAdapter将保存Fragment状态
 **/
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    List<BaseFragment> fragments;

    public ViewPagerAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
