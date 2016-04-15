package com.itkc_carlife.box.handler;

import com.itkc_carlife.box.ServiceObj;
import com.itkc_carlife.handlers.JsonHandle;

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
 * Created by Hua on 16/1/12.
 */
public class ServiceObjHandler {

    public static ServiceObj getServiceObj(JSONObject json) {
        ServiceObj obj = new ServiceObj();

        obj.setAddress(JsonHandle.getString(json, ServiceObj.ADDRESS));
        obj.setObjectId(JsonHandle.getString(json, ServiceObj.OBJECT_ID));
        obj.setCreatedAt(JsonHandle.getString(json, ServiceObj.CREATED_AT));
        obj.setName(JsonHandle.getString(json, ServiceObj.NAME));
        obj.setRemark(JsonHandle.getString(json, ServiceObj.REMARK));
        obj.setServiceType(JsonHandle.getString(json, ServiceObj.SERVICE_TYPE));
        obj.setUpdatedAt(JsonHandle.getString(json, ServiceObj.UPDATE_AT));

        return obj;
    }

}
