package com.wpt.frame.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;

public class FileUtil {

	/**
	 * 保存数据到本地
	 * 
	 * @param context
	 * @param fileName
	 * @param content
	 */
	public static void saveData(Context context, String fileName, String content) {

		try {
			FileOutputStream outStream = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			outStream.write(content.getBytes());
			outStream.close();
		} catch (FileNotFoundException e) {
			return;
		} catch (IOException e) {
			return;
		}

	}

	/**
	 * @Desc: 创建文件
	 * @since 2013-11-25 下午2:08:22 
	 */
	public static void makeDirs(String dirPath) {
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	/**
	 * 从本地读取文件里的数据
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String readData(Context context, String fileName) {
		String content = null;
		try {
			FileInputStream inStream = context.openFileInput(fileName);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length = -1;
			while ((length = inStream.read(buffer)) != -1) {
				stream.write(buffer, 0, length);
			}
			stream.close();
			inStream.close();
			content = stream.toString();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			return null;
		}
		return content;
	}

	public static String getSdcardPath() {
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		return path;
	}

	public static void saveSharedPre(Context context, String name, String key,
			String value) {
		SharedPreferences sp = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getSharedPreValue(Context context, String name,
			String key) {
		SharedPreferences settingInfo = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		String value = settingInfo.getString(key, null);
		return value;
	}

	/**
	 * @Desc: 删除文件
	 * @since 2013-11-20 上午11:17:44
	 */
	public static boolean delFile(String filePath) {
		File file = new File(filePath);
		getFile(file);
		return true;
	}

	private static void getFile(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (File f : files) {
					getFile(f);
				}
			} else {
				file.delete();
			}
		}
	}

	public static long getDataSize(String filePath) {
		File file = new File(filePath);
		long fileSize = getFileSize(file);
		return fileSize;
	}

	private static long getFileSize(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				long size = 0;
				File[] files = file.listFiles();
				for (File f : files) {
					size += getFileSize(f);
				}
				return size;
			} else {
				long size = file.length();
				return size;
			}
		}
		return 0;
	}

	/**
	 * 格式化文件大小
	 * 
	 * @param length
	 * @return
	 */
	public static String formatFileSize(long length) {
		String result = null;
		int sub_string = 0;
		if (length >= 1073741824) {
			sub_string = String.valueOf((float) length / 1073741824).indexOf(
					".");
			result = ((float) length / 1073741824 + "000").substring(0,
					sub_string + 3) + "GB";
		} else if (length >= 1048576) {
			sub_string = String.valueOf((float) length / 1048576).indexOf(".");
			result = ((float) length / 1048576 + "000").substring(0,
					sub_string + 3) + "MB";
		} else if (length >= 1024) {
			sub_string = String.valueOf((float) length / 1024).indexOf(".");
			result = ((float) length / 1024 + "000").substring(0,
					sub_string + 3) + "KB";
		} else if (length < 1024)
			result = Long.toString(length) + "B";
		return result;
	}
}
