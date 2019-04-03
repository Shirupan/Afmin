package com.stone.api;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.widget.Toast;

import com.stone.lib.internal.ChannelCache;
import com.stone.lib.config.BaseConfig;
import com.stone.lib.utils.LogUtils;
import com.stone.single.pack.entity.PayResult;
import com.stone.single.pack.notifier.ExitListener;
import com.stone.single.pack.notifier.InitListener;
import com.stone.single.pack.notifier.LoginListener;
import com.stone.single.pack.notifier.LogoutListener;
import com.stone.single.pack.notifier.PayOrderListener;
import com.stone.single.pack.notifier.PayResultListener;
import com.stone.jo.api.ChannelApi;
import com.stone.jo.api.ChargeFrameInterface;
import com.stone.jo.utils.JoUtils;
import com.stone.lib.config.URLConfig;
import com.stone.lib.internal.RequestExecutor;
import com.stone.lib.plugin.PluginManager;
import com.stone.lib.plugin.PluginResult;
import com.stone.lib.plugin.PluginResultHandler;
import com.stone.lib.plugin.interfaces.PluginInterface;
import com.stone.lib.plugin.leisure.interfaces.OnLifeCycleListener;

public final class MainSDK {

    private static MainSDKLoginListener notifer;
    private static final String TAG = "MainSDK";

    public static final int SOUND_STATUS_ON = 1;
    public static final int SOUND_STATUS_OFF = 2;
    public static final int SOUND_STATUS_LAST_SET = 3;
    protected static boolean isDebug = false;

    private static boolean isSettingPer = false;

    /**
     * 登录的用户信息
     */
    public static class UserInfo {
        public String username; // 用户名
        public String nickname; // 用户昵称
        public String sessionId;
    }

    /**
     * SDK初始化、登录回调
     */
    public static interface MainSDKLoginListener {

        public void onLoginFailed(int errorCode, String reason);

        /**
         * @param user {@link UserInfo}
         */
        public void onLoginSuccess(UserInfo user);

        /**
         * 取消登录
         */
        public void onCancel();

        /**
         * 登录注销
         */
        public void onLogOut();
    }

    /**
     * 公用回调
     */
    public static abstract class MainSDKCallBack {

        /**
         * 失败回调
         *
         * @param code
         * @param error
         */
        public abstract void onFailed(int code, String error);

        /**
         * 成功回调
         *
         * @param data
         */
        public abstract void onSucceeded(String data);

    }

    /**
     * 退出回调
     */
    public static interface ExitCallBack {

        public void onExitConfirmed();

        public void onExitCanceled();
    }

    /**
     * 下载游戏存档回调
     */
    public static interface MainSDKDownloadListener {

        /**
         * 成功返回数据
         */
        public void onSuccess(Object obj);

        /**
         * 失败
         */
        public void onFailed(int code, String error);


    }

    /**
     * 退出回调
     */
    public static interface MainSDKExitListener {

        /**
         * 退出确认
         */
        public void onExitConfirmed();

        /**
         * 退出取消
         */
        public void onExitCanceled();
    }

    /**
     * 支付回调
     */
    @Deprecated
    public static interface MainSDKPayResultListener {

        /**
         * 支付成功
         *
         * @param identifier 所购买的道具标识
         */
        public void onPaySucceeded(String identifier);

        /**
         * 支付失败
         *
         * @param identifier 所购买的道具标识
         * @param errorMsg   失败信息
         */
        public void onPayFailed(String identifier, String errorMsg);

        /**
         * 取消支付（不是所有支付方式都支持）
         *
         * @param identifier
         */
        public void onCanceled(String identifier);

    }


    /**
     * 通用接口
     */
    public static interface MainSDKCommonListener {

        public void onSucceed(Object data);

        public void onFailed(int code, String error);

        public void onCanceled();
    }

    public static Handler sApiHandler;
    protected static boolean sHasInitializingDone = false;
    protected static final Handler sMainHandler = new Handler(
            Looper.getMainLooper());

    /**
     * 初始化接口
     *
     * @param activity
     */
    public static void initialize(final Activity activity) {
        // -------------------------初始化接口调用线程-------------------------//
        // ---------------将所有接口抛在一个单独的线程执行，以免阻塞UI线程----------------//
        if (sApiHandler == null) {
            HandlerThread ht = new HandlerThread("api_call_thread",
                    android.os.Process.THREAD_PRIORITY_BACKGROUND);
            ht.start();
            sApiHandler = new Handler(ht.getLooper());
        }

        try {
            String str = JoUtils.readFile(activity.getAssets().open(
                    "stone/config"));//json字符串取数据
            JSONObject obj = new JSONObject(str);
            String one = obj.optString("one");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //设置环境
        URLConfig.checkConfig(activity);
        RequestExecutor.init();
        isDebug = Boolean.valueOf(ChannelCache.getInstance(activity).getParamsValue("isdebug"));
        sHasInitializingDone = true;

        //加载所有插件
        PluginManager.getDefault(activity).loadAllPlugins();

        // 初始化渠道插件
        initModules(activity);
    }

    private static void initModules(final Activity activity) {
        // 2.-------------------------设置环境----------------------------//
        //先读取到地区以后才能分配域名
        URLConfig.checkConfig(activity);

        ChannelApi channelApi = (ChannelApi) PluginManager.getDefault(activity).findPlugin("jo");

        channelApi.init(activity, new InitListener() {

            @Override
            public void onSuccess() {
//                渠道初始化成功
            }

            @Override
            public void onFailed(final int code, final String msg) {
            }
        });

        Runnable r = new Runnable() {

            @Override
            public void run() {

                //初始化会检查更新
                invokePluginMethod("basic", "initialize", new Class<?>[]{
                                Activity.class, PluginResultHandler.class},
                        new Object[]{activity, null});

            }
        };
        sApiHandler.post(r);
    }

    private static void initPaySDK(final Activity activity) {
        showTips(activity, "initPaySDK");
        ChargeFrameInterface payApi = (ChargeFrameInterface) PluginManager
                .getDefault(null).findPlugin("pay");
        payApi.initPay(activity, new PluginResultHandler() {

            @Override
            public void onHandlePluginResult(PluginResult result) {

            }
        });
    }

    private static Object invokePluginMethod(String pluginName, String method,
                                             Class<?>[] argClasses, Object[] args) {
        PluginManager pm = PluginManager.getDefault(null);
        PluginInterface p = pm.findPlugin(pluginName);
        return p.invoke(method, argClasses, args);
    }

    private static void findPlugin(final String method,
                                   final Class<?>[] argClasses, final Object[] args) {
        PluginManager pm = PluginManager.getDefault(null);
        ArrayList<PluginInterface> initInterfaces = pm
                .findAllPlugins(OnLifeCycleListener.class);
        for (final PluginInterface initInterface : initInterfaces) {
            if (initInterface instanceof OnLifeCycleListener) {
                initInterface.invoke(method, argClasses, args);
            }
        }
    }


    private static void invoke(String className, String method, Class<?>[] cls,Object[] obj) {
        try {
            Class<?> classObj = Class.forName(className);
            Method apiMethod = classObj.getMethod(method, cls);
            int modifiers = apiMethod.getModifiers();
            boolean isStatic = Modifier.isStatic(modifiers);
            Object objInstance = null;
            if (!isStatic) {
                objInstance = classObj.newInstance();
            }
            apiMethod.invoke(objInstance, obj);
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    /**
     *
     * @param activity  Activity
     * @param extral    支付参数
     * @param listener  pay result callback
     */
    public static void pay(final Activity activity, HashMap<String, Object> extral, final PayResultListener listener) {
        LogUtils.d(TAG, "channelPay(Activity activity, int payType, String productId,String extral,PayResultListener listener)");
        if (isDebug) {
            showDialog(activity, "pay", extral.toString(),
                    listener);
            return;
        }
        ChannelApi joApi = (ChannelApi) PluginManager.getDefault(null)
                .findPlugin("jo");
        //判断支付成功
        joApi.pay(activity, extral, new PayOrderListener() {

            @Override
            public void onPayNotify(final PayResult result) {
                listener.onPayNotify(result);
            }

            @Override
            public void onCreateOrder(PayResult result) {
                ((PayOrderListener) listener).onCreateOrder(result);
            }
        });
    }


    /**
     * 退出帐号接口
     *
     * @param activity
     */
    public static void logout(Activity activity) {
        showTips(activity, "logout");
        ChannelApi joApi = (ChannelApi) PluginManager.getDefault(null)
                .findPlugin("jo");
        joApi.logout(activity, new LogoutListener() {

            @Override
            public void onSuccess() {
                LogUtils.d(TAG, "logout success " + (notifer == null ? "null" : "not null"));
                if (notifer != null) {
                    notifer.onLogOut();
                }
            }

            @Override
            public void onFailed(int arg0, String arg1) {
                LogUtils.d(TAG, "logout onFAILED code: " + arg0 + "  args: " + arg1);
            }
        });
    }

    /**
     * Activity 生命周期函数
     *
     * @param activity
     */
    public static void onActivityResult(Activity activity, int requestCode,
                                        int resultCode, Intent data) {
        showTips(activity, "onActivityResult");
        findPlugin("onActivityResult", new Class<?>[]{int.class, int.class,
                Intent.class}, new Object[]{requestCode, resultCode, data});
    }

    /**
     * Activity 生命周期函数
     *
     * @param activity
     */
    public static void onStart(Activity activity) {
        showTips(activity, "onStart");
        findPlugin("onStart", new Class<?>[]{Activity.class},
                new Object[]{activity});
    }

    /**
     * Activity 生命周期函数
     *
     * @param activity
     */
    public static void onPause(final Activity activity) {
        showTips(activity, "onPause");
        findPlugin("onPause", new Class<?>[]{Activity.class},
                new Object[]{activity});
    }

    /**
     * Activity 生命周期函数
     *
     * @param activity
     */
    public static void onCreate(Activity activity) {
        showTips(activity, "onCreate");
        findPlugin("onCreate", new Class<?>[]{Activity.class, Bundle.class},
                new Object[]{activity, null});
    }

    /**
     * Activity 生命周期函数
     *
     * @param activity
     */
    public static void onResume(final Activity activity) {
        showTips(activity, "onResume");
        findPlugin("onResume", new Class<?>[]{Activity.class},
                new Object[]{activity});
    }

    /**
     * Activity 生命周期函数
     *
     * @param activity
     */
    public static void onNewIntent(Activity activity, Intent newIntent) {
        showTips(activity, "onNewIntent");
        findPlugin("onNewIntent", new Class<?>[]{Intent.class},
                new Object[]{newIntent});
    }

    /**
     * Activity 生命周期函数
     *
     * @param activity
     */
    public static void onSaveInstanceState(Activity activity, Bundle outState) {
        showTips(activity, "onSaveInstanceState");
        findPlugin("onSaveInstanceState", new Class<?>[]{Activity.class, Bundle.class},
                new Object[]{activity, outState});
    }

    /**
     * Activity 生命周期函数
     *
     * @param activity
     */
    public static void onStop(Activity activity) {
        showTips(activity, "onStop");
        findPlugin("onStop", new Class<?>[]{Activity.class},
                new Object[]{activity});
    }

    /**
     * Activity 生命周期函数
     *
     * @param activity
     */
    public static void onDestroy(Activity activity) {
        showTips(activity, "onDestroy");
        findPlugin("onDestroy", new Class<?>[]{Activity.class},
                new Object[]{activity});
    }

    /**
     * Activity 生命周期函数
     *
     * @param activity
     */
    public static void onRestart(Activity activity) {
        showTips(activity, "onRestart");
        findPlugin("onRestart", new Class<?>[]{Activity.class},
                new Object[]{activity});
    }

    /**
     * Application onCreate 生命周期回调
     *
     * @param application
     */
    public static void onAppAttachBaseContext(Application application) {
        if (isEnable()) {
            MultiDex.install(application);
        }
    }

    public static boolean isEnable() {
        boolean isEnable = false;
        try {
            Class.forName("android.support.multidex.MultiDex",
                    false, MainSDK.class.getClassLoader());
            isEnable = true;
        } catch (ClassNotFoundException e) {
        }
        return isEnable;
    }

    /**
     * Application onCreate 生命周期回调
     *
     * @param application
     */
    public static void onAppCreate(Application application) {
        if (JoUtils.isAppMainProcess(application)) {
            PluginManager pm = PluginManager.getDefault(application);
            ChargeFrameInterface i = (ChargeFrameInterface) pm
                    .findPluginWithoutInit("pay");
            i.onApplicationCreate(application);
        }
        PluginManager pm = PluginManager.getDefault(application);
        ChannelApi channelApi = (ChannelApi) pm.findPluginWithoutInit("jo");
        channelApi.onApplicationCreate(application);
    }

    private static LoginListener mLoginListener = new LoginListener() {

        @Override
        public void onFailed(int code, String arg1) {
            if (notifer != null) {
                notifer.onLoginFailed(code, arg1);
            }
        }

        @Override
        public void onCancel() {
            if (notifer != null) {
                notifer.onCancel();
            }
        }

        @Override
        public void onSuccess(
                com.stone.single.pack.entity.UserInfo innerUserInfo) {
            if (notifer != null) {
                UserInfo user = new UserInfo();
                user.username = innerUserInfo.userName;
                user.nickname = innerUserInfo.nickname;
                user.sessionId = innerUserInfo.sessionId;
                notifer.onLoginSuccess(user);
            }
        }

        @Override
        public void logoutOnSuccess() {
            LogUtils.d(TAG, "logout success " + (notifer == null ? "null" : "not null"));
            if (notifer != null) {
                notifer.onLogOut();
            }
        }
    };

    /**
     * 渠道登录接口
     *
     * @param activity
     */
    public static void login(Activity activity, HashMap<String, Object> extral, MainSDKLoginListener listener) {
        notifer = listener;
        showTips(activity, "login");
        ChannelApi joApi = (ChannelApi) PluginManager.getDefault(null)
                .findPlugin("jo");
        joApi.login(activity, extral, mLoginListener);
    }

    /**
     * 显示退出界面
     *
     */
    public static void showExit(final Activity activity, final ExitCallBack cb) {
        LogUtils.i(TAG, "showExit");
        if (!isInitDone()) {
            if (null != cb) {
                LogUtils.i(TAG, "SDK not init ,callback onExitConfirmed");
                cb.onExitCanceled();
            }
            return;
        }
        ChannelApi joApi = (ChannelApi) PluginManager.getDefault(null)
                .findPlugin("jo");

        boolean isShowExitDialog = joApi.isShowExitDialog();
        LogUtils.i(TAG, "isShowExitDialog:" + isShowExitDialog);
        joApi.exit(activity, new ExitListener() {

            @Override
            public void onConfirmed() {
                onEixt(activity);
                cb.onExitConfirmed();
            }

            @Override
            public void onCanceled() {
                LogUtils.i(TAG, "onCanceled");
                if (null != cb) {
                    cb.onExitCanceled();
                }
            }
        });

    }

    public static void onEixt(final Activity activity) {
        showTips(activity, "onEixt");
        if (!isInitDone()) {
            return;
        }
        invokePluginMethod("dlog", "onExit", null, null);
    }

    private static boolean isInitDone() {
        if (sHasInitializingDone) {
            return true;
        }
        return false;
    }

    public static void showTip(final Activity context, final String msg) {
        LogUtils.d(TAG, msg + "--接口接入正常");
        if (isDebug) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, msg + "--接口接入成功", Toast.LENGTH_SHORT)
                            .show();
                }
            });
            return;
        }
    }

    public static void showTips(final Activity context, final String msg) {
        LogUtils.d(TAG, msg + "--接口接入正常");
        if (isDebug) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, msg + "--接口接入成功", Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }
    }

    private static Handler mainHandler = new Handler(Looper.getMainLooper());

    public static void showDialog(final Activity act, final String msg,
                                  final String identifier, final PayResultListener listener) {
        final String methodName = new Throwable().getStackTrace()[1]
                .getMethodName();
        LogUtils.d(TAG, "methodName = " + methodName);
        mainHandler.post(new Runnable() {

            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(act);
                builder.setTitle(msg);
                builder.setCancelable(false);
                builder.setMessage(identifier);
                builder.setPositiveButton("OK", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (listener != null) {
                            showTips(act, "test OK");
                            listener.onPayNotify(new PayResult(BaseConfig.SUCCESS,
                                    "test OK", identifier,
                                    58,
                                    BaseConfig.PAY_METHOD_TYPE_THIRD_PARTY));
                            return;
                        }
                    }
                });

                builder.setNegativeButton("CANCEL", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (listener != null) {
                            showTips(act, "test CANCEL");
                            listener.onPayNotify(new PayResult(BaseConfig.CANCEL,
                                    "test CANCEL", identifier,
                                    58,
                                    BaseConfig.PAY_METHOD_TYPE_THIRD_PARTY));
                            return;
                        }
                    }
                });
                builder.setNeutralButton("ERROR", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (listener != null) {
                            showTips(act, "test ERROR");
                            listener.onPayNotify(new PayResult(BaseConfig.FAILED,
                                    "test ERROR", identifier,
                                    58,
                                    BaseConfig.PAY_METHOD_TYPE_THIRD_PARTY));
                            return;
                        }
                    }
                });
                builder.create().show();
            }
        });

    }

    public static void showDialog(final Activity act, final String msg,
                                  final MainSDKCallBack cb) {
        final String methodName = new Throwable().getStackTrace()[1]
                .getMethodName();
        LogUtils.d(TAG, "methodName = " + methodName);
        mainHandler.post(new Runnable() {

            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(act);
                builder.setMessage(msg);
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (cb != null) {
                            showTips(act, msg + "--test OK");
                            if (msg.equals("getPredictPayment")) {
                                cb.onSucceeded("{\"method_id\":\"26\",\"name\":\"移动基地支付插件\"}");
                                return;
                            } else if (msg.equals("isSupportSms")) {
                                cb.onSucceeded("{\"isSupport\":true}");
                                return;
                            } else if (msg.equals("isAvailablePayment")) {
                                cb.onSucceeded("{\"method_id\":\"31\",\"is_enable\":\"true\"}");
                                return;
                            } else if (msg.equals("getWXUserInfo")) {
                                cb.onSucceeded("{ \"openid\":\"OPENID\",\"nickname\":\"NICKNAME\",\"sex\":1,\"province\":\"PROVINCE\",	\"city\":\"CITY\",\"country\":\"COUNTRY\",	\"headimgurl\": \"http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0\",\"privilege\":[\"PRIVILEGE1\", \"PRIVILEGE2\"],\"unionid\": \" o6_bmasdasdsad6_2sgVt7hMZOPfL\" }");
                                return;
                            } else if (msg.equals("searchNearby")) {
                                cb.onSucceeded("{\"data\":{\"contents\":[//附近的人{\"distance\":0,//距离，单位千米\"playerId\":1043890783,\"position\":{\"building\":\"\",\"city\":\"深圳市\",\"country\":\"南山区\",\"nation\":\"中国\",\"province\":\"广东省\",\"road\":\"高新中一道\",\"tag\":\"\"},\"time\":1444444719},{\"distance\":0,\"playerId\":1050200005,\"position\":{\"building\":\"\",\"city\":\"深圳市\",\"country\":\"南山区\",\"nation\":\"中国\",\"province\":\"广东省\",\"road\":\"高新中一道\",\"tag\":\"\"},\"time\":1444465868}],\"gameId\":10389,\"playerId\":1050200005,\"size\":20,\"total\":20,//总数\"udid\":\"74p0nq8_26306P1855555N4q6n06rpp1\"},\"error_code\":0,\"msg\":\"ok\",\"ret\":0}");
                                return;
                            }
                            cb.onSucceeded("test OK");
                            return;
                        }
                    }
                });
                builder.setNegativeButton("CANCEL", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int code) {
                        if (cb != null) {
                            showTips(act, "test CANCEL");
                            cb.onFailed(code, "test CANCEL");
                            return;
                        }
                    }
                });
                builder.setNeutralButton("ERROR", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int code) {
                        if (cb != null) {
                            showTips(act, "test ERROR");
                            cb.onFailed(code, "test ERROR");
                            return;
                        }
                    }
                });
                builder.create().show();
            }
        });
    }

}
