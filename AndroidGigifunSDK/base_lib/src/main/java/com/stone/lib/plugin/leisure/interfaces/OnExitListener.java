package com.stone.lib.plugin.leisure.interfaces;

import android.app.Activity;

import com.stone.lib.plugin.PluginResultHandler;

public interface OnExitListener {
	
	/**
	 * 如果该渠道需要接入渠道退出规范，则需要实现渠道退出逻辑并返回true，否则返回false。
	 * 
	 * @param activity
	 * @param handler
	 * @return
	 */
	public boolean onExit(final Activity activity, final PluginResultHandler handler);
}
