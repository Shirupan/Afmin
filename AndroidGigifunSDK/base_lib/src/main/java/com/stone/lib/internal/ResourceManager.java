package com.stone.lib.internal;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.sax.Element;
import android.sax.RootElement;
import android.sax.TextElementListener;
import android.util.Log;

import com.stone.lib.utils.ContextUtil;
import com.stone.lib.utils.LogUtils;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ResourceManager {

	private static final String TAG = "ResourceManager";

	private Context mApplicationContext;

	private ArrayList<StringDir> mFilteredStringDirs;
	private ArrayList<DrawableDir> mFilteredDrawableDirs;
	private ArrayList<ColorDir> mFilteredColorDirs;

	private ArrayList<StringDir> mNewlyCandidateStringDirs;

	private ArrayList<DrawableDir> mNewlyCandidateDrawableDirs;

	private ArrayList<ColorDir> mNewlyCandidateColorDirs;

	private LinkedList<String> mDrawablePathList = new LinkedList<String>();
	private LinkedList<String> mStringPathList = new LinkedList<String>();
	private LinkedList<String> mColorPathList = new LinkedList<String>();

	private boolean mDrawableDirsChanged;
	private boolean mStringDirsChanged;
	private boolean mColorDirsChanged;

	private static ResourceManager sGlobalManager;

	/**
	 * Get the global single {@link ResourceManager} instance, with the global
	 *
	 * @param appContext
	 *            the context
	 * @return the global {@link ResourceManager}
	 */
	public static ResourceManager getGlobal(Context appContext) {
		if (sGlobalManager == null) {
			synchronized (ResourceManager.class) {
				if (sGlobalManager == null) {
					sGlobalManager = new ResourceManager(
							appContext.getApplicationContext());
					sGlobalManager.addStringPath("stone/common", "string",
							"values.xml");
					sGlobalManager.addStringPath("stone/common", "string-en_US",
							"values.xml");
					sGlobalManager.commit();
				}
			}
		}
		return sGlobalManager;
	}

	/**
	 *
	 * @param appContext
	 *            better be a global application context.
	 */
	public ResourceManager(Context appContext) {
		mApplicationContext = appContext;

		mFilteredDrawableDirs = new ArrayList<DrawableDir>();
		mFilteredStringDirs = new ArrayList<StringDir>();
		mFilteredColorDirs = new ArrayList<ColorDir>();

		mNewlyCandidateStringDirs = new ArrayList<StringDir>();
		mNewlyCandidateDrawableDirs = new ArrayList<DrawableDir>();
		mNewlyCandidateColorDirs = new ArrayList<ColorDir>();
	}

	private boolean mCommited;

	public void commit() {
		if (mCommited) {
			throw new IllegalStateException("commit called twice");
		}
		filterDrawableDirs();
		filterColorDirs();
		filterStringDirs();
		mCommited = true;
	}

	private static class DrawableDir {
		String filePath;
		String parentPath;
		ResourceConfig config;

		@Override
		public String toString() {
			return "[path=" + filePath + "]";
		}
	}

	private static class StringDir {
		ResourceConfig config;
		ArrayList<String> files = new ArrayList<String>();
		HashMap<String, String> stringsMap = new HashMap<String, String>();

		@Override
		public String toString() {
			return files.toString();
		}
	}

	private static class ColorDir {
		ResourceConfig config;
		ArrayList<String> files = new ArrayList<String>();
		HashMap<String, Integer> colorsMap = new HashMap<String, Integer>();

		@Override
		public String toString() {
			return files.toString();
		}
	}

	private void checkCommited() {
		if (mCommited) {
			throw new IllegalStateException("already commited");
		}
	}

	/**
	 * 增加一个Drawable资源入口
	 *
	 * @param parentDir
	 *            父路径，如'sn/payment'
	 * @param qualifiedFolders
	 *            合法的drawable文件夹，如'drawable', 'drawable-hdpi'等
	 */
	public void addDrawablePath(String parentDir, String... qualifiedFolders) {
		checkCommited();
		if (parentDir == null || qualifiedFolders == null) {
			throw new NullPointerException();
		}
		int size = qualifiedFolders.length;
		for (int i = 0; i < size; i++) {
			String fd = qualifiedFolders[i];
			if (fd == null) {
				throw new IllegalArgumentException(
						"has a null qualified folder in argument qualifiedFolders");
			}
			String wholePath = parentDir + "/" + fd;
			if (mDrawablePathList.contains(wholePath)) {
				LogUtils.w(TAG, "[" + wholePath + "] is already in the list");
				return;
			}
			mDrawablePathList.add(wholePath);

			DrawableDir dd = new DrawableDir();
			dd.config = ResourceConfig.getConfigOfFolder(fd);
			if (dd.config == null) {
				throw new RuntimeException("invalid drawable folder [" + fd
						+ "], path=[" + parentDir + "]");
			}
			dd.filePath = wholePath;
			dd.parentPath = parentDir;

			mDrawableDirsChanged = true;
			mNewlyCandidateDrawableDirs.add(dd);
		}
	}

	/**
	 * 增加一个String资源入口
	 *
	 * @param parentDir
	 *            父路径，如'sn/payment'
	 * @param qualifiedFolder
	 *            符合规范的string资源文件夹，如'string', 'string-zh_CN'等
	 * @param files
	 *            文件名集合如'values.xml', 'string.xml'等
	 */
	public void addStringPath(String parentDir, String qualifiedFolder,
			String... files) {
		checkCommited();
		if (parentDir == null || qualifiedFolder == null || files == null) {
			throw new NullPointerException();
		}
		int size = files.length;
		for (int i = 0; i < size; i++) {
			String file = files[i];
			if (file == null) {
				throw new IllegalArgumentException(
						"has a null file in argument files");
			}
			String wholePath = parentDir + "/" + qualifiedFolder + "/" + file;
			if (mStringPathList.contains(wholePath)) {
				LogUtils.w(TAG, "[" + wholePath + "] is already in the list");
				return;
			}
			mStringPathList.add(wholePath);

			StringDir sd = new StringDir();
			sd.config = ResourceConfig.getConfigOfFolder(qualifiedFolder);
			if (sd.config == null) {
				throw new RuntimeException("invalid string folder ["
						+ qualifiedFolder + "], path=[" + parentDir + "]");
			}
			InputStream is = null;
			try {
				is = mApplicationContext.getAssets().open(wholePath);
				parseXmlStringFile(is, sd.stringsMap);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (Exception e) {
					}
				}
			}

			mStringDirsChanged = true;
			mNewlyCandidateStringDirs.add(sd);
		}
	}

	/**
	 * 增加一个Color资源文件夹路径
	 *
	 * @param parentDir
	 *            父路径，如'sn/charge'
	 * @param qualifiedFolder
	 *            符合规范的string资源文件夹，如'color', 'color-zh_CN'等
	 * @param files
	 *            文件名集合如'values.xml', 'colors.xml'等
	 */
	public void addColorPath(String parentDir, String qualifiedFolder,
			String... files) {
		checkCommited();
		if (parentDir == null || qualifiedFolder == null || files == null) {
			throw new NullPointerException();
		}
		int size = files.length;
		for (int i = 0; i < size; i++) {
			String file = files[i];
			if (file == null) {
				throw new IllegalArgumentException(
						"has a null file in argument files");
			}
			String wholePath = parentDir + "/" + qualifiedFolder + "/" + file;
			if (mColorPathList.contains(wholePath)) {
				LogUtils.w(TAG, "[" + wholePath + "] is already in the list");
				return;
			}
			mColorPathList.add(wholePath);

			ColorDir cd = new ColorDir();
			cd.config = ResourceConfig.getConfigOfFolder(qualifiedFolder);
			if (cd.config == null) {
				throw new RuntimeException("invalid color folder ["
						+ qualifiedFolder + "], path=[" + parentDir + "]");
			}
			InputStream is = null;
			try {
				is = mApplicationContext.getAssets().open(wholePath);
				parseXmlColorFile(is, cd.colorsMap);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (Exception e) {
					}
				}
			}

			mColorDirsChanged = true;
			mNewlyCandidateColorDirs.add(cd);
		}
	}

	/**
	 * This should be called when any of local Configuration changes.
	 */
	private void filterDrawableDirs() {
		if (!mDrawableDirsChanged) {
			// no drawable directories added
			return;
		}

		// mFilteredDrawableDirs.clear();

		/*
		 * If the count of the newly candidate drawable directories is less than
		 * 1...
		 */
		if (mNewlyCandidateDrawableDirs.size() == 1) {
			mFilteredDrawableDirs.addAll(mNewlyCandidateDrawableDirs);
			return;
		}

		/*
		 * See "How Android Finds the Best-matching Resource"
		 *
		 * <p>
		 * . html#BestMatch </p>
		 *
		 * /* get the local Configuration.
		 */
		final ResourceConfig localConfig = getLocalConfig(mApplicationContext);

		/*
		 * cache the candidate directories.
		 */
		ArrayList<DrawableDir> dirs = mNewlyCandidateDrawableDirs;

		/*
		 * Use a collection to cache different parent directories.
		 */
		HashSet<String> parentDirSet = new HashSet<String>();

		/*
		 * Step 1. Eliminate resource files that contradict the device
		 * configuration
		 */
		ArrayList<DrawableDir> noConflictDirs = new ArrayList<DrawableDir>();
		for (DrawableDir r : dirs) {
			parentDirSet.add(r.parentPath);
			if (!localConfig.conflictWith(r.config)) {
				noConflictDirs.add(r);
			}
		}

		/*
		 * Step 2. Pick the (next) highest-precedence qualifier in the
		 * ResourceConfig. (Start with screenSize, then move down.)
		 *
		 * Step 3. Do any of the resource directories include this qualifier?
		 *
		 * -If No, return to step 2 and look at the next qualifier.
		 *
		 * -If Yes, eliminate resource directories that do not include this
		 * qualifier.
		 */

		localConfig.beginTravelQualifiers();
		while (localConfig.hasNext()) {
			String qualifier = localConfig.nextQualifier();
			/*
			 * Indicates that whether there is any of the resource directories
			 * include this qualifier.
			 */
			boolean has = false;
			ArrayList<DrawableDir> toBeRemoved = new ArrayList<DrawableDir>();
			for (DrawableDir resourceDir : noConflictDirs) {
				ResourceConfig tmpConfig = resourceDir.config;
				String tmpQualifier = tmpConfig.getQualifierAtIndex(localConfig
						.getTravelIndex() - 1);
				if (qualifier.equals(tmpQualifier)) {
					has = true;
				} else {
					toBeRemoved.add(resourceDir);
				}
			}
			if (has) {
				/*
				 * At least one resource directory contains includes this
				 * qualifier, remove those do not include.
				 */
				noConflictDirs.removeAll(toBeRemoved);
			} else {
				/*
				 * No resource directories include this qualifier, to next
				 * qualifier.
				 */
				continue;
			}

			/*
			 * To make sure at least one directory is left.
			 */
			if (noConflictDirs.size() == 1) {
				break;
			}
		}

		for (String parentDir : parentDirSet) {
			/*
			 * Add an default drawable directories.
			 */
			// TODO not perfect, will fix in future.
			DrawableDir dir = new DrawableDir();
			if (localConfig.screenDensity.equals("mdpi")
					|| localConfig.screenDensity.equals("ldpi")) {
				dir.filePath = parentDir + "/drawable-mdpi";
			} else {
				dir.filePath = parentDir + "/drawable-hdpi";
			}
			dir.parentPath = parentDir;
			noConflictDirs.add(dir);

			DrawableDir defaultDir = new DrawableDir();
			defaultDir.filePath = parentDir + "/drawable";
			defaultDir.parentPath = parentDir;
			noConflictDirs.add(defaultDir);
		}

		mFilteredDrawableDirs.addAll(noConflictDirs);
		mDrawableDirsChanged = false;
	}

	/**
	 * This should be called when any of local Configuration changes.
	 */
	private void filterStringDirs() {
		if (!mStringDirsChanged) {
			// no drawable directories added
			return;
		}

		if (mNewlyCandidateStringDirs.size() <= 1) {
			mFilteredStringDirs.addAll(mNewlyCandidateStringDirs);
			return;
		}

		/*
		 * get the local Configuration.
		 */
		final ResourceConfig localConfig = getLocalConfig(mApplicationContext);

		/*
		 * cache the candidate directories.
		 */
		ArrayList<StringDir> dirs = mNewlyCandidateStringDirs;

		/*
		 * Step 1. Eliminate resource files that contradict the device
		 */
		ArrayList<StringDir> noConflictDirs = new ArrayList<StringDir>();
		for (StringDir r : dirs) {
			if (!localConfig.conflictWith(r.config)) {
				noConflictDirs.add(r);
			}
		}

		/*
		 * Step 2. Pick the (next) highest-precedence qualifier in the
		 * ResourceConfig. (Start with screenSize, then move down.)
		 *
		 * Step 3. Do any of the resource directories include this qualifier?
		 *
		 * -If No, return to step 2 and look at the next qualifier.
		 *
		 * -If Yes, eliminate resource directories that do not include this
		 * qualifier.
		 */

		localConfig.beginTravelQualifiers();
		while (localConfig.hasNext()) {
			String qualifier = localConfig.nextQualifier();
			/*
			 * Indicates that whether there is any of the resource directories
			 * include this qualifier.
			 */
			boolean has = false;
			ArrayList<StringDir> toBeRemoved = new ArrayList<StringDir>();
			for (StringDir resourceDir : noConflictDirs) {
				ResourceConfig tmpConfig = resourceDir.config;
				String tmpQualifier = tmpConfig.getQualifierAtIndex(localConfig
						.getTravelIndex() - 1);
				if (qualifier.equals(tmpQualifier)) {
					has = true;
				} else {
					toBeRemoved.add(resourceDir);
				}
			}
			if (has) {
				/*
				 * At least one resource directory contains includes this
				 * qualifier, remove those do not include.
				 */
				noConflictDirs.removeAll(toBeRemoved);
			} else {
				/*
				 * No resource directories include this qualifier, to next
				 * qualifier.
				 */
				continue;
			}

			/*
			 * To make sure at least one directory is left.
			 */
			if (noConflictDirs.size() == 1) {
				break;
			}
		}

		mFilteredStringDirs.addAll(noConflictDirs);

		mStringDirsChanged = false;
	}

	/**
	 * This should be called when any of local Configuration changes.
	 */
	private void filterColorDirs() {
		if (!mColorDirsChanged) {
			// no color directories added
			return;
		}

		if (mNewlyCandidateColorDirs.size() <= 1) {
			mFilteredColorDirs.addAll(mNewlyCandidateColorDirs);
			return;
		}

		/*
		 * get the local Configuration.
		 */
		final ResourceConfig localConfig = getLocalConfig(mApplicationContext);

		/*
		 * cache the candidate directories.
		 */
		ArrayList<ColorDir> dirs = mNewlyCandidateColorDirs;

		/*
		 * Step 1. Eliminate resource files that contradict the device
		 */
		ArrayList<ColorDir> noConflictDirs = new ArrayList<ColorDir>();
		for (ColorDir r : dirs) {
			if (!localConfig.conflictWith(r.config)) {
				noConflictDirs.add(r);
			}
		}

		/*
		 * Step 2. Pick the (next) highest-precedence qualifier in the
		 * ResourceConfig. (Start with screenSize, then move down.)
		 *
		 * Step 3. Do any of the resource directories include this qualifier?
		 *
		 * -If No, return to step 2 and look at the next qualifier.
		 *
		 * -If Yes, eliminate resource directories that do not include this
		 * qualifier.
		 */

		localConfig.beginTravelQualifiers();
		while (localConfig.hasNext()) {
			String qualifier = localConfig.nextQualifier();
			/*
			 * Indicates that whether there is any of the resource directories
			 * include this qualifier.
			 */
			boolean has = false;
			ArrayList<ColorDir> toBeRemoved = new ArrayList<ColorDir>();
			for (ColorDir resourceDir : noConflictDirs) {
				ResourceConfig tmpConfig = resourceDir.config;
				String tmpQualifier = tmpConfig.getQualifierAtIndex(localConfig
						.getTravelIndex() - 1);
				if (qualifier.equals(tmpQualifier)) {
					has = true;
				} else {
					toBeRemoved.add(resourceDir);
				}
			}
			if (has) {
				/*
				 * At least one resource directory contains includes this
				 * qualifier, remove those do not include.
				 */
				noConflictDirs.removeAll(toBeRemoved);
			} else {
				/*
				 * No resource directories include this qualifier, to next
				 * qualifier.
				 */
				continue;
			}

			/*
			 * To make sure at least one directory is left.
			 */
			if (noConflictDirs.size() == 1) {
				break;
			}
		}

		mFilteredColorDirs.addAll(noConflictDirs);

		mColorDirsChanged = false;
	}

	/**
	 * 获取指定资源的{@link Drawable}对象，如果没找到，则返回null
	 */
	public Drawable getDrawable(String name) {
		printWarningIfNotCommited();
		Drawable drawable = null;
		InputStream is = null;
		AssetManager am = mApplicationContext.getAssets();
		ArrayList<DrawableDir> dirs = mFilteredDrawableDirs;
		int len = dirs.size();
		for (int i = 0; i < len; i++) {
			DrawableDir dir = dirs.get(i);
			try {
				is = am.open(dir.filePath + "/" + name);
				break;
			} catch (IOException e) {
			}
		}

		if (name.endsWith(".9.png") || name.endsWith(".9.PNG")) {
			drawable = Drawable.createFromStream(is, "drawable");
		} else {
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			if (bitmap != null) {
				drawable = new BitmapDrawable(
						mApplicationContext.getResources(), bitmap);
			}
		}

		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
			}
		}
		if (drawable == null) {
			LogUtils.e(TAG, "drawable '" + name + "' not found in config: "
					+ getLocalConfig(mApplicationContext).toString());
			LogUtils.e(TAG, "resource path=" + mDrawablePathList);
		}
		return drawable;
	}

	/**
	 * 获取指定资源的{@link Drawable}对象
	 */
	public Drawable getDrawable(String name, BitmapFactory.Options o) {
		printWarningIfNotCommited();
		Drawable drawable = null;
		InputStream is = null;
		AssetManager am = mApplicationContext.getAssets();
		ArrayList<DrawableDir> dirs = mFilteredDrawableDirs;
		int len = dirs.size();
		for (int i = 0; i < len; i++) {
			DrawableDir dir = dirs.get(i);
			try {
				is = am.open(dir.filePath + "/" + name);
				break;
			} catch (IOException e) {
			}
		}

		if (name.endsWith(".9.png") || name.endsWith(".9.PNG")) {
			drawable = Drawable.createFromStream(is, "drawable");
		} else {
			Bitmap bitmap = BitmapFactory.decodeStream(is, null, o);
			if (bitmap != null) {
				drawable = new BitmapDrawable(
						mApplicationContext.getResources(), bitmap);
			}
		}

		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
			}
		}
		if (drawable == null) {
			LogUtils.e(TAG, "drawable '" + name + "' not found in config: "
					+ getLocalConfig(mApplicationContext).toString());
			LogUtils.e(TAG, "resource path=" + mDrawablePathList);
		}
		return drawable;
	}

	private void printWarningIfNotCommited() {
		if (!mCommited) {
			LogUtils.w(TAG, "accessing resource with forgetting to call commit()");
		}
	}

	/**
	 * 获取指定资源的字符串，如果没找到，则返回null
	 */
	public String getString(String key) {
		printWarningIfNotCommited();
		String value = null;
		final ArrayList<StringDir> dirs = mFilteredStringDirs;
		int len = dirs.size();
		for (int i = 0; i < len; i++) {
			StringDir dir = dirs.get(i);
			HashMap<String, String> stringMap = dir.stringsMap;
			value = stringMap.get(key);
			if (value != null) {
				break;
			}
		}
		if (value == null) {
			LogUtils.e(TAG, "string '" + key + "' not found in config: "
					+ getLocalConfig(mApplicationContext).toString());
			LogUtils.e(TAG, "resource path=" + mStringPathList);
		}

		return value;
	}

	/**
	 * 获取指定资源的颜色值，如果没找到，则返回{@link Color#TRANSPARENT}
	 * ，color资源支持#AARRGGBB,#RRGGBB格式
	 */
	public int getColor(String key) {
		printWarningIfNotCommited();
		Integer value = null;
		ArrayList<ColorDir> dirs = mFilteredColorDirs;
		int len = dirs.size();
		for (int i = 0; i < len; i++) {
			ColorDir dir = dirs.get(i);
			HashMap<String, Integer> stringMap = dir.colorsMap;
			value = stringMap.get(key);
			if (value != null) {
				break;
			}
		}
		if (value == null) {
			LogUtils.e(TAG, "color '" + key + "' not found in config: "
					+ getLocalConfig(mApplicationContext).toString());
			LogUtils.e(TAG, "resource path=" + mColorPathList);
		}

		return value != null ? value.intValue() : Color.TRANSPARENT;

	}

	private class KeyStorage {
		String key;
	}

	private void parseXmlStringFile(InputStream is,
			final HashMap<String, String> stringMap) throws IOException,
			SAXException, ParserConfigurationException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		XMLReader reader = parser.getXMLReader();
		RootElement rootElement = new RootElement("resources");
		Element stringElement = rootElement.getChild("string");
		final KeyStorage m = new KeyStorage();
		stringElement.setTextElementListener(new TextElementListener() {

			@Override
			public void end(String body) {
				stringMap.put(m.key, body.replace("\\n", "\n"));
			}

			@Override
			public void start(Attributes attributes) {
				m.key = attributes.getValue("name");
			}
		});

		reader.setContentHandler(rootElement.getContentHandler());
		reader.parse(new InputSource(is));
	}

	private void parseXmlColorFile(InputStream is,
			final HashMap<String, Integer> stringMap) throws IOException,
			SAXException, ParserConfigurationException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		XMLReader reader = parser.getXMLReader();
		RootElement rootElement = new RootElement("resources");
		Element colorElement = rootElement.getChild("color");
		final KeyStorage colorStorage = new KeyStorage();
		colorElement.setTextElementListener(new TextElementListener() {

			@Override
			public void end(String body) {
				stringMap.put(colorStorage.key, Color.parseColor(body));
			}

			@Override
			public void start(Attributes attributes) {
				colorStorage.key = attributes.getValue("name");
			}
		});

		reader.setContentHandler(rootElement.getContentHandler());
		reader.parse(new InputSource(is));
	}

	private String getLanguage() {
		// 如果没有配置项，或者该配置项为空，那么读取本地语言
		String loc = "en_US";//"zh_CN";
		Locale locale = Locale.getDefault();
		String country = locale.getCountry();
		String language = locale.getLanguage();
		if (Locale.CHINESE.getLanguage().equals(language)) {
			if (Locale.CHINA.getCountry().equals(country)) {
				loc = "zh_CN";
			} else {
				loc = "zh_TW";
			}
		} else if (Locale.ENGLISH.getLanguage().equals(language)) {
			loc = "en_US";
		}
		return loc;
	}

	public ResourceConfig getLocalConfig(Context context) {
		ResourceConfig config = new ResourceConfig();

		String locale = getLanguage();
		config.setLocale(locale);

		String size = null;
		double screenSize = ContextUtil.getPhysicalScreenSize(context);
		if (screenSize < 2.7d) {
			size = "small";
		} else if (screenSize >= 2.7d && screenSize < 4.65d) {
			size = "normal";
		} else if (screenSize >= 4.65d && screenSize < 7.0d) {
			size = "large";
		} else {
			size = "xlarge";
		}
		config.setSize(size);

		String des = null;
		int dpi = ContextUtil.getDensityInt(context);
		if (dpi < 140) {
			des = "ldpi";
		} else if (dpi >= 140 && dpi < 200) {
			des = "mdpi";
		} else if (dpi >= 200 && dpi < 280) {
			des = "hdpi";
		} else {
			des = "xhdpi";
		}
		config.setDensity(des);
		return config;
	}

}

