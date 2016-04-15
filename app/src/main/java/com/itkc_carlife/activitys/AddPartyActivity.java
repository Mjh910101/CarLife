package com.itkc_carlife.activitys;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.baidu.location.BDLocation;
import com.itkc_carlife.R;
import com.itkc_carlife.box.handler.UserObjHandler;
import com.itkc_carlife.dialogs.ListDialog;
import com.itkc_carlife.download.DownloadImageLoader;
import com.itkc_carlife.handlers.DateHandle;
import com.itkc_carlife.handlers.JsonHandle;
import com.itkc_carlife.handlers.MapHandler;
import com.itkc_carlife.handlers.MessageHandler;
import com.itkc_carlife.handlers.TextHandeler;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
public class AddPartyActivity extends BaseActivity {

    public final static int REQUEST_CODE = 100;
    public final static String IS_UPLOAD = "upload";

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.addParty_titleInput)
    private EditText titleInput;
    @ViewInject(R.id.addParty_typeInput)
    private TextView typeInput;
    @ViewInject(R.id.addParty_startDayInput)
    private TextView startDayInput;
    @ViewInject(R.id.addParty_startTimeInput)
    private TextView startTimeInput;
    @ViewInject(R.id.addParty_endDayInput)
    private TextView endDayInput;
    @ViewInject(R.id.addParty_endTimeInput)
    private TextView endTimeInput;
    @ViewInject(R.id.addParty_addressInput)
    private EditText addressInput;
    @ViewInject(R.id.addParty_peopleInput)
    private EditText peopleInput;
    @ViewInject(R.id.addParty_descriptInput)
    private EditText descriptInput;
    @ViewInject(R.id.addParty_progress)
    private ProgressBar progress;
    @ViewInject(R.id.addParty_addPic)
    private ImageView addPic;

    private List<String> typeList;
    private long start = 0;
    private String path;
    private double latitude = 0, longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_party);
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
                        getResizeImage(data.getData());
                    }
                    break;
                case Passageway.CAMERA_REQUEST_CODE:
                    if (isSdcardExisting()) {
                        getResizeImage(getImageUri());
                    } else {
                        MessageHandler.showToast(context, "找不到SD卡");
                    }
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.title_backBtn, R.id.addParty_uploadBtn, R.id.addParty_typeInput,
            R.id.addParty_startDayInput, R.id.addParty_startTimeInput, R.id.addParty_endDayInput,
            R.id.addParty_endTimeInput, R.id.addParty_addPic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.addParty_uploadBtn:
                upload();
                break;
            case R.id.addParty_typeInput:
                showTypeList();
                break;
            case R.id.addParty_startDayInput:
                showDateDialog(startDayInput, 0);
                break;
            case R.id.addParty_startTimeInput:
                showTimeDialog(startTimeInput);
                break;
            case R.id.addParty_endDayInput:
                showDateDialog(endDayInput, 1);
                break;
            case R.id.addParty_endTimeInput:
                showTimeDialog(endTimeInput);
                break;
            case R.id.addParty_addPic:
                showPicList();
                break;
        }
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

    private void getResizeImage(Uri uri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            String img_path = actualimagecursor.getString(actual_image_column_index);
            File f = new File(img_path);
            getResizeImage(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isSdcardExisting() {
        final String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    private File getImageUri() {
        return new File(DownloadImageLoader.getImagePath(),
                path);
    }

    private void getResizeImage(File f) {
        if (f.exists()) {
            try {
                FileInputStream fis = new FileInputStream(f.toString());
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                saveImage(bitmap, f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveImage(Bitmap photo, File file) {
        Drawable drawable = new BitmapDrawable(photo);
        FileOutputStream foutput = null;
        File f = getImageFile();
        try {
            foutput = new FileOutputStream(f);
            photo.compress(Bitmap.CompressFormat.JPEG, 40, foutput);
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
        try {
            ExifInterface formFile = new ExifInterface(file.toString());
            ExifInterface toFile = new ExifInterface(f.toString());

            toFile.setAttribute(ExifInterface.TAG_ORIENTATION, formFile.getAttribute(ExifInterface.TAG_ORIENTATION));
            toFile.saveAttributes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DownloadImageLoader.loadImageForFile(addPic, file.toString());
    }

    public File getImageFile() {
        String name = "party_" + start + ".jpg";
        return new File(DownloadImageLoader.getImagePath(), name);
    }

    private void takePhoto() {
        path = Passageway.takePhoto(context);
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

    private void showTimeDialog(final TextView textView) {
        Calendar c = DateHandle.getToday();
        TimePickerDialog dialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String h;
                if (hourOfDay < 10) {
                    h = "0" + String.valueOf(hourOfDay);
                } else {
                    h = String.valueOf(hourOfDay);
                }
                String m;
                if (minute < 10) {
                    m = "0" + String.valueOf(minute);
                } else {
                    m = String.valueOf(minute);
                }

                textView.setText(h + ":" + m);
            }
        }, 0, 0, true);
        dialog.show();

    }

    private void showDateDialog(final TextView textView, int d) {
        Calendar c = DateHandle.getToday();
        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                textView.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, DateHandle.getYear(c), DateHandle.getMonth(c), (DateHandle.getDay(c) + d));

        dialog.show();

    }

    private void showTypeList() {
        final ListDialog dialog = new ListDialog(context);
        dialog.setTitleGone();
        dialog.setList(typeList);
        dialog.setItemListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeInput.setText(typeList.get(position));
                dialog.dismiss();
            }
        });
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("活动发布");
        downloadType();
        MapHandler.getAddress(context, new MapHandler.MapListener() {
            @Override
            public void callback(BDLocation location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        });
    }


    private void downloadType() {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getDictPostType();

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
                                    initTypeList(array);
                                }
                            }
                        }
                    }

                });
    }

    private void initTypeList(JSONArray array) {
        typeList = new ArrayList<>(array.length());
        for (int i = 0; i < array.length(); i++) {
            typeList.add(JsonHandle.getString(array, i));
        }
    }

    public boolean isThrough() {
        if (TextHandeler.getText(titleInput).equals("")) {
            MessageHandler.showToast(context, "活动标题不能为空");
            return false;
        }
        if (TextHandeler.getText(titleInput).length() > 45) {
            MessageHandler.showToast(context, "活动标题不能多于45个字符");
            return false;
        }
        if (TextHandeler.getText(typeInput).equals("")) {
            MessageHandler.showToast(context, "活动类型不能为空");
            return false;
        }
        if (TextHandeler.getText(addressInput).equals("")) {
            MessageHandler.showToast(context, "活动地址不能为空");
            return false;
        }
        if (TextHandeler.getText(peopleInput).equals("")) {
            MessageHandler.showToast(context, "活动人数不能为空");
            return false;
        }
        if (Integer.valueOf(TextHandeler.getText(peopleInput)) <= 0) {
            MessageHandler.showToast(context, "活动人数不能为0");
            return false;
        }
        if (TextHandeler.getText(descriptInput).equals("")) {
            MessageHandler.showToast(context, "活动介绍不能为空");
            return false;
        }
        if (TextHandeler.getText(startDayInput).equals("        ") || TextHandeler.getText(startTimeInput).equals("        ")) {
            MessageHandler.showToast(context, "活动开始时间不能为空");
            return false;
        }
        if (TextHandeler.getText(endDayInput).equals("        ") || TextHandeler.getText(endTimeInput).equals("        ")) {
            MessageHandler.showToast(context, "活动结束时间不能为空");
            return false;
        }
        if (!getImageFile().exists()) {
            MessageHandler.showToast(context, "请添加活动图片");
            return false;
        }
//        try {
//            String[] startDay = TextHandeler.getText(startDayInput).split("-");
//            String[] endDay = TextHandeler.getText(endDayInput).split("-");
//            for (int i = 0; i < 3; i++) {
//                if (Integer.valueOf(startDay[i]) < Integer.valueOf(endDay[i])) {
//                    MessageHandler.showToast(context, "活动时间不正确");
//                    return false;
//                }
//            }
//
//            String[] startTime = TextHandeler.getText(startTimeInput).split(":");
//            String[] endTime = TextHandeler.getText(endTimeInput).split(":");
//            for (int i = 0; i < 2; i++) {
//                if (Integer.valueOf(startTime[i]) < Integer.valueOf(endTime[i])) {
//                    MessageHandler.showToast(context, "活动时间不正确");
//                    return false;
//                }
//            }
//        } catch (Exception e) {
//            MessageHandler.showToast(context, "活动时间不正确");
//            return false;
//        }
        return true;
    }

    private void upload() {
        if (isThrough()) {
            uploadPic();
        }
    }

    private void uploadPic() {
        progress.setVisibility(View.VISIBLE);
        AVFileHandler.uploadFile(getImageFile(), new AVFileHandler.AVSaveCallback() {
            @Override
            public void callnack(AVException e, AVFile file) {
                if (e == null) {
                    uploadMessage(file.getObjectId());
                }
            }
        });
    }

    private void uploadMessage(String id) {
        String url = UrlHandler.getUserPostAdd();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("title", TextHandeler.getText(titleInput));
        params.addBodyParameter("type", TextHandeler.getText(typeInput));
        params.addBodyParameter("address", TextHandeler.getText(addressInput));
        params.addBodyParameter("limit", TextHandeler.getText(peopleInput));
        params.addBodyParameter("descript", TextHandeler.getText(descriptInput));
        params.addBodyParameter("cover", id);
        params.addBodyParameter("latitude", String.valueOf(latitude));
        params.addBodyParameter("longitude", String.valueOf(longitude));
        params.addBodyParameter("startTime", TextHandeler.getText(startDayInput) + " " + TextHandeler.getText(startTimeInput));
        params.addBodyParameter("endTime", TextHandeler.getText(endDayInput) + " " + TextHandeler.getText(endTimeInput));

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
                                MessageHandler.showToast(context, "发布成功");
                                colse();
                            } else {
                                MessageHandler.showToast(context, JsonHandle.getString(json, "result"));
                            }
                        }
                    }

                });
    }

    private void colse() {
        Intent i = new Intent();
        Bundle b = new Bundle();
        b.putBoolean(IS_UPLOAD, true);
        i.putExtras(b);
        setResult(REQUEST_CODE, i);
        finish();
    }
}
