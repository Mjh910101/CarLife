package com.itkc_carlife.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.tool.Passageway;
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
 * Created by Hua on 16/1/25.
 */
public class ResultActivity extends BaseActivity {

    public final static int REQUEST_CODE = 106;
    private final static long SLEEP_TIME = 1500;

    @ViewInject(R.id.result_icon)
    private ImageView resultIcon;
    @ViewInject(R.id.result_statusText)
    private TextView statusText;
    @ViewInject(R.id.result_messageText)
    private TextView messageText;

    private Bundle mBundle;
    private boolean isStatus;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ViewUtils.inject(this);

        initActivity();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            close();
        }
        return false;
    }

    @OnClick({R.id.result_bg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.result_bg:
//                finish();
                break;
        }
    }

    private void initActivity() {

        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            message = mBundle.getString("message");
            isStatus = mBundle.getBoolean("isStatus");
            if (isStatus) {
                setSuccessfulView();
            } else {
                setFailureView(message);
            }
        }
        startRun();

    }

    private void startRun() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, SLEEP_TIME);
    }

    private void setFailureView(String message) {
        resultIcon.setImageResource(R.drawable.failure_icon);
        statusText.setText("保存失败");
        messageText.setVisibility(View.VISIBLE);
        messageText.setText(message);
    }

    private void setSuccessfulView() {
        resultIcon.setImageResource(R.drawable.successful_icon);
        statusText.setText("保存成功");
    }

}
