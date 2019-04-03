package com.stone.lib.utils;

import android.util.Log;

import com.stone.lib.config.BaseConfig;

/*
 * 日志工具
 * 可开关日志打印
 * */
public final class LogUtils {

	private LogUtils() {
	}

	public static void i(String tag, CharSequence c) {
		if (BaseConfig.isDebug && c != null) {
			Log.i(BaseConfig.COMPANY + ":" + tag, c.toString());
		}
	}

	public static void i(String tag, Character c) {
		if (BaseConfig.isDebug && c != null) {
			Log.i(BaseConfig.COMPANY + ":" + tag, c.toString());
		}
	}

	public static void i(String tag, boolean c) {
		if (BaseConfig.isDebug) {
			Log.i(BaseConfig.COMPANY + ":" + tag, "" + c);
		}
	}

	public static void i(String tag, char c) {
		if (BaseConfig.isDebug) {
			Log.i(BaseConfig.COMPANY + ":" + tag, "" + c);
		}
	}

	public static void i(String tag, byte[] b) {
		if (BaseConfig.isDebug && b !=null ) {
			Log.i(BaseConfig.COMPANY + ":" + tag, new String(b));
		}
	}

	public static void i(String tag, int c) {
		if (BaseConfig.isDebug) {
			Log.i(BaseConfig.COMPANY + ":" + tag, "" + c);
		}
	}

	public static void d(String tag, CharSequence c) {
		if (BaseConfig.isDebug && c != null) {
			Log.d(BaseConfig.COMPANY + ":" + tag, c.toString());
		}
	}

	public static void d(String tag, Character c) {
		if (BaseConfig.isDebug && c != null) {
			Log.d(BaseConfig.COMPANY + ":" + tag, c.toString());
		}
	}

	public static void d(String tag, boolean c) {
		if (BaseConfig.isDebug) {
			Log.d(BaseConfig.COMPANY + ":" + tag, "" + c);
		}
	}

	public static void d(String tag, char c) {
		if (BaseConfig.isDebug) {
			Log.d(BaseConfig.COMPANY + ":" + tag, "" + c);
		}
	}

	public static void d(String tag, byte[] b) {
		if (BaseConfig.isDebug && b !=null ) {
			Log.d(BaseConfig.COMPANY + ":" + tag, new String(b));
		}
	}

	public static void d(String tag, int c) {
		if (BaseConfig.isDebug) {
			Log.d(BaseConfig.COMPANY + ":" + tag, "" + c);
		}
	}

	public static void w(String tag, CharSequence c) {
		if (BaseConfig.isDebug && c != null) {
			Log.w(BaseConfig.COMPANY + ":" + tag, c.toString());
		}
	}

	public static void w(String tag, Character c) {
		if (BaseConfig.isDebug && c != null) {
			Log.w(BaseConfig.COMPANY + ":" + tag, c.toString());
		}
	}

	public static void w(String tag, boolean c) {
		if (BaseConfig.isDebug) {
			Log.w(BaseConfig.COMPANY + ":" + tag, "" + c);
		}
	}

	public static void w(String tag, char c) {
		if (BaseConfig.isDebug) {
			Log.w(BaseConfig.COMPANY + ":" + tag, "" + c);
		}
	}

	public static void w(String tag, byte[] b) {
		if (BaseConfig.isDebug && b !=null ) {
			Log.w(BaseConfig.COMPANY + ":" + tag, new String(b));
		}
	}

	public static void w(String tag, int c) {
		if (BaseConfig.isDebug) {
			Log.w(BaseConfig.COMPANY + ":" + tag, "" + c);
		}
	}

	public static void e(String tag, Character c) {
		Log.e(BaseConfig.COMPANY + ":" + tag, c.toString());
	}

	public static void e(String tag, CharSequence c) {
		Log.e(BaseConfig.COMPANY + ":" + tag, c.toString());
	}

	public static void e(String tag, boolean c) {
		Log.e(BaseConfig.COMPANY + ":" + tag, "" + c);
	}

	public static void e(String tag, char c) {
		Log.e(BaseConfig.COMPANY + ":" + tag, "" + c);
	}

	public static void e(String tag, byte[] b) {
		Log.e(BaseConfig.COMPANY + ":" + tag, new String(b));
	}

	public static void e(String tag, int c) {
		Log.e(BaseConfig.COMPANY + ":" + tag, "" + c);
	}

	public static void e(String tag, String msg, Throwable tr) {
		Log.e(BaseConfig.COMPANY + ":" + tag, msg, tr);
	}
}
