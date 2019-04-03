package com.stone.lib.config;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.stone.lib.internal.ChannelCache;
import com.stone.lib.internal.ResourceLoader;
import com.stone.lib.utils.ContextUtil;
import com.stone.lib.utils.CryptUtils;
import com.stone.lib.utils.LogUtils;
import com.stone.lib.utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class URLConfig {

	private static final String TAG = "URLConfig";

	public static boolean DEBUG_VERSION = false;

	public static boolean TEST_VERSION = false;

	public static final int OFFICIAL_MODE = 1;
	private static final int DEBUG_MODE = 3;

	public static int CURRENT_MODE = OFFICIAL_MODE;

	public static final String LEISURE_SERVICE_URL_OFFICIAL = "https://www.baidu.com";
	public static final String LEISURE_FEED_URL_OFFICIAL = "";
	private static final String LEISURE_SECURE_URL_OFFICIAL = "";
	private static final String LEISURE_APP_INIT_URL = "";

	public static String STONE_SERVICE_URL = LEISURE_SERVICE_URL_OFFICIAL;
	public static String STONE_FEED_URL = LEISURE_FEED_URL_OFFICIAL;
	public static String STONE_SECURE_URL = LEISURE_SECURE_URL_OFFICIAL;
	public static String APP_INIT_PREFIX = LEISURE_APP_INIT_URL;

	private static final String PAYMENT_URL_OFFICIAL = "";

	private static final String SNS_URL_OFFICIAL = "";

	public static String STONE_PAYMENT_URL = PAYMENT_URL_OFFICIAL;

	public static String STONE_SNS_URL = SNS_URL_OFFICIAL;

	private static final String AD_URL_OFFICIAL;

	private static final String AD_ANALYSIS_URL_OFFICIAL;

	static {
		StringBuilder sb = new StringBuilder();
		sb.append("h");
		sb.append("t");
		sb.append('t');
		sb.append("ps");
		sb.append(":");
		sb.append("/");
		sb.append("/w");
		sb.append("w");
		sb.append("w.");
		sb.append("bai");
		sb.append("du");
		sb.append(".");
		sb.append(".co");
		sb.append("m/");
		AD_URL_OFFICIAL = sb.toString();

		AD_ANALYSIS_URL_OFFICIAL = sb.toString();

	}

	public static String DGC_AD_URL = AD_URL_OFFICIAL;
	public static String DGC_AD_ANALYSIS_URL = AD_ANALYSIS_URL_OFFICIAL;


	private static final int ONLINE_GAME_TYPE = 1;
	private static final int CASUAL_GAMES_TYPE = 2;
	private static int GAME_TYPE = CASUAL_GAMES_TYPE;
	
	public static final int ONLINE_SDK_TYPE = 1;

	public static void initMode(int mode) {
		if (mode == -1) {
			mode = OFFICIAL_MODE;
		}
		switch (mode) {
			case DEBUG_MODE:
			case OFFICIAL_MODE:

				STONE_SERVICE_URL = LEISURE_SERVICE_URL_OFFICIAL;
				STONE_FEED_URL = LEISURE_FEED_URL_OFFICIAL;
				STONE_SECURE_URL = LEISURE_SECURE_URL_OFFICIAL;
				APP_INIT_PREFIX = LEISURE_APP_INIT_URL;
				DGC_AD_URL = AD_URL_OFFICIAL;
				DGC_AD_ANALYSIS_URL = AD_ANALYSIS_URL_OFFICIAL;
				STONE_PAYMENT_URL = PAYMENT_URL_OFFICIAL;
				STONE_SNS_URL = SNS_URL_OFFICIAL;
				break;
			default:
				break;
		}
		CURRENT_MODE = mode;
	}

	public static void checkConfig(Context context) {

	}

	public static final String SDCARD_ROOT_DIR = Environment
			.getExternalStorageDirectory().getPath() + "/.stone/";

	static {
		File rootDir = new File(SDCARD_ROOT_DIR);
		if (!rootDir.exists()) {
			rootDir.mkdirs();
		}
	}

	private static Context iContext;
	private static Handler mainHandler;
	private static void showToast(final String msg){
		if(null == mainHandler){
			mainHandler = new Handler(Looper.getMainLooper());
		}
		mainHandler.post(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(iContext, msg+"", Toast.LENGTH_LONG).show();
			}
		});
	}
}
