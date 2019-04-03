package com.stone.jo.config;

import android.content.Context;
import android.text.TextUtils;

import com.stone.lib.utils.Des;
import com.stone.lib.utils.LogUtils;
import com.stone.lib.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JoConfig {
    private static final String TAG = "JoConfig";
    private static Map<String, Object> mData = new HashMap<String, Object>();
    private static StringBuilder sKey = new StringBuilder();

    static {
        sKey.append("a");
        sKey.append("stone");
        sKey.delete(2, 3);
        sKey.append("b");
        sKey.append((int) Math.pow(3, 3) - 7);
        sKey.append((int) Math.cbrt(1));
        sKey.append((int) Math.sqrt(64));
        sKey.append("0101");
    }

    public static void loadConfig(Context context) {
        if (null != mData
                && !mData.isEmpty()) {
            return;
        }

        String configs = "";
        try {
            configs = Utils.readSimplelyFile(context.getAssets().open("channel/channel_name.txt"));
            if (TextUtils.isEmpty(configs)) {
                return;
            }
            JSONArray data = new JSONArray(configs);
            StringBuffer outputStr = new StringBuffer();
            int len = data.length();
            for (int i = 0; i < len; i++) {
                String inputStr = data.getString(i);
                outputStr.append(Des.decode(sKey.toString(), inputStr));
            }
            String decryptStr = URLDecoder.decode(outputStr.toString(),
                    "utf-8");
            LogUtils.i(TAG, "channel_name.txt content:"+ decryptStr);
            JSONObject jo = new JSONObject(decryptStr);
            Iterator<?> keys = jo.keys();
            Map<String, Object> gs = mData;
            while (keys.hasNext()) {
                String key = keys.next().toString();
                gs.put(key, jo.getString(key));
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            try {
                JSONObject jo = new JSONObject(configs);
                Iterator<?> keys = jo.keys();
                Map<String, Object> gs = mData;
                while (keys.hasNext()) {
                    String key = keys.next().toString();
                    gs.put(key, jo.getString(key));
                }
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
    }

    public static Object get(String key) {
        if (null == mData) {
            return null;
        }
        return mData.get(key);
    }

    public static String getString(String key) {
        if (null == mData) {
            return null;
        }
        return String.valueOf(mData.get(key));
    }
}

