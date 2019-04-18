package com.stone.baselib.net.example;

import com.stone.baselib.net.SModel;

/**
 * Stone
 * 2019/4/4
 **/
public class BaseModel implements SModel {
    //TODO 根据返回数据字段赋值
    protected boolean error;
    public int code;
    public String errmsg;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public boolean isAuthError() {
        //TODO 根据code判断返回,在SHttpUtils.getApiTransformer()中处理
        return false;
    }

    @Override
    public boolean isBusinessError() {
        //TODO 根据code判断返回,在SHttpUtils.getApiTransformer()中处理
        return false;
    }

    @Override
    public String getErrorMsg() {
        return errmsg;
    }

}
