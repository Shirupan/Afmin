package com.stone.pay.impl;

import android.app.Activity;
import android.content.Context; 


import com.stone.jo.api.ChargeFrameInterface;
import com.stone.lib.internal.ChannelCache;
import com.stone.lib.plugin.Plugin;
import com.stone.lib.plugin.PluginResultHandler;
import com.stone.lib.plugin.leisure.interfaces.OnLoginListener;

public class ChargePlugin extends Plugin implements OnLoginListener, ChargeFrameInterface {
	private static final String TAG = "ChargePlugin";
	private static ChargePlugin sPlugin; 
	private boolean chargeOnce = false;

	public static ChargePlugin getInstance() {
		if (sPlugin == null) {
			sPlugin = new ChargePlugin();
		}
		return sPlugin;
	} 
	
	@Override
	protected void onInitialize(final Context appContext) { 
	}
	
	@Override
	public void onUserLoggedIn(Activity activity) {
		ChannelCache chche = ChannelCache.getInstance(activity);
		String token = chche.getAccessToken();
		String secret = chche.getTokenSecret();
		String userid = (String) chche.get("Userid");
		String gameid = (String) chche.get("Gameid");

	}

	@Override
	public void orderCreated(String orderId) {

	}

	@Override
	public void initPay(Activity activity, final PluginResultHandler callback){

	}

	@Override
	public void onApplicationCreate(Context ctx) {

	}


}
