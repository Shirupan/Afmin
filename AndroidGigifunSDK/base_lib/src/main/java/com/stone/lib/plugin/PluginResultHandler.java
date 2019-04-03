package com.stone.lib.plugin;

/**
 * A callback interface to handle callbacks between plugins.
 * 
 */
public interface PluginResultHandler {

	/**
	 * Called when the specified plugin has finished doing something and has the
	 * {@link PluginResult} sent to the receiver.
	 * 
	 * @param result
	 *            The {@link PluginResult} object.
	 */
	public void onHandlePluginResult(PluginResult result);

}
