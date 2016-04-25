package com.itkc_carlife.http;

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
public class UrlHandler {

    //    private final static String index = "http://192.168.1.237:3000";
    private final static String index = "http://carlive.leanapp.cn";
//    private final static String index = "http://stg-carlive.leanapp.cn";
    //    private final static String index = "http://dev.carlive.avosapps.com";
    private final static String api = "/api/v1";

    public final static String getIndex() {
        return index;
    }

    private final static String getApiIndex() {
        return getIndex() + api;
    }

    public final static String getLoginRequestSms() {
        return getApiIndex() + "/login/request/sms";
    }

    public static String getLogin() {
        return getApiIndex() + "/login";
    }

    public static String getUserInfo() {
        return getApiIndex() + "/user/info";
    }

    public static String getUserCarInfo() {
        return getApiIndex() + "/user/car_info";
    }

    public static String getUserInsuranceInfo() {
        return getApiIndex() + "/user/insurance_info";
    }

    public static String getDictCity() {
        return getApiIndex() + "/dict/city";
    }

    public static String getUserPayShow() {
        return getApiIndex() + "/user/pay/show";
    }

    public static String getUserPayAction() {
        return getApiIndex() + "/user/pay/action";
    }

    public static String getUserBalance() {
        return getApiIndex() + "/user/balance";
    }

    public static String getServiceScan() {
        return getApiIndex() + "/services/scan";
    }

    public static String getUserSettlingCreate() {
        return getApiIndex() + "/user/settling/create";
    }

    public static String getUserSettlingConfirm() {
        return getApiIndex() + "/user/settling/confirm";
    }

    public static String getUserSettlingDetail() {
        return getApiIndex() + "/user/settling/detail";
    }

    public static String getUserSettlingArea() {
        return getApiIndex() + "/user/settling/area";
    }

    public static String getUserSettlingVideo() {
        return getApiIndex() + "/user/settling/video";
    }

    public static String getSettling() {
        return getApiIndex() + "/settling";
    }

    public static String getUserSettlingOrder() {
        return getApiIndex() + "/user/settling/order";
    }

    public static String getUserRepair() {
        return getApiIndex() + "/user/repair";
    }

    public static String getUserRepairArea() {
        return getApiIndex() + "/user/repair/area";
    }

    public static String getUserRepairVideo() {
        return getApiIndex() + "/user/repair/video";
    }

    public static String getRepair() {
        return getApiIndex() + "/repair";
    }

    public static String getUserRepairOrder() {
        return getApiIndex() + "/user/repair/order";
    }

    public static String getUserRepairGrab() {
        return getApiIndex() + "/user/repair/grab";
    }

    public static String getUserRepairOrderConfirm() {
        return getApiIndex() + "/user/repair/order/confirm";
    }

    public static String getUserRepairPay() {
        return getApiIndex() + "/user/repair/pay";
    }

    public static String getPost() {
        return getApiIndex() + "/post";
    }

    public static String getDictPostType() {
        return getApiIndex() + "/dict/postType";
    }

    public static String getUserPostAdd() {
        return getApiIndex() + "/user/post/add";
    }

    public static String getUserPost() {
        return getApiIndex() + "/user/post";
    }


    public static String getPostJoin() {
        return getApiIndex() + "/user/post/join";
    }

    public static String getUserPostFavor() {
        return getApiIndex() + "/user/post/favor";
    }

    public static String getFavorPostRemove() {
        return getApiIndex() + "/favor/post/remove";
    }

    public static String getFavorPostAdd() {
        return getApiIndex() + "/favor/post/add";
    }

    public static String getJoinPostRemove() {
        return getApiIndex() + "/join/post/remove";
    }

    public static String getJoinPostAdd() {
        return getApiIndex() + "/join/post/add";
    }

    public static String getUserSettlingOrderDetail() {
        return getApiIndex() + "/user/settling/order/detail";
    }

    public static String getNearServices() {
        return getApiIndex() + "/near/services";
    }

    public static String getServicesDetail() {
        return getApiIndex() + "/services/detail";
    }


    public static String getServicesComment() {
        return getApiIndex() + "/services/comment";
    }

    public static String getUserCharge() {
        return getApiIndex() + "/user/charge";
    }

    public static String getUserRepairOrderSettling() {
        return getApiIndex() + "/user/repair/order/settling";
    }

    public static String getUserRepairOrderRefresh() {
        return getApiIndex() + "/user/repair/order/refresh";
    }
}
