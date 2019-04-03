package com.stone.channel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.stone.single.pack.entity.LoginInfo;
import com.stone.single.pack.notifier.ChannelLoginListener;
import com.stone.single.pack.notifier.ExitListener;
import com.stone.single.pack.notifier.InitListener;
import com.stone.single.pack.notifier.LoginListener;
import com.stone.single.pack.notifier.LogoutListener;
import com.stone.single.pack.notifier.Notifier;
import com.stone.single.pack.notifier.PayResultListener;
import com.stone.single.pack.notifier.ReportListener;
import com.stone.jo.api.ChannelApi;
import com.stone.jo.config.JoConfig;
import com.stone.jo.utils.JoUtils;
import com.stone.lib.plugin.Plugin;
import com.stone.lib.plugin.leisure.interfaces.OnLifeCycleListener;
import com.stone.lib.plugin.leisure.interfaces.OnLoginListener;
import com.stone.lib.utils.LogUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class JointOperate extends Plugin implements ChannelApi, OnLifeCycleListener, OnLoginListener {

    private static final String TAG = "JointOperate";
    private static Class<?> classObj;

    @Override
    protected void onInitialize(Context appContext) {
    }

    @Override
    public void init(Activity activity, InitListener listener) {
        if (isChannelEnable(activity)) {
            invokeJoMethod("init", new Class<?>[]{Activity.class, InitListener.class},
                    new Object[]{activity, listener});
        } else {
            if (null != listener) {
                listener.onSuccess();
            }
        }
    }

    @Override
    public void login(Activity activity, HashMap<String, Object> extral, LoginListener listener) {
        invokeJoMethod("login", new Class<?>[]{Activity.class, HashMap.class, LoginListener.class},
                new Object[]{activity, extral, listener});
    }

    @Override
    public void onApplicationCreate(Context context) {
        if (JoUtils.isAppMainProcess(context)) {
            String className = (String) JoConfig.get("class_name");
            if (TextUtils.isEmpty(className)) {
                JoConfig.loadConfig(context);
            }
        }
        invokeJoMethod("onApplicationCreate", new Class<?>[]{Context.class},
                new Object[]{context});
    }

    @Override
    public void attachBaseContext(Context context) {

    }

    @Override
    public void logout(Activity activity, LogoutListener listener) {
        invokeJoMethod("logout", new Class<?>[]{Activity.class, LogoutListener.class},
                new Object[]{activity, listener});
    }

    @Override
    public boolean isShowExitDialog() {
        Boolean isShowExitDialog = (Boolean) invokeJoMethod("isShowExitDialog",
                new Class<?>[]{}, new Object[]{});
        if (null == isShowExitDialog) {
            return false;
        } else {
            return isShowExitDialog;
        }
    }

    @Override
    public void exit(Activity activity, ExitListener listener) {
        invokeJoMethod("exit", new Class<?>[]{Activity.class, ExitListener.class},
                new Object[]{activity, listener});
    }

    @Override
    public void onCreate(Activity activity,
                         Bundle savedInstanceState) {
        invokeJoMethod("onCreate", new Class<?>[]{Activity.class, Bundle.class},
                new Object[]{activity, savedInstanceState});
    }

    @Override
    public void onResume(Activity activity) {
        invokeJoMethod("onResume", new Class<?>[]{Activity.class},
                new Object[]{activity});
    }

    @Override
    public void onPause(Activity activity) {
        invokeJoMethod("onPause", new Class<?>[]{Activity.class},
                new Object[]{activity});
    }

    @Override
    public void onStop(Activity activity) {
        invokeJoMethod("onStop", new Class<?>[]{Activity.class},
                new Object[]{activity});
    }

    @Override
    public boolean isFunctionSupported(int functionType) {
        Boolean isFunctionSupported = (Boolean) invokeJoMethod("isFunctionSupported",
                new Class<?>[]{int.class}, new Object[]{functionType});
        if (null == isFunctionSupported) {
            return false;
        } else {
            return isFunctionSupported;
        }
    }

    @Override
    public void callFunction(Context context, int functionType, Map<?, ?> map,
                             Notifier callback) {
        invokeJoMethod("callFunction", new Class<?>[]{Context.class,
                        int.class, Map.class, Notifier.class},
                new Object[]{context, functionType, map, callback});
    }

    @Override
    public void onNewIntent(Intent intent) {
        invokeJoMethod("onNewIntent", new Class<?>[]{Intent.class},
                new Object[]{intent});
    }

    @Override
    public void onDestroy(Activity activity) {
        LogUtils.d(TAG, "joint onDestroy");
        invokeJoMethod("onDestroy", new Class<?>[]{Activity.class},
                new Object[]{activity});
    }

    @Override
    public void onStart(Activity activity) {
        LogUtils.d(TAG, "joint onStart");
        invokeJoMethod("onStart", new Class<?>[]{Activity.class},
                new Object[]{activity});
    }

    @Override
    public void onRestart(Activity activity) {
        LogUtils.d(TAG, "joint onReStart");
        invokeJoMethod("onRestart", new Class<?>[]{Activity.class},
                new Object[]{activity});
    }

    @Override
    public void onSaveInstanceState(Activity activity,
                                    Bundle outState) {
        LogUtils.d(TAG, "joint onSaveInstanceState");
        invokeJoMethod("onSaveInstanceState", new Class<?>[]{Activity.class, Bundle.class},
                new Object[]{activity, outState});
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {
        invokeJoMethod("onActivityResult", new Class<?>[]{int.class,
                        int.class, Intent.class},
                new Object[]{requestCode, resultCode, data});
    }

    public boolean isChannelEnable(Activity activity) {
        JoConfig.loadConfig(activity);
        boolean isChannelEnable = false;
        String className = (String) JoConfig.get("class_name");
        if (TextUtils.isEmpty(className)) {
            LogUtils.e(TAG, "No assets/channel/channel_name.txt or class_name don't config in channel_name.txt");
            isChannelEnable = false;
        } else {
            try {
                classObj = Class.forName(className, false,
                        JointOperate.class.getClassLoader());
                isChannelEnable = true;
            } catch (ClassNotFoundException e) {
                isChannelEnable = false;
            }
        }
        return isChannelEnable;
    }

    private Object invokeJoMethod(String method, Class<?>[] cls, Object[] obj) {
        String className = (String) JoConfig.get("class_name");
        if (TextUtils.isEmpty(className)) {
            LogUtils.e(TAG, "No assets/channel/channel_name.txt or class_name don't config in channel_name.txt");
            return null;
        }
        Object objInstance = null;
        Object resultObj = null;
        try {
            if (null == classObj) {
                classObj = Class.forName(className, false,
                        JointOperate.class.getClassLoader());
            }
            Method apiMethod = classObj.getMethod(method, cls);
            int modifiers = apiMethod.getModifiers();
            boolean isStatic = Modifier.isStatic(modifiers);
            if (!isStatic) {
                objInstance = classObj.newInstance();
            }
            resultObj = apiMethod.invoke(objInstance, obj);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return resultObj;
    }

    @Override
    public void pay(Activity activity, HashMap<String, Object> extral, PayResultListener listener) {
        invokeJoMethod("pay", new Class<?>[]{Activity.class, HashMap.class, PayResultListener.class},
                new Object[]{activity, extral, listener});
    }

    @Override
    public void onUserLoggedIn(Activity ui) {

    }
}
