package com.itkc_carlife.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.box.handler.UserObjHandler;
import com.itkc_carlife.download.DownloadImageLoader;
import com.itkc_carlife.handlers.DateHandle;
import com.itkc_carlife.handlers.JsonHandle;
import com.itkc_carlife.handlers.MessageHandler;
import com.itkc_carlife.handlers.QRCodeHandler;
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
 * Created by Hua on 16/1/13.
 */
public class QRCodeActivity extends BaseActivity {

    public final static String QR_CODE = "qrcode";

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.qrCode_codeImage)
    private ImageView codeImage;
    @ViewInject(R.id.qrCode_progress)
    private ProgressBar progress;
    @ViewInject(R.id.qrCode_stateBtn)
    private TextView stateBtn;

    private String qrcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_qrcode);
        context = this;
        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick({R.id.title_backBtn, R.id.qrCode_stateBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.qrCode_stateBtn:
                jumpInputMessage();
                break;
        }
    }

    private void jumpInputMessage() {
        Bundle b = new Bundle();
        b.putBoolean(SettingInputMessageActivity.IS_THR_MAIN, true);
        b.putString(SettingInputMessageActivity.SRTTLING_ID_KEY, qrcode);
        Passageway.jumpActivity(context, SettingInputMessageActivity.class, b);
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("定损二维码");

        Bundle b = getIntent().getExtras();
        if (b == null) {
            downloadSetingId();
            stateBtn.setVisibility(View.VISIBLE);
        } else {
            showQrCode(b.getString(QR_CODE));
            stateBtn.setVisibility(View.GONE);
        }
    }


    private void showQrCode(String qrcode) {
        this.qrcode = qrcode;
        int w = WinTool.getWinWidth(context) / 4 * 3;
        File f = new File(DownloadImageLoader.getImagePath(), "qr_" + DateHandle.getTime() + ".jpg");
        boolean b = QRCodeHandler.createQRImage(qrcode, w, w, null, f.toString());
        if (b) {
            DownloadImageLoader.loadImageForFile(codeImage, f.toString());
        }
    }

    private void downloadSetingId() {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getUserSettlingCreate();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));

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
                                if (resultJson != null) {
                                    showQrCode(JsonHandle.getString(resultJson, "qrcode"));
                                }
                            } else {
                                MessageHandler.showToast(context, JsonHandle.getString(json, "result"));
                            }
                        }
                    }

                });
    }


}
