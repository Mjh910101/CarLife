package com.itkc_carlife.box.handler;

import android.content.Context;

import com.itkc_carlife.box.UserClaimsInfoObj;
import com.itkc_carlife.handlers.JsonHandle;
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
 * Created by Hua on 16/1/11.
 */
public class UserClaimsInfoObjHandler {


    public static void delete(Context context) {
        saveClaimsInfo(context, "", "", "", "");
        savePositiveImage(context, "");
        savePositiveImageId(context, "");
        saveBackImage(context, "");
        saveBackImageId(context, "");
        savePeopleImage(context, "");
        savePeopleImageId(context, "");
        saveDriverImage(context, "");
        saveDriverImageId(context, "");
        saveDriverViceImage(context, "");
        saveDriverViceImageId(context, "");
    }

    public static void saveUserClaimsInfo(Context context, JSONObject json) {
        saveClaimsInfo(context, JsonHandle.getString(json, "insurance_company"), JsonHandle.getString(json, "insurance_number"),
                JsonHandle.getString(json, "insurance_realname"), JsonHandle.getString(json, "insurance_id_card_number"));
        JSONObject insurance_id_card_front = JsonHandle.getJSON(json, "insurance_id_card_front");
        if (insurance_id_card_front != null) {
            savePositiveImage(context, JsonHandle.getString(insurance_id_card_front, "url"));
            savePositiveImageId(context, JsonHandle.getString(insurance_id_card_front, "id"));
        }
        JSONObject insurance_id_card_back = JsonHandle.getJSON(json, "insurance_id_card_back");
        if (insurance_id_card_back != null) {
            saveBackImage(context, JsonHandle.getString(insurance_id_card_back, "url"));
            saveBackImageId(context, JsonHandle.getString(insurance_id_card_back, "id"));
        }
        JSONObject insurance_id_card_in_hand = JsonHandle.getJSON(json, "insurance_id_card_in_hand");
        if (insurance_id_card_in_hand != null) {
            savePeopleImage(context, JsonHandle.getString(insurance_id_card_in_hand, "url"));
            savePeopleImageId(context, JsonHandle.getString(insurance_id_card_in_hand, "id"));
        }
        JSONObject major_driving_license = JsonHandle.getJSON(json, "major_driving_license");
        if (major_driving_license != null) {
            saveDriverImage(context, JsonHandle.getString(major_driving_license, "url"));
            saveDriverImageId(context, JsonHandle.getString(major_driving_license, "id"));
        }
        JSONObject secondary_driving_license = JsonHandle.getJSON(json, "secondary_driving_license");
        if (secondary_driving_license != null) {
            saveDriverViceImage(context, JsonHandle.getString(secondary_driving_license, "url"));
            saveDriverViceImageId(context, JsonHandle.getString(secondary_driving_license, "id"));
        }
    }

    public static void savePositiveImageId(Context context, String id) {
        SystemHandle.saveStringMessage(context, KEY + "positive" + "_id", id);
    }

    public static String getPositiveImageId(Context context) {
        return SystemHandle.getString(context, KEY + "positive" + "_id");
    }

    public static void saveBackImageId(Context context, String id) {
        SystemHandle.saveStringMessage(context, KEY + "back" + "_id", id);
    }

    public static String getBackImageId(Context context) {
        return SystemHandle.getString(context, KEY + "back" + "_id");
    }

    public static void savePeopleImageId(Context context, String id) {
        SystemHandle.saveStringMessage(context, KEY + "people" + "_id", id);
    }

    public static String getPeopleImageId(Context context) {
        return SystemHandle.getString(context, KEY + "people" + "_id");
    }

    public static void saveDriverImageId(Context context, String id) {
        SystemHandle.saveStringMessage(context, KEY + "driver" + "_id", id);
    }

    public static String getDriverImageId(Context context) {
        return SystemHandle.getString(context, KEY + "driver" + "_id");
    }

    public static void saveDriverViceImageId(Context context, String id) {
        SystemHandle.saveStringMessage(context, KEY + "dricer_vide" + "_id", id);
    }

    public static String getDriverViceImageId(Context context) {
        return SystemHandle.getString(context, KEY + "dricer_vide" + "_id");
    }

    private final static String KEY = "claims_";

    public static void saveDriverImage(Context context, String url) {
        SystemHandle.saveStringMessage(context, KEY + "driver", url);
    }

    public static String getDriverImage(Context context) {
        return SystemHandle.getString(context, KEY + "driver");
    }

    public static void saveDriverViceImage(Context context, String url) {
        SystemHandle.saveStringMessage(context, KEY + "dricer_vide", url);
    }


    public static String getDriverVideImage(Context context) {
        return SystemHandle.getString(context, KEY + "dricer_vide");
    }

    public static void savePeopleImage(Context context, String url) {
        SystemHandle.saveStringMessage(context, KEY + "people", url);
    }

    public static String getPeopleImage(Context context) {
        return SystemHandle.getString(context, KEY + "people");
    }

    public static void saveBackImage(Context context, String url) {
        SystemHandle.saveStringMessage(context, KEY + "back", url);
    }

    public static String getBackImage(Context context) {
        return SystemHandle.getString(context, KEY + "back");
    }

    public static void savePositiveImage(Context context, String url) {
        SystemHandle.saveStringMessage(context, KEY + "positive", url);
    }

    public static String getPositiveImage(Context context) {
        return SystemHandle.getString(context, KEY + "positive");
    }

    public static void saveClaimsInfo(Context context, String company, String code, String name, String id) {
        SystemHandle.saveStringMessage(context, KEY + "company", company);
        SystemHandle.saveStringMessage(context, KEY + "code", code);
        SystemHandle.saveStringMessage(context, KEY + "name", name);
        SystemHandle.saveStringMessage(context, KEY + "id_card_id", id);
    }

    public static String getCompany(Context context) {
        return SystemHandle.getString(context, KEY + "company");
    }

    public static String getCode(Context context) {
        return SystemHandle.getString(context, KEY + "code");
    }

    public static String getName(Context context) {
        return SystemHandle.getString(context, KEY + "name");
    }

    public static String getIdCardId(Context context) {
        return SystemHandle.getString(context, KEY + "id_card_id");
    }

    public static boolean isThrough(Context context) {
        return !getCompany(context).equals("");
    }

    public static UserClaimsInfoObj getUserClaimsInfoObj(JSONObject json) {
        UserClaimsInfoObj obj = new UserClaimsInfoObj();

        obj.setObjectId(JsonHandle.getString(json, "objectId"));
        obj.setInsurance_company(JsonHandle.getString(json, "insurance_company"));
        obj.setInsurance_id_card_number(JsonHandle.getString(json, "insurance_id_card_number"));
        obj.setInsurance_number(JsonHandle.getString(json, "insurance_number"));
        obj.setInsurance_realname(JsonHandle.getString(json, "insurance_realname"));

        return obj;
    }

}
