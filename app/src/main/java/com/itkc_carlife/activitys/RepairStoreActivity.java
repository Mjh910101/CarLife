package com.itkc_carlife.activitys;

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
import com.itkc_carlife.box.RepairStoreObj;
import com.itkc_carlife.box.ServersCommentObj;
import com.itkc_carlife.box.ServicesObj;
import com.itkc_carlife.box.handler.RepairStoreObjHandler;
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
 * Created by Hua on 16/1/21.
 */
public class RepairStoreActivity extends BaseActivity {

    public final static int LIMIT = 10;

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.repairStore_coverImage)
    private ImageView coverImage;
    @ViewInject(R.id.repairStore_moneyText)
    private TextView moneyText;
    @ViewInject(R.id.repairStore_nameText)
    private TextView nameText;
    @ViewInject(R.id.repairStore_addressText)
    private TextView addressText;
    @ViewInject(R.id.repairStore_telText)
    private TextView telText;
    @ViewInject(R.id.repairStore_descriptText)
    private TextView descriptText;
    @ViewInject(R.id.repairStore_progress)
    private ProgressBar progress;
    @ViewInject(R.id.repairStore_imageListBox)
    private LinearLayout imageListBox;
    @ViewInject(R.id.repairStore_scroll)
    private ScrollView scroll;
    @ViewInject(R.id.repairStore_detailList)
    private InsideListView commentList;
    @ViewInject(R.id.repairStore_commentSize)
    private TextView commentSize;
    @ViewInject(R.id.repairStore_dayText)
    private TextView dayText;

    private RepairStoreObj mRepaitStore;
    private ServicesObj mServerObj;
    private String repairId;
    private int page = 1, pages = 1;
    private boolean isDownload = true;

    private CommentAdapter mCommentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_repair_store);
        context = this;
        ViewUtils.inject(this);

        initActivity();
        setOnTouchListener();
    }

    @OnClick({R.id.title_backBtn, R.id.repairStore_determineBtn, R.id.repairStore_addressText})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.repairStore_determineBtn:
                confirm();
                break;
            case R.id.repairStore_addressText:
                jumpPositionActivity();
                break;
        }
    }

    private void jumpPositionActivity() {
        Bundle b = new Bundle();
        b.putBoolean(PositionActivity.IS_HAVE, true);
        b.putDouble(PositionActivity.LATITUDE_KEY, mRepaitStore.getLatitude());
        b.putDouble(PositionActivity.LONGITUDE_KEY, mRepaitStore.getLongitude());
        Passageway.jumpActivity(context, PositionActivity.class, b);
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("抢修发布");

        mRepaitStore = RepairStoreObjHandler.getSaveRepairStoreObj();
        if (mRepaitStore != null) {
            downloadShop(mRepaitStore.getObjectId());
            downloadComment(mRepaitStore.getObjectId());
        } else {
            finish();
        }

        Bundle b = getIntent().getExtras();
        if (b != null) {
            repairId = b.getString("id");
            moneyText.setText("￥" + b.getString("fee"));
            dayText.setText(b.getInt("day") + "天");
        } else {
            finish();
        }
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
                        }
                        if ((scrollY + height) == scrollViewMeasuredHeight) {
                            if (progress.getVisibility() != View.VISIBLE) {
                                if (isDownload) {
                                    isDownload = false;
                                    if (pages >= page) {
                                        downloadComment(mRepaitStore.getObjectId());
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

    public void setRepaitStoreView(RepairStoreObj obj) {
        DownloadImageLoader.loadImage(coverImage, obj.getCoverUrl());

        moneyText.setText("￥" + obj.getFee());
        nameText.setText(obj.getName());
        addressText.setText(obj.getAddress());
        telText.setText(obj.getContact());
        descriptText.setText(obj.getDescript());

//        imageListBox.addView(new PptView(context, obj.getImages()));
    }

    private void setShopMessage(ServicesObj obj) {
        double w = WinTool.getWinWidth(context);
        double h = w / 32d * 15d;
//        coverImage.setLayoutParams(new LinearLayout.LayoutParams((int) w, (int) h));
//        DownloadImageLoader.loadImage(coverImage, obj.getCover());
        nameText.setText(obj.getName());
        addressText.setText(obj.getAddress());
        telText.setText(obj.getContact());
        descriptText.setText(obj.getDescript());

        imageListBox.addView(new PptView(context, obj.getImages(), (int) w, (int) h));
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

    private void confirm() {
        progress.setVisibility(View.VISIBLE);

        String url = UrlHandler.getUserRepairOrderConfirm();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("repairid", repairId);
        params.addBodyParameter("storeid", mRepaitStore.getObjectId());

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
                                Passageway.jumpActivity(context, RepairConfirmActivity.class);
                            }
                        }
                    }

                });
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

}
