package com.itkc_carlife.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.itkc_carlife.R;
import com.itkc_carlife.box.handler.UserObjHandler;
import com.itkc_carlife.download.DownloadImageLoader;
import com.itkc_carlife.handlers.JsonHandle;
import com.itkc_carlife.handlers.MessageHandler;
import com.itkc_carlife.http.AVFileHandler;
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

import org.json.JSONObject;

import java.io.File;

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
 * Created by Hua on 16/1/15.
 */
public class InputVideoActivity extends BaseActivity {

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.insuranceInputVideo_videoBtn)
    private ImageView videoBtn;
    @ViewInject(R.id.insuranceInputVideo_progress)
    private ProgressBar progress;
    @ViewInject(R.id.title_sainningIcon)
    private ImageView sainningIcon;

    private Bundle mBundle;
    private String settlingId, repairId, where;

    private File videoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_insurance_input_video);
        context = this;
        ViewUtils.inject(this);

        initActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Passageway.VIDEO_REQUEST_CODE:
                setVideoBtn();
                break;
            case PlayVideoActivity.REQUEST_CODE:
                if (data != null) {
                    if (data.getExtras().getBoolean(PlayVideoActivity.IS_DELETE)) {
                        videoFile = null;
                        setVideoBtn();
                    }
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @OnClick({R.id.title_backBtn, R.id.insuranceInputVideo_videoBtn, R.id.insuranceInputVideo_saveBtn, R.id.title_sainningIcon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.insuranceInputVideo_videoBtn:
                if (isThrough()) {
                    jumpVideoPlay();
                } else {
                    videoFile = new File(DownloadImageLoader.getImagePath(), Passageway.takeVideo(context));
                }
                break;
            case R.id.insuranceInputVideo_saveBtn:
                if (isThrough()) {
                    uploadVideo();
                } else {
                    MessageHandler.showToast(context, "请拍摄现场视频");
                }
                break;
            case R.id.title_sainningIcon:
                jumpQrCodeActivity();
                break;
        }
    }

    private void jumpQrCodeActivity() {
        Bundle b = new Bundle();
        b.putString(QRCodeActivity.QR_CODE, settlingId);
        Passageway.jumpActivity(context, QRCodeActivity.class, b);
    }

    private void jumpVideoPlay() {
        Bundle b = new Bundle();
        b.putBoolean(PlayVideoActivity.IS_FILE, true);
        b.putString(PlayVideoActivity.PATH, videoFile.toString());
        Passageway.jumpActivity(context, PlayVideoActivity.class, PlayVideoActivity.REQUEST_CODE, b);
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("车辆定损视频");

        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            where = mBundle.getString(InputCarDamagedActivity.WHERE_KEY);
            if (where.equals(InputCarDamagedActivity.INSURANCE_KEY)) {
                settlingId = mBundle.getString(SettingInputMessageActivity.SRTTLING_ID_KEY);
            } else if (where.equals(InputCarDamagedActivity.REPAIR_KEY)) {
                repairId = mBundle.getString(RepairInputMessageActivity.REPAIR_ID_KEY);
            }
            if (mBundle.getBoolean(SettingInputMessageActivity.IS_THR_MAIN)) {
                sainningIcon.setVisibility(View.VISIBLE);
            }
        } else {
            finish();
        }

    }

    private void setVideoBtn() {
        if (isThrough()) {
            int p = 30;
            videoBtn.setImageResource(R.drawable.play_video_icon);
            videoBtn.setBackgroundResource(R.color.text_black);
            videoBtn.setPadding(p, p, p, p);
        } else {
            int p = 0;
            videoBtn.setImageResource(R.drawable.add_pic_icon);
            videoBtn.setBackgroundResource(R.color.text_white);
            videoBtn.setPadding(p, p, p, p);
        }
    }

    public boolean isThrough() {
        if (videoFile == null) {
            return false;
        }
        if (!videoFile.exists()) {
            return false;
        }
        return true;
    }


    private void uploadVideo() {
        progress.setVisibility(View.VISIBLE);
        AVFileHandler.uploadFile(videoFile, new AVFileHandler.AVSaveCallback() {
            @Override
            public void callnack(AVException e, AVFile file) {
                if (e == null) {
                    uploadMessage(file.getObjectId());
                } else {
                    progress.setVisibility(View.GONE);
                    MessageHandler.showToast(context, "保存失败");
                    e.printStackTrace();
                }
            }
        });
    }

    private void uploadMessage(String id) {
        if (where.equals(InputCarDamagedActivity.INSURANCE_KEY)) {
            uploadMessage(UrlHandler.getUserSettlingVideo(), id, "settlingid", settlingId);
        } else if (where.endsWith(InputCarDamagedActivity.REPAIR_KEY)) {
            uploadMessage(UrlHandler.getUserRepairVideo(), id, "repairid", repairId);
        }
    }

    private void uploadMessage(String url, String videoId, String idKey, String id) {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter(idKey, id);
        params.addBodyParameter("video_objectid", videoId);

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
                                jump();
                            } else {
                                MessageHandler.showToast(context, JsonHandle.getString(json, "result"));
                            }
                        }
                    }

                });
    }

    private void jump() {
        if (where.equals(InputCarDamagedActivity.INSURANCE_KEY)) {
            jumpInsurance();
        } else if (where.equals(InputCarDamagedActivity.REPAIR_KEY)) {
            jumpRepair();
        }
    }

    private void jumpRepair() {
        Passageway.jumpActivity(context, RepairActivity.class, mBundle);
    }


    private void jumpInsurance() {
        Passageway.jumpActivity(context, SettlingActivity.class, mBundle);
    }
}
