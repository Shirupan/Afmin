package com.example.example.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.example.R;
import com.example.example.adpter.ViewPagerAdapter;
import com.example.example.base.BaseActivity;
import com.example.example.base.BaseFragment;
import com.example.example.fragment.HomeFragment;
import com.example.example.fragment.ThreeFragment;
import com.example.example.fragment.FileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.example.anim.SPageTransformerFadeIn;
import com.stone.baselib.utils.SLogUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

/**
 * Stone
 * 2019/4/23
 **/
@Route(path = "/viewpager/activity")
public class ViewPagerActivity extends BaseActivity {

    public static final String TAG = "ViewPagerActivity";
    @BindView(R.id.vp_main)
    ViewPager viewPager;
    @BindView(R.id.bnv_viewpager)
    BottomNavigationView bottomNavigationView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_viewpager;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initViewPager();
        initBottomNavigationView();
    }

    private void initBottomNavigationView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_one:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.menu_two:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.menu_three:
                        viewPager.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        });
    }

    private void initViewPager() {
        List<BaseFragment> list = new ArrayList<>();
        list.add(new HomeFragment());
        list.add(new FileFragment());
        list.add(new ThreeFragment());
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.s_left_in, R.anim.s_right_out);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), list));
        viewPager.setOffscreenPageLimit(list.size()-1);//设置页面缓存
        viewPager.setPageTransformer(true, new SPageTransformerFadeIn());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SLogUtils.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        SLogUtils.d(TAG, "onPause");
    }

    @Override
    public Object getPInstance() {
        return null;
    }
}
