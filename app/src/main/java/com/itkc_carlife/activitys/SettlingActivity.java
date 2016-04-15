package com.itkc_carlife.activitys;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.box.SettlingBriefObj;
import com.itkc_carlife.box.SettlingObj;
import com.itkc_carlife.box.handler.SettlingObjHandler;
import com.itkc_carlife.box.handler.UserObjHandler;
import com.itkc_carlife.handlers.ColorHandle;
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
 * Created by Hua on 16/1/15.
 */
public class SettlingActivity extends BaseActivity {

    public final static String IS_DATA = "isData";

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.title_sainningIcon)
    private ImageView sainningIcon;
    @ViewInject(R.id.insurance_progress)
    private ProgressBar progress;
    @ViewInject(R.id.insurance_majorCarInfoBox)
    private LinearLayout majorCarInfoBox;
    @ViewInject(R.id.insurance_majorCarNumber)
    private TextView majorCarNumber;
    @ViewInject(R.id.insurance_majorEngineNumber)
    private TextView majorEngineNumber;
    @ViewInject(R.id.insurance_majorFrameNumber)
    private TextView majorFrameNumber;
    @ViewInject(R.id.insurance_majorAddressBox)
    private RelativeLayout majorAddressBox;
    @ViewInject(R.id.insurance_majorAddress)
    private TextView majorAddress;
    @ViewInject(R.id.insurance_majorCarArea)
    private TextView majorCarArea;
    @ViewInject(R.id.insurance_majorVideoBox)
    private LinearLayout majorVideoBox;
    @ViewInject(R.id.insurance_majorDescriptBox)
    private LinearLayout majorDescriptBox;
    @ViewInject(R.id.insurance_majorDescript)
    private TextView majorDescript;
    @ViewInject(R.id.insurance_secondaryCarInfoBox)
    private LinearLayout secondaryCarInfoBox;
    @ViewInject(R.id.insurance_secondaryCarNumber)
    private TextView secondaryCarNumber;
    @ViewInject(R.id.insurance_secondaryEngineNumber)
    private TextView secondaryEngineNumber;
    @ViewInject(R.id.insurance_secondaryFrameNumber)
    private TextView secondaryFrameNumber;
    @ViewInject(R.id.insurance_secondaryAddressBox)
    private RelativeLayout secondaryAddressBox;
    @ViewInject(R.id.insurance_secondaryAddress)
    private TextView secondaryAddress;
    @ViewInject(R.id.insurance_secondaryCarArea)
    private TextView secondaryCarArea;
    @ViewInject(R.id.insurance_secondaryVideoBox)
    private LinearLayout secondaryVideoBox;
    @ViewInject(R.id.insurance_secondaryDescriptBox)
    private LinearLayout secondaryDescriptBox;
    @ViewInject(R.id.insurance_secondaryDescript)
    private TextView secondaryDescript;
    @ViewInject(R.id.insurance_id)
    private TextView settlingIdText;
    @ViewInject(R.id.insurance_dayText)
    private TextView dayText;
    @ViewInject(R.id.insurance_messageText)
    private TextView messageText;
    @ViewInject(R.id.image_btnBox)
    private LinearLayout btnBox;
    @ViewInject(R.id.insurance_backBtn)
    private TextView backBtn2;
    @ViewInject(R.id.insurance_swipeRefresh)
    private SwipeRefreshLayout swipeRefresh;
    @ViewInject(R.id.insurance_setlingMessage)
    private LinearLayout setlingMessageText;


    private Bundle mBundle;
    private String settlingId;
    private boolean isBack, isDate;

    private SettlingObj majorSettling;
    private SettlingObj secondarySettling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_insurance);
        context = this;
        ViewUtils.inject(this);

        initActivity();
        setOnRefreshListener();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (isBack) {
                close();
            } else {
                finish();
            }
        }
        return false;
    }

    @OnClick({R.id.title_backBtn, R.id.insurance_majorVideoBox, R.id.insurance_secondaryVideoBox,
            R.id.insurance_majorAddress, R.id.insurance_secondaryAddress, R.id.insurance_carArea,
            R.id.insurance_notInsuranceBtn, R.id.insurance_insuranceBtn, R.id.title_sainningIcon,
            R.id.insurance_backBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.insurance_backBtn:
                close();
                break;
            case R.id.insurance_majorVideoBox:
                jumpVideoActivity(majorSettling);
                break;
            case R.id.insurance_secondaryVideoBox:
                jumpVideoActivity(secondarySettling);
                break;
            case R.id.insurance_majorAddress:
                jumpPositionActivity(majorSettling);
                break;
            case R.id.insurance_secondaryAddress:
                jumpPositionActivity(secondarySettling);
                break;
            case R.id.insurance_carArea:
                jumpCarAreActivity();
                break;
            case R.id.insurance_notInsuranceBtn:
                if (isComplete()) {
                    upload("0");
                }
                break;
            case R.id.insurance_insuranceBtn:
                if (isComplete()) {
                    upload("1");
                }
                break;
            case R.id.title_sainningIcon:
                jumpQrCodeActivity();
                break;
        }
    }

    private void setOnRefreshListener() {
        swipeRefresh.setColorScheme(R.color.holo_blue_bright,
                R.color.holo_green_light, R.color.holo_orange_light,
                R.color.holo_red_light);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                dowmloadData(settlingId, false);
            }
        });
    }

    private void close() {
        Passageway.jumpToActivity(context, SettingDataListActivity.class);
        finish();
    }

    private void jumpQrCodeActivity() {
        Bundle b = new Bundle();
        b.putString(QRCodeActivity.QR_CODE, settlingId);
        Passageway.jumpActivity(context, QRCodeActivity.class, b);
    }

    private void jumpCarAreActivity() {
        SettlingObjHandler.setSettlingObjs(majorSettling, secondarySettling);
        Passageway.jumpActivity(context, CarAreaActivity.class);
    }

    private void jumpPositionActivity(SettlingObj obj) {
        if (obj == null) {
            return;
        }
        if (obj.getDetail() == null) {
            return;
        }
        Bundle b = new Bundle();
        b.putBoolean(PositionActivity.IS_HAVE, true);
        b.putDouble(PositionActivity.LATITUDE_KEY, obj.getDetail().getLatitude());
        b.putDouble(PositionActivity.LONGITUDE_KEY, obj.getDetail().getLongitude());
        Passageway.jumpActivity(context, PositionActivity.class, b);
    }

    private void jumpVideoActivity(SettlingObj obj) {
        if (obj == null) {
            return;
        }
        if (obj.getDetail() == null) {
            return;
        }
        Bundle b = new Bundle();
        b.putBoolean(PlayVideoActivity.IS_FILE, false);
        b.putString(PlayVideoActivity.URL, obj.getVideo().getUrl());
        Passageway.jumpActivity(context, PlayVideoActivity.class, b);
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("事故处理单");

        setMajorView(null);
        setSecondaryView(null);

        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            settlingId = mBundle.getString(SettingInputMessageActivity.SRTTLING_ID_KEY);
//        settlingId = "56986a4a00b00ef384f9d9c8";
            settlingIdText.setText(settlingId);
            isBack = mBundle.getBoolean(SettlingBriefObj.ORDER_STATUS, false);
            isDate = mBundle.getBoolean(IS_DATA, false);
//            if (isBack) {
//                showBackBtnView();
//            }
            dowmloadData(settlingId, true);
            if (mBundle.getBoolean(SettingInputMessageActivity.IS_THR_MAIN, false)) {
                sainningIcon.setVisibility(View.VISIBLE);
            }
        } else {
            finish();
        }

    }

    private void showBackBtnView() {
        isBack = true;
        btnBox.setVisibility(View.GONE);
        backBtn2.setVisibility(View.VISIBLE);
        backBtn.setVisibility(View.GONE);
    }


    public void setMajorView(SettlingObj obj) {
        if (obj == null) {
//            majorCarInfoBox.setVisibility(View.GONE);
//            majorAddressBox.setVisibility(View.GONE);
            majorCarArea.setVisibility(View.GONE);
            majorVideoBox.setVisibility(View.GONE);
//            majorDescriptBox.setVisibility(View.GONE);
        } else {
            majorCarInfoBox.setVisibility(View.VISIBLE);
            majorAddressBox.setVisibility(View.VISIBLE);
            majorCarArea.setVisibility(View.VISIBLE);
            majorVideoBox.setVisibility(View.VISIBLE);
            majorDescriptBox.setVisibility(View.VISIBLE);
            majorCarNumber.setText(obj.getCarInfo().getCar_number());
            majorEngineNumber.setText(obj.getCarInfo().getEngine_number());
            majorFrameNumber.setText(obj.getCarInfo().getFrame_number());
            majorAddress.setText(obj.getDetail().getAddress());
            majorCarArea.setText(obj.getAreaNameList());
            majorDescript.setText(obj.getDetail().getDescript());
        }
    }


    public void setSecondaryView(SettlingObj obj) {
        if (obj == null) {
//            secondaryCarInfoBox.setVisibility(View.GONE);
//            secondaryAddressBox.setVisibility(View.GONE);
            secondaryCarArea.setVisibility(View.GONE);
            secondaryVideoBox.setVisibility(View.GONE);
//            secondaryDescriptBox.setVisibility(View.GONE);
        } else {
            secondaryCarInfoBox.setVisibility(View.VISIBLE);
            secondaryAddressBox.setVisibility(View.VISIBLE);
            secondaryCarArea.setVisibility(View.VISIBLE);
            secondaryVideoBox.setVisibility(View.VISIBLE);
            secondaryDescriptBox.setVisibility(View.VISIBLE);
            secondaryCarNumber.setText(obj.getCarInfo().getCar_number());
            secondaryEngineNumber.setText(obj.getCarInfo().getEngine_number());
            secondaryFrameNumber.setText(obj.getCarInfo().getFrame_number());
            secondaryAddress.setText(obj.getDetail().getAddress());
            secondaryCarArea.setText(obj.getAreaNameList());
            secondaryDescript.setText(obj.getDetail().getDescript());
        }
    }

    private void dowmloadData(String id, boolean isShow) {
        if (isShow) {
            progress.setVisibility(View.VISIBLE);
        }
        String url;
        if (!isDate) {
            url = UrlHandler.getSettling() + "?settlingid=" + id;
        } else {
            url = UrlHandler.getUserSettlingOrderDetail() + "?settlingid=" + id + "&sessiontoken=" + UserObjHandler.getSessionToken(context);
        }
        RequestParams params = HttpUtilsBox.getRequestParams(context);

        HttpUtilsBox.getHttpUtil().send(HttpMethod.GET, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception, String msg) {
                        progress.setVisibility(View.GONE);
                        MessageHandler.showFailure(context);
                        closeSwipeRefresh();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.d("", result);
                        closeSwipeRefresh();
                        progress.setVisibility(View.GONE);

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            int status = JsonHandle.getInt(json, "status");
                            if (status == 1) {
                                JSONObject resultJson = JsonHandle.getJSON(json, "result");
                                if (resultJson != null) {
                                    JSONObject major = JsonHandle.getJSON(resultJson, "major");
                                    if (major != null) {
                                        majorSettling = SettlingObjHandler.getSettlingObj(major);
                                        setMajorView(majorSettling);
                                    }

                                    JSONObject secondary = JsonHandle.getJSON(resultJson, "secondary");
                                    if (secondary != null) {
                                        secondarySettling = SettlingObjHandler.getSettlingObj(secondary);
                                        setSecondaryView(secondarySettling);
                                    }
                                    setMessageText(JsonHandle.getString(resultJson, "message"), JsonHandle.getInt(resultJson, "status"));
                                    setDatText(JsonHandle.getString(resultJson, "createdAt"));
                                    setDisplayID(JsonHandle.getString(resultJson, "displayId"));
                                }
                            }
                        }
                    }

                });
    }

    private void closeSwipeRefresh() {
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }

    private void setDatText(String day) {
//        if (majorSettling != null) {
//            dayText.setText(majorSettling.getDetail().getCreatedAt());
//            return;
//        }
//        if (secondarySettling != null) {
//            dayText.setText(secondarySettling.getDetail().getCreatedAt());
//        }
        dayText.setText(day);
    }

    public void setDisplayID(String displayID) {
        if (displayID != null && !displayID.equals("") && !displayID.equals("null")) {
            settlingIdText.setText(displayID);
        }
    }

    public void setMessageText(JSONObject json) {
        showBackBtnView();
        setMessageText(JsonHandle.getString(json, "message"), JsonHandle.getInt(json, "order_status"));
    }

    public void setMessageText(String msg, int status) {
        messageText.setText(msg);
        switch (status) {
            case 4:
                showBackBtnView();
                messageText.setTextColor(ColorHandle.getColorForID(context, R.color.text_gary_05));
                break;
            case 0:
            case 1:
                messageText.setTextColor(ColorHandle.getColorForID(context, R.color.text_red));
                break;
            default:
                showBackBtnView();
                messageText.setTextColor(ColorHandle.getColorForID(context, R.color.text_red));
                setlingMessageText.setVisibility(View.VISIBLE);
                break;


        }

    }

    public boolean isComplete() {
        if (majorSettling == null || secondarySettling == null) {
            MessageHandler.showToast(context, "未完成操作");
            return false;
        }
        return true;
    }

    private void upload(String order_status) {
        progress.setVisibility(View.VISIBLE);

        String url = UrlHandler.getUserSettlingOrder();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("settlingid", settlingId);
        params.addBodyParameter("order_status", order_status);

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
                                setMessageText(JsonHandle.getJSON(json, "result"));
                            }
                        }
                    }

                });

    }


}
