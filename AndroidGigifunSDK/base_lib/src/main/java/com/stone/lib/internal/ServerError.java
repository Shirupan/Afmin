package com.stone.lib.internal;


public class ServerError{

	/**
	 * The error detail from server.
	 */
	public String err_detail;

	/**
	 * The error code from server.
	 */
	public int err_code;

	@Override
	public String toString() {
//		ResourceManager m = ResourceManager.getGlobal(ChannelCache.get()
//				.getApplicationContext());
		ServerError error = this;
//		if (error == ResponseWrapper.TIMEOUT_ERROR) {
//			return m.getString("TIMEOUT_ERROR");
//		} else if (error == ResponseWrapper.IO_ERROR) {
//			return m.getString("NETWORK_ERROR");
//		} else if (error == ResponseWrapper.DATA_ERROR
//				|| error == ResponseWrapper.PARSE_JSON_OBJECT_ERROR) {
//			return m.getString("DATA_ERROR");
//		} else if (error == ResponseWrapper.SHIT_LIKE_CMCC_ERROR) {
//			return m.getString("UNAUTHORIZED_PUBLIC_HOT_SPOT_ERROR");
//		} else if (error.err_code >= 500 && error.err_code < 600) {
//			return m.getString("SERVER_ERROR");
//		}
		return error.err_detail;
	}

}
