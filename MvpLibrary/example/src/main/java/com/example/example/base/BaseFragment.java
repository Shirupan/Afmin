package com.example.example.base;


import com.jaeger.library.StatusBarUtil;
import com.stone.baselib.mvp.SAbstractFragment;
import com.stone.baselib.mvp.SPresentImpl;

/**
 * Stone
 * 2019/4/11
 * 处理一些定制化的加载提示
 **/
public abstract class BaseFragment<P extends SPresentImpl> extends SAbstractFragment<P> {

    @Override
    public void setStatusBarBg() {

    }

    @Override
    public void showLoading() {
//TODO
    }

    @Override
    public void showError() {
//TODO
    }

    @Override
    public void showNoNet() {
//TODO
    }

    @Override
    public void showEmpty() {
//TODO
    }
}
