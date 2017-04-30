package com.wpt.frame.net.volley;

import java.io.UnsupportedEncodingException;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

public abstract class BaseRequest<T> extends Request<T> {

	private Listener<T> mListener;

	public BaseRequest(int method, String url, Listener<T> listener,
			ErrorListener errorListener) {
		super(method, url, errorListener);
		this.mListener = listener;
	}

	@Override
	protected void deliverResponse(T arg0) {

	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse arg0) {
		try {
			String jsonStr = new String(arg0.data,
					HttpHeaderParser.parseCharset(arg0.headers));
			T result = parseNetworkResponseDelegate(jsonStr);
			mListener.onResponse(result);
			return Response.success(result,
					HttpHeaderParser.parseCacheHeaders(arg0));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected abstract T parseNetworkResponseDelegate(String jsonString);
}
