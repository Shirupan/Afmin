package com.stone.single.pack.notifier;

public interface ChannelLoginListener {
	
	/**
	 * 登录成功，获取到渠道登陆返回数据
	 * 
	 * @param obj
	 */
	void onSuccess(Object obj);
	
	/**
	 * 切换账号
	 * 
	 * @param obj
	 */
	void onChange(Object obj);
	
	   /**
     * 登录失败
     * 
     * @param code 返回码
     * @param errorMsg 错误信息
     */
	void onFailed(int code, String errorMsg);

}
