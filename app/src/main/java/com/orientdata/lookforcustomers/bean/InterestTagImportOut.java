package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wy on 2017/11/19.
 * 商业兴趣
 */

public class InterestTagImportOut implements Serializable {
    private List<InterestCategory> eri;//二级兴趣返回
    private String industryName;//兴趣名
    private int parentId;//父id
    private int areaId;//地区码
    private int interestIndustryId;//自增id
    private boolean isChecked = false;

    public List<InterestCategory> getEri() {
        return eri;
    }

    public void setEri(List<InterestCategory> eri) {
        this.eri = eri;
    }

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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setInterestIndustryId(int interestIndustryId) {
        this.interestIndustryId = interestIndustryId;
    }

    @Override
    public String toString() {
        return "InterestTagImportOut{" +
                "eri=" + eri +
                ", industryName='" + industryName + '\'' +
                ", parentId=" + parentId +
                ", areaId=" + areaId +
                ", interestIndustryId=" + interestIndustryId +
                ", isChecked=" + isChecked +
                '}';
    }
}
