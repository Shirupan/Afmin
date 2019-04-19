package com.example.example.http;

import com.stone.baselib.net.SHttpUtils;

/**
 * Stone
 * 2019/4/4
 **/
public class SHttpFactory {
    public static final String URL = "http://www.stone.com";

    private static volatile SService service;
    private static SService merchantdetailService;

    public static SService getService() {
        if (service == null) {
            synchronized (SHttpFactory.class) {
                if (service == null) {
                    service = SHttpUtils.getInstance().getRetrofit(URL, true).create(SService.class);
                }
            }
        }
        return service;
    }

    //fengwan附近商家信息
    public static SService getMerchantdetailService() {
        if (merchantdetailService == null) {
            synchronized (SHttpFactory.class) {
                if (merchantdetailService == null) {
                    merchantdetailService = SHttpUtils.getInstance().getRetrofit("https://beta.playtoken.com/PACpay/", true).create(SService.class);
                }
            }
        }
        return merchantdetailService;
    }
}
