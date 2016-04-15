package com.itkc_carlife.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ant.liao.GifView;
import com.itkc_carlife.R;
import com.itkc_carlife.box.RepairStoreObj;
import com.itkc_carlife.box.handler.RepairStoreObjHandler;
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
 * Created by Hua on 16/3/4.
 */
public class WaitingRepairStoreActivity extends BaseActivity {

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.waitingRepairStore_timeText)
    private TextView timeText;
    @ViewInject(R.id.waitingRepairStore_progress)
    private ProgressBar progress;
    @ViewInject(R.id.waitingRepairStore_gifView)
    private GifView gifView;

    private boolean isRun;
    private String repairId;
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_waiting_repair_store);
        context = this;
        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick({R.id.title_backBtn, R.id.waitingRepairStore_cancelBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                close();
                break;
            case R.id.waitingRepairStore_cancelBtn:
                cancel();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            close();
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
//        isRun = false;
    }

    private void close() {
        isRun = false;
        finish();
    }

    private void initActivity() {
//        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("车辆抢修");
        isRun = true;
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            repairId = mBundle.getString(RepairInputMessageActivity.REPAIR_ID_KEY);
            startTimeRun();
            download(repairId);
        } else {
            close();
        }
    }

    private void cancel() {
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
                                Passageway.jumpToActivity(context, RepairDataListActivity.class);
                                close();
                            }
                        }
                    }

                });
    }

    private void download(final String repairId) {

        String url = UrlHandler.getUserRepairGrab() + "?repairid=" + repairId + "&sessiontoken=" + UserObjHandler.getSessionToken(context);

        RequestParams params = HttpUtilsBox.getRequestParams(context);

        HttpUtilsBox.getHttpUtil().send(HttpMethod.GET, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception, String msg) {
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.d("", result);

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            int status = JsonHandle.getInt(json, "status");
                            if (status == 1) {
                                JSONObject resultJson = JsonHandle.getJSON(json, "result");
                                if (resultJson != null) {
                                    JSONArray array = JsonHandle.getArray(resultJson, "store");
                                    if (array != null) {
                                        List<RepairStoreObj> list = RepairStoreObjHandler.getRepairStoreObjList(array);
                                        if (isRun) {
                                            if (list.size() == 0) {
                                                download(repairId);
                                            } else {
                                                Passageway.jumpActivity(context, RepairStoreListActivity.class, mBundle);
                                                close();
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }

                });
    }

    private void startTimeRun() {
        gifView.setGifImage(R.drawable.watting);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (isRun) {
                    Message.obtain(mHandler, i).sendToTarget();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i += 1;
                }
            }
        }).start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int time = msg.what;
            int s = time % 60;
            int m = time / 60;
            String sSrt;
            if (s < 10) {
                sSrt = "0" + String.valueOf(s);
            } else {
                sSrt = String.valueOf(s);
            }

            String mSrt;
            if (m < 10) {
                mSrt = "0" + String.valueOf(m);
            } else {
                mSrt = String.valueOf(m);
            }
            timeText.setText(mSrt + ":" + sSrt);
        }
    };

}
