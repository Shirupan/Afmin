package com.gigifun.gp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.gigifun.gp.service.FloatViewService;
import com.gigifun.gp.ui.ProgressWheel;
import com.gigifun.gp.utils.JSONParseUtil;
import com.gigifun.gp.utils.LanucherMonitor;
import com.gigifun.gp.utils.LogUtil;
import com.gigifun.gp.utils.MD5Utils;
import com.gigifun.gp.utils.MResource;
import com.gigifun.gp.utils.UcallBack;
import com.gigifun.gp.utils.UgameUtil;
import com.gigifun.gp.utils.UhttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

public class MorePay extends Activity {
    private Activity mActivity;
    private WebSettings webSettings;
    private WebView mWebView;
    private ProgressWheel mProgressWheel;
    private Button btnCancel;

    private String serverId;
    private String roleid;
    private String sdkuid;
    private String ctext;
    private String GAME_ID;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(com.gigifun.gp.utils.MResource.getIdByName(this, "layout", "activity_more_pay"));
        btnCancel = (Button) this.findViewById(com.gigifun.gp.utils.MResource.getIdByName(MorePay.this, "id", "btn_cancel"));
        mWebView = (WebView) this.findViewById(com.gigifun.gp.utils.MResource.getIdByName(MorePay.this, "id", "web_molpay"));
        mProgressWheel = (ProgressWheel) this.findViewById(com.gigifun.gp.utils.MResource.getIdByName(MorePay.this, "id", "web_progress"));

        preferences = this.getSharedPreferences("LoginCount", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("floatStatus", "1");
        edit.commit();
        try {
            Intent intent = new Intent(this, FloatViewService.class);
            startService(intent);
            bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
        }
        //隐藏悬浮
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LogUtil.k("More Pay service==" + mFloatViewService);
                if (mFloatViewService != null) {
                    mFloatViewService.hideFloat();
                }
            }
        });


        GAME_ID = UgameUtil.getInstance().GAME_ID;
        Intent intent = getIntent();
        serverId = intent.getStringExtra("serverId");
//         roleid = intent.getStringExtra("roleid");
//         sdkuid = intent.getStringExtra("sdkuid");
        ctext = intent.getStringExtra("sPcText");
//        从本地获取角色id
        SharedPreferences preferences = getSharedPreferences("LoginCount", Context.MODE_PRIVATE);
        sdkuid = preferences.getString("paysdkUid", "");
        roleid = preferences.getString("payroleId", "");

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btnCancel.setClickable(false);
                postCountPayTask(GAME_ID, sdkuid);
                finish();
                mFloatViewService.showFloat();
            }
        });

        if (Build.VERSION.SDK_INT >= 19) {
            mWebView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            mWebView.getSettings().setLoadsImagesAutomatically(false);
        }


        mWebView.requestFocusFromTouch();
        webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {

                Toast.makeText(MorePay.this, MResource.getIdByName(MorePay.this, "string", "loading_error"), Toast.LENGTH_SHORT).show();

                super.onReceivedError(view, errorCode, description, failingUrl);
            }


//            @Override
//            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
//                super.onReceivedHttpError(view, request, errorResponse);
//                Toast.makeText(MorePay.this, MResource.getIdByName(MorePay.this, "string", "loading_error"), Toast.LENGTH_SHORT).show();
//            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);

                if (!mWebView.getSettings().getLoadsImagesAutomatically()) {
                    mWebView.getSettings().setLoadsImagesAutomatically(true);
                }
                closeProgressWheel();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
                //dialog.show();
                showProgressWheel();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false;
                }
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {

                }
                return true;
            }

            //            處理瀏覽器的按鍵
            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //System.out.println(newProgress);
                //setProgress(newProgress * 100);

                super.onProgressChanged(view, newProgress);
            }

        });
        String otherUrl = "https://mpay.gigifun.com/index.php?g=Mpay&m=Index&a=index&gameId="
                + GAME_ID
                + "&serverId="
                + serverId
                + "&userId="
                + sdkuid
                + "&roleId="
                + roleid
                + "&cp_id="
                + ctext
                + "&type=0";
        LogUtil.k("webview地址==" + otherUrl);
        mWebView.getSettings().setUserAgentString("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
        mWebView.loadUrl(otherUrl);
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            LogUtil.k("点击退出");
            if (null != mWebView && mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            } else {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    LogUtil.k("postCountPayTask");
                    postCountPayTask(GAME_ID, sdkuid);
                }
                finish();
//                mFloatViewService.showFloat();
            }
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtil.k("点击===" + event.getAction());
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            LogUtil.k("点击退出");
            if (null != mWebView && mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            } else {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    LogUtil.k("postCountPayTask");
                    postCountPayTask(GAME_ID, sdkuid);
                }
                finish();
                mFloatViewService.showFloat();
            }
        }
        return false;
    }


    /**
     * 发起统计请求
     */
    public void postCountPayTask(String gameid, String userid) {

        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("floatStatus", "0");
        edit.commit();

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("gameid", gameid);
        param.put("userid", userid);
        param.put("sign", MD5Utils.md5MorePaySign(gameid, userid, "0"));
        param.put("isios", "0");
        LogUtil.k("拼接的签名：" + MD5Utils.md5MorePaySign(gameid, userid, "0"));

        UhttpUtil.post(UgameUtil.getInstance().GET_USER_PAY, param, new UcallBack() {

            @Override
            public void onResponse(String response, int arg1) {

                requestResult(response);
                LogUtil.k("第三方支付返回==" + response);
            }

            @Override
            public void onError(Call arg0, Exception arg1, int arg2) {

                LogUtil.k("第三方支付返回==失败");
            }
        });
    }

    /**
     * 处理返回结果信息
     *
     * @param result
     */
    private void requestResult(String result) {


        try {
            JSONObject jo = new JSONObject((String) result);
            HashMap<String, String> requestStatus = JSONParseUtil
                    .requestParse(jo);
            if (requestStatus.get("Status").equals("1")) {
                //请求成功
                JSONArray jsonArray = jo.optJSONArray("Data");
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        // LanucherMonitor.payTrack(MolPayActivity.this,jo.optString("Userid"),jsonArray.get(i).toString(),"WebPay");
                        JSONObject payOb = (JSONObject) jsonArray.get(i);
                        String amount = payOb.optString("amount");
                        String channel = payOb.optString("channel");
                        LogUtil.k("amount： " + amount + "channel： " + channel);
                        LanucherMonitor.getInstance().payTrack(MorePay.this, jo.optString("Userid"), amount, channel);
                    }
                }
            }
            LogUtil.k(jo.toString());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            stopService(new Intent(this, FloatViewService.class));
            unbindService(mServiceConnection);

        } catch (Exception e) {
        }
    }

    public MorePay() {
    }

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

    private FloatViewService mFloatViewService;
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LogUtil.k("init,成功连接服务");
            FloatViewService.FloatViewServiceBinder iBinder1 = (FloatViewService.FloatViewServiceBinder) iBinder;
            mFloatViewService = (iBinder1).getService();
            LogUtil.k("MorePay mFloatViewService==" + mFloatViewService);

            mFloatViewService.hideFloat();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            LogUtil.k("init,连接服务失败");
        }
    };
}
