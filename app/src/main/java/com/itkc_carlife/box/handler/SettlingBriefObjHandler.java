package com.itkc_carlife.box.handler;

import com.itkc_carlife.box.SettlingBriefObj;
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
 * Created by Hua on 16/1/19.
 */
public class SettlingBriefObjHandler {

    public static List<SettlingBriefObj> getSettlingBriefObjList(JSONArray array) {
        List<SettlingBriefObj> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(getSettlingBriefObj(JsonHandle.getJSON(array, i)));
        }
        return list;
    }

    private static SettlingBriefObj getSettlingBriefObj(JSONObject json) {
        SettlingBriefObj obj = new SettlingBriefObj();

        obj.setCreatedAt(JsonHandle.getString(json, SettlingBriefObj.CREATED_AT));
        obj.setFeedback(JsonHandle.getString(json, SettlingBriefObj.FEEDBACK));
        obj.setOrder_status(JsonHandle.getInt(json, SettlingBriefObj.ORDER_STATUS));
        obj.setOrder_status_title(JsonHandle.getString(json, SettlingBriefObj.ORDER_STATUS_TITLE));
        obj.setPay(JsonHandle.getString(json, SettlingBriefObj.PAY));
        obj.setSettling_type(JsonHandle.getString(json, SettlingBriefObj.SETTLING_TYPE));
        obj.setSettlingId(JsonHandle.getString(json, SettlingBriefObj.SETTLING_ID));
        obj.setSettlingOrderId(JsonHandle.getString(json, SettlingBriefObj.SETTLING_ORDER_ID));

        return obj;
    }

}
