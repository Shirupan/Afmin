package com.example.example.fragment;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.example.example.R;
import com.example.example.base.BaseFragment;
import com.stone.baselib.utils.SLogUtils;


/**
 * Stone
 * 2019/4/11
 **/
public class ThreeFragment extends BaseFragment {

    @Autowired
    int intMsg;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_three;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        SLogUtils.d("intMsg:"+intMsg);
    }

    @Override
    public Object getPInstance() {
        return null;
    }
}
