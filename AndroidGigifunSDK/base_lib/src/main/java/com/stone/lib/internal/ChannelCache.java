package com.stone.lib.internal;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;

import com.stone.single.pack.notifier.BasicInitCallBack;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

public class ChannelCache {

    public static final String CHANNEL_PARAMS = "channel_params";
	public static final String KEY_CONSUMER_KEY = "consumer_key";

	public static final String KEY_CONSUMER_SECRET = "consumer_secret";

	public static final String KEY_ACCESS_TOKEN = "access_token";

	public static final String KEY_TOKEN_SECRET = "token_secret";

	public static final String KEY_USER_AGENT = "user_agent";

	public static final String KEY_USER_PROPERTY = "user_property";

	public static final String KEY_TOKEN_READY = "is_token_ready";

	private static final String KEY_OLD_UDID = "udid";
	private static final String KEY_NEW_UDID = "nudid";

	public static final String KEY_PLATFORM = "platform";
	public static final String KEY_OPENID = "open_id";
	public static final String PLATFORM_QQ = "qq";
	public static final String PLATFORM_WEIXIN = "weixin";
	public static final String PLATFORM_SINA = "sina";

	private static final String TAG = "ChannelCache";

	private static volatile ChannelCache sCache;

	private Context mAppContext;

	private ReentrantLock mLock = new ReentrantLock();

	private HashMap<String, Object> mValues = new HashMap<String, Object>();

	//初始化参数集合值
	private HashMap<String, Object> params = null;

	private String mConsumerKey;

	private String mConsumerSecret;

	private String mAccessToken;

	private String mTokenSecret;

	private String mUserAgent = null;

	private boolean mTokenReady;

	public static final String KEY_PAY_MATE = "p_meta";

	private ChannelCache(Context appContext) {
		mAppContext = appContext;
		Channel.init(appContext);
	}

//	public Context getApplicationContext() {
//		return mAppContext;
//	}

	public static ChannelCache getInstance(Context cxt) {
		if (sCache == null) {
			synchronized (ChannelCache.class) {
				if (sCache == null) {
					sCache = new ChannelCache(cxt.getApplicationContext());
				}
			}
		}
		return sCache;
	}

	public void put(String key, Object value) {
		mLock.lock();
		mValues.put(key, value);
		mLock.unlock();
	}

	public Object get(String key) {
		return mValues.get(key);
	}

	public void commit() {
		mLock.lock();
		mConsumerKey = (String) mValues.get(KEY_CONSUMER_KEY);
		mConsumerSecret = (String) mValues.get(KEY_CONSUMER_SECRET);
		mAccessToken = (String) mValues.get(KEY_ACCESS_TOKEN);
		mTokenSecret = (String) mValues.get(KEY_TOKEN_SECRET);
		Boolean ready = (Boolean) mValues.get(KEY_TOKEN_READY);
		mTokenReady = ready != null ? ready.booleanValue() : false;
		mUserAgent = (String) mValues.get(KEY_USER_AGENT);
		params = (HashMap)mValues.get(CHANNEL_PARAMS);
		mLock.unlock();
	}

	public void initUserAgent(final Context cxt,final BasicInitCallBack basic) {

	}

	public String getConsumerKey() {
		mLock.lock();
		String key = mConsumerKey;
		mLock.unlock();
		return key;
	}

	public String getChannelId(){
		return "TEST0000000000";
	}

	public String getConsumerSecret() {
		mLock.lock();
		String secret = mConsumerSecret;
		mLock.unlock();
		return secret;
	}

	public String getAccessToken() {
		mLock.lock();
		String token = mAccessToken;
		mLock.unlock();
		return token;
	}

	public String getTokenSecret() {
		mLock.lock();
		String secret = mTokenSecret;
		mLock.unlock();
		return secret;
	}

	public boolean isTokenReady() {
		mLock.lock();
		boolean ready = mTokenReady;
		mLock.unlock();
		return ready;
	}

	public String getUserAgent() {
		mLock.lock();
		String userAgent = mUserAgent;
		mLock.unlock();
		return userAgent;
	}

	public String getOldUDID() {
		return (String) get(KEY_OLD_UDID);
	}

	public String getNewUDID() {
		return (String) get(KEY_NEW_UDID);
	}

	public static final Uri CONTENT_URI1 = Uri
			.parse("content://cn.iplaychess.ly.ch/channel_id");

	private static String getQiPaiChannelId(Context context) {

		Cursor cursor = context.getContentResolver().query(CONTENT_URI1, null,
				null, null, null);
		String channel = null;
		if (cursor != null && cursor.moveToNext()) {
			channel = cursor.getString(cursor
					.getColumnIndexOrThrow("channel_id"));

		}
		if (cursor != null) {
			cursor.close();
		}
		return channel;
	}

	public String getConfig(String key) {
		return ResourceLoader.getDefault(mAppContext).getConfig(key);
	}

	public String getSdkVersion() {
		return ResourceLoader.getDefault(mAppContext).getConfig("sdk_version");
	}

	private WeakReference<Activity> mCurrActivityRef;
	private LinkedList<WeakReference<Activity>> mActivities;

	/**
	 * 设置top activity
	 * 
	 * @param activity
	 */
	public void setCurrentActivity(Activity activity) {
		if (activity == null) {
			throw new NullPointerException(
					"null passed to setCurrentActivity(Activity)");
		}
		// ----------------保存当前Activity实例---------------//
		if (mCurrActivityRef != null) {
			mCurrActivityRef.clear();
		}
		mCurrActivityRef = new WeakReference<Activity>(activity);
		// ----------------end ----------------//

		// -----------------保存所有开发者设置的Activity--------------------//
		// 用于在退出时，所有activity都退出
		if (mActivities == null) {
			mActivities = new LinkedList<WeakReference<Activity>>();
		}
		mActivities.add(new WeakReference<Activity>(activity));
		// -----------------------end---------------------------//

	}

	public void onPause(Activity activity) {

	}

	/**
	 * 获取当前top activity, 依赖于开发者调用，可能返回空
	 * 
	 * @return
	 */
	public Activity getCurrentActivity() {
		return mCurrActivityRef != null ? mCurrActivityRef.get() : null;
	}

	private static final long DEFAULT_EXIT_DELAY = 1500;

	// -----------------------退出Application-----------------------//
	/**
	 * 退出程序
	 * 
	 * @param delayMs
	 *            杀死进程的延迟，单位为毫秒，最短时间间隔1500ms
	 */
	public void destroyActivitiesAndExit(long delayMs) {
		if (mActivities != null) {
			int len = mActivities.size();
			for (int i = len - 1; i >= 0; i--) {
				WeakReference<Activity> ref = mActivities.get(i);
				Activity activity = ref.get();
				if (activity != null) {
					activity.finish();
				}
			}
			mActivities.clear();
		}
		if (delayMs <= DEFAULT_EXIT_DELAY) {
			delayMs = DEFAULT_EXIT_DELAY;
		}

		sDestroyHandler.sendEmptyMessageDelayed(0, delayMs);
	}

	private static Handler sDestroyHandler = new Handler(Looper.getMainLooper()) {

		@Override
		public void handleMessage(Message msg) {
			Process.killProcess(Process.myPid());
		}

	};

	/**
	 * 判断程序是否准备退出
	 * 
	 * @see #destroyActivitiesAndExit(long)
	 */
	public boolean isDestroyingSelf() {
		return sDestroyHandler.hasMessages(0);
	}

	private static class Channel {

		private static final String TAG = "Channel";

		public final static String CHANNEL_FROM = "channel_from"; // 游戏是否来自游戏中心
		public final static String GAME_CENTER_CHANNEL_ID = "gc_channel_id"; // 游戏中心的渠道号
		public final static String HAS_LANCHED = "lanched_before"; // 游戏是否第一次启动标记
		private final static String GAME_CENTER_PROVIDER_COMMON_PREFIX = "com.idreamsky.gc"; // 公版游戏中心包名
		public final static String GAME_ID = "game_id"; // 游戏id
		private static Context sAppContext;

		public static void init(Context appContext) {
			sAppContext = appContext;
		}

		public static void resetChannelId() {
			String cId = null;
			String channelFrom = ResourceLoader.getDefault(sAppContext)
					.getConfig(CHANNEL_FROM);

			if (!TextUtils.isEmpty(channelFrom)) {
				channelFrom = channelFrom.trim();
			}

		}

//		private static String getChannelIdFromGameCenter() {
//			String channelId = null;
//			List<String> providers = new ArrayList<String>();
//
//			List<PackageInfo> list = sAppContext.getPackageManager()
//					.getInstalledPackages(PackageManager.GET_PROVIDERS);
//			for (PackageInfo packageInfo : list) {
//				ProviderInfo[] infos = packageInfo.providers;
//				if (null != infos) {
//					for (ProviderInfo providerInfo : infos) {
//						if (providerInfo != null
//								&& providerInfo.authority != null) {
//							if (providerInfo.authority
//									.startsWith(GAME_CENTER_PROVIDER_COMMON_PREFIX)) {
//								if (providerInfo.authority
//										.equals(GAME_CENTER_PROVIDER_COMMON_PREFIX)) {
//									channelId = getChannelIdFromContentProvider(providerInfo.authority);
//								} else {
//									providers.add(providerInfo.authority);
//								}
//							}
//						}
//					}
//				}
//			}
//			if (null == channelId) {
//				for (String name : providers) {
//					channelId = getChannelIdFromContentProvider(name);
//					if (null != channelId) {
//						break;
//					}
//				}
//			}
//			LogUtil.d(TAG, "getChannelIdFromGameCenter channelId = "
//					+ channelId);
//			return channelId;
//		}

//		private static String getChannelIdFromContentProvider(
//				String authorityName) {
//			String channelId = null;
//			String uriString = "content://" + authorityName + "/channel_id";
//			Uri uri = Uri.parse(uriString);
//			Cursor cursor = sAppContext.getContentResolver().query(uri,
//					new String[] { "channel_id" }, null, null, null);
//			if (null != cursor) {
//				if (cursor.moveToNext()) {
//					channelId = cursor.getString(0);
//				}
//				cursor.close();
//			}
//			if (ChannelConfig.DEBUG_VERSION) {
//				Log.d(TAG, "getChannelIdFromContentProvider channelId = "
//						+ channelId);
//			}
//			return channelId;
//		}

//		private static String getChannelIdFromGPlus() {
//			String channelId = null;
//			String uriString = "content://is.cc.mobile.g.c/channel_id";
//			Uri uri = Uri.parse(uriString);
//			Cursor cursor = sAppContext.getContentResolver().query(uri,
//					new String[] { "channel_id" }, null, null, null);
//			if (null != cursor) {
//				if (cursor.moveToNext()) {
//					channelId = cursor.getString(0);
//				}
//				cursor.close();
//			}
//			Log.d("test", "getChannelIdFromContentProvider channelId = "
//					+ channelId);
//			return channelId;
//		}
	}

	public HashMap<String, Object> getParams() {
		return params;
	}
	
	public void setParams(final HashMap<String, Object> params) {
		this.params = params;
	}
	
	public String getParamsValue(String key){
		String result = "";
		String value = getConfig(key);
		if (!TextUtils.isEmpty(value)){
			result = value;
		}else{
			if (null != params && params.containsKey(key)){
				result = (String) params.get(key);
			}
		} 
		return result;
	}
}
