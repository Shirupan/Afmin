package com.stone.lib.plugin.leisure.interfaces;

import android.app.Activity;


/**
 * <li>如果插件需要在登录完成之后做点事情，那么实现这个接口
 * <p>
 * <li>在plugin.xml处配置该插件实现的接口列表
 * 
 * @see {@link com.s1.lib.plugin.PluginEntry#interfaces}
 * 
 */
public interface OnLoginListener {

	/**
	 * 此方法将被调用在Non-UI thread
	 * 
	 * @param ui
	 */
	public void onUserLoggedIn(Activity ui);
	
}
