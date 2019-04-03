package com.stone.lib.plugin.interfaces;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.stone.lib.plugin.PluginResultHandler;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 用户模块接口
 * 
 */
public interface UserLoginInterface extends PluginInterface{
	
	/**
	 * 玩家呢称
	 */
	public static final String EXTEND_KEY_NICK_NAME = "NICK_NAME";
	
	/**
	 * 玩家ID
	 */
	public static final String EXTEND_KEY_PLAYER_ID = "PID";
	/**
	 * 游戏ID
	 */
	public static final String EXTEND_KEY_GAME_ID = "GID";
	
	public static final String EXTEND_KEY_PLAYER_PAYMENT_GROUP = "PLAYER_PAYMENT_GROUP";
	
	public static final String EXTEND_KEY_LAST_LOGIN_NICK_NAME = "LAST_LOGIN_NICK_NAME";
	
	public static final String EXTEND_KEY_LAST_LOGIN_USER_ID = "LAST_LOGIN_USER_ID";
	
	public static final String EXTEND_KEY_GAME_UID = "GAME_UID";
	
	public static final String EXTEND_KEY_PLAYER_PHONE = "PLAYER_PHONE_NUMBER";
	
	public static final String EXTEND_KEY_PLAYER_PHONE_ACTIVATE = "PLAYER_PHONE_ACTIVATE";
	public static final String EXTEND_KEY_PLAYER_AVATAR_URL = "PLAYER_AVATAR_URL";
	
	public static final String EXTEND_KEY_PLAYER_UDID = "PLAYER_UDID";
	public static final String EXTEND_KEY_GRADE_ID = "GRADE_ID";
	
	public static final String EXTEND_KEY_OPEN_ID = "OPEN_ID";
	
	/**
	 * 获取本地账号列表:列表仅包含用户名
	 * @return
	 */
	public ArrayList<String> getAccountList();
	
	/**
	 * 统一接口get
	 * @param actionName
	 * @param params
	 * @param handler
	 */
	public void get(String actionName, HashMap<String, Object> params, PluginResultHandler handler);
	
	/**
	 * 统一接口post
	 * @param actionName
	 * @param params
	 * @param handler
	 */
	public void post(String actionName, HashMap<String, Object> params, PluginResultHandler handler);
	
	public void socialLogin(Activity activity, int trye, HashMap<String, Object> params, PluginResultHandler handler);

	public void socialInit(Activity activity, HashMap<String, Object> params);
	public void onCreate(Activity activity);
	public void onActivityResult(int requestCode, int resultCode, Intent data);
	public void onNewIntent(final Intent intent);
	public void onRestart(Context context);
	public void onResume(Activity activity);
	public void onPause(final Activity activity);
	public void onStop(final Activity activity);
	public void onDestroy(final Activity activity);
	public void socialLogout(Context activity, PluginResultHandler ph);
	public void getVerifyCode(PluginResultHandler handler);
	public void getQQWXUserInfo(PluginResultHandler cb);
	public void ledouNoUiAccountLogout(final PluginResultHandler handler);
	public void requestUserInfo(String accessToken, String tokenSecret, PluginResultHandler loginHandler);
	public void guestLogin(PluginResultHandler loginHandler);
	public void channelLogin(HashMap<String, Object> params, PluginResultHandler handler);
	/**
	 * 获取扩展属性值
	 * @param key EXTEND_KEY
	 * @return
	 */
	public Object getExtendValue(String key);

	/**
	 * 游戏uid查询接口
	 * @param userId
	 * @param handler
	 */
	public void checkGameUid(final String userId, final PluginResultHandler handler);
}
