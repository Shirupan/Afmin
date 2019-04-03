package com.gigifun.wszj;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.gigifun.gp.UgameSDK;
import com.gigifun.gp.listener.GooglePayListener;
import com.gigifun.gp.listener.IFuntionCheck;
import com.gigifun.gp.listener.OnLoginListener;
import com.gigifun.gp.listener.OnThirdPurchaseListener;
import com.gigifun.gp.ui.ProgressWheel;
import com.gigifun.gp.utils.LogUtil;
import com.gigifun.gp.utils.MResource;
import com.gigifun.gp.utils.UcallBack;
import com.gigifun.gp.utils.UgameUtil;
import com.gigifun.gp.utils.UhttpUtil;
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

/**
 * 登陆有两次重复追踪，已删除
 * facebook登录注册追踪修改
 */
public class TestActivity extends Activity implements View.OnTouchListener {
    //uid: 173  lnid: 329196  serverid: 8900
    private Button btn_login, btn_pay, btn_fb, btn_check, btn_molpay,btn_goo,btn_wxpay,btngift,btnflow;
    private String Roleid = "117440547";
    private String Sdkuid = "120";
    private String Serverid = "1";
    private String googlesku = "ah_gp_gem1";
    private String sPcText="iiu12345";
    private ProgressWheel progressWheel;
    private String url = "http://api.gigifun.com/online.php?a=weixin";

    /**
     * 连接到Service
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_test);

        //LinearLayout ll =  (LinearLayout) findViewById(R.id.mainView);
        //ll.setOnTouchListener(this);


//
//        new IAction() {
//            @Override
//            public boolean call() {
        //初始化view
        initView();
        //初始化USDK
        UgameSDK.sdkInitialize(TestActivity.this);
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
        btn_goo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                UgameSDK.getInstance().googlePay(TestActivity.this, "", "", "", "101", "", new GooglePayListener() {


                    @Override
                    public void success(String res) {

                    }

                    @Override
                    public void cancel(String res) {

                    }
                });
                LogUtil.d("stestetste======");
                return;
//                UgameSDK.getInstance().ShowWebView(
//                        new OnThirdPurchaseListener() {
//                            @Override
//                            public void onPurchaseSuccessful(String orderId) {
//                                LogUtil.d("sdkUid======" + orderId);
//                                //  AppsFlyerLib.getInstance().setCustomerUserId(sdkUid);
//                            }
//
//                            @Override
//                            public void onPurchaseFailed(String reason) {
//
//                                LogUtil.d("Failed Reason======" + reason);
//                            }
//                        }
//                );
            }
        });
        /**
         * 点击登录
         */
        btn_login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UgameSDK.getInstance().login(TestActivity.this, new OnLoginListener() {
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
        });
        //进行功能开启检查(调到选择角色里)
//        btn_checkfuction.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                UgameUtil.getInstance().checkFuction(Serverid, new UgameUtil.IFuntionCheck() {
//                    @Override
//                    public void checkFunctionOpen(String fbflag, String rateFlag, String paymentflag) {
//                        LogUtil.k("fbflag===" + fbflag + ",rateFlag===" + rateFlag + ",paymentflag===" + paymentflag);
//                    }
//                });
//            }
//        });
        /**
         * 点击打开fb活动页面
         */
        btn_fb.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //打开fb活动页面
                UgameSDK.getInstance().startForGift(TestActivity.this, Serverid);
            }
        });

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String date = format.format(calendar.getTime());
        Random random = new Random();
        final String coOrderId = date + random.nextInt(100) + "";

        /**
         * 点击谷歌支付
         */
        /***
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
        btn_pay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("TestActivity-pay==订单号=", coOrderId);
                // TODO Auto-generated method stub
                //google pay 支付
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UgameSDK.getInstance().googlePay(TestActivity.this,
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

                return;
//                UgameSDK.getInstance().ShowWebView(
//                        new OnThirdPurchaseListener() {
//                            @Override
//                            public void onPurchaseSuccessful(String orderId) {
//                                LogUtil.d("orderId======" + orderId);
//                                //  AppsFlyerLib.getInstance().setCustomerUserId(sdkUid);
//                            }
//
//                            @Override
//                            public void onPurchaseFailed(String reason) {
//
//                                LogUtil.d("Failed Reason======" + reason);
//                            }
//                        }
//                );
            }
        });

        btn_check.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                UgameSDK.getInstance().checkFailBill(TestActivity.this,
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


        });
        btn_molpay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                UgameSDK.getInstance().morePay( Serverid,"gigifun");

            }
        });
        btn_wxpay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String,String> map = new HashMap<String, String>();
                map.put("Sku",googlesku);
                map.put("userId",Sdkuid);
                map.put("roleId",Roleid);
                map.put("serverId",Serverid);
                map.put("gameId","10000");
                map.put("type","0");
                map.put("cp_orderId",coOrderId);
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
                            Intent intent = new Intent(TestActivity.this,WxActivity.class);
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
        });
        findViewById(R.id.btn_webFlo).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                UgameSDK.getInstance().showFloatView(TestActivity.this,Serverid);

            }
        });

        btnflow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                UgameSDK.getInstance().showFloatingView();
            }


        });


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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 初始化view
     */
    private void initView() {
//        String Imei = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE))
//                .getDeviceId();
//        Log.d("imei========", Imei);
//        String brand = android.os.Build.BRAND;
//        Log.d("brand========", brand);

        btn_login = (Button) findViewById(MResource.getIdByName(this, "id", "btn_login"));
        btn_pay = (Button) findViewById(MResource.getIdByName(this, "id", "btn_pay"));
        btn_fb = (Button) findViewById(MResource.getIdByName(this, "id", "btn_fb"));
        progressWheel = (ProgressWheel) findViewById(MResource.getIdByName(this, "id", "progress_wheel"));
        btn_check = (Button) findViewById(MResource.getIdByName(this, "id", "btn_check"));
        btn_molpay = (Button) findViewById(MResource.getIdByName(this, "id", "btn_molpay"));
        btn_goo = (Button) findViewById(MResource.getIdByName(this, "id", "btn_gogle"));
        btn_wxpay = (Button) findViewById(MResource.getIdByName(this, "id", "btn_wxpay"));
        btngift = (Button) findViewById(MResource.getIdByName(this, "id", "btngift"));
        btnflow = (Button) findViewById(MResource.getIdByName(this, "id", "btnflow"));

//        btn_checkfuction = (Button) findViewById(MResource.getIdByName(this, "id", "btn_checkfuction"));
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        LogUtil.k("touch x:" + motionEvent.getX() + "touch y:" + motionEvent.getY());

        switch (motionEvent.getAction())
        {
            case MotionEvent.ACTION_DOWN:

                LogUtil.k("touch x:" + motionEvent.getX() + "touch y:" + motionEvent.getY());
                break;
        }
        return false;
    }
}



