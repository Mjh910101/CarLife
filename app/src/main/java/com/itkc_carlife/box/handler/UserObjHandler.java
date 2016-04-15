package com.itkc_carlife.box.handler;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.box.UserObj;
import com.itkc_carlife.download.DownloadImageLoader;
import com.itkc_carlife.handlers.JsonHandle;
import com.itkc_carlife.handlers.MessageHandler;
import com.itkc_carlife.handlers.SystemHandle;
import com.itkc_carlife.http.HttpUtilsBox;
import com.itkc_carlife.http.UrlHandler;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;

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
 * Created by Hua on 16/1/7.
 */
public class UserObjHandler {

    private static File sessionToken;

    public static UserObj getUserObj(JSONObject json) {
        UserObj obj = new UserObj();

        obj.setMobilePhoneNumber(JsonHandle.getString(json, UserObj.MOBILE_PHONE_NUMBER));
        obj.setSessiontoken(JsonHandle.getString(json, UserObj.SESSION_TOKEN));
        obj.setUsername(JsonHandle.getString(json, UserObj.USER_NAME));
        obj.setSex(JsonHandle.getInt(json, UserObj.SEX));
        obj.setBalance(JsonHandle.getDouble(json, UserObj.BALANCE));

        JSONObject avatarJson = JsonHandle.getJSON(json, UserObj.AVATAR);
        if (avatarJson != null) {
            obj.setAvatar(JsonHandle.getString(avatarJson, "url"));
            obj.setAvatarId(JsonHandle.getString(avatarJson, "id"));
        }

        return obj;
    }

    private final static String KEY = "user_";

    public static void saveUser(Context context, UserObj obj) {
        SystemHandle.saveStringMessage(context, KEY + UserObj.SESSION_TOKEN, obj.getSessiontoken());
        SystemHandle.saveStringMessage(context, KEY + UserObj.MOBILE_PHONE_NUMBER, obj.getMobilePhoneNumber());
        SystemHandle.saveStringMessage(context, KEY + UserObj.AVATAR, obj.getAvatar());
        SystemHandle.saveStringMessage(context, KEY + UserObj.AVATAR + "_id", obj.getAvatarId());
        saveUserBalance(context, obj.getBalance());
        saveUserName(context, obj.getUsername());
        saveUserSex(context, obj.getSex());
    }

    public static void saveUserBalance(Context context, double d) {
        SystemHandle.saveDoubleMessage(context, KEY + UserObj.BALANCE, d);

    }

//    public static String getUserBalance(Context context) {
//        double b = SystemHandle.getDouble(context, KEY + UserObj.BALANCE);
//        return new DecimalFormat("0.00").format(b);
//    }

    public static void setUserBalance(final Context context, final TextView view) {
        view.setText("￥" + 0);
        String url = UrlHandler.getUserBalance() + "?sessiontoken=" + UserObjHandler.getSessionToken(context);
        RequestParams params = HttpUtilsBox.getRequestParams(context);

        HttpUtilsBox.getHttpUtil().send(HttpMethod.GET, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception, String msg) {
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.d("", result);
                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            int status = JsonHandle.getInt(json, "status");
                            if (status == 1) {
                                JSONObject resultJson = JsonHandle.getJSON(json, "result");
                                if (resultJson != null) {
                                    double balance = JsonHandle.getDouble(resultJson, "balance");
                                    setUserBalance(view, balance);
                                    UserObjHandler.saveUserBalance(context, balance);
                                }
                            }
                        }
                    }

                });
    }

    public static void setUserBalance(TextView view, double balance) {
        String s = new DecimalFormat("0.00").format(balance);
        view.setText("￥" + s);
    }

    public static boolean isLogin(Context context) {
        return !getSessiontoken(context).equals("");
    }

    private static String getSessiontoken(Context context) {
        return SystemHandle.getString(context, KEY + UserObj.SESSION_TOKEN);
    }

    public static void setUserAvatar(Context context, ImageView view, int r) {
        String a = getUserAvatar(context);
        if (a.equals("")) {
            if (isMan(context)) {
                view.setImageResource(R.drawable.man_pic_icon);
            } else {
                view.setImageResource(R.drawable.woman_pic_icon);
            }
        } else {
            DownloadImageLoader.loadImage(view, a, r);
        }
    }

    public static void setUserAvatar(Context context, ImageView view) {
        String a = getUserAvatar(context);
        if (a.equals("")) {
            if (isMan(context)) {
                view.setImageResource(R.drawable.man_pic_icon);
            } else {
                view.setImageResource(R.drawable.woman_pic_icon);
            }
        } else {
            DownloadImageLoader.loadImage(view, a);
        }
    }

    public static int getUserSex(Context context) {
        return SystemHandle.getInt(context, KEY + UserObj.SEX);
    }

    public static boolean isMan(int s) {
        return s == 1;
    }

    public static boolean isMan(Context context) {
        int sex = SystemHandle.getInt(context, KEY + UserObj.SEX);
        return isMan(sex);
    }

    public static String getUserName(Context context) {
        return SystemHandle.getString(context, KEY + UserObj.USER_NAME);
    }

    public static String getUserMobile(Context context) {
        return SystemHandle.getString(context, KEY + UserObj.MOBILE_PHONE_NUMBER);
    }

    public static String getSessionToken(Context context) {
        return SystemHandle.getString(context, KEY + UserObj.SESSION_TOKEN);
    }

    public static void saveUserAvatar(Context context, String url) {
        SystemHandle.saveStringMessage(context, KEY + "avatar", url);
    }

    public static String getUserAvatar(Context context) {
        return SystemHandle.getString(context, KEY + UserObj.AVATAR);
    }

    public static void saveUserName(Context context, String s) {
        SystemHandle.saveStringMessage(context, KEY + UserObj.USER_NAME, s);
    }

    public static void saveUserSex(Context context, int sex) {
        SystemHandle.saveIntMessage(context, KEY + UserObj.SEX, sex);
    }

    public static String getUserAvatarId(Context context) {
        return SystemHandle.getString(context, UserObj.AVATAR + "_id");
    }

    public static void logout(Context context) {
        SystemHandle.saveStringMessage(context, KEY + UserObj.SESSION_TOKEN, "");
        SystemHandle.saveStringMessage(context, KEY + UserObj.MOBILE_PHONE_NUMBER, "");
        SystemHandle.saveStringMessage(context, KEY + UserObj.AVATAR, "");
        SystemHandle.saveStringMessage(context, KEY + UserObj.AVATAR + "_id", "");
        saveUserBalance(context, 0);
        saveUserName(context, "");
        saveUserSex(context, 0);
        UserCarInfoObjHandler.delete(context);
        UserClaimsInfoObjHandler.delete(context);

    }
}
