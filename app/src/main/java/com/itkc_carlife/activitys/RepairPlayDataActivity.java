package com.itkc_carlife.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.box.RepairObj;
import com.itkc_carlife.box.RepairStoreObj;
import com.itkc_carlife.box.Video;
import com.itkc_carlife.box.handler.RepairObjHandler;
import com.itkc_carlife.box.handler.RepairStoreObjHandler;
import com.itkc_carlife.box.handler.UserCarInfoObjHandler;
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
 * Created by Hua on 16/1/21.
 */
public class RepairPlayDataActivity extends BaseActivity {

    private final static int REQUEST_CODE = 126;
    private final static int COMMENT_REQUEST = 166;

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
    @ViewInject(R.id.repair_storeNameText)
    private TextView storeNameText;
    @ViewInject(R.id.repair_storeMasterName)
    private TextView storeMasterName;
    @ViewInject(R.id.repair_storeMasterTel)
    private TextView storeMasterTel;
    @ViewInject(R.id.repair_storeAddressText)
    private TextView storeAddressText;
    @ViewInject(R.id.repair_maneyText)
    private TextView maneyText;
    @ViewInject(R.id.repair_playBtn)
    private TextView playBtn;
    @ViewInject(R.id.repair_statusText)
    private TextView statusText;
    @ViewInject(R.id.repair_commentTopLine)
    private View commentTopLine;
    @ViewInject(R.id.repair_commentBottonLine)
    private View commentBottonLine;
    @ViewInject(R.id.repair_commentBox)
    private LinearLayout commentBox;
    @ViewInject(R.id.repair_commentText)
    private TextView commentText;
    @ViewInject(R.id.repair_daysText)
    private TextView daysText;

    private Bundle mBundle;
    private String repairId;
    private boolean isPlay;
    private RepairObj mRepairObj;
    private RepairStoreObj storeObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_repair_play);
        context = this;
        ViewUtils.inject(this);

        initActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                Passageway.jumpToActivity(context, RepairDataListActivity.class);
                finish();
                break;
            case COMMENT_REQUEST:
                dowmloadData(repairId);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.title_backBtn, R.id.repair_videoideo, R.id.repair_position, R.id.repair_carDamaged,
            R.id.repair_playBtn})
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
            case R.id.repair_playBtn:
                if (isPlay) {
//                    finish();
                    if (mRepairObj.isHaveComment()) {
                        finish();
                    } else {
                        jumpCommentActivity();
                    }
                } else {
                    jumpPlayActivity();
                }
                break;
        }
    }

    private void jumpCommentActivity() {
        Bundle b = new Bundle();
        b.putString("id", mRepairObj.getStoreObj().getObjectId());
        b.putString("repairId", mRepairObj.getObjectId());
        b.putBoolean("isRepair", true);
        Passageway.jumpActivity(context, ServerShopCommentActivity.class, COMMENT_REQUEST, b);
    }

    private void jumpPlayActivity() {
        Passageway.jumpActivity(context, PayRepairMoneyActivity.class, REQUEST_CODE, mBundle);
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
        storeObj = RepairStoreObjHandler.getSaveRepairStoreObj();
        if (mBundle != null || storeObj != null) {
            repairId = mBundle.getString(RepairInputMessageActivity.REPAIR_ID_KEY);
            isPlay = mBundle.getBoolean("isPlay");
            repairIdText.setText(repairId);
            setStoreMessage(storeObj);
            dowmloadData(repairId);
            setUserView();
        } else {
            finish();
        }

    }

    public void setStoreMessage(RepairStoreObj obj) {
        storeNameText.setText(obj.getName());
        storeMasterName.setText(obj.getLinkman());
        storeMasterTel.setText(obj.getContact());
        storeAddressText.setText(obj.getAddress());
        maneyText.setText("￥" + obj.getFee());
        daysText.setText(obj.getDays() + "天");

        if (isPlay) {
            playBtn.setText("评价");
        } else {
            playBtn.setText("付款");
        }


    }

    private void setUserView() {
        carMasterName.setText(UserObjHandler.getUserName(context));
        carMasterTel.setText(UserObjHandler.getUserMobile(context));
        carNumber.setText(UserCarInfoObjHandler.getUserCarNumber(context));
    }

    public void setMessageView(RepairObj obj) {
        dayText.setText(obj.getCreatedAt());
        position.setText(obj.getAddress());
        carDamaged.setText(obj.getAreaListSize());
        descript.setText(obj.getDescript());

        switch (obj.getOrder_status()) {
            case "1":
                statusText.setText("等待中");
                break;
            case "2":
                statusText.setText("未付款");
                break;
            case "3":
                statusText.setText("已付款");
                break;
            default:
                statusText.setText("未发布");
                break;
        }


        if (obj.isHaveComment()) {
            commentTopLine.setVisibility(View.VISIBLE);
            commentBottonLine.setVisibility(View.VISIBLE);
            commentBox.setVisibility(View.VISIBLE);
            commentText.setText(obj.getComment().getContent());
            playBtn.setText("返回");
        } else {
            playBtn.setText("评价");
        }

        if (isPlay) {
            if (obj.isHaveComment()) {
                playBtn.setText("返回");
            } else {
                playBtn.setText("评价");
            }
        } else {
            playBtn.setText("付款");
        }
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
}
