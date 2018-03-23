package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;

/**
 * Created by wy on 2017/12/4.
 * 头像
 */

public class UserPicStore implements Serializable{
//    是	int	图片id
    private String picUrl;//图片路径
    private int userId;//用户id
    private int userPicStoreId;//图片id
    private boolean isEdit = false;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserPicStoreId() {
        return userPicStoreId;
    }

    public void setUserPicStoreId(int userPicStoreId) {
        this.userPicStoreId = userPicStoreId;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }
}
