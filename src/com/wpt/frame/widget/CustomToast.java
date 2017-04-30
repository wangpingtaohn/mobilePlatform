package com.wpt.frame.widget;

import android.content.Context;
import android.widget.Toast;

public class CustomToast {

	public static void showToast(Context context, String msg) {
		Toast toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
		toast.setText(msg);
		toast.show();
	}

	public static void showToast(Context context, int msgId) {
		Toast toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
		toast.setText(msgId);
		toast.show();
	}
	public static void showShortToast(Context context, String msg) {
		Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
		toast.setText(msg);
		toast.show();
	}
	
	public static void showShortToast(Context context, int msgId) {
		Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
		toast.setText(msgId);
		toast.show();
	}
}
