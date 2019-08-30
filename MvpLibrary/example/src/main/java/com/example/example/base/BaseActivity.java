package com.example.example.base;

import com.example.example.R;
import com.jaeger.library.StatusBarUtil;

import android.view.View;

import com.stone.baselib.mvp.SAbstractActivity;
import com.stone.baselib.mvp.SPresentImpl;
import com.stone.baselib.widget.STitleBar;

/**
 * Stone
 * 2019/4/10
 * 处理一些定制化的title，StatusBar，加载提示
 **/
public abstract class BaseActivity<P extends SPresentImpl> extends SAbstractActivity<P>{
    STitleBar titleBar;

    public void setStatusBarBg() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorMain), 0);
    }

    @Override
    public void initTopBar() {
        //要么在xml中统一设置，要么在此统一设置，单个activity修改。在此设置将覆盖xml设置。
        titleBar = findViewById(R.id.title_bar);
        if(titleBar == null){
            return;
        }
        titleBar.setTitle(R.string.title_title);//在子类activity中单独设置
        titleBar.setBg(R.color.bg_title);
        titleBar.setTextColor(R.color.text_title);
        titleBar.setLeftImg(R.mipmap.ic_back, close);
        titleBar.setLeftText(R.string.title_left, close);
        titleBar.setRightText(R.string.title_right, close);
        titleBar.setRightImg(R.mipmap.ic_close, close);
        titleBar.setLeftImgVisible(View.VISIBLE);
        titleBar.setLeftTextVisible(View.VISIBLE);
        titleBar.setTitleVisible(View.VISIBLE);
        titleBar.setRightTextVisible(View.VISIBLE);
        titleBar.setRightImgVisible(View.VISIBLE);
    }

    View.OnClickListener close= view -> finish();

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

    //是否支持滑动返回
    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }
}
