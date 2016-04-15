package com.itkc_carlife.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.box.handler.UserObjHandler;
import com.itkc_carlife.handlers.JsonHandle;
import com.itkc_carlife.handlers.MessageHandler;
import com.itkc_carlife.handlers.TextHandeler;
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

import org.json.JSONObject;

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
public class ServerShopCommentActivity extends BaseActivity {

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.serverShopComment_start01)
    private ImageView start01;
    @ViewInject(R.id.serverShopComment_start02)
    private ImageView start02;
    @ViewInject(R.id.serverShopComment_start03)
    private ImageView start03;
    @ViewInject(R.id.serverShopComment_progress)
    private ProgressBar progress;
    @ViewInject(R.id.serverShopComment_contentInput)
    private EditText contentInput;

    private Bundle mBundle;
    private int rate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_server_shop_comment);
        context = this;
        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick({R.id.title_backBtn, R.id.serverShopComment_uploadBtn, R.id.serverShopComment_start01,
            R.id.serverShopComment_start02, R.id.serverShopComment_start03})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.serverShopComment_start01:
                setStare(1);
                break;
            case R.id.serverShopComment_start02:
                setStare(2);
                break;
            case R.id.serverShopComment_start03:
                setStare(3);
                break;
            case R.id.serverShopComment_uploadBtn:
                if (isThrough()) {
                    upload();
                }
                break;
        }
    }

    private boolean isThrough() {
        if (rate == 0) {
            MessageHandler.showToast(context, "您还没有对商家评星哦~");
            return false;
        }

        if (TextHandeler.getText(contentInput).equals("")) {
            MessageHandler.showToast(context, "请先填写评论");
            return false;
        }

        return true;
    }

    private void upload() {
        String url = UrlHandler.getServicesComment();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("servicesid", mBundle.getString("id"));
        params.addBodyParameter("content", TextHandeler.getText(contentInput));
        params.addBodyParameter("rate", String.valueOf(rate));
        if(mBundle.getBoolean("isRepair")){
            params.addBodyParameter("repairid", mBundle.getString("repairId"));
        }


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
                                MessageHandler.showToast(context, "发表成功");
                                finish();
                            } else {
                                MessageHandler.showToast(context, JsonHandle.getString(json, "result"));
                            }
                        }
                    }

                });
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("评论");

        mBundle = getIntent().getExtras();
        if (mBundle != null) {
        } else {
            finish();
        }
    }

    public void setStare(int rate) {
        this.rate = rate;
        start01.setImageResource(R.drawable.comment_start_icon);
        start02.setImageResource(R.drawable.comment_start_icon);
        start03.setImageResource(R.drawable.comment_start_icon);
        switch (rate) {
            case 3:
                start03.setImageResource(R.drawable.comment_on_start_icon);
            case 2:
                start02.setImageResource(R.drawable.comment_on_start_icon);
            case 1:
                start01.setImageResource(R.drawable.comment_on_start_icon);
                break;
        }
    }
}
