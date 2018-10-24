/**   
 * 文件名：JSONParseUtil.java</br>
 * 描述： </br>
 * 开发人员：杜逸平 </br>
 * 创建时间： 2014-4-17
 */ 

package com.gigifun.gp.utils;

import java.util.HashMap;

import org.json.JSONObject;

/** 
 * 类名: JSONParseUtil</br> 
 * 包名：com.ln.lngamesdkjar.utils </br> 
 * 描述: </br>
 * 发布版本号：</br>
 * 开发人员： 杜逸平</br>
 * 创建时间： 2014-4-17 
 */

public class JSONParseUtil {

	  public static HashMap<String,String> requestParse(JSONObject jo){
		  HashMap<String,String> map=new HashMap<String,String>();
		  map.put("Status", jo.optString("Status"));
		  map.put("Code", jo.optString("Code"));
		  map.put("Lnorderid", jo.optString("Lnorderid"));
		  return map;
	  }
}
