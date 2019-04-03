package com.stone.lib.plugin;

import android.content.Context;

import com.stone.lib.utils.LogUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class PluginEntry {

	private static final String TAG = "PluginEntry";

	/**
	 * 插件的唯一标识
	 */
	public String label;

	/**
	 * 插件的版本
	 */
	public String version;

	/**
	 * 插件的描述
	 */
	public String description;

	/**
	 * 插件是否必须，用于做插件存在与否的预先判断
	 */
	public boolean required;

	/**
	 * 插件是否处于可用状态，默认true
	 */
	public boolean enabled = true;

	/**
	 * 插件的入口类
	 */
	public String mainClass;
	
	/**
	 * 插件实现的接口列表
	 */
	public ArrayList<String> interfaces;

	/**
	 * 实例化该插件，先找getInstance静态方法，找不到的情况下找默认的构造函数
	 * 
	 * @return 返回插件实例，若为空，则抛出异常
	 */
	public Plugin createPlugin() {
		Plugin p = null;
		Class<?> glass = null;
		try {
			glass = Class.forName(mainClass);
			Method m = glass
					.getDeclaredMethod("getInstance", new Class<?>[] {});
			m.setAccessible(true);
			p = (Plugin) m.invoke(null, new Object[] {});
		} catch (ClassNotFoundException e) {
			LogUtils.e(TAG, "class [" + mainClass + "] not found");
		} catch (NoSuchMethodException e1) {
			try {
				p = (Plugin) glass.newInstance();
			} catch (Exception ex) {
				LogUtils.e(TAG, "could not init plugin entry[" + label + "]");
				LogUtils.e(TAG, "error in default constructor: " + ex.getMessage());
			}
		} catch (Exception e2) {
			LogUtils.e(TAG, "could not init plugin entry[" + label + "]");
			LogUtils.e(TAG, "error in getInstance(): " + e2.getMessage());
		}
		if (p == null) {
			LogUtils.e(TAG, "plugin [" + label
					+ "] can not be inited, see error log above");
		}
		if(p != null){
			p.entry = this;
		}
		return p;
	}

	/**
	 * 插件的模拟Service组件类
	 */
	public String serviceClass;

	PluginService createService(Context context) {

		if (serviceClass == null || "".equals(serviceClass)) {
			return null;
		}

		PluginService p = null;
		Class<?> glass = null;
		try {
			glass = Class.forName(serviceClass);
			Method m = glass.getDeclaredMethod("getInstance",
					new Class<?>[] { Context.class });
			m.setAccessible(true);
			p = (PluginService) m.invoke(null, new Object[] { context });
		} catch (ClassNotFoundException e) {
			LogUtils.e(TAG, "serviceClass [" + serviceClass + "] not found");
		} catch (NoSuchMethodException e1) {
			try {
				Constructor<?> constructor = glass
						.getDeclaredConstructor(new Class[] { Context.class });
				constructor.setAccessible(true);
				p = (PluginService) constructor.newInstance(context);
			} catch (Exception ex) {
				LogUtils.e(TAG, "could not init plugin entry[" + label + "]");
				LogUtils.e(TAG, "error in default constructor: " + ex.getMessage());
			}
		} catch (Exception e2) {
			LogUtils.e(TAG, "could not init plugin entry[" + label + "]");
			LogUtils.e(TAG, "error in getInstance(): " + e2.getMessage());
		}

		return p;

	}

	@Override
	public String toString() {
		return "[" + label + "=" + mainClass + "]";
	}
}
