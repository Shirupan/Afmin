package com.stone.lib.plugin.interfaces;

import android.app.Activity;
import android.content.Context;

public interface PluginInterface {

	public String getVersion();

	public String getDescription();

	public Object invoke(String methodName, Class<?>[] argTypes, Object[] args);

	public Context getApplicationContext();

	public void makeToast(final CharSequence msg);	
	
	public void showLoadingBar(final Activity activity);
	
	public void closeLoadingBar();
}
