package com.itkc_carlife.activitys;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.adapters.RepairAdaper;
import com.itkc_carlife.adapters.SettingBriefAdpter;
import com.itkc_carlife.box.RepairObj;
import com.itkc_carlife.box.SettlingBriefObj;
import com.itkc_carlife.box.handler.RepairObjHandler;
import com.itkc_carlife.box.handler.SettlingBriefObjHandler;
import com.itkc_carlife.box.handler.UserCarInfoObjHandler;
import com.itkc_carlife.box.handler.UserClaimsInfoObjHandler;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

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
public class RepairDataListActivity extends BaseActivity {

    private final static int REQUEST_CODE = 121;

    private final static int WAITING = R.id.repairDataList_waitingBtn;
    private final static int DATA = R.id.repairDataList_dateBtn;

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.repairDataList_notThroughLayou)
    private LinearLayout notThroughLayou;
    @ViewInject(R.id.repairDataList_progress)
    private ProgressBar progress;
    @ViewInject(R.id.repairDataList_dataList)
    private ListView dataList;
    @ViewInject(R.id.repairDataList_throughLayou)
    private LinearLayout throughLayou;
    @ViewInject(R.id.repairDataList_waitingText)
    private TextView waitingText;
    @ViewInject(R.id.repairDataList_waitingLine)
    private View waitingLine;
    @ViewInject(R.id.repairDataList_dateText)
    private TextView dateText;
    @ViewInject(R.id.repairDataList_dateLine)
    private View dateLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_repair_data_list);
        context = this;
        ViewUtils.inject(this);

        initActivity();
        onClickType(WAITING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (isThroughLayou()) {
                    jumpInputDataActivity();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @OnClick({R.id.title_backBtn, R.id.insuranceDataList_backBtn, R.id.repairDataList_responsibilityBtn, R.id.repairDataList_addResponsibilityBtn})
    public void onClick(View view) {
        Bundle b = new Bundle();
        switch (view.getId()) {
            case R.id.insuranceDataList_backBtn:
                jumpInputDataActivity();
                break;
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.repairDataList_responsibilityBtn:
                Passageway.jumpActivity(context, SettlingOrderListActivity.class);
                break;
            case R.id.repairDataList_addResponsibilityBtn:
                Passageway.jumpActivity(context, RepairInputMessageActivity.class);
                break;
        }
    }

    @OnClick({R.id.repairDataList_waitingBtn, R.id.repairDataList_dateBtn})
    public void onClickType(View v) {
        onClickType(v.getId());
    }

    private void onClickType(int i) {
        initType();
        switch (i) {
            case WAITING:
                waitingLine.setVisibility(View.VISIBLE);
                waitingText.setTextColor(ColorHandle.getColorForID(context, R.color.title_bg));
                downloadSettlingData();
                break;
            case DATA:
                dateLine.setVisibility(View.VISIBLE);
                dateText.setTextColor(ColorHandle.getColorForID(context, R.color.title_bg));
                downloadData();
                break;
        }
    }

    private void initType() {
        waitingLine.setVisibility(View.GONE);
        dateLine.setVisibility(View.GONE);

        waitingText.setTextColor(ColorHandle.getColorForID(context, R.color.text_black));
        dateText.setTextColor(ColorHandle.getColorForID(context, R.color.text_black));

        dataList.setAdapter(null);
    }

    private void jumpInputDataActivity() {
        if (!UserClaimsInfoObjHandler.isThrough(context)) {
            Passageway.jumpActivity(context, InputClaimsDataActivity.class, REQUEST_CODE);
        } else if (!UserCarInfoObjHandler.isThrough(context)) {
            Passageway.jumpActivity(context, InputCarDataActivity.class, REQUEST_CODE);
        }
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("车辆抢修");
        isThroughLayou();
    }

    private boolean isThroughLayou() {
        if (UserClaimsInfoObjHandler.isThrough(context) && UserCarInfoObjHandler.isThrough(context)) {
            notThroughLayou.setVisibility(View.GONE);
            throughLayou.setVisibility(View.VISIBLE);
            return true;
        } else {
            notThroughLayou.setVisibility(View.VISIBLE);
            throughLayou.setVisibility(View.GONE);
            return false;
        }
    }

    public void setSettlingDataList(List<SettlingBriefObj> list) {
        dataList.setAdapter(new SettingBriefAdpter(context, list));
    }

    public void setDataList(List<RepairObj> list) {
        dataList.setAdapter(new RepairAdaper(context, list));
    }

    private void downloadData() {
        progress.setVisibility(View.VISIBLE);

        String url = UrlHandler.getUserRepairOrder() + "?sessiontoken=" + UserObjHandler.getSessionToken(context);

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
                                JSONArray array = JsonHandle.getArray(json, "result");
                                if (array != null) {
                                    List<RepairObj> list = RepairObjHandler.getRepairList(array);
                                    setDataList(list);
                                }
                            }
                        }
                    }

                });
    }

    private void downloadSettlingData() {
        progress.setVisibility(View.VISIBLE);

        String url = UrlHandler.getUserSettlingOrder() + "?sessiontoken=" + UserObjHandler.getSessionToken(context) + "&order_status=" + 1 + "&type=repair";

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
                                JSONArray array = JsonHandle.getArray(json, "result");
                                if (array != null) {
                                    List<SettlingBriefObj> list = SettlingBriefObjHandler.getSettlingBriefObjList(array);
                                    setSettlingDataList(list);
                                }
                            }
                        }
                    }

                });
    }

}
