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
 * Created by Hua on 16/1/18.
 */
public class SettlingObj {

    UserCarInfoObj carInfo;
    UserClaimsInfoObj insurance;
    UserObj user;
    List<Area> areaList;
    Video video;
    Detail detail;

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

    public void setDetail(JSONObject json) {
        this.detail = new Detail(json);
    }

    public List<Area> getAreaList() {
        return areaList;
    }

    public Video getVideo() {
        return video;
    }

    public Detail getDetail() {
        return detail;
    }

    public UserCarInfoObj getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(UserCarInfoObj carInfo) {
        this.carInfo = carInfo;
    }

    public UserClaimsInfoObj getInsurance() {
        return insurance;
    }

    public void setInsurance(UserClaimsInfoObj insurance) {
        this.insurance = insurance;
    }

    public UserObj getUser() {
        return user;
    }

    public void setUser(UserObj user) {
        this.user = user;
    }

    public String getAreaNameList() {
        StringBuffer sb = new StringBuffer();
        for (Area obj : areaList) {
            sb.append(obj.getArea());
            sb.append(",");
        }
        if (sb.length() > 1) {
            return sb.substring(0, sb.length()).toString();
        }
        return sb.toString();
    }

    public List<String> getAreaImageList() {
        List<String> list = new ArrayList<>();

        for (int i = 0; i < areaList.size(); i++) {
            list.addAll(areaList.get(i).getImageList());
        }

        return list;
    }

    public class Detail {
        String objectId;
        String date;
        String address;
        String descript;
        String createdAt;
        double longitude;
        double latitude;

        Detail(JSONObject json) {
            this.objectId = JsonHandle.getString(json, "objectId");
            this.date = JsonHandle.getString(json, "date");
            this.address = JsonHandle.getString(json, "address");
            this.descript = JsonHandle.getString(json, "descript");
            this.createdAt = JsonHandle.getString(json, "createdAt");
            JSONObject point = JsonHandle.getJSON(json, "point");
            this.longitude = JsonHandle.getDouble(point, "longitude");
            this.latitude = JsonHandle.getDouble(point, "latitude");
        }

        public String getObjectId() {
            return objectId;
        }

        public String getDate() {
            return date;
        }

        public String getAddress() {
            return address;
        }

        public String getDescript() {
            return descript;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public double getLongitude() {
            return longitude;
        }

        public double getLatitude() {
            return latitude;
        }
    }


}
