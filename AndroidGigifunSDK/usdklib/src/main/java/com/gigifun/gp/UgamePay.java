package com.gigifun.gp;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.gigifun.gp.db.DBDao;
import com.gigifun.gp.db.DBFile;
import com.gigifun.gp.db.DBOpenHelper;
import com.gigifun.gp.db.DatabaseManager;
import com.gigifun.gp.listener.GooglePayListener;
import com.gigifun.gp.utils.AESEncode;
import com.gigifun.gp.utils.IabHelper;
import com.gigifun.gp.utils.IabResult;
import com.gigifun.gp.utils.Inventory;
import com.gigifun.gp.utils.LanucherMonitor;
import com.gigifun.gp.utils.LogUtil;
import com.gigifun.gp.utils.MD5Utils;
import com.gigifun.gp.utils.MResource;
import com.gigifun.gp.utils.Purchase;
import com.gigifun.gp.utils.UcallBack;
import com.gigifun.gp.utils.UgameUtil;
import com.gigifun.gp.utils.UhttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by iuu1 on 2017/1/6.
 */

public class UgamePay {


    //==================================================================================

    // GooglePlay支付
    IabHelper mHelper;
    // int mTank;
    static final int GOOGLE_CHECK_ORDER = 2000;
    static final int RC_REQUEST = 10001;//request code for the purchase flow请求码
    static final int PAYACTIVITY_ALIREQUEST = 1000;
    static final int PAY_DEL = 10002;
    static final int PAY_UPDATE = 10003;
    static final int PAY_INSERT = 10004;
    private boolean iap_is_ok = false;
    private String[] skus = new String[1];
    private String coin; // 货币名称
    private String roleid; // 角色id
    private String sdkuid; // 平台id
    private String product;// 商品名称
    private String sku; // google play内购商品id
    private String coOrderId;// 游戏商订单号
    private String gameId;
    private String serverId;// 服务器id
    private String Ctext;//自定义参数
    private Context context;
    private String transactionId = "";//谷歌订单id
    private String encodeTransactionId = "";//谷歌订单id进行md5加密
    private String signture = "";//谷歌签名
    private String originalJson = "";//谷歌返回数据
    private UgamePay.MHandler handler;
    private int payStyle;
    private Purchase mPurchase;
    private Activity activity;
    private static final int ERROR_OPERATE_DB = 2020;
    private IInAppBillingService billingservice;
    private String curSku; //检查错误订单时从数据库获取的商品ID
    private String curCoorderid;//检查错误订单时从数据库获取的订单号
    private String curOriginaljson;//检查错误订单时从数据库获取的谷歌数据
    private String curSignture;//检查错误订单时从数据库获取的谷歌签名
    private String curCtext;
    private String curisfit;
    private String curRoled;
    private String curSdkuid;
    private String curServerId;
    private String curGameId;
    private String paysdkUid;
    private String payroleId;
    private String reOrderId;
    private Dialog dialog;
    private GooglePayListener googlePayListener;

    public UgamePay(Context context) {
        LogUtil.k("gigifunpay-iigampay" + "==初始化BEGIN");
        this.context = context;
        DatabaseManager.initializeInstance(context);
        payStyle = 1;
        init(context);
        LogUtil.k("gigifunpay-iigampay" + "==初始化END");

    }


    public void init(Context context) {

        handler = new UgamePay.MHandler();
        //判断有没有安装谷歌
        if (!isInstallGoogleStore()) {
            return;
        }
        // 正式版key
        LogUtil.i("base64EncodedPublicKey=  " + UgameUtil.getInstance().base64EncodedPublicKey);
        //开启googlepay
        mHelper = new IabHelper(context, UgameUtil.getInstance().base64EncodedPublicKey);

        mHelper.enableDebugLogging(true);
        //绑定ServiceConnection到IInAppBillingService
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {

                if (!result.isSuccess()) {
                    return;
                }
                iap_is_ok = true;
            }
        });
    }

    /*
     * 查询没有上传的订单，所有数据从手机上取
     * */
    private void resentFailBill(Activity activity, final String serverId) {
        this.serverId = serverId;
        this.activity = activity;
        DBOpenHelper helper = new DBOpenHelper(context);
        final SQLiteDatabase db = helper.getWritableDatabase();
        //查询表中所有的数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtil.k("进入查询数据库");
                Cursor cursor = db.query(DBOpenHelper.tab_syzg, null, null, null, null, null, null);
                int columnCount = cursor.getColumnCount();
                int count = cursor.getCount();
                LogUtil.k("gigifunpay-checkFail,行数===" + count + ",列数==" + columnCount);
                if (cursor.moveToFirst()) {
                    do {
                        LogUtil.k("数据库信息:" + "uid=" + cursor.getString(cursor.getColumnIndex("uid")) +
                                ",lnid=" + cursor.getString(cursor.getColumnIndex("lnid")) +
                                ",coorderid=" + cursor.getString(cursor.getColumnIndex("coorderid")) +
                                ",encode_coorderid=" + cursor.getString(cursor.getColumnIndex("encode_coorderid")) +
                                ",mode=" + cursor.getString(cursor.getColumnIndex("mode")) +
                                ",coin=" + cursor.getString(cursor.getColumnIndex("coin")) +
                                ",originaljson=" + cursor.getString(cursor.getColumnIndex("originaljson")) +
                                ",signture=" + cursor.getString(cursor.getColumnIndex("signture")) +
                                ",product=" + cursor.getString(cursor.getColumnIndex("product")) +
                                ",amount=" + cursor.getString(cursor.getColumnIndex("amount")) +
                                ",clientid=" + cursor.getString(cursor.getColumnIndex("clientid")) +
                                ",isPayment=" + cursor.getString(cursor.getColumnIndex("isPayment")) +
                                ",requestStatus=" + cursor.getString(cursor.getColumnIndex("requestStatus")) +
                                ",reason=" + cursor.getString(cursor.getColumnIndex("reason")) +
                                ",serverid=" + cursor.getString(cursor.getColumnIndex("serverid")) +
                                ",encode_transaction_id=" + cursor.getString(cursor.getColumnIndex("encode_transaction_id")) +
                                ",serverid=" + cursor.getString(cursor.getColumnIndex("serverid")) +
                                ",current_time=" + cursor.getString(cursor.getColumnIndex("current_time")) +
                                ",transaction_id=" + cursor.getString(cursor.getColumnIndex("transaction_id")) +
                                ",sku=" + cursor.getString(cursor.getColumnIndex("sku")) +
                                ",isfit=" + cursor.getString(cursor.getColumnIndex("isfit")) +
                                ",Ctext=" + cursor.getString(cursor.getColumnIndex("Ctext"))
                        );

                        curSku = cursor.getString(cursor.getColumnIndex("sku"));
                        curGameId = cursor.getString(cursor.getColumnIndex("clientid"));
                        curServerId = cursor.getString(cursor.getColumnIndex("serverid"));
                        curRoled = cursor.getString(cursor.getColumnIndex("uid"));
                        curSdkuid = cursor.getString(cursor.getColumnIndex("lnid"));
                        curCoorderid = cursor.getString(cursor.getColumnIndex("coorderid"));
                        curOriginaljson = cursor.getString(cursor.getColumnIndex("originaljson"));
                        curSignture = cursor.getString(cursor.getColumnIndex("signture"));
                        curCtext = cursor.getString(cursor.getColumnIndex("Ctext"));
                        curisfit = cursor.getString(cursor.getColumnIndex("isfit"));
                        //上传到后端。无论成功失败都把订单delete
                        HashMap<String, String> map = new HashMap<String, String>();

                        LogUtil.k("gigifunpay_checkfailbill" + "==检查的post");
                        LogUtil.k("补单roleid==" + curRoled);
                        LogUtil.k("补单serverId==" + curServerId);
                        LogUtil.k("补单sdkUid==" + curSdkuid);

                        map.put("Ugamekey", UgameUtil.getInstance().CLIENT_SECRET);
                        map.put("Version", getVersionName(context));
                        if (!"null".equals(curGameId) && !TextUtils.isEmpty(curGameId)) {
                            map.put("Ugameid", curGameId);
                        } else {
                            map.put("Ugameid", "");
                        }
                        if (!"null".equals(curRoled) && !TextUtils.isEmpty(curRoled)) {
                            map.put("Roleid", curRoled);
                        } else {
                            map.put("Roleid", "");
                        }
                        if (!"null".equals(curServerId) && !TextUtils.isEmpty(curServerId)) {
                            map.put("Serverid", curServerId);
                        } else {
                            map.put("Serverid", "");
                        }
                        if (!"null".equals(curSdkuid) && !TextUtils.isEmpty(curSdkuid)) {
                            map.put("Uid", curSdkuid);
                        } else {
                            map.put("Uid", "");
                        }
                        if (!"null".equals(curSdkuid) && !TextUtils.isEmpty(curSdkuid)) {
                            map.put("Uid", curSdkuid);
                        } else {
                            map.put("Uid", "");
                        }

                        if (!"null".equals(curSku) && !TextUtils.isEmpty(curSku)) {
                            map.put("Sku", curSku);
                        } else {
                            map.put("Sku", "");
                        }
                        //在数据库中获取的订单号已经是aes加密，所以直接上传就可以，可以为空
                        if (!"null".equals(curCoorderid) && !TextUtils.isEmpty(curCoorderid)) {
                            map.put("Cp_orderid", dec(curCoorderid));
                        } else {
                            map.put("Cp_orderid", "");
                        }
                        if (!"null".equals(curOriginaljson) && !TextUtils.isEmpty(curOriginaljson)) {
                            map.put("Receive_data", aes(curOriginaljson));
                        } else {
                            map.put("Receive_data", "");
                        }
                        if (!"null".equals(curSignture) && !TextUtils.isEmpty(curSignture)) {
                            map.put("Sign", curSignture);
                        } else {
                            map.put("Sign", "");
                        }
                        //透传参数传CP传过来
//                map.put("Cp_orderid","");
                        if (!"null".equals(curCtext) && !TextUtils.isEmpty(curCtext)) {
                            map.put("Ctext", curCtext);
                        } else {
                            map.put("Ctext", "");
                        }
                        //判断是否为补单，1为补单，0为正常
                        if (!"null".equals(curisfit) && !TextUtils.isEmpty(curisfit)) {
                            map.put("Isfix", curisfit);
                        } else {
                            map.put("Isfix", "");
                        }
                        //把map保存到本地

                        UhttpUtil.post(UgameUtil.getInstance().GOOGLE_PAY, map, new UcallBack() {

                            @Override
                            public void onResponse(String response, int arg1) {
                                try {
                                    JSONObject jo = new JSONObject(response);
                                    String status = jo.optString("Status");
                                    if ("0".equals(status)) {
                                        LogUtil.d("谷歌订单不正确");
                                        //在数据库删除该订单
                                        paydelDb(curCoorderid);

                                    } else if ("1".equals(status)) {
                                        LogUtil.d("谷歌订单正确");
                                        //在数据库删除该订单
                                        paydelDb(curCoorderid);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Call arg0, Exception arg1, int arg2) {
                                LogUtil.i("检查谷歌订单网络连接错误");
                            }
                        });
                        //上传到后端。无论成功失败都把订单delete
                        // delDb(coOrderId);
                    } while (cursor.moveToNext());
                    cursor.close();
                } else {
                    LogUtil.k("gigifunpay-init," + "数据库没有数据");
                }
            }
        }).start();

    }

    public void queryUnfinishBill(Activity activity, String serverId, String roleid, String sdkuid) {
        LogUtil.i("sku: " + sku);
        this.roleid = roleid;
        this.sdkuid = sdkuid;
        this.gameId = UgameUtil.getInstance().GAME_ID;
        this.coin = "USD";
        this.serverId = serverId;
        this.activity = activity;

//    查询商品，执行掉单操作
        if (iap_is_ok) {
            mHelper.queryInventoryAsync(mGotInventoryListener);
            LogUtil.i("调用查询方法");
        }
        //保存用户id 和角色id到手机
        SharedPreferences preferences = activity.getSharedPreferences("LoginCount", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("paysdkUid", sdkuid);
        edit.putString("payroleId", roleid);
        edit.putString("payserverId", serverId);
        LogUtil.k("保存角色uid，sdkuid=" + sdkuid);
        LogUtil.k("保存角色roleid，roleid=" + roleid);
        LogUtil.k("保存角色payserverId，payserverId=" + serverId);
        edit.commit();

        //检查有无未完成订单
        LogUtil.k("gigifunpay-选择角色" + "==检查订单前");
        resentFailBill(activity, serverId);
        LogUtil.k("gigifunpay-选择角色" + "==检查订单后");

    }

    /**
     * @param activity
     * @param serverId  服务器id
     *                  //     * @param roleid    游戏角色id
     *                  //     * @param sdkuid	平台id
     * @param product   商品描述
     *                  //     * @param amount	商品价格
     * @param coOrderId 订单号
     * @param sku       谷歌sku
     * @param Ctext     自定义参数
     */
    public void googlePlay(Activity activity, String serverId, String product,
                           String coOrderId, String sku, String Ctext, GooglePayListener googlePayListener) {

        UgameUtil.getInstance().changeLang(activity);
        this.googlePayListener = googlePayListener;
        LogUtil.k("gigifunpay-googlepay" + "==BEGIN");
        if (iap_is_ok) {
            mHelper.queryInventoryAsync(mGotInventoryListener);
            LogUtil.i("调用查询方法");
        }
        //再检查还有没有未完成订单
        LogUtil.k("gigifunpay-googleplay" + "进入购买前检查未完成订单");
        resentFailBill(activity, serverId);
        LogUtil.k("gigifunpay-googleplay==" + "进入购买前检查完成");

        LogUtil.d("mHelper==" + mHelper);

        SharedPreferences preferences = activity.getSharedPreferences("LoginCount", Context.MODE_PRIVATE);
        paysdkUid = preferences.getString("paysdkUid", "");
        payroleId = preferences.getString("payroleId", "");

        LogUtil.k("购买时的paysdkUid=" + paysdkUid);
        LogUtil.k("购买时的payroleId=" + payroleId);

        // 获取googleplay参数
        this.sku = sku;
        LogUtil.i("sku: " + sku);
        if (null != sku) {
            skus[0] = sku;
        }
        this.gameId = UgameUtil.getInstance().GAME_ID;
        this.coin = "USD";
        this.product = product;
        this.coOrderId = coOrderId;
        this.serverId = serverId;
        this.Ctext = Ctext;
        this.activity = activity;
        LogUtil.k("packname=" + activity.getPackageName());

        LogUtil.d("Gameid: " + gameId + " roleId:" + payroleId + " sdkUid:" + paysdkUid + " serverId:"
                + serverId + " coOrderId:" + coOrderId + " product:" +
                product + "coin" + coin + "Ctext" + Ctext);

        if (TextUtils.isEmpty(payroleId) || TextUtils.isEmpty(paysdkUid) || TextUtils.isEmpty(serverId) ||
                TextUtils.isEmpty(coOrderId)
                ) {
            LogUtil.k("请检查参数类型是否正确");
        }
        if (null == Ctext) {
            Ctext = "";
        }
        if (null == skus[0] || "".equals(skus[0])) {
            Toast.makeText(context, MResource.getIdByName(context, "string", "recharge_failure"), Toast.LENGTH_SHORT).show();
            return;
        }
        LogUtil.k("gamepay-googleplay," + " iap_is_ok=" + iap_is_ok);
        if (iap_is_ok) {
            //传送消息到handler中调用支付
            handler.sendEmptyMessage(PAY_INSERT);
        } else {
            Toast.makeText(context, MResource.getIdByName(context, "string", "google_play_initialization_failure"), Toast.LENGTH_SHORT).show();
        }
        LogUtil.k("gigifunpay-googlepay" + "==END");
    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {

            if (mHelper == null)
                return;
            if (result.isFailure()) {
                LogUtil.v("查询失败 " + result.getMessage());
//                Toast.makeText(context, result.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }

            int unComsumeSize = inventory.getAllOwnedSkus().size();
            LogUtil.d("未消耗商品数量：" + unComsumeSize);
            List<String> allOwnedSkus = inventory.getAllOwnedSkus();

            if (unComsumeSize > 0) {
                for (int i = 0; i < unComsumeSize; i++) {
                    final Purchase purchase = inventory.getPurchase(allOwnedSkus.get(i));
                    LogUtil.k("查询未消耗purchase==" + purchase + "");

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                    String date = format.format(calendar.getTime());
                    reOrderId = "fill_" + date;

//                    查询到未消耗，post到服务器上
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("Ugamekey", UgameUtil.getInstance().CLIENT_SECRET);
                    map.put("Ugameid", UgameUtil.getInstance().GAME_ID);
                    map.put("Roleid", roleid);
                    map.put("Serverid", serverId);
                    map.put("Uid", sdkuid);
                    map.put("Cp_orderid", reOrderId);
                    map.put("Receive_data", aes(purchase.getOriginalJson())); //对Receive_data进行aes加密
                    map.put("Version", getVersionName(context));
                    map.put("Sku", purchase.getSku());
                    map.put("Sign", purchase.getSignature());
                    map.put("Ctext", "");
                    map.put("Isfix", "1");
                    map.put("packname", activity.getPackageName());
                    //要不要插入数据库,保存到数据库后如果得不到后台回调，就要等下一次，
                    insertDb("2", "haspay", "2", "refill", purchase, reOrderId, "1");
                    if (null != mHelper && null != purchase) {
                        LogUtil.d("states 0 查询未消耗 执行消耗之前 ");
                        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                        LogUtil.d("states 0 查询未消耗 执行消耗之后 ");
                    }
                    UhttpUtil.post(UgameUtil.getInstance().GOOGLE_PAY, map, new UcallBack() {

                        @Override
                        public void onResponse(String response, int arg1) {
                            try {
                                JSONObject jo = new JSONObject(response);
                                String status = jo.optString("Status");
                                if ("0".equals(status)) {
                                    Toast.makeText(context, MResource.getIdByName(context, "string", "link_error"), Toast.LENGTH_SHORT)
                                            .show();
                                    LogUtil.d("谷歌订单不正确");
                                    paydelDb(aes(reOrderId));
                                } else if ("1".equals(status)) {
                                    String mount = jo.getString("amount");
                                    //把金额改成从服务上取
                                    LogUtil.k("amount = " + mount);
                                    LanucherMonitor.getInstance().payTrack(context, payroleId + "_" + serverId + "_" + paysdkUid, mount, "googlePay");
                                    paydelDb(aes(reOrderId));
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
        }
    };

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            LogUtil.i("谷歌支付结果: " + purchase);

            payStyle = 1;
            if (null != purchase) {
                mPurchase = purchase;
                transactionId = purchase.getOrderId();//谷歌的订单号
                signture = purchase.getSignature();
                originalJson = purchase.getOriginalJson();
            }

            if (result.isFailure()) {
                LogUtil.i("response: " + result.getResponse());
                if (result.getResponse() == IabHelper.IABHELPER_USER_CANCELLED) {
                    if (null == purchase) {

                    }
                }

                if (null == purchase) {
                    Toast.makeText(context, MResource.getIdByName(context, "string", "user_cancel"), Toast.LENGTH_SHORT)
                            .show();
                    googlePayListener.cancel("cancel");
                    LogUtil.i("支付失败 : " + result.getMessage());
                } else {
                    int purchaseState = purchase.getPurchaseState();
                    if (0 == purchaseState) {//已扣钱
                        Toast.makeText(context, MResource.getIdByName(context, "string", "pay_error") + purchaseState, Toast.LENGTH_SHORT)
                                .show();
                        googlePayListener.cancel("error");
                    } else {
                        LogUtil.i("其他情况");
                        Toast.makeText(context, MResource.getIdByName(context, "string", "pay_error") + purchaseState, Toast.LENGTH_SHORT)
                                .show();
                        googlePayListener.cancel("error");
                    }
                }
                return;
            } else {
                int purchaseState = purchase.getPurchaseState();
                LogUtil.i("支付成功  purchaseState=" + purchaseState);
                if (0 == purchaseState) {//已扣钱
//                    purchase.setSku("com.iiugame.sdk.99");
//                    LogUtil.i("谷歌sku结果: "+purchase.getSku());
                    LogUtil.k("谷歌支付成功结果: " + purchase);
                    googlePay(purchase);
                } else {
                    LogUtil.i("其他情况");
                }
            }


        }
    };
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            LogUtil.e("PayActivity 消费结果 " + result.getMessage());
            /**
             * 消费结果提交日志
             */
            LogUtil.e("正常支付：  消费是否成功 ： " + result.isSuccess());
            if (result.isSuccess()) {
                //delDb(coOrderId);
                LogUtil.e("PayActivity 消费成功");
            } else {
                LogUtil.e("PayActivity 消费失败");
            }
            onBack();
        }
    };

    private void googlePay(final Purchase purchase) {

        if (null == purchase) {
            LogUtil.i("正常支付： 查询谷歌订单是否正确，purchase为空 ");
            return;
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("Ugamekey", UgameUtil.getInstance().CLIENT_SECRET);
        map.put("Ugameid", UgameUtil.getInstance().GAME_ID);
        map.put("Roleid", payroleId);
        map.put("Serverid", serverId);
        map.put("Uid", paysdkUid);
        map.put("Cp_orderid", coOrderId);
        map.put("Receive_data", aes(purchase.getOriginalJson())); //对Receive_data进行aes加密
        map.put("Version", getVersionName(context));
        map.put("Sku", purchase.getSku());
        map.put("Sign", purchase.getSignature());
        map.put("Ctext", Ctext);
        map.put("Isfix", "0");
        map.put("packname", activity.getPackageName());

        //测试未成功消耗
//        int i = 2/0;
        insertDb("1", "before Paying", "1", "before pay", purchase, coOrderId, "0");

        if (null != mHelper && null != purchase) {
            LogUtil.d("states 0 购买成功 执行消耗之前 ");
            mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            LogUtil.d("states 0 购买成功 执行消耗之后 ");
        }
//        int i = 2/0;
        UhttpUtil.post(UgameUtil.getInstance().GOOGLE_PAY, map, new UcallBack() {

            @Override
            public void onResponse(String response, int arg1) {
                try {
                    JSONObject jo = new JSONObject(response);
                    String status = jo.optString("Status");
                    if ("0".equals(status)) {
                        Toast.makeText(context, MResource.getIdByName(context, "string", "link_error"), Toast.LENGTH_SHORT)
                                .show();
                        LogUtil.k("谷歌订单不正确");

                        googlePayListener.cancel("google orderNo error");

                        //在数据库删除该订单
                        LogUtil.k("购买完成后删除订单" + "订单号为==" + coOrderId + ",加密后=" + aes(coOrderId));
                        paydelDb(aes(coOrderId));

                    } else if ("1".equals(status)) {

                        googlePayListener.success("success");
                        LogUtil.k("购买完成后删除订单," + "订单号为==" + coOrderId + ",加密后=" + aes(coOrderId));
                        LogUtil.k("谷歌订单正确");
                        //把金额改成从服务器上取
                        String mount = jo.getString("amount");
                        LogUtil.k("amount = " + mount);
                        LanucherMonitor.getInstance().payTrack(context, payroleId + "_" + serverId + "_" + paysdkUid, mount, "googlePay");
                        //在数据库删除该订单
                        paydelDb(aes(coOrderId));
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Call arg0, Exception arg1, int arg2) {
                LogUtil.i("检查谷歌订单网络连接错误");
//                    googlePay(purchase);
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
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    private boolean isInstallGoogleStore() {
        List<String> packages = new ArrayList<String>();
        List<PackageInfo> appList = getAllApps(context);
        for (int i = 0; i < appList.size(); i++) {
            PackageInfo pinfo = appList.get(i);
            packages.add(pinfo.packageName);
        }

        if (!packages.contains("com.android.vending")) {
            try {
//                Toast.makeText(context, MResource.getIdByName(context, "string", "the_device_is_not_installed_google_play_store"),
//                        Toast.LENGTH_SHORT).show();
                LogUtil.k("没有安装谷歌商店");
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

    public static List<PackageInfo> getAllApps(Context context) {

        ArrayList<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager pManager = context.getPackageManager();
        // 获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = paklist.get(i);
            apps.add(pak);
        }
        return apps;
    }


    private void insertDb(final String mode, final String ispay, final String requestStatus, final String reason,
                          final Purchase purchase, final String orderid, final String isfit) {

        LogUtil.k("originalJson=" + purchase.getOriginalJson() + ",signture=" + purchase.getSignature() + ",orderid=" + orderid);
        new Thread() {
            @Override
            public void run() {
                super.run();
                ContentValues values = new ContentValues();
                values.put(DBFile.UID, payroleId);
                values.put(DBFile.LNID, paysdkUid);
//                String aescoOrderId=aes(coOrderId);
                String aescoOrderId = aes(orderid);
                values.put(DBFile.COORDERID, aescoOrderId);
                values.put(DBFile.ENCODE_COORDERID, MD5Utils.md5Sign(aescoOrderId));
                values.put(DBFile.MODE, mode);
                values.put(DBFile.COIN, coin);
                values.put(DBFile.PRODUCT, product);
                values.put(DBFile.AMOUNT, "");
                values.put(DBFile.SKU, sku);
                values.put(DBFile.CLIENTID, gameId);
                values.put(DBFile.SERVERID, serverId);
                values.put(DBFile.ISPAYMENT, ispay);
                values.put(DBFile.REQUESTSTATUS, requestStatus);
                values.put(DBFile.REASON, reason);
                values.put(DBFile.TRANSACTION_ID, transactionId);
                values.put(DBFile.ENCODE_TRANSACTION_ID, encodeTransactionId);
                values.put(DBFile.ORIGINALJSON, purchase.getOriginalJson());
                values.put(DBFile.SIGNTURE, purchase.getSignature());
                values.put(DBFile.CTEXT, Ctext);
                values.put(DBFile.ISFIT, isfit);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = format.format(calendar.getTime());
                values.put(DBFile.CURRENT_TIME, date);

                SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
                long insert = DBDao.insertPay(db, values);
                if (insert > 0) {
                    LogUtil.e("插入数据库成功: " + insert);
                } else {
                    LogUtil.e("插入数据库失败");
                }
                DatabaseManager.getInstance().closeDatabase();
            }
        }.start();

    }
//private void insertDb(final String mode,final String ispay,final String requestStatus,final String reason
//        ){
//    new Thread(){
//        @Override
//        public void run() {
//            super.run();
//            LogUtil.k("payroleid="+payroleId+",paysdkUid="+paysdkUid+",coOrderId="+coOrderId+",serverId="+serverId);
//            ContentValues values=new ContentValues();
//            values.put(DBFile.UID, payroleId);
//            values.put(DBFile.LNID, paysdkUid);
//            String aescoOrderId=aes(coOrderId);
//            values.put(DBFile.COORDERID, aescoOrderId);
//            values.put(DBFile.ENCODE_COORDERID, MD5Utils.md5Sign(aescoOrderId));
//            values.put(DBFile.MODE, mode);
//            values.put(DBFile.COIN, coin);
//            values.put(DBFile.PRODUCT, product);
//            values.put(DBFile.AMOUNT, aes(amount));
//            values.put(DBFile.SKU, sku);
//            values.put(DBFile.CLIENTID, gameId);
//            values.put(DBFile.SERVERID, serverId);
//            values.put(DBFile.ISPAYMENT, ispay);
//            values.put(DBFile.REQUESTSTATUS, requestStatus);
//            values.put(DBFile.REASON, reason);
//            values.put(DBFile.TRANSACTION_ID, transactionId);
//            values.put(DBFile.ENCODE_TRANSACTION_ID, encodeTransactionId);
//            values.put(DBFile.ORIGINALJSON, originalJson);
//            values.put(DBFile.SIGNTURE, signture);
//            values.put(DBFile.CTEXT, Ctext);
//            Calendar calendar=Calendar.getInstance();
//            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String date=format.format(calendar.getTime());
//            values.put(DBFile.CURRENT_TIME, date);
//
//            SQLiteDatabase db=DatabaseManager.getInstance().openDatabase();
//            long insert= DBDao.insertPay(db, values);
//            if(insert>0){
//                LogUtil.i("插入数据库成功: "+insert);
//            }else{
//                LogUtil.i("插入数据库失败");
//            }
//
//            DatabaseManager.getInstance().closeDatabase();
//        }
//
//    }.start();
//
//
//}

    private void delDb(final String coorderid) {
        new Thread() {

            @Override
            public void run() {
                super.run();
                SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

                int del = DBDao.delPay(db, MD5Utils.md5Sign(coorderid));
                if (del > 0) {
                    LogUtil.i("删除数据成功: " + del);
                } else {
                    LogUtil.i("删除数据库失败: ");

                    /**
                     * 删除数据失败 发送日志
                     */
                    //getLog("删除数据失败");
                }
                DatabaseManager.getInstance().closeDatabase();

            }

        }.start();

    }

    /**
     * delete bill according to coorderid number
     *
     * @param coorderid
     */
    private void paydelDb(final String coorderid) {
        new Thread() {

            @Override
            public void run() {
                super.run();
                SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
                int del = DBDao.delPay(db, MD5Utils.md5Sign(coorderid));
                LogUtil.k("gigifunpay_删除订单====" + coorderid);
                if (del > 0) {
                    LogUtil.k("删除数据成功: " + del);
                } else {
                    LogUtil.k("删除数据库失败: ");

                }

                DatabaseManager.getInstance().closeDatabase();
                //handler.sendEmptyMessage(PAY_DEL);
            }

        }.start();

    }

    /**
     * 更新数据库
     *
     * @param coorderid
     * @param uid
     * @param lnid
     * @param mode
     * @param ispay
     * @param requestStatus
     * @param reason
     */
    private void updateDb(final String coorderid, final String uid, final String lnid, final String mode, final String ispay, final String requestStatus, final String reason) {
        new Thread() {

            @Override
            public void run() {
                super.run();
                ContentValues values = new ContentValues();
                values.put(DBFile.MODE, mode);
                values.put(DBFile.ISPAYMENT, ispay);
                values.put(DBFile.REQUESTSTATUS, requestStatus);
                values.put(DBFile.REASON, reason);
                if (payStyle == 1) {
                    String aes_transactionId = aes(transactionId);
                    values.put(DBFile.TRANSACTION_ID, aes_transactionId);
                    values.put(DBFile.ENCODE_TRANSACTION_ID, MD5Utils.md5Sign(aes_transactionId));
                    values.put(DBFile.ORIGINALJSON, aes(originalJson));
                    values.put(DBFile.SIGNTURE, aes(signture));
                }
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = format.format(calendar.getTime());
                values.put(DBFile.CURRENT_TIME, date);
                SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
                int update = DBDao.updataPayResult(db, values, new String[]{uid, lnid, MD5Utils.md5Sign(aes(coorderid))});
                if (update > 0) {
                    LogUtil.i("修改数据成功: " + update);
                } else {
                    LogUtil.i("修改数据库失败: ");


                    /**
                     * 修改数据失败 发送日志
                     */

                    // getLog("修改数据失败");
                }
                DatabaseManager.getInstance().closeDatabase();
            }

        }.start();

    }


    private void exceptionupdateDb(final String coorderid, final String uid, final String lnid, final String mode, final String ispay, final String requestStatus, final String reason) {
        new Thread() {

            @Override
            public void run() {
                super.run();
                ContentValues values = new ContentValues();
                values.put(DBFile.MODE, mode);
                values.put(DBFile.ISPAYMENT, ispay);
                values.put(DBFile.REQUESTSTATUS, requestStatus);
                values.put(DBFile.REASON, reason);
                if (payStyle == 1) {
                    String aes_transactionId = aes(transactionId);
                    values.put(DBFile.TRANSACTION_ID, aes_transactionId);
                    values.put(DBFile.ENCODE_TRANSACTION_ID, MD5Utils.md5Sign(aes_transactionId));
                    values.put(DBFile.ORIGINALJSON, aes(originalJson));
                    values.put(DBFile.SIGNTURE, aes(signture));
                }
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = format.format(calendar.getTime());
                values.put(DBFile.CURRENT_TIME, date);

                SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
                int update = DBDao.updataPayResult(db, values, new String[]{uid, lnid, MD5Utils.md5Sign(aes(coorderid))});
                if (update > 0) {
                    LogUtil.i("修改数据成功: " + update);
                } else {
                    LogUtil.i("修改数据库失败: ");

                    /**
                     * 修改数据失败 发送日志
                     */

                    //getLog("修改数据失败");
                }
                DatabaseManager.getInstance().closeDatabase();
                //handler.sendEmptyMessage(PAY_UPDATE);
            }

        }.start();

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

    public class MHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PAY_INSERT:
                    //写入数据成功
                    LogUtil.k("gigifunpay-handler-进入购买," + "paystyle=" + payStyle);
                    if (payStyle == 1) {//谷歌
                        LogUtil.k("gigifunpay-handler-进入购买," + "sku=" + skus[0] + ",RC_REQUEST=" + RC_REQUEST);
                        try {
                            mHelper.launchPurchaseFlow(activity, skus[0], RC_REQUEST,
                                    mPurchaseFinishedListener, coOrderId);
                        } catch (Exception e) {
                            try {
                                Toast.makeText(context, MResource.getIdByName(context, "string", "checking_unconsume_order"), Toast.LENGTH_LONG).show();
                            } catch (Resources.NotFoundException e1) {
                                e1.printStackTrace();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    private void onBack() {
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void onDestroy() {
        UgameUtil.getInstance().PAY_STATE = 0;
        try {
            if (mHelper != null) mHelper.dispose();
            mHelper = null;
        } catch (IllegalArgumentException e) {
            LogUtil.d("Error: " + e.getMessage());
            mHelper = null;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (null != mHelper) {
            if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {

            }
        }
    }
}
