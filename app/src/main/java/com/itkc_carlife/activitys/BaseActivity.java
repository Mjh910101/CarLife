package com.itkc_carlife.activitys;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.avos.avoscloud.AVOSCloud;

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
 * Created by Hua on 15/12/25.
 */
public class BaseActivity extends Activity {

    public Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AVOSCloud.initialize(this, "E4G44AwCMV2DGLdthLdYy3lo-gzGzoHsz", "83Qt7Ol6yK8kcpMm8G4KXiuF");
        context = this;
    }

}
