package com.stone.lib.plugin;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.stone.lib.plugin.interfaces.PluginInterface;
import com.stone.lib.utils.LogUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class Plugin implements PluginInterface {

	protected static final Handler sMainHandler = new Handler(
			Looper.getMainLooper());

	private boolean mHasInited;

	Context mAppContext;

	boolean mShowToast;

	PluginEntry entry;

	@Override
	public String getVersion() {
		return entry.version;
	}

	@Override
	public String getDescription() {
		return entry.description;
	}

	synchronized void init(Context appContext) {
		if (mHasInited) {
			return;
		}
		LogUtils.i(getClass().getSimpleName(), "onInitialize()");
		mAppContext = appContext;
		mShowToast = true;
		onInitialize(appContext);
		mHasInited = true;
	}

	/**
	 * 为这个插件实例提供 Application Context
	 */
	protected abstract void onInitialize(Context appContext);

	@Override
	public Object invoke(String methodName, Class<?>[] argTypes, Object[] args) {
		try {
			Method method = getClass().getMethod(methodName, argTypes);
			return method.invoke(this, args);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				Method method = getClass().getDeclaredMethod(methodName, argTypes);
				return method.invoke(this, args);
			} catch (Exception e1) {
				e1.printStackTrace(); 
				return null;
			}
		}
	}

	@Override
	public Context getApplicationContext() {
		return mAppContext;
	}

	@Override
	public void makeToast(final CharSequence msg) {
		sMainHandler.post(new Runnable() {

			@Override
			public void run() {
				if (mShowToast) {
					Toast.makeText(mAppContext, msg, Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	public void post(Runnable postRunnable) {
		sMainHandler.post(postRunnable);
	}

	private List<Dialog> loadDialogs = new ArrayList<Dialog>();

	public void showLoadingBar(final Activity activity) {
		if (activity == null)
			return;
		post(new Runnable() {
			@Override
			public void run() {

				for (Dialog d : loadDialogs) {
					if (d != null && d.isShowing()) {
						return;
					}
				}

				ProgressBar pb = new ProgressBar(activity);
				final Dialog pdDialog = new Dialog(activity,
						android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
				RelativeLayout layout = new RelativeLayout(activity);
				layout.setBackgroundColor(0x99000000);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
				layout.setGravity(Gravity.CENTER);
				layout.addView(pb, params);
				pdDialog.setContentView(layout);
				try {
					pdDialog.show();
				} catch (WindowManager.BadTokenException e) {
				}
				loadDialogs.add(pdDialog);
			}
		});

	}

	public void closeLoadingBar() {
		post(new Runnable() {
			@Override
			public void run() {
				for (Dialog d : loadDialogs) {
					d.dismiss();
				}
				loadDialogs.clear();
			}
		});

	}
	
	public String getString(String key) {
		return null;
	}

	public Drawable getDrawable(String key) {
		return null;
	}

	private HandlerThread mThread;
	Handler mHandler;

	public Handler getPluginHandler() {
		if (mThread == null) {
			mThread = new HandlerThread("PluginHandleThread:"
					+ getClass().getName(), Process.THREAD_PRIORITY_BACKGROUND);
			mThread.start();
			mHandler = new Handler(mThread.getLooper()){
				@Override
				public void handleMessage(Message msg) {
					handlePluginMessage(msg);
				}
			};
		}
		return mHandler;
	}

	public void handlePluginMessage(Message msg){
		
	}
}
