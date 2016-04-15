package com.itkc_carlife.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.adapters.SettingBriefAdpter;
import com.itkc_carlife.box.SettlingBriefObj;
import com.itkc_carlife.box.handler.SettlingBriefObjHandler;
import com.itkc_carlife.box.handler.UserObjHandler;
import com.itkc_carlife.handlers.JsonHandle;
import com.itkc_carlife.handlers.MessageHandler;
import com.itkc_carlife.http.HttpUtilsBox;
import com.itkc_carlife.http.UrlHandler;
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
 * Created by Hua on 16/2/5.
 */
public class SettlingOrderListActivity extends BaseActivity {

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.repairOmitList_progress)
    private ProgressBar progress;
    @ViewInject(R.id.repairOmitList_dataList)
    private ListView dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_repair_omit_list);
        context = this;
        ViewUtils.inject(this);

        initActivity();
        downloadData();
    }

    @OnClick({R.id.title_backBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
        }
    }


    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("快赔抢修");

    }

    public void setDataList(List<SettlingBriefObj> list) {
        dataList.setAdapter(new SettingBriefAdpter(context, list));
    }

    private void downloadData() {
        progress.setVisibility(View.VISIBLE);

        String url = UrlHandler.getUserSettlingOrder() + "?sessiontoken=" + UserObjHandler.getSessionToken(context) + "&order_status=" + 1;

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
