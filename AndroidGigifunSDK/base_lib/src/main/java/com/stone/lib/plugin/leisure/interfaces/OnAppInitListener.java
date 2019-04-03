package com.stone.lib.plugin.leisure.interfaces;

import android.app.Activity;

/**
 * <li>如果插件需要在应用程序打开的时候，做一些事情， 那么实现这个接口
 * <p>
 * <li>在plugins.xml处配置该插件实现的接口列表
 *
 */
public interface OnAppInitListener {

	/**
	 * 此方法将在UI线程被调用
	 * 
	 * @param activity
	 */
	public void onAppInit(Activity activity);
}
