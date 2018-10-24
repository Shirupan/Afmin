package com.gigifun.gp.utils;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 类名: MD5Utils</br>
 * 包名：com.ln.lngamesdkjar.utils </br>
 * 描述: MD5工具类</br>
 * 发布版本号：</br>
 * 开发人员： 杜逸平</br>
 * 创建时间： 2014-4-17
 */

public class MD5Utils {
    public static final String SECRET = "6FQ3F8cNPSjUYZjsChUyUxyFMwxcQKRb";

    //统计KEY
    public static final String PAYSECRET = "gigifungame_1234";
    public static final String FLOATPAYSECRET = "GIGIFUNGAME123456";
    //  微信API密钥，在商户平台设置
    public static final String WECHATSECRET = "gigifungame_1234";

    public static String md5Sign(String param) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(param.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //Log.i(StringUtil.Log,"sign= "+re_md5);
        return re_md5;
    }

    /*
        接收所有的参数排序
     */
    public static String getToString(Map<String,String> params){
        StringBuffer buffer=new StringBuffer();
        List<String> keys=new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        for(int i=0;i<keys.size();i++){
           String key=keys.get(i);
            if(key.equals("Sign")){
                continue;
            }
            String value=(String)params.get(key);

            if(value!=null&&value.length()>0){
                buffer.append(value);
            }
        }
        return buffer.toString();
    }

    /*
     生成签名的算法
     */
    public  static String createSign(String content,String Key){
        String tosign = (content == null ? "" : content) + Key;
        return md5Sign(tosign);
    }





    public static String md5Sign(String orderId, String uid, float amount,
                                 String coOrderId, int success) {
        String param = md5Param(orderId, uid, amount, coOrderId, success);

        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(param.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;

    }

    public static String md5Param(String orderId, String uid, float amount,
                                  String coOrderId, int success) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("orderId").append("=").append(orderId).append("&")
                .append("uid").append("=").append(uid).append("&")
                .append("amount").append("=").append(amount).append("&")
                .append("coOrderId").append("=").append(coOrderId).append("&")
                .append("success").append("=").append(success).append("&")
                .append("secret").append("=").append(SECRET);
        return buffer.toString();
    }

    //genappsign备用
    public static String weChatPaySign(String appId, String partnerId, String prepayId,
                                       String packageValue, String nonceStr, String timeStamp) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("appId").append("=").append(appId).append("&")
                .append("nonceStr").append("=").append(nonceStr).append("&")
                .append("packageValue").append("=").append(packageValue).append("&")
                .append("partnerId").append("=").append(partnerId).append("&")
                .append("prepayId").append("=").append(prepayId).append("&")
                .append("timeStamp").append("=").append(timeStamp).append("&")
                .append("key").append("=").append(WECHATSECRET);
        LogUtil.k("wechat拼接的签名：==" + buffer);
        String sign = MD5Utils.getMessageDigest(buffer.toString().getBytes()).toUpperCase();
        return sign;
//		return buffer.toString();
    }


    /**
     * @param gameid
     * @param userid
     * @return
     */
    public static String md5PaySign(String gameid, String userid) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(gameid).append(userid).append(PAYSECRET);
        String param = buffer.toString();
        ;
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(param.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;

    }

    /**
     * 更多支付使用的签名
     *
     * @param gameid
     * @param userid
     * @param isios
     * @return
     */
    public static String md5MorePaySign(String gameid, String userid, String isios) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(gameid).append(isios).append(userid).append(PAYSECRET);
        String param = buffer.toString();

        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(param.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;

    }

    /**
     * 悬浮窗口支付的签名
     * @param userId
     * @param roleId
     * @param serverId
     * @param gameId
     * @param lang
     * @param isios
     * @return
     */
    public static String md5MorePaySign(String userId, String roleId,String serverId,String gameId, String lang,String isios,String version) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(gameId).append(isios).append(lang).append(roleId).append(serverId).append(userId).append(version).append(FLOATPAYSECRET);
        String param = buffer.toString();

        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(param.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;

    }

    @SuppressWarnings("deprecation")
    public static String md5Sign(String log, String timestamp) {
        String secret = "cUixmNch.";
        StringBuffer buffer = new StringBuffer();
        buffer.append(secret).append(URLEncoder.encode(log)).append(timestamp);
        String param = buffer.toString();

        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(param.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //Log.i(StringUtil.Log,"sign= "+re_md5);
        return re_md5;
    }

    /**
     * 微信支付使用MD5加密方法，测试时用的,就是demo中的MD5
     *
     * @param buffer
     * @return
     */
    public static String getMessageDigest(byte[] buffer) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

}
