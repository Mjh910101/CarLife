package com.itkc_carlife.activitys;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.itkc_carlife.R;
import com.itkc_carlife.box.UserObj;
import com.itkc_carlife.box.handler.UserObjHandler;
import com.itkc_carlife.dialogs.ListDialog;
import com.itkc_carlife.download.DownloadImageLoader;
import com.itkc_carlife.handlers.ColorHandle;
import com.itkc_carlife.handlers.DateHandle;
import com.itkc_carlife.handlers.JsonHandle;
import com.itkc_carlife.handlers.MessageHandler;
import com.itkc_carlife.handlers.TextHandeler;
import com.itkc_carlife.http.AVFileHandler;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
 * Created by Hua on 16/1/5.
 */
public class UserModifyActivity extends BaseActivity {

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.title_saveBtn)
    private TextView saveBtn;
    @ViewInject(R.id.modify_userPic)
    private ImageView userPic;
    @ViewInject(R.id.modify_userNameInput)
    private EditText userNameInput;
    @ViewInject(R.id.modify_userGender_man)
    private TextView userGenderMan;
    @ViewInject(R.id.modify_userGender_woman)
    private TextView userGenderWoman;
    @ViewInject(R.id.modify_userPhoneInput)
    private TextView userPhone;
    @ViewInject(R.id.modify_progress)
    private ProgressBar progress;
    @ViewInject(R.id.modify_versionName)
    private TextView versionName;

    private long start = 0;
    private String picPath;
    private int userSex = 0;
    private int userPicWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_modify_user);
        context = this;
        ViewUtils.inject(this);

        initActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case Passageway.IMAGE_REQUEST_CODE:
                    if (data != null) {
                        resizeImage(data.getData());
                    }
                    break;
                case Passageway.CAMERA_REQUEST_CODE:
                    if (isSdcardExisting()) {
                        resizeImage(getImageUri());
                    } else {
                        MessageHandler.showToast(context, "找不到SD卡");
                    }
                    break;
                case Passageway.RESULT_REQUEST_CODE:
                    if (data != null) {
                        getResizeImage(data);
                    }
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.title_backBtn, R.id.modify_userPic, R.id.title_saveBtn,
            R.id.modify_userGender_man, R.id.modify_userGender_woman, R.id.modify_logoutBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.modify_userPic:
                showPicList();
                break;
            case R.id.title_saveBtn:
                saveUserMessage();
                break;
            case R.id.modify_userGender_man:
                setUserSex(1);
                break;
            case R.id.modify_userGender_woman:
                setUserSex(0);
                break;
            case R.id.modify_logoutBtn:
                UserObjHandler.logout(context);
                Passageway.jumpToActivity(context, MainActivity.class);
                break;
        }
    }

    private void saveUserMessage() {
        progress.setVisibility(View.VISIBLE);
        File f = getImageFile();
        if (f.exists()) {
            AVFileHandler.uploadFile(f, new AVFileHandler.AVSaveCallback() {
                @Override
                public void callnack(AVException e, AVFile file) {
                    if (e == null) {
                        UserObjHandler.saveUserAvatar(context, file.getUrl());
                        uploadMessage(file.getObjectId());
                    }
                }
            });
        } else {
            uploadMessage(UserObjHandler.getUserAvatarId(context));
        }

    }

    private void uploadMessage(String s) {
        String url = UrlHandler.getUserInfo();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("username", TextHandeler.getText(userNameInput));
        params.addBodyParameter("sex", String.valueOf(userSex));
        params.addBodyParameter("avatar", s);


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
//                                UserObjHandler.saveUser(context, UserObjHandler.getUserObj(JsonHandle.getJSON(json, "result")));
                                UserObjHandler.saveUserName(context, TextHandeler.getText(userNameInput));
                                UserObjHandler.saveUserSex(context, userSex);
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
        saveBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("会员资料");
        userPicWidth = WinTool.getWinWidth(context) / 3;

        String version;
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            version = packInfo.versionName;

        } catch (Exception e) {
            version = "";
        }
        versionName.setText(version);
        initUserMessage();
    }

    private void initUserMessage() {
        userPhone.setText(UserObj.getGoneMobilePhoneNumber(UserObjHandler.getUserMobile(context)));
        userNameInput.setText(UserObjHandler.getUserName(context));
        setUserSex(UserObjHandler.getUserSex(context));
        setUserAvatar();
    }

    private void setUserAvatar() {
        userPic.setLayoutParams(new LinearLayout.LayoutParams(userPicWidth, userPicWidth));
        UserObjHandler.setUserAvatar(context, userPic, userPicWidth / 2);
    }

    private void setUserSex(int s) {
        userSex = s;
        initUserSex();
        if (UserObjHandler.isMan(s)) {
            userGenderMan.setTextColor(ColorHandle.getColorForID(context, R.color.text_blue));
            userGenderMan.setBackgroundResource(R.drawable.on_gender_box);
            if (UserObjHandler.getUserAvatar(context).equals("") && picPath == null) {
                userPic.setImageResource(R.drawable.man_pic_icon);
            }
        } else {
            userGenderWoman.setTextColor(ColorHandle.getColorForID(context, R.color.text_blue));
            userGenderWoman.setBackgroundResource(R.drawable.on_gender_box);
            if (UserObjHandler.getUserAvatar(context).equals("") && picPath == null) {
                userPic.setImageResource(R.drawable.woman_pic_icon);
            }
        }
    }

    private void initUserSex() {
        userGenderMan.setTextColor(ColorHandle.getColorForID(context, R.color.text_gary_02));
        userGenderMan.setBackgroundResource(R.drawable.off_gender_box);
        userGenderWoman.setTextColor(ColorHandle.getColorForID(context, R.color.text_gary_02));
        userGenderWoman.setBackgroundResource(R.drawable.off_gender_box);
    }

    private void showPicList() {
        start = DateHandle.getTime();
        final List<String> msgList = getMsgList();
        final ListDialog dialog = new ListDialog(context);
        dialog.setTitleGone();
        dialog.setList(msgList);
        dialog.setItemListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (msgList.get(position).equals("拍照")) {
                    takePhoto();
                } else {
                    selectImage();
                }
                dialog.dismiss();
            }

        });
    }

    private void takePhoto() {
        picPath = Passageway.takePhoto(context);
    }

    private void selectImage() {
        Passageway.selectImage(context);
    }

    public List<String> getMsgList() {
        List<String> list = new ArrayList<String>();
        list.add("拍照");
        list.add("本地相册");
        return list;
    }

    public void resizeImage(Uri uri) {
        Passageway.resizeImage(context, uri);
    }

    private Uri getImageUri() {
        return Uri.fromFile(new File(DownloadImageLoader.getImagePath(),
                picPath));
    }

    private boolean isSdcardExisting() {
        final String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    private File getImageFile() {
        String name = "head_" + start + ".jpg";
        return new File(DownloadImageLoader.getImagePath(), name);
    }

    private void getResizeImage(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            FileOutputStream foutput = null;
            try {
                foutput = new FileOutputStream(getImageFile());
                photo.compress(Bitmap.CompressFormat.JPEG, 100, foutput);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (null != foutput) {
                    try {
                        foutput.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            DownloadImageLoader.loadImageForFile(userPic, getImageFile()
                    .toString(), userPicWidth / 2);
        }
    }

}
