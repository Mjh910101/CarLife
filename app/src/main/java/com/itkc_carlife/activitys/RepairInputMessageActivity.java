package com.itkc_carlife.activitys;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.itkc_carlife.R;
import com.itkc_carlife.box.handler.UserCarInfoObjHandler;
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
 * Created by Hua on 16/1/19.
 */
public class RepairInputMessageActivity extends BaseActivity {

    public final static String REPAIR_ID_KEY = "repair_id";

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.repairMessageInput_carMasterName)
    private TextView carMasterName;
    @ViewInject(R.id.repairMessageInput_carMasterTel)
    private TextView carMasterTel;
    @ViewInject(R.id.repairMessageInput_carNumberInput)
    private TextView carNumberInput;
    @ViewInject(R.id.repairMessageInput_dayInput)
    private TextView dayInput;
    @ViewInject(R.id.repairMessageInput_positionInput)
    private TextView positionInput;
    @ViewInject(R.id.repairMessageInput_descriptInput)
    private EditText descriptInput;
    @ViewInject(R.id.repairMessageInput_maneyInput)
    private EditText maneyInput;
    @ViewInject(R.id.repairMessageInput_expectWorkDayInout)
    private EditText expectWorkDayInout;
    @ViewInject(R.id.repairMessageInput_progress)
    private ProgressBar progress;

    private double latitude, longitude;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_repair_input_message);
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

    @OnClick({R.id.title_backBtn, R.id.repairMessageInput_dayInput, R.id.repairMessageInput_positionInput,
            R.id.repairMessageInput_nextBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
//            case R.id.repairMessageInput_dayInput:
//                showDateDialog();
//                break;
            case R.id.repairMessageInput_positionInput:
                jumpPosition();
                break;
            case R.id.repairMessageInput_nextBtn:
                uploadMessage();
                break;
        }
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("新增抢修单");

        carMasterName.setText(UserObjHandler.getUserName(context));
        carMasterTel.setText(UserObjHandler.getUserMobile(context));
        carNumberInput.setText(UserCarInfoObjHandler.getUserCarNumberForGone(context));

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

    private void jumpPosition() {
        Bundle b = new Bundle();
        b.putBoolean(PositionActivity.IS_ARTICIAL, true);
        if (latitude > 0 && longitude > 0) {
            b.putDouble(PositionActivity.LATITUDE_KEY, latitude);
            b.putDouble(PositionActivity.LONGITUDE_KEY, longitude);
        }
        Passageway.jumpActivity(context, PositionActivity.class, PositionActivity.REQUEST_CODE, b);
    }


    private void jumpInputCarDamaged(JSONObject json) {
        if (json != null) {
            Bundle b = new Bundle();
            b.putString(InputCarDamagedActivity.WHERE_KEY, InputCarDamagedActivity.REPAIR_KEY);
            b.putString(REPAIR_ID_KEY, JsonHandle.getString(json, "objectId"));
            Passageway.jumpActivity(context, InputCarDamagedActivity.class, b);
        }
    }

    private void uploadMessage() {
        if (isThrough()) {
            upload();
        }
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
            MessageHandler.showToast(context, "请填写受损描述");
            return false;
        }
        if (TextHandeler.getText(maneyInput).equals("")) {
            MessageHandler.showToast(context, "请填写期望维修价");
            return false;
        }
        if (TextHandeler.getText(maneyInput).equals("0")) {
            MessageHandler.showToast(context, "期望维修价不能为0");
            return false;
        }
        if (TextHandeler.getText(expectWorkDayInout).equals("")) {
            MessageHandler.showToast(context, "请填写期望工时");
            return false;
        }
        if (TextHandeler.getText(expectWorkDayInout).equals("0")) {
            MessageHandler.showToast(context, "期望工时不能为0");
            return false;
        }
        return true;
    }

    private void upload() {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getUserRepair();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("expect", TextHandeler.getText(maneyInput));
        params.addBodyParameter("expectworkdays", TextHandeler.getText(expectWorkDayInout));
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
                                if (resultJson != null) {
                                    jumpInputCarDamaged(resultJson);
                                }
                            } else {
                                MessageHandler.showToast(context, JsonHandle.getString(json, "result"));
                            }
                        }
                    }

                });
    }


}
