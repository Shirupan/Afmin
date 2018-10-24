/**   
 * 文件名：CrashHandler.java</br>
 * 描述： </br>
 * 开发人员：杜逸平 </br>
 * 创建时间： 2014-10-22
 */

package com.gigifun.gp.exception;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.gigifun.gp.utils.UcallBack;
import com.gigifun.gp.utils.UgameUtil;
import com.gigifun.gp.utils.UhttpUtil;
import com.gigifun.gp.utils.LogUtil;
import com.gigifun.gp.utils.MD5Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.TreeSet;

import okhttp3.Call;


@SuppressLint("SimpleDateFormat")
public class CrashHandler implements UncaughtExceptionHandler {
	/** Debug Log tag */
	public static final String TAG = "CrashHandler";
	/**
	 * 是否开启日志输出,在Debug状态下开启, 在Release状态下关闭以提示程序性能
	 * */
	public static final boolean DEBUG = true;
	/** 系统默认的UncaughtException处理类 */
	private UncaughtExceptionHandler mDefaultHandler;
	/** CrashHandler实例 */
	private static CrashHandler INSTANCE;
	/** 程序的Context对象 */
	private Context mContext;

	/** 使用Properties来保存设备的信息和错误堆栈信息 */
	private Properties mDeviceCrashInfo = new Properties();
	private static final String VERSION_NAME = "versionName";
	private static final String VERSION_CODE = "versionCode";
	private static final String STACK_TRACE = "STACK_TRACE";
	/** 错误报告文件的扩展名 */
	private static final String CRASH_REPORTER_EXTENSION = ".log";

	private SharedPreferences preferences;
	
	
	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CrashHandler();
		}
		return INSTANCE;
	}

	/**
	 * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
	 * 
	 * @param ctx
	 */
	public void init(Context ctx) {
		mContext = ctx;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
		preferences = ((Activity)ctx).getSharedPreferences("MyCount", Context.MODE_PRIVATE);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			// Sleep一会后结束程序
			/*try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {  
				Log.e(TAG, "Error : ", e);
			}*/
			
			//android.os.Process.killProcess(android.os.Process.myPid());
			//System.exit(10);
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false
	 */
	private boolean handleException(final Throwable ex) {
		if (ex == null) {
			return true;
		}
		//getErrorInfo(ex);
		//final String msg = ex.getMessage();  
		//System.exit(0);
		
		//((Activity)mContext).finish();
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				
				Toast.makeText(mContext, "程序出错啦", Toast.LENGTH_LONG).show();
				getPhoneErrorInfo(ex);
				
				Looper.loop();
			}

		}.start();
		return true;
	}

	/**
	 * 在程序启动时候, 可以调用该函数来发送以前没有发送的报告
	 */
	public void sendPreviousReportsToServer() {
		sendCrashReportsToServer(mContext);
	}

	private void getPhoneErrorInfo(Throwable arg1){
		final String versioninfo = getVersionInfo();
		//时间
		Calendar calendar=Calendar.getInstance();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date=format.format(calendar.getTime());
        //1.获取用户名
		   String userName=preferences.getString("username", "no username");
		   userName="用户名= "+userName;
		// 2.获取手机的硬件信息.
		final String mobileInfo = getMobileInfo();

		// 3.把错误的堆栈信息 获取出来
		final String errorinfo = getErrorInfo(arg1);

		final String log=date+'\n'+userName+'\n' +versioninfo + '\n' + mobileInfo + '\n' + errorinfo+ '\n'+ '\n';
		
		pushErrorLog(log);
		LogUtil.e(log);
		
		
		//4、将错误信息保存到手机(sdcard)
		/*if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			
			String sdcardPath = "/mnt/sdcard/";
			File file=new File(sdcardPath,"U");
			if(!file.exists()){
				file.mkdirs();
			}
			
			writeLog(log,
					file.getAbsolutePath());  
			 Log.e("U_LOG","写入sdcard成功");
		}*/
		//将错误信息保存到手机(内存)
		//writeLog(log);
		//5、将错误信息发送到服务器
		
	}
	
	
	
	
	
	/**
	 * 把错误报告发送给服务器,包含新产生的和以前没发送的.
	 * 
	 * @param ctx
	 */
	private void sendCrashReportsToServer(Context ctx) {
		String[] crFiles = getCrashReportFiles(ctx);
		if (crFiles != null && crFiles.length > 0) {
			TreeSet<String> sortedFiles = new TreeSet<String>();
			sortedFiles.addAll(Arrays.asList(crFiles));

			for (String fileName : sortedFiles) {
				File cr = new File(ctx.getFilesDir(), fileName);
				postReport(cr);
				cr.delete();// 删除已发送的报告
			}
		}
	}

	private void postReport(File file) {
		// TODO 使用HTTP Post 发送错误报告到服务器
		// 这里不再详述,开发者可以根据OPhoneSDN上的其他网络操作
		// 教程来提交错误报告
		pushErrorLog("错误信息");
		
	}
	
	
	
	
	 /**
		 * 将错误日志发送到服务端
		 */
	    @SuppressWarnings("deprecation")
		private void pushErrorLog(String log){
	    	String timestamp=String.valueOf(System.currentTimeMillis());
	    	final HashMap<String,Object> map=new HashMap<String,Object>();
	    	map.put("text", URLEncoder.encode(log)); 
	    	map.put("time", timestamp);
	    	map.put("sign", MD5Utils.md5Sign(log, timestamp));
	    	new Thread(){

				@Override  
				public void run() {
					requestHttp(map);  
					super.run();
				}
	    		
	    	}.start();
	    	
	    }
	    
	    
	    
	    private void requestHttp(HashMap<String,Object> param){
	    	
	    	UhttpUtil.post(UgameUtil.getInstance().ERROR_LOG, new UcallBack() {

				@Override
				public void onResponse(String response, int arg1) {
					 parseJson(response);
					
				}
				
				@Override
				public void onError(Call arg0, Exception arg1, int arg2) {
					// TODO Auto-generated method stub
					
				}
			});
	    	
	    	
	    	
	    }
	    
	
	  private void parseJson(String json){
		  try {
			JSONObject jo=new JSONObject(json);
			  String status= jo.optString("Status");
			  if(status.equals("1")){
				  Log.i("U_LOG","发送成功");
			  }else{
				  String code=jo.optString("Code");
				  if("301".equals(code)){
					  Log.i("U_LOG","缺少参数");
				  }else if("302".equals(code)){
					  Log.i("U_LOG","签名失败");
				  }
			  }
		} catch (JSONException e) {
			e.printStackTrace();
		}
	  }  
	    

	/**
	 * 获取错误报告文件名
	 * 
	 * @param ctx
	 * @return
	 */
	private String[] getCrashReportFiles(Context ctx) {
		File filesDir = ctx.getFilesDir();
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(CRASH_REPORTER_EXTENSION);
			}
		};
		return filesDir.list(filter);
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return
	 */
	@SuppressWarnings("unused")
	private String saveCrashInfoToFile(Throwable ex) {
		Writer info = new StringWriter();
		PrintWriter printWriter = new PrintWriter(info);
		ex.printStackTrace(printWriter);

		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}

		String result = info.toString();
		printWriter.close();
		mDeviceCrashInfo.put(STACK_TRACE, result);

		try {
			long timestamp = System.currentTimeMillis();
			String fileName = "crash-" + timestamp + CRASH_REPORTER_EXTENSION;
			FileOutputStream trace = mContext.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			mDeviceCrashInfo.store(trace, "");
			trace.flush();
			trace.close();
			return fileName;
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing report file...", e);
		}
		return null;
	}

	/**
	 * 收集程序崩溃的设备信息
	 * 
	 * @param ctx
	 */
	public void collectCrashDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				mDeviceCrashInfo.put(VERSION_NAME,
						pi.versionName == null ? "not set" : pi.versionName);
				mDeviceCrashInfo.put(VERSION_CODE, pi.versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "Error while collect package info", e);
		}
		// 使用反射来收集设备信息.在Build类中包含各种设备信息,
		// 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
		// 具体信息请参考后面的截图
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				mDeviceCrashInfo.put(field.getName(), field.get(null));
				if (DEBUG) {
					Log.d(TAG, field.getName() + " : " + field.get(null));
				}
			} catch (Exception e) {
				Log.e(TAG, "Error while collect crash info", e);
			}

		}

	}

	// 在上面CrashHandler实现中，当错误发生的时候使用Toast显示错误信息，然后收集错误报告并保存在文件中。
	// 发送错误报告代码请读者自己实现。在uncaughtException函数中调用了Thread.sleep(3000)；来让线程停止一会是为了显示Toast信息给用户，然后Kill程序。如果你不用Toast来显示信息则可以去除该代码。除了Toast外，开发者还可以选择使用Notification来显示错误内容并让用户选择是否提交错误报告而不是自动提交。关于Notification的实现请读者参考：NotificationManager。在发送错误报道的时候，可以先检测网络是否可用，如果不可用则可以在以后网络情况可用的情况下发送。
	// 网络监测代码如下：

	/**
	 * 检测网络连接是否可用
	 * 
	 * @param ctx
	 * @return true 可用; false 不可用
	 */
	@SuppressWarnings("unused")
	private boolean isNetworkAvailable(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		}
		NetworkInfo[] netinfo = cm.getAllNetworkInfo();
		if (netinfo == null) {
			return false;
		}
		for (int i = 0; i < netinfo.length; i++) {
			if (netinfo[i].isConnected()) {
				return true;
			}
		}
		return false;
	}
	
	
	
	/**
	 * 获取错误的信息
	 * 
	 * @param arg1
	 * @return
	 */
	private String getErrorInfo(Throwable arg1) {
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		arg1.printStackTrace(pw);
		pw.close();
		String error = writer.toString();
		Log.e("log",error);
		return error;
	}

	/**
	 * 获取手机的硬件信息
	 * 
	 * @return
	 */
	private String getMobileInfo() {
		StringBuffer sb = new StringBuffer();
		// 通过反射获取系统的硬件信息
		try {

			Field[] fields = Build.class.getDeclaredFields();
			for (Field field : fields) {
				// 暴力反射 ,获取私有的信息
				field.setAccessible(true);
				String name = field.getName();
				String value = field.get(null).toString();
				sb.append(name + "=" + value);
				sb.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 获取手机的版本信息
	 * 
	 * @return
	 */
	private String getVersionInfo() {
		try {
			PackageManager pm = mContext.getPackageManager();
			PackageInfo info = pm.getPackageInfo(mContext.getPackageName(), 0);
			return "包名= "+info.packageName+'\n'+"版号号 = " + info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
			return "版本号未知";
		}
	}

	
	// 写入Log信息的方法，写入到SD卡里面  
		@SuppressLint("NewApi")
		private void writeLog(String log, String name) {
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String filename = "U" + "_" + format.format(new Date()) + CRASH_REPORTER_EXTENSION;
            File file=new File(name,filename);
            
			try {
				FileOutputStream stream = new FileOutputStream(file);
				OutputStreamWriter output = new OutputStreamWriter(stream);
				BufferedWriter bw = new BufferedWriter(output);
				// 写入相关Log到文件
				bw.write(log);
				bw.newLine();
				bw.close();
				output.close();
				Log.e("U_LOG", "写入成功");
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("U_LOG", "写入异常: " + e.getMessage());
			}
		}
		
		
		@SuppressWarnings("unused")
		private void writeLog(String log) {
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String fileName = "U" + "_" + format.format(new Date()) + CRASH_REPORTER_EXTENSION;
            try {
				FileOutputStream stream = mContext.openFileOutput(fileName,
						Context.MODE_PRIVATE);
				OutputStreamWriter output = new OutputStreamWriter(stream);
				BufferedWriter bw = new BufferedWriter(output);
				// 写入相关Log到文件
				bw.write(log);
				bw.newLine();
				bw.close();
				output.close();
				Log.e("U_LOG", "写入成功");
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("U_LOG", "写入异常: " + e.getMessage());
			}
		}
		
		
		
}