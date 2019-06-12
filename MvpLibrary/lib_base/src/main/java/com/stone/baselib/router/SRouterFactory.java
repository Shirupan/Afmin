package com.stone.baselib.router;

import android.content.Context;

import com.stone.baselib.router.arouter.SARouterImpl;

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
