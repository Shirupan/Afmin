/**   
 * 文件名：PurchaseInfo.java</br>
 * 描述： </br>
 * 开发人员：杜逸平 </br>
 * 创建时间： 2014-10-25
 */ 

package com.gigifun.gp.model;

import java.io.Serializable;

/** 
 * 类名: PurchaseInfo</br> 
 * 包名：com.shenyuzhiguang.gamesdkjar </br> 
 * 描述: </br>
 * 发布版本号：</br>
 * 开发人员： 杜逸平</br>
 * 创建时间： 2014-10-25 
 */

public class PurchaseInfo implements Serializable{
	/**
	 * 描述:
	 */
	
	private static final long serialVersionUID = 1L;
	private String coin; // 货币名称
	private String product;// 商品名称
	private String sku; // google play内购商品id
	private String uid; // 游戏客户端id
	private String lnid;// 登录id
	private String amount; // 充值金额(美元)
	private String coOrderId;// 游戏商订单号
	private String encode_coOrderId;
	private String Client_id;
	private String serverId;// 服务器id
	private String mode;//哪种支付方式
	private String payStatus;//支付前 1, 支付成功  2, 支付后检查订单出现异常 3
	private String transactionId;//谷歌订单id
	private String encode_transactionId;
	private String payMent;
	private String reason;
	private String originalJson;
	private String signture;
	private String currentTime;
	private String Ctext;
	
	public String getCoin() {
		return coin;
	}
	public void setCoin(String coin) {
		this.coin = coin;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getLnid() {
		return lnid;
	}
	public void setLnid(String lnid) {
		this.lnid = lnid;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCoOrderId() {
		return coOrderId;
	}
	public void setCoOrderId(String coOrderId) {
		this.coOrderId = coOrderId;
	}
	public String getClient_id() {
		return Client_id;
	}
	public void setClient_id(String client_id) {
		Client_id = client_id;
	}
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getPayMent() {
		return payMent;
	}
	public void setPayMent(String payMent) {
		this.payMent = payMent;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getOriginalJson() {
		return originalJson;
	}
	public void setOriginalJson(String originalJson) {
		this.originalJson = originalJson;
	}
	
	public String getSignture() {
		return signture;
	}
	public void setSignture(String signture) {
		this.signture = signture;
	}
	public String getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}
	public String getEncode_coOrderId() {
		return encode_coOrderId;
	}
	public void setEncode_coOrderId(String encode_coOrderId) {
		this.encode_coOrderId = encode_coOrderId;
	}
	public String getEncode_transactionId() {
		return encode_transactionId;
	}
	public void setEncode_transactionId(String encode_transactionId) {
		this.encode_transactionId = encode_transactionId;
	}
	public String getCtext() {
		return Ctext;
	}
	public void setCtext(String ctext) {
		Ctext = ctext;
	}
	
	
	
}
