package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;

/**
 * Created by wy on 2017/11/19.
 */

public class InterestCategory implements Serializable {
    private String industryName;//兴趣名
    private int parentId;//父id
    private int areaId;//地区码
    private int interestIndustryId;//自增id
    private boolean isChecked = false;
    private boolean isClicked = false;
    private boolean enable = true;


    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public int getInterestIndustryId() {
        return interestIndustryId;
    }

    public void setInterestIndustryId(int interestIndustryId) {
        this.interestIndustryId = interestIndustryId;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
