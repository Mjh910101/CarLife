package com.itkc_carlife.client;

import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.itkc_carlife.activitys.WebActivity;
import com.itkc_carlife.tool.Passageway;

public class MyWebViewClient extends WebViewClient {

    public final static String KEY = "URL";
    public final static String COLOR = "COLOR";

    private Context context;
    private int color;

    public MyWebViewClient(Context context) {
        this.context = context;
    }

    // 重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // view.loadUrl(url);// 如果不需要其他对点击链接事件的处理返回true，否则返回false

        Bundle b = new Bundle();
        b.putString(WebActivity.URL, url);
        b.putString(WebActivity.TITLE, "");
        Passageway.jumpActivity(context, WebActivity.class, b);

        return true;

    }
}
