package com.stone.single.pack.notifier;

public interface Notifier {
	
	/**
	 * on Error
	 * 
	 * @param code
	 * @param msg
	 */
	public void onError(int code, String msg);
	
	/** 
	 * On Notify Result
	 * 
	 * @param code
	 * @param msg
	 * @param result
	 */
	public void onNotify(String message);
}
