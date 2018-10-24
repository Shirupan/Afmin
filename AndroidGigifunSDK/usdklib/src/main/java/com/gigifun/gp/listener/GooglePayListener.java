package com.gigifun.gp.listener;

/**
 * Created by jerry on 2017/7/29.
 */

public interface GooglePayListener {

    //充值成功回调
    public void success(String res);
    //充值失败回调
    public void cancel(String res);
}
