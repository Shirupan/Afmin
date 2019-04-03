package com.stone.single.pack.notifier;

import com.stone.single.pack.entity.UserInfo;

public interface LoginListener { 
	
	/**
	 * 登录成功，获取到用户信息userInfo
	 * 通过userInfo中的UID、token做服务器登录认证
	 * 
	 * @param userInfo
	 */
    public void onSuccess(UserInfo userInfo);
    
    /**
     * 取消登录
     * 
     */
    public void onCancel();
    
    /**
     * 登录失败
     * 
     * @param code 返回码
     * @param msg 错误信息
     */
    public void onFailed(int code, String msg);
    
	/**
	 * logout Succeed
	 * 
	 */
    public void logoutOnSuccess(); 
}
