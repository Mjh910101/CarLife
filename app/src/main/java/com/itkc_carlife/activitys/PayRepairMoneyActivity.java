package com.itkc_carlife.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.box.RepairStoreObj;
import com.itkc_carlife.box.handler.RepairStoreObjHandler;
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
 * Created by Hua on 16/1/22.
 */
public class PayRepairMoneyActivity extends BaseActivity {

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
    @ViewInject(R.id.payFixed_moneyText)
    private TextView moneyText;
    @ViewInject(R.id.payFixed_remarkText)
    private TextView remarkText;

    private Bundle mBundle;
    private String repairId;
    private RepairStoreObj storeObj;

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
        storeObj = RepairStoreObjHandler.getSaveRepairStoreObj();
        if (mBundle != null || storeObj != null) {
            repairId = mBundle.getString(RepairInputMessageActivity.REPAIR_ID_KEY);
            Log.e("", "repairId : " + repairId);
            setMessage(storeObj);
        } else {
            finish();
        }

    }

    public void setMessage(RepairStoreObj obj) {
        remarkText.setText(obj.getName());
        moneyText.setText(obj.getFee());
    }


    private void confirm() {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getUserRepairPay();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("total", storeObj.getFee());
        params.addBodyParameter("repairid", repairId);


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
                                } else {
                                    jumpPayResultsActivity(false, JsonHandle.getJSON(json, "result"));
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

    private void close() {
        Passageway.jumpToActivity(context, RepairDataListActivity.class);
        finish();
    }

}
