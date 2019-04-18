package com.example.example.Activity;

import android.os.Bundle;

import com.stone.baselib.mvp.example.Present.LoginPresent;
import com.stone.baselib.mvp.example.base.BaseActivity;

/**
 * Stone
 * 2019/4/11
 **/
public class LoginActivity extends BaseActivity<LoginPresent> {

    @Override
    public int getLayoutId() {
//        R.id.activity_login
        return 0;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //登录请求
        getP().requestLogin();
    }

    @Override
    public LoginPresent getPInstance() {
        return new LoginPresent();
    }
}
