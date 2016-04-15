package com.itkc_carlife.box.handler;

import com.itkc_carlife.box.ServersCommentObj;
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
 * Created by Hua on 16/2/19.
 */
public class ServersCommentObjHandler {

    public static List<ServersCommentObj> getServersCommentObjList(JSONArray array) {
        List<ServersCommentObj> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(getServersCommentObj(JsonHandle.getJSON(array, i)));
        }
        return list;
    }

    public static ServersCommentObj getServersCommentObj(JSONObject json) {
        ServersCommentObj obj = new ServersCommentObj();

        obj.setObjectId(JsonHandle.getString(json, ServersCommentObj.OBJECT_ID));
        obj.setUpdatedAt(JsonHandle.getString(json, ServersCommentObj.UPDARED_AT));
        obj.setRate(JsonHandle.getInt(json, ServersCommentObj.RATE));
        obj.setContent(JsonHandle.getString(json, ServersCommentObj.CONTENT));
        obj.setCreatedAt(JsonHandle.getString(json, ServersCommentObj.CREATED_AT));
        obj.setUser(UserObjHandler.getUserObj(JsonHandle.getJSON(json, ServersCommentObj.USER)));

        return obj;
    }

}
