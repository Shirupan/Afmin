package com.stone.lib.plugin;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/**
 * if you want to put some mode of your plugin running in a service ,please
 * extends this class and ,do what you do in a service 
 * 
 * it's a proxy service
 * 
 */
public class PluginService{

	protected Context mCtx;
	
	public String serviceName="";
	
	public int version_code=-1;

	protected PluginService(Context context) {
		mCtx = context;
	}

	public void onCreate() {
	}

	public void onStart(Intent intent, int startId) {
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		return Service.START_STICKY_COMPATIBILITY;
	}

	public IBinder onBind(Intent intent) {
		return null;
	}

	public void onDestroy() {
	}

	public Context getApplicationContext(){
		return mCtx;
	}
}
