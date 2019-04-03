package com.stone.lib.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * a util class to handle the io or have nothing to do with context
 * 
 */
public class Utils {

	private static final String TAG = "Utils";//add by Bush.luo,for sdk version3.0,20140919 
	
	/**
	 * InputStream to ByteArray
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static final byte[] toByteArray(InputStream is) throws IOException {
		final int CHUNK_SIZE = 4096;
		byte readBuffer[] = new byte[CHUNK_SIZE];
		ByteArrayOutputStream accumulator = new ByteArrayOutputStream();
		int count;
		while ((count = is.read(readBuffer)) > 0) {
			accumulator.write(readBuffer, 0, count);
		}
		accumulator.close();
		return accumulator.toByteArray();
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 */
	public static void deleteFiles(File path) {
		if (path.isDirectory()) {
			String[] files = path.list();
			for (String name : files) {
				File child = new File(path, name);
				deleteFiles(child);
			}
		}
		path.delete();
	}

	// Copies all files under srcDir to dstDir.
	// If dstDir does not exist, it will be created.
	public static void copyDirectory(File srcDir, File dstDir)
			throws IOException {
		if (srcDir.isDirectory()) {
			if (!dstDir.exists()) {
				dstDir.mkdir();
			}

			String[] children = srcDir.list();
			for (int i = 0; i < children.length; i++) {
				copyDirectory(new File(srcDir, children[i]), new File(dstDir,
						children[i]));
			}
		} else {
			// This method is implemented in Copying a File
			copyFile(srcDir, dstDir);
		}
	}

	public static void copyFile(File src, File dst) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);
		copyStream(in, out);
	}

	public static void copyStreamAndLeaveInputOpen(InputStream in,
			OutputStream out) throws IOException {
		// Copy the bits from instream to outstream
		int len;
		byte[] copyBuffer = new byte[16384];
		while ((len = in.read(copyBuffer)) > 0) {
			out.write(copyBuffer, 0, len);
		}
		out.close();
	}

	/**
	 * 拷贝 流
	 * 
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public static void copyStream(InputStream in, OutputStream out)
			throws IOException {
		copyStreamAndLeaveInputOpen(in, out);
		in.close();
	}

	public static void saveFile(byte[] in, String path) throws IOException {
		File file = new File(path);
		file.getParentFile().mkdirs();
		FileOutputStream out = new FileOutputStream(file);
		out.write(in);
		out.close();
	}

	/**
	 * 拷贝文件
	 * 
	 * @param in
	 *            输入流
	 * @param path
	 *            拷贝文件的地址
	 * @throws IOException
	 */
	public static void saveStreamAndLeaveInputOpen(InputStream in, String path)
			throws IOException {
		File file = new File(path);
		file.getParentFile().mkdirs();
		FileOutputStream out = new FileOutputStream(file);
		copyStreamAndLeaveInputOpen(in, out);
	}

	/**
	 * 
	 * @param in
	 * @param path
	 * @throws IOException
	 */
	public static void saveStream(InputStream in, String path)
			throws IOException {
		saveStreamAndLeaveInputOpen(in, path);
		in.close();
	}

	private static final String SYSTEM_DOWNLOAD_PATH = Environment
			.getExternalStorageState() + "download/";

	/**
	 * 文件是否存在
	 * 
	 * @param url
	 *            文件地址
	 * @return 文件是否存在
	 */
	public static boolean isFileExist(String url) {
		int start = url.indexOf("/");
		if (start == -1) {
			return false;
		}
		String fileName = url.substring(start + 1);
		File target = new File(SYSTEM_DOWNLOAD_PATH + fileName);
		return target.exists();
	}

	/**
	 * create folder if it didn't exist
	 * 
	 * @param str
	 *            path
	 */
	public static void createFolder(String str) {

		String path2 = str;
		File PATH2 = new File(path2);
		if (!PATH2.exists()) {
			boolean isCreate = PATH2.mkdirs();
			if (!isCreate) {
				return;
			}
		}
	}

	/**
	 * 读简单的文件
	 * 
	 * @param file
	 *            文件对象
	 * @return 字符
	 */
	public static String readSimplelyFile(File file) {
		if (!file.exists()) {
			return null;
		}
		ByteArrayOutputStream baos = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			baos = new ByteArrayOutputStream(4096);
			byte[] b = new byte[1024];
			int a = -1;
			while ((a = fis.read(b)) != -1) {
				baos.write(b, 0, a);
			}
			return baos.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != fis) {
				try {
					fis.close();

				} catch (IOException e) {
				}
			}
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}
	/**
	 * 读简单的文件
	 * 
	 * @param is
	 *            文件流
	 * @return 字符
	 */
	public static String readSimplelyFile(InputStream is) {
		if (is == null) {
			return null;
		}
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream(4096);
			byte[] b = new byte[1024];
			int a = -1;
			while ((a = is.read(b)) != -1) {
				baos.write(b, 0, a);
			}
			return baos.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != is) {
				try {
					is.close();

				} catch (IOException e) {
				}
			}
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	/**
	 * 写简单的文件
	 * 
	 * @param file
	 *            文件对象
	 * @param src
	 *            要写入的字节
	 * @return
	 */
	public static boolean writeSimplyFile(File file, byte[] src) {
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(src);
			fos.flush();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != fos) {
				try {
					fos.close();
				} catch (IOException e) {
				}
			}

		}

		return false;
	}

	public static String getInputStreamAsString(InputStream is, String encoder) {
		if (is == null) {
			return "";
		}
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream(1024);
			byte[] buffer = new byte[1024];
			int bytesRead = is.read(buffer);
			while (bytesRead > 0) {
				baos.write(buffer, 0, bytesRead);
				bytesRead = is.read(buffer);
			}
			return baos.toString(encoder);
		} catch (IOException e) {
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
				}
			}
			try {
				is.close();
			} catch (IOException e) {
			}
		}
		return "";
	}

	public static boolean saveStringAsOutputStream(OutputStream os,
			byte[] content) {
		if (os == null) {
			return false;
		}
		try {
			os.write(content);
			os.flush();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (IOException e) {
			}
		}

		return false;
	}

	/**
	 * 解压一个压缩文档 到指定位置
	 * 
	 * @param zipFileString
	 *            压缩包的名字
	 * @param outPathString
	 *            指定的路径
	 * @throws Exception
	 */
	public static void unZipFolder(String zipFileString, String outPathString)
			throws Exception {
		java.util.zip.ZipInputStream inZip = new java.util.zip.ZipInputStream(
				new FileInputStream(zipFileString));
		java.util.zip.ZipEntry zipEntry;
		String szName = "";

		while ((zipEntry = inZip.getNextEntry()) != null) {
			szName = zipEntry.getName();

			if (zipEntry.isDirectory()) {

				// get the folder name of the widget
				szName = szName.substring(0, szName.length() - 1);
				File folder = new File(outPathString
						+ File.separator + szName);
				folder.mkdirs();

			} else {

				File file = new File(outPathString
						+ File.separator + szName);
				file.createNewFile();
				// get the output stream of the file
				FileOutputStream out = new FileOutputStream(
						file);
				int len;
				byte[] buffer = new byte[1024];
				// read (len) bytes into buffer
				while ((len = inZip.read(buffer)) != -1) {
					// write (len) byte from buffer at the position 0
					out.write(buffer, 0, len);
					out.flush();
				}
				out.close();
			}
		}// end of while

		inZip.close();

	}// end of func

	public static void bitmapToFile(Bitmap bitmap, String filePath) {
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(new File(filePath));
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			if (filePath.matches("(jpg|JPG)$")) {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			} else {
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
			}
			stream.write(bos.toByteArray());
			stream.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stream != null) {
					stream.close();
					stream = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String secondsToDateStr(float seconds, String format) {
		int hours = (int) seconds / 3600;
		// int dateInt = (int)(seconds % 3600) ;
		int mins = (int) seconds / 60;
		int afterSeconds = (int) (seconds % 60);
		StringBuilder sb = new StringBuilder();
		if (format.contains("hh")) {
			if (hours < 10) {
				sb.append("0").append(hours).append(":");
			} else {
				sb.append(hours).append(":");
			}
		}

		if (!format.contains("hh") && format.contains("mm")) {
			if (mins < 10) {
				sb.append("0").append(mins).append(":");
			} else {
				sb.append(mins).append(":");
			}
		} else {
			mins = (int) ((seconds % 3600) / 60);

			if (mins < 10) {
				sb.append("0").append(mins).append(":");
			} else {
				sb.append(mins).append(":");
			}
		}

		if (afterSeconds < 10) {
			sb.append("0").append(afterSeconds);
		} else {
			sb.append(afterSeconds);
		}
		if (format.contains(".")) {
			String secondsStr = String.valueOf(seconds);
			int f_S_len = format.length() - format.lastIndexOf(".") - 1;
			int s_S_len = secondsStr.length() - secondsStr.lastIndexOf(".") - 1;
			String subMilSeconds = null;
			if (f_S_len <= s_S_len) {
				subMilSeconds = secondsStr.substring(
						secondsStr.lastIndexOf("."),
						secondsStr.lastIndexOf(".") + f_S_len + 1);
			} else {
				subMilSeconds = secondsStr.substring(
						secondsStr.lastIndexOf("."),
						secondsStr.lastIndexOf(".") + s_S_len + 1)
						+ appendZero(f_S_len - s_S_len);
			}
			sb.append(subMilSeconds);
		}

		return sb.toString();
	}

	private static String appendZero(int num) {
		String s = "";
		for (int i = 0; i < num; i++) {
			s = s + "0";
		}
		return s;
	}

	/**
	 * 生成一个特殊13位标示
	 * 
	 * @return
	 */
	public static String generate13Label() {
		StringBuilder sb = new StringBuilder();
		String time = String.valueOf(System.currentTimeMillis());
		int len = time.length();
		if (len < 13) {
			sb.append(time);
			int suffixLen = 13 - len;
			for (int i = 0; i < suffixLen; i++) {
				sb.append(0);
			}
		} else if (len == 13) {
			sb.append(time);
		} else if (len > 13) {
			sb.append(time.substring(len - 13));
		}
		return sb.toString();
	}

	/**
	 * 对于MD5加密后的字符，做绝对值，低于2位的补0处理
	 * 
	 * @param target
	 *            要加密的字符
	 * @return md5加密后的字符
	 */
	public static String md5(String target) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] md5 = md.digest(target.getBytes());
			StringBuffer sb = new StringBuffer();
			String part = null;
			for (int i = 0; i < md5.length; i++) {
				part = Integer.toHexString(md5[i] & 0xFF);
				if (part.length() == 1) {
					part = "0" + part;
				}
				sb.append(part);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException ex) {
		}
		return null;
	}

	public static String getLanguage() {
		String loc = "zh_CN";
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

	public static String arrayToStr(ArrayList<String> list) {
		if (list == null || list.size() == 0) {
			return "[]";
		} else {
			JSONArray array = new JSONArray();
			for (String s : list) {
				array.put(s);
			}
			return array.toString();
		}
	}

	public static ArrayList<String> strToArray(String jsonArray) {
		ArrayList<String> list = new ArrayList<String>();
		if (jsonArray != null && !"".equals(jsonArray)) {
			try {
				String s = jsonArray.replace("[", "").replace("]", "");
				String[] ss = s.split(",");
				if (ss != null && ss.length >= 1) {
					for (int i = 0; i < ss.length; i++) {
						list.add(ss[i]);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

}
