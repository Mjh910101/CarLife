package com.itkc_carlife.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.adapters.PartyAdapter;
import com.itkc_carlife.box.PartyObj;
import com.itkc_carlife.box.handler.PartyObjHandler;
import com.itkc_carlife.box.handler.UserObjHandler;
import com.itkc_carlife.download.DownloadImageLoader;
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
 * Created by Hua on 16/2/1.
 */
public class PartyListActivity extends BaseActivity {

    public final static int LIMIT = 10;

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.partyList_progress)
    private ProgressBar progress;
    @ViewInject(R.id.partyList_dataaList)
    private ListView dataList;
    @ViewInject(R.id.title_partyDataIcon)
    private ImageView partyDataIcon;
    @ViewInject(R.id.partyList_bannerImage)
    private ImageView bannerImage;
    @ViewInject(R.id.partyList_swipeRefresh)
    private SwipeRefreshLayout swipeRefresh;

    private PartyAdapter mPartyAdapter;

    private int page = 1, pages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_party_data_list);
        context = this;
        ViewUtils.inject(this);

        initActivity();
        setDataListScrollListener();
        setOnRefreshListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AddPartyActivity.REQUEST_CODE:
                if (data != null) {
                    Bundle b = data.getExtras();
                    if (b.getBoolean(AddPartyActivity.IS_UPLOAD, false)) {
                        resultDownload(true);
                    }
                }
                break;

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.title_backBtn, R.id.title_partyDataIcon, R.id.partyList_bannerImage})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.title_partyDataIcon:
                Passageway.jumpActivity(context, UserPartyListActivity.class, AddPartyActivity.REQUEST_CODE);
                break;
            case R.id.partyList_bannerImage:
                Bundle b = new Bundle();
                b.putString(WebActivity.URL, "http://www.51dojoy.com");
                b.putString(WebActivity.TITLE, "");
                Passageway.jumpActivity(context, WebActivity.class, b);
                break;
        }
    }

    private void setDataListScrollListener() {
        dataList.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() >= (view.getCount() - 1)) {
                        if (page < pages) {
                            if (progress.getVisibility() == View.GONE) {
                                downloadData(true);
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

    private void setOnRefreshListener() {
        swipeRefresh.setColorScheme(R.color.holo_blue_bright,
                R.color.holo_green_light, R.color.holo_orange_light,
                R.color.holo_red_light);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                resultDownload(false);
            }
        });
    }

    private void closeSwipeRefresh() {
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        partyDataIcon.setVisibility(View.VISIBLE);
        titleName.setText("会员活动");
        downloadData(true);

        double w = WinTool.getWinWidth(context);
        double h = w / 128d * 105d;
        bannerImage.setLayoutParams(new RelativeLayout.LayoutParams((int) w, (int) h));
    }


    public void setDataList(List<PartyObj> list) {
        if (mPartyAdapter == null) {
            mPartyAdapter = new PartyAdapter(context, list);
            dataList.setAdapter(mPartyAdapter);
        } else {
            mPartyAdapter.addItems(list);
        }

        closeSwipeRefresh();
    }

    private void resultDownload(boolean b) {
        page = 1;
        pages = 1;
        mPartyAdapter = null;
        downloadData(b);
    }


    public void setBanner(final JSONObject banner) {
        if (banner == null || page > 1) {
            return;
        }
//        bannerImage.setVisibility(View.VISIBLE);
//        DownloadImageLoader.loadImage(bannerImage, JsonHandle.getString(banner, "img"));
//        bannerImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle b = new Bundle();
//                b.putString(WebActivity.URL, JsonHandle.getString(banner, "url"));
//                b.putString(WebActivity.TITLE, "");
//                Passageway.jumpActivity(context, WebActivity.class, b);
//            }
//        });
    }

    private void downloadData(boolean b) {
        if (b) {
            progress.setVisibility(View.VISIBLE);
        }
        String url = UrlHandler.getPost() + "?sessiontoken=" + UserObjHandler.getSessionToken(context) + "&limit=" + LIMIT + "&page=" + page;

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
                                    List<PartyObj> list = PartyObjHandler.getPartyObjList(array);
                                    setDataList(list);
                                }
                                pages = JsonHandle.getInt(json, "pages");
                                setBanner(JsonHandle.getJSON(json, "banner"));
                                page += 1;
                            }
                        }
                    }

                });
    }

}
