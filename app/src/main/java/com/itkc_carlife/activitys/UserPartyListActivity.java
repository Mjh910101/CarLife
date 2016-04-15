package com.itkc_carlife.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.adapters.PartyAdapter;
import com.itkc_carlife.box.PartyObj;
import com.itkc_carlife.box.handler.PartyObjHandler;
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
 * Created by Hua on 16/2/1.
 */
public class UserPartyListActivity extends BaseActivity {

    public final static int LIMIT = 10;

    public final static int JOIN = 1;
    public final static int FAVOR = 2;
    public final static int POST = 3;

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.userPartyList_progress)
    private ProgressBar progress;
    @ViewInject(R.id.userPartyList_dataaList)
    private ListView dataList;
    @ViewInject(R.id.title_addIcon)
    private ImageView addIcon;
    @ViewInject(R.id.userPartyList_joinText)
    private TextView joinText;
    @ViewInject(R.id.userPartyList_favorText)
    private TextView favorText;
    @ViewInject(R.id.userPartyList_postText)
    private TextView postText;
    @ViewInject(R.id.userPartyList_joinIcon)
    private ImageView joinIcon;
    @ViewInject(R.id.userPartyList_favorIcon)
    private ImageView favorIcon;
    @ViewInject(R.id.userPartyList_postIcon)
    private ImageView postIcon;
    @ViewInject(R.id.userPartyList_swipeRefresh)
    private SwipeRefreshLayout swipeRefresh;

    private PartyAdapter mPartyAdapter;

    private int page = 1, pages = 1;
    private String nowUrl;
    private int nowType;

    private boolean isUpload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_party_data_list);
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
                    if (b != null) {
                        isUpload = b.getBoolean(AddPartyActivity.IS_UPLOAD, false);
                        if (nowType==POST){
                            onClickType(POST);
                        }
                    }
                }
                break;

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            colse();
        }
        return false;
    }

    @OnClick({R.id.title_backBtn, R.id.title_addIcon, R.id.userPartyList_joinType, R.id.userPartyList_favorType, R.id.userPartyList_postType})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                colse();
                break;
            case R.id.title_addIcon:
                Passageway.jumpActivity(context, AddPartyActivity.class, AddPartyActivity.REQUEST_CODE);
                break;
            case R.id.userPartyList_joinType:
                onClickType(JOIN);
                break;
            case R.id.userPartyList_favorType:
                onClickType(FAVOR);
                break;
            case R.id.userPartyList_postType:
                onClickType(POST);
                break;
        }
    }

    private void onClickType(int i) {
        nowType = i;
        initType();
        switch (i) {
            case JOIN:
                joinIcon.setImageResource(R.drawable.on_in_paray_type);
                joinText.setTextColor(ColorHandle.getColorForID(context, R.color.text_blue));
                downloadData(UrlHandler.getPostJoin());
                break;
            case FAVOR:
                favorIcon.setImageResource(R.drawable.on_favor_paray_type);
                favorText.setTextColor(ColorHandle.getColorForID(context, R.color.text_blue));
                downloadData(UrlHandler.getUserPostFavor());
                break;
            case POST:
                postIcon.setImageResource(R.drawable.on_post_paray_type);
                postText.setTextColor(ColorHandle.getColorForID(context, R.color.text_blue));
                downloadData(UrlHandler.getUserPost());
                break;
        }
    }

    private void initType() {
        joinText.setTextColor(ColorHandle.getColorForID(context, R.color.text_blue_02));
        favorText.setTextColor(ColorHandle.getColorForID(context, R.color.text_blue_02));
        postText.setTextColor(ColorHandle.getColorForID(context, R.color.text_blue_02));

        joinIcon.setImageResource(R.drawable.off_in_party_type);
        favorIcon.setImageResource(R.drawable.off_favor_paray_type);
        postIcon.setImageResource(R.drawable.off_post_paray_type);

        mPartyAdapter = null;
        page = 1;
        pages = 1;
        dataList.setAdapter(null);
    }

    private void setDataListScrollListener() {
        dataList.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() >= (view.getCount() - 1)) {
                        if (page < pages) {
                            if (progress.getVisibility() == View.GONE) {
                                downloadData(nowUrl);
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
                onClickType(nowType);
                progress.setVisibility(View.GONE);

            }
        });
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("我的活动");
        addIcon.setVisibility(View.VISIBLE);

        onClickType(JOIN);
    }

    public void setDataList(List<PartyObj> list) {
        if (mPartyAdapter == null) {
            mPartyAdapter = new PartyAdapter(context, list, nowType);
            dataList.setAdapter(mPartyAdapter);
        } else {
            mPartyAdapter.addItems(list);
        }

        closeSwipeRefresh();
    }

    private void closeSwipeRefresh() {
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }

    private void downloadData(String nowUrl) {
        progress.setVisibility(View.VISIBLE);
        this.nowUrl = nowUrl;
        String url = nowUrl + "?sessiontoken=" + UserObjHandler.getSessionToken(context) + "&limit=" + LIMIT + "&page=" + page;

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
                                    page += 1;
                                }
                                pages = JsonHandle.getInt(json, "pages");
                            }
                        }
                    }

                });
    }

    private void colse() {
        Intent i = new Intent();
        Bundle b = new Bundle();
        b.putBoolean(AddPartyActivity.IS_UPLOAD, isUpload);
        i.putExtras(b);
        setResult(AddPartyActivity.REQUEST_CODE, i);
        finish();
    }

}
