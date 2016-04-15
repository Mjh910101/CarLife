package com.itkc_carlife.activitys;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.client.MyWebChromeClient;
import com.itkc_carlife.client.MyWebViewClient;
import com.itkc_carlife.interfaces.CallbackForString;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * *
 * * ┏┓      ┏┓
 * *┏┛┻━━━━━━┛┻┓
 * *┃          ┃
 * *┃          ┃
 * *┃ ┳┛   ┗┳  ┃
 * *┃          ┃
 * *┃    ┻     ┃
 * *┃          ┃
 * *┗━┓      ┏━┛
 * *  ┃      ┃
 * *  ┃      ┃
 * *  ┃      ┗━━━┓
 * *  ┃          ┣┓
 * *  ┃         ┏┛
 * *  ┗┓┓┏━━━┳┓┏┛
 * *   ┃┫┫   ┃┫┫
 * *   ┗┻┛   ┗┻┛
 * Created by Hua on 16/1/11.
 */
public class WebActivity extends BaseActivity {

    public static final String TITLE = "title";
    public static final String URL = "url";

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.web_contentWeb)
    private WebView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        context = this;
        ViewUtils.inject(this);

        initAcitvity();
    }

    @OnClick({R.id.title_backBtn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
        }
    }

    private void initAcitvity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);

        content.getSettings().setJavaScriptEnabled(true);
        content.setWebViewClient(new MyWebViewClient(context));
        content.setWebChromeClient(new MyWebChromeClient(
                new CallbackForString() {

                    @Override
                    public void callback(String result) {
                        titleName.setText(result);
                    }
                }));

        WebSettings settings = content.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        // 设置可以支持缩放
        settings.setSupportZoom(true);
// 设置出现缩放工具
        settings.setBuiltInZoomControls(true);
//设置可在大视野范围内上下左右拖动，并且可以任意比例缩放
        settings.setUseWideViewPort(true);
//设置默认加载的可视范围是大视野范围
        settings.setLoadWithOverviewMode(true);
//自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            titleName.setText(b.getString(TITLE, ""));
            content.loadUrl(b.getString(URL));
        } else {
            finish();
        }

    }

}
