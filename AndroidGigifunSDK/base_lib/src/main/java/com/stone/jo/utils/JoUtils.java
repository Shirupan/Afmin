package com.stone.jo.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class JoUtils {

	/**
	 * 是否在主进程
	 * 
	 * @param context 
	 * @return true or false
	 */
	public static boolean isAppMainProcess(final Context context){
		long pid = android.os.Process.myPid(); 
		ActivityManager mActivityManager = (ActivityManager)context.getSystemService(
				Context.ACTIVITY_SERVICE);
	    for (ActivityManager.RunningAppProcessInfo appProcess :
	    	mActivityManager.getRunningAppProcesses()) { 
	    	if (appProcess.pid == pid) {   
	    	    if (!appProcess.processName.equals(context.
	    	    		getApplicationInfo().processName)){ //非应用主进程
	    	    	return false;  
	    	    }else{ //应用主进程
	    	    	return true;
	    	    } 
	    	} 
	    }  
	    return true;
	}
	
	public static String readFile(InputStream is) {
		ByteArrayOutputStream baos = null;
		try {
			byte[] buffer = new byte[1024];
			int readBytes = is.read(buffer);
			baos = new ByteArrayOutputStream(1024);
			while (0 < readBytes) {
				baos.write(buffer, 0, readBytes);
				readBytes = is.read(buffer);
			}
			String s = baos.toString();
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

		return "";
	}
	
}
