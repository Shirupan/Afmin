package com.stone.lib.internal;

import android.util.Log;

import com.stone.lib.utils.LogUtils;

public class ResourceConfig {

	private static final int POS_LOCALE = 0;
	private static final int POS_SCREEN_SIZE = 1;
	private static final int POS_SCREEN_DENSITY = 2;

	private static final int[] INDEXES = new int[] { POS_LOCALE,
			POS_SCREEN_SIZE, POS_SCREEN_DENSITY };

	/**
	 * The supported resources folder prefix.
	 */
	private static final String[] SUPPORTED_RESOURCES = new String[] {
			"string", "drawable", "color" };

	private static final String TAG = "ResourceConfig";

	private static final String DEFAULT = "any";

	/**
	 * Since SDK V1.2
	 */
	String screenSize = DEFAULT;

	/**
	 * Since SDK V1.2
	 */
	String screenDensity = DEFAULT;

	/**
	 * Since SDK V1.3
	 */
	String locale = DEFAULT;

	private int mQualifierIndex;

	public ResourceConfig() {
		mQualifierIndex = 0;
	}

	/**
	 * Check whether the two {@link #ResourceConfig} objects conflict with each
	 * other.
	 * 
	 * @param config
	 * @return
	 * @throws NullPointerException
	 *             if the config is null.
	 */
	public boolean conflictWith(ResourceConfig config) {
		boolean conflict = false;
		beginTravelQualifiers();
		config.beginTravelQualifiers();
		while (hasNext() && config.hasNext()) {
			String q = nextQualifier();
			String q2 = config.nextQualifier();
			if (!DEFAULT.equals(q) && !DEFAULT.equals(q2) && !q.equals(q2)) {
				conflict = true;
				break;
			}
		}
		return conflict;
	}

	public void beginTravelQualifiers() {
		mQualifierIndex = 0;
	}

	public boolean hasNext() {
		return mQualifierIndex < INDEXES.length;
	}

	public String nextQualifier() {
		String q = null;
		switch (mQualifierIndex) {
		case POS_LOCALE:
			q = locale;
			break;
		case POS_SCREEN_SIZE:
			q = screenSize;
			break;
		case POS_SCREEN_DENSITY:
			q = screenDensity;
			break;
		default:
			throw new RuntimeException("Has hasNext() returned true?");
		}
		mQualifierIndex++;
		return q;
	}

	int getTravelIndex() {
		return mQualifierIndex;
	}

	String getQualifierAtIndex(int index) {
		String q = null;
		switch (index) {
		case POS_LOCALE:
			q = locale;
			break;
		case POS_SCREEN_SIZE:
			q = screenSize;
			break;
		case POS_SCREEN_DENSITY:
			q = screenDensity;
			break;
		default:
			throw new RuntimeException("no qualifier found at index " + index);
		}
		return q;
	}

	boolean setLocale(String part) {
		if (part.equals("zh_CN")) {
			locale = part;
			return true;
		}
		if (part.equals("zh_TW")) {
			locale = part;
			return true;
		}
		if (part.equals("en_US")) {
			locale = part;
			return true;
		}
		return false;
	}

	boolean setDensity(String part) {
		if (part.equals("ldpi")) {
			screenDensity = part;
			return true;
		}
		if (part.equals("mdpi")) {
			screenDensity = part;
			return true;
		}
		if (part.equals("hdpi")) {
			screenDensity = part;
			return true;
		}
		if (part.equals("xhdpi")) {
			screenDensity = part;
			return true;
		}
		return false;
	}

	boolean setSize(String part) {
		if (part.equals("small")) {
			screenSize = part;
			return true;
		}
		if (part.equals("normal")) {
			screenSize = part;
			return true;
		}
		if (part.equals("large")) {
			screenSize = part;
			return true;
		}
		if (part.equals("xlarge")) {
			screenSize = part;
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "locale=" + locale + ", size=" + screenSize + ", density="
				+ screenDensity;
	}

	static int getQualifierCount() {
		return INDEXES.length;
	}

	/**
	 * Get the configuration specified by the folder name.
	 * 
	 * @param folderName
	 *            The folder name, like 'drawable-normal-hdpi', 'string-en_US',
	 *            etc.
	 * @return Null if the folder name is invalid.
	 */
	public static ResourceConfig getConfigOfFolder(String folderName) {
		final String lower = folderName.toLowerCase();

		String resource = null;
		for (String res : SUPPORTED_RESOURCES) {
			if (lower.startsWith(res)) {
				resource = res;
				break;
			}
		}
		if (resource == null) {
			/*
			 * Not in supported list.
			 */
			return null;
		}

		String leftStrings = folderName.substring(resource.length());
		if (leftStrings.length() != 0 && !leftStrings.startsWith("-")) {
			/*
			 * Like 'drawablexlarge'
			 */
			LogUtils.w(TAG, "Invalid folder: " + folderName);
			return null;
		}
		ResourceConfig config = new ResourceConfig();
		int pos = -1;
		while (leftStrings.startsWith("-")) {
			String tmp = leftStrings.substring(1);
			int index = tmp.indexOf("-");
			String q = null;
			if (index != -1) {
				q = tmp.substring(0, index);
				leftStrings = leftStrings.substring(index + 1);
			} else {
				q = tmp;
				leftStrings = "";
			}
			if (config.setLocale(q)) {
				if (pos > POS_LOCALE) {
					LogUtils.w(TAG, "Invalid sequence of qualifies in folder '"
							+ folderName + "'");
					return null;
				}
				pos = POS_LOCALE;
				continue;
			}

			if (config.setSize(q)) {
				if (pos > POS_SCREEN_SIZE) {
					LogUtils.w(TAG, "Invalid sequence of qualifies in folder '"
							+ folderName + "'");
					return null;
				}
				pos = POS_SCREEN_SIZE;
				continue;
			}

			if (config.setDensity(q)) {
				if (pos > POS_SCREEN_DENSITY) {
					LogUtils.w(TAG, "Invalid sequence of qualifies in folder '"
							+ folderName + "'");
					return null;
				}
				pos = POS_SCREEN_DENSITY;
				continue;
			}
			LogUtils.w(TAG, "Invalid folder: " + folderName
					+ ", unknown qualifier: " + q);
			return null;
		}

		return config;
	}

	public String getDensity() {
		return screenDensity;
	}

	public String getLocale() {
		return locale;
	}

	public String getScreenSize() {
		return screenSize;
	}
}
