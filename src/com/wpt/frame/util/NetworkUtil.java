package com.wpt.frame.util;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import java.net.InetSocketAddress;

/**
 * @dec 网络工具类
 * @author we
 * 
 */
public class NetworkUtil {
	private final Context mContext;
	private static volatile NetworkUtil INSTANCE;
	private volatile WifiManager.WifiLock mWifiLock;
	private volatile PowerManager.WakeLock mWakeLock;

	private NetworkUtil(Context context) {
		this.mContext = context.getApplicationContext();
	}

	public static NetworkUtil getInstance(Context context) {
		if (context == null) {
			throw new NullPointerException();
		}
		if (INSTANCE == null) {
			synchronized (NetworkUtil.class) {
				if (INSTANCE == null) {
					INSTANCE = new NetworkUtil(context);
				}
			}
		}
		return INSTANCE;
	}

	public int getNetworkType() {
		ConnectivityManager manager = (ConnectivityManager) this.mContext
				.getSystemService("connectivity");
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();

		return ((networkInfo == null) || (!networkInfo.isConnected())) ? -1
				: networkInfo.getType();
	}

	public InetSocketAddress getAPNProxy() {
		if (getNetworkType() == 0) {
			Uri uri = Uri.parse("content://telephony/carriers/preferapn");
			Cursor cursor = this.mContext.getContentResolver().query(uri, null,
					null, null, null);
			if ((cursor != null) && (cursor.moveToFirst())) {
				String address = cursor.getString(cursor
						.getColumnIndex("proxy"));
				String port = cursor.getString(cursor.getColumnIndex("port"));
				if ((address != null) && (address.trim().length() > 0))
					return new InetSocketAddress(address, Integer.getInteger(
							port, 80).intValue());
			}
			if (cursor != null) {
				cursor.close();
			}
		}

		return null;
	}

	public void acquireWakeLock() {
		acquireWakeLock(true);
	}

	public void acquireWakeLock(boolean isReferenceCounted) {
		synchronized (this) {
			if (this.mWakeLock == null) {
				WifiManager wifiManager = (WifiManager) this.mContext
						.getSystemService("wifi");
				this.mWifiLock = wifiManager.createWifiLock("wifiLock");

				PowerManager powerManager = (PowerManager) this.mContext
						.getSystemService("power");
				this.mWakeLock = powerManager.newWakeLock(1, "wakelock");
			}

		}

		this.mWifiLock.setReferenceCounted(isReferenceCounted);
		this.mWakeLock.setReferenceCounted(isReferenceCounted);

		this.mWifiLock.acquire();
		this.mWakeLock.acquire();
	}

	public void releaseWakeLock() {
		if (this.mWifiLock != null) {
			this.mWifiLock.release();
		}

		if (this.mWakeLock != null)
			this.mWakeLock.release();
	}
}