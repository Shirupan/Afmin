package com.gigifun.wszj;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.gigifun.gp.UgameSDK;
import com.gigifun.gp.listener.GooglePayListener;
import com.gigifun.gp.listener.IFuntionCheck;
import com.gigifun.gp.listener.OnLoginListener;
import com.gigifun.gp.ui.ProgressWheel;
import com.gigifun.gp.utils.LogUtil;
import com.gigifun.gp.utils.UcallBack;
import com.gigifun.gp.utils.UgameUtil;
import com.gigifun.gp.utils.UhttpUtil;
import com.stone.api.MainSDK;
import com.gtoken.unclejoe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import okhttp3.Call;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private String Roleid = "117440547";
    private String Sdkuid = "120";
    private String Serverid = "1";
    private String googlesku = "ah_gp_gem1";
    private String sPcText="iiu12345";
    private ProgressWheel progressWheel;
    private String url = "http://api.gigifun.com/online.php?a=weixin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_test);
        initView();
        //初始化USDK
        initSDK();

        getSign();

        MainSDK.initialize(this);
    }

    private void initSDK() {
        UgameSDK.sdkInitialize(this);
    }

    //查看签名
    private void getSign() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                LogUtil.k("MY KEY HASH:" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void initView() {
        progressWheel = findViewById(R.id.progress_wheel);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_pay).setOnClickListener(this);
        findViewById(R.id.btn_fb).setOnClickListener(this);
        findViewById(R.id.btn_check).setOnClickListener(this);
        findViewById(R.id.btn_molpay).setOnClickListener(this);
        findViewById(R.id.btn_gogle).setOnClickListener(this);
        findViewById(R.id.btn_wxpay).setOnClickListener(this);
        findViewById(R.id.btngift).setOnClickListener(this);
        findViewById(R.id.btnflow).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                showLoginView();
                break;
            case R.id.btn_pay:
                googlePay();
                break;
            case R.id.btn_fb:
                fb();
                break;
            case R.id.btn_check:
                checkFailBill();
                break;
            case R.id.btn_molpay:
                morePay();
                break;
            case R.id.btn_gogle:
                googleLogin();
                break;
            case R.id.btn_wxpay:
                wxPay();
                break;
            case R.id.btngift:

                break;
            case R.id.btnflow:
                floatView();
                break;
        }
    }

    private void morePay() {
        UgameSDK.getInstance().morePay( Serverid,"gigifun");
    }

    private void checkFailBill() {
        UgameSDK.getInstance().checkFailBill(this,
                Serverid,
                Roleid,
                Sdkuid,
                sPcText,
                new IFuntionCheck() {
                    @Override
                    public void checkFunctionOpen(String fbflag, String rateFlag, String paymentflag) {
                        LogUtil.k("fbflag===" + fbflag + ",rateFlag===" + rateFlag + ",paymentflag===" + paymentflag);
                    }
                }
        );
    }

    /*
     * 谷歌支付
     * @param serverId  服务器ID
     * @param roleid    游戏角色ID
     * @param sdkuid    平台ID
     * @param product   商品描述
     * @param amount    商品金额
     * @param coOrderId 游戏商订单号
     * @param sku       谷歌计费点SKU
     * @param Ctext     自定义参数(可选)
     */
    private void googlePay() {
        final String coOrderId = createOrder();
        Log.d("TestActivity-pay==订单号=", coOrderId);
        // TODO Auto-generated method stub
        //google pay 支付
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UgameSDK.getInstance().googlePay(MainActivity.this,
                        Serverid,
                        "血包",
                        coOrderId,
                        "101",
                        "iiu", new GooglePayListener() {
                            @Override
                            //google充值成功
                            public void success(String res) {
                                LogUtil.k("success="+res);
                            }

                            @Override
                            //google充值失败
                            public void cancel(String res) {
                                LogUtil.k("cancel="+res);
                            }
                        });
            }
        });
    }

    private String createOrder() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String date = format.format(calendar.getTime());
        Random random = new Random();
        return date + random.nextInt(100) + "";
    }

    private void wxPay() {

        HashMap<String,String> map = new HashMap<String, String>();
        map.put("Sku",googlesku);
        map.put("userId",Sdkuid);
        map.put("roleId",Roleid);
        map.put("serverId",Serverid);
        map.put("gameId","10000");
        map.put("type","0");
        map.put("cp_orderId",createOrder());
        map.put("cText","iiu");
        map.put("Ugamekey",UgameUtil.getInstance().CLIENT_SECRET);
        map.put("packName","com.gigifun.wszj");
        map.put("name","王者");

        UhttpUtil.post(url, map, new UcallBack() {
            @Override
            public void onResponse(String response, int arg1) {
                LogUtil.k("wx response = "+response);

                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    String pay_info=jo.optString("pay_info");
                    Intent intent = new Intent(MainActivity.this,WxActivity.class);
                    intent.putExtra("payinfo",pay_info);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Call arg0, Exception arg1, int arg2) {
                LogUtil.k("wx error = "+arg1);
            }
        });
    }

    private void floatView() {
        UgameSDK.getInstance().showFloatingView();
    }

    //打开fb活动页面
    private void fb() {
        UgameSDK.getInstance().startForGift(this, Serverid);
    }

    private void showLoginView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UgameSDK.getInstance().login(MainActivity.this, new OnLoginListener() {
                    @Override
                    public void onLoginSuccessful(String sdkUid) {
                        LogUtil.d("sdkUid======" + sdkUid);
                        Sdkuid = sdkUid;
                        //  AppsFlyerLib.getInstance().setCustomerUserId(sdkUid);
                    }

                    @Override
                    public void onLoginFailed(String reason) {

                    }
                });
            }
        });
    }

    private void googleLogin() {
        UgameSDK.getInstance().googlePay(this, "", "", "", "101", "", new GooglePayListener() {

            @Override
            public void success(String res) {

            }

            @Override
            public void cancel(String res) {

            }
        });
        LogUtil.d("stestetste======");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UgameSDK.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.k("onStop");
        UgameSDK.getInstance().onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.k("onResume");
        UgameSDK.getInstance().onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UgameSDK.getInstance().onDestroy();
    }
}
