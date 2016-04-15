package com.itkc_carlife.box;

import com.itkc_carlife.handlers.JsonHandle;

import org.json.JSONArray;

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
public class RepairStoreObj {

    public final static String ADDRESS = "address";
    public final static String NAME = "name";
    public final static String OBJECT_ID = "objectId";
    public final static String DESCRIPT = "descript";
    public final static String CONTACT = "contact";
    public final static String CREATED_AT = "createdAt";
    public final static String FEE = "fee";
    public final static String LINLMAN = "linkman";
    public final static String LONGITUDE = "longitude";
    public final static String LATITUDE = "latitude";
    public final static String IMAGE = "images";
    public final static String DAYS = "days";

    private String address;
    private String name;
    private String objectId;
    private String descript;
    private String contact;
    private String createdAt;
    private String fee;
    private String coverUrl;
    private String linkman;
    private double longitude;
    private double latitude;
    private List<String> imageList;
    private int days;

    public int getDays() {
        if (days <= 0) {
            return 0;
        }
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public List<String> getImages() {
        return imageList;
    }

    public void setImages(JSONArray array) {
        imageList = new ArrayList<>();
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                imageList.add(JsonHandle.getString(JsonHandle.getJSON(array, i), "url"));
            }
        }
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
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
}
