package com.itkc_carlife.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.adapters.ActionAdaper;
import com.itkc_carlife.box.PayObj;
import com.itkc_carlife.box.ServiceObj;
import com.itkc_carlife.box.handler.PayObjHandler;
import com.itkc_carlife.box.handler.UserObjHandler;
import com.itkc_carlife.handlers.JsonHandle;
import com.itkc_carlife.handlers.MessageHandler;
import com.itkc_carlife.http.HttpUtilsBox;
import com.itkc_carlife.http.UrlHandler;
import com.itkc_carlife.tool.Passageway;
import com.itkc_carlife.tool.WinTool;
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
 * Created by Hua on 16/1/11.
 */
public class ActionDataListActivity extends BaseActivity {


    public final static String TYPE = "type";
    public final static int LIMIT = 10;


    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.actionDataList_progtess)
    private ProgressBar progress;
    @ViewInject(R.id.actionDataList_balanceText)
    private TextView balanceText;
    @ViewInject(R.id.actionDataList_typeImage)
    private ImageView typeImage;
    @ViewInject(R.id.actionDataList_dataList)
    private ListView dataList;

    private int page = 1, pages = 1;
    private Bundle mBundle;
    private String type = "";


    private ActionAdaper mActionAdaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_action_data_list);
        context = this;
        ViewUtils.inject(this);

        initActivity();
        setDataListScrollListener();
    }

    @OnClick({R.id.title_backBtn, R.id.actionDataList_typeImage})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.actionDataList_typeImage:
                mBundle.putString(ScanningActivity.TYPE_KEY,ScanningActivity.ACTION);
                Passageway.jumpActivity(context, ScanningActivity.class, mBundle);
//                Passageway.jumpActivity(context, PayInputMoneyActivity.class, mBundle);
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        balanceText.setText("￥" + UserObjHandler.getUserBalance(context));
        UserObjHandler.setUserBalance(context,balanceText);
        restartDataList();
    }

    private void setDataListScrollListener() {
        dataList.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() >= (view.getCount() - 1)) {
                        if (page <= pages) {
                            if (progress.getVisibility() == View.GONE) {
                                downloadDataList();
                            }
                        } else {
                            MessageHandler.showLast(context);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

            }
        });
    }

    private void restartDataList() {
        page = 1;
        pages = 1;
        mActionAdaper = null;
        dataList.setAdapter(null);
        downloadDataList();
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
//        balanceText.setText("￥" + UserObjHandler.getUserBalance(context));
        UserObjHandler.setUserBalance(context,balanceText);
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            type = mBundle.getString(TYPE);
            setTypeView(type);
            downloadDataList();
        } else {
            finish();
        }
    }

    private void downloadDataList() {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getUserPayShow() + "?sessiontoken=" + UserObjHandler.getSessionToken(context) + "&type=" + type + "&limit=" + LIMIT + "&page=" + page;

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
                                    List<PayObj> list = PayObjHandler.getPayObjList(array);
                                    setDataList(list);
                                    page += 1;
                                }
                                pages = JsonHandle.getInt(json, "pages");
                            }
                        }
                    }

                });
    }

    public void setTypeView(String t) {
        double w = WinTool.getWinWidth(context);
        double h = w / 640d * 419d;
        typeImage.setLayoutParams(new RelativeLayout.LayoutParams((int) w, (int) h));
        switch (t) {
            case ServiceObj.GAS:
                titleName.setText("我要加油");
                typeImage.setImageResource(R.drawable.gas_image);
                break;
            case ServiceObj.PARKING:
                titleName.setText("停车收费");
                typeImage.setImageResource(R.drawable.parking_image);
                break;
            case ServiceObj.HIGHWAY:
                titleName.setText("高速缴费");
                typeImage.setImageResource(R.drawable.highway_image);
                break;
        }
    }

    public void setDataList(List<PayObj> list) {
        if (mActionAdaper == null) {
            mActionAdaper = new ActionAdaper(context, list, type);
            dataList.setAdapter(mActionAdaper);
        } else {
            mActionAdaper.addItems(list);
        }
    }
}
