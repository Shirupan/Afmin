package com.stone.lib.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("MissingPermission")
public class ContextUtil {
	private static final String TAG = "ContextUtil";

	public static Object invokeTelephonyManagerMethod(String methodName,
			Context cxt) {
		try {
			Method m = Context.class.getMethod("getS" + "yste" + "mSer"
					+ "vice", new Class<?>[] { String.class });
			Object phone = m.invoke(cxt, new Object[] { "phone" });

			Method m2 = phone.getClass().getMethod(methodName,
					(Class<?>[]) null);
			return m2.invoke(phone, (Object[]) null);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public static Object invokeTelephoneManagerMethod(String methodName,
			Class<?>[] paramTypes, Object[] params, Context cxt) {
		try {
			Method m = Context.class.getMethod("getS" + "yste" + "mSer"
					+ "vice", new Class<?>[] { String.class });
			Object phone = m.invoke(cxt, new Object[] { "phone" });

			Method m2 = phone.getClass().getMethod(methodName, paramTypes);
			return m2.invoke(phone, (Object[]) params);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	// private static final String TAG = "ContextUtil";

	public static final String UNKNOWN = "unknown";
	public static final String WIFI = "wifi";
	public static final String MOBILE = "mobile";

	/***
	 * Return {@link #UNKNOWN} if the network type can not be accessed.
	 * 
	 * @return 'wifi' or 'uninet' etc.
	 */
	public static String getNetworkType(Context context) {
		if (!checkPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)) {
			return UNKNOWN;
		}
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		 NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return UNKNOWN;
		}
		int net_type = networkInfo.getType();
		if (net_type == ConnectivityManager.TYPE_MOBILE) {
			networkInfo = manager.getNetworkInfo(0);
			String netString = networkInfo.getExtraInfo();
			if (TextUtils.isEmpty(netString)) {
				return UNKNOWN;
			} else {
				return netString.length() > 10 ? netString.substring(0, 10)
						: netString;
			}
		} else {
			return WIFI;
		}
	}

	public static String getSimpleNetwork(Context context) {
		if (!checkPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)) {
			return UNKNOWN;
		}
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return UNKNOWN;
		}
		int net_type = networkInfo.getType();
		if (net_type == ConnectivityManager.TYPE_MOBILE) {
			return MOBILE;
		} else {
			return WIFI;
		}
	}

	/**
	 * 区分2G、3G
	 * 
	 * unknow 、wifi、 1、2、3
	 * 
	 * @param context
	 * @return
	 */
	public static String getNetworkDetail(Context context) {
		String simpleType = getSimpleNetwork(context);
		if (simpleType.equals(MOBILE)) {
			int type = (Integer) invokeTelephonyManagerMethod("getN" + "etwo"
					+ "rkType", context);
			// 中国电信为CTC
			// NETWORK_TYPE_EVDO_A是中国电信3G的getNetworkType
			// NETWORK_TYPE_CDMA电信2G是CDMA

			int gtype = (Integer) invokeTelephoneManagerMethod(
					"getNetworkClass", new Class<?>[] { int.class },
					new Object[] { type }, context);
			if (gtype == 0) {
				return UNKNOWN;
			} else {
				return "" + gtype;
			}
		}
		return simpleType;
	}

	public static String getDeviceId(Context context) {
		String imei = getIMEI(context);
		if (!imei.equals(UNKNOWN)) {
			return imei;
		}
		return getLocalMacAddress(context);
	}

	/**
	 * Get the phone number of the device.
	 * 
	 * @param context
	 * @return {@link #UNKNOWN} if not found.
	 */
	public static String getPhoneNum(Context context) {
		return UNKNOWN;
	}

	private static boolean isZero(String id) {
		for (int i = 0; i < id.length(); i++) {
			char index = id.charAt(i);
			if (index != '0')
				return false;
		}
		return true;
	}

	public static String getAccountInfo(Context context) {
		return UNKNOWN;
	}

	// get cpu frequence
	public static long getCpuFre() {
		// #cat "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"
		// /proc/cpuinfo

		String cpuFreFile = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq";
		return readLong(cpuFreFile);
	}

	private static long readLong(String file) {
		RandomAccessFile raf = null;

		try {
			raf = getFile(file);
			return Long.valueOf(raf.readLine());
		} catch (Exception e) {
			return 0;
		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private static RandomAccessFile getFile(String filename) throws IOException {
		File f = new File(filename);
		return new RandomAccessFile(f, "r");
	}

	/**
	 * 拿到resolution as width X height
	 * 
	 * @param context
	 * @return
	 */
	public static String getResolutionAsString(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		int widthPixels = display.getWidth();
		int heightPixels = display.getHeight();
		return widthPixels < heightPixels ? widthPixels + "X" + heightPixels
				: heightPixels + "X" + widthPixels;
	}

	/**
	 * Check whether the specified permission is granted to the current package.
	 * 
	 * @param context
	 * @param permissionName
	 *            The permission.
	 * @return True if granted, false otherwise.
	 */
	public static boolean checkPermission(Context context, String permissionName) {
		PackageManager packageManager = context.getPackageManager();
		String pkgName = context.getPackageName();
		return packageManager.checkPermission(permissionName, pkgName) == PackageManager.PERMISSION_GRANTED;
	}

	/**
	 * 是否有网
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isServerReachable(Context context) {
		if (!checkPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)) {
			return false;
		}
		ConnectivityManager conMan = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = conMan.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnected();
	}

	/**
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isApplicationInstalled(Context context,
			String packageName) {
		if (TextUtils.isEmpty(packageName)) {
			return false;
		}
		PackageManager pm = context.getPackageManager();
		try {
			pm.getPackageInfo(packageName, 0);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
		}
		return false;
	}

	/***
	 * 启用系统发邮件界面
	 * 
	 * @param activity
	 * @param body
	 *            邮件正文
	 * 
	 * @param chooseTitle
	 *            Optional title that will be displayed in the chooser
	 */
	public static void startSendEmail(Activity activity, String body,
			String emailReciver, String title, String chooseTitle) {
		try {
			Intent email = new Intent(Intent.ACTION_SEND);
			email.setType("message/rfc882");
			// 设置邮件默认地址
			if (emailReciver != null) {
				email.putExtra(Intent.EXTRA_EMAIL,
						new String[] { emailReciver });
			}
			// 设置邮件默认标题
			email.putExtra(Intent.EXTRA_SUBJECT, title);
			// 设置要默认发送的内容
			if (body != null) {
				email.putExtra(Intent.EXTRA_TEXT, body);
			}
			// 调用系统的邮件系统
			activity.startActivity(Intent.createChooser(email, chooseTitle));
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 启用系统发短信界面
	 * @param body
	 *            短信正文
	 * @param objectTo
	 *            收件人
	 */
	public static void startSendMsg(Activity activity, String body,
			String objectTo) {
		try {
			Intent message = null;
			if (null != objectTo && !"".equals(objectTo)) {
				message = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
						+ objectTo));
			} else {
				message = new Intent(Intent.ACTION_VIEW);
				message.setType("vnd.android-dir/mms-sms");
			}
			message.putExtra("sms_body", body);
			activity.startActivity(message);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 去本地图库选择一张图片
	 * 
	 * @param activity
	 * @param requestId
	 *            请求ID
	 */
	public static void chooseLocalPicture(Activity activity, int requestId) {

		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		try {
			activity.startActivityForResult(intent, requestId);
		} catch (Exception e) {
		}
	}

	/**
	 * 打开系统照相界面
	 * 
	 * @param activity
	 * @param requestId
	 *            请求ID，onActivityResult中判定ID
	 */
	public static void takePhoto(Activity activity, int requestId) {
		try {
			Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			activity.startActivityForResult(it, requestId);
		} catch (Exception e) {

		}
	}

	public static final int CODE_PKG_NOT_FOUND = 1,
			CODE_MAIN_ACT_NOT_FOUND = 2, CODE_SUCCESS = 3;

	/**
	 * Start the package's main activity. If the package contains many
	 * activities, only one of them will be launched.
	 * 
	 * @param targetPkg
	 * @param context
	 */
	public static final int startPackage(String targetPkg, Context context) {
		PackageManager pm = context.getPackageManager();
		/*
		 * try { Intent intent = pm.getLaunchIntentForPackage(targetPkg); if
		 * (null != intent) { context.startActivity(intent); return
		 * CODE_SUCCESS; } return CODE_MAIN_ACT_NOT_FOUND; } catch (Exception e)
		 * { return CODE_PKG_NOT_FOUND; }
		 */

		String pkg = targetPkg;
		ResolveInfo resolveInfo = null;

		final Intent mainIntentLeDou = new Intent(Intent.ACTION_MAIN, null);
		mainIntentLeDou.addCategory("intent.category.HILEDOU");

		final List<ResolveInfo> appsListLeDou = pm.queryIntentActivities(
				mainIntentLeDou, 0);
		for (int i = 0; i < appsListLeDou.size(); i++) {
			resolveInfo = appsListLeDou.get(i);
			if (resolveInfo.activityInfo.packageName.equals(pkg)) {
				break;
			}
			resolveInfo = null;
		}

		if (resolveInfo != null) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			String name = resolveInfo.activityInfo.name;
			ComponentName comp = new ComponentName(pkg, name);
			intent.setComponent(comp);
			context.startActivity(intent);
			return CODE_SUCCESS;
		} else {
			Intent mainIntent = pm.getLaunchIntentForPackage(pkg);
			if (mainIntent != null) {
				try {
					mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(mainIntent);
					return CODE_SUCCESS;
				} catch (ActivityNotFoundException e) {
					LogUtils.e("", "No entrance activity found in package " + pkg);
					return CODE_PKG_NOT_FOUND;
				}
			} else {
				return CODE_MAIN_ACT_NOT_FOUND;
			}
		}

	}

	public interface CommonSelect {

		public void onItemSelected(String onSelectedText, String onSelectedValue);

		// public void onCancel();
	}

	/**
	 * 通过的alertDialog,本方法提供给js，因js会传递过来jsonArray字符
	 * 
	 * @param context
	 * @param array
	 * @param listener
	 *            dialog中每一项点击的事件
	 * @throws JSONException
	 */
	public static final void commonAlertDialog(Context context,
			JSONArray array, int index, final CommonSelect listener)
			throws JSONException {
		int len = array.length();
		final String[] texts = new String[len];
		final String[] values = new String[len];
		for (int i = 0; i < len; i++) {
			JSONObject object = (JSONObject) array.get(i);
			texts[i] = (String) object.get("text");
			values[i] = (String) object.get("value");
		}
		AlertDialog dialog = new AlertDialog.Builder(context)
				.setSingleChoiceItems(texts, index,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String onSelectedText = texts[which];
								String onSelectedValue = values[which];
								dialog.dismiss();
								listener.onItemSelected(onSelectedText,
										onSelectedValue);
							}
						}).create();
		dialog.show();
	}

	public static void call(Context context, String num) {
		try {
			Uri uri = Uri.parse("tel:" + num);
			Intent it = new Intent(Intent.ACTION_DIAL, uri);
			it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(it);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get IP address of the device.
	 * 
	 * @return {@link #UNKNOWN} will be returned if any unexpected occurs.
	 */
	public static String getLocalIpAddress() {
		try {
			Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces();
			if (en == null) {
				return UNKNOWN;
			}
			while (en.hasMoreElements()) {
				NetworkInterface intf = en.nextElement();
				Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
				if (enumIpAddr == null) {
					continue;
				}

				while (enumIpAddr.hasMoreElements()) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress();
					}
				}
			}
		} catch (SocketException ex) {
		}
		return UNKNOWN;
	}

	/**
	 * Get IMEI of the device. If it has no IMEI or no
	 * {@link Manifest.permission#READ_PHONE_STATE} permission or it is an
	 * emulator, {@link #UNKNOWN} will be returned.
	 */
	public static String getIMEI(Context context) {
		String id = null;
		if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
			id = (String) invokeTelephonyManagerMethod("getD" + "evi" + "ceId",
					context);
		}
//		else{
//			Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//		}
		if (TextUtils.isEmpty(id) || isZero(id)) {
			return UNKNOWN;
		}

		return id;
	}

	public static String getIMSI(Context context) {
		String id = null;
		if (checkPermission(context,
				Manifest.permission.READ_PHONE_STATE)) {
			id = (String) invokeTelephonyManagerMethod("getS" + "ubscr"
					+ "iberId", context);
		}
		if (TextUtils.isEmpty(id) || isZero(id)) {
			return UNKNOWN;
		}

		return id;
	}

	/**
	 * Return the WIFI-MAC address.
	 * 
	 * @return {@link #UNKNOWN} will be returned if any unexpected occurs.
	 */
	public static String getLocalMacAddress(Context context) {
		if (!checkPermission(context, Manifest.permission.ACCESS_WIFI_STATE)) {
			return UNKNOWN;
		}
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		if (null != info) {
			String mac = info.getMacAddress();
			if (!TextUtils.isEmpty(mac)) {
				return mac;
			}
		}

		return UNKNOWN;
	}

	public static String getUUID(Context context) {
		final String tmDevice, tmSerial, androidId;
		if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
			tmDevice = ""
					+ invokeTelephonyManagerMethod(
							"ge" + "tDe" + "vic" + "eId", context);
			tmSerial = ""
					+ invokeTelephonyManagerMethod("getSi" + "mSer" + "ialNu"
							+ "mber", context);
		} else {
			tmDevice = "";
			tmSerial = "";
		}
		androidId = ""
				+ Settings.Secure.getString(context.getContentResolver(),
						Settings.Secure.ANDROID_ID);

		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		return deviceUuid.toString();
	}

	/**
	 * Return the current application's label.
	 */
	public static String getLabel(Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			ApplicationInfo info = pm.getApplicationInfo(
					context.getPackageName(), 0);
			String label = info.loadLabel(pm).toString();
			return label;
		} catch (PackageManager.NameNotFoundException e) {
			// Should never happen
		}
		return null;
	}

	/**
	 * Return the current application's ICON.
	 */
	public static Drawable getIcon(Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			ApplicationInfo info = pm.getApplicationInfo(
					context.getPackageName(), 0);

			return info.loadIcon(pm);

		} catch (PackageManager.NameNotFoundException e) {
			// Should never happen
		}
		return null;
	}

	public static int getIconInt(Context context) {
		try {
			PackageManager pm = context.getPackageManager();

			PackageInfo pInfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);

			return pInfo.applicationInfo.icon;

		} catch (PackageManager.NameNotFoundException e) {
			// Should never happen
		}
		return -1;
	}

	public static String getLabel(String packageName, Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			ApplicationInfo info = pm.getApplicationInfo(packageName, 0);
			String label = info.loadLabel(pm).toString();
			return label;
		} catch (PackageManager.NameNotFoundException e) {
			// Should never happen
		}
		return null;
	}

	/**
	 * Install the specified file.
	 */
	public static void installPackage(Context context, File file) {
		try {
			Intent  installIntent = new Intent(Intent.ACTION_VIEW); 
			installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
			Uri uri;
			//判断兼容7.0版本
			// 系统版本大于N的统一用FileProvider处理
			if (Build.VERSION.SDK_INT >= 24) {
			    // 将文件转换成content://Uri的形式
			     uri = FileProvider.getUriForFile(context,
			    		context.getPackageName()+".provider.xProvider",
			    		file);
			    // uri = Uri.fromFile(file);
			} else {
				 uri = Uri.fromFile(file);
			}
			installIntent.setDataAndType(uri,
					"application/vnd.android.package-archive");
			context.startActivity(installIntent);
		} catch (Exception e) {
			e.printStackTrace();
			if (file != null) {
				file.delete();
			}
		}

	}

	/**
	 * Check whether the current SDCard is writable.
	 * 
	 * A lack of permission {@link Manifest.permission#WRITE_EXTERNAL_STORAGE}
	 * or a not mounted SDCard will return false.
	 */
	public static boolean isSdcardWritable(Context context) {
		if (!checkPermission(context,
				Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
			return false;
		}
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}

	/**
	 * 
	 * 是否是横屏
	 * 
	 */
	public static boolean isLandScape(Context activity) {
		boolean b = false;
		int orientation = activity.getResources().getConfiguration().orientation;
		if (orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
			b = true;
		}
		return b;
	}

	private static float sDensity = -1.0f;

	/**
	 * The logical density of the display.
	 */
	public static float getDensity(Context context) {
		if (sDensity < 0.0f) {
			sDensity = context.getResources().getDisplayMetrics().density / 1.5f;
		}
		return sDensity;
	}

	public static int getDensityInt(Context context) {
		return (int) (getDensity(context) * 240);
	}

	public static int getVersionCode(Context context, String packageName) {
		PackageInfo pi = null;
		try {
			pi = context.getPackageManager().getPackageInfo(packageName, 0);
			return pi.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 拿到当前包的签名的MD5值
	 * 
	 * @param context
	 * @return
	 */
	public static String md5Sign(Context context) {
		String packageName = context.getPackageName();
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(packageName,
					PackageManager.GET_SIGNATURES);
			Signature[] signatures = info.signatures;
			StringBuilder sb = new StringBuilder();
			for (Signature signature : signatures) {
				sb.append(signature.toCharsString());
			}
			return Utils.md5(sb.toString());
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取特定包名的签名
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static String md5Sign(Context context, String packageName) {

		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(packageName,
					PackageManager.GET_SIGNATURES);
			Signature[] signatures = info.signatures;
			StringBuilder sb = new StringBuilder();
			for (Signature signature : signatures) {
				sb.append(signature.toCharsString());
			}
			return Utils.md5(sb.toString());
		} catch (PackageManager.NameNotFoundException e) {
//			e.printStackTrace();
			LogUtils.e(TAG, e.toString());
		}
		return null;
	}

	public static boolean isDesktopPackage(Context context, String packageName) {
		boolean result = false;
		PackageManager pm = context.getPackageManager();
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		@SuppressLint("WrongConstant") List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent,PackageManager.GET_ACTIVITIES);
		if (resolveInfos == null || resolveInfos.size() <= 0) {
			return false;
		}

		for (ResolveInfo info : resolveInfos) {
			if (info.activityInfo.packageName.equals(packageName)) {
				result = true;
			}

		}
		return result;
	}

	public static boolean containActivity(Context context, String packageName,
			String activityName) {
		PackageManager pm = context.getPackageManager();
		boolean result = false;
		try {
			PackageInfo info = pm.getPackageInfo(packageName,
					PackageManager.GET_ACTIVITIES);
			ActivityInfo[] activities = info.activities;
			for (ActivityInfo aif : activities) {
				if (activityName.equals(aif.name)) {
					result = true;
					break;
				}
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static int[] getResolution(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);
		return new int[] { metrics.widthPixels, metrics.heightPixels };
	}

	/**
	 * 获取手机状态栏的高度
	 * 
	 * @param mContext
	 * @return
	 */
	public static int getStatusBarHeight(Activity mContext) {
		int statusBarHeight = 0;
		Rect frame = new Rect();
		mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		statusBarHeight = frame.top;
		if (statusBarHeight == 0) {
			try {
				Class<?> c = Class.forName("com.android.internal.R$dimen");
				Object obj = c.newInstance();
				Field field = c.getField("status_bar_height");
				int x = field.getInt(obj);
				statusBarHeight = mContext.getResources()
						.getDimensionPixelSize(x);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return statusBarHeight;
	}

	/**
	 * dip2px
	 * 
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 取sim卡所属运营商
	 * 
	 * @see {@link #SIM_TYPE_CMCC}, {@link #SIM_TYPE_UNICOM},
	 *      {@link #SIM_TYPE_TELECOM}
	 * 
	 * @return 如果判定不了，则返回{@link #SIM_TYPE_UNKNOWN}
	 */
	public static int getSimCardType(Context context) {
		try {
			if (!checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
				return SIM_TYPE_UNKNOWN;
			}
			int state = (Integer) invokeTelephonyManagerMethod("getSi" + "mState",
					context);
			if (!(state == TelephonyManager.SIM_STATE_READY)) {
				return SIM_TYPE_UNKNOWN;
			}
			String imsi = (String) invokeTelephonyManagerMethod("getSu" + "bscr"
					+ "ibe" + "rId", context);
			if (TextUtils.isEmpty(imsi)) {
				return SIM_TYPE_UNKNOWN;
			}
			if (imsi.contains("46000") || imsi.contains("46002")
					|| imsi.contains("46007")) {
				return SIM_TYPE_CMCC;
			} else if (imsi.contains("46001") || imsi.contains("46006")) {
				return SIM_TYPE_UNICOM;
			} else if (imsi.contains("46003") || imsi.contains("46005")) {
				return SIM_TYPE_TELECOM;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SIM_TYPE_UNKNOWN;
	}

	public final static int SIM_TYPE_CMCC = 1;
	public final static int SIM_TYPE_UNICOM = 2;
	public final static int SIM_TYPE_TELECOM = 3;
	public final static int SIM_TYPE_UNKNOWN = -1;

	public static double getPhysicalScreenSize(Context context) {
		int[] screenWH = getResolution(context);
		double screenSize = Math.sqrt(screenWH[0] * screenWH[0] + screenWH[1]
				* screenWH[1]);
		int densityInt = ContextUtil.getDensityInt(context);
		return screenSize / densityInt;
	}

	public static String getVersionName(Context context, String packageName) {
		PackageInfo pi = null;
		try {
			pi = context.getPackageManager().getPackageInfo(packageName, 0);
			return pi.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getVersionName(Context appContext) {
		try {
			PackageInfo pi = appContext.getPackageManager().getPackageInfo(
					appContext.getPackageName(), 0);
			return pi.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			return "";
		}
	}

	public static String getVersionCode(Context appContext) {
		try {
			PackageInfo pi = appContext.getPackageManager().getPackageInfo(
					appContext.getPackageName(), 0);
			return String.valueOf(pi.versionCode);
		} catch (PackageManager.NameNotFoundException e) {
			return "0";
		}
	}

	/**
	 * 读取sim卡序列号（已检查权限）
	 */
	public static String getSimSerialNumber(Context context) {
		String tmSerial = null;
		if (checkPermission(context,
				Manifest.permission.READ_PHONE_STATE)) {
			tmSerial = (String) invokeTelephonyManagerMethod("getSi" + "mSer"
					+ "ialNu" + "mber", context);
		} else {
			tmSerial = UNKNOWN;
		}
		return tmSerial == null ? UNKNOWN : tmSerial;
	}

	public static boolean verifyApkAvailability(Context context, String apkPath) {
		PackageManager pm = context.getPackageManager();
		PackageInfo info = null;
		try {
			info = pm.getPackageArchiveInfo(apkPath,
					PackageManager.GET_ACTIVITIES);
		} catch (Exception e) {
		}
		return info != null;
	}

	public static int getSimCardServerType(Context context) {
		int simType = 4;
		int type = ContextUtil.getSimCardType(context);
		if (type == ContextUtil.SIM_TYPE_CMCC) {
			simType = 1;
		} else if (type == ContextUtil.SIM_TYPE_UNICOM) {
			simType = 2;
		} else if (type == ContextUtil.SIM_TYPE_TELECOM) {
			simType = 3;
		}
		return simType;
	}

	/**
	 * public static final int ORIENTATION_UNDEFINED = 0; public static final
	 * int ORIENTATION_PORTRAIT = 1; public static final int
	 * ORIENTATION_LANDSCAPE = 2; public static final int ORIENTATION_SQUARE =
	 * 3;
	 * 
	 * @param context
	 * @return
	 */
	public static int getOrientation(Context context) {
		android.content.res.Configuration configuration = context
				.getResources().getConfiguration();
		return configuration.orientation;
	}

	public static boolean fuzzyInstall(Context context, String regex) {
		PackageManager pm = context.getPackageManager();
		List<ApplicationInfo> appInfos = pm.getInstalledApplications(-1);
		for (ApplicationInfo info : appInfos) {
			if (info.packageName.matches(regex)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否是手机号码
	 * 
	 * @param phoneNum
	 * @return
	 */
	public static boolean isMobileNO(String phoneNum) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		String tempPhoneNum = phoneNum.replace(" ", "");
		if (tempPhoneNum.startsWith("+86")) {
			tempPhoneNum = tempPhoneNum.replace("+86", "");
		}
		Matcher m = p.matcher(tempPhoneNum);
		return m.matches();
	}

	/**
	 * 判断是否是手机设备
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isPhoneDevice(Context context) {
		boolean isPhoneDevice = true;
		TelephonyManager telephony = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		int type = telephony.getPhoneType();
		if (type == TelephonyManager.PHONE_TYPE_NONE) {
			isPhoneDevice = false;
		} else {
			isPhoneDevice = true;
		}
		return isPhoneDevice;
	}

	/**
	 * 判断网络连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 判断SD card 是否可读
	 */
	public static boolean isSdcardReadable(Context context) {
		if (Build.VERSION.SDK_INT >= 19) {
			// 如果是KitKat，先检查是否有read权限，如果没有，则直接返回false
			if (!checkPermission(context,
					"android.permission.READ_EXTERNAL_STORAGE")) {
				return false;
			}
		}
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}
	
}
