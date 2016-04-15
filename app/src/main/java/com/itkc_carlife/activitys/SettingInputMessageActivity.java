package com.itkc_carlife.activitys;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.itkc_carlife.R;
import com.itkc_carlife.box.handler.UserObjHandler;
import com.itkc_carlife.handlers.DateHandle;
import com.itkc_carlife.handlers.JsonHandle;
import com.itkc_carlife.handlers.MapHandler;
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

import java.util.Calendar;

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
 * Created by Hua on 16/1/13.
 */
public class SettingInputMessageActivity extends BaseActivity {


    public final static String SRTTLING_ID_KEY = "settling_id";
    public final static String SRTTLING_ORDER_ID_KEY = "settling_order_id";
    public final static String QRCODE_KEY = "qrcode_id";
    public final static String IS_THR_MAIN = "is_the_main";

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.insuranceMessageInput_progress)
    private ProgressBar progress;
    @ViewInject(R.id.insuranceMessageInput_dayInput)
    private TextView dayInput;
    @ViewInject(R.id.insuranceMessageInput_positionInput)
    private TextView positionInput;
    @ViewInject(R.id.insuranceMessageInput_descriptInput)
    private EditText descriptInput;
    @ViewInject(R.id.title_sainningIcon)
    private ImageView sainningIcon;

    private Bundle mBundle;

    private String settlingId, qrCode, address;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_insurance_input_message);
        context = this;
        ViewUtils.inject(this);

        initActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Bundle b = data.getExtras();
            switch (requestCode) {
                case PositionActivity.REQUEST_CODE:
                    setPosition(b);
                    break;

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.title_backBtn, R.id.insuranceMessageInput_nextBtn, R.id.insuranceMessageInput_dayInput,
            R.id.insuranceMessageInput_positionInput, R.id.insuranceMessageInput_nextBtn, R.id.title_sainningIcon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.insuranceMessageInput_nextBtn:
//                jumpInputCarDamaged();
                uploadMessage();
                break;
            case R.id.insuranceMessageInput_dayInput:
//                showDateDialog();
                break;
            case R.id.insuranceMessageInput_positionInput:
                jumpPosition();
                break;
            case R.id.title_sainningIcon:
                jumpQrCodeActivity();
                break;
        }
    }

    private void jumpQrCodeActivity() {
        Bundle b = new Bundle();
        b.putString(QRCodeActivity.QR_CODE, settlingId);
        Passageway.jumpActivity(context, QRCodeActivity.class, b);
    }

    private void jumpPosition() {
        Bundle b = new Bundle();
        b.putBoolean(PositionActivity.IS_ARTICIAL, false);
        Passageway.jumpActivity(context, PositionActivity.class, PositionActivity.REQUEST_CODE, b);
    }

    private void showDateDialog() {
        Calendar c = DateHandle.getToday();
        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dayInput.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, DateHandle.getYear(c), DateHandle.getMonth(c), DateHandle.getDay(c));

        dialog.show();

    }

    private void jumpInputCarDamaged() {
        if (settlingId != null) {
            mBundle.putString(InputCarDamagedActivity.WHERE_KEY, InputCarDamagedActivity.INSURANCE_KEY);
            mBundle.putString(SRTTLING_ID_KEY, settlingId);
            Passageway.jumpActivity(context, InputCarDamagedActivity.class, mBundle);
        }
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("快速理赔");

        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            setSettlingId(mBundle.getString(SRTTLING_ID_KEY));
            if (settlingId == null) {
                qrCode = mBundle.getString(QRCODE_KEY);
                downloadSetingId(qrCode);
            }
            if (mBundle.getBoolean(IS_THR_MAIN)) {
                sainningIcon.setVisibility(View.VISIBLE);
            }
        } else {
            finish();
        }

        Calendar c = DateHandle.getToday();
        dayInput.setText(DateHandle.getYear(c) + "-" + (DateHandle.getMonth(c) + 1) + "-" + DateHandle.getDay(c));
        getMapPosition();
    }

    public void getMapPosition() {
        progress.setVisibility(View.VISIBLE);
        LocationClient locationClient = MapHandler.getAddress(context, new MapHandler.MapListener() {
            @Override
            public void callback(BDLocation location) {
                address = location.getAddrStr();
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                positionInput.setText(address);
                progress.setVisibility(View.GONE);
            }
        });
    }

    public void setPosition(Bundle b) {
        if (b != null) {
            address = b.getString(PositionActivity.ADDRESS_KEY);
            latitude = b.getDouble(PositionActivity.LATITUDE_KEY);
            longitude = b.getDouble(PositionActivity.LONGITUDE_KEY);
            positionInput.setText(address);
        }
    }

    public void setSettlingId(String settlingId) {
        this.settlingId = settlingId;
    }

    private void downloadSetingId(String qrCode) {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getUserSettlingConfirm();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("qrcode", qrCode);

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
                                    setSettlingId(JsonHandle.getString(resultJson, "settlingid"));
                                }
                            } else {
                                MessageHandler.showToast(context, JsonHandle.getString(json, "result"));
                            }
                        }
                    }

                });
    }


    private void uploadMessage() {
        if (isThrough()) {
            upload();
        }
    }

    private void upload() {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getUserSettlingDetail();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("settlingid", settlingId);
        params.addBodyParameter("date", TextHandeler.getText(dayInput));
        params.addBodyParameter("descript", TextHandeler.getText(descriptInput));
        params.addBodyParameter("address", address);
        params.addBodyParameter("latitude", String.valueOf(latitude));
        params.addBodyParameter("longitude", String.valueOf(longitude));

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
                                jumpInputCarDamaged();
                            } else {
                                MessageHandler.showToast(context, JsonHandle.getString(json, "result"));
                            }
                        }
                    }

                });
    }

    public boolean isThrough() {
        if (TextHandeler.getText(dayInput).equals("")) {
            MessageHandler.showToast(context, "请选择事故日期");
            return false;
        }
        if (latitude == 0 || longitude == 0) {
            MessageHandler.showToast(context, "请定位现场");
            return false;
        }
        if (TextHandeler.getText(descriptInput).equals("")) {
            MessageHandler.showToast(context, "请填写事故描述");
            return false;
        }
        return true;
    }
}
