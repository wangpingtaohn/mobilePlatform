package com.wpt.frame.util;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

/**
 * Created with IntelliJ IDEA.
 * User: TangCan
 * Date: 13-2-18
 * Time: 下午2:32
 */
public class WebViewUtil {

    public static void clearWebViewCookies(Context ctx) {
        CookieSyncManager.createInstance(ctx);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }
}
