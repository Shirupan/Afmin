package com.stone.baselib.net;

/**
 * Stone
 * 2019/4/4
 **/

public interface SModel {
    boolean isNull();       //无数据

    boolean isAuthError();  //验证错误

    boolean isBusinessError();   //业务错误

    String getErrorMsg();   //后台返回的错误信息
}
