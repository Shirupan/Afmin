package com.stone.basic.impl;

import android.app.Activity;
import android.content.Context;

import com.stone.lib.plugin.Plugin;
import com.stone.lib.plugin.PluginManager;
import com.stone.lib.plugin.PluginResultHandler;
import com.stone.lib.plugin.interfaces.PluginInterface;
import com.stone.lib.plugin.leisure.interfaces.InitializeInterface;
import com.stone.lib.plugin.leisure.interfaces.OnAppInitListener;

import java.util.ArrayList;
import java.util.Map;

public class BasicPlugin extends Plugin implements InitializeInterface {
    private static final String TAG = "BasicPlugin";

    @Override
    protected void onInitialize(Context appContext) {

    }

    @Override
    public void initialize(Activity activity, PluginResultHandler h) {
        final PluginManager pm = PluginManager.getDefault(null);
        ArrayList<PluginInterface> initInterfaces = pm.findAllPlugins(OnAppInitListener.class);

        for (final PluginInterface initInterface : initInterfaces) {
            if (initInterface instanceof OnAppInitListener) {
                ((OnAppInitListener) initInterface).onAppInit(activity);
            }
        }
    }

    @Override
    public void checkUpdate(Map<String, Object> extras, PluginResultHandler cb) {

    }
}
