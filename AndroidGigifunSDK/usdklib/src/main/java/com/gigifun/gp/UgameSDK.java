package com.gigifun.gp;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.IBinder;

import com.gigifun.gp.listener.GooglePayListener;
import com.gigifun.gp.listener.IFuntionCheck;
import com.gigifun.gp.listener.OnFloatLintener;
import com.gigifun.gp.listener.OnInitListener;
import com.gigifun.gp.listener.OnLoginListener;
import com.gigifun.gp.listener.OnThirdPurchaseListener;
import com.gigifun.gp.listener.OnUpGradeListener;
import com.gigifun.gp.ui.LoginDialog;
import com.gigifun.gp.utils.ButtonUtil;
import com.gigifun.gp.utils.UgameUtil;
import com.gigifun.gp.utils.LanucherMonitor;
import com.gigifun.gp.utils.LogUtil;

import com.gigifun.gp.service.FloatViewService;
import com.gigifun.gp.widget.FloatView;
import com.gigifun.gp.ui.FloatWebviewDialog;

import kr.co.namee.permissiongen.PermissionGen;


/**
 * Created by tim on 16/8/29.
 */
public class UgameSDK {
    private static Activity mActivity;
    private UgamePay googlePay;
    private static UgameSDK instance = null;
    private int type = 0;
    private SharedPreferences preferences;


    public static UgameSDK sdkInitialize(Activity activity) {
        mActivity = activity;
        if (instance == null) {
            synchronized (UgameSDK.class) {
                if (instance == null) {
                    instance = new UgameSDK(activity);

                    LogUtil.k("20170927,Version=1.0.0");
                }
            }
        }
        return instance;
    }


    public static UgameSDK getInstance() {

        if (instance == null) {
            synchronized (UgameSDK.class) {
                if (instance == null) {
                    sdkInitialize(mActivity);
                }
            }
        }
        return instance;
    }

    public UgameSDK(Context context) {
        LogUtil.w("---ning test UgameSDK()");
        //xuanfu
        try {

            LogUtil.w("---ning test FloatViewService");
//            Intent intent = new Intent(context, FloatViewService.class);
//            context.startService(intent);
//            context.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            LogUtil.w("---ning test Exception");
            LogUtil.w(e.getMessage());
        }

        PermissionGen.with((Activity) context)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .request();
        initSDK(context);

    }


    /**
     * 初始化SDK APPFLY统计 Facebook
     *
     * @param context
     */
    public void initSDK(Context context) {
        LogUtil.w("---init facebook 之前");
        LogUtil.w("___init gigifunData之前");
        LogUtil.w("----init---");
        UgameUtil.uInitialize(context);
        LanucherMonitor.LanucherInitialize((Activity) context);

        initUData(context);
        LogUtil.w("___init gigifunData 之后");



}

    /***
     * 谷歌支付
     * @param serverId  服务器ID
     * @param product   商品描述
     * @param coOrderId 游戏商订单号
     * @param sku       谷歌计费点SKU
     * @param Ctext     自定义参数(可选)
     */

    public void googlePay(Activity activity, String serverId, String product,
                          String coOrderId, String sku, String Ctext, GooglePayListener googlePayListener) {

        if (googlePay != null) {
            if (ButtonUtil.isFastDoubleClick()) {
                return;
            }
            googlePay.googlePlay(activity, serverId, product, coOrderId, sku, Ctext,googlePayListener);
        }

    }

    public boolean isChoolse = false;
    public void checkFailBill(Activity activity, String serverId, String roleid, String sdkuid,String sPcText,IFuntionCheck mFuntionCheck) {
        if (googlePay != null) {
            if (ButtonUtil.isFastDoubleClick()) {
                return;
            }
           googlePay.queryUnfinishBill(activity, serverId, roleid, sdkuid);
        }
        UgameUtil.getInstance().checkFuction(serverId, roleid, sdkuid, sPcText,mFuntionCheck,mFloatViewService);
        isChoolse = true;
    }

    public void login(Activity activity, OnLoginListener onLoginListener) {

        new LoginDialog(activity,onLoginListener, onFloatLintener);
    }


    /**
     * 第三方支付
     *
     * @param serverId 服务器ID
     *                 //     * @param roleid    游戏角色ID
     *                 //     * @param sdkuid    平台ID
     * @param Ctext    自定义参数(可选)
     */
    public void morePay(String serverId, String Ctext) {
        if (ButtonUtil.isFastDoubleClick()) {
            return;
        }
//        FloatWebviewDialog.show(activity,serverId,roleid,sdkuid,Ctext);
        Intent intent = new Intent(mActivity, MorePay.class);
        intent.putExtra("serverId", serverId);
        intent.putExtra("sPcText", Ctext);
        mActivity.startActivity(intent);
    }

    /**
     * 跳转谷歌商店
     *
     * @param pacageName
     */
    public void goGooglePlay(Context context, String pacageName) {
        if (ButtonUtil.isFastDoubleClick()) {
            return;
        }
        UgameUtil.getInstance().goGooglePlay(context, pacageName);
    }


    /**
     * 打开FB活动页面
     *
     * @param serverid 服务器ID
     *                 //     * @param roleid     游戏角色ID
     *                 //     * @param sdkuid    平台ID
     */
    public void startForGift(Activity activity, String serverid) {
        if (ButtonUtil.isFastDoubleClick()) {
            return;
        }
//        new GiftDialog(activity, serverid,mFloatViewService);
    }

    public void startForGift1(Activity activity, String serverid) {
        if (ButtonUtil.isFastDoubleClick()) {
            return;
        }
    }

    public void showFloatView(Activity activity, String serverid){
        if (ButtonUtil.isFastDoubleClick()) {
            return;
        }
//        new FloatWebviewDialog(activity, serverid);
    }


    /**
     * 取消自动登录
     *
     * @param isAuto
     */
    public void setAutoLoginStauts(Context context, boolean isAuto) {
        UgameUtil.getInstance().setAutoLoginStauts(context, isAuto);
    }


    public void initUData(Context context) {
        LogUtil.d("initUData");
        //繁体
        UgameUtil.getInstance().changeLang(context);

        preferences = mActivity.getSharedPreferences("LoginCount", Context.MODE_PRIVATE);

        UgameUtil.getInstance().getKeyhash(context);
        //初始化谷歌
        UgameUtil.getInstance().init(context, new OnInitListener() {
            @Override
            public void initGooglePay(UgamePay pay1) {
                googlePay = pay1;
                LogUtil.d("googlePay" + googlePay);
            }
        });
        //初始化强更
        UgameUtil.getInstance().getUpGradeGame(context, new OnUpGradeListener() {
            @Override
            public void success() {
                LogUtil.k("UgameSDK-initEd" + "更新");
            }

            @Override
            public void faile() {
                LogUtil.k("UgameSDK-initEd" + "失败");
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (googlePay != null) {
            googlePay.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void ShowWebView(OnThirdPurchaseListener onThirdPurchaseListener)
    {
        boolean isVer=true;//默认是横屏
        switch(mActivity.getResources().getConfiguration().orientation){

            case Configuration.ORIENTATION_LANDSCAPE:
                //横屏
                isVer=true;
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                //竖屏
                isVer=false;
                break;
            default:
                //默认是横屏
                isVer=true;
        }
        new FloatWebviewDialog(mActivity, "1", FloatView.floatViewService, "pay",isVer, onThirdPurchaseListener);
    }

    public void onDestroy() {
        LogUtil.k("onDestroy");
        if (googlePay != null) {
            googlePay.onDestroy();
        }
        instance = null;
        UgameUtil.getInstance().clean();
        destroy();

    }
    public void onResume(){
        LogUtil.k("onResume");
        String floatStatus = preferences.getString("floatStatus", "");
        String floatgiftStatus = preferences.getString("floatgiftStatus", "");
//        boolean isChoose = preferences.getBoolean("isChoose", false);
        if ("0".equals(floatStatus)&&isChoolse){
            showFloatingView();
        }
//
    }

    public void onStop(){
        LogUtil.k("onStop() onFloatLintener=="+onFloatLintener);
        hideFloatingView();
    }
    /**
     * 释放PJSDK数据
     */
    public void destroy() {
        try {
            if (mFloatViewService!= null){
                mActivity.stopService(new Intent(mActivity, FloatViewService.class));
            }

            mActivity.unbindService(mServiceConnection);

        } catch (Exception e) {
        }
    }
    private  FloatViewService mFloatViewService;
    /**
     * 连接到Service
     */
    //xuanfu
    public OnFloatLintener onFloatLintener;
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LogUtil.k("init,成功连接服务");
            FloatViewService.FloatViewServiceBinder iBinder1 = (FloatViewService.FloatViewServiceBinder) iBinder;
            mFloatViewService = (iBinder1).getService();
            onFloatLintener = (OnFloatLintener) iBinder;
            LogUtil.k("init,成功连接服务"+ onFloatLintener);

            LogUtil.k("UgameSDK mFloatViewService=="+mFloatViewService);
            //传入xuanfu接口和上下文
            new FloatView(mActivity,mFloatViewService);
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            LogUtil.k("init,连接服务失败");
            mFloatViewService = null;
        }
    };

    /**
     * 隐藏悬浮图标
     */
    public void hideFloatingView() {
        LogUtil.k("mFloatViewService hide===="+mFloatViewService);
        if ( mFloatViewService != null ) {
            mFloatViewService.hideFloat();
        }
    }

    /**
     * 显示悬浮图标
     */
    public  void showFloatingView() {
        LogUtil.k("mFloatViewService===="+mFloatViewService);
        if ( mFloatViewService != null ) {
            mFloatViewService.showFloat();
        }
    }
}
