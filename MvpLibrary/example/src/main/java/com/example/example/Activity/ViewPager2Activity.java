package com.example.example.activity;

import android.os.Bundle;

import com.example.example.adpter.ViewPager2Adapter;
import com.example.example.fragment.HomeFragment;
import com.example.example.fragment.ThreeFragment;
import com.example.example.fragment.TwoFragment;
import com.example.example.R;
import com.example.example.base.BaseActivity;
import com.example.example.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

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
        list.add(new HomeFragment());
        list.add(new TwoFragment());
        list.add(new ThreeFragment());
        viewPager.setAdapter(new ViewPager2Adapter(this , list));
    }

    @Override
    public Object getPInstance() {
        return null;
    }
}
