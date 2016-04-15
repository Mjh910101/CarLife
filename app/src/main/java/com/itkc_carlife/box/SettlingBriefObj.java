package com.itkc_carlife.box;

import java.text.DecimalFormat;

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
 * Created by Hua on 16/1/19.
 */
public class SettlingBriefObj {

    public final static String SETTLING_TYPE = "settling_type";
    public final static String SETTLING_ORDER_ID = "settlingOrderId";
    public final static String SETTLING_ID = "settlingId";
    public final static String CREATED_AT = "createdAt";
    public final static String PAY = "pay";
    public final static String FEEDBACK = "feedback";
    public final static String ORDER_STATUS_TITLE = "order_status_title";
    public final static String ORDER_STATUS = "order_status";

    private String settling_type;
    private String settlingOrderId;
    private String settlingId;
    private String createdAt;
    private String pay;
    private String feedback;
    private String order_status_title;
    private int order_status;

    public String getSettling_type() {
        return settling_type;
    }

    public void setSettling_type(String settling_type) {
        this.settling_type = settling_type;
    }

    public String getSettlingOrderId() {
        return settlingOrderId;
    }

    public void setSettlingOrderId(String settlingOrderId) {
        this.settlingOrderId = settlingOrderId;
    }

    public String getSettlingId() {
        return settlingId;
    }

    public void setSettlingId(String settlingId) {
        this.settlingId = settlingId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPay() {
        return pay;
    }

    public String getPayText() {
        try {
            return new DecimalFormat("0.00").format(pay);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getOrder_status_title() {
        if (order_status_title == null || order_status_title.equals("null")) {
            return "";
        }
        return order_status_title;
    }

    public void setOrder_status_title(String order_status_title) {
        this.order_status_title = order_status_title;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public boolean isMajor() {
        return getSettling_type().equals("major");
    }
}
