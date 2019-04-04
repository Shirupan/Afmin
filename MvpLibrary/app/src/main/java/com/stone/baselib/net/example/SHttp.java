package com.stone.baselib.net.example;

import com.stone.baselib.net.SHttpUtils;

/**
 * Stone
 * 2019/4/4
 **/
public class SHttp {
    public static final String URL = "http://www.stone.com";

    private static SService service;

    public static SService getService() {
        if (service == null) {
            synchronized (SHttp.class) {
                if (service == null) {
                    service = SHttpUtils.getInstance().getRetrofit(URL, true).create(SService.class);
                }
            }
        }
        return service;
    }
}
