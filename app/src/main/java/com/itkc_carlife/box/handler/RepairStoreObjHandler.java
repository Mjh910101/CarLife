package com.itkc_carlife.box.handler;

import com.itkc_carlife.box.RepairStoreObj;
import com.itkc_carlife.box.ServicesObj;
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
public class RepairStoreObjHandler {

    public static List<RepairStoreObj> getRepairStoreObjList(JSONArray array) {
        List<RepairStoreObj> list = new ArrayList<RepairStoreObj>();
        for (int i = 0; i < array.length(); i++) {
            list.add(getRepairStoreObj(JsonHandle.getJSON(array, i)));
        }
        return list;
    }

    public static RepairStoreObj getRepairStoreObj(JSONObject storeJson) {
        RepairStoreObj obj = new RepairStoreObj();

//        obj.setCreatedAt(JsonHandle.getString(json, RepairStoreObj.CREATED_AT));
        obj.setFee(JsonHandle.getString(storeJson, RepairStoreObj.FEE));
        obj.setDays(JsonHandle.getInt(storeJson, RepairStoreObj.DAYS));

        obj.setObjectId(JsonHandle.getString(storeJson, RepairStoreObj.OBJECT_ID));
        obj.setAddress(JsonHandle.getString(storeJson, RepairStoreObj.ADDRESS));
        obj.setName(JsonHandle.getString(storeJson, RepairStoreObj.NAME));
        obj.setDescript(JsonHandle.getString(storeJson, RepairStoreObj.DESCRIPT));
        obj.setContact(JsonHandle.getString(storeJson, RepairStoreObj.CONTACT));
        obj.setLinkman(JsonHandle.getString(storeJson, RepairStoreObj.LINLMAN));
        obj.setImages(JsonHandle.getArray(storeJson, ServicesObj.IMAGE));

        JSONObject geoJson = JsonHandle.getJSON(storeJson, "geo");
        if (geoJson != null) {
            obj.setLongitude(JsonHandle.getDouble(geoJson, RepairStoreObj.LONGITUDE));
            obj.setLatitude(JsonHandle.getDouble(geoJson, RepairStoreObj.LATITUDE));
        }

        JSONObject coverJson = JsonHandle.getJSON(storeJson, "cover");
        if (coverJson != null) {
            obj.setCoverUrl(JsonHandle.getString(coverJson, "url"));
        }

        return obj;
    }

    private static RepairStoreObj repairStoreObj;

    public static void save(RepairStoreObj obj) {
        if (repairStoreObj != null) {
            repairStoreObj = null;
        }
        repairStoreObj = obj;
    }

    public static RepairStoreObj getSaveRepairStoreObj() {
        return repairStoreObj;
    }

}
