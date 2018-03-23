package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wy on 2017/12/3.
 * 图片模版
 */

public class IndustryTemplate implements Serializable{
    private int tradeId;//模板id
    private String imageId;//图片路径
    private int sizeId;//尺寸ID
    private Date createDate;//创建日期
    private int industryTemplateId;//自增id
    private boolean isChecked;

    public int getTradeId() {
        return tradeId;
    }

    public void setTradeId(int tradeId) {
        this.tradeId = tradeId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public int getSizeId() {
        return sizeId;
    }

    public void setSizeId(int sizeId) {
        this.sizeId = sizeId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getIndustryTemplateId() {
        return industryTemplateId;
    }

    public void setIndustryTemplateId(int industryTemplateId) {
        this.industryTemplateId = industryTemplateId;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
