package com.itkc_carlife.activitys;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.box.ServiceObj;
import com.itkc_carlife.box.handler.ServiceObjHandler;
import com.itkc_carlife.box.handler.UserObjHandler;
import com.itkc_carlife.handlers.ColorHandle;
import com.itkc_carlife.handlers.JsonHandle;
import com.itkc_carlife.handlers.MessageHandler;
import com.itkc_carlife.handlers.TextHandeler;
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
public class PayInputMoneyActivity extends BaseActivity {

    public final static String PAY_ID_KEY = "PAY_ID_KEY";

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.payInput_balanceText)
    private TextView balanceText;
    @ViewInject(R.id.payInput_50Btn)
    private TextView monty50Btn;
    @ViewInject(R.id.payInput_100Btn)
    private TextView monty100Btn;
    @ViewInject(R.id.payInput_300Btn)
    private TextView monty300Btn;
    @ViewInject(R.id.payInput_500Btn)
    private TextView monty500Btn;
    @ViewInject(R.id.payInput_1000Btn)
    private TextView monty1000Btn;
    @ViewInject(R.id.payInput_3000Btn)
    private TextView monty3000Btn;
    @ViewInject(R.id.payInput_moneyInput)
    private EditText moneyInput;
    @ViewInject(R.id.payInput_progress)
    private ProgressBar progress;
    @ViewInject(R.id.payInput_confirm)
    private TextView confirm;
    @ViewInject(R.id.payInput_remarkText)
    private TextView remarkText;
    @ViewInject(R.id.payInput_chooseBox)
    private LinearLayout chooseBox;

    private Bundle mBundle;
    private String type = "";
    private ServiceObj mServiceObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pay_input_money);
        context = this;
        ViewUtils.inject(this);

        initActivity();
        addTextChangedListener();
    }

    @OnClick({R.id.title_backBtn, R.id.payInput_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.payInput_confirm:
                if (isThrough()) {
                    confirm();
                }
                break;
        }
    }

    @OnClick({R.id.payInput_50Btn, R.id.payInput_100Btn, R.id.payInput_300Btn, R.id.payInput_500Btn, R.id.payInput_1000Btn, R.id.payInput_3000Btn})
    public void onMontyBtn(View view) {
        initMontyBtn();
        switch (view.getId()) {
            case R.id.payInput_50Btn:
                onMoneyBtn(monty50Btn, 50);
                break;
            case R.id.payInput_100Btn:
                onMoneyBtn(monty100Btn, 100);
                break;
            case R.id.payInput_300Btn:
                onMoneyBtn(monty300Btn, 300);
                break;
            case R.id.payInput_500Btn:
                onMoneyBtn(monty500Btn, 500);
                break;
            case R.id.payInput_1000Btn:
                onMoneyBtn(monty1000Btn, 1000);
                break;
            case R.id.payInput_3000Btn:
                onMoneyBtn(monty3000Btn, 3000);
                break;
        }
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("付    款");
        UserObjHandler.setUserBalance(context, balanceText);
//        balanceText.setText("￥" + UserObjHandler.getUserBalance(context));
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            type = mBundle.getString(ActionDataListActivity.TYPE);
            if (!type.equals(ServiceObj.HIGHWAY)) {
                chooseBox.setVisibility(View.VISIBLE);
            } else {
                chooseBox.setVisibility(View.GONE);
            }
            downloadData(mBundle.getString(PAY_ID_KEY));
        } else {
            finish();
        }

        initMontyBtn();
    }

    public void setMessage(ServiceObj obj) {
        if (obj.getServiceType().equals(ServiceObj.GAS)) {
            String str;
            try {
                String[] s = obj.getAddress().split(" ");
                str = obj.getRemark() + s[1] + "\n" + obj.getName() + s[0];
            } catch (Exception e) {
                str = obj.getRemark() + "\n" + obj.getName() + obj.getAddress();
            }
            remarkText.setText(str);
        } else {
            remarkText.setText(obj.getName() + obj.getAddress() + obj.getRemark());
        }
    }

    private void addTextChangedListener() {
        moneyInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        moneyInput.setText(s);
                        moneyInput.setSelection(s.length());
                    }
                }

                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    moneyInput.setText(s);
                    moneyInput.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        moneyInput.setText(s.subSequence(0, 1));
                        moneyInput.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                confirm.setText("¥" + TextHandeler.getMoneyText(moneyInput) + "元,确定支付");
            }
        });
    }

    private void initMontyBtn() {
        monty50Btn.setTextColor(ColorHandle.getColorForID(context, R.color.text_black));
        monty50Btn.setBackgroundResource(R.drawable.gray_box);
        monty100Btn.setTextColor(ColorHandle.getColorForID(context, R.color.text_black));
        monty100Btn.setBackgroundResource(R.drawable.gray_box);
        monty300Btn.setTextColor(ColorHandle.getColorForID(context, R.color.text_black));
        monty300Btn.setBackgroundResource(R.drawable.gray_box);
        monty500Btn.setTextColor(ColorHandle.getColorForID(context, R.color.text_black));
        monty500Btn.setBackgroundResource(R.drawable.gray_box);
        monty1000Btn.setTextColor(ColorHandle.getColorForID(context, R.color.text_black));
        monty1000Btn.setBackgroundResource(R.drawable.gray_box);
        monty3000Btn.setTextColor(ColorHandle.getColorForID(context, R.color.text_black));
        monty3000Btn.setBackgroundResource(R.drawable.gray_box);
    }

    private void onMoneyBtn(TextView view, double m) {
        view.setTextColor(ColorHandle.getColorForID(context, R.color.text_red));
        view.setBackgroundResource(R.drawable.red_box);

        String mText = String.valueOf(m);
        moneyInput.setText(mText);
        moneyInput.setSelection(mText.length());
    }

    private String getMoney() {
        String m = TextHandeler.getText(moneyInput);
        Log.e("", m.indexOf(".") + "+" + m.length());
        if (m.indexOf(".") != -1 && m.indexOf(".") == m.length() - 1) {
            return m.substring(0, m.length() - 1);
        }
        return m;
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

    private boolean isThrough() {
        String m = getMoney();
        if (m.equals("") || m.equals("0")) {
            MessageHandler.showToast(context, "请输入金额");
            return false;
        }
        return true;
    }

    private void confirm() {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getUserPayAction();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("type", type);
        params.addBodyParameter("total", getMoney());
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
