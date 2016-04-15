package com.itkc_carlife.activitys;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.baidu.location.BDLocation;
import com.itkc_carlife.R;
import com.itkc_carlife.box.handler.UserCarInfoObjHandler;
import com.itkc_carlife.box.handler.UserObjHandler;
import com.itkc_carlife.dialogs.ListDialog;
import com.itkc_carlife.download.DownloadImageLoader;
import com.itkc_carlife.handlers.DateHandle;
import com.itkc_carlife.handlers.JsonHandle;
import com.itkc_carlife.handlers.LicensePlateHandler;
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
public class InputCarDataActivity extends BaseActivity {

    private final static int DRIVING = 1;
    private final static int DRIVING_VICE = 2;

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.inputCarData_provincesText)
    private TextView provincesText;
    @ViewInject(R.id.inputCarData_cityCodeText)
    private TextView cityCodeText;
    @ViewInject(R.id.inputCarData_drivingPic)
    private ImageView drivingPic;
    @ViewInject(R.id.inputCarData_drivingVicePic)
    private ImageView drivingVicePic;
    @ViewInject(R.id.inputCarData_carLicenseInput)
    private EditText carLicenseInput;
    @ViewInject(R.id.inputCarData_engineInput)
    private EditText engineInput;
    @ViewInject(R.id.inputCarData_frameInput)
    private EditText frameInput;
    @ViewInject(R.id.inputCarData_Progress)
    private ProgressBar progress;
    @ViewInject(R.id.inputCarData_cityText)
    private TextView cityText;

    private String provinces = "", cityCode = "";
    private long deivingStart = 0, deivingViceStart = 0;
    private String deivingPath, deivingVicePath;
    private int where;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_input_cardata);
        context = this;
        ViewUtils.inject(this);

        initActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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
            case ResultActivity.REQUEST_CODE:
                finish();
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.title_backBtn, R.id.inputCarData_provincesText, R.id.inputCarData_cityCodeText, R.id.inputCarData_drivingPic, R.id.inputCarData_drivingVicePic,
            R.id.inputCarData_uploadBtn, R.id.inputCarData_cityText})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.inputCarData_provincesText:
                showProvincesDialog();
                break;
            case R.id.inputCarData_cityCodeText:
                showCityCodeDialog();
                break;
            case R.id.inputCarData_drivingPic:
                showPicList(DRIVING);
                break;
            case R.id.inputCarData_drivingVicePic:
                showPicList(DRIVING_VICE);
                break;
            case R.id.inputCarData_uploadBtn:
                upload();
                break;
            case R.id.inputCarData_cityText:
                downloadCity();
                break;
        }
    }

    private void initCity() {
        progress.setVisibility(View.VISIBLE);
        MapHandler.getAddress(context, new MapHandler.MapListener() {
            @Override
            public void callback(BDLocation location) {
                progress.setVisibility(View.GONE);
                cityText.setText(location.getCity());
            }
        });
    }

    private void downloadCity() {
        progress.setVisibility(View.VISIBLE);

        String url = UrlHandler.getDictCity();

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
                                    showCityList(array);
                                }
                            }
                        }
                    }

                });

    }

    private void showCityList(JSONArray array) {
        final List<String> cityList = new ArrayList<>(array.length());
        for (int i = 0; i < array.length(); i++) {
            cityList.add(JsonHandle.getString(array, i));
        }
        final ListDialog dialog = new ListDialog(context);
        dialog.setTitleGone();
        dialog.setList(cityList);
        dialog.setLayout();
        dialog.setItemListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cityText.setText(cityList.get(position));
                dialog.dismiss();
            }
        });
    }

    private void upload() {
        if (isThrough()) {
            uploadDrivingImage();
        }
    }

    private void uploadDrivingImage() {
        progress.setVisibility(View.VISIBLE);
        File f = getDrivingImageFile();
        if (!f.exists()) {
            uploadDrivingViceImage(UserCarInfoObjHandler.getDrivingImageId(context));
        } else {
            AVFileHandler.uploadFile(f, new AVFileHandler.AVSaveCallback() {
                @Override
                public void callnack(AVException e, AVFile file) {
                    if (e == null) {
                        UserCarInfoObjHandler.saveDrivingImage(context, file.getUrl());
                        UserCarInfoObjHandler.saveDrivingImageId(context, file.getObjectId());
                        uploadDrivingViceImage(file.getObjectId());
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void uploadDrivingViceImage(final String drivingImageId) {
        progress.setVisibility(View.VISIBLE);
        File f = getDrivingViceImageFile();
        if (!f.exists()) {
            uploadMessage(drivingImageId, UserCarInfoObjHandler.getDrivingViewImageId(context));

        } else {
            AVFileHandler.uploadFile(f, new AVFileHandler.AVSaveCallback() {
                @Override
                public void callnack(AVException e, AVFile file) {
                    if (e == null) {
                        UserCarInfoObjHandler.saveDrivingVideImage(context, file.getUrl());
                        uploadMessage(drivingImageId, file.getObjectId());
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void showUploadDialog(boolean b, String message) {
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        bundle.putBoolean("isStatus", b);
        Passageway.jumpActivity(context, ResultActivity.class, ResultActivity.REQUEST_CODE, bundle);
    }

    private void uploadMessage(final String drivingImageId, final String drivingVideImageId) {
        String url = UrlHandler.getUserCarInfo();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("car_number", provinces + cityCode + "-" + TextHandeler.getText(carLicenseInput));
        params.addBodyParameter("engine_number", TextHandeler.getText(engineInput));
        params.addBodyParameter("frame_number", TextHandeler.getText(frameInput));
        params.addBodyParameter("city", TextHandeler.getText(cityText));
        params.addBodyParameter("major_car_license", drivingImageId);
        params.addBodyParameter("secondary_car_license", drivingVideImageId);

        HttpUtilsBox.getHttpUtil().send(HttpMethod.POST, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception, String msg) {
                        progress.setVisibility(View.GONE);
//                        MessageHandler.showFailure(context);
                        showUploadDialog(false, "网络不佳");
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
                                UserCarInfoObjHandler.saveCarInfo(context, provinces, cityCode, TextHandeler.getText(carLicenseInput),
                                        TextHandeler.getText(engineInput), TextHandeler.getText(frameInput), TextHandeler.getText(cityText));
//                                MessageHandler.showToast(context, "上传成功，等待审核");
//                                finish();
                                showUploadDialog(true, "");
                            } else {
                                showUploadDialog(false, JsonHandle.getString(json, "result"));
//                                MessageHandler.showToast(context, JsonHandle.getString(json, "result"));
                            }
                        }
                    }

                });
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("车辆资料");
        initCarInfoMessage();
    }

    private void initCarInfoMessage() {
        setLicnsePlateView();
        carLicenseInput.setText(UserCarInfoObjHandler.getCarNumber(context));
        engineInput.setText(UserCarInfoObjHandler.getEnginerNumber(context));
        frameInput.setText(UserCarInfoObjHandler.getFrameNumber(context));
        String di = UserCarInfoObjHandler.getDrivingImage(context);
        String dvi = UserCarInfoObjHandler.getDrivingVideImage(context);
        if (!di.equals("")) {
            DownloadImageLoader.loadImage(drivingPic, di);
        }
        if (!dvi.equals("")) {
            DownloadImageLoader.loadImage(drivingVicePic, dvi);
        }

        String c = UserCarInfoObjHandler.getCity(context);
        if (c.equals("")) {
            initCity();
        } else {
            cityText.setText(c);
        }

    }

    private void setLicnsePlateView() {
        setLicnsePlateView(LicensePlateHandler.getProvinceText(context), LicensePlateHandler.getCityCodeText(context));
    }

    private void setLicnsePlateView(String provinces, String cityCode) {
        this.provinces = provinces;
        this.cityCode = cityCode;
        provincesText.setText(provinces + "  ");
        cityCodeText.setText(cityCode + "  ");
    }


    private void showCityCodeDialog() {
        if (provinces.equals("")) {
            MessageHandler.showToast(context, "请先选择省份");
        } else {
            final List<String> list = LicensePlateHandler.getCityCodeList(provinces);
            final ListDialog dialog = new ListDialog(context);
            dialog.setList(list);
            dialog.setTitleGone();
            dialog.setItemListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    setLicnsePlateView(provinces, list.get(position));
                    dialog.dismiss();
                }
            });
        }
    }

    private void showProvincesDialog() {
        final List<String> list = LicensePlateHandler.getProvincesTextList();
        final ListDialog dialog = new ListDialog(context);
        dialog.setList(list);
        dialog.setTitleGone();
        dialog.setItemListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setLicnsePlateView(list.get(position), "");
                dialog.dismiss();
            }
        });
    }

    private void showPicList(final int where) {
        this.where = where;
        if (where == DRIVING) {
            deivingStart = DateHandle.getTime();
        } else {
            deivingViceStart = DateHandle.getTime();
        }
        final List<String> msgList = getMsgList();
        final ListDialog dialog = new ListDialog(context);
        dialog.setTitleGone();
        dialog.setList(msgList);
        dialog.setItemListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (msgList.get(position).equals("拍照")) {
                    takePhoto(where);
                } else {
                    selectImage();
                }
                dialog.dismiss();
            }

        });
    }

    private void takePhoto(int w) {
        if (w == DRIVING) {
            deivingPath = Passageway.takePhoto(context);
        } else {
            deivingVicePath = Passageway.takePhoto(context);
        }
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

    private boolean isSdcardExisting() {
        final String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    private File getImageUri() {
        if (where == DRIVING) {
            return new File(DownloadImageLoader.getImagePath(),
                    deivingPath);
        } else {
            return new File(DownloadImageLoader.getImagePath(),
                    deivingVicePath);
        }
    }

    private File getImageFile() {
        if (where == DRIVING) {
            return getDrivingImageFile();
        } else {
            return getDrivingViceImageFile();
        }
    }

    public File getDrivingViceImageFile() {
        String name = "deiving_vice_" + deivingViceStart + ".jpg";
        return new File(DownloadImageLoader.getImagePath(), name);
    }

    public File getDrivingImageFile() {
        String name = "deiving_" + deivingStart + ".jpg";
        return new File(DownloadImageLoader.getImagePath(), name);
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
        if (where == DRIVING) {
            DownloadImageLoader.loadImageForFile(drivingPic, file.toString());
//            DownloadImageLoader.loadImageForFile(drivingPic, getImageFile()
//                    .toString());
        } else {
            DownloadImageLoader.loadImageForFile(drivingVicePic, file.toString());
//            DownloadImageLoader.loadImageForFile(drivingVicePic, getImageFile()
//                    .toString());
        }
    }

    public boolean isThrough() {
        if (provinces.equals("") || cityCode.equals("") || TextHandeler.getText(carLicenseInput).equals("")) {
            MessageHandler.showToast(context, "请填写车牌号码");
            return false;
        }
        if (TextHandeler.getText(carLicenseInput).length() != 5) {
            MessageHandler.showToast(context, "请填写5位车牌号码");
            return false;
        }
        if (TextHandeler.getText(engineInput).equals("")) {
            MessageHandler.showToast(context, "请填写发动机号");
            return false;
        }
        if (TextHandeler.getText(engineInput).length() != 6) {
            MessageHandler.showToast(context, "请填写6位发动机号");
            return false;
        }
        if (TextHandeler.getText(frameInput).equals("")) {
            MessageHandler.showToast(context, "请填写车架号");
            return false;
        }
        if (TextHandeler.getText(frameInput).length() != 6) {
            MessageHandler.showToast(context, "请填写6位车架号");
            return false;
        }
        if (TextHandeler.getText(cityText).equals("")) {
            MessageHandler.showToast(context, "请选择所在城市");
            return false;
        }
        if (!getDrivingImageFile().exists() && UserCarInfoObjHandler.getDrivingImageId(context).equals("")) {
            MessageHandler.showToast(context, "请选择行驶证照片");
            return false;
        }
        if (!getDrivingViceImageFile().exists() && UserCarInfoObjHandler.getDrivingViewImageId(context).equals("")) {
            MessageHandler.showToast(context, "请选择行驶证副证照片");
            return false;
        }
        return true;
    }
}
