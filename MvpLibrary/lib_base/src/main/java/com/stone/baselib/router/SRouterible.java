package com.stone.baselib.router;

import android.app.Activity;
import android.content.Context;

/**
 * Stone
 * 2019/6/10
 **/
public interface SRouterible {

    SRouterible setAction(String action);
    SRouterible setTransition(int enterId, int exitId);
    SRouterible putShort(String key, short msg);
    SRouterible putBoolean(String key, boolean msg);
    SRouterible putInt(String key, int msg);
    SRouterible putLong(String key, long msg);
    SRouterible putDouble(String key, double msg);
    SRouterible putFloat(String key, float msg);
    SRouterible putString(String key, String msg);
    SRouterible putObject(String key, Object msg);
    void start();

}
