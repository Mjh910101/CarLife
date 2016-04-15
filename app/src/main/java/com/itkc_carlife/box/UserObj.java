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
 * Created by Hua on 16/1/7.
 */
public class UserObj {

    public final static String MOBILE_PHONE_NUMBER = "mobilePhoneNumber";
    public final static String USER_NAME = "username";
    public final static String SESSION_TOKEN = "sessiontoken";
    public final static String SEX = "sex";
    public final static String AVATAR = "avatar";
    public final static String BALANCE = "balance";

    private String mobilePhoneNumber;
    private String username;
    private String sessiontoken;
    private int sex;
    private String avatar;
    private String avatarId;
    private double balance;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(String avatarId) {
        this.avatarId = avatarId;
    }

    public String getAvatar() {
        if (avatar == null) {
            return "";
        }
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public String getGoneMobilePhoneNumber(){
        return getGoneMobilePhoneNumber(mobilePhoneNumber);
    }

    public static String getGoneMobilePhoneNumber(String tel){
        if(tel.length()>=11){
            tel = tel.substring(0, 6) + "****" + tel.substring(tel.length() - 1, tel.length());
        }
        return tel;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSessiontoken() {
        return sessiontoken;
    }

    public void setSessiontoken(String sessiontoken) {
        this.sessiontoken = sessiontoken;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
