package com.itkc_carlife.box.handler;

import com.itkc_carlife.box.SettlingObj;
import com.itkc_carlife.handlers.JsonHandle;

import org.json.JSONArray;
import org.json.JSONObject;

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
 * Created by Hua on 16/1/18.
 */
public class SettlingObjHandler {

    public static List<SettlingObj> getSettlingObjList(JSONArray array) {
        List<SettlingObj> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {

        }
        return list;
    }

    public static SettlingObj getSettlingObj(JSONObject json) {
        SettlingObj obj = new SettlingObj();

        JSONObject carInfoJson = JsonHandle.getJSON(json, "carInfo");
        if (carInfoJson != null) {
            obj.setCarInfo(UserCarInfoObjHandler.getUserCarInfoObj(carInfoJson));
        }

        JSONObject insuranceJson = JsonHandle.getJSON(json, "insurance");
        if (insuranceJson != null) {
            obj.setInsurance(UserClaimsInfoObjHandler.getUserClaimsInfoObj(insuranceJson));
        }

        obj.setUser(UserObjHandler.getUserObj(json));

        JSONArray array = JsonHandle.getArray(json, "area");
        if (array != null) {
            obj.setAreaList(array);
        }

        JSONObject video = JsonHandle.getJSON(json, "video");
        if (video != null) {
            obj.setVideo(video);
        }

        JSONObject detail = JsonHandle.getJSON(json, "detail");
        if (detail != null) {
            obj.setDetail(detail);
        }

        return obj;
    }

    private static SettlingObj majorSettling;
    private static SettlingObj secondarySettling;

    public static void setSettlingObjs(SettlingObj m, SettlingObj s) {
        if (majorSettling != null) {
            majorSettling = null;
        }
        if (secondarySettling != null) {
            secondarySettling = null;
        }
        majorSettling = m;
        secondarySettling = s;
    }

    public static SettlingObj getMajorSettling() {
        return majorSettling;
    }

    public static SettlingObj getSecondarySettling() {
        return secondarySettling;
    }
}
