package com.stone.jo.api;

import android.app.Activity;
import android.content.Context;

import com.stone.single.pack.notifier.ExitListener;
import com.stone.single.pack.notifier.InitListener;
import com.stone.single.pack.notifier.LoginListener;
import com.stone.single.pack.notifier.LogoutListener;
import com.stone.single.pack.notifier.Notifier;
import com.stone.single.pack.notifier.PayResultListener;

import java.util.HashMap;
import java.util.Map;

public interface ChannelApi {
	
	/**
	 * on Application Create
	 * 
	 * @param context
	 */
	public void onApplicationCreate(Context context);
	
	/**
	 * attach Base Context
	 * 
	 * @param context
	 */
	public void attachBaseContext(Context context);
	
	/**
	 * 初始化
	 * 
	 * @param activity Activity
	 * @param listener 初始化监听
	 */
	public void init(Activity activity, InitListener listener);
	

	/**
	 * 登录
	 * 
	 * @param activity Context
	 * @param extral 登录参数
	 * @param listener 登录监听
	 */
	public void login(Activity activity, HashMap<String, Object> extral, LoginListener listener);
	
	/**
	 * logout
	 * 
	 * @param activity Activity
	 * @param listener logout listener
	 */
	public void logout(Activity activity, LogoutListener listener);
	

	/**
	 * 支付接口
	 * @param extral  支付参数
	 * @param listener 支付结果监听
	 */
	public void pay(Activity activity, HashMap<String, Object> extral, PayResultListener listener);

	public boolean isShowExitDialog();

	/**
	 * Is Function Supported
	 *
	 * @param functionType
	 * @return
	 */
	public boolean isFunctionSupported(int functionType);

	public void callFunction(Context context, int functionType, Map<?, ?> map,
                             Notifier callback);

	/**
	 * exit
	 *
	 * @param activity Context
	 * @param listener exit listener
	 */
	public void exit(Activity activity, ExitListener listener);

	public boolean isChannelEnable(Activity activity);

}
