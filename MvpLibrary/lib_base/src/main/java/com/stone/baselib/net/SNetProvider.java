package com.stone.baselib.net;

import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Stone
 * 2019/4/3
 * 可设置超时时间，拦截器等功能
 **/
public interface SNetProvider {
    Interceptor[] configInterceptors();

    void configHttps(OkHttpClient.Builder builder);

    CookieJar configCookie();

    SRequestHandler configHandler();

    long configConnectTimeoutSecond();

    long configReadTimeoutSecond();

    boolean configLogEnable();

    boolean handleError(SNetError error);

    boolean dispatchProgressEnable();
}
