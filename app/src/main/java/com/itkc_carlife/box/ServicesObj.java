package com.itkc_carlife.box;

import com.itkc_carlife.R;
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
public class ServicesObj {

    public final static String ADDRESS = "address";
    public final static String ID_CARD_NUMBER = "id_card_number";
    public final static String UPDATED_AT = "updatedAt";
    public final static String IMAGE = "images";
    public final static String NAME = "name";
    public final static String OBJECT_ID = "objectId";
    public final static String DESCRIPT = "descript";
    public final static String COVER = "cover";
    public final static String CREATED_AT = "createdAt";
    public final static String ICON = "icon";
    public final static String SERVICE_TYPE = "serviceType";
    public final static String BUSINESS_LICENSE = "business_license";
    public final static String LINKMAN = "linkman";
    public final static String CONTACT = "contact";
    public final static String RATE = "rate";

    private String address;
    private String id_card_number;
    private String updatedAt;
    //    private String images;
    private String name;
    private String objectId;
    private String descript;
    private String cover;
    private String createdAt;
    private String icon;
    private String serviceType;
    private String business_license;
    private String linkman;
    private String contact;
    private int rate;
    private double longitude;
    private double latitude;
    private List<String> imageList;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId_card_number() {
        return id_card_number;
    }

    public void setId_card_number(String id_card_number) {
        this.id_card_number = id_card_number;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
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
        if (descript == null || descript.equals("null")) {
            return "";
        }
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(JSONObject json) {
        if (json == null) {
            cover = "";
        } else {
            cover = JsonHandle.getString(json, "url");
        }
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getBusiness_license() {
        return business_license;
    }

    public void setBusiness_license(JSONObject json) {
        if (json == null) {
            business_license = "";
        } else {
            business_license = JsonHandle.getString(json, "url");
        }
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getContact() {
        if (contact == null || contact.equals("null")) {
            return "";
        }
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getRate() {
        return rate;
//        return 3;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setGeo(JSONObject json) {
        if (json == null) {
            latitude = 0;
            longitude = 0;
        } else {
            latitude = JsonHandle.getDouble(json, "latitude");
            longitude = JsonHandle.getDouble(json, "longitude");
        }

    }

    public boolean isHaveCoordinate() {
        return latitude != 0 && longitude != 0;
    }

    public int getIconDrawble() {
        switch (getIcon()) {
            case "001":
                return R.drawable.map_002;
            case "002":
                return R.drawable.map_001;
            case "003":
                return R.drawable.map_005;
            case "004":
                return R.drawable.map_006;
            case "005":
                return R.drawable.map_003;
            case "006":
                return R.drawable.map_008;
            case "007":
                return R.drawable.map_001;
            default:
                return R.drawable.map_002;
//                return R.drawable.map_007;
//                return R.drawable.map_004;
        }
    }
}
