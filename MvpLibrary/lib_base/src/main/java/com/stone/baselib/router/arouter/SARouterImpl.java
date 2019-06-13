package com.stone.baselib.router.arouter;

import android.content.Context;
import android.os.Parcelable;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import com.stone.baselib.SConfig;
import com.stone.baselib.router.SRouterible;
import com.stone.baselib.utils.SLogUtils;

import java.io.Serializable;

/**
 * Stone
 * 2019/6/10
 **/
public class SARouterImpl implements SRouterible {
    private Postcard instance;

    public static int ROUTER_ENTER_ANIM;
    public static int ROUTER_EXIT_ANIM;

    public void SARouterImpl(){

    }

    @Override
    public SARouterImpl setAction(String action) {
        ROUTER_ENTER_ANIM = SConfig.ROUTER_ENTER_ANIM;
        ROUTER_EXIT_ANIM = SConfig.ROUTER_EXIT_ANIM;
        instance = ARouter.getInstance().build(action);
        return this;
    }

    @Override
    public SARouterImpl setTransition(int enterId, int exitId) {
        ROUTER_ENTER_ANIM = enterId;
        ROUTER_EXIT_ANIM = exitId;
        return this;
    }

    @Override
    public SARouterImpl putShort(String key, short msg) {
        instance.withShort(key, msg);
        return this;
    }

    @Override
    public SARouterImpl putBoolean(String key, boolean msg) {
        instance.withBoolean(key, msg);
        return this;
    }

    @Override
    public SARouterImpl putInt(String key, int msg) {
        instance.withInt(key, msg);
        return this;
    }

    @Override
    public SARouterImpl putLong(String key, long msg) {
        instance.withLong(key, msg);
        return this;
    }

    @Override
    public SARouterImpl putDouble(String key, double msg) {
        instance.withDouble(key, msg);
        return this;
    }

    @Override
    public SARouterImpl putFloat(String key, float msg) {
        instance.withFloat(key, msg);
        return this;
    }

    @Override
    public SARouterImpl putString(String key, String msg) {
        instance.withString(key, msg);
        return this;
    }

    @Override
    public SARouterImpl putObject(String key, Object msg) {
        instance.withObject(key, msg);
        return this;
    }

    public static void init(Context context) {
        ARouter.getInstance().inject(context);
    }

    public SARouterImpl putSerializable(String key, Serializable msg) {
        instance.withSerializable(key, msg);
        return this;
    }

    public SARouterImpl putParcelable(String key, Parcelable msg) {
        instance.withParcelable(key, msg);
        return this;
    }

    @Override
    public void start() {
        instance.navigation();
    }

    public void startWithAnim(Context context) {
        SLogUtils.d(ROUTER_ENTER_ANIM+"");

        if(ROUTER_ENTER_ANIM >0&& ROUTER_EXIT_ANIM >0){
            instance.withTransition(ROUTER_ENTER_ANIM, ROUTER_EXIT_ANIM);
        }
        instance.navigation(context);
    }
}
