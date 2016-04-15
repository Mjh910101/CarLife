package com.itkc_carlife.activitys;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.itkc_carlife.R;
import com.itkc_carlife.box.handler.UserObjHandler;
import com.itkc_carlife.handlers.JsonHandle;
import com.itkc_carlife.handlers.MessageHandler;
import com.itkc_carlife.http.HttpUtilsBox;
import com.itkc_carlife.http.UrlHandler;
import com.itkc_carlife.topup.zhifubao.PayResult;
import com.itkc_carlife.topup.zhifubao.ZhiFuBao;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
 * Created by Hua on 16/1/5.
 */
public class TopUpConfirmActivity extends BaseActivity {

    public final static String TOPUP_MONEY = "money";

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.topUp_confirm_zhifubaoRadio)
    private RadioButton zhifubaoRadio;
    @ViewInject(R.id.topUp_confirm_topUpManey)
    private TextView topUpManeyText;
    @ViewInject(R.id.topUp_confirm_progress)
    private ProgressBar progress;

    private double topUpManey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_topup_confirm);
        context = this;
        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick({R.id.title_backBtn, R.id.topUp_confirm_zhifubaoLayout, R.id.topUp_confirm_topUpBtm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.topUp_confirm_zhifubaoLayout:
                setZhiFuBaoRadio();
                break;
            case R.id.topUp_confirm_topUpBtm:
                topUp();
                break;
        }
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("充值方式");

        Bundle b = getIntent().getExtras();
        if (b != null) {
            topUpManey = b.getDouble(TOPUP_MONEY, 0);
            if (topUpManey <= 0) {
                finish();
            } else {
                topUpManeyText.setText("￥" + topUpManey);
            }
        } else {
            finish();
        }
    }

    public void setZhiFuBaoRadio() {
        Boolean b = zhifubaoRadio.isChecked();
        zhifubaoRadio.setChecked(!b);
    }

    private void topUp() {
        if (zhifubaoRadio.isChecked()) {
            downloadObjectId("alipay");
        }
    }

    private void downloadObjectId(String paytype) {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getUserCharge();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("total", String.valueOf(topUpManey));
        params.addBodyParameter("paytype", paytype);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));

        HttpUtilsBox.getHttpUtil().send(HttpMethod.POST, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception, String msg) {
                        progress.setVisibility(View.GONE);
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.d("", result);
                        progress.setVisibility(View.GONE);

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            int status = JsonHandle.getInt(json, "status");
                            if (status == 1) {
                                JSONObject resultJson = JsonHandle.getJSON(json, "result");
                                if (resultJson != null) {
                                    tipUpToZhiFubao(JsonHandle.getString(resultJson, "objectId"));
                                }
                            }
                        }
                    }

                });
    }

    private void tipUpToZhiFubao(String objectId) {
        String orderInfo = ZhiFuBao.getOrderInfo("测试充值", "测试充值描述", "0.01", objectId);

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = ZhiFuBao.sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + ZhiFuBao.getSignType();

        new Thread(new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(TopUpConfirmActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = ZhiFuBao.SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        }).start();

    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ZhiFuBao.SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        MessageHandler.showToast(context, "支付成功");
                        finish();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            MessageHandler.showToast(context, "支付结果确认中");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            MessageHandler.showToast(context, "支付失败 ：" + resultStatus);

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };
}
