package com.wpt.frame.util;

import java.util.List;
import java.util.Locale;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.util.Log;

/**
 * @Desc: APP工具类
 * @author wpt
 * @since 2013-6-27 下午3:09:35
 */
public class AppUtils {

	/**
	 * 查看service是否正在运行
	 */
	public static boolean isServiceStarted(Context context, String serviceName) {
		boolean isStarted = false;
		try {
			int intGetTastCounter = 1000;
			ActivityManager mActivityManager = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			List<ActivityManager.RunningServiceInfo> mRunningService = mActivityManager
					.getRunningServices(intGetTastCounter);
			for (ActivityManager.RunningServiceInfo amService : mRunningService) {
				if (amService.service.getClassName().equals(serviceName)) {
					isStarted = true;
					break;
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return isStarted;
	}

	/**
	 * 获取当前程序的版本名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getCurrentVersionName(Context context) {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo;
		String versionName = "0";
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
			versionName = packInfo.versionName;
		} catch (NameNotFoundException e) {
			Log.e("AppUtils", "getCurrentVersionName error", e);
		}
		return versionName;
	}

	/**
	 * 从SharedPreference中获取字段值
	 * 
	 * @param context
	 * @param fileName
	 * @param fieldName
	 * @return
	 */
	public static String getValueFromSp(Context context, String fileName,
			String fieldName) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				fileName, Context.MODE_PRIVATE);
		return sharedPreferences.getString(fieldName, "");
	}

	/**
	 * 向SharedPreference中更新字段值
	 * 
	 * @param context
	 * @param fileName
	 * @param fieldName
	 * @param fieldValue
	 */
	public static void putValueToSp(Context context, String fileName,
			String fieldName, String fieldValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				fileName, Context.MODE_PRIVATE);
		sharedPreferences.edit().putString(fieldName, fieldValue).commit();
	}

	/**
	 * 清空SharedPreference
	 * 
	 * @param context
	 * @param fileName
	 */
	public static void clearSp(Context context, String fileName) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				fileName, Context.MODE_PRIVATE);
		sharedPreferences.edit().clear().commit();
	}

	/**
	 * 添加桌面快捷方式
	 * 
	 * @param context
	 * @param pkg
	 * @return
	 */
	public static boolean addShortCut(Context context, String pkg) {
		String name = "unknown";
		String mainAct = null;
		int iconIdentifier = -1;
		PackageManager pkManager = context.getPackageManager();

		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		List<ResolveInfo> list = pkManager.queryIntentActivities(intent,
				PackageManager.GET_ACTIVITIES);
		for (ResolveInfo info : list) {
			if (info.activityInfo.packageName.equals(pkg)) {
				name = info.loadLabel(pkManager).toString();
				iconIdentifier = info.activityInfo.applicationInfo.icon;
				mainAct = info.activityInfo.name;
				break;
			}
		}

		if (mainAct == null || mainAct.equals("")) {
			return false;
		}

		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
		shortcut.putExtra("duplicate", false);
		ComponentName comp = new ComponentName(pkg, mainAct);

		Intent shortCutIntent = new Intent(Intent.ACTION_MAIN);
		shortCutIntent.setComponent(comp);
		shortCutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortCutIntent);

		Context pkgContext = null;
		if (pkg.equals(context.getPackageName())) {
			pkgContext = context;
		} else {
			try {
				pkgContext = context.createPackageContext(pkg,
						Context.CONTEXT_IGNORE_SECURITY
								| Context.CONTEXT_INCLUDE_CODE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (pkgContext != null) {
			Intent.ShortcutIconResource iconResource = Intent.ShortcutIconResource
					.fromContext(pkgContext, iconIdentifier);
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);
		}
		context.sendBroadcast(shortcut);

		return true;
	}

	public static String getAppLanguage(Context context, String configName,
			String key) {
		String language = FileUtil.getSharedPreValue(context, configName, key);
		return language;
	}

	@SuppressLint("DefaultLocale")
	public static String getSystemLanguage() {
		Locale locale = Locale.getDefault();
		String sysLanguage = locale.getLanguage();
		String country = locale.getCountry().toLowerCase();
		if ("zh".equals(sysLanguage)) {
			if ("cn".equals(country)) {
				sysLanguage = "CN";
			} else if ("tw".equals(country)) {
				sysLanguage = "TW";
			}
		}
		return sysLanguage;
	}

	// 判断应用是否在前台
	public static boolean isTopApp(Context context, String packageName) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> taskList = am.getRunningTasks(1);
		if (taskList != null && taskList.size() > 0) {
			if (packageName
					.equals(taskList.get(0).topActivity.getPackageName())) {
				return true;
			}
		}

		return false;
	}
}
