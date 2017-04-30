package com.wpt.frame.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @Desc:手机设备工具类
 * @author wpt
 * @since 2013-6-27 下午3:07:44
 */
public class DevicesUtil {

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取手机IMEI
	 */
	public static String getDeviceId(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

	/**
	 * @Desc: 获取手机IMSI
	 * @author wpt
	 * @since 2013-6-27 下午3:19:33
	 */
	public static String getImsiCode(Context context) {
		final TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		final String res = tm.getSubscriberId();
		if (res == null || "".equals(res)) {
			return "";
		}
		return res;
	}

	/**
	 * 获取ip
	 */
	public static String getIp(Context context) {
		Enumeration<NetworkInterface> netInterfaces = null;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> ips = ni.getInetAddresses();
				while (ips.hasMoreElements()) {
					InetAddress ip = ips.nextElement();
					if (!ip.isLoopbackAddress()
							&& RegUtils.isIp(ip.getHostAddress())) {
						return ip.getHostAddress();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "0.0.0.0";
	}

	/**
	 * 获取ip
	 */
	public static String getWifiIp(Context context) {
		WifiManager wifi_service = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcpInfo = wifi_service.getDhcpInfo();
		WifiInfo wifiinfo = wifi_service.getConnectionInfo();
		System.out.println("Wifi info----->" + wifiinfo.getIpAddress());
		System.out.println("DHCP info gateway----->"
				+ Formatter.formatIpAddress(dhcpInfo.gateway));
		System.out.println("DHCP info netmask----->"
				+ Formatter.formatIpAddress(dhcpInfo.netmask));
		// DhcpInfo中的ipAddress是一个int型的变量，通过Formatter将其转化为字符串IP地址
		return Formatter.formatIpAddress(dhcpInfo.ipAddress);
	}

	/**
	 * @Desc: 获取手机Mac地址
	 * @author wpt
	 * @since 2013-6-27 下午3:20:31
	 */
	public static String getLocalMacAddress(Context context) {
		final WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		final WifiInfo info = wifiManager.getConnectionInfo();
		final String res = info.getMacAddress();
		if (res == null || "".equals(res)) {
			return "00:00:00:00:00:00";
		}
		return res;
	}

	/**
	 * @Desc: 获取手机Android id
	 * @author wpt
	 * @since 2013-6-27 下午3:21:05
	 */
	public static String getAndroidId(Context context) {
		String androidId = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);
		return androidId;
	}

	/**
	 * @Desc: 获取手机屏幕宽度
	 * @author wpt
	 * @since 2013-6-18 下午4:18:25
	 */
	public static int getScreenWidth(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.widthPixels;
	}

	/**
	 * @Desc: 获取手机尺寸
	 * @author wpt
	 * @since 2013-6-27 下午3:25:43
	 */
	public static String getDisplay(Context context) {

		DisplayMetrics metric = new DisplayMetrics();
		WindowManager manager = (WindowManager) context
				.getSystemService("window");
		manager.getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;
		int height = metric.heightPixels;
		// float density = metric.density;
		int densityDpi = metric.densityDpi;

		// 对角线的长度
		Double diagonal = Math.sqrt(width * width + height * height);
		Double d = diagonal / densityDpi;
		String c = Double.toString(d);
		if (c != null && c.indexOf(".") != -1) {
			c = c.substring(0, c.indexOf(".") + 2);
		}

		return c;
	}

	/**
	 * @Desc: 判断是否为正确的手机号
	 * @author wpt
	 * @since 2013-6-27 下午3:13:00
	 */
	public static boolean isMobileNO(String mobiles) {
		// Pattern p =
		// Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-3,5-9]))\\d{8}$");
		Pattern p = Pattern
				.compile("^1(3[\\d]|4[57]|5[012356789]|8[012356789])\\d{8}$");
		Matcher m = p.matcher(mobiles);
		// System.out.println(m.matches()+"---");
		return m.matches();
	}

	/**
	 * @Desc: 获取系统版本
	 * @author wpt
	 * @since 2013-6-27 下午3:27:19
	 */
	public static String getOs() {

		String os = Build.VERSION.RELEASE;

		return os;
	}

	/**
	 * @Desc: 获取手机型号
	 * @author wpt
	 * @since 2013-6-27 下午3:28:17
	 */
	public static String getModel() {

		String model = Build.MODEL;
		return model;
	}

	/**
	 * @Desc: 获取手机生产商
	 * @author wpt
	 * @since 2013-6-27 下午3:28:04
	 */
	public static String getBrand() {

		String brand = Build.MANUFACTURER;

		return brand;
	}
}
