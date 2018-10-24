package com.gigifun.gp.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.ads.conversiontracking.AdWordsAutomatedUsageReporter;
import com.google.ads.conversiontracking.AdWordsConversionReporter;
import com.gigifun.gp.UgamePay;


import com.gigifun.gp.listener.IFuntionCheck;
import com.gigifun.gp.listener.OnInitListener;
import com.gigifun.gp.listener.OnUpGradeListener;
import com.gigifun.gp.service.FloatViewService;
import com.gigifun.gp.ui.UpGradeDialog;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import okhttp3.Call;

/**
 * 开发人员： 谁抢了我的飞宇 </br>
 * QQ：460543600</br>
 */
@SuppressLint("SimpleDateFormat")
public class UgameUtil {

    private static SharedPreferences preferences;

    public static final int ClOSE_RESULTCODE = 402;

    private static UgameUtil instance = null;

    public static final String PROPER_NAME = "app.properties";

    public String UGAMURL;

    public String GAME_ID;

    public String CLIENT_SECRET;
    //Facebook分配的id
    public String FBAPP_ID;
    //区别googlepay和mol
    public Boolean ISGMPAY;

    //货币单位
    public String CURRENCY_CODE;

    // 登录
    public String LOGINURL;
    //游客登录
    public String GUESTLOG;
    // 注册
    public String REGURL;
    //修改密码
    public String MOTIFYURL;
    //找回密码
    public String RECOVERPWURL;
    //游客绑定账号
    public String BINDURL;
    //绑定邮箱
    public String BINDEMAILURL;
    //绑定facebook
    public String BINDFBURL;

    // 提交地址
    public String SECOND_REQUEST;

    // 正式版key
    public String base64EncodedPublicKey;

    // 查询谷歌订单状态
    public String GOOGLE_ORDER_CHECK;
    public String GOOGLE_PAY;
    //检查功能开关
    public String CHECK_FUNCTION;
    // 查询订单是否提交过服务器
    public String CHECK_ORDER;

    // 正式错误日志接口
    public String ERROR_LOG;

    // 收集手机信息地址
    public String TELPHONE_INFO;

    // Facebook登录
    public String FACEBOOK_BINDING_VERIFY;
    // Facebook绑定 没用的
    public String U_FACEBOOK_BINDING;
    // 存储Facebook数据
    public String FACEBOOK_STORE;

    // 验证当前ip
    public String VERTIFY_IP;

    // Facebook追踪登录
    public String EVENT_NAME_COMPLETED_LOGIN = "fb_mobile_complete_login";

    //Facebook点赞
    public String FACEBOOK_LIKE;

    //Facebook获取分享
    public String FACEBOOK_SHARE;

    //发送分享礼包
    public String FACEBOOK_SEND_GIFT;

    //发送邀请礼包
    public String FACEBOOK_INVITE_GIFT;

    //Facebook 邀请好友
    public String FACEBOOK_INVITE;

    //保存邀请过的好友
    public String FACEBOOK_SAVE_INVITED_FRIENDS;

    //获取Facebook活动数量信息
    public String FACEBOOK_ACTIVITE_QUANTITY;

    //统计第三方接口
//	public  String GET_USER_PAY="http://glog.ugame.com/getuserpaydata.php";
//	public  String GET_USER_PAY="https://sdkapi.iiugame.com/online.php?a=getPayData";
//	//强制弹出框接口
//	public  String GET_UPGRADEWINDOS="http://sdkapi.iiugame.com/online.php?a=sdkUpdate";

    public String GET_USER_PAY;
    //强制弹出框接口
    public String GET_UPGRADEWINDOS;

    //获取显示红点接口
    public String GET_REDPOINT;


    //微信支付
    public String WECHAT_PAY = "http://api.gigifun.com/online.php?a=getPayData";
    //支付宝支付
    public String ALI_PAY = "https://api.gigifun.com/online.php?a=getPayData";

    //获取谷歌key接口
    public String GET_GOOGLE_KEY = "http://api.ugame.com/googlekey.php";


    public String gkey;

    public String GET_LOCALCURRENCY;

    //Facebook 数量
    public int FBCOUNT;

    /**
     * Google Play包名
     */
    public final String GOOGLE_PLAY_PACKAGE = "com.android.vending";

    //支付状态
    public int PAY_STATE = 0;//0支付前或支付完成      1支付中

    public LanucherMonitor lanucherMonitor;

    //Live
    public static String LOSS_BILL_URL;
    public static Context mycontext;



    public static UgameUtil uInitialize(Context context) {
        mycontext = context;
        if (instance == null) {
            synchronized (UgameUtil.class) {
                if (instance == null) {
                    instance = new UgameUtil(context);
                }
            }
        }
        return instance;
    }


    public static UgameUtil getInstance() {

        if (instance == null) {
            synchronized (UgameUtil.class) {
                if (instance == null) {
                    uInitialize(mycontext);
                }
            }
        }
        return instance;
    }

    private UgameUtil() {

    }


    public void clean() {
        instance = null;
    }

    private static OnInitListener onInitListener;

    public void init(Context context, OnInitListener onInitListeners) {
        onInitListener = onInitListeners;
        UgamePay pay1 = new UgamePay(context);
        onInitListener.initGooglePay(pay1);
    }

    private UgameUtil(Context context) {
        preferences = context.getSharedPreferences("googlekey", Context.MODE_PRIVATE);

        LogUtil.d("UgameUtil init＝＝＝＝＝＝＝＝＝＝＝");
        UGAMURL = getDatainfo(context, "UGAME_URL");
        GAME_ID = getDatainfo(context, "UGAME_ID");
        FBAPP_ID = getDatainfo(context, "FACEBOOK_APP_ID");
        CLIENT_SECRET = getDatainfo(context, "UGAME_CLIENT_SECRET");
        base64EncodedPublicKey = getDatainfo(context, "UGAME_GM_KEY");

//		String coin=context.getResources().getString(MResource.getIdByName(context, "string", "currency_code"));
//		CURRENCY_CODE=coin==null?"USD":coin;
        LogUtil.k("UgameUtil,UGAMURL=" + UGAMURL);
        LogUtil.k("UgameUtil,GAME_ID=" + GAME_ID);
        LogUtil.k("UgameUtil,FBAPP_ID=" + FBAPP_ID);
        LogUtil.k("UgameUtil,CLIENT_SECRET=" + CLIENT_SECRET);
        if (GAME_ID == null) {
            UGAMURL = context.getResources().getString(MResource.getIdByName(context, "string", "ugame_url"));
            GAME_ID = context.getResources().getString(MResource.getIdByName(context, "string", "ugame_id"));
            FBAPP_ID = context.getResources().getString(MResource.getIdByName(context, "string", "facebook_app_id"));
            CLIENT_SECRET = context.getResources().getString(MResource.getIdByName(context, "string", "ugame_client_secret"));
            base64EncodedPublicKey = context.getResources().getString(MResource.getIdByName(context, "string", "ugame_gm_key"));
        }

//		getGoogleKey(context, GAME_ID);

        LOGINURL = UGAMURL + "online.php?a=userLogin";
        GUESTLOG = UGAMURL + "online.php?a=visitorLogin";
        REGURL = UGAMURL + "online.php?a=comRegister";
        MOTIFYURL = UGAMURL + "online.php?a=resetPwd";
        RECOVERPWURL = UGAMURL + "online.php?a=findPassword";
        BINDURL = UGAMURL + "online.php?a=vistorBind";
        BINDEMAILURL = UGAMURL + "online.php?a=emailBind";
        BINDFBURL = UGAMURL + "online.php?a=facebookBind";
        TELPHONE_INFO = UGAMURL + "online.php?a=sdkCollection";
        FACEBOOK_BINDING_VERIFY = UGAMURL + "online.php?a=facebook_check";
        GOOGLE_PAY = UGAMURL + "online.php?a=google_pay";
        CHECK_FUNCTION = UGAMURL + "online.php?a=btnStart";
        FACEBOOK_STORE = UGAMURL + "online.php?a=collection";
        FACEBOOK_LIKE = UGAMURL + "online.php?a=like";
        FACEBOOK_SHARE = UGAMURL + "online.php?a=facebookShare";
        FACEBOOK_INVITE = UGAMURL + "online.php?a=inviteList";
        FACEBOOK_SEND_GIFT = UGAMURL + "online.php?a=shareGift";
        FACEBOOK_INVITE_GIFT = UGAMURL + "online.php?a=inviteGift";
        GET_USER_PAY = UGAMURL + "online.php?a=getPayData";
        GET_UPGRADEWINDOS = UGAMURL + "online.php?a=sdkUpdate";
        GET_REDPOINT=UGAMURL+"online.php?a=showRedPoint";

        SECOND_REQUEST = UGAMURL + "android_paycallback.php";
        ERROR_LOG = UGAMURL + "pushlogs.php";
        U_FACEBOOK_BINDING = UGAMURL + "bindfacebook.php";
        FACEBOOK_SAVE_INVITED_FRIENDS = UGAMURL + "fbinvite.php";
        FACEBOOK_ACTIVITE_QUANTITY = UGAMURL + "fbactiveinfo.php";
        LOSS_BILL_URL = UGAMURL + "android_paymentfix.php";
        VERTIFY_IP = UGAMURL + "getlocallist.php";
        GOOGLE_ORDER_CHECK = UGAMURL + "googleverification.php";
        CHECK_ORDER = UGAMURL + "checkgooglepay.php";
        GET_LOCALCURRENCY = UGAMURL + "getlocalcurrency.php";

        //LogUtil.d("UgameUtil end＝＝＝＝＝＝＝＝＝＝＝ "+VERTIFY_IP);
    }


    public void getUpGradeGame(final Context context, final OnUpGradeListener onUpGradeListener) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("Ugameid", GAME_ID);
        map.put("version", getVersionName(context));
        map.put("currentbundleId", context.getPackageName());
        map.put("isios", "0");
        UhttpUtil.post(GET_UPGRADEWINDOS, map, new UcallBack() {

            @Override
            public void onResponse(String response, int arg1) {
                try {
                    changeLang(context);
                    JSONObject jo = new JSONObject(response);
                    String status = jo.optString("Status");
                    String uprDesc = jo.optString("updateDesc");
                    final String urlBuildid = jo.optString("urlbundleid");
                    String isSwitch = jo.optString("switch");
                    String isUpgrade = jo.optString("isUpdate");
                    String adId = jo.optString("gid");
                    String adkey = jo.optString("glabel");
                    AdWordsAutomatedUsageReporter.enableAutomatedUsageReporting(mycontext.getApplicationContext(), adId);
                    if (!"".equals(adId) && !"".equals(adkey)) {
                        AdWordsConversionReporter.reportWithConversionId(mycontext.getApplicationContext(),
                                adId, adkey, "0", true);
                    }
                    if (status.equals("1")) {

                        if (isSwitch.equals("1")) {
                            final UpGradeDialog.Builder builder = new UpGradeDialog.Builder(mycontext);
                            builder.setMessage(uprDesc);
                            builder.setPositiveButton(new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    if (urlBuildid.startsWith("http://")) {
                                        Uri uri = Uri.parse(urlBuildid);
                                        Intent it = new Intent(Intent.ACTION_VIEW, uri);
                                        context.startActivity(it);
                                    } else {
                                        goGooglePlay(context, urlBuildid);
                                    }

                                }
                            });

                            if (isUpgrade.equals("0")) {

                                builder.setCancelButton(new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();

                                    }
                                });
                            }
                            builder.create().show();

                            onUpGradeListener.success();
                        }
                    } else {
                        onUpGradeListener.faile();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                LogUtil.d("response" + response);

            }

            @Override
            public void onError(Call arg0, Exception arg1, int arg2) {
                LogUtil.d("UpGradeGame error" + arg1);

            }
        });

    }


    public static String getVersionName(Context context)//获取版本号
    {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getApplicationContext().getPackageName(),
                    PackageManager.GET_SIGNATURES);
            return info.versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block  
            e.printStackTrace();
            return "";
        }
    }

    public void getKeyhash(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getApplicationContext().getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                LogUtil.d("KeyHash:" + android.util.Base64.encodeToString(md.digest(), android.util.Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


    private static void switchCode(String code, Context context) {
        if (TextUtils.isEmpty(code)) {
            return;
        }

        Toast.makeText(context, code + "", Toast.LENGTH_SHORT).show();
        ;
    }

    /**
     *
     * @param context
     * @return true是横屏，false竖屏
     */
   public boolean isVer(Context context){
       boolean flag=true;
       switch(context.getResources().getConfiguration().orientation){
           case Configuration.ORIENTATION_LANDSCAPE:
               //横屏
               flag=true;
               break;
           case Configuration.ORIENTATION_PORTRAIT:
               //竖屏
               flag=false;
               break;
           default:
               flag=true;
       }
       return flag;
   }

    /*
     * 验证手机号，11位，且必须含全是数字
     */
    public static boolean isMoble(String mobile) {
        return mobile.matches("^\\d{11}$");
    }

    /*
     * 验证是否为空
     */
    public static boolean isNullOrEmpty(String s) {
        return s == null || "".equals(s.trim());
    }

    /**
     * 判断是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否为邮箱
     *
     * @param str
     * @return
     */
    public static boolean isEmail(String str) {
        Pattern pattren = Pattern
                .compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
        return pattren.matcher(str).matches();
    }

    /**
     * Get请求字符串拼接
     *
     * @param map
     * @return
     */
    public static String encodeUrl(Map<String, String> map) {
        if (map == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String key : map.keySet()) {
            if (first) {
                first = false;
                sb.append("?" + key + "=" + map.get(key) + "&");
            } else {
                sb.append(key + "=" + map.get(key) + "&");
            }
        }
        String str = sb.toString();
        return str.substring(0, str.length() - 1);
    }

    public static String spliceParam(Map<String, String> map) {
        String aesParam = "";
        if (map == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();

        for (String key : map.keySet()) {
            sb.append(key + "=" + map.get(key) + "&");
        }
        String str = sb.toString();
        String s = str.substring(0, str.length() - 1);

        //LogUtil.w("请求参数： " + s);
        try {
            AESEncode aes = new AESEncode();
            aesParam = aes.encrypt(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aesParam;
    }

    public static String aesData(String s) {
        String aesParam = "";
        try {
            AESEncode aes = new AESEncode();
            aesParam = aes.encrypt(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // System.out.println("加密后的字符串：" + aesParam);
        return aesParam;
    }


    public static String getPayTime(long timesTmp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = format.format(timesTmp);
        return d;
    }


    public void getGoogleKey(final Context c, String gameid) {
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("gameid", gameid);
        map.put("buildid", c.getPackageName());

        UhttpUtil.post(GET_GOOGLE_KEY, map, new UcallBack() {

            @Override
            public void onResponse(String response, int arg1) {

                base64EncodedPublicKey = getDatainfo(c, "UGAME_GM_KEY");
                UgamePay pay1 = new UgamePay(c);
                onInitListener.initGooglePay(pay1);
                LogUtil.d("GET_GOOGLE_KEY" + gkey);
            }

            @Override
            public void onError(Call arg0, Exception arg1, int arg2) {
                LogUtil.d("getGoogleKey error" + arg1);
                base64EncodedPublicKey = getDatainfo(c, "UGAME_GM_KEY");
                UgamePay pay1 = new UgamePay(c);
                onInitListener.initGooglePay(pay1);

            }
        });

    }

    public static String getPropertiesURL(Context c, String s) {
        String url = null;
        Properties properties = new Properties();
        try {
            properties.load(c.getAssets().open(PROPER_NAME));
            url = properties.getProperty(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getDatainfo(Context c, String s) {
        String msg = null;
        try {
            ApplicationInfo appInfo = c.getPackageManager().getApplicationInfo(c.getPackageName(), PackageManager.GET_META_DATA);

            msg = appInfo.metaData.getString(s);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return msg;
    }


    public void setAutoLoginStauts(Context context, boolean bl) {
        SharedPreferences preferences = context.getSharedPreferences("MyCount", Context.MODE_PRIVATE);
        Editor edit = preferences.edit();
        edit.putBoolean("flag2", bl);
        edit.commit();
    }

    /**
     * 跳转到Google play
     */
    public boolean goGooglePlay(Context context) {
        return goGooglePlay(context, context.getPackageName());
    }


    /**
     * 跳转到Google play评论
     *
     * @param pkgname 包名
     */
    public boolean goGooglePlay(Context context, String pkgname) {
        Uri uri = Uri.parse("market://details?id=" + pkgname);
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        i.setPackage(GOOGLE_PLAY_PACKAGE);
        try {
            context.startActivity(i);
            return true;
        } catch (Exception e) {
            L.w(e.getMessage(), e);
            //Toast.makeText(context, text, duration)
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pkgname)));
        }
        return false;
    }

    //设置字体繁简
    public void changeLang(Context context) {

//        int idByName = MResource.getIdByName(context, "string", "lang");
//        Resources resources = context.getResources();
//            DisplayMetrics dm = resources.getDisplayMetrics();
//            Configuration config = resources.getConfiguration();
//            // 应用用户选择语言
//            config.locale = Locale.TRADITIONAL_CHINESE;
//            resources.updateConfiguration(config, dm);
        //CN ：简体：TW:繁体  EN:英语
        String languageToLoad = context.getResources().getString(MResource.getIdByName(context, "string", "lang"));
        Locale locale = null;
        if ("EN".equals(languageToLoad)) {
            locale = new Locale("en");
            LogUtil.k("字体改为英文");
        }else if("TH".equals(languageToLoad)) {
            locale = new Locale("th");
            LogUtil.k("字体改为泰文");
        } else  {
            locale = new Locale("zh", languageToLoad);
            LogUtil.k("字体改为" + languageToLoad);
        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
    }

    /**
     * 根据view来生成bitmap图片，可用于截图功能
     */

    public static Bitmap getViewBitmap(View v) {

        v.clearFocus(); //

        v.setPressed(false); //

        // 能画缓存就返回false

        boolean willNotCache = v.willNotCacheDrawing();

        v.setWillNotCacheDrawing(false);

        int color = v.getDrawingCacheBackgroundColor();

        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {

            v.destroyDrawingCache();

        }

        v.buildDrawingCache();

        Bitmap cacheBitmap = v.getDrawingCache();

        if (cacheBitmap == null) {

            return null;

        }

        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        // Restore the view

        v.destroyDrawingCache();

        v.setWillNotCacheDrawing(willNotCache);

        v.setDrawingCacheBackgroundColor(color);

        return bitmap;

    }


    /**
     * 保存Bitmap图片为本地文件
     */

    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "uPic");

        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
    }


    /**
     * 上传设备id
     *
     * @param userName
     * @param pwd
     * @param uid
     */
    public void getAdvertisingId(final Context context, final String userName, final String pwd, final String uid) {

        new Thread(new Runnable() {
            @SuppressLint("MissingPermission")
            public void run() {
                Info adInfo = null;
                try {
                    adInfo = AdvertisingIdClient.getAdvertisingIdInfo(mycontext);
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                PackageManager pm = context.getPackageManager();
                String deviceId = "";
                boolean permission = (PackageManager.PERMISSION_GRANTED ==
                        pm.checkPermission(Manifest.permission.READ_PHONE_STATE, context.getPackageName()));

                if (permission) {
                    deviceId = tel.getDeviceId();
                }

                WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                String androidId = Secure.getString(context.getContentResolver(),
                        Secure.ANDROID_ID);
                //String macAddress = wifi.getConnectionInfo().getMacAddress();
                String version = android.os.Build.VERSION.RELEASE;
//				SharedPreferences preferences = context.getSharedPreferences("MyCount",0);
//				if ( preferences.getString("android_device_id", "").equals(deviceId)
//					/*&& preferences.getString("android_mac_address", "").equals(macAddress)*/) {
//					Editor editor = preferences.edit();
//					editor.putString("username", userName);
//					editor.putString("password", aes(pwd));
//					editor.commit();
//					return;
//				}
                /*获取当前MINI*/
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

                @SuppressLint("MissingPermission") String imei = tm.getDeviceId();

                HashMap<String, String> map = new HashMap<String, String>();
                Editor editor = preferences.edit();
                if (adInfo != null) {
                    String advertisingId = adInfo.getId();
                    if (advertisingId != null) {
                        map.put("google_advertising_id", advertisingId);
                        editor.putString("google_advertising_id", advertisingId);
                    }
                }

                if (deviceId != null) {
                    map.put("devicetype", deviceId);
                    editor.putString("devicetype", deviceId);
                }
                if (androidId != null) {
                    map.put("android_id ", androidId);
                    editor.putString("android_id ", androidId);
                }
//				if(macAddress!=null){
//					map.put("android_mac_address", macAddress);
//					editor.putString("android_mac_address", macAddress);
//				}
//				if(userName!=null){
//					map.put("username", userName);
//					editor.putString("username", userName);
//				}
//				if(uid!=null){
//					map.put("userid", uid);
//					editor.putString("userid", uid);
//
//				}
                if (GAME_ID != null) {
                    map.put("Ugameid", GAME_ID);
                    editor.putString("Ugameid", GAME_ID);
                }
                map.put("Sdktype", "0");
                //系统版本
                if (version != null) {
                    map.put("osversion", version);
                    editor.putString("version", version);
                }
                editor.putString("password", aes(pwd));
                if (imei != null) {
                    map.put("imei", imei);
                    editor.putString("imei", imei);
                }
                editor.commit();
                UhttpUtil.post(TELPHONE_INFO, map, new UcallBack() {

                    @Override
                    public void onResponse(String response, int arg1) {
                        // TODO Auto-generated method stub
                        LogUtil.d("EGU-getAvtId-TELPHONE_INFO=========" + response);
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1, int arg2) {
                        // TODO Auto-generated method stub
                        LogUtil.d("EGU-getAvtId-TELPHONE_INFO=========error");
                    }
                });


            }
        }).start();


    }

    private String aes(String coorderid) {

        try {
            AESEncode aes = new AESEncode();
            return aes.encrypt(coorderid);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }


    private String dec(String enc) {
        try {
            AESEncode aes = new AESEncode();
            return aes.decrypt(enc);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }




    private FloatViewService floatViewService;

    public void checkFuction(final String serverId, String roleId, String sdkuId,final String sPcText,final IFuntionCheck mFCheck, final FloatViewService floatViewService) {


        final SharedPreferences preferences = mycontext.getSharedPreferences("LoginCount", Context.MODE_PRIVATE);
        String paysdkUid = preferences.getString("paysdkUid", "");
        String payroleId = preferences.getString("payroleId", "");
//        Editor edit = preferences.edit();
//        edit.putBoolean("isChoose",true);
//        edit.commit();

        LogUtil.k("paysdkuid=" + paysdkUid);
        LogUtil.k("payroleId=" + payroleId);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("Ugamekey", UgameUtil.getInstance().CLIENT_SECRET);
        map.put("Ugameid", UgameUtil.getInstance().GAME_ID);
//		map.put("Ugameid", "");
        map.put("Serverid", serverId);
        map.put("Uid", sdkuId);
        map.put("Roleid", roleId);
//		map.put("Uid",paysdkUid);
//		map.put("Roleid",payroleId);
        map.put("Sdktype", "0");
        map.put("Version", getVersionName(mycontext));
        map.put("currentbundleId", mycontext.getPackageName());

        UhttpUtil.post(UgameUtil.getInstance().CHECK_FUNCTION, map, new UcallBack() {
            @Override
            public void onResponse(String response, int arg1) {
                LogUtil.k("gameutil-chekfunction====" + response);
                JSONObject obj;
                try {
                    obj = new JSONObject(response);
                    String status = obj.getString("Status");
//					if (status == "1"){
                    if ("1".equals(status)) {
                        String fbflag = obj.getString("fbflag");
                        String starflag = obj.getString("5starflag");
                        String payflag = obj.getString("paymentflag");
                        String isRelax=obj.getString("relaxflag");

                        mFCheck.checkFunctionOpen(fbflag, starflag, payflag);


                        SharedPreferences.Editor edit = preferences.edit();
                        edit.putString("floatStatus", "0");
                        edit.putString("fbflag", fbflag);
                        edit.putString("5starflag", starflag);
                        edit.putString("paymentflag", payflag);
                        edit.putString("serverId",serverId);
                        edit.putString("sPcText",sPcText);
                        edit.putString("isRelax",isRelax);


                        edit.commit();
                        //floatViewService.showFloat();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Call arg0, Exception arg1, int arg2) {

            }
        });
    }
}