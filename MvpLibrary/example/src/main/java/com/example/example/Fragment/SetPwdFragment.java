package com.example.example.Fragment;

import android.os.Bundle;



/**
 * Stone
 * 2019/4/11
 **/
public class SetPwdFragment extends BaseFragment<SetPwdPresent> {
    @Override
    public int getLayoutId() {
        //        R.id.activity_login
        return 0;
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
