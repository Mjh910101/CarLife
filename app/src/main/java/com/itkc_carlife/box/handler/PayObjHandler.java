package com.itkc_carlife.box.handler;

import com.itkc_carlife.box.PayObj;
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
 * Created by Hua on 16/1/12.
 */
public class PayObjHandler {

    public static List<PayObj> getPayObjList(JSONArray array) {
        List<PayObj> list = new ArrayList<PayObj>(array.length());

        for (int i = 0; i < array.length(); i++) {
            list.add(getPayObj(JsonHandle.getJSON(array, i)));
        }

        return list;
    }

    private static PayObj getPayObj(JSONObject json) {
        PayObj obj = new PayObj();

        obj.setObjectId(JsonHandle.getString(json, PayObj.OBJECT_ID));
        obj.setCreatedAt(JsonHandle.getString(json, PayObj.CREATED_AT));
        obj.setDescript(JsonHandle.getString(json, PayObj.DESCRIPT));
        obj.setTotal(JsonHandle.getDouble(json, PayObj.TOTAL));

        JSONObject serverJson = JsonHandle.getJSON(json, "services");
        if (serverJson != null) {
            obj.setServiceObj(ServiceObjHandler.getServiceObj(serverJson));
        }

        return obj;
    }

}
