package com.gigifun.gp.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appsflyer.AppsFlyerLib;
import com.gigifun.gp.UgameSDK;
import com.gigifun.gp.db.DatabaseManager;

import com.gigifun.gp.listener.OnFloatLintener;
import com.gigifun.gp.listener.OnLoginListener;
import com.gigifun.gp.model.FacebookEntity;
import com.gigifun.gp.utils.AESEncode;
import com.gigifun.gp.utils.ButtonUtil;
import com.gigifun.gp.utils.UcallBack;
import com.gigifun.gp.utils.UgameUtil;
import com.gigifun.gp.utils.UhttpUtil;
import com.gigifun.gp.utils.LanucherMonitor;
import com.gigifun.gp.utils.LogUtil;
import com.gigifun.gp.utils.MResource;
import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;


/**
 * Created by tim on 16/8/29.
 */
public class LoginDialog implements OnClickListener {
    private static Activity mActivity;
    private static Dialog mDialog;
    private OnFloatLintener onFloatLintener;
    public static final int LOGIN_REQUESTCODE = 1;
    public static int LOGIN_RESULTCODE = 2;
    private EditText userNameEt;
    private EditText passwordEt;
    private Button rbPasswordBtn;
    private Button autoBtn;
    private Button loginBtn;
    private ImageView settingImg;
    private TextView forgetPwTv;
    private TextView meiyouzhanghaoTv;

    private TextView registeredTv;
    private SharedPreferences preferences;
    private boolean flag1;
    private boolean flag2;
    private boolean flag3;
    private String Login_name;
    private String Login_password;
    private LinearLayout layoutLogin;
    private Message msg;
    private String GAME_ID;
    private String FBAPP_ID;
    private String CLIENT_SECRET;
    private String sdkUid;
    private String facebookUrl;

    private PendingAction pendingAction = PendingAction.NONE;
    boolean enableButtons;

    private static OnLoginListener mOnLoginListener;
    private ProgressWheel progressWheel;
    private String deviceId;
    private String zeName = "";

    private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }

    //加上这句会 java.lang.NullPointerException
//at com.gigifun.gp.utils.MResource.getIdByName(MResource.java:16)   无解
//    BindAccountDialog bindAccountDialog = new BindAccountDialog(mActivity);
    public LoginDialog(Activity activity, OnLoginListener onLoginListener, OnFloatLintener onFloatLintener) {

        this.mOnLoginListener = onLoginListener;
        this.mActivity = activity;
        this.onFloatLintener = onFloatLintener;
        if (this.mOnLoginListener == null) {
            LogUtil.d("mOnLoginListener 监听失败");
            return;
        }

        DatabaseManager.initializeInstance(activity);
        initUI();
        initData();
    }

    public void initData() {
//        preferences = mActivity.getSharedPreferences("MyCount", Context.MODE_PRIVATE);
        GAME_ID = UgameUtil.getInstance().GAME_ID;
        FBAPP_ID = UgameUtil.getInstance().FBAPP_ID;
        CLIENT_SECRET = UgameUtil.getInstance().CLIENT_SECRET;
        LogUtil.d("GAME_ID:" + GAME_ID + " CLIENT_SECRET:" + CLIENT_SECRET);
//        TelephonyManager tm = (TelephonyManager) mActivity.getSystemService(mActivity.TELEPHONY_SERVICE);
//        deviceId = tm.getDeviceId();
        deviceId = getDeviceId(mActivity);
//        SharedPreferences share=mActivity.getSharedPreferences("MyCount",Activity.MODE_WORLD_READABLE);

//        Login_name = preferences.getString("userloginname","");
//        Login_password = preferences.getString("userloginpassword","");
        //vertifyIP();
        //getlocalcurrency();
        //initAutoLogin();
    }

    @SuppressLint("MissingPermission")
    public static String getDeviceId(Context context) {
        String id;
        //android.telephony.TelephonyManager
        TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephony.getDeviceId() != null) {
            id = mTelephony.getDeviceId();
        } else {
            //android.provider.Settings;
            id = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return id;
    }

    //设置字体繁简
    private static void changeLang(Context context) {

        String languageToLoad = context.getResources().getString(MResource.getIdByName(context, "string", "lang"));
        Locale locale = null;
        if ("EN".equals(languageToLoad)) {
            locale = new Locale("en");
            LogUtil.k("字体改为英文");
        } else if ("TH".equals(languageToLoad)) {
            locale = new Locale("th");
            LogUtil.k("字体改为泰文");
        } else {
            locale = new Locale("zh", languageToLoad);
            LogUtil.k("字体改为" + languageToLoad);
        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
    }

    public void initUI() {
//        WindowManager wm = mActivity.getWindowManager();
//        Display display = wm.getDefaultDisplay();
//        android.view.WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
//        lp.width = display.getWidth();
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//       Log.d("width======",""+lp.width);
//       Log.d("height======",""+lp.height);
//        DisplayMetrics dm = new DisplayMetrics();
//        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int densityDpi = dm.densityDpi;
//        Log.d("=====", "densityDpi=" + densityDpi);
        preferences = mActivity.getSharedPreferences("LoginCount", Context.MODE_PRIVATE);
        //  UgameSDK.getInstance().changeLang(mActivity);
        UgameUtil.getInstance().changeLang(mActivity);
        // changeLang(mActivity);
        mDialog = new Dialog(mActivity, MResource.getIdByName(mActivity, "style", "custom_dialog"));
        mDialog.getWindow().getAttributes().windowAnimations = MResource.getIdByName(mActivity, "style", "dialogAnim");
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

//        mDialog.setContentView(MResource.getIdByName(mActivity, "layout", "dialog_login_ver"));
//        switch(mActivity.getResources().getConfiguration().orientation){
//            //横屏
//            case Configuration.ORIENTATION_LANDSCAPE:
//                mDialog.setContentView(MResource.getIdByName(mActivity, "layout", "dialog_login"));
//                LogUtil.k("横屏----1");
//                break;
//            //竖屏
//            case Configuration.ORIENTATION_PORTRAIT:
//                mDialog.setContentView(MResource.getIdByName(mActivity, "layout", "dialog_login_ver"));
//                LogUtil.k("竖屏----");
//                break;
//            default:
//                mDialog.setContentView(MResource.getIdByName(mActivity, "layout", "dialog_login"));
//                LogUtil.k("横屏----");
//
//        }
        //判断横竖屏
        boolean isLandscape = UgameUtil.getInstance().isVer(mActivity);
        if (isLandscape) {
            mDialog.setContentView(MResource.getIdByName(mActivity, "layout", "dialog_login"));
            LogUtil.d("Landscape");
        } else {
            mDialog.setContentView(MResource.getIdByName(mActivity, "layout", "dialog_login_ver"));
            LogUtil.d("Portrait");
        }
        mDialog.setCancelable(false);

        progressWheel = (ProgressWheel) mDialog.findViewById(MResource.getIdByName(mActivity, "id", "progress_wheel"));
        userNameEt = (EditText) mDialog.findViewById(MResource.getIdByName(mActivity, "id", "edt_username"));
        passwordEt = (EditText) mDialog.findViewById(MResource.getIdByName(mActivity, "id", "edt_psw"));
        loginBtn = (Button) mDialog.findViewById(MResource.getIdByName(mActivity, "id", "btn_login"));
        registeredTv = (TextView) mDialog.findViewById(MResource.getIdByName(mActivity, "id", "tv_toregist"));
        layoutLogin = (LinearLayout) mDialog.findViewById(MResource.getIdByName(mActivity, "id", "layout_login"));
        //设置
        settingImg = (ImageView) mDialog.findViewById(MResource.getIdByName(mActivity, "id", "img_setting"));
        //忘记密码
        forgetPwTv = (TextView) mDialog.findViewById(MResource.getIdByName(mActivity, "id", "tv_forgetpw"));

//        meiyouzhanghaoTv = (TextView)mDialog.findViewById(MResource.getIdByName(mActivity, "id", "tv_meiyouzhanghao"));
//         meiyouzhanghaoTv = (TextView)mDialog.findViewById(R.id.tv_meiyouzhanghao);
        registeredTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        registeredTv.getPaint().setAntiAlias(true);//抗锯齿
        //设置英文没间隔
        userNameEt.setTypeface(Typeface.SANS_SERIF);
        passwordEt.setTypeface(Typeface.SANS_SERIF);

        loginBtn.setOnClickListener(this);
        registeredTv.setOnClickListener(this);
        settingImg.setOnClickListener(this);
        forgetPwTv.setOnClickListener(this);
        mDialog.show();
//        LogUtil.k("OnFloatLintener=="+ onFloatLintener);
//        //xuanfu
//        onFloatLintener.hideFt();
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("floatStatus", "1");
        edit.commit();
        setUserMsg();

    }

    public void setUserMsg() {
//        SharedPreferences preferences = mActivity.getSharedPreferences("LoginCount", Context.MODE_PRIVATE);
        String key = preferences.getString("key", "");
        //判断key,如果1显示登录密码，2显示注册密码，3显示游客登录密码
        if ("1".equals(key)) {
            userNameEt.setText(preferences.getString("userloginname", ""));
            passwordEt.setText(preferences.getString("userloginpassword", ""));
        } else if ("2".equals(key)) {
            userNameEt.setText(preferences.getString("registname", ""));
            passwordEt.setText(preferences.getString("registpassword", ""));
        } else if ("3".equals(key)) {
            userNameEt.setText(preferences.getString("zeName", ""));
            passwordEt.setText(preferences.getString("guestpassword", ""));
        } else if ("4".equals(key)) {
            userNameEt.setText(preferences.getString("fbbindname", ""));
            passwordEt.setText(preferences.getString("fbbindpassword", ""));
        }
    }

    //验证facebook账号是否绑定过账号
    private void verifyFacebookBinding(FacebookEntity facebook) {
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("Ugameid", facebook.getClientId());
//        map.put("Client_secret", facebook.getClientSecret());
        map.put("id", facebook.getId());
        map.put("Sdktype", "0");
        if (null != facebook.getEmail()) {
            map.put("email", facebook.getEmail());
        }
//        if(null!=facebook.getFirstName()){
//            map.put("first_name", facebook.getFirstName());
//        }
        if (null != facebook.getGender()) {
            map.put("gender", facebook.getGender());
        }
//        if(null!=facebook.getLastName()){
//            map.put("last_name", facebook.getLastName());
//        }
        if (null != facebook.getLink()) {
            map.put("link", facebook.getLink());
        }
        if (null != facebook.getLocale()) {
            map.put("locale", facebook.getLocale());
        }
        if (null != facebook.getName()) {
            map.put("name", facebook.getName());
        }
//        if(null!=facebook.getTimezone()){
//            map.put("timezone", facebook.getTimezone());
//        }
        if (null != facebook.getVerified()) {
            map.put("verified", facebook.getVerified());
        }
//        if(null!=facebook.getUpdatedTime()){
//            map.put("updated_time", facebook.getUpdatedTime());
//        }


        UhttpUtil.post(UgameUtil.getInstance().FACEBOOK_STORE, map, new UcallBack() {

            @Override
            public void onResponse(String response, int arg1) {
                LogUtil.d("FACEBOOK_STORE" + response);

            }

            @Override
            public void onError(Call arg0, Exception arg1, int arg2) {
                LogUtil.d("FACEBOOK_STORE error" + arg1);

            }
        });


    }

    @Override
    public void onClick(View v) {

        if (v.getId() == MResource.getIdByName(mActivity, "id", "tv_forgetpw")) {
            LogUtil.k("logindialog===" + "点击忘记密码");
            mDialog.dismiss();
            new RecoverPwDialog(mActivity, new RecoverPwDialog.OnRecoverPwListener() {
                @Override
                public void notifyLoginDialogShow() {
                    mDialog.show();
                }
            });
        } else if (v.getId() == MResource.getIdByName(mActivity, "id", "img_setting")) {
            //点击设置
            LogUtil.k("logindialog===" + "点击设置");

            mDialog.dismiss();
            new SettingDialog(mActivity, new SettingDialog.OnSettingListener() {

                @Override
                public void notifyLoginDialogShow() {
                    mDialog.show();
                }
            });

        } else if (v.getId() == MResource.getIdByName(mActivity, "id", "img_guest")) {
            if (ButtonUtil.isFastDoubleClick(v.getId())) {
                return;
            }


//            preferences = mActivity.getSharedPreferences("LoginCount", Activity.MODE_PRIVATE);
            String sdkuid = preferences.getString("sdkUid", "");

//if (判断sdkuid是否为空){
//          不为空，
//            if(用户名是否为空){
//                空，提示绑定
//                        进入游戏，登录，保存sdkuid
//                        进行绑定，保存用户名
//            }else{
//                提示用这个用户名登录
//            }
//}else{
//     第一次登录，保存sdkuid，保存用户名
//   }
            if (!"".equals(sdkuid)) {
//                share = mActivity.getSharedPreferences("LoginCount", Activity.MODE_PRIVATE);
                zeName = preferences.getString("zeName", "");

                //判断用户名是否为空
                if ("".equals(zeName)) {
                    //弹出dialog,
                    dialog();
                    LogUtil.k("username为空，进入提醒绑定窗口");
                } else {
                    //提示用这个账号登录
                    LogUtil.k("username不为空");
                    hadBindDialog();
                    userNameEt.setText(zeName);
                }
            } else {
                //   showProgressWheel();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Ugameid", GAME_ID);
                map.put("Ugamekey", CLIENT_SECRET);
                map.put("Uuid", deviceId);
//                map.put("Uuid", "11115");//kong
                map.put("Sdktype", "0");
                UhttpUtil.post(UgameUtil.getInstance().GUESTLOG, map, new UcallBack() {

                    @Override
                    public void onResponse(String response, int arg1) {
                        parseGuestJson(response);
                        LogUtil.k("游客登录=" + response);
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1, int arg2) {
                        // TODO Auto-generated method stub
                        loginfailed(arg1.toString());
                    }
                });

            }


        } else if (v.getId() == MResource.getIdByName(mActivity, "id", "btn_select2")) {

//            if (flag3) {
//                autoBtn.setBackgroundResource(MResource.getIdByName(mActivity, "drawable", "check_normal"));
//                flag2 = false;
//                flag3 = false;
//                preferences.edit().putBoolean("flag3", false).commit();
//            } else {
//                rbPasswordBtn.setBackgroundResource(MResource.getIdByName(mActivity, "drawable", "check_click"));
//                autoBtn.setBackgroundResource(MResource.getIdByName(mActivity, "drawable", "check_click"));
//                flag1 = true;
//                flag2 = true;
//                flag3 = true;
//                preferences.edit().putBoolean("flag3", true).commit();
//            }
        } else if (v.getId() == MResource.getIdByName(mActivity, "id", "btn_login")) {
            if (ButtonUtil.isFastDoubleClick(v.getId())) {
                return;
            }

            Login_name = userNameEt.getText().toString().trim();
            Login_password = passwordEt.getText().toString().trim();
//            preferences = mActivity.getSharedPreferences("LoginCount", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString("userloginname", Login_name);
            edit.putString("userloginpassword", Login_password);
            edit.putString("key", "1");
            edit.commit();

            if (UgameUtil.isNullOrEmpty(Login_name) | UgameUtil.isNullOrEmpty(Login_password)) {
                Toast.makeText(mActivity, MResource.getIdByName(mActivity, "string", "username_pwd_not_null"), Toast.LENGTH_SHORT)
                        .show();
                return;
            }


            // showProgressWheel();

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Ugameid", GAME_ID);
            map.put("Ugamekey", CLIENT_SECRET);
            map.put("Username", Login_name);
            map.put("Password", aes(Login_password));
            UhttpUtil.post(UgameUtil.getInstance().LOGINURL, map, new UcallBack() {

                @Override
                public void onResponse(String response, int arg1) {
                    parseLoginJosn(response);

                }

                @Override
                public void onError(Call arg0, Exception arg1, int arg2) {
                    // TODO Auto-generated method stub
                    loginfailed(arg1.toString());
                }
            });
        } else if (v.getId() == MResource.getIdByName(mActivity, "id", "tv_toregist")) {
            if (ButtonUtil.isFastDoubleClick(v.getId())) {
                return;
            }
            mDialog.dismiss();
            new RegistDialog(mActivity, new RegistDialog.OnRegistListener() {
                @Override
                public void onRegistSuccessful(String userName, String passWord, String sdkUid) {
                    LogUtil.k("LoginDialog-RegistDilog===" + "successful");
                    requestHD(userName, passWord, sdkUid);//获取avtertisingId
                    addAppFly(sdkUid);
                    mOnLoginListener.onLoginSuccessful(sdkUid);

                }

                @Override
                public void notifyLoginDialogShow() {
                    LogUtil.k("LoginDialog-RegistDilog" + "SHOW");
                    mDialog.show();
                }
            });
        } else if (v.getId() == MResource.getIdByName(mActivity, "id", "img_facebook")) {
            if (ButtonUtil.isFastDoubleClick(v.getId())) {
                return;
            }
            LogUtil.d("facebook  login ---------------");
            if (AccessToken.getCurrentAccessToken() != null && !AccessToken.getCurrentAccessToken().isExpired()) {
                //已经登录过
                showProgressWheel();
                String facebookUrl = "https://graph.facebook.com/me?fields=token_for_business&access_token="
                        + AccessToken.getCurrentAccessToken().getToken() + "";

                LogUtil.k("fb登录的url=" + facebookUrl);
                UhttpUtil.get(facebookUrl, new UcallBack() {
                    @Override
                    public void onResponse(String response, int arg1) {
                        mDialog.dismiss();
                        LogUtil.k("fb登录的response=" + response);
                        parsesecLogFbJson(response);
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1, int arg2) {
                        closeProgressWheel();
                        LogUtil.k("facebook第二次登录 onError,exception" + arg1);
                        Toast.makeText(mActivity, MResource.getIdByName(mActivity, "string", "network_error"), Toast.LENGTH_SHORT)
                                .show();
                    }
                });

            } else {
                //第一次登录
                //执行登录操作
                LoginManager.getInstance().logInWithReadPermissions(mActivity, Arrays.asList("email", "public_profile", "user_friends"));
                //注册callback
                LogUtil.k("注册facebook callback");
                LoginManager.getInstance().registerCallback(UgameSDK.callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
//                        保存FB用户信息
                        // updateUI();
                        showProgressWheel();
                        loginForFacebook();
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {
                        // closeProgressWheel();
                        LogUtil.d("facebook login error" + error);
                        Toast.makeText(mActivity, "facebook error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void getfBUserInfo() {

//        boolean isSava = preferences.getBoolean("isfbInfo", true);
//
//        if (isSava) {
        //preferences.edit().putBoolean("isfbInfo", false).commit();
        // 获取基本文本信息
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject user, GraphResponse response) {
                if (user != null) {
                    LogUtil.d("LOginDialog-->getfBUserInfo,user========" + user);
                    Message msg = handler.obtainMessage();
                    msg.obj = user;
                    msg.what = 4;
                    handler.sendMessage(msg);
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,first_name,last_name,link,email,gender,locale,timezone,verified,updated_time");
        request.setParameters(parameters);
        request.executeAsync();
//        }
    }


    private void updateUI() {
        enableButtons = AccessToken.getCurrentAccessToken() != null;
        LogUtil.d("enableButtons" + enableButtons);
        if (enableButtons) {
            getfBUserInfo();
        } else {
            Toast.makeText(mActivity, MResource.getIdByName(mActivity, "string", "network_error"), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    protected void hadBindDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        int idByName = MResource.getIdByName(mActivity, "string", "hadbindcontent");
        String hadBind = mActivity.getString(idByName);
//        builder.setMessage(com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "hadbindcontent"));
        builder.setMessage(hadBind + zeName);
        builder.setTitle(null);
        builder.setPositiveButton(com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "OK"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    //弹出提示框
    protected void dialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage(com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "askforbindcontent"));
        builder.setTitle(null);
//        builder.setTitle(com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "askforbind"));
        builder.setPositiveButton(com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "determine"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDialog.dismiss();

                new BindAccountDialog(mActivity, new BindAccountDialog.OnBindAccountListener() {
                    @Override
                    public void notifyLoginDialogShow() {
                        LogUtil.k("LoginDialog-RegistDilog" + "SHOW");
//                        mDialog.show();
                        new LoginDialog(mActivity, new OnLoginListener() {
                            @Override
                            public void onLoginSuccessful(String sdkUid) {

                            }

                            @Override
                            public void onLoginFailed(String reason) {

                            }
                        }, onFloatLintener);
                    }
                });
                builder.create().dismiss();
            }
        });

        builder.setNegativeButton(com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "cancel"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDialog.dismiss();
                LogUtil.k("提醒后进入游戏");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Ugameid", GAME_ID);
                map.put("Ugamekey", CLIENT_SECRET);
                map.put("Uuid", deviceId);
//                map.put("Uuid", "11115");//kong
                map.put("Sdktype", "0");
                UhttpUtil.post(UgameUtil.getInstance().GUESTLOG, map, new UcallBack() {

                    @Override
                    public void onResponse(String response, int arg1) {
                        parseGuestJson(response);
                        LogUtil.k("游客登录=" + response);
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1, int arg2) {
                        // TODO Auto-generated method stub
                        loginfailed(arg1.toString());
                    }
                });

            }
        });
        builder.create().show();
    }

    private void parseGuestJson(String json) {
        if (TextUtils.isEmpty(json)) {
            closeProgressWheel();
            handler.sendEmptyMessage(203);
            return;
        }
        JSONObject obj;
        try {
            obj = new JSONObject(json);
            LogUtil.d("parseLoginJosn:" + json);
            String status = obj.getString("Status");
            String code = obj.getString("Code");
            String isNew = obj.getString("Isnew");
            String sdkUid = obj.getString("Sdkuid");
            String zeName = obj.getString("Username");
            SharedPreferences preferences = mActivity.getSharedPreferences("LoginCount", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString("zeName", zeName);
            edit.commit();
            addAppFly(sdkUid);
            mOnLoginListener.onLoginSuccessful(sdkUid);
            if ("1".equals(status)) {
                mDialog.dismiss();
                //进入游客模式
                Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "visitormode"), Toast.LENGTH_SHORT).show();
                edit.putString("sdkUid", sdkUid);
                edit.commit();
//                //xuanfu
//                onFloatLintener.showFt();
                LanucherMonitor.getInstance().loginTrack(mActivity.getApplicationContext(), sdkUid, "Ugame");
            } else if ("0".equals(status)) {
                if ("104".equals(code)) {
                    msg.what = 101;
                    handler.sendMessage(msg);
                } else if ("110".equals(code)) {
                    msg.what = 102;
                    handler.sendMessage(msg);
                } else if ("111".equals(code) || "148".equals(code)) {
                    msg.what = 103;
                    handler.sendMessage(msg);
                } else if ("151".equals(code)) {
                    msg.what = 104;
                    handler.sendMessage(msg);
                } else if ("153".equals(code)) {
                    msg.what = 105;
                    handler.sendMessage(msg);
                } else if ("155".equals(code)) {
                    msg.what = 106;
                    handler.sendMessage(msg);
                } else if ("156".equals(code) || "163".equals(code)) {
                    msg.what = 107;
                    handler.sendMessage(msg);
                } else if ("157".equals(code)) {
                    msg.what = 108;
                    handler.sendMessage(msg);
                } else if ("158".equals(code)) {
                    msg.what = 109;
                    handler.sendMessage(msg);
                } else if ("161".equals(code)) {
                    msg.what = 111;
                    handler.sendMessage(msg);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            closeProgressWheel();
        }

    }

//    private void parseGuestJson(String json) {
//        if (TextUtils.isEmpty(json)) {
//            closeProgressWheel();
//            handler.sendEmptyMessage(203);
//            return;
//        }
//        JSONObject obj;
//        try {
//            obj = new JSONObject(json);
//            LogUtil.d("parseLoginJosn:" + json);
//            String status = obj.getString("Status");
//            String code = obj.getString("Code");
//            String isNew = obj.getString("Isnew");
//            String sdkUid = obj.getString("Sdkuid");
//            String userName = obj.getString("Username");
//            mOnLoginListener.onLoginSuccessful(sdkUid);
//            if ("1".equals(status)){
//                mDialog.dismiss();
//                if ("1".equals(isNew)) {
//                    //进入游客模式
//                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "visitormode"), Toast.LENGTH_SHORT).show();
//                    SharedPreferences preferences = mActivity.getSharedPreferences("LoginCount", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor edit = preferences.edit();
//                    edit.putString("sdkUid", sdkUid);
//                    edit.commit();
//                }else {
//                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "visitormode"), Toast.LENGTH_SHORT).show();
//                }
//            }else if ("0".equals(status)){
//                if ("104".equals(code)) {
//                    msg.what = 101;
//                    handler.sendMessage(msg);
//                } else if ("110".equals(code)) {
//                    msg.what = 102;
//                    handler.sendMessage(msg);
//                } else if ("111".equals(code) || "148".equals(code)) {
//                    msg.what = 103;
//                    handler.sendMessage(msg);
//                } else if ("151".equals(code)) {
//                    msg.what = 104;
//                    handler.sendMessage(msg);
//                } else if ("153".equals(code)) {
//                    msg.what = 105;
//                    handler.sendMessage(msg);
//                } else if ("155".equals(code)) {
//                    msg.what = 106;
//                    handler.sendMessage(msg);
//                } else if ("156".equals(code) || "163".equals(code)) {
//                    msg.what = 107;
//                    handler.sendMessage(msg);
//                } else if ("157".equals(code)) {
//                    msg.what = 108;
//                    handler.sendMessage(msg);
//                } else if ("158".equals(code)) {
//                    msg.what = 109;
//                    handler.sendMessage(msg);
//                } else if ("161".equals(code)) {
//                    msg.what = 111;
//                    handler.sendMessage(msg);
//                }
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } finally {
//            closeProgressWheel();
//        }
//
//    }

    private void parseLoginJosn(String josn) {
        if (TextUtils.isEmpty(josn)) {
            closeProgressWheel();
            handler.sendEmptyMessage(203);
            return;
        }

        msg = new Message();
        JSONObject obj;
        try {
            obj = new JSONObject(josn);
            LogUtil.d("parseLoginJosn:" + josn);

            String status = obj.getString("Status");
            String code = obj.getString("Code");
            if ("1".equals(status) && "100".equals(code)) {// 可以登录
                sdkUid = obj.optString("Sdkuid");
                msg.what = 100;
                handler.sendMessage(msg);
//                //xuanfu
//                onFloatLintener.showFt();
            } else if ("0".equals(status)) {
                if ("104".equals(code)) {
                    msg.what = 101;
                    handler.sendMessage(msg);
                } else if ("110".equals(code)) {
                    msg.what = 102;
                    handler.sendMessage(msg);
                } else if ("111".equals(code) || "148".equals(code)) {
                    msg.what = 103;
                    handler.sendMessage(msg);
                } else if ("151".equals(code)) {
                    msg.what = 104;
                    handler.sendMessage(msg);
                } else if ("153".equals(code)) {
                    msg.what = 105;
                    handler.sendMessage(msg);
                } else if ("155".equals(code)) {
                    msg.what = 106;
                    handler.sendMessage(msg);
                } else if ("156".equals(code) || "163".equals(code)) {
                    msg.what = 107;
                    handler.sendMessage(msg);
                } else if ("157".equals(code)) {
                    msg.what = 108;
                    handler.sendMessage(msg);
                } else if ("158".equals(code)) {
                    msg.what = 109;
                    handler.sendMessage(msg);
                } else if ("161".equals(code)) {
                    msg.what = 111;
                    handler.sendMessage(msg);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            closeProgressWheel();
        }
    }

    private void parsesecLogFbJson(String json) {
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("Ugameid", UgameUtil.getInstance().GAME_ID);
        map.put("Ugamekey", UgameUtil.getInstance().CLIENT_SECRET);
        map.put("Fbappid", UgameUtil.getInstance().FBAPP_ID);
        map.put("Sdktype", "0");
        try {
            JSONObject obj = new JSONObject(json);
            map.put("Fbtoken", obj.getString("token_for_business"));
            map.put("Fbuid", obj.getString("id"));

            UhttpUtil.post(UgameUtil.getInstance().FACEBOOK_BINDING_VERIFY, map, new UcallBack() {

                @Override
                public void onResponse(String response, int arg1) {
                    LogUtil.i("关联Facebook返回结果： " + response);
                    if (null == response) {
                        return;
                    }
                    closeProgressWheel();
                    try {
                        JSONObject json = new JSONObject(response);
                        String status = json.optString("Status");
                        String isNew = json.optString("Isnew");
                        if ("1".equals(status)) {
                            String Access_token = json.optString("Access_token");
                            String sdkUid = json.optString("Sdkuid");
                            String Is_new = json.optString("Isnew");
                            Toast.makeText(mActivity, MResource.getIdByName(mActivity, "string", "fb_lonin_success"), Toast.LENGTH_SHORT).show();
                            addAppFly(sdkUid);
                            mOnLoginListener.onLoginSuccessful(sdkUid);

//                            储存FB信息
//                            StoreFacebookInfo storeInfo = new StoreFacebookInfo(mActivity.getApplicationContext(), preferences, map);
//                            storeInfo.storeFacebookInfo();

                            if ("1".equals(isNew)) {
                                LanucherMonitor.getInstance().registrationTrack(mActivity, sdkUid, "Facebook");
                            } else if ("0".equals(isNew)) {
                                LanucherMonitor.getInstance().loginTrack(mActivity, sdkUid, "Facebook");
                            }
                        } else if ("0".equals(status)) {
                            Toast.makeText(mActivity, MResource.getIdByName(mActivity, "string", "program_error"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Call arg0, Exception arg1, int arg2) {
                    // TODO Auto-generated method stub
                    mOnLoginListener.onLoginFailed(arg1.toString());
                    closeProgressWheel();
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parseVertifyJson(String json) {
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("Ugameid", UgameUtil.getInstance().GAME_ID);
        map.put("Ugamekey", UgameUtil.getInstance().CLIENT_SECRET);
        map.put("Fbappid", UgameUtil.getInstance().FBAPP_ID);
        map.put("Sdktype", "0");
        try {
            JSONObject obj = new JSONObject(json);
            map.put("Fbtoken", obj.getString("token_for_business"));
            map.put("Fbuid", obj.getString("id"));

            UhttpUtil.post(UgameUtil.getInstance().FACEBOOK_BINDING_VERIFY, map, new UcallBack() {

                @Override
                public void onResponse(String response, int arg1) {
                    LogUtil.i("关联Facebook返回结果： " + response);
                    if (null == response) {
                        return;
                    }
                    closeProgressWheel();
                    try {
                        JSONObject json = new JSONObject(response);
                        String status = json.optString("Status");
                        if ("1".equals(status)) {
                            String Access_token = json.optString("Access_token");
                            String sdkUid = json.optString("Sdkuid");
                            String Is_new = json.optString("Isnew");
                            Toast.makeText(mActivity, MResource.getIdByName(mActivity, "string", "fb_lonin_success"), Toast.LENGTH_SHORT).show();

                            if ("0".equals(Is_new)) {
                                //追踪登录
                                LanucherMonitor.getInstance().loginTrack(mActivity, sdkUid, "Facebook");
                                //                            上传手机信息
                                requestHD(userNameEt.getText().toString(), passwordEt.getText().toString(), sdkUid);
                            } else if ("1".equals(Is_new)) {
                                //追踪注册
                                LanucherMonitor.getInstance().registrationTrack(mActivity, sdkUid, "Facebook");
                            }
                            SharedPreferences.Editor edit = preferences.edit();
                            if (flag3 == true) {
                                edit.putBoolean("flag2", true);
                            } else {
                                edit.putBoolean("flag2", false);
                            }
                            edit.commit();
                            preferences.edit().putString("loginType", "facebook").commit();

//                            StoreFacebookInfo storeInfo = new StoreFacebookInfo(mActivity.getApplicationContext(), preferences, map);
//                            storeInfo.storeFacebookInfo();
                            closeProgressWheel();
                            mDialog.dismiss();
                            addAppFly(sdkUid);
                            mOnLoginListener.onLoginSuccessful(sdkUid);


                        } else if ("0".equals(status)) {

                            Toast.makeText(mActivity, MResource.getIdByName(mActivity, "string", "program_error"), Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onError(Call arg0, Exception arg1, int arg2) {
                    // TODO Auto-generated method stub

                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
//                    不用这个
                    layoutLogin.setVisibility(View.VISIBLE);
                    closeProgressWheel();
                    if (GAME_ID == null) {
                        Toast.makeText(mActivity, "401", Toast.LENGTH_SHORT);
                        return;
                    }
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("Ugameid", GAME_ID);
                    map.put("Ugamekey", CLIENT_SECRET);
                    map.put("Username", Login_name);
                    map.put("Password", Login_password);

                    UhttpUtil.post(UgameUtil.getInstance().LOGINURL, map, new UcallBack() {

                        @Override
                        public void onResponse(String response, int arg1) {
                            parseLoginJosn(response);

                        }

                        @Override
                        public void onError(Call arg0, Exception arg1, int arg2) {
                            loginfailed(arg1.toString());

                        }
                    });


                    break;

                case 4:
//                    保存FB用户信息
                    JSONObject arg2 = (JSONObject) msg.obj;
                    FacebookEntity facebook = new FacebookEntity();
                    facebook.setClientId(GAME_ID);
                    facebook.setClientSecret(CLIENT_SECRET);
                    facebook.setId(String.valueOf(arg2.optString("id")));
                    String email = String.valueOf(arg2.optString("email"));
                    if (!"null".equals(email) && !TextUtils.isEmpty(email)) {
                        facebook.setEmail(email);
                    }
                    String firstName = String.valueOf(arg2.optString("first_name"));
                    if (!"null".equals(firstName) && !TextUtils.isEmpty(firstName)) {
                        facebook.setFirstName(firstName);
                    }
                    String gender = String.valueOf(arg2.optString("gender"));
                    if (!"null".equals(gender) && !TextUtils.isEmpty(gender)) {
                        facebook.setGender(gender);
                    }

                    String lastName = String.valueOf(arg2.optString("last_name"));
                    if (!"null".equals(lastName) && !TextUtils.isEmpty(lastName)) {
                        facebook.setLastName(lastName);
                    }

                    String link = String.valueOf(arg2.optString("link"));
                    if (!"null".equals(link) && !TextUtils.isEmpty(link)) {
                        facebook.setLink(link);
                    }

                    String locale = String.valueOf(arg2.optString("locale"));
                    if (!"null".equals(locale) && !TextUtils.isEmpty(locale)) {
                        facebook.setLocale(locale);
                    }

                    String name = String.valueOf(arg2.optString("name"));
                    if (!"null".equals(name) && !TextUtils.isEmpty(name)) {
                        facebook.setName(name);
                    }

                    String timezone = String.valueOf(arg2.optString("timezone"));
                    if (!"null".equals(timezone) && !TextUtils.isEmpty(timezone)) {
                        facebook.setTimezone(timezone);
                    }

                    String verified = String.valueOf(arg2.optString("verified"));
                    if (!"null".equals(verified) && !TextUtils.isEmpty(verified)) {
                        facebook.setVerified(verified);
                    }

                    String updatedTime = String.valueOf(arg2.optString("updated_time"));
                    if (!"null".equals(updatedTime) && !TextUtils.isEmpty(updatedTime)) {
                        facebook.setUpdatedTime(updatedTime);
                    }
                    LogUtil.d("用户资料： " + facebook);

                    verifyFacebookBinding(facebook);
                    break;

                case 100:
                    String Access_token = (String) msg.obj;
                    LanucherMonitor.getInstance().loginTrack(mActivity.getApplicationContext(), sdkUid, "Ugame");
                    //preferences.edit().putString("loginType", "Ugame").commit();
                    mDialog.dismiss();
//                    SharedPreferences.Editor edit = preferences.edit();
//                    edit.putString("floatStatus", "1");
//                    edit.commit();
                    addAppFly(sdkUid);
                    mOnLoginListener.onLoginSuccessful(sdkUid);
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "login_success"), Toast.LENGTH_SHORT).show();

                    break;

                case 400:

                    break;
                case 101:
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "exsituname"), Toast.LENGTH_SHORT).show();
                    break;
                case 102:
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "incorrectpw"), Toast.LENGTH_SHORT).show();
                    break;
                case 103:
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "notexsituname"), Toast.LENGTH_SHORT).show();
                    break;
                case 104:
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "vistornotexsit"), Toast.LENGTH_SHORT).show();
                    break;
                case 105:
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "vistorhadbind"), Toast.LENGTH_SHORT).show();
                    break;
                case 106:
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "emailhadbind"), Toast.LENGTH_SHORT).show();
                    break;
                case 107:
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "acchadbindemail"), Toast.LENGTH_SHORT).show();
                    break;
                case 108:
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "emailnotbind"), Toast.LENGTH_SHORT).show();
                    break;
                case 109:
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "sendemailfail"), Toast.LENGTH_SHORT).show();
                    break;
                case 111:
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "invaliduname"), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "contactcustomservice"), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void requestHD(String userName, String pwd, String uid) {

        Log.d("LoginD-requestHD", "requestHD");
        UgameUtil.getInstance().getAdvertisingId(mActivity, userName, pwd, uid);

    }

    private void vertifyIP() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("Ugameid", UgameUtil.getInstance().GAME_ID);

        UhttpUtil.post(UgameUtil.getInstance().VERTIFY_IP, map, new UcallBack() {

            @Override
            public void onResponse(String response, int arg1) {
                LogUtil.i("读取result： " + response);
                parseIPJson(response);

            }

            @Override
            public void onError(Call arg0, Exception arg1, int arg2) {
                LogUtil.i("读取vertifyIP所在地异常： " + arg1.getMessage());

            }
        });


    }

    private void parseIPJson(Object result) {
        //LogUtil.i("登录检测ip结果: "+result);
        try {
            JSONObject jo = new JSONObject((String) result);
            String status = jo.optString("Status");
            if ("1".equals(status)) {
                String local = jo.optString("Local");
                if ("1".equals(local)) {//新马
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("ip", "1");
                    editor.commit();
                    //LogUtil.i("新马ip写入成功");
                } else if ("2".equals(local)) {//其他
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("ip", "2");
                    editor.commit();
                    //LogUtil.i("其他ip写入成功");
                }
            } else if ("0".equals(status)) {//读不到IP,默认为其他
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("ip", "2");
                editor.commit();
                //LogUtil.i("检测不到ip写入成功 ");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void getlocalcurrency() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("Ugameid", UgameUtil.getInstance().GAME_ID);
        map.put("isios", "0");

        UhttpUtil.post(UgameUtil.getInstance().GET_LOCALCURRENCY, map, new UcallBack() {

            @Override
            public void onResponse(String response, int arg1) {
                LogUtil.i("读取localcurrency： " + response);
                parseLocalJson(response);

            }

            @Override
            public void onError(Call arg0, Exception arg1, int arg2) {
                LogUtil.i("获取getlocalcurrency失败" + arg1.getMessage());

            }
        });


    }


    private void parseLocalJson(Object result) {
        try {
            JSONObject jo = new JSONObject((String) result);
            String status = jo.optString("Status");
            if ("1".equals(status)) {
                String local = jo.optString("Code");
                if ("1".equals(local)) {//台币
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("localc", "1");
                    editor.commit();
                    ;
                } else if ("2".equals(local)) {//韩币
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("localc", "2");
                    editor.commit();
                    //LogUtil.i("其他ip写入成功");
                } else if ("0".equals(local)) {//默认美金
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("localc", "0");
                    editor.commit();
                    //LogUtil.i("其他ip写入成功");
                }
            } else if ("0".equals(status)) {//读不到IP,默认为其他
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("localc", "1");
                editor.commit();
                LogUtil.i("检测不到local失败 ");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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


    public void loginfailed(String reason) {
        closeProgressWheel();
        mOnLoginListener.onLoginFailed(reason);
        LogUtil.d("login error======" + reason.toString());
        Toast.makeText(mActivity, MResource.getIdByName(mActivity, "string", "network_error"), Toast.LENGTH_SHORT)
                .show();

    }

    public void loginForFacebook() {
        //获取AccessTokens
        String facebookUrl = "https://graph.facebook.com/me?fields=token_for_business&access_token="
                + AccessToken.getCurrentAccessToken().getToken() + "";

        LogUtil.d("LOin-loin4fb-facebookUrl" + facebookUrl);
        UhttpUtil.get(facebookUrl, new UcallBack() {
            @Override
            public void onResponse(String response, int arg1) {
                LogUtil.k("LogDialog->loginfb,返回=" + response);
                mDialog.dismiss();
                parseVertifyJson(response);
            }

            @Override
            public void onError(Call arg0, Exception arg1, int arg2) {
                LogUtil.k("facebook onerror,exception=" + arg1);
                Toast.makeText(mActivity, MResource.getIdByName(mActivity, "string", "network_error"), Toast.LENGTH_SHORT)
                        .show();
            }
        });


    }

    private void showProgressWheel() {
        if (progressWheel != null) {
            progressWheel.setVisibility(View.VISIBLE);
            progressWheel.spin();
        }
    }

    private void closeProgressWheel() {
        if (progressWheel != null) {
            progressWheel.stopSpinning();
            progressWheel.setVisibility(View.GONE);
        }

    }

    /**
     * af加入，注册,登录
     */
    public void addAppFly(String uid) {
//        AppsFlyerLib.getInstance().setCustomerUserId(uid);
    }


}
