package com.itkc_carlife.activitys;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.box.handler.UserObjHandler;
import com.itkc_carlife.handlers.ColorHandle;
import com.itkc_carlife.handlers.TextHandeler;
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
 * Created by Hua on 16/1/5.
 */
public class TopUpMoneyActivity extends BaseActivity {

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.topUp_moneyInput)
    private EditText moneyInput;
    @ViewInject(R.id.topUp_balanceText)
    private TextView balanceText;
    @ViewInject(R.id.topUp_50Btn)
    private TextView moneyFor50Btn;
    @ViewInject(R.id.topUp_100Btn)
    private TextView moneyFor100Btn;
    @ViewInject(R.id.topUp_300Btn)
    private TextView moneyFor300Btn;
    @ViewInject(R.id.topUp_500Btn)
    private TextView moneyFor500Btn;
    @ViewInject(R.id.topUp_1000Btn)
    private TextView moneyFor1000Btn;
    @ViewInject(R.id.topUp_3000Btn)
    private TextView moneyFor3000Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_topup_money);
        context = this;
        ViewUtils.inject(this);

        initActivity();
        addTextChangedListener();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        UserObjHandler.setUserBalance(context, balanceText);
    }


    @OnClick({R.id.title_backBtn, R.id.topUp_confirm, R.id.topUp_agreement})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.topUp_confirm:
                onConfirmBtn();
                break;
            case R.id.topUp_agreement:
                Bundle b = new Bundle();
                b.putString(WebActivity.URL, "http://dev.carlive.avosapps.com/static/cost.html");
                b.putString(WebActivity.TITLE, "充值协议");
                Passageway.jumpActivity(context, WebActivity.class, b);
                break;
        }
    }

    @OnClick({R.id.topUp_50Btn, R.id.topUp_100Btn, R.id.topUp_300Btn,
            R.id.topUp_500Btn, R.id.topUp_1000Btn, R.id.topUp_3000Btn})
    public void onMoneyBtn(View view) {
        initMoneyBtn();
        switch (view.getId()) {
            case R.id.topUp_50Btn:
                setMoneyInout(50);
                moneyFor50Btn.setTextColor(ColorHandle.getColorForID(context, R.color.text_red));
                moneyFor50Btn.setBackgroundResource(R.drawable.red_box);
                break;
            case R.id.topUp_100Btn:
                setMoneyInout(100);
                moneyFor100Btn.setTextColor(ColorHandle.getColorForID(context, R.color.text_red));
                moneyFor100Btn.setBackgroundResource(R.drawable.red_box);
                break;
            case R.id.topUp_300Btn:
                setMoneyInout(300);
                moneyFor300Btn.setTextColor(ColorHandle.getColorForID(context, R.color.text_red));
                moneyFor300Btn.setBackgroundResource(R.drawable.red_box);
                break;
            case R.id.topUp_500Btn:
                setMoneyInout(500);
                moneyFor500Btn.setTextColor(ColorHandle.getColorForID(context, R.color.text_red));
                moneyFor500Btn.setBackgroundResource(R.drawable.red_box);
                break;
            case R.id.topUp_1000Btn:
                setMoneyInout(1000);
                moneyFor1000Btn.setTextColor(ColorHandle.getColorForID(context, R.color.text_red));
                moneyFor1000Btn.setBackgroundResource(R.drawable.red_box);
                break;
            case R.id.topUp_3000Btn:
                setMoneyInout(3000);
                moneyFor3000Btn.setTextColor(ColorHandle.getColorForID(context, R.color.text_red));
                moneyFor3000Btn.setBackgroundResource(R.drawable.red_box);
                break;
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

            }
        });
    }

    private void initMoneyBtn() {
        moneyFor50Btn.setTextColor(ColorHandle.getColorForID(context, R.color.text_black));
        moneyFor50Btn.setBackgroundResource(R.drawable.gray_box);
        moneyFor100Btn.setTextColor(ColorHandle.getColorForID(context, R.color.text_black));
        moneyFor100Btn.setBackgroundResource(R.drawable.gray_box);
        moneyFor300Btn.setTextColor(ColorHandle.getColorForID(context, R.color.text_black));
        moneyFor300Btn.setBackgroundResource(R.drawable.gray_box);
        moneyFor500Btn.setTextColor(ColorHandle.getColorForID(context, R.color.text_black));
        moneyFor500Btn.setBackgroundResource(R.drawable.gray_box);
        moneyFor1000Btn.setTextColor(ColorHandle.getColorForID(context, R.color.text_black));
        moneyFor1000Btn.setBackgroundResource(R.drawable.gray_box);
        moneyFor3000Btn.setTextColor(ColorHandle.getColorForID(context, R.color.text_black));
        moneyFor3000Btn.setBackgroundResource(R.drawable.gray_box);
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("会员充值");

//        balanceText.setText("￥" + UserObjHandler.getUserBalance(context));
        UserObjHandler.setUserBalance(context, balanceText);
    }

    private void onConfirmBtn() {
        Bundle b = new Bundle();
        b.putDouble(TopUpConfirmActivity.TOPUP_MONEY, getTopupMoney());
        Passageway.jumpActivity(context, TopUpConfirmActivity.class, b);
    }

    public void setMoneyInout(int m) {
        moneyInput.setText(String.valueOf(m));
    }

    public double getTopupMoney() {
        try {
            return Double.valueOf(TextHandeler.getText(moneyInput));
        } catch (Exception e) {
            return 0;
        }
    }
}
