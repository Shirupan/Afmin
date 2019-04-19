package com.example.example.base;


import com.stone.baselib.mvp.SAbstractActivity;
import com.stone.baselib.mvp.SPresentImpl;

/**
 * Stone
 * 2019/4/10
 * 处理一些定制化的title，StatusBar，加载提示
 **/
public abstract class BaseActivity<P extends SPresentImpl> extends SAbstractActivity<P> {

    private void setStatusBarBg() {
        //TODO
    }

    @Override
    public void initTopBar() {
        //TODO
    }

    @Override
    public void showNoNetTips() {
        //TODO
    }

    public void showLoading(){
        //TODO
    }

    public void showError(){
        //TODO
    }

    public void showNoNet(){
        //TODO
    }

    public void showEmpty(){
        //TODO
    }
}
