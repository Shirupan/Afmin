package com.gigifun.gp.utils;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;

public class UhttpUtil {

    public static void post(String url, final UcallBack onCallBack) {
        OkHttpUtils
                .post()
                .url(url)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response, int arg1) {
                        // TODO Auto-generated method stub
                        onCallBack.onResponse(response, arg1);
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1, int arg2) {
                        // TODO Auto-generated method stub
                        onCallBack.onError(arg0, arg1, arg2);
                    }
                });
    }


    public static void post(String url, Map<String, String> parmMap, final UcallBack onCallBack) {
        LogUtil.k(("UhttpUtil-->post" + url));
        Iterator it = parmMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            String key = entry.getKey();
            String value = entry.getValue();
            LogUtil.k("key=" + key);
            LogUtil.k("value=" + value);
        }
        OkHttpUtils
                .post()
                .params(parmMap)
                .url(url)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response, int arg1) {

                        // TODO Auto-generated method stub
                        LogUtil.k("HttpLog" + "response:" + response);
                        onCallBack.onResponse(response, arg1);

                    }

                    @Override
                    public void onError(Call arg0, Exception arg1, int arg2) {
                        // TODO Auto-generated method stub
                        onCallBack.onError(arg0, arg1, arg2);
                    }
                });
    }


    public static void get(String url, HashMap<String, String> parmMap, final UcallBack onCallBack) {
        OkHttpUtils
                .get()
                .params(parmMap)
                .url(url)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response, int arg1) {
                        // TODO Auto-generated method stub
                        onCallBack.onResponse(response, arg1);
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1, int arg2) {
                        // TODO Auto-generated method stub
                        onCallBack.onError(arg0, arg1, arg2);
                    }
                });
    }


    public static void get(String url, final UcallBack onCallBack) {
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response, int arg1) {
                        // TODO Auto-generated method stub
                        onCallBack.onResponse(response, arg1);
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1, int arg2) {
                        // TODO Auto-generated method stub
                        onCallBack.onError(arg0, arg1, arg2);
                    }
                });
    }


}
