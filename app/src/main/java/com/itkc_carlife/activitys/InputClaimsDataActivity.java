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
import com.itkc_carlife.R;
import com.itkc_carlife.box.handler.UserClaimsInfoObjHandler;
import com.itkc_carlife.box.handler.UserObjHandler;
import com.itkc_carlife.dialogs.ListDialog;
import com.itkc_carlife.download.DownloadImageLoader;
import com.itkc_carlife.handlers.DateHandle;
import com.itkc_carlife.handlers.JsonHandle;
import com.itkc_carlife.handlers.MessageHandler;
import com.itkc_carlife.handlers.RegexHandler;
import com.itkc_carlife.handlers.TextHandeler;
import com.itkc_carlife.http.AVFileHandler;
import com.itkc_carlife.http.HttpUtilsBox;
import com.itkc_carlife.http.UrlHandler;
import com.itkc_carlife.tool.IdcardValidator;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * Created by Hua on 16/1/6.
 */
public class InputClaimsDataActivity extends BaseActivity {

    private final static int DRIVING = 1;
    private final static int DRIVING_VICE = 2;
    private final static int PEOPLE = 3;
    private final static int BACK = 4;
    private final static int POSITIVE = 5;

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.claimsData_driverCard)
    private ImageView driverCard;
    @ViewInject(R.id.claimsData_driverViceCard)
    private ImageView driverViceCard;
    @ViewInject(R.id.claimsData_idcardPeople)
    private ImageView idcardPeople;
    @ViewInject(R.id.claimsData_idcardBack)
    private ImageView idcardBack;
    @ViewInject(R.id.claimsData_idcardPositive)
    private ImageView idcardPositive;
    @ViewInject(R.id.claimsData_companyInput)
    private EditText companyInput;
    @ViewInject(R.id.claimsData_codeInput)
    private EditText codeInput;
    @ViewInject(R.id.claimsData_nameInput)
    private EditText nameInput;
    @ViewInject(R.id.claimsData_idInput)
    private EditText idInput;
    @ViewInject(R.id.claimsData_progress)
    private ProgressBar progress;

    private long driverStart = 0, driverViceStart = 0, positiveStart = 0, backStart = 0, peopleStart = 0;
    private String driverPath = "", driverVicePath = "", positivePath = "", backPath = "", peoplePath = "";
    private int where;

    private Map<Integer, String> idMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_input_claimsdata);
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

    @OnClick({R.id.title_backBtn, R.id.claimsData_driverCard, R.id.claimsData_driverViceCard, R.id.claimsData_idcardPeople,
            R.id.claimsData_idcardBack, R.id.claimsData_idcardPositive, R.id.claimsData_uploadBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.claimsData_driverCard:
                showPicList(DRIVING);
                break;
            case R.id.claimsData_driverViceCard:
                showPicList(DRIVING_VICE);
                break;
            case R.id.claimsData_idcardPeople:
                showPicList(PEOPLE);
                break;
            case R.id.claimsData_idcardBack:
                showPicList(BACK);
                break;
            case R.id.claimsData_idcardPositive:
                showPicList(POSITIVE);
                break;
            case R.id.claimsData_uploadBtn:
                upload();
                break;
        }
    }

    private void upload() {
        if (isThrough()) {
            uploadDriverImage();
        }
    }

    private void uploadDriverImage() {
        progress.setVisibility(View.VISIBLE);
        File f = getImageFile(DRIVING);
        if (!f.exists()) {
            uploadDrivingViceImage();
        } else {
            AVFileHandler.uploadFile(f, new AVFileHandler.AVSaveCallback() {
                @Override
                public void callnack(AVException e, AVFile file) {
                    if (e == null) {
                        UserClaimsInfoObjHandler.saveDriverImage(context, file.getUrl());
                        UserClaimsInfoObjHandler.saveDriverImageId(context, file.getObjectId());
                        idMap.put(DRIVING, file.getObjectId());
                        uploadDrivingViceImage();
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void uploadDrivingViceImage() {
        File f = getImageFile(DRIVING_VICE);
        if (!f.exists()) {
            uploadPeopleImage();
        } else {
            AVFileHandler.uploadFile(f, new AVFileHandler.AVSaveCallback() {
                @Override
                public void callnack(AVException e, AVFile file) {
                    if (e == null) {
                        UserClaimsInfoObjHandler.saveDriverViceImage(context, file.getUrl());
                        UserClaimsInfoObjHandler.saveDriverViceImageId(context, file.getObjectId());
                        idMap.put(DRIVING_VICE, file.getObjectId());
                        uploadPeopleImage();
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void uploadPeopleImage() {
        File f = getImageFile(PEOPLE);
        if (!f.exists()) {
            uploadBackImage();
        } else {
            AVFileHandler.uploadFile(f, new AVFileHandler.AVSaveCallback() {
                @Override
                public void callnack(AVException e, AVFile file) {
                    if (e == null) {
                        UserClaimsInfoObjHandler.savePeopleImage(context, file.getUrl());
                        UserClaimsInfoObjHandler.savePeopleImageId(context, file.getObjectId());
                        idMap.put(PEOPLE, file.getObjectId());
                        uploadBackImage();
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void uploadBackImage() {
        File f = getImageFile(BACK);
        if (!f.exists()) {
            uploadPositiveImage();
        } else {
            AVFileHandler.uploadFile(f, new AVFileHandler.AVSaveCallback() {
                @Override
                public void callnack(AVException e, AVFile file) {
                    if (e == null) {
                        UserClaimsInfoObjHandler.saveBackImage(context, file.getUrl());
                        UserClaimsInfoObjHandler.saveBackImageId(context, file.getObjectId());
                        idMap.put(BACK, file.getObjectId());
                        uploadPositiveImage();
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void uploadPositiveImage() {
        File f = getImageFile(POSITIVE);
        if (!f.exists()) {
            uploadMessage();
        } else {
            AVFileHandler.uploadFile(f, new AVFileHandler.AVSaveCallback() {
                @Override
                public void callnack(AVException e, AVFile file) {
                    if (e == null) {
                        UserClaimsInfoObjHandler.savePositiveImage(context, file.getUrl());
                        UserClaimsInfoObjHandler.savePositiveImageId(context, file.getObjectId());
                        idMap.put(POSITIVE, file.getObjectId());
                        uploadMessage();
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

    private void uploadMessage() {
        String url = UrlHandler.getUserInsuranceInfo();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("insurance_company", TextHandeler.getText(companyInput));
        params.addBodyParameter("insurance_number", TextHandeler.getText(codeInput));
        params.addBodyParameter("insurance_realname", TextHandeler.getText(nameInput));
        params.addBodyParameter("insurance_id_card_number", TextHandeler.getText(idInput));
        params.addBodyParameter("insurance_id_card_front", idMap.get(POSITIVE));
        params.addBodyParameter("insurance_id_card_back", idMap.get(BACK));
        params.addBodyParameter("insurance_id_card_in_hand", idMap.get(PEOPLE));
        params.addBodyParameter("major_driving_license", idMap.get(DRIVING));
        params.addBodyParameter("secondary_driving_license", idMap.get(DRIVING_VICE));

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
                                UserClaimsInfoObjHandler.saveClaimsInfo(context, TextHandeler.getText(companyInput),
                                        TextHandeler.getText(codeInput), TextHandeler.getText(nameInput), TextHandeler.getText(idInput));
//                                MessageHandler.showToast(context, "上传成功，等待审核");
//                                finish();
                                showUploadDialog(true, "");
                            } else {
//                                MessageHandler.showToast(context, JsonHandle.getString(json, "result"));
                                showUploadDialog(false, JsonHandle.getString(json, "result"));

                            }
                        }
                    }

                });
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("理赔资料");
        idMap = new HashMap<Integer, String>();
        initClaimsMessage();
    }

    private void initClaimsMessage() {
        companyInput.setText(UserClaimsInfoObjHandler.getCompany(context));
        codeInput.setText(UserClaimsInfoObjHandler.getCode(context));
        nameInput.setText(UserClaimsInfoObjHandler.getName(context));
        idInput.setText(UserClaimsInfoObjHandler.getIdCardId(context));

        String di = UserClaimsInfoObjHandler.getDriverImage(context);
        String dvi = UserClaimsInfoObjHandler.getDriverVideImage(context);
        String pei = UserClaimsInfoObjHandler.getPeopleImage(context);
        String bi = UserClaimsInfoObjHandler.getBackImage(context);
        String pi = UserClaimsInfoObjHandler.getPositiveImage(context);

        if (!di.equals("")) {
            DownloadImageLoader.loadImage(driverCard, di);
            idMap.put(DRIVING, UserClaimsInfoObjHandler.getDriverImageId(context));
        }
        if (!dvi.equals("")) {
            DownloadImageLoader.loadImage(driverViceCard, dvi);
            idMap.put(DRIVING_VICE, UserClaimsInfoObjHandler.getDriverViceImageId(context));

        }
        if (!pei.equals("")) {
            DownloadImageLoader.loadImage(idcardPeople, pei);
            idMap.put(PEOPLE, UserClaimsInfoObjHandler.getPeopleImageId(context));
        }
        if (!bi.equals("")) {
            DownloadImageLoader.loadImage(idcardBack, bi);
            idMap.put(BACK, UserClaimsInfoObjHandler.getBackImageId(context));
        }
        if (!pi.equals("")) {
            DownloadImageLoader.loadImage(idcardPositive, pi);
            idMap.put(POSITIVE, UserClaimsInfoObjHandler.getPositiveImageId(context));
        }

    }

    private void showPicList(final int where) {
        this.where = where;
        setStart(where, DateHandle.getTime());
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
        String p = Passageway.takePhoto(context);
        setPath(w, p);
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
        return new File(DownloadImageLoader.getImagePath(),
                getPath(where));
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
        File f = getImageFile(where);
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
        setImage(where, file);
    }


    public void setImage(int w, File image) {
//        File image = getImageFile(w);
        switch (w) {
            case DRIVING:
                DownloadImageLoader.loadImageForFile(driverCard, image.toString());
                break;
            case DRIVING_VICE:
                DownloadImageLoader.loadImageForFile(driverViceCard, image.toString());
                break;
            case PEOPLE:
                DownloadImageLoader.loadImageForFile(idcardPeople, image.toString());
                break;
            case BACK:
                DownloadImageLoader.loadImageForFile(idcardBack, image.toString());
                break;
            case POSITIVE:
                DownloadImageLoader.loadImageForFile(idcardPositive, image.toString());
                break;
        }
    }

    private File getImageFile(int w) {
        String name = "";
        switch (w) {
            case DRIVING:
                name = "driver_" + driverStart + ".jpg";
                break;
            case DRIVING_VICE:
                name = "driver_vice_" + driverViceStart + ".jpg";
                break;
            case PEOPLE:
                name = "people_" + peopleStart + ".jpg";
                break;
            case BACK:
                name = "back_" + backStart + ".jpg";
                break;
            case POSITIVE:
                name = "positive_" + positiveStart + ".jpg";
                break;
        }
        return new File(DownloadImageLoader.getImagePath(), name);
    }

    public void setStart(int w, long start) {
        switch (w) {
            case DRIVING:
                driverStart = start;
                break;
            case DRIVING_VICE:
                driverViceStart = start;
                break;
            case PEOPLE:
                peopleStart = start;
                break;
            case BACK:
                backStart = start;
                break;
            case POSITIVE:
                positiveStart = start;
                break;
        }
    }


    private void setPath(int w, String p) {
        switch (w) {
            case DRIVING:
                driverPath = p;
                break;
            case DRIVING_VICE:
                driverVicePath = p;
                break;
            case PEOPLE:
                peoplePath = p;
                break;
            case BACK:
                backPath = p;
                break;
            case POSITIVE:
                peoplePath = p;
                break;
        }
    }

    private String getPath(int w) {
        switch (w) {
            case DRIVING:
                return driverPath;
            case DRIVING_VICE:
                return driverVicePath;
            case PEOPLE:
                return peoplePath;
            case BACK:
                return backPath;
            case POSITIVE:
                return peoplePath;
        }
        return "";
    }

    public boolean isThrough() {
        if (TextHandeler.getText(companyInput).equals("")) {
            MessageHandler.showToast(context, "请填写保险公司全称");
            return false;
        }
        if (TextHandeler.getText(codeInput).equals("")) {
            MessageHandler.showToast(context, "请填写保险单号");
            return false;
        }
//        if (TextHandeler.getText(codeInput).length() != 9) {
//            MessageHandler.showToast(context, "请输入9位保险单号");
//            return false;
//        }
        if (TextHandeler.getText(nameInput).equals("")) {
            MessageHandler.showToast(context, "请填写身份证姓名");
            return false;
        }
        if (TextHandeler.getText(idInput).equals("")) {
            MessageHandler.showToast(context, "请填写身份证号");
            return false;
        }
        if (!IdcardValidator.isValidate18Idcard(TextHandeler.getText(idInput))) {
            MessageHandler.showToast(context, "身份证格式不正确");
            return false;
        }
        if ((!getImageFile(PEOPLE).exists() && !idMap.containsKey(PEOPLE)) || (!getImageFile(BACK).exists() && !idMap.containsKey(BACK)) || (!getImageFile(POSITIVE).exists() && !idMap.containsKey(POSITIVE))) {
            MessageHandler.showToast(context, "请选择身份证照片");
            return false;
        }
        if (!getImageFile(DRIVING).exists() && !idMap.containsKey(DRIVING)) {
            MessageHandler.showToast(context, "请选择驾驶证照片");
            return false;
        }
        if (!getImageFile(DRIVING_VICE).exists() && !idMap.containsKey(DRIVING_VICE)) {
            MessageHandler.showToast(context, "请选择驾驶证副证照片");
            return false;
        }
        return true;
    }

}
