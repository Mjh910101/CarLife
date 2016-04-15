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
 * Created by Hua on 16/1/12.
 */
public class PayObj {

    public final static String TOTAL = "total";
    public final static String DESCRIPT = "descript";
    public final static String OBJECT_ID = "objectId";
    public final static String CREATED_AT = "createdAt";

    private double total;
    private String descript;
    private String objectId;
    private String createdAt;
    private ServiceObj serviceObj;

    public double getTotal() {
        return total;
    }

    public String getTotalText() {
        return new DecimalFormat("0.00").format(total);
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public ServiceObj getServiceObj() {
        return serviceObj;
    }

    public void setServiceObj(ServiceObj serviceObj) {
        this.serviceObj = serviceObj;
    }
}
