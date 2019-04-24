package com.example.example.fragment;

import android.os.Bundle;

import com.example.example.Present.SetPwdPresent;
import com.example.example.R;
import com.example.example.base.BaseFragment;


/**
 * Stone
 * 2019/4/11
 **/
public class ThreeFragment extends BaseFragment<SetPwdPresent> {
    @Override
    public int getLayoutId() {
        return R.layout.fragment_three;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getP().updatePwd();
    }

    @Override
    public SetPwdPresent getPInstance() {
        return new SetPwdPresent();
    }
}
