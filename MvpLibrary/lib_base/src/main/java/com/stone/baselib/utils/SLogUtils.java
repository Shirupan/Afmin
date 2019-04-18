package com.stone.baselib.utils;

import android.util.Log;

import com.stone.baselib.SConfig;

/**
 * Stone
 * 2019/4/3
 * 在非debug模式下只打印e级日志，在debug模式下打印其他日志
 **/
public class SLogUtils {
    private static boolean mIsDebug = SConfig.ISLOGDEBUG;
    private static final String TAG = SConfig.LOGTAG;

    public void init(boolean isDebug){
        mIsDebug = isDebug;
    }

    public static void d(String msg){
        d(TAG, msg);
    }

    public static void d(String tag, String msg){
        if (mIsDebug){
            Log.d(tag, msg);
        }
    }

    public static void i(String msg){
        i(TAG, msg);
    }

    public static void i(String tag, String msg){
        if (mIsDebug){
            Log.d(tag, msg);
        }
    }

    public static void w(String msg){
        w(TAG, msg);
    }

    public static void w(String tag, String msg){
        if (mIsDebug){
            Log.w(tag, msg);
        }
    }

    public static void e(String msg){
        e(TAG, msg);
    }

    public static void e(String tag, String msg){
        Log.e(tag, msg);
    }

}
