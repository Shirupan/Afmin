package com.stone.baselib.router;

import android.app.Activity;
import android.content.Context;

/**
 * Stone
 * 2019/6/11
 **/
public class SRouterFactory {
    private static volatile SARouterImpl instance;

    private SRouterFactory() {

    }

    public static void init(Context context){
        SARouterImpl.init(context);
    }

    public static SARouterImpl getARouter() {
        if (instance == null) {
            synchronized (SRouterFactory.class) {
                if (instance == null) {
                    instance = new SARouterImpl();
                }
            }
        }
        return instance;
    }
}
