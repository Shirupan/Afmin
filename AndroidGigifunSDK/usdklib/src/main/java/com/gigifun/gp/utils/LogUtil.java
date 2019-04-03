/**
 * 文件名：LogUtil.java</br>
 * 描述： </br>
 * 开发人员：杜逸平 </br>
 * 创建时间： 2014-10-24
 */

package com.gigifun.gp.utils;

import android.util.Log;

/**
 * 类名: LogUtil</br> 
 * 包名：com.shenyuzhiguang.gamesdkjar.utils </br> 
 * 描述: </br>
 * 发布版本号：</br>
 * 开发人员： 杜逸平</br>
 * 创建时间： 2014-10-24 
 */

public class LogUtil {
    //	public  static final String LogVersion = "GIGIFUNLOG_V2.0";
    public static final String LogVersion = "gigifun_kong，";
    public static final String myLog = "gigifun_kong";
    private static boolean isDebug = true;//是否调试模式

    public static void k(String log) {
        if (isDebug) {
            Log.d(myLog, log);
        }
    }

    public static void i(String log) {
        if (isDebug) {
            Log.i(LogVersion, log);
        }
    }

    public static void e(String log) {
        if (isDebug) {
            Log.e(LogVersion, log);
        }
    }

    public static void v(String log) {
        if (isDebug) {
            Log.v(LogVersion, log);
        }
    }

    public static void d(String log) {
        if (isDebug) {
            Log.d(LogVersion, log);
        }
    }

    public static void w(String log) {
        if (isDebug) {
            Log.w(LogVersion, log);
        }
    }

}
