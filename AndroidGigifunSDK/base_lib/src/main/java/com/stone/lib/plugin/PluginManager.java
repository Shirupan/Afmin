package com.stone.lib.plugin;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.stone.lib.plugin.interfaces.PluginInterface;
import com.stone.lib.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class PluginManager {

	private HashMap<String, PluginEntry> mPlugins = new HashMap<String, PluginEntry>();

	private static final String TAG = "PluginManager";

	private static PluginManager sManager;

	private boolean mHasLoaded;

	private boolean mLoadSuccess;

	private Context mAppContext;

	private static final byte[] SYNC = new byte[0];

	public static PluginManager getDefault(Context cxt) {
		if (sManager == null) {
			synchronized (SYNC) {
				if (sManager == null) {
					sManager = new PluginManager(cxt.getApplicationContext());
				}
			}
		}
		return sManager;
	}

	private PluginManager(Context cxt) {
		mAppContext = cxt;
	}

	private void setPlugins(PluginProvider provider) {
		ArrayList<PluginEntry> list = provider.providePlugins();
		if (list == null) {
			list = new ArrayList<PluginEntry>();
		}
		int len = list.size();
		for (int i = 0; i < len; i++) {
			PluginEntry entry = list.get(i);
			mPlugins.put(entry.label, entry);
		}
	}

	/**
	 * 加载所有的插件，插件由PluginProvider提供
	 */
	public synchronized void loadAllPlugins() {
		if (mHasLoaded) {
			return ;
		}
		// 改用其他的PluginProvider
		setPlugins(new AssetsPluginProvider(mAppContext));

		StringBuilder sb = new StringBuilder();
		sb.append("supported plugins:\n[\n");
		/*--- 根据每个plugin的init属性，决定是否初始化plugin ---*/
		HashMap<String, PluginEntry> entries = mPlugins;
		Set<String> set = entries.keySet();
		for (String key : set) {
			PluginEntry entry = entries.get(key);
			try {
				Class.forName(entry.mainClass);
				sb.append(entry.description);
				sb.append(" ");
				sb.append(entry.version);
				sb.append("\n");
			} catch (Exception e) {
				if (entry.required) {
					LogUtils.e(TAG, "plugin [" + entry.label
							+ "] required=true, but jar is not found in SDK");
				}
			}
		}
		sb.append("]");
		mLoadSuccess = true;
		mHasLoaded = true;
		/*-------------------- end -----------------*/
	}

	/**
	 * 根据类名返回相应的PluginEntry
	 */
	public PluginEntry findEntryByClassName(String className) {

		if (TextUtils.isEmpty(className))
			return null;
		HashMap<String, PluginEntry> entries = mPlugins;
		for (PluginEntry pe : entries.values()) {
			if (pe.mainClass.equals(className)) {
				return pe;
			}
		}
		return null;
	}

	/**
	 * 返回plugin基本属性
	 *
	 * @param label
	 * @return
	 */
	public PluginEntry findEntry(String label) {
		HashMap<String, PluginEntry> entries = mPlugins;
		PluginEntry entry = entries.get(label);
		if (entry == null) {
			throw new RuntimeException("label [" + label
					+ "] does not exists in plugin.xml");
		}
		return entry;
	}

	/**
	 * Get the plugin instance specified by the label
	 *
	 * @throws RuntimeException
	 *             if the plugin label can not be found, or a instance can not
	 *             be created.
	 */
	public PluginInterface findPlugin(String label) {
		HashMap<String, PluginEntry> entries = mPlugins;
		PluginEntry entry = entries.get(label);
		if (entry == null) {
			throw new RuntimeException("label [" + label
					+ "] does not exists in plugins.xml");
		}
		Plugin p = entry.createPlugin();
		if(p != null){
			p.init(mAppContext);
		}
		return p;
	}

	/**
	 * 查找某插件，获取它的实例，但是不执行其{@link Plugin#init(Context)}方法。谨慎调用此方法。
	 */
	public PluginInterface findPluginWithoutInit(String label) {
		if(mPlugins.isEmpty()){
			// 改用其他的PluginProvider
			setPlugins(new AssetsPluginProvider(mAppContext));
		}

		HashMap<String, PluginEntry> entries = mPlugins;
		PluginEntry entry = entries.get(label);
		if (entry == null) {
			throw new RuntimeException("label [" + label
					+ "] does not exists in plugin.xml");
		}
		Plugin p = entry.createPlugin();
		return p;
	}

	/**
	 * 查找实现了指定接口名称的所有插件
	 *
	 */
	public ArrayList<PluginInterface> findAllPlugins(Class<?> glass) {
		ArrayList<PluginInterface> list = new ArrayList<PluginInterface>();
		HashMap<String, PluginEntry> entries = mPlugins;
		Set<String> labels = entries.keySet();
		for (String label : labels) {
			PluginEntry entry = entries.get(label);
			ArrayList<String> interfaces = entry.interfaces;
			if (interfaces != null
					&& interfaces.contains(glass.getSimpleName())) {
				Plugin p = null;
				try {
					p = entry.createPlugin();
				} catch (Exception e) {
				}
				if (p != null) {
					p.init(mAppContext);
					list.add(p);
				}
			}
		}
		return list;
	}

	/**
	 * 判断插件是否存在并可用
	 *
	 */
	public boolean existsPlugin(String label) {
		HashMap<String, PluginEntry> entries = mPlugins;
		PluginEntry entry = entries.get(label);
		if (entry != null && entry.enabled) {
			try {
				if(entry.createPlugin() != null)
					return true;
			} catch (Exception e) {
			}
		}
		return false;
	}

}
