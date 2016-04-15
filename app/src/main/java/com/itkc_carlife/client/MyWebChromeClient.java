package com.itkc_carlife.client;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.itkc_carlife.interfaces.CallbackForString;


public class MyWebChromeClient extends WebChromeClient {

	private CallbackForString callback;

	public MyWebChromeClient(CallbackForString callback) {
		this.callback = callback;
	}

	@Override
	public void onReceivedTitle(WebView view, String title) {
		super.onReceivedTitle(view, title);
		if (callback != null) {
			callback.callback(title);
		}
	}
}
