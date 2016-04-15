package com.itkc_carlife.box.handler;

import com.itkc_carlife.box.ServiceObj;
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
 * Created by Hua on 16/2/15.
 */
public class ServicesObjHandler {

    public static List<ServicesObj> getServicesObjList(JSONArray array) {
        List<ServicesObj> list = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            list.add(getServicesObj(JsonHandle.getJSON(array, i)));
        }

        return list;
    }

    public static ServicesObj getServicesObj(JSONObject json) {
        ServicesObj obj = new ServicesObj();

        obj.setObjectId(JsonHandle.getString(json, ServicesObj.OBJECT_ID));
        obj.setUpdatedAt(JsonHandle.getString(json, ServiceObj.UPDATE_AT));
        obj.setAddress(JsonHandle.getString(json, ServiceObj.ADDRESS));
        obj.setBusiness_license(JsonHandle.getJSON(json, ServicesObj.BUSINESS_LICENSE));
        obj.setContact(JsonHandle.getString(json, ServicesObj.CONTACT));
        obj.setCover(JsonHandle.getJSON(json, ServicesObj.COVER));
        obj.setCreatedAt(JsonHandle.getString(json, ServicesObj.CREATED_AT));
        obj.setDescript(JsonHandle.getString(json, ServicesObj.DESCRIPT));
        obj.setGeo(JsonHandle.getJSON(json, "geo"));
        obj.setIcon(JsonHandle.getString(json, ServicesObj.ICON));
        obj.setId_card_number(JsonHandle.getString(json, ServicesObj.ID_CARD_NUMBER));
        obj.setImages(JsonHandle.getArray(json, ServicesObj.IMAGE));
        obj.setLinkman(JsonHandle.getString(json, ServicesObj.LINKMAN));
        obj.setName(JsonHandle.getString(json, ServicesObj.NAME));
        obj.setRate(JsonHandle.getInt(json, ServicesObj.RATE));
        obj.setServiceType(JsonHandle.getString(json, ServicesObj.SERVICE_TYPE));
        obj.setUpdatedAt(JsonHandle.getString(json, ServicesObj.UPDATED_AT));

        return obj;
    }

    private static ServicesObj mServicesObj;

    public static void saveServicesObj(ServicesObj obj) {
        if (mServicesObj != null) {
            mServicesObj = null;
        }
        mServicesObj = obj;
    }

    public static ServicesObj getServicesObj() {
        return mServicesObj;
    }

}
