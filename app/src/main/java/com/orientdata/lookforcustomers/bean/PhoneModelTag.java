package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wy on 2017/11/19.
 */

public class PhoneModelTag implements Serializable {
    private String tagName;//机型名
    private int parentId;//父id
    private int type;//机型
    private int interestTagImportId;//自增id
    private boolean isChecked = false;
    private boolean isClicked = false;
    private boolean enable = true;



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

    public int getInterestTagImportId() {
        return interestTagImportId;
    }

    public void setInterestTagImportId(int interestTagImportId) {
        this.interestTagImportId = interestTagImportId;
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
