package com.stone.single.pack.notifier;

/**
 * reportUserInfo listener 
 * 
 */
public interface ReportListener {
	
	/**
	 * on pay callback
	 * 
	 * @param message
	 */
	public void onSuccess(String message);
	
	/**
	 * on Error
	 * 
	 * @param code
	 * @param msg
	 */
	public void onError(int code, String msg);
	
}
