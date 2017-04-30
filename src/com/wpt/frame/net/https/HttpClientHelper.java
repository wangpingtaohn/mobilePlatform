package com.wpt.frame.net.https;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * @author wpt
 */
public class HttpClientHelper {

	private Context context;
	private HttpClient httpClient;
	
	public HttpClientHelper(Context context)
	{
		this.context = context;
		HttpParams params = new BasicHttpParams();

        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 5000);

        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", new EasySSLSocketFactory(), 9443));

        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
		httpClient = new DefaultHttpClient(ccm, params);
	}
	
	public boolean getInternetIsValid()
	{
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) 
		{
			return false;
		} 
		return true;
	}
	
	public void setNetworkType()
	{
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if(info != null && info.isConnected())
		{
			String typeName = info.getTypeName().toLowerCase();
			if (typeName.equals("mobile")) {
				String proxyHost = android.net.Proxy.getDefaultHost();
				if (proxyHost != null) {
					HttpHost proxy = new HttpHost(android.net.Proxy.getDefaultHost(),
							android.net.Proxy.getDefaultPort());
					httpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY,
							proxy);
				}
			}
		}
	}

	/**
	 * @param url 发送请求的URL
	 * @return 服务器响应字符串
	 * @throws Exception
	 */
	public String getRequest(String url) throws Exception {
		
		setNetworkType();
		// 创建HttpGet对象
		HttpGet get = new HttpGet(url);
		// 发送GET请求
		HttpResponse response = httpClient.execute(get);
		// 服务器成功地返回响应
		int status = response.getStatusLine().getStatusCode();
		if (status == 200) {
			// 获取服务端响应字符串
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, HTTP.UTF_8);
			}
			return result;
		}
		return null;
	}
	public InputStream getInputStreamRequest(String url) {
		try{
			// 创建HttpGet对象
			HttpGet get = new HttpGet(url);
			// 发送GET请求
			HttpResponse response = httpClient.execute(get);
			// 服务器成功地返回响应
			int status = response.getStatusLine().getStatusCode();
			if (status == 200) {
				// 获取服务端响应字符串
				return response.getEntity().getContent();
			}
		}catch(Exception e){
			Log.e("HttpClientHelper", "getRequest error", e);
		}
		
		return null;
	}
	/**
	 * @param url
	 *            发送请求的URL
	 * @param rawParams
	 *            请求参数
	 * @return 服务器响应字符串
	 * @throws Exception
	 */
	public String postRequest(String url, Map<String, String> rawParams)
			throws Exception {

		setNetworkType();
		// 创建HttpPost对象
		HttpPost post = new HttpPost(url);
		// 如果传送的参数比较多，可以对传送的参数进行封装
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (String key : rawParams.keySet()) {
			// 封装参数
			params.add(new BasicNameValuePair(key, rawParams.get(key)));
		}
		// 设置请求参数
		post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		// 发送POST请求
		HttpResponse response = httpClient.execute(post);
		// 服务器成功地返回响应
		if (response.getStatusLine().getStatusCode() == 200) {
			// 获取服务端响应字符串
			return EntityUtils.toString(response.getEntity());
		}
		return null;
	}
	
	public boolean downloadFile(String url,String file,Map<String, String> rawParams) throws Exception {

		// 创建HttpPost对象
		HttpPost post = new HttpPost(url);
		// 如果传送的参数比较多，可以对传装的参数进行封封装
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (String key : rawParams.keySet()) {
			// 封装参数
			params.add(new BasicNameValuePair(key, rawParams.get(key)));
		}
		// 设置请求参数
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
		post.setEntity(entity);
		// 发送POST请求
		HttpResponse response = httpClient.execute(post);
		// 服务器成功地返回响应
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream is = response.getEntity().getContent();
			FileOutputStream fos = null;
			try {
				File imgFile = new File(file.substring(0, file.lastIndexOf("/")));
				if (!imgFile.exists()) {
					imgFile.mkdirs();// 创建文件夹
				}
				fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				try {
					fos.flush();
					fos.close();
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;		
	}

}
