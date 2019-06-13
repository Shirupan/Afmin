package com.example.example.app;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.stone.baselib.App.SApp;
import com.stone.baselib.SConfig;
import com.stone.baselib.net.SHttpUtils;
import com.stone.baselib.net.SNetError;
import com.stone.baselib.net.SNetProvider;
import com.stone.baselib.net.SRequestHandler;
import com.stone.baselib.utils.SLogUtils;

import java.io.IOException;
import java.util.ArrayList;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Stone
 * 2019/4/19
 **/
public class App extends SApp {

    @Override
    public void onCreate() {
        super.onCreate();
        Constants.init();
        SLogUtils.init();
    }

    @Override
    protected SNetProvider getSNetProvider() {
        return super.getSNetProvider();
    }

    @Override
    protected boolean isDebug() {
        return true;
    }
}
