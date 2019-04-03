package com.stone.lib.plugin.leisure.interfaces;

import java.util.Map;

import android.app.Activity;

import com.stone.lib.plugin.PluginResultHandler;
import com.stone.lib.plugin.interfaces.PluginInterface;

public interface InitializeInterface extends PluginInterface { 

	public void initialize(Activity activity, PluginResultHandler h);

	/**
	 * 检测更新接口
	 * 
	 * @param extras 扩展参数可以为null
	 * @param cb
	 */
	public void checkUpdate(final Map<String, Object> extras, final PluginResultHandler cb);
	
}
