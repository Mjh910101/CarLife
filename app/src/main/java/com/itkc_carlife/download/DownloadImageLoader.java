package com.itkc_carlife.download;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.itkc_carlife.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;

public class DownloadImageLoader {

    private final static int INDEX_IMAGE = R.drawable.download_icon;

    private final static String RootName = "CSHFILE";
    private final static String ImageFileName = RootName + "/Image";
    private final static String NOMEDIA = ImageFileName + "/.nomedia";

    private static ImageLoader imageLoader = ImageLoader.getInstance();
    private static DisplayImageOptions mDisplayImageOptions = null;
    private static DisplayImageOptions mRoundedDisplayImageOptions = null;

    private static int rounded = 0;

    public static void initLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .threadPoolSize(3).denyCacheImageMultipleSizesInMemory()
                        // .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(200) // 缓存的文件数量
                .discCache(new UnlimitedDiscCache(getImageFile()))// 自定义缓存路径
                .writeDebugLogs().build();

        imageLoader.init(config);
    }

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
            return sdDir.toString();
        }
        return "";
    }

    public static String getImagePath() {
        return getSDPath() + "/" + ImageFileName;
    }

    private static File getImageFile() {
        return new File(getImagePath());
    }

    public static void loadImageForFile(ImageView mImageView, String url) {
        loadImage(mImageView, "file://" + url, 0);
    }

    public static void loadImageForFile(ImageView mImageView, String url, int rounded) {
        loadImage(mImageView, "file://" + url, rounded);
    }

    public static void loadImageForFile(ImageView mImageView, String url,
                                        ImageLoadingListener mImageLoadingListener) {
        loadImage(mImageView, "file://" + url, 0, mImageLoadingListener);
    }

    public static void loadImageForID(ImageView mImageView, int id) {
        loadImage(mImageView, "drawable://" + id, 0);
    }

    public static void loadImage(ImageView mImageView, String url) {
        loadImage(mImageView, url, 0);
    }

    public static void loadImage(ImageView mImageView, String url,
                                 ImageLoadingListener mImageLoadingListener) {
        loadImage(mImageView, url, 0, mImageLoadingListener);
    }

    public static void loadImage(ImageView mImageView, String url, int rounded) {
        loadImage(mImageView, url, rounded, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                Log.e("", "onLoadingStarted");
            }

            @Override
            public void onLoadingFailed(String imageUri, View view,
                                        FailReason failReason) {
                Log.e("", "onLoadingFailed");
            }

            @Override
            public void onLoadingComplete(String imageUri, View view,
                                          Bitmap loadedImage) {
                Log.e("", "onLoadingComplete");
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                Log.e("", "onLoadingCancelled");
            }
        });
    }

    public static void loadImage(ImageView mImageView, String url, int rounded,
                                 ImageLoadingListener mImageLoadingListener) {
        Log.e("image_URL", url);
        DisplayImageOptions imageOptions = null;
        if (rounded == 0) {
            imageOptions = getDisplayImageOptions();
        } else {
            imageOptions = getRoundedDisplayImageOptions(rounded);
        }
        imageLoader.displayImage(url, mImageView, imageOptions,
                mImageLoadingListener);
    }

    private static DisplayImageOptions getRoundedDisplayImageOptions(int rounded) {
        if (rounded != DownloadImageLoader.rounded) {
            mRoundedDisplayImageOptions = null;
            DownloadImageLoader.rounded = rounded;
        }
        if (mRoundedDisplayImageOptions == null) {
            mRoundedDisplayImageOptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(INDEX_IMAGE)
                    .showImageForEmptyUri(INDEX_IMAGE)
                    .showImageOnFail(INDEX_IMAGE)
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
                    .cacheOnDisc(true).considerExifParams(true)
                    .displayer(new RoundedBitmapDisplayer(rounded)).build();
        }
        return mRoundedDisplayImageOptions;
    }

    private static DisplayImageOptions getDisplayImageOptions() {
        if (mDisplayImageOptions == null) {
            mDisplayImageOptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(INDEX_IMAGE)
                    .showImageForEmptyUri(INDEX_IMAGE)
                    .showImageOnFail(INDEX_IMAGE)
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
                    .considerExifParams(true)
                    .cacheOnDisc(true).build();
        }
        return mDisplayImageOptions;
        // .displayer(new RoundedBitmapDisplayer(20))是否设置为圆角，弧度为多少
        // .displayer(new FadeInBitmapDisplayer(100))是否图片加载好后渐入的动画时间
    }
}
