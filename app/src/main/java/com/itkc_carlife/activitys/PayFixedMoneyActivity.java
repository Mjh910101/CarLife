package com.itkc_carlife.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.box.ServiceObj;
import com.itkc_carlife.box.handler.ServiceObjHandler;
import com.itkc_carlife.box.handler.UserObjHandler;
import com.itkc_carlife.handlers.JsonHandle;
import com.itkc_carlife.handlers.MessageHandler;
import com.itkc_carlife.http.HttpUtilsBox;
import com.itkc_carlife.http.UrlHandler;
import com.itkc_carlife.tool.Passageway;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONObject;

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
public class PayFixedMoneyActivity extends BaseActivity {

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.payFixed_balanceText)
    private TextView balanceText;
    @ViewInject(R.id.payFixed_progress)
    private ProgressBar progress;
    @ViewInject(R.id.payFixed_confirm)
    private TextView confirm;
    @ViewInject(R.id.payFixed_remarkText)
    private TextView remarkText;

    private Bundle mBundle;
    private String type = "";
    private ServiceObj mServiceObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pay_fixed_money);
        context = this;
        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick({R.id.title_backBtn, R.id.payFixed_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.payFixed_confirm:
                confirm();
                break;
        }
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("付    款");
//        balanceText.setText("￥" + UserObjHandler.getUserBalance(context));
        UserObjHandler.setUserBalance(context, balanceText);
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            type = mBundle.getString(ActionDataListActivity.TYPE);
            downloadData(mBundle.getString(PayInputMoneyActivity.PAY_ID_KEY));
        } else {
            finish();
        }

    }

    public void setMessage(ServiceObj obj) {
        remarkText.setText( obj.getRemark());
    }

    private void downloadData(String id) {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getServiceScan() + "?qrcode=" + id;
        RequestParams params = HttpUtilsBox.getRequestParams(context);

        HttpUtilsBox.getHttpUtil().send(HttpMethod.GET, url, params,
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
                                    mServiceObj = ServiceObjHandler.getServiceObj(resultJson);
                                    setMessage(mServiceObj);
                                }
                            }
                        }
                    }

                });
    }

    private void confirm() {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getUserPayAction();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("type", type);
        params.addBodyParameter("total", "10.00");
        params.addBodyParameter("servicesid", mServiceObj.getObjectId());


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
                                checkBalance();
                            } else {
                                jumpPayResultsActivity(false, JsonHandle.getJSON(json, "result"));
                            }
                        }
                    }

                });
    }

    private void checkBalance() {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getUserBalance() + "?sessiontoken=" + UserObjHandler.getSessionToken(context);
        RequestParams params = HttpUtilsBox.getRequestParams(context);

        HttpUtilsBox.getHttpUtil().send(HttpMethod.GET, url, params,
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
                                    UserObjHandler.saveUserBalance(context, JsonHandle.getDouble(resultJson, "balance"));
                                    jumpPayResultsActivity(true, null);
                                }
                            }
                        }
                    }

                });
    }

    private void jumpPayResultsActivity(boolean b, JSONObject json) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(PayResultsActivity.KEY, b);
        if (json != null) {
            bundle.putString(PayResultsActivity.MESSAGE, JsonHandle.getString(json, "message"));
        }
        Passageway.jumpActivity(context, PayResultsActivity.class, bundle);
//        if (b) {
        finish();
//        }
    }

}
