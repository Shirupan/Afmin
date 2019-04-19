package com.example.example.Present;

import com.example.example.Activity.MainActivity;
import com.stone.baselib.mvp.SPresentImpl;

/**
 * Stone
 * 2019/4/11
 **/
public class MainPresent extends SPresentImpl<MainActivity> {

    public void getTestStr() {
        getV().showMsg("test");
    }
}
