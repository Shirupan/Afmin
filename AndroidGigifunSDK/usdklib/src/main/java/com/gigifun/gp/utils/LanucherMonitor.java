/**   
 * 文件名：AppsFlyerLanucherMonitor.java</br>
 * 描述： </br>
 * 开发人员：杜逸平 </br>
 * 创建时间： 2014-9-28
 */ 

package com.gigifun.gp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import kr.co.namee.permissiongen.PermissionSuccess;


/** 
 * 类名: AppsFlyerLanucherMonitor</br>
 * 描述: </br>
 * 发布版本号：</br>
 */

public class LanucherMonitor {
	
    /**   
     * 描述: 安装追踪</br>
     * 开发人员：tim</br>
     * 创建时间：2014-12-11</br>
     * @param context  
     */


	private static LanucherMonitor instance = null;
	private static Activity mActivity;
	private SharedPreferences preferences;
	public static LanucherMonitor LanucherInitialize(Activity context) {
		mActivity =context;
		if (instance == null) {
			synchronized (LanucherMonitor.class){
				if (instance == null) {
					instance = new LanucherMonitor(context);
				}
			}
		}
		return instance;
	}


	@PermissionSuccess(requestCode = 100)
	public static LanucherMonitor getInstance() {

		if (instance == null) {
			synchronized (LanucherMonitor.class){
				if (instance == null) {
					LanucherInitialize(mActivity);
				}
			}
		}
		return instance;
	}



	public LanucherMonitor(Activity context){
		//AF追踪打开
		mActivity=context;
		AppsFlyerLib.getInstance().setImeiData(getIMEI(context));
		AppsFlyerLib.getInstance().setAndroidIdData(getAndroidId(mActivity));
		AppsFlyerLib.getInstance().startTracking(mActivity.getApplication(),"yr67ZUfGQowVmHZND534We");

	}






    /** 
     * 方法名: </br>
     * 详述: Purchase追踪</br>
     * 开发人员：tim</br>
     * 创建时间：2014-12-11</br>
     */ 
    public void payTrack(Context context,String userID,String money,String paytype){
    	
    	Map<String, Object> eventValue = new HashMap<String, Object>();
    	eventValue.put(AFInAppEventParameterName.REVENUE,money);
		LogUtil.k("payTrack==="+money);
    	AppsFlyerLib.getInstance().trackEvent(context,"Purchase",eventValue);
    	
    	
//    	//Facebook追踪支付
    	AppEventsLogger logger=AppEventsLogger.newLogger(context);
    	logger.logPurchase(new BigDecimal(String.valueOf(money)), Currency.getInstance("USD"));

    }
    
    
  
    /** 
     * 方法名:KOC统计 </br>
     * 详述: Login追踪</br>
     * 开发人员：tim</br>
     * 创建时间：2014-12-11</br>
     */ 
    public  void loginTrack(Context context,String userID,String loginType){

    	//AF追踪登录
    	Map<String, Object> eventValue = new HashMap<String, Object>();
    	AppsFlyerLib.getInstance().trackEvent(context,"Login",eventValue);
		LogUtil.k("appflyer登录name="+userID);
    	//Facebook追踪登录
    	AppEventsLogger logger=AppEventsLogger.newLogger(context);
        logger.logEvent("name");
   	    logger.logEvent(UgameUtil.getInstance().EVENT_NAME_COMPLETED_LOGIN);

    }
    
   
    
    /** 
     * 方法名: </br>
     * 详述: Registration追踪</br>
     * 开发人员：tim</br>
     * 创建时间：2014-12-11</br>
     */ 
    public  void registrationTrack(Context context,String userID,String registrationType){
    	//注册追踪

    	Map<String, Object> eventValue = new HashMap<String, Object>();
    	AppsFlyerLib.getInstance().trackEvent(context,"Registration",eventValue);
		LogUtil.k("appflyer注册name="+userID);
    	
    	//Facebook注册追踪
    	AppEventsLogger logger=AppEventsLogger.newLogger(context);
    	logger.logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION);
		

    }

	public String getAndroidId(Context context)
	{
		String mAndroidId = android.provider.Settings.Secure.getString(context.getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);
		if (mAndroidId == null)
		{
			mAndroidId = android.provider.Settings.System.getString(context.getContentResolver(),
					android.provider.Settings.System.ANDROID_ID);
		}
		return mAndroidId;
	}

	private String getIMEI(Context context) {

		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		PackageManager pm = context.getPackageManager();
		boolean permission = (PackageManager.PERMISSION_GRANTED ==
				pm.checkPermission(Manifest.permission.READ_PHONE_STATE, context.getPackageName()));

		if(permission){
			return telephonyManager.getDeviceId();
		}else{
			return "";
		}
		//String imei = telephonyManager.getDeviceId();

		//return imei;
	}

}
