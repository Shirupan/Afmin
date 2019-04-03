package com.stone.single.pack.entity;

public class PayResult {

	public int code = -1;
	public String msg;
	public String productId;
	public int methodId;
	public int paymentType;
	public String orderId;
	public String extral;
	public long date;

	public PayResult(int code, String msg, String productId, int methodId,
                     int paymentType) {
		this.code = code;
		this.msg = msg;
		this.productId = productId;
		this.methodId = methodId;
		this.paymentType = paymentType;
	}

	public PayResult(int code, String msg, String productId, int methodId,
                     int paymentType, String orderId) {
		this.code = code;
		this.msg = msg;
		this.productId = productId;
		this.methodId = methodId;
		this.paymentType = paymentType;
		this.orderId = orderId;
	}

	public PayResult(int code, String msg, String productId, int methodId,
                     int paymentType, String orderId, String extral, long date) {
		this.code = code;
		this.msg = msg;
		this.productId = productId;
		this.methodId = methodId;
		this.paymentType = paymentType;
		this.orderId = orderId;
		this.extral = extral;
		this.date = date;
	}
}
