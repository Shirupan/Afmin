package com.stone.lib.internal;

import android.os.Environment;

import com.stone.lib.utils.LogUtils;
import com.stone.lib.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class RequestExecutor {
	private static final String TAG="RequestExecutor";

	public static void init() {
//		if(!isEnable()){
//			LogUtils.d(TAG, "Okhttp init failed");
//			return;
//		}
//		LogUtils.d(TAG, "Okhttp init success");
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        if (URLConfig.DEBUG_VERSION) {
//        	builder.addInterceptor(new LoggerInterceptor(null, true));
//		}
//        OkHttpClient client = builder.build();
//        OkHttpClient okHttpClient = HttpManager.init(client);
    }
	
	public static boolean isEnable(){   
		try {
			Class.forName("com.okhttp.okhttp3.OkHttpClient",
					false, RequestExecutor.class.getClassLoader()); 
		} catch (Throwable e) {
			LogUtils.e(TAG, "OkHttp jar is no found!");
			return false;    
		}   
		return true;
	}

	
	public static void makeBlockRequest(String method, String path,
                                        HashMap<String, ?> params, int flags, final Class<?> parserType, long timeout, final RequestCallback cb) {
//		if(!isEnable()){
//			return;
//		}
//		HttpManager.loadHttpRequest(makeHttpRequest(method, path, params, flags,(int)timeout), new CasualGsonCallBack<JSONObject>() {
//
//			@Override
//			public void onError(BasicException baseResponse, int i) {
//				Object makeBlockObj=asObject(baseResponse.getCode(),baseResponse.getDesc(),parserType);
//				cb.onFail((ServerError)makeBlockObj);
//			}
//			@Override
//			public void onResponse(JSONObject arg0, int arg1) {
//				Object makeBlockObj=asObject(200,arg0.toString(),parserType);
//				cb.onSuccess(makeBlockObj);
//			}
//
//		});
	}


	//2017.9.6 add by stone.shi start
	public static final ServerError PARSE_JSON_OBJECT_ERROR = new ServerError();
	static{
		PARSE_JSON_OBJECT_ERROR.err_code = -75124;
		PARSE_JSON_OBJECT_ERROR.err_detail = "Parse JSON object error";
	}
	
	public static Object asObject(int code, String strings, Class<?> glass) {
		ServerError se = new ServerError();
		se.err_detail = strings;
		se.err_code = code;
		return se;
	}

	public static Object asObject(int code, String strings, Type type) {
		if (code == 200) {
			return null;
		} else if (code >= 400 && code <= 499) {
			return null;
		} else {
			ServerError se = new ServerError();
			se.err_detail = strings;
			se.err_code = code;
			return se;
		}
	}
	
	private static HashMap<String, String> getV2SignParams(int flag, HashMap<String, String> pra, String nonce) {
		return null;
	}
	
	private static void appendGetParams(StringBuilder urlBuilder,
			HashMap<String, ?> params) {
		if (params == null || params.size() == 0) {
			return;
		}
		if(urlBuilder.indexOf("?") == -1){
			urlBuilder.append("?");
		}else{
			urlBuilder.append("&");
		}
		Set<String> keys = params.keySet();
		String encoder = "UTF-8";
		try {
			boolean flag = true;
			for (String key : keys) {
				if (!flag) {
					urlBuilder.append("&");
				}
				flag = false;
				Object value = params.get(key);
				urlBuilder.append(URLEncoder.encode(key, encoder));
				urlBuilder.append("=");
				urlBuilder.append(value != null ? URLEncoder.encode(
						value.toString(), encoder) : "");
			}
		} catch (UnsupportedEncodingException e) {
		}
	}
	
	public static String addHeader(String url, int flag, String method) {
		String header = null;
//		String consumerKey = ChannelCache.get().getConsumerKey();
//		String consumerSecret = ChannelCache.get().getConsumerSecret();
//		String accessToken = ChannelCache.get().getAccessToken();
//		String tokenSecret = ChannelCache.get().getTokenSecret();
//
//		String signatureMethod = OAuthUtils.SIGNATURE_METHOD;
//
//		String timeStamp = OAuthUtils.generateTimestamp();
//		String nonce = OAuthUtils.generateNonce();
//		String version = OAuthUtils.VERSION_1_0;
//
//		HashMap<String, String> params = new HashMap<String, String>();
//		params.put(OAuthUtils.OAUTH_CONSUMER_KEY, consumerKey);
//		params.put(OAuthUtils.OAUTH_TOKEN, accessToken);
//		params.put(OAuthUtils.OAUTH_SIGNATURE_METHOD, signatureMethod);
//		params.put(OAuthUtils.OAUTH_TIMESTAMP, timeStamp);
//		params.put(OAuthUtils.OAUTH_NONCE, nonce);
//		params.put(OAuthUtils.OAUTH_VERSION, version);
//
//		HashMap<String, String> signV2Params = getV2SignParams(flag,(HashMap<String, String>)params,nonce);
//
//		try {
//			String signature = OAuthUtils.sign(method, url,
//					params, consumerSecret, tokenSecret);
//			String signature_v2 = OAuthUtils.sign(method, url,
//					signV2Params, consumerSecret, tokenSecret);
//			String[] kvs = new String[] { OAuthUtils.OAUTH_CONSUMER_KEY,
//					consumerKey, OAuthUtils.OAUTH_TOKEN, accessToken,
//					OAuthUtils.OAUTH_SIGNATURE_METHOD, signatureMethod,
//					OAuthUtils.OAUTH_SIGNATURE, signature,
//					OAuthUtils.OAUTH_TIMESTAMP, timeStamp,
//					OAuthUtils.OAUTH_NONCE, nonce,
//					OAuthUtils.OAUTH_VERSION, version,
//					OAuthUtils.OAUTH_SIGNATURE_V2, signature_v2 };
//			header = OAuthUtils.prepareOAuthHeader(kvs);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return header;
	}
	
//	public static HttpRequest makeHttpRequest(String method, String path, HashMap<String, ?> params, int requestFlags, int timeout){
//		String reqMethod=HttpGet.METHOD_NAME;
//		String finalUrl= RequestBuilder.wrapUrl(path, requestFlags);
//		if (timeout==0) {
//			timeout=15000;
//		}
//		//okhttp3
//		HttpRequest httpRequest = HttpRequest.convert(finalUrl, params)
//				.readTimeOut(timeout)
//				.connTimeOut(timeout);
//		//设置请求方式
//		if("POST".equals(method)){
//			httpRequest.method(Method.POST);
//
//			reqMethod=HttpPost.METHOD_NAME;
//		}else if ("GET".equals(method)){
//			httpRequest.method(Method.GET);
//
//			reqMethod=HttpGet.METHOD_NAME;
//			HashMap<String, Object> ps = (HashMap<String, Object>)params;
//			if ((requestFlags & FLAG_NO_UDID) == 0 && ChannelCache.get() != null && ps  != null) {
//				ps.put("nudid", ChannelCache.get().getNewUDID());
//				ps.put("udid", ChannelCache.get().getOldUDID());
//			}
//			StringBuilder urlBuilder = new StringBuilder(finalUrl);
//			appendGetParams(urlBuilder, ps);
//			finalUrl = urlBuilder.toString();
//		}
//		LogUtils.d(TAG,"finalUrl:"+finalUrl);
//		//设置请求头
//		httpRequest.addHeader(OAuthUtils.HTTP_AUTHORIZATION_HEADER, addHeader(finalUrl, requestFlags, reqMethod))
//        .addHeader("User-Agent", ChannelCache.get().getUserAgent()!= null ? ChannelCache.get().getUserAgent():Version.userAgent()+"");
////		httpRequest.addHeader(OAuthUtils.HTTP_AUTHORIZATION_HEADER, addHeader(finalUrl, requestFlags, reqMethod))
////        .addHeader("User-Agent", "");
//
//		return httpRequest;
//	}
	//2017.9.6 add by stone.shi end

//	public static void makeRequestInBackground(final String method,
//                                               final String path, final HashMap<String, ?> params,
//                                               final int requestFlags, final Class<?> parserType,
//                                               final int timeout, final RequestCallback cb) {
//		sThreadPoolExecutor.execute(new Runnable() {
//
//			@Override
//			public void run() {
//				if(!isEnable()){
//					return;
//				}
//				HttpManager.loadHttpRequest(makeHttpRequest(method, path, params, requestFlags,timeout), new CasualStringCallBack() {
//
//					@Override
//					public void onError(BasicException baseResponse, int i) {
//						final Object obj=asObject(baseResponse.getCode(),baseResponse.getDesc(),parserType);
//						final boolean isRefreshToken = baseResponse.getCode() == 401
//								&& (requestFlags & FLAG_NEED_OAUTH) != 0;
//
//						mainHandler.post(new Runnable() {
//
//							@Override
//							public void run() {
//									if (isRefreshToken) {
//										if (cb != null) {
//											cb.onFail(ResponseWrapper.DATA_ERROR);
//										}
//									} else {
//										if (cb != null) {
//											cb.onFail((ServerError) obj);
//										}
//									}
//							}
//						});
//					}
//					@Override
//					public void onResponse(String arg0, int arg1) {
//						final Object obj=asObject(200,arg0,parserType);
//						mainHandler.post(new Runnable() {
//
//							@Override
//							public void run() {
//									if (cb != null) {
//										cb.onSuccess(obj);
//									}
//							}
//						});
//					}
//				});
//			}
//		});
//	}

	/** 
     * 根据byte数组，生成文件 
     */  
    public static void getFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {  
//            File dir = new File(Environment.getExternalStorageDirectory());
//            if(!dir.exists()){//判断文件目录是否存在  
//                dir.mkdirs();  
//            }  
            file = new File(Environment.getExternalStorageDirectory()+"//"+fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);  
        } catch (Exception e) {
            e.printStackTrace();  
        } finally {  
            if (bos != null) {  
                try {  
                    bos.close();  
                } catch (IOException e1) {
                    e1.printStackTrace();  
                }  
            }  
            if (fos != null) {  
                try {  
                    fos.close();  
                } catch (IOException e1) {
                    e1.printStackTrace();  
                }  
            }  
        }  
    }

	/**
	 * Flag which identifies that, the request need OAuth verifying.
	 */
	public static final int FLAG_NEED_OAUTH = 0x00000001;

	/**
	 * Flag which identifies the request is a https request, so at the runtime,
	 * we automatically add the prefix 'https://' server base url if necessary.
	 */
	public static final int FLAG_SECURE = 0x00000010;

	/**
	 * Flag which identifies that, the request needs User-Agent header.
	 */
	public static final int FLAG_NEED_USER_AGENT = 0x00000100;

	/**
	 * Flag which identifies that, parameters need parsing to JSON pattern
	 * before transmitting.
	 * 
	 * @note For <b>POST</b> only
	 */
	public static final int FLAG_CONTENT_TYPE_JSON = 0x00001000;

	/**
	 * Flag which identifies that, content with type 'application/json' need not
	 * encoding.
	 * 
	 * @note For <b>POST</b> only
	 */
	public static final int FLAG_DO_NOT_ENCODE_JSON_CONTENT = 0x00010000;

	/**
	 * 支付API专用flag
	 */
	public static final int FLAG_PAYMENT = 0x00100000;

	/**
	 * 社交API专用flag
	 */
	public static final int FLAG_SNS = 0x10000000;

	/**
	 * 不附加UDID 参数
	 */
	public static final int FLAG_NO_UDID = 0x01000000;

	/**
	 * The default flag for 'GET' request.
	 */
	public static final int DEFAULT_GET_FLAG = FLAG_NEED_OAUTH
			| FLAG_NEED_USER_AGENT;

	/**
	 * The default flag for 'POST' request.
	 */
	public static final int DEFAULT_POST_FLAG = FLAG_NEED_OAUTH
			| FLAG_CONTENT_TYPE_JSON | FLAG_NEED_USER_AGENT;

	static String HMAC_SHA1 = "HmacSHA1";

	private static String generateServiceSignature(String data, String key) {
		byte[] byteHMAC = null;
		StringBuffer sb = new StringBuffer();
		try {
			Mac mac = Mac.getInstance(HMAC_SHA1);
			SecretKeySpec spec = new SecretKeySpec(key.getBytes(), HMAC_SHA1);
			mac.init(spec);
			byteHMAC = mac.doFinal(data.getBytes());
			String byteString;
			for (byte b : byteHMAC) {
				byteString = Integer.toHexString(b >= 0 ? b : 256 + b);
				if (byteString.length() < 2)
					sb.append("0" + byteString);
				else
					sb.append(byteString);
			}
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException ignore) {
			// should never happen
		}

		return sb.toString();
	}

	private static String generateServiceAuthorizationHeader(int nonce,
                                                             String sign) {
		StringBuffer sb = new StringBuffer();
		sb.append("stone_signature_method=HMAC-SHA1");
		sb.append(",");
		sb.append("stone_nonce=" + nonce);
		sb.append(",");
		sb.append("stone_signature=" + sign);
		return sb.toString();
	}

	// just for the one level map
	private static HashMap<String, Object> json2Map(JSONObject object) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (object == null) {
			return new HashMap<String, Object>();
		}
		Iterator<String> iterator = object.keys();
		while (iterator.hasNext()) {
			String key = iterator.next();
			try {
				map.put(key, object.get(key));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return map;

	}

	public static HashMap<String, Object> sortHashMap(
			HashMap<String, Object> hashMap) {
		if (hashMap == null || hashMap.size() < 1) {
			return new HashMap<String, Object>();
		}
		if (hashMap.size() == 1) {
			return hashMap;
		}
		HashMap<String, Object> target = new LinkedHashMap<String, Object>();
		List<String> arrayList = new ArrayList<String>(hashMap.keySet());
		if (arrayList.size() > 1) {
			Collections.sort(arrayList, new Comparator<String>() {
				public int compare(String o1, String o2) {
					return o1.compareTo(o2);
				}
			});
		}

		// 将HASHMAP中的数据排序-------------------------------------------
		for (Iterator<String> iter = arrayList.iterator(); iter.hasNext();) {
			String key = iter.next();
			target.put(key, hashMap.get(key));
		}
		return target;
	}

	private static final String NONE_AUTH_SECRET = "590ebe8ea3617ffb8a44541b13e184f0";

	private static HashMap<String, Object> signedParams(
			HashMap<String, Object> params) {
		Set<String> keys = params.keySet();
		StringBuilder builder = new StringBuilder();
		for (String key : keys) {
			Object o = params.get(key);
			String value = o != null ? o.toString() : null;
			if (value != null && !"null".equals(value)) {
				builder.append(key);
				builder.append(value);
			}
		}
		String baseStr = builder.toString();
		String signbyte = Utils.md5(NONE_AUTH_SECRET + baseStr);
		String lowSign = signbyte.toLowerCase();
		params.put("encrypt_str", lowSign);
		return params;
	}
	
}
