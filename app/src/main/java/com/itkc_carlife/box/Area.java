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
public class Area {
    String area;
    //        String imageId;
//        String image;
    String objectId;

    List<String> imageList;
    List<String> imageIdList;

    public Area(JSONObject json) {
        this.objectId = JsonHandle.getString(json, "objectId");
        this.area = JsonHandle.getString(json, "area");
        JSONArray image = JsonHandle.getArray(json, "images");
//            this.imageId = JsonHandle.getString(image, "objectId");
//            this.image = JsonHandle.getString(image, "url");
        this.imageList = new ArrayList<>();
        this.imageIdList = new ArrayList<>();
        for (int i = 0; i < image.length(); i++) {
            JSONObject jsonObject = JsonHandle.getJSON(image, i);
            imageIdList.add(JsonHandle.getString(jsonObject, "objectId"));
            imageList.add(JsonHandle.getString(jsonObject, "url"));
        }
    }

    public List<String> getImageList() {
        return imageList;
    }

    public List<String> getImageIdList() {
        return imageIdList;
    }

    public String getArea() {
        return area;
    }


    public String getObjectId() {
        return objectId;
    }
}
