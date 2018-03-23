package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wy on 2017/12/19.
 *
 * 消息Bean
 */

public class Result implements Serializable{
    private int pushMessageId;//自增id
    private String title;//标题
    private	String text;//内容
    private int userId;
    private int objectId;//1 未读 2已读
    private String createTime;
    private String updateTime;
    private boolean isEdit;

    public int getPushMessageId() {
        return pushMessageId;
    }

    public void setPushMessageId(int pushMessageId) {
        this.pushMessageId = pushMessageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }


    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
