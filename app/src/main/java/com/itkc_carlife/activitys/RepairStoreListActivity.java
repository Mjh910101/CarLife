package com.itkc_carlife.activitys;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.adapters.RepairStoreAadpter;
import com.itkc_carlife.box.RepairStoreObj;
import com.itkc_carlife.box.handler.RepairStoreObjHandler;
import com.itkc_carlife.box.handler.UserObjHandler;
import com.itkc_carlife.handlers.JsonHandle;
import com.itkc_carlife.handlers.MessageHandler;
import com.itkc_carlife.http.HttpUtilsBox;
import com.itkc_carlife.http.UrlHandler;
import com.itkc_carlife.tool.Passageway;
import com.itkc_carlife.views.InsideListView;
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
 * Created by Hua on 16/1/20.
 */
public class RepairStoreListActivity extends BaseActivity {

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.repairStoreList_progress)
    private ProgressBar progress;
    @ViewInject(R.id.repairStoreList_dataList)
    private ListView dataList;
    @ViewInject(R.id.repairStoreList_swipeRefresh)
    private SwipeRefreshLayout swipeRefresh;

    private Bundle mBundle;
    private String repairId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_repair_store_list);
        context = this;
        ViewUtils.inject(this);

        initActivity();
        setOnRefreshListener();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            close();
        }
        return false;
    }

    @OnClick({R.id.title_backBtn, R.id.repairStoreList_againBtn, R.id.repairStoreList_cancelBtn,
            R.id.repairStoreList_confirmBtn, R.id.repairStoreList_cancelBtn02})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.repairStoreList_confirmBtn:
            case R.id.title_backBtn:
                close();
                break;
            case R.id.repairStoreList_againBtn:
                againUpload();
                break;
            case R.id.repairStoreList_cancelBtn:
                break;
            case R.id.repairStoreList_cancelBtn02:
                cancelRepair();
                break;
        }
    }


    private void close() {
        Passageway.jumpToActivity(context, RepairDataListActivity.class);
        finish();
    }

    private void setOnRefreshListener() {
        swipeRefresh.setColorScheme(R.color.holo_blue_bright,
                R.color.holo_green_light, R.color.holo_orange_light,
                R.color.holo_red_light);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                againDownload(false);
            }
        });
    }

    private void closeSwipeRefresh() {
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }

    private void initActivity() {
//        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("车辆抢修");

        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            repairId = mBundle.getString(RepairInputMessageActivity.REPAIR_ID_KEY);
            againDownload(true);
        } else {
            finish();
        }
//        repairId = "569f448d8ac2470054e4562b";
    }


    public void setDataList(List<RepairStoreObj> list) {
        closeSwipeRefresh();
        dataList.setAdapter(new RepairStoreAadpter(context, list, repairId));
    }

    private void againDownload(boolean b) {
        if (b) {
            progress.setVisibility(View.VISIBLE);
        }
        String url = UrlHandler.getUserRepairGrab() + "?repairid=" + repairId + "&sessiontoken=" + UserObjHandler.getSessionToken(context);

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
                                    JSONArray array = JsonHandle.getArray(resultJson, "store");
                                    if (array != null) {
                                        List<RepairStoreObj> list = RepairStoreObjHandler.getRepairStoreObjList(array);
                                        setDataList(list);
                                    }
                                }

                            }
                        }
                    }

                });
    }


    private void cancelRepair() {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getUserRepairOrder();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("repairid", repairId);
        params.addBodyParameter("order_status", "0");

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
                                close();
                            }
                        }
                    }

                });
    }

    private void againUpload() {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getUserRepairOrderRefresh();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("repairid", repairId);
//        params.addBodyParameter("order_status", "1");


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
                                finish();
                            }
                        }
                    }

                });
    }

}
