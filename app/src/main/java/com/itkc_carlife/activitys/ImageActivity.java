package com.itkc_carlife.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.download.DownloadImageLoader;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import uk.co.senab.photoview.PhotoView;

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
 * Created by Hua on 16/1/15.
 */
public class ImageActivity extends BaseActivity {

    public final static int REQUEST_CODE = 102;
    public final static String PATH = "path";
    public final static String URL = "url";
    public final static String IS_DELETE = "isDelete";
    public final static String IS_ONLINE = "isOnline";

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleBg)
    private RelativeLayout titleBg;
    @ViewInject(R.id.image_imageView)
    private PhotoView imageView;
    @ViewInject(R.id.image_btnBox)
    private LinearLayout btnBox;


    private Bundle mBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image);
        context = this;
        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick({R.id.title_backBtn, R.id.image_backBtn, R.id.image_deleteBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
            case R.id.image_backBtn:
                finish();
                break;
            case R.id.image_deleteBtn:
                deleteClose();
                break;
        }
    }

    private void deleteClose() {
        Intent i = new Intent();
        mBundle.putBoolean(IS_DELETE, true);
        i.putExtras(mBundle);
        setResult(REQUEST_CODE, i);
        finish();
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleBg.setBackgroundResource(R.color.text_black);
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            if (mBundle.getBoolean(IS_ONLINE)) {
                btnBox.setVisibility(View.GONE);
                String url = mBundle.getString(URL);
                DownloadImageLoader.loadImage(imageView, url);
            } else {
                String path = mBundle.getString(PATH);
                DownloadImageLoader.loadImageForFile(imageView, path);
            }
        } else {
            finish();
        }

    }

}
