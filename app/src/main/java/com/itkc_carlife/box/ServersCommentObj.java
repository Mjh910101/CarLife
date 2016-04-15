package com.itkc_carlife.box;

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
 * Created by Hua on 16/2/18.
 */
public class ServersCommentObj {

    public final static String USER = "user";
    public final static String CONTENT = "content";
    public final static String RATE = "rate";
    public final static String CREATED_AT = "createdAt";
    public final static String UPDARED_AT = "updatedAt";
    public final static String OBJECT_ID = "objectId";

    UserObj user;
    String content;
    int rate;
    String createdAt;
    String updatedAt;
    String objectId;
    private String userGoneTel;

    public UserObj getUser() {
        return user;
    }

    public void setUser(UserObj user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUserTel() {
        if (user == null) {
            return "";
        }
        return user.getMobilePhoneNumber();
    }

    public String getUserGoneTel() {
        if (user == null) {
            return "";
        }
        return user.getGoneMobilePhoneNumber();
    }
}
