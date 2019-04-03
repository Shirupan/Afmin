package com.stone.jo.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.stone.single.pack.notifier.ChannelLoginListener;
import com.stone.single.pack.notifier.Notifier;
import com.stone.single.pack.notifier.PayResultListener;
import com.stone.lib.plugin.leisure.interfaces.OnLifeCycleListener;

import java.util.Map;

public abstract class ChannelApiPlugin implements ChannelApi, OnLifeCycleListener{

	/**
	 * 是否有渠道支付接口
	 * 
	 * @param activity Context
	 */
	public boolean isChannelEnable(Activity activity){
		return false;
	}


	public void callFunction(Context context, int functionType, Map<?, ?> map,
                             Notifier callback){
		
	}

}
