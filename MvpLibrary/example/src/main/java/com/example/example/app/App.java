package com.example.example.app;

import android.app.Application;
import android.content.Context;

import com.stone.baselib.net.SHttpUtils;
import com.stone.baselib.net.SNetError;
import com.stone.baselib.net.SNetProvider;
import com.stone.baselib.net.SRequestHandler;

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
public class App extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        registerProvider();
        //左滑关闭activity
        BGASwipeBackHelper.init(this, null);
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

    //注册网络提供
    private void registerProvider() {
        SHttpUtils.registerProvider(new SNetProvider() {
            @Override
            public Interceptor[] configInterceptors() {
                ArrayList<Interceptor> interceptors = new ArrayList<>();
                interceptors.add(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originRequest = chain.request();
                        return chain.proceed(originRequest);
                    }
                });

                Interceptor[] interceptorsArray = new Interceptor[interceptors.size()];
                return interceptors.toArray(interceptorsArray);
            }

            @Override
            public void configHttps(OkHttpClient.Builder builder) {

            }

            @Override
            public CookieJar configCookie() {
                return null;
            }

            @Override
            public SRequestHandler configHandler() {
                return null;
            }

            @Override
            public long configConnectTimeoutSecond() {
                return 15;
            }

            @Override
            public long configReadTimeoutSecond() {
                return 15;
            }

            @Override
            public boolean configLogEnable() {
                return true;
            }

            @Override
            public boolean handleError(SNetError error) {
                return false;
            }

            @Override
            public boolean dispatchProgressEnable() {
                return false;
            }
        });

    }
}
