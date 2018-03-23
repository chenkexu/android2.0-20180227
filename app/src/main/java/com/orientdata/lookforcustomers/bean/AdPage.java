package com.orientdata.lookforcustomers.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wy
 * on 2017-12-03.
 * 广告页
 */

public class AdPage{
    int pageTemplateId; //; 自增id
    String name; //; 页面名称
    String sopsName;  //商铺名称
    String sopsIphone; //商铺电话
    BigDecimal longitude;  //经度
    BigDecimal dimension; //bigdecimal纬度
    String templateUrl; //String图片链接
    Date createDate; //Date创建日期
    String fenxiangUrl; //String分享链接
    int userId;//int 用户id

    public int getPageTemplateId() {
        return pageTemplateId;
    }

    public void setPageTemplateId(int pageTemplateId) {
        this.pageTemplateId = pageTemplateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSopsName() {
        return sopsName;
    }

    public void setSopsName(String sopsName) {
        this.sopsName = sopsName;
    }

    public String getSopsIphone() {
        return sopsIphone;
    }

    public void setSopsIphone(String sopsIphone) {
        this.sopsIphone = sopsIphone;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getDimension() {
        return dimension;
    }

    public void setDimension(BigDecimal dimension) {
        this.dimension = dimension;
    }

    public String getTemplateUrl() {
        return templateUrl;
    }

    public void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getFenxiangUrl() {
        return fenxiangUrl;
    }

    public void setFenxiangUrl(String fenxiangUrl) {
        this.fenxiangUrl = fenxiangUrl;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
