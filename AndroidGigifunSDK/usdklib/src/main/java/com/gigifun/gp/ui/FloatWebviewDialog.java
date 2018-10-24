package com.gigifun.gp.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.gigifun.gp.listener.OnLoginListener;
import com.gigifun.gp.listener.OnThirdPurchaseListener;
import com.gigifun.gp.service.FloatViewService;
import com.gigifun.gp.utils.UgameUtil;
import com.gigifun.gp.utils.LogUtil;
import com.gigifun.gp.utils.MD5Utils;
import com.gigifun.gp.utils.MResource;

import java.io.File;

/**
 * Created by tim on 16/9/1.
 */
public class FloatWebviewDialog {

    private Activity mActivity;
    private Dialog mDialog;

    private WebView mWebView;
    private WebSettings webSettings;
    private String sdkUid;
    private String roleid;
    private String GAME_ID;
    private String serverId;
    private String sPcText;
    private boolean isVer;


    //    private Button btnCancel;
//    private ProgressBar mProgressBar;
    private ProgressWheel mProgressWheel;
    private static FloatViewService floatViewService;

    private static OnThirdPurchaseListener mOnThirdPurchaseListener;
    private String lang;//language
    private String device;//设备类型 Android 0 ios 1
    private String seltctBtn;
    private final SharedPreferences preferences;

    /**
     * @param activity
     * @param serverid
     * @param floatViewService
     * @param str              对应按钮
     */
    public FloatWebviewDialog(Activity activity, String serverid, FloatViewService floatViewService, String str, boolean isVer, OnThirdPurchaseListener onThirdPurchaseListener) {

        LogUtil.k("FloatWebviewDialog floatViewService==" + floatViewService);
        this.floatViewService = floatViewService;
        this.mOnThirdPurchaseListener = onThirdPurchaseListener;
        this.mActivity = activity;
        this.GAME_ID = UgameUtil.getInstance().GAME_ID;
        this.serverId = serverid;
        this.lang = getLang();
        this.device = "0";
        this.seltctBtn = str;
        preferences = mActivity.getSharedPreferences("LoginCount", Context.MODE_PRIVATE);
        this.sdkUid = preferences.getString("paysdkUid", "");
        this.roleid = preferences.getString("payroleId", "");
        this.sPcText=preferences.getString("sPcText","");
        this.isVer=isVer;
        initUI();
    }

    public void initUI() {
        mDialog = new Dialog(mActivity, MResource.getIdByName(mActivity, "style", "custom_dialog"));
        mDialog.getWindow().getAttributes().windowAnimations = MResource.getIdByName(mActivity, "style", "dialogAnim");
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(MResource.getIdByName(mActivity, "layout", "dialog_floatwebview"));
//        onFloatLintener.hideFt();
        //隐藏悬浮
        //floatViewService.hideFloat();
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("floatStatus", "1");
        edit.commit();

        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                LogUtil.k("点击===" + event.getAction());
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    LogUtil.k("点击退出");
                    if (null != mWebView && mWebView.canGoBack()) {
                        mWebView.goBack();
                        return true;
                    } else {
                        if (event.getAction() == KeyEvent.ACTION_UP) {
                            dissmiss("");
                        }
                    }
                 if(isVer){
                     mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                 }else{
                     mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                 }
                    //mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

                   // mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);//竖屏
                    //默认横屏
                    //mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                   // mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                }
                return false;

            }
        });

        mDialog.show();
//        String signaa = MD5Utils.md5MorePaySign(sdkUid, roleid, serverId, GAME_ID, lang, device);
//        LogUtil.k("签名是===" + signaa);

        mWebView = (WebView) mDialog.findViewById(MResource.getIdByName(mActivity, "id", "web_molpay"));
        mProgressWheel = (ProgressWheel) mDialog.findViewById(MResource.getIdByName(mActivity, "id", "web_progress"));

        if (Build.VERSION.SDK_INT >= 19) {
            mWebView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            mWebView.getSettings().setLoadsImagesAutomatically(false);
        }

        mWebView.requestFocusFromTouch();
        webSettings = mWebView.getSettings();
        webSettings.setDomStorageEnabled(true);

        webSettings.setJavaScriptEnabled(true);//启用js
        webSettings.setBlockNetworkImage(false);//解决图片不显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //不使用缓存
//        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        mWebView.addJavascriptInterface(new JavaScriptInterface(), "ncp");
        mWebView.setWebViewClient(new WebViewClient() {

            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {



                // handler.proceed();
                // 不要使用super，否则有些手机访问不了，因为包含了一条 handler.cancel()
                // super.onReceivedSslError(view, handler, error);
                final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setMessage("SSL Cert Invalid");
                builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {

                Toast.makeText(mActivity, MResource.getIdByName(mActivity, "string", "loading_error"), Toast.LENGTH_SHORT).show();
                LogUtil.e("description=="+description+",failingurl=="+failingUrl);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
                closeProgressWheel();

//                if (!mWebView.getSettings().getLoadsImagesAutomatically()) {
//                    mWebView.getSettings().setLoadsImagesAutomatically(true);
//                }
                mWebView.getSettings().setLoadsImagesAutomatically(true);
                LogUtil.k("完成是加载的地址==" + url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showProgressWheel();
            }

            //            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                // TODO Auto-generated method stub
//                mProgressBar.setVisibility(View.VISIBLE);
//                view.loadUrl(url);
//                return true;
//            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false;
                }
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    mActivity.startActivity(intent);
                } catch (Exception e) {
                }
                return true;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

        });
//https://sdkapi.iiugame.com/webonline.php?a=pay&userId=120&serverId=33&roleId=1000&gameId=10002&isios=0&lang=zh-cn
        String otherUrl = "https://api.playtoken.com/webonline.php?" +
                "a="
                + seltctBtn
                + "&gameId="
                + GAME_ID
                + "&serverId="
                + serverId
                + "&userId="
                + sdkUid
                + "&roleId="
                + roleid
                + "&lang="
                + lang
                + "&isios="
                + device
                + "&sPcText="
                + this.sPcText
                + "&version="
                + getVersionName(mActivity)
                +"&packname="
                +mActivity.getPackageName()
                + "&sign="
                + MD5Utils.md5MorePaySign(sdkUid, roleid, serverId, GAME_ID, lang, device,getVersionName(mActivity));

      LogUtil.k("---url--="+otherUrl);
        mWebView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Mobile Safari/537.36");
//        mWebView.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
        mWebView.loadUrl(otherUrl);

    }

    public final class JavaScriptInterface {

        @JavascriptInterface
        public void go2Googleplay() {
            launchAppDetail(mActivity.getPackageName(), "com.android.vending");
        }

        @JavascriptInterface
        public void close(final String str) {
            if(isVer){
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }else{
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);//竖屏
            }


          //  mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
           // mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);//竖屏
            //默认设置
           // mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            dissmiss(str);
        }
    }


    // clear the cache before time numDays
    private int clearCacheFolder(File dir, long numDays) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, numDays);
                    }
                    if (child.lastModified() < numDays) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

    /**
     * 启动到应用商店app详情界面
     *
     * @param appPkg    目标App的包名
     * @param marketPkg 应用商店包名 ,如果为""则由系统弹出应用商店列表供用户选择,否则调转到目标市场的应用详情界面，某些应用商店可能会失败
     */
    public void launchAppDetail(String appPkg, String marketPkg) {
        try {
            if (TextUtils.isEmpty(appPkg)) return;
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg)) {
                intent.setPackage(marketPkg);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getLang() {
        String lang = mActivity.getResources().getString(MResource.getIdByName(mActivity, "string", "lang"));
        if ("TW".equals(lang)) {
            lang = "zh-tw";
        } else if ("EN".equals(lang)) {
            lang = "zh-en";
        } else if("CN".equals(lang)){
            lang = "zh-cn";
        }else if("TH".equals(lang)){
            lang="zh-th";
        }else{
            lang="zh-cn";
        }
        LogUtil.k("FloatView lang = " + lang);
        return lang;
    }

    public static String getVersionName(Context context)//获取版本号
    {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getApplicationContext().getPackageName(),
                    PackageManager.GET_SIGNATURES);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    public String getCText() {

//        String ctext = mActivity.getResources().getString(MResource.getIdByName(mActivity, "string", "sPcText"));
        String ctext=this.sPcText;
        LogUtil.k("sPcText = " + ctext);
        return ctext;
    }

    public void dissmiss(final String str) {

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //floatViewService.showFloat();

                    if(str.equals(""))
                    {
                        mOnThirdPurchaseListener.onPurchaseFailed("cancel");
                    }
                    else
                    {
                        mOnThirdPurchaseListener.onPurchaseSuccessful(str);
                    }
                    }
                });
        clearWebViewCache();
//        SharedPreferences.Editor edit = preferences.edit();
//        edit.putString("floatStatus", "0");
//        edit.commit();

        mDialog.dismiss();

    }
//    public static void show(Activity activity, String serverid) {
//        new FloatWebviewDialog(activity, serverid);
//    }
private void showProgressWheel() {
    if (mProgressWheel != null) {
        mProgressWheel.setVisibility(View.VISIBLE);
        mProgressWheel.spin();
    }
}

    private void closeProgressWheel() {
        if (mProgressWheel != null) {
            mProgressWheel.stopSpinning();
            mProgressWheel.setVisibility(View.GONE);
        }

    }


    public void clearWebViewCache() {

//        清理Webview缓存数据库
        try {
            mActivity.deleteDatabase("webview.db");
            mActivity.deleteDatabase("webviewCache.db");
            LogUtil.k("删除数据库");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //WebView 缓存文件
        File appCacheDir = new File(mActivity.getFilesDir().getAbsolutePath() + "/webcache");
        Log.e("gigifun_kong", "appCacheDir path=" + appCacheDir.getAbsolutePath());

        File webviewCacheDir = new File(mActivity.getCacheDir().getAbsolutePath());
        Log.e("gigifun_kong", "webviewCacheDir path=" + webviewCacheDir.getAbsolutePath());

        //删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir);
            LogUtil.k("删除数据  webviewCacheDir");
        }
        //删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir);
            LogUtil.k("删除数据  appCacheDir");
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public void deleteFile(File file) {

        Log.e("gigifun_kong", "delete file path=" + file.getAbsolutePath());

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
            Log.e("kong", "delete file no exists " + file.getAbsolutePath());
        }
    }
}

