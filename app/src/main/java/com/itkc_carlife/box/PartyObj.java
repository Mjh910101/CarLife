package com.itkc_carlife.box;

import com.itkc_carlife.box.handler.UserObjHandler;
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
public class PartyObj {

    public final static String ADDRESS = "address";
    public final static String UPDATE_AT = "updatedAt";
    public final static String OBJECT_ID = "objectId";
    public final static String DESCRIPT = "descript";
    public final static String COVER = "cover";
    public final static String CREATED_AT = "createdAt";
    public final static String TYPE = "type";
    public final static String TITLE = "title";
    public final static String LONGITUDE = "longitude";
    public final static String LATIUDE = "latitude";
    public final static String LIMIT = "limit";
    public final static String END_DATA_STR = "endDateStr";
    public final static String END_TIME_STR = "endTimeStr";
    public final static String START_DATA_STR = "startDateStr";
    public final static String START_TIME_STR = "startTimeStr";
    public final static String IS_FAVOR = "isfavor";
    public final static String IS_JOIN = "isjoin";
    public final static String JOIN_LIST = "joinlist";
    public final static String STATUS_LABEL = "statusLabel";
    public final static String STATUS = "status";
    public final static String JOIN_TOTAL = "join_total";


    String address;
    String updatedAt;
    String endDateStr;
    String endTimeStr;
    String startDateStr;
    String startTimeStr;
    String objectId;
    String descript;
    String coverUrl;
    String createdAt;
    String type;
    String title;
    String statusLabel;
    int status;
    int join_total;
    double longitude;
    double latitude;
    int limit;
    int isfavor;
    int isjoin;
    int isshow;
    List<UserObj> joinlist;

    public int getJoin_total() {
        return join_total;
    }

    public void setJoin_total(int join_total) {
        this.join_total = join_total;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setJoinlist(JSONObject json) {
        joinlist = new ArrayList<>();
        if (json == null) {
            isshow = 0;
            return;
        }
        isshow = JsonHandle.getInt(json, "show");
        JSONArray array = JsonHandle.getArray(json, "data");
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                joinlist.add(UserObjHandler.getUserObj(JsonHandle.getJSON(array, i)));
            }
        }
    }

    public boolean isShow() {
        return isshow == 1;
    }

    public List<UserObj> getJoinlist() {
        return joinlist;
    }

    public int getJoinlistSize() {
        if (joinlist == null) {
            return 0;
        }
        return joinlist.size();
    }

    public boolean isfavor() {
        return isfavor == 1;
    }

    public void setIsfavor(int isfavor) {
        this.isfavor = isfavor;
    }

    public boolean isjoin() {
        return isjoin == 1;
    }

    public void setIsjoin(int isjoin) {
        this.isjoin = isjoin;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setStartDateStr(String startDateStr) {
        this.startDateStr = startDateStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStartTime() {
        return startDateStr + startTimeStr;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getEndTime() {
        return endDateStr + endTimeStr;
    }

    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(JSONObject json) {
        if (json == null) {
            this.coverUrl = "";
            return;
        }
        this.coverUrl = JsonHandle.getString(json, "url");
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLongitude() {
        return longitude;
    }

    private void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    private void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setPoint(JSONObject json) {
        if (json == null) {
            setLatitude(0);
            setLongitude(0);
            return;
        }

        setLatitude(JsonHandle.getDouble(json, LATIUDE));
        setLongitude(JsonHandle.getDouble(json, LONGITUDE));

    }

    public String getInfo() {
        return getLimit() + "人/" + getStartTime() + " 至 " + getEndTime() + "/" + getAddress();
    }

    public boolean isOutTime() {
        return status == 2;
    }
}
