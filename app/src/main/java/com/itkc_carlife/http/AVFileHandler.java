package com.itkc_carlife.http;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.SaveCallback;

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
 * Created by Hua on 16/1/8.
 */
public class AVFileHandler {

    public static void uploadFile(File f, final AVSaveCallback callback) {
        try {
            final AVFile file = AVFile.withAbsoluteLocalPath(f.getName(), f.toString());
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    callback.callnack(e, file);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface AVSaveCallback {
        void callnack(AVException e, AVFile file);
    }

}
