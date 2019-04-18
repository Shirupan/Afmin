package com.stone.baselib.mvp;

import android.os.Bundle;
import android.view.View;

/**
 * Stone
 * 2019/4/10
 * Activity and Fragment
 **/
public interface SViewible<P> {

    //具体activity实现这三个方法
    /**
     * get view's layout
     */
    int getLayoutId();

    /**
     * init view,data,event
     */
    void initView(Bundle savedInstanceState);

    /**
     * new a instance
     */
    P getPInstance();



    //AbstractActivity
    /**
     * 展示顶部无网提示
     */
    void showNoNetTips();

    /**
     * if use Eventbus
     */
    boolean useEventBus();


    void initTopBar();

    void butterKnifeBind(View rootView);

    /**
     * Show toast
     *
     * @param msg Message
     */
    void showToast(String msg);

    /**
     * Show snackBar
     *
     * @param msg Message
     * @param textColor text int color
     * @param btnText btn int Message
     */
    void showSnackBar(String msg, int textColor, int btnText);

    /**
     * Show loading view
     */
    void showLoading();

    /**
     * Show loaded error
     */
    void showError();

    /**
     * Show network error
     */
    void showNoNet();

    /**
     * Show date is empty
     */
    void showEmpty();

}
