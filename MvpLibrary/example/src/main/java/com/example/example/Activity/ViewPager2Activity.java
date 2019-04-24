package com.example.example.Activity;

import android.os.Bundle;

import com.example.example.Adpter.ViewPager2Adapter;
import com.example.example.Adpter.ViewPagerAdapter;
import com.example.example.Fragment.OneFragment;
import com.example.example.Fragment.ThreeFragment;
import com.example.example.Fragment.TwoFragment;
import com.example.example.R;
import com.example.example.base.BaseActivity;
import com.example.example.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;
import butterknife.BindView;

/**
 * Stone
 * 2019/4/23
 **/
public class ViewPager2Activity extends BaseActivity {
    @BindView(R.id.vp2_main)
    ViewPager2 viewPager;
    @Override
    public int getLayoutId() {
        return R.layout.activity_viewpager2;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initViewPager();
    }

    private void initViewPager() {
        List<BaseFragment> list = new ArrayList<>();
        list.add(new OneFragment());
        list.add(new TwoFragment());
        list.add(new ThreeFragment());
        viewPager.setAdapter(new ViewPager2Adapter(this , list));
    }

    @Override
    public Object getPInstance() {
        return null;
    }
}
