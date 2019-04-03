package com.stone.channel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.stone.jo.api.ChannelApiPlugin;
import com.stone.lib.plugin.leisure.interfaces.OnLifeCycleListener;
import com.stone.lib.utils.LogUtils;
import com.stone.single.pack.notifier.ExitListener;
import com.stone.single.pack.notifier.InitListener;
import com.stone.single.pack.notifier.LoginListener;
import com.stone.single.pack.notifier.LogoutListener;
import com.stone.single.pack.notifier.PayResultListener;

import java.util.HashMap;

public class Baidu extends ChannelApiPlugin implements OnLifeCycleListener {
    private static final String TAG = "Baidu";

    @Override
    public void onApplicationCreate(Context context) {

    }

    @Override
    public void onSaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onCreate(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onStart(Activity activity) {

    }

    @Override
    public void onRestart(Activity activity) {

    }

    @Override
    public void onResume(Activity activity) {

    }

    @Override
    public void onPause(Activity activity) {

    }

    @Override
    public void onStop(Activity activity) {

    }

    @Override
    public void onDestroy(Activity activity) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void attachBaseContext(Context context) {

    }

    @Override
    public void init(Activity activity, InitListener listener) {
        LogUtils.d(TAG,"init");
    }

    @Override
    public void login(Activity activity, HashMap<String, Object> extral, LoginListener listener) {

    }

    @Override
    public void logout(Activity activity, LogoutListener listener) {

    }

    @Override
    public void pay(Activity activity, HashMap<String, Object> extral, PayResultListener listener) {

    }

    @Override
    public boolean isShowExitDialog() {
        return false;
    }

    @Override
    public boolean isFunctionSupported(int functionType) {
        return false;
    }

    @Override
    public void exit(Activity activity, ExitListener listener) {

    }
}
