package com.itkc_carlife.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.box.PartyObj;
import com.itkc_carlife.box.UserObj;
import com.itkc_carlife.box.handler.PartyObjHandler;
import com.itkc_carlife.box.handler.UserObjHandler;
import com.itkc_carlife.download.DownloadImageLoader;
import com.itkc_carlife.handlers.JsonHandle;
import com.itkc_carlife.handlers.MessageHandler;
import com.itkc_carlife.handlers.TextHandeler;
import com.itkc_carlife.handlers.TextJustification;
import com.itkc_carlife.http.HttpUtilsBox;
import com.itkc_carlife.http.UrlHandler;
import com.itkc_carlife.tool.Passageway;
import com.itkc_carlife.tool.WinTool;
import com.itkc_carlife.views.InsideListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

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
 * Created by Hua on 16/2/3.
 */
public class ParytActivity extends BaseActivity {

    public final static String ID_KEY = "id";

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.title_addIcon)
    private ImageView addIcon;
    @ViewInject(R.id.party_progress)
    private ProgressBar progress;
    @ViewInject(R.id.party_pic)
    private ImageView partyPic;
    @ViewInject(R.id.party_title)
    private TextView partyTitle;
    @ViewInject(R.id.party_info)
    private TextView partyInfo;
    @ViewInject(R.id.party_descript)
    private TextView partyDescript;
    @ViewInject(R.id.party_descriptWeb)
    private WebView partyDescriptWeb;
    @ViewInject(R.id.party_favorBtnText)
    private TextView favorBtnText;
    @ViewInject(R.id.party_joinBtnText)
    private TextView joinBtnText;
    @ViewInject(R.id.party_userList)
    private InsideListView userList;
    @ViewInject(R.id.party_favorBtnIcon)
    private ImageView favorBtnIcon;
    @ViewInject(R.id.party_joinBtnIcon)
    private ImageView joinBtnIcon;
    @ViewInject(R.id.party_joinBtnBox)
    private LinearLayout joinBtnBox;
    @ViewInject(R.id.party_userSum)
    private TextView userSum;
    @ViewInject(R.id.party_userSumLine)
    private View userSumLine;

    private String partyId;
    private PartyObj mPartyObj;

    private boolean isUpload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_party);
        context = this;
        ViewUtils.inject(this);

        initActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AddPartyActivity.REQUEST_CODE:
                if (data != null) {
                    Bundle b = data.getExtras();
                    if (b != null) {
                        isUpload = b.getBoolean(AddPartyActivity.IS_UPLOAD, false);
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

    @OnClick({R.id.title_backBtn, R.id.party_favorBtn, R.id.party_joinBtn, R.id.title_addIcon, R.id.party_pic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                colse();
                break;
            case R.id.party_favorBtn:
                favor();
                break;
            case R.id.party_joinBtn:
                join();
                break;
            case R.id.title_addIcon:
                Passageway.jumpActivity(context, AddPartyActivity.class, AddPartyActivity.REQUEST_CODE);
                break;
            case R.id.party_pic:
                jumpImageActivity();
                break;
        }
    }

    private void jumpImageActivity() {
        if (mPartyObj != null) {
            Bundle b = new Bundle();
            b.putBoolean(ImageActivity.IS_ONLINE, true);
            b.putString(ImageActivity.URL, mPartyObj.getCoverUrl());
            Passageway.jumpActivity(context, ImageActivity.class, ImageActivity.REQUEST_CODE, b);
        }
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("活动详细");
        addIcon.setVisibility(View.VISIBLE);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            partyId = b.getString(ID_KEY);
            downloadData();
        } else {
            finish();
        }
    }

    private void setMessageView(PartyObj obj) {
        double w = WinTool.getWinWidth(context);
        double h = w / 64d * 35d;
        partyPic.setLayoutParams(new LinearLayout.LayoutParams((int) w, (int) h));
        DownloadImageLoader.loadImage(partyPic, obj.getCoverUrl());
        partyTitle.setText(obj.getTitle());
        partyInfo.setText(obj.getType() + "/" + obj.getInfo());
        partyDescript.setText(obj.getDescript());
        setFavor(obj);
        setJoin(obj);
        setUserList(obj);

        TextHandeler.setNeatText(partyDescriptWeb, obj.getDescript(), 18, "#000000");

    }

    public void setUserList(PartyObj obj) {
        if (!obj.isShow()) {
            userSum.setVisibility(View.GONE);
            userList.setVisibility(View.GONE);
            userSumLine.setVisibility(View.GONE);
            return;
        }
        String sizeStr = "<font color=\"#b8b8b8\">已经报名</font>" +
                "<font color=\"#0075bb\">?</font>" +
                "<font color=\"#b8b8b8\">人</font>";
        userSum.setText(Html.fromHtml(sizeStr.replace("?", String.valueOf(obj.getJoinlistSize()))));
        userSum.setVisibility(View.VISIBLE);
        userList.setVisibility(View.VISIBLE);
        userSumLine.setVisibility(View.VISIBLE);
        userList.setFocusable(false);
        userList.setAdapter(new PeopleAdapter(obj.getJoinlist()));
    }

    public void setJoin(PartyObj obj) {
        if (obj.isOutTime()) {
            joinBtnIcon.setImageResource(R.drawable.out_time_icon);
            joinBtnText.setText("已经结束");
            joinBtnBox.setBackgroundResource(R.drawable.solid_gray_box);
        } else {
            if (obj.isjoin()) {
                joinBtnIcon.setImageResource(R.drawable.out_party_icon);
                joinBtnText.setText("已经参加");
                joinBtnBox.setBackgroundResource(R.drawable.solid_bule_box_02);
            } else {
                joinBtnIcon.setImageResource(R.drawable.in_party_icon);
                joinBtnText.setText("我要参加");
                joinBtnBox.setBackgroundResource(R.drawable.solid_bule_box);
            }
        }
    }

    public void setFavor(PartyObj obj) {
        if (obj.isfavor()) {
            favorBtnIcon.setImageResource(R.drawable.delete_party_icon);
            favorBtnText.setText("取消收藏");
        } else {
            favorBtnIcon.setImageResource(R.drawable.like_party_icon);
            favorBtnText.setText("我感兴趣");
        }
    }


    private void favor() {
        if (mPartyObj == null) {
            return;
        }
        if (mPartyObj.isfavor()) {
            uploadPost(UrlHandler.getFavorPostRemove());
        } else {
            uploadPost(UrlHandler.getFavorPostAdd());
        }
    }


    private void join() {
        if (mPartyObj == null) {
            return;
        }
        if (mPartyObj.isjoin()) {
            uploadPost(UrlHandler.getJoinPostRemove());
        } else {
            uploadPost(UrlHandler.getJoinPostAdd());
        }
    }

    private void uploadPost(String url) {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("id", mPartyObj.getObjectId());

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
                            JSONObject resultJson = JsonHandle.getJSON(json, "result");
                            if (status == 1) {
                                downloadData();
                            } else {
                                MessageHandler.showToast(context, JsonHandle.getString(resultJson, "message"));
                            }
                        }
                    }

                });
    }

    private void downloadData() {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getPost() + "/" + partyId + "?sessiontoken=" + UserObjHandler.getSessionToken(context);

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
                                    mPartyObj = PartyObjHandler.getPartyObj(resultJson);
                                    setMessageView(mPartyObj);
                                }
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

    class PeopleAdapter extends BaseAdapter {

        List<UserObj> list;
        LayoutInflater inflater;

        PeopleAdapter(List<UserObj> list) {
            this.list = list;
            this.inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            HolderView holder;
            if (convertView == null) {
                convertView = inflater.inflate(
                        R.layout.layout_user_item, null);
                holder = new HolderView();

                holder.name = (TextView) convertView.findViewById(R.id.partyUserItem_name);
                holder.tel = (TextView) convertView.findViewById(R.id.partyUserItem_tel);
                holder.pic = (ImageView) convertView.findViewById(R.id.partyUserItem_pic);

                convertView.setTag(holder);
            } else {
                holder = (HolderView) convertView.getTag();
            }

            UserObj obj = list.get(position);
            setView(holder, obj);

            return convertView;
        }

        private void setView(HolderView holder, UserObj obj) {
            holder.name.setText(obj.getUsername());
            holder.tel.setText(obj.getMobilePhoneNumber());

            int w = WinTool.getWinWidth(context) / 12;
            holder.pic.setLayoutParams(new RelativeLayout.LayoutParams(w, w));
            DownloadImageLoader.loadImage(holder.pic, obj.getAvatar(), w / 2);
        }
    }


    class HolderView {
        TextView name;
        TextView tel;
        ImageView pic;
    }

}
