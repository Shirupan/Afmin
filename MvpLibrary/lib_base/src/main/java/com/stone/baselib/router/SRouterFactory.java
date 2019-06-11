package com.stone.baselib.router;

/**
 * Stone
 * 2019/6/11
 **/
public class SRouterFactory {
    private static volatile SARouterImpl instance;

    private SRouterFactory() {

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
