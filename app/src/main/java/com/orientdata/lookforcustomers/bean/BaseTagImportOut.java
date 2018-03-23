package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wy on 2017/11/19.
 * 机型
 */

public class BaseTagImportOut implements Serializable {
    private List<PhoneModelTag> erp;//二级分类
    private String tagName;//机型名
    private int parentId;//父id
    private int type;//机型
    private int baseTagImportId;//自增id
    private boolean isChecked = false;

    public List<PhoneModelTag> getErp() {
        return erp;
    }

    public void setErp(List<PhoneModelTag> erp) {
        this.erp = erp;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBaseTagImportId() {
        return baseTagImportId;
    }

    public void setBaseTagImportId(int baseTagImportId) {
        this.baseTagImportId = baseTagImportId;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
