package com.itkc_carlife.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.box.RepairObj;
import com.itkc_carlife.box.Video;
import com.itkc_carlife.box.handler.RepairObjHandler;
import com.itkc_carlife.box.handler.UserCarInfoObjHandler;
import com.itkc_carlife.box.handler.UserObjHandler;
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

import java.util.ArrayList;

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
 * Created by Hua on 16/1/20.
 */
public class RepairActivity extends BaseActivity {

    public final static String IS_REPAIR = "isRepair";

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.repair_progress)
    private ProgressBar progress;
    @ViewInject(R.id.repair_id)
    private TextView repairIdText;
    @ViewInject(R.id.repair_carMasterName)
    private TextView carMasterName;
    @ViewInject(R.id.repair_carMasterTel)
    private TextView carMasterTel;
    @ViewInject(R.id.repair_carNumber)
    private TextView carNumber;
    @ViewInject(R.id.repair_dayText)
    private TextView dayText;
    @ViewInject(R.id.repair_position)
    private TextView position;
    @ViewInject(R.id.repair_carDamaged)
    private TextView carDamaged;
    @ViewInject(R.id.repair_descript)
    private TextView descript;
    @ViewInject(R.id.repair_expectText)
    private EditText expectText;
    @ViewInject(R.id.repair_expectWorkDayText)
    private TextView expectWorkDayText;

    private Bundle mBundle;
    private String repairId;
    private boolean isRepair;
    private String settlingId, settlingOrdedId;
    private RepairObj mRepairObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_repair);
        context = this;
        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick({R.id.title_backBtn, R.id.repair_videoideo, R.id.repair_position, R.id.repair_carDamaged,
            R.id.repair_uploadBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.repair_videoideo:
                if (isThrough()) {
                    jumpVideoActivity(mRepairObj.getVideo());
                }
                break;
            case R.id.repair_position:
                if (isThrough()) {
                    jumpPositionActivity();
                }
                break;
            case R.id.repair_carDamaged:
                if (isThrough()) {
                    jumpImageGrid();
                }
                break;
            case R.id.repair_uploadBtn:
                if (!isRepair) {
                    upload();
                } else {
                    if (isHaveExpect()) {
                        uploadRepair();
                    }
                }
                break;
        }
    }

    public boolean isHaveExpect() {
        if (TextHandeler.getText(expectText).equals("")) {
            MessageHandler.showToast(context, "请填写期望维修价");
            return false;
        }
        if (TextHandeler.getText(expectText).equals("0")) {
            MessageHandler.showToast(context, "期望维修价不能为0");
            return false;
        }
        if (TextHandeler.getText(expectWorkDayText).equals("")) {
            MessageHandler.showToast(context, "请填写期望工期");
            return false;
        }
        if (TextHandeler.getText(expectWorkDayText).equals("0")) {
            MessageHandler.showToast(context, "期望工期不能为0");
            return false;
        }
        return true;
    }

    private boolean isThrough() {
        return mRepairObj != null;
    }


    private void jumpImageGrid() {
        Bundle b = new Bundle();
        b.putStringArrayList(ImageGridActivity.LIST_KEY, (ArrayList) mRepairObj.getAreaImageList());
        Passageway.jumpActivity(context, ImageGridActivity.class, b);
    }

    private void jumpPositionActivity() {
        Bundle b = new Bundle();
        b.putBoolean(PositionActivity.IS_HAVE, true);
        b.putDouble(PositionActivity.LATITUDE_KEY, mRepairObj.getLatitude());
        b.putDouble(PositionActivity.LONGITUDE_KEY, mRepairObj.getLongitude());
        Passageway.jumpActivity(context, PositionActivity.class, b);
    }

    private void jumpVideoActivity(Video video) {
        Bundle b = new Bundle();
        b.putBoolean(PlayVideoActivity.IS_FILE, false);
        b.putString(PlayVideoActivity.URL, video.getUrl());
        Passageway.jumpActivity(context, PlayVideoActivity.class, b);
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("车辆抢修");

        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            repairId = mBundle.getString(RepairInputMessageActivity.REPAIR_ID_KEY, "");
            settlingId = mBundle.getString(SettingInputMessageActivity.SRTTLING_ID_KEY, "");
            settlingOrdedId = mBundle.getString(SettingInputMessageActivity.SRTTLING_ORDER_ID_KEY, "");
            if (repairId.equals("")) {
                dowmloadDataForSettling(settlingId);
            } else {
                dowmloadData(repairId);
                repairIdText.setText(repairId);
            }
            setUserView();
            isRepair = mBundle.getBoolean(IS_REPAIR, false);
            if (isRepair) {
                setManeyInput();
            } else {
                expectText.setKeyListener(null);
                expectWorkDayText.setKeyListener(null);
            }
        } else {
            finish();
        }

    }

    private void setManeyInput() {
    }

    private void setUserView() {
        carMasterName.setText(UserObjHandler.getUserName(context));
        carMasterTel.setText(UserObjHandler.getUserMobile(context));
        carNumber.setText(UserCarInfoObjHandler.getUserCarNumberForGone(context));
    }

    public void setMessageView(RepairObj obj) {
        repairIdText.setText(obj.getObjectId());
        dayText.setText(obj.getCreatedAt());
        position.setText(obj.getAddress());
        carDamaged.setText(obj.getAreaListSize());
        descript.setText(obj.getDescript());
        expectText.setText(obj.getExpect());
        expectWorkDayText.setText(obj.getExpectworkdaysText());
    }

    private void dowmloadData(String id) {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getRepair() + "?repairid=" + id;

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
                                    mRepairObj = RepairObjHandler.getRepairObj(resultJson);
                                    setMessageView(mRepairObj);
                                }
                            }
                        }
                    }

                });
    }

    private void dowmloadDataForSettling(String id) {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getUserRepairOrderSettling() + "?settlingid=" + id + "&sessiontoken=" + UserObjHandler.getSessionToken(context);

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
                                    mRepairObj = RepairObjHandler.getRepairObj(resultJson);
                                    setMessageView(mRepairObj);
                                }
                            }
                        }
                    }

                });
    }


    private void upload() {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getUserRepairOrder();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("repairid", repairId);
        params.addBodyParameter("order_status", "1");


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
                                Bundle b = new Bundle();
                                b.putString(RepairInputMessageActivity.REPAIR_ID_KEY, repairId);
                                Passageway.jumpActivity(context, WaitingRepairStoreActivity.class, b);
//                                Passageway.jumpActivity(context, RepairStoreListActivity.class, mBundle);
                            }
                        }
                    }

                });
    }


    private void uploadRepair() {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getUserRepairOrderSettling();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("expect", TextHandeler.getText(expectText));
        params.addBodyParameter("expectworkdays", TextHandeler.getText(expectWorkDayText));
        params.addBodyParameter("address", mRepairObj.getAddress());
        params.addBodyParameter("descript", mRepairObj.getDescript());
        params.addBodyParameter("latitude", String.valueOf(mRepairObj.getLatitude()));
        params.addBodyParameter("longitude", String.valueOf(mRepairObj.getLongitude()));
        params.addBodyParameter("settlingid", settlingId);
        params.addBodyParameter("settlingorderid", settlingOrdedId);

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
//                                Bundle b = new Bundle();
//                                b.putString(RepairInputMessageActivity.REPAIR_ID_KEY, mRepairObj.getObjectId());
//                                Passageway.jumpActivity(context, WaitingRepairStoreActivity.class, b);
                                if (resultJson != null) {
                                    repairId = JsonHandle.getString(resultJson, "objectId");
                                    upload();
                                }

                            }
                        }
                    }

                });
    }


}
