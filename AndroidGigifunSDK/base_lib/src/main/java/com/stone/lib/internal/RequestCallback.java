package com.stone.lib.internal;

import android.graphics.Bitmap;

/**
 * The basic HTTP basic callback.
 */
public interface RequestCallback {

	/**
	 * Callback method indicating that HTTP request succeeded.
	 * 
	 * <li>
	 * For 'GET', object may be a JSON object or the response string.
	 * 
	 * <li>
	 * For 'POST', object may be a JSON object or the response string.
	 * 
	 * <li>
	 * For 'UPLOAD', object is null.
	 * 
	 * <li>
	 * For 'BITMAP', object is a {@link Bitmap}.
	 * 
	 * @param object
	 */
	public void onSuccess(Object object);

	/**
	 * Callback method indicating that HTTP request failed.
	 * 
	 * @param error
	 *            The {@link ServerError} object.
	 */
	public void onFail(ServerError error);

}
