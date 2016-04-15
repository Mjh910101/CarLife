package com.itkc_carlife.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.adapters.CommentAdapter;
import com.itkc_carlife.box.ServersCommentObj;
import com.itkc_carlife.box.ServicesObj;
import com.itkc_carlife.box.handler.ServersCommentObjHandler;
import com.itkc_carlife.box.handler.ServicesObjHandler;
import com.itkc_carlife.box.handler.UserObjHandler;
import com.itkc_carlife.download.DownloadImageLoader;
import com.itkc_carlife.handlers.JsonHandle;
import com.itkc_carlife.handlers.MessageHandler;
import com.itkc_carlife.http.HttpUtilsBox;
import com.itkc_carlife.http.UrlHandler;
import com.itkc_carlife.tool.Passageway;
import com.itkc_carlife.tool.WinTool;
import com.itkc_carlife.views.InsideListView;
import com.itkc_carlife.views.PptView;
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
 * Created by Hua on 16/2/18.
 */
public class ServerShopActivity extends BaseActivity {

    public final static int LIMIT = 10;
    public final static int Request = 1;

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.serverShop_image)
    private ImageView image;
    @ViewInject(R.id.serverShop_name)
    private TextView nameText;
    @ViewInject(R.id.serverShop_address)
    private TextView addressText;
    @ViewInject(R.id.serverShop_tel)
    private TextView telText;
    @ViewInject(R.id.serverShop_descript)
    private TextView descriptText;
    @ViewInject(R.id.serverShop_progress)
    private ProgressBar progress;
    @ViewInject(R.id.serverShop_detailList)
    private InsideListView commentList;
    @ViewInject(R.id.serverShop_scroll)
    private ScrollView scroll;
    @ViewInject(R.id.serverShop_commentSize)
    private TextView commentSize;
    @ViewInject(R.id.serverShop_imageListBox)
    private LinearLayout imageListBox;

    private ServicesObj mServerObj;
    private int page = 1, pages = 1;
    private String serverId;
    private boolean isDownload = true;

    private Bundle mBundle;
    private CommentAdapter mCommentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_server_shop);
        context = this;
        ViewUtils.inject(this);

        initActivity();
        setOnTouchListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Request:
                page = 1;
                pages = 1;
                mCommentAdapter = null;
                downloadComment(serverId);
        }
    }

    @OnClick({R.id.title_backBtn, R.id.serverShop_address, R.id.serverShop_navigationBtn,
            R.id.serverShop_commentBtn_01, R.id.serverShop_commentBtn_02})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.serverShop_address:
                jumpPositionActivity();
                break;
            case R.id.serverShop_navigationBtn:
                jumpNavigationActivity();
                break;
            case R.id.serverShop_commentBtn_01:
            case R.id.serverShop_commentBtn_02:
                Passageway.jumpActivity(context, ServerShopCommentActivity.class, Request, mBundle);
                break;
        }
    }

    private void jumpNavigationActivity() {
        Bundle b = new Bundle();
        b.putBoolean(PositionActivity.IS_HAVE, true);
        b.putBoolean(PositionActivity.IS_NAVIGATION, true);
        b.putDouble(PositionActivity.LATITUDE_KEY, mServerObj.getLatitude());
        b.putDouble(PositionActivity.LONGITUDE_KEY, mServerObj.getLongitude());
        Passageway.jumpActivity(context, PositionActivity.class, b);
    }

    private void jumpPositionActivity() {
        Bundle b = new Bundle();
        b.putBoolean(PositionActivity.IS_HAVE, true);
        b.putDouble(PositionActivity.LATITUDE_KEY, mServerObj.getLatitude());
        b.putDouble(PositionActivity.LONGITUDE_KEY, mServerObj.getLongitude());
        Passageway.jumpActivity(context, PositionActivity.class, b);
    }

    public void setOnTouchListener() {
        scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_MOVE:
                        int scrollY = v.getScrollY();
                        int height = v.getHeight();
                        int scrollViewMeasuredHeight = scroll.getChildAt(0).getMeasuredHeight();
                        if (scrollY == 0) {
                            System.out.println("滑动到了顶端 view.getScrollY()=" + scrollY);
                        }
                        if ((scrollY + height) == scrollViewMeasuredHeight) {
                            System.out.println("滑动到了底部 scrollY=" + scrollY);
                            System.out.println("滑动到了底部 height=" + height);
                            System.out.println("滑动到了底部 scrollViewMeasuredHeight=" + scrollViewMeasuredHeight);
                            if (progress.getVisibility() != View.VISIBLE) {
                                if (isDownload) {
                                    isDownload = false;
                                    if (pages >= page) {
                                        downloadComment(serverId);
                                    } else {
                                        MessageHandler.showLast(context);
                                    }
                                }


                            }
                        }
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("商家推荐");

        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            serverId = mBundle.getString("id");
            downloadShop(serverId);
            downloadComment(serverId);
        } else {
            finish();
        }
    }

    public void setServersCommentList(List<ServersCommentObj> list, int cont) {
        String sizeStr = "<font color=\"#b8b8b8\">共</font>" +
                "<font color=\"#0075bb\">?</font>" +
                "<font color=\"#b8b8b8\">条评论</font>";
        commentSize.setText(Html.fromHtml(sizeStr.replace("?", String.valueOf(cont))));
        if (mCommentAdapter == null) {
            commentList.setFocusable(false);
            mCommentAdapter = new CommentAdapter(context, list);
            commentList.setAdapter(mCommentAdapter);
        } else {
            mCommentAdapter.addItem(list);
        }
    }

    private void downloadComment(String id) {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getServicesComment() + "?sessiontoken=" + UserObjHandler.getSessionToken(context) + "&servicesid=" + id + "&page=" + page + "&limit=" + LIMIT;

        RequestParams params = HttpUtilsBox.getRequestParams(context);

        HttpUtilsBox.getHttpUtil().send(HttpMethod.GET, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception, String msg) {
                        progress.setVisibility(View.GONE);
                        isDownload = true;
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.d("", result);
                        progress.setVisibility(View.GONE);
                        isDownload = true;
                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            int status = JsonHandle.getInt(json, "status");
                            if (status == 1) {
                                JSONArray array = JsonHandle.getArray(json, "result");
                                if (array != null) {
                                    List<ServersCommentObj> list = ServersCommentObjHandler.getServersCommentObjList(array);
                                    setServersCommentList(list, JsonHandle.getInt(json, "count"));
                                    page += 1;
                                }
                                pages = JsonHandle.getInt(json, "pages");
                            }
                        }
                    }

                });
    }

    private void setShopMessage(ServicesObj obj) {
        double w = WinTool.getWinWidth(context);
        double h = w / 32d * 15d;
        image.setLayoutParams(new LinearLayout.LayoutParams((int) w, (int) h));
        DownloadImageLoader.loadImage(image, obj.getCover());
        nameText.setText(obj.getName());
        addressText.setText(obj.getAddress());
        telText.setText(obj.getContact());
        descriptText.setText(obj.getDescript());

        imageListBox.addView(new PptView(context, obj.getImages(), (int) w, (int) h));
    }

    private void downloadShop(String id) {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getServicesDetail() + "/" + id;

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
                                    mServerObj = ServicesObjHandler.getServicesObj(resultJson);
                                    setShopMessage(mServerObj);
                                }
                            }
                        }
                    }

                });
    }

}
