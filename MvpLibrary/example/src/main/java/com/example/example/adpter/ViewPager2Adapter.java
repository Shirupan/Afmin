package com.example.example.adpter;

import com.example.example.base.BaseFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


/**
 * Stone
 * 2019/4/22
 **/
public class ViewPager2Adapter extends FragmentStateAdapter {
    List<BaseFragment> fragments;

    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity, List<BaseFragment> fragments) {
        super(fragmentActivity);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
