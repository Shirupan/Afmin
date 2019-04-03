package com.stone.single.pack.notifier;

/**
 * initialize listener 
 * 
 */
public interface InitListener {
	/**
	 * initialize succeed
	 * 
	 */
	public void onSuccess();
	
	/**
	 * initialize failed
	 * 
	 * @param code return code
	 * @param msg
	 */
	public void onFailed(int code, String msg);
}
