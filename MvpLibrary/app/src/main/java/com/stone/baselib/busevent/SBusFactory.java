package com.stone.baselib.busevent;

/**
 * Stone
 * 2019/4/9
 **/
public class SBusFactory {
    private static volatile SEventBusImpl instance;

    public static SEventBusImpl getBus() {
        if (instance == null) {
            synchronized (SBusFactory.class) {
                if (instance == null) {
                    instance = new SEventBusImpl();
                }
            }
        }
        return instance;
    }
}
