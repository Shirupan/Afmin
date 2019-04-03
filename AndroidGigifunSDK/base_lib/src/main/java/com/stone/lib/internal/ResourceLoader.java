package com.stone.lib.internal;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceLoader {

	private static ResourceLoader sLoader;

	private static final String TAG = "ResourceLoader";

	private static final byte[] SYN = new byte[0];

	public static ResourceLoader getDefault(Context cxt) {
		if (sLoader == null) {
			synchronized (SYN) {
				if (sLoader == null) {
					sLoader = new ResourceLoader(cxt.getApplicationContext());
				}
			}
		}
		return sLoader;
	}

	private Context mAppContext;

	/**
	 * _config.txt
	 */
	private HashMap<String, String> mConfigs = new HashMap<String, String>();

	private ConcurrentHashMap<String, String> mFileStrings = new ConcurrentHashMap<String, String>();

	private ConcurrentHashMap<String, ConcurrentHashMap<String, String>> mPluginsConfigs = new ConcurrentHashMap<String, ConcurrentHashMap<String, String>>();

	private ResourceLoader(Context appContext) {
		mAppContext = appContext;
		initChannelConfig();
	}

//	用于加解密
	private static StringBuilder sKey = new StringBuilder();
	static{
		sKey.append("a");
		sKey.append("stone");
		sKey.delete(2, 3);
		sKey.append("b");
		sKey.append((int)Math.pow(3, 3)-7);
		sKey.append((int)Math.cbrt(1));
		sKey.append((int)Math.sqrt(64));
		sKey.append("0101");
	}

	// 初始化渠道参数配置文件
	private void initChannelConfig() {
		String fileName = "channel_config.txt";
		String configs = readFile(fileName);
		if (!configs.equals("")) {
			try {
				JSONObject jo = new JSONObject(configs);
				Iterator<?> keys = jo.keys();
				HashMap<String, String> gs = mConfigs;
				while (keys.hasNext()) {
					String key = keys.next().toString();
					gs.put(key, jo.getString(key));
				}
			} catch (JSONException e2) {
//						e2.printStackTrace();
			}
		}
	}

	/**
	 * Never returns null, "" is returned instead.
	 */
	public String getConfig(String key) {
		String config = mConfigs.get(key);
		return config == null ? "" : config;
	}

	public void loadPluginConfig(String fileName) {
		String configs = readFile(fileName);
		if (!configs.equals("")) {
			// File ad/ad_config.txt exists in assets directory
			ConcurrentHashMap<String, String> pluginConfigs = null;
			try {
				JSONObject jo = new JSONObject(configs);
				Iterator<?> keys = jo.keys();
				while (keys.hasNext()) {
					if (pluginConfigs == null) {
						pluginConfigs = new ConcurrentHashMap<String, String>();
					}
					String key = keys.next().toString();
					pluginConfigs.put(key, jo.getString(key));
				}
				if (pluginConfigs != null) {
					mPluginsConfigs.put(fileName, pluginConfigs);
				}
			} catch (JSONException e) {
			}
		}
	}

	public String getPluginConfig(String fileName, String key) {
		ConcurrentHashMap<String, String> pluginConfigs = mPluginsConfigs.get(fileName);
		if (pluginConfigs == null) {
			return "";
		}
		String config = pluginConfigs.get(key);
		return config != null ? config : "";
	}

	public synchronized String readFile(String fileName) {
		if (TextUtils.isEmpty(fileName)) {
			return "";
		}

		String content = mFileStrings.get(fileName);
		if (content != null) {
			return content;
		}

		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			is = mAppContext.getAssets().open(fileName);
			byte[] buffer = new byte[1024];
			int readBytes = is.read(buffer);
			baos = new ByteArrayOutputStream(1024);
			while (0 < readBytes) {
				baos.write(buffer, 0, readBytes);
				readBytes = is.read(buffer);
			}
			String s = baos.toString();
			mFileStrings.put(fileName, s);
			return s;
		} catch (IOException e) {
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
			if (null != baos) {
				try {
					baos.close();
				} catch (IOException e) {
				}
			}
		}

		mFileStrings.put(fileName, "");
		return "";
	}

}
