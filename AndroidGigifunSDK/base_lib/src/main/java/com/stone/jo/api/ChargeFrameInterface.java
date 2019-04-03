package com.stone.jo.api;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.stone.single.pack.notifier.PayResultListener;
import com.stone.lib.plugin.PluginResultHandler;

import java.util.Map;

/**
 * 充值模块接口
 * 
 *
 */
public interface ChargeFrameInterface {

	/**
	 * 某个订单已创建
	 *
	 * @param orderId
	 *            订单号
	 */
	public void orderCreated(String orderId);

	public void initPay(Activity activity, final PluginResultHandler callback);

	public void onApplicationCreate(Context ctx);

}
