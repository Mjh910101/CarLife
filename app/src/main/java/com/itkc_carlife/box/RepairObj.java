package com.itkc_carlife.box;

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
public class RepairObj {

    public final static String OBJECT_ID = "objectId";
    public final static String DESCRIPT = "descript";
    public final static String CREATED_AT = "createdAt";
    public final static String ORDER_STATUS = "order_status";
    public final static String ADDRESS = "address";
    public final static String EXPECT = "expect";
    public final static String LONGITUDE = "longitude";
    public final static String LATITUDE = "latitude";
    public final static String COMMENT = "comment";
    public final static String EXPECT_WORK_DAY = "expectworkdays";

    String objectId;
    String descript;
    String createdAt;
    String order_status;
    String address;
    String expect;
    String fee;
    int days;
    double longitude;
    double latitude;
    List<Area> areaList;
    Video video;
    RepairStoreObj storeObj;
    int isSettling;
    int expectworkdays;
    ServersCommentObj comment;

    public String getDayText() {
        try {
            return String.valueOf(days);
        } catch (Exception e) {
            return "";
        }
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getExpectworkdays() {
        return expectworkdays;
    }

    public void setExpectworkdays(int expectworkdays) {
        this.expectworkdays = expectworkdays;
    }

    public boolean isHaveComment() {
        return comment != null;
    }

    public ServersCommentObj getComment() {
        return comment;
    }

    public void setComment(ServersCommentObj comment) {
        this.comment = comment;
    }

    public boolean isSettling() {
        return isSettling == 1;
    }

    public void setIsSettling(int isSettling) {
        this.isSettling = isSettling;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public RepairStoreObj getStoreObj() {
        return storeObj;
    }

    public void setStoreObj(RepairStoreObj storeObj) {
        this.storeObj = storeObj;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getExpect() {
        if (expect == null || expect.equals("null")) {
            return "";
        }
        return expect;
    }

    public void setExpect(String expect) {
        this.expect = expect;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setAreaList(JSONArray array) {
        areaList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            Area obj = new Area(JsonHandle.getJSON(array, i));
            areaList.add(obj);
        }
    }

    public void setVideo(JSONObject json) {
        this.video = new Video(json);
    }

    public List<Area> getAreaList() {
        return areaList;
    }

    public Video getVideo() {
        return video;
    }

    public List<String> getAreaImageList() {
        List<String> list = new ArrayList<>();
        if (areaList != null) {
            for (int i = 0; i < areaList.size(); i++) {
                list.addAll(areaList.get(i).getImageList());
            }
        }
        return list;
    }

    public String getAreaListSize() {
        return String.valueOf(getAreaImageList().size());
    }

    public String getExpectworkdaysText() {
        try {
            if (expectworkdays <= 0) {
                return "";
            }
            return String.valueOf(expectworkdays);
        } catch (Exception e) {
            return "";
        }
    }
}
