package com.stone.lib.plugin.leisure.interfaces;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * <li>如果插件需要在生命周期的时候，做一些事情， 那么实现这个接口
 * <p>
 * <li>在plugin.xml处配置该插件实现的接口列表
 * 
 * @see {@link com.s1.lib.plugin.PluginEntry#interfaces}
 * 
 */
public interface OnLifeCycleListener {

	public void onApplicationCreate(Context context);
	
	/**
	 * 生命周期onSaveInstanceState
	 * 
	 * @param Activity
	 *            游戏的的主Activity
	 *            
	 * @param  Bundle
	 * 			保存状态         
	 */
	public void onSaveInstanceState(Activity activity, Bundle outState);
	
	/**
	 * 生命周期onNewIntent
	 * 
	 */
	public void onNewIntent(Intent intent);
	
	/**
	 * 生命周期onCreate
	 * 
	 * @param Activity
	 *            游戏的的主Activity
	 * @param  Bundle
	 * 				状态      
	 */
	public void onCreate(Activity activity, Bundle savedInstanceState);
	
	/**
	 * 生命周期onStart
	 * 
	 * @param Activity
	 *            游戏的的主Activity
	 */
	public void onStart(Activity activity);
	
	/**
	 * 生命周期onRestart
	 * 
	 * @param activity
	 *            游戏的的主Activity
	 */
	public void onRestart(Activity activity);
	
	/**
	 * 生命周期onResume
	 * 
	 * @param activity
	 *            游戏的的主Activity
	 */
	public void onResume(Activity activity);
	
	/**
	 * 生命周期onPause
	 * 
	 * @param activity
	 */
	public void onPause(Activity activity);
	
	/**
	 * 生命周期onStop
	 * 
	 * @param activity
	 *            游戏的的主Activity
	 */
	public void onStop(Activity activity);
	
	/**
	 * 生命周期onDestroy
	 * 
	 * @param activity
	 *            游戏的的主Activity
	 */
	public void onDestroy(Activity activity);
	
	/**
	 * 回调函数onActivityResult
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data);
}
