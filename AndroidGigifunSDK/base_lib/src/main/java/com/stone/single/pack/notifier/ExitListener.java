package com.stone.single.pack.notifier;

public interface ExitListener {
	
	/**
	 * Confirmed exit
	 * 
	 */
	public void onConfirmed();

	/**
	 * 取消退出
	 * 
	 */
	public void onCanceled(); 
}
