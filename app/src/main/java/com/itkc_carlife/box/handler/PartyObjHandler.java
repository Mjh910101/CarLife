package com.itkc_carlife.box.handler;

import com.itkc_carlife.box.PartyObj;
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
 * Created by Hua on 16/2/1.
 */
public class PartyObjHandler {

    public static List<PartyObj> getPartyObjList(JSONArray array) {
        List<PartyObj> list = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            list.add(getPartyObj(JsonHandle.getJSON(array, i)));
        }

        return list;
    }

    public static PartyObj getPartyObj(JSONObject json) {
        PartyObj obj = new PartyObj();

        obj.setObjectId(JsonHandle.getString(json, PartyObj.OBJECT_ID));
        obj.setAddress(JsonHandle.getString(json, PartyObj.ADDRESS));
        obj.setCoverUrl(JsonHandle.getJSON(json, PartyObj.COVER));
        obj.setCreatedAt(JsonHandle.getString(json, PartyObj.CREATED_AT));
        obj.setDescript(JsonHandle.getString(json, PartyObj.DESCRIPT));
        obj.setPoint(JsonHandle.getJSON(json, "point"));
        obj.setTitle(JsonHandle.getString(json, PartyObj.TITLE));
        obj.setType(JsonHandle.getString(json, PartyObj.TYPE));
        obj.setUpdatedAt(JsonHandle.getString(json, PartyObj.UPDATE_AT));
        obj.setLimit(JsonHandle.getInt(json, PartyObj.LIMIT));
        obj.setStartDateStr(JsonHandle.getString(json, PartyObj.START_DATA_STR));
        obj.setStartTimeStr(JsonHandle.getString(json, PartyObj.START_TIME_STR));
        obj.setEndDateStr(JsonHandle.getString(json, PartyObj.END_DATA_STR));
        obj.setEndTimeStr(JsonHandle.getString(json, PartyObj.END_TIME_STR));
        obj.setIsfavor(JsonHandle.getInt(json, PartyObj.IS_FAVOR));
        obj.setIsjoin(JsonHandle.getInt(json, PartyObj.IS_JOIN));
        obj.setJoinlist(JsonHandle.getJSON(json, PartyObj.JOIN_LIST));
        obj.setStatusLabel(JsonHandle.getString(json, PartyObj.STATUS_LABEL));
        obj.setStatus(JsonHandle.getInt(json, PartyObj.STATUS));
        obj.setJoin_total(JsonHandle.getInt(json,PartyObj.JOIN_TOTAL));

        return obj;
    }

}
