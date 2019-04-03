package com.stone.single.pack.notifier;

public interface LogoutListener { 
	
	/**
	 * logout Succeed
	 * 
	 */
    public void onSuccess(); 
    
    /**
     * logout failed
     * 
     * @param code error code
     * @param msg error message
     */
    public void onFailed(int code, String msg);
}
