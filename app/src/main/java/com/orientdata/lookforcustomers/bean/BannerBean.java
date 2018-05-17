package com.orientdata.lookforcustomers.bean;

import java.util.Date;

/**
 * Created by wy
 * on 2017-12-03.
 */

public class BannerBean {
    int bannerId;//是 int 自增id
    int delFlag;//是 int 删除状态 0正常 1删除
    Date createDate;//是 Date 创建时间
    String imageId;//是 String 图片路径
    String title;//是 String 图片路径
    String imageUrl;//是 String 点击图片跳转的链接
    int sort;//是 int 排序大小


    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBannerId() {
        return bannerId;
    }

    public void setBannerId(int bannerId) {
        this.bannerId = bannerId;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
