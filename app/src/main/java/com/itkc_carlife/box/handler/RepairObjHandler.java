package com.itkc_carlife.box.handler;

import com.itkc_carlife.box.RepairObj;
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
 * Created by Hua on 16/1/20.
 */
public class RepairObjHandler {

    public static RepairObj getRepairObj(JSONObject json) {
        RepairObj obj = new RepairObj();

        obj.setCreatedAt(JsonHandle.getString(json, RepairObj.CREATED_AT));
        obj.setObjectId(JsonHandle.getString(json, RepairObj.OBJECT_ID));
        obj.setAddress(JsonHandle.getString(json, RepairObj.ADDRESS));
        obj.setDescript(JsonHandle.getString(json, RepairObj.DESCRIPT));
        obj.setExpect(JsonHandle.getString(json, RepairObj.EXPECT));
        obj.setExpectworkdays(JsonHandle.getInt(json, RepairObj.EXPECT_WORK_DAY));
        obj.setOrder_status(JsonHandle.getString(json, RepairObj.ORDER_STATUS));
        obj.setFee(JsonHandle.getString(json, "fee"));
        obj.setDays(JsonHandle.getInt(json, "days"));
        obj.setIsSettling(JsonHandle.getInt(json, "isSettling"));


        JSONObject geoJson = JsonHandle.getJSON(json, "geo");
        if (geoJson != null) {
            obj.setLatitude(JsonHandle.getDouble(geoJson, RepairObj.LATITUDE));
            obj.setLongitude(JsonHandle.getDouble(geoJson, RepairObj.LONGITUDE));
        }


        JSONObject pointJson = JsonHandle.getJSON(json, "point");
        if (pointJson != null) {
            obj.setLatitude(JsonHandle.getDouble(pointJson, RepairObj.LATITUDE));
            obj.setLongitude(JsonHandle.getDouble(pointJson, RepairObj.LONGITUDE));
        }

        JSONArray array = JsonHandle.getArray(json, "area");
        if (array != null) {
            obj.setAreaList(array);
        }

        JSONObject video = JsonHandle.getJSON(json, "video");
        if (video != null) {
            obj.setVideo(video);
        }

        JSONObject storeJson = JsonHandle.getJSON(json, "store");
        if (storeJson != null) {
            obj.setStoreObj(RepairStoreObjHandler.getRepairStoreObj(storeJson));
            obj.getStoreObj().setFee(JsonHandle.getString(json, "fee"));
            obj.getStoreObj().setDays(JsonHandle.getInt(json, "days"));
        }

        JSONObject commentJson = JsonHandle.getJSON(json, RepairObj.COMMENT);
        if (commentJson != null) {
            obj.setComment(ServersCommentObjHandler.getServersCommentObj(commentJson));
        }

        return obj;
    }

    public static List<RepairObj> getRepairList(JSONArray array) {
        List<RepairObj> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(getRepairObj(JsonHandle.getJSON(array, i)));
        }
        return list;
    }
}
