package com.itkc_carlife.http;

import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class MyHttpUtils extends HttpUtils {

	@Override
	public <T> HttpHandler<T> send(HttpMethod method, String url,
			RequestParams params, RequestCallBack<T> callBack) {
		url = url.replace(" ", "");
		Log.e("url", "URL:    " + url);
		// String newUrl = url;
		// try {
		// newUrl = URLEncoder.encode(url, "utf-8");
		// Log.e("new_url", "new_url:    " + newUrl);
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
		return super.send(method, url, params, callBack);
	}

}
