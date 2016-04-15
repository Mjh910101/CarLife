package com.itkc_carlife.tool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;


import com.itkc_carlife.download.DownloadImageLoader;
import com.itkc_carlife.handlers.DateHandle;

import java.io.File;

public class Passageway {

    public static final int CAMERA_REQUEST_CODE = 322;
    public static final int IMAGE_REQUEST_CODE = 323;
    public static final int RESULT_REQUEST_CODE = 321;
    public static final int VIDEO_REQUEST_CODE = 324;

    public static void resizeImage(Context context, Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        ((Activity) (context)).startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    public static void selectImage(Context context) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
        galleryIntent.setType("image/*");
        ((Activity) (context)).startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE);
    }

    public static String takePhoto(Context context) {
        String fileName = "pic_" + DateHandle.getTime() + ".jpg";
        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri(fileName));
        cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        ((Activity) (context)).startActivityForResult(cameraIntent,
                CAMERA_REQUEST_CODE);
        return fileName;
    }

    public static String takeVideo(Context context) {
        String fileName = "video_" + DateHandle.getTime() + ".3gp";
        Intent intent = new Intent();
        intent.setAction("android.media.action.VIDEO_CAPTURE");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri(fileName));
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
        ((Activity) (context)).startActivityForResult(intent, VIDEO_REQUEST_CODE);
        return fileName;
    }

    private static Uri getImageUri(String fileName) {
        return Uri.fromFile(new File(DownloadImageLoader.getImagePath(), fileName));
    }

    /**
     * @param context
     * @param cls
     */
    public static void jumpActivity(Context context, Class<?> cls) {
        jumpActivity(context, cls, -1, null, -1);
    }

    /**
     * @param context
     * @param cls
     * @param requestCode
     */
    public static void jumpActivity(Context context, Class<?> cls,
                                    int requestCode) {
        jumpActivity(context, cls, requestCode, null, -1);
    }

    /**
     * @param context
     * @param cls
     * @param bundle
     */
    public static void jumpActivity(Context context, Class<?> cls, Bundle bundle) {
        jumpActivity(context, cls, -1, bundle, -1);
    }

    /**
     * @param context
     * @param cls
     * @param requestCode
     * @param bundle
     */
    public static void jumpActivity(Context context, Class<?> cls,
                                    int requestCode, Bundle bundle) {
        jumpActivity(context, cls, requestCode, bundle, -1);
    }

    public static void jumpDialing(Context context, String tel) {
        Uri uri = Uri.parse("tel:" + tel);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param flagActivityClearTop
     * @param cls
     */
    public static void jumpActivity(Context context, int flagActivityClearTop,
                                    Class<?> cls) {
        jumpActivity(context, cls, -1, null, flagActivityClearTop);
    }

    public static void jumpActivity(Context context, int flagActivityClearTop,
                                    Class<?> cls, Bundle bundle) {
        jumpActivity(context, cls, -1, bundle, flagActivityClearTop);
    }

    /**
     * @param context
     * @param cls
     * @param bundle
     * @param flagActivityClearTop
     */
    public static void jumpActivity(Context context, Class<?> cls,
                                    Bundle bundle, int flagActivityClearTop) {
        jumpActivity(context, cls, -1, bundle, flagActivityClearTop);
    }

    /**
     * @param context
     * @param cls
     * @param requestCode
     * @param bundle
     * @param flagActivityClearTop
     */
    public static void jumpActivity(Context context, Class<?> cls,
                                    int requestCode, Bundle bundle, int flagActivityClearTop) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        if (flagActivityClearTop > 0) {
            intent.addFlags(flagActivityClearTop);
        }
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        ((Activity) (context)).startActivityForResult(intent, requestCode);
    }

    /**
     * 忽略中间页面，直接跳转
     *
     * @param context
     * @param cls
     */
    public static void jumpToActivity(Context context, Class<?> cls) {
        jumpActivity(context, Intent.FLAG_ACTIVITY_CLEAR_TOP, cls);
    }
}
