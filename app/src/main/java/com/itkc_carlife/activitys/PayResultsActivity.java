package com.itkc_carlife.activitys;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itkc_carlife.R;
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
 * Created by Hua on 16/1/12.
 */
public class PayResultsActivity extends BaseActivity {

    public final static String KEY = "pay_result_key";
    public final static String MESSAGE = "pay_message_key";

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.payResult_payResultIcon)
    private ImageView payResultIcon;
    @ViewInject(R.id.payResult_payResultText)
    private TextView payResultText;


    private Bundle mBundle;
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pay_results);
        context = this;
        ViewUtils.inject(this);

        initActivity();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            close();
        }
        return false;
    }

    @OnClick({R.id.title_backBtn, R.id.payResult_payResultText})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.payResult_payResultText:
            case R.id.title_backBtn:
                close();
                break;
        }
    }

    private void close() {
//        Passageway.jumpToActivity(context, ActionDataListActivity.class);
        finish();
    }

    private void initActivity() {
//        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("付    款");
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            if (mBundle.getBoolean(KEY)) {
                setSuccessfulView();
            } else {
                setFailureView(mBundle.getString(MESSAGE, "支付失败"));
            }
        } else {
            finish();
        }

    }

    private void setFailureView(String msg) {
        payResultIcon.setImageResource(R.drawable.pay_failure_icon);
        payResultText.setBackgroundResource(R.color.red);
        payResultText.setText(msg + "，知道了!");
    }

    private void setSuccessfulView() {
        payResultIcon.setImageResource(R.drawable.pay_successfu_icon);
        payResultText.setBackgroundResource(R.color.title_bg);
        payResultText.setText("支付成功");
    }

}
