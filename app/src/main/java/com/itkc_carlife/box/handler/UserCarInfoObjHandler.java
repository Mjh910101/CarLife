package com.itkc_carlife.box.handler;

import android.content.Context;

import com.itkc_carlife.box.UserCarInfoObj;
import com.itkc_carlife.handlers.JsonHandle;
import com.itkc_carlife.handlers.LicensePlateHandler;
import com.itkc_carlife.handlers.SystemHandle;

import org.json.JSONObject;

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
public class UserCarInfoObjHandler {

    public static void delete(Context context) {
        SystemHandle.saveStringMessage(context, KEY + UserCarInfoObj.CAR_NUNMER, "");
        SystemHandle.saveStringMessage(context, KEY + UserCarInfoObj.ENGINE_NUMBER, "");
        SystemHandle.saveStringMessage(context, KEY + UserCarInfoObj.FRAME_NUMBER, "");
        SystemHandle.saveStringMessage(context, KEY + UserCarInfoObj.CITY, "");
        saveDrivingVideImage(context, "");
        saveDrivingVideImageId(context, "");
        saveDrivingImage(context, "");
        saveDrivingImageId(context, "");
        LicensePlateHandler.saveProvinceText(context, "");
        LicensePlateHandler.saveCityCodeText(context, "");
    }

    public static UserCarInfoObj getUserCarInfoObj(JSONObject json) {
        UserCarInfoObj obj = new UserCarInfoObj();

        obj.setCar_number(JsonHandle.getString(json, UserCarInfoObj.CAR_NUNMER));
        obj.setObjectId(JsonHandle.getString(json, UserCarInfoObj.OBJECT_ID));
        obj.setCreatedAt(JsonHandle.getString(json, UserCarInfoObj.CREATED_AT));
        obj.setEngine_number(JsonHandle.getString(json, UserCarInfoObj.ENGINE_NUMBER));
        obj.setFrame_number(JsonHandle.getString(json, UserCarInfoObj.FRAME_NUMBER));
        obj.setMajor_car_license(JsonHandle.getString(json, UserCarInfoObj.MAJOR_CAR_LICENSE));
        obj.setSecondary_car_license(JsonHandle.getString(json, UserCarInfoObj.SECONDARY_CAR_LICENSE));
        obj.setUpdatedAt(JsonHandle.getString(json, UserCarInfoObj.UPLOATE_AT));
        obj.setCity(JsonHandle.getString(json, UserCarInfoObj.CITY));

        return obj;
    }

    private final static String KEY = "car_info_";

    public static void saveCarInfo(Context context, JSONObject json) {
        String carNumber = JsonHandle.getString(json, "car_number");
        try {
            String p = carNumber.substring(0, 1);
            String c = carNumber.substring(1, carNumber.indexOf("-"));
            String n = carNumber.substring(carNumber.indexOf("-") + 1, carNumber.length());
            LicensePlateHandler.saveProvinceText(context, p);
            LicensePlateHandler.saveCityCodeText(context, c);
            SystemHandle.saveStringMessage(context, KEY + UserCarInfoObj.CAR_NUNMER, n);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SystemHandle.saveStringMessage(context, KEY + UserCarInfoObj.ENGINE_NUMBER, JsonHandle.getString(json, "engine_number"));
        SystemHandle.saveStringMessage(context, KEY + UserCarInfoObj.FRAME_NUMBER, JsonHandle.getString(json, "frame_number"));
        SystemHandle.saveStringMessage(context, KEY + UserCarInfoObj.CITY, JsonHandle.getString(json, "city"));

        JSONObject secondary_car_license = JsonHandle.getJSON(json, "secondary_car_license");
        if (secondary_car_license != null) {
            saveDrivingVideImage(context, JsonHandle.getString(secondary_car_license, "url"));
            saveDrivingVideImageId(context, JsonHandle.getString(secondary_car_license, "id"));
        }

        JSONObject major_car_license = JsonHandle.getJSON(json, "major_car_license");
        if (major_car_license != null) {
            saveDrivingImage(context, JsonHandle.getString(major_car_license, "url"));
            saveDrivingImageId(context, JsonHandle.getString(major_car_license, "id"));
        }
    }


    public static void saveCarInfo(Context context, String provinces, String cityCode, String carNumber, String engine, String frame, String cityName) {
        LicensePlateHandler.saveProvinceText(context, provinces);
        LicensePlateHandler.saveCityCodeText(context, cityCode);
        SystemHandle.saveStringMessage(context, KEY + UserCarInfoObj.CAR_NUNMER, carNumber);
        SystemHandle.saveStringMessage(context, KEY + UserCarInfoObj.ENGINE_NUMBER, engine);
        SystemHandle.saveStringMessage(context, KEY + UserCarInfoObj.FRAME_NUMBER, frame);
        SystemHandle.saveStringMessage(context, KEY + UserCarInfoObj.CITY, cityName);
    }

    public static void saveDrivingImage(Context context, String drivingImage) {
        SystemHandle.saveStringMessage(context, KEY + UserCarInfoObj.MAJOR_CAR_LICENSE, drivingImage);
    }

    public static void saveDrivingVideImage(Context context, String drivingVideImage) {
        SystemHandle.saveStringMessage(context, KEY + UserCarInfoObj.SECONDARY_CAR_LICENSE, drivingVideImage);
    }

    public static String getCity(Context context) {
        return SystemHandle.getString(context, KEY + UserCarInfoObj.CITY);
    }

    public static String getCarNumber(Context context) {
        return SystemHandle.getString(context, KEY + UserCarInfoObj.CAR_NUNMER);
    }

    public static String getUserCarNumber(Context context) {
        return LicensePlateHandler.getProvinceText(context) +
                LicensePlateHandler.getCityCodeText(context) + "-" +
                SystemHandle.getString(context, KEY + UserCarInfoObj.CAR_NUNMER);
    }

    public static String getUserCarNumberForGone(Context context) {
        String str = SystemHandle.getString(context, KEY + UserCarInfoObj.CAR_NUNMER);
        str = "**" + str.substring(2, str.length());
        String carNumber = LicensePlateHandler.getProvinceText(context) +
                LicensePlateHandler.getCityCodeText(context) + "-" + str;

        return carNumber;
    }

    public static String getEnginerNumber(Context context) {
        return SystemHandle.getString(context, KEY + UserCarInfoObj.ENGINE_NUMBER);
    }

    public static String getFrameNumber(Context context) {
        return SystemHandle.getString(context, KEY + UserCarInfoObj.FRAME_NUMBER);
    }

    public static String getDrivingImage(Context context) {
        return SystemHandle.getString(context, KEY + UserCarInfoObj.MAJOR_CAR_LICENSE);
    }

    public static String getDrivingVideImage(Context context) {
        return SystemHandle.getString(context, KEY + UserCarInfoObj.SECONDARY_CAR_LICENSE);
    }

    public static void saveDrivingImageId(Context context, String objectId) {
        SystemHandle.saveStringMessage(context, KEY + UserCarInfoObj.MAJOR_CAR_LICENSE + "_id", objectId);
    }

    public static String getDrivingImageId(Context context) {
        return SystemHandle.getString(context, KEY + UserCarInfoObj.MAJOR_CAR_LICENSE + "_id");
    }

    private static void saveDrivingVideImageId(Context context, String id) {
        SystemHandle.saveStringMessage(context, KEY + UserCarInfoObj.SECONDARY_CAR_LICENSE + "_id", id);
    }

    public static String getDrivingViewImageId(Context context) {
        return SystemHandle.getString(context, KEY + UserCarInfoObj.SECONDARY_CAR_LICENSE + "_id");
    }

    public static boolean isThrough(Context context) {
        return !getCarNumber(context).equals("");
    }


}
