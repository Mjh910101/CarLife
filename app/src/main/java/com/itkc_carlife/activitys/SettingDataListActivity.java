package com.itkc_carlife.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.adapters.SettingAdpter;
import com.itkc_carlife.box.SettlingBriefObj;
import com.itkc_carlife.box.handler.SettlingBriefObjHandler;
import com.itkc_carlife.box.handler.UserCarInfoObjHandler;
import com.itkc_carlife.box.handler.UserClaimsInfoObjHandler;
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
 * Created by Hua on 16/1/13.
 */
public class SettingDataListActivity extends BaseActivity {

    private final static int REQUEST_CODE = 122;

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.insuranceDataList_notThroughLayou)
    private LinearLayout notThroughLayou;
    @ViewInject(R.id.insuranceDataList_throughLayou)
    private LinearLayout throughLayou;
    @ViewInject(R.id.insuranceDataList_progress)
    private ProgressBar progress;
    @ViewInject(R.id.insuranceDataList_dataList)
    private ListView dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_insurance_data_list);
        context = this;
        ViewUtils.inject(this);

        initActivity();
        downloadData();
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

    @OnClick({R.id.title_backBtn, R.id.insuranceDataList_backBtn, R.id.insuranceDataList_responsibilityBtn,
            R.id.insuranceDataList_notResponsibilityBtn})
    public void onClick(View view) {
        Bundle b = new Bundle();
        switch (view.getId()) {
            case R.id.insuranceDataList_backBtn:
                jumpInputDataActivity();
                break;
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.insuranceDataList_responsibilityBtn:
                Passageway.jumpActivity(context, QRCodeActivity.class);
                break;
            case R.id.insuranceDataList_notResponsibilityBtn:
                b.putString(ScanningActivity.TYPE_KEY, ScanningActivity.INSURANCE);
                Passageway.jumpActivity(context, ScanningActivity.class, b);
                break;
        }
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
        titleName.setText("快速理赔");

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

    public void setDataList(List<SettlingBriefObj> list) {
        dataList.setAdapter(new SettingAdpter(context, list, false));
    }

    private void downloadData() {
        progress.setVisibility(View.VISIBLE);

        String url = UrlHandler.getUserSettlingOrder() + "?sessiontoken=" + UserObjHandler.getSessionToken(context);

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
                                    setDataList(list);
                                }
                            }
                        }
                    }

                });
    }

}
