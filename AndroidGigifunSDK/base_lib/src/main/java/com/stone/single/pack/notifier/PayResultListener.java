package com.stone.single.pack.notifier;

import com.stone.single.pack.entity.PayResult;

public interface PayResultListener {
	
	/**
	 * on pay callback
	 * 
	 * @param result
	 */
	public void onPayNotify(PayResult result);
}
