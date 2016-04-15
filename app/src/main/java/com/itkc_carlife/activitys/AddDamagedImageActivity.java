package com.itkc_carlife.activitys;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.opengl.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.itkc_carlife.R;
import com.itkc_carlife.box.handler.UserObjHandler;
import com.itkc_carlife.dialogs.ListDialog;
import com.itkc_carlife.download.DownloadImageLoader;
import com.itkc_carlife.handlers.DateHandle;
import com.itkc_carlife.handlers.JsonHandle;
import com.itkc_carlife.handlers.MessageHandler;
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
 * Created by Hua on 16/1/14.
 */
public class AddDamagedImageActivity extends BaseActivity {

    public final static int REQUEST_CODE = 101;
    public final static String WHERE_KEY = "where";
    public final static String PATH_LIST_KEY = "path";

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.addDamaged_dataGrid)
    private GridView dataGrid;
    @ViewInject(R.id.addDamaged_progress)
    private ProgressBar progress;
    @ViewInject(R.id.title_sainningIcon)
    private ImageView sainningIcon;

    private Bundle mBundle;
    private long start;
    private String settlingId, where, path, repairId, whereForm;
    private List<String> pathList;
    private List<String> idList;

    private DamagedAdapter mDamagedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_damaged_image);
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
            case ImageActivity.REQUEST_CODE:
                if (data != null) {
                    deleteImage(data.getExtras());
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.title_backBtn, R.id.addDamaged_saveBtn, R.id.title_sainningIcon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.addDamaged_saveBtn:
                uploadImage(0);
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

    private void uploadImage(final int i) {
        if (!pathList.isEmpty()) {
            progress.setVisibility(View.VISIBLE);
            Log.e("", pathList.get(i));
            File f = new File(pathList.get(i));
            AVFileHandler.uploadFile(f, new AVFileHandler.AVSaveCallback() {
                @Override
                public void callnack(AVException e, AVFile file) {
                    if (e == null) {
                        idList.add(file.getObjectId());
                        int s = i + 1;
                        if (s == pathList.size()) {
                            uploadMessage();
                        } else {
                            uploadImage(s);
                        }
                    } else {
                        progress.setVisibility(View.GONE);
                        MessageHandler.showToast(context, "保存失败");
                        e.printStackTrace();
                    }
                }
            });
        } else {
            MessageHandler.showToast(context, "最少添加一张图片");
        }
    }

    private void uploadMessage() {
        if (whereForm.equals(InputCarDamagedActivity.INSURANCE_KEY)) {
            uploadMessage(UrlHandler.getUserSettlingArea(), "settlingid", settlingId);
        } else if (whereForm.endsWith(InputCarDamagedActivity.REPAIR_KEY)) {
            uploadMessage(UrlHandler.getUserRepairArea(), "repairid", repairId);
        }
    }

    private void uploadMessage(String url, String idKey, String id) {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter(idKey, id);
        params.addBodyParameter("area", where);
        params.addBodyParameter("images", getIdList());


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
                                close();
                            } else {
                                MessageHandler.showToast(context, JsonHandle.getString(json, "result"));
                            }
                        }
                    }

                });
    }

    private void close() {
        Intent i = new Intent();
        Bundle b = new Bundle();
        b.putString(WHERE_KEY, where);
        b.putStringArrayList(PATH_LIST_KEY, (ArrayList) pathList);
        i.putExtras(b);
        setResult(REQUEST_CODE, i);
        finish();
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("车辆定损");

        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            whereForm = mBundle.getString(InputCarDamagedActivity.WHERE_KEY);
            if (whereForm.equals(InputCarDamagedActivity.INSURANCE_KEY)) {
                settlingId = mBundle.getString(SettingInputMessageActivity.SRTTLING_ID_KEY);
            } else if (whereForm.equals(InputCarDamagedActivity.REPAIR_KEY)) {
                repairId = mBundle.getString(RepairInputMessageActivity.REPAIR_ID_KEY);
            }

            if (mBundle.getBoolean(SettingInputMessageActivity.IS_THR_MAIN)) {
                sainningIcon.setVisibility(View.VISIBLE);
            }
            where = mBundle.getString(WHERE_KEY);
            pathList = mBundle.getStringArrayList(PATH_LIST_KEY);
            idList = new ArrayList<String>();
            mDamagedAdapter = new DamagedAdapter();
            dataGrid.setAdapter(mDamagedAdapter);
        } else {
            finish();
        }

    }


    private void showAddImageDailod() {
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

    private void getResizeImage(Uri uri) {
        try {
            Log.e("", uri.toString());
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
//        Drawable drawable = new BitmapDrawable(photo);
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
            Log.e("ExifInterface", formFile.getAttribute(ExifInterface.TAG_ORIENTATION));
            toFile.setAttribute(ExifInterface.TAG_ORIENTATION, formFile.getAttribute(ExifInterface.TAG_ORIENTATION));
            toFile.saveAttributes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mDamagedAdapter.addPath(f.toString());
    }

    private File getImageFile() {
        String name = "damaged_" + where + "_" + start + ".jpg";
        return new File(DownloadImageLoader.getImagePath(), name);
    }

    public String getIdList() {
        StringBuffer sb = new StringBuffer();
        for (String s : idList) {
            sb.append(s);
            sb.append(",");
        }

        if (sb.length() > 1) {
            return sb.substring(0, sb.length() - 1).toString();
        }
        return sb.toString();

    }

    private void deleteImage(Bundle b) {
        if (b != null) {
            if (b.getBoolean(ImageActivity.IS_DELETE)) {
                if (mDamagedAdapter != null) {
                    mDamagedAdapter.deleteImage(b.getString(ImageActivity.PATH));
                }
            }
        }
    }

    class DamagedAdapter extends BaseAdapter {

        String NULL = "null";
        LayoutInflater inflater;

        DamagedAdapter() {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void addPath(String s) {
            pathList.add(s);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (pathList.size() >= 6) {
                return 6;
            }
            return pathList.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            if (pathList.size() < position) {
                return pathList.get(position);
            }
            return NULL;
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
                        R.layout.layout_damaged_item, null);
                holder = new HolderView();

                holder.deleteIcon = (ImageView) convertView.findViewById(R.id.damagedItem_deleteIcon);
                holder.image = (ImageView) convertView.findViewById(R.id.damagedItem_image);
                holder.addImage = (ImageView) convertView.findViewById(R.id.damagedItem_addImage);

                convertView.setTag(holder);
            } else {
                holder = (HolderView) convertView.getTag();
            }

            String path;
            if (pathList.size() > position) {
                path = pathList.get(position);
            } else {
                path = NULL;
            }
            Log.e("", "size : " + pathList.size() + " , path : " + position + " : " + path);
            setView(holder, path);
            setOnClickImage(holder.image, path);
            setOnClickAddImage(holder.addImage);
            setDeleteImage(holder.deleteIcon, path);
            return convertView;
        }


        private void setDeleteImage(ImageView deleteIcon, final String path) {
            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteImage(path);
                }
            });
        }

        private void deleteImage(String path) {
            pathList.remove(path);
            notifyDataSetChanged();
        }

        private void setOnClickAddImage(ImageView view) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAddImageDailod();
                }
            });
        }

        private void setOnClickImage(ImageView image, final String path) {
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putString(ImageActivity.PATH, path);
                    Passageway.jumpActivity(context, ImageActivity.class, ImageActivity.REQUEST_CODE, b);
                }
            });
        }

        private void setView(HolderView holder, String path) {
            int w = WinTool.getWinWidth(context) / 6;
            holder.image.setLayoutParams(new RelativeLayout.LayoutParams(w, w));
            holder.addImage.setLayoutParams(new RelativeLayout.LayoutParams(w, w));
//            if (path.equals(NULL)) {
//                holder.deleteIcon.setVisibility(View.INVISIBLE);
//                holder.image.setScaleType(ImageView.ScaleType.FIT_XY);
//                holder.image.setImageResource(R.drawable.add_damaged_icon);
//            } else {
//                holder.deleteIcon.setVisibility(View.VISIBLE);
//                holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                DownloadImageLoader.loadImageForFile(holder.image, path);
//            }

//            holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            holder.image.setImageBitmap(null);
//            holder.image.setBackgroundResource(R.drawable.add_damaged_icon);
            if (path.equals(NULL)) {
                holder.deleteIcon.setVisibility(View.INVISIBLE);
                holder.addImage.setVisibility(View.VISIBLE);
                holder.image.setVisibility(View.GONE);
            } else {
                holder.deleteIcon.setVisibility(View.VISIBLE);
                holder.addImage.setVisibility(View.GONE);
                holder.image.setVisibility(View.VISIBLE);
                DownloadImageLoader.loadImageForFile(holder.image, path);
            }
        }


    }


    class HolderView {
        ImageView deleteIcon;
        ImageView image;
        ImageView addImage;
    }

}
