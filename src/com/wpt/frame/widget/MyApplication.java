package com.wpt.frame.widget;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.wanmei.mobileplatform.R;

public class MyApplication extends Application implements
		UncaughtExceptionHandler {

	private static final String TAG = "MyApplication";

	private Context mContext;

	private Thread.UncaughtExceptionHandler handler;

	private List<Activity> mList;

	@Override
	public void onCreate() {
		super.onCreate();

		mList = new ArrayList<Activity>();

		mContext = getApplicationContext();
		handler = Thread.getDefaultUncaughtExceptionHandler();
		// Thread.setDefaultUncaughtExceptionHandler(this);
	}

	public void addActivity(Activity activity) {
		Log.i("wpt", TAG + "***addActivity_start****");
		if (activity != null) {
			mList.add(activity);
		}
		Log.i("wpt", TAG + "list.size()=" + mList.size());
		Log.i("wpt", TAG + "***addActivity_end****");
	}

	public void removeActivity(Activity activity) {
		Log.i("wpt", TAG + "***removeActivity_start****");
		mList.remove(activity);
		Log.i("wpt", TAG + "***removeActivity_end****");
	}

	public void eixtApp() {
		Log.i("wpt", TAG + "***eixtApp_start****");
		for (int i = 0; i < mList.size(); i++) {
			if (mList.get(i) != null) {
				mList.get(i).finish();
			}
		}
		Log.i("wpt", TAG + "***eixtApp_end****");
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handlerExcepition(ex) && handler != null) {
			handler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// ex.getCause().toString();
			Log.i("wpt", TAG + "***ex=" + ex.getCause().toString());
			eixtApp();
		}
	}

	private boolean handlerExcepition(Throwable ex) {
		if (ex == null) {
			return false;
		}
		new Thread() {
			public void run() {
				Looper.prepare();
				CustomToast
						.showToast(mContext, R.string.str_app_exception_exit);
				Looper.loop();
			};
		}.start();
		return true;
	}

}
