package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;

/**
 * Created by ckx on 2018/6/29.
 */

public class MessageAndNoticeBean implements Serializable {
    private static final long serialVersionUID = 1969228895889034895L;


    /**
     * err : {"code":0,"msg":"正常","eventId":""}
     * result : [{"title":"测试公告","type":1,"content":"测试公告内容","createDate":"2018-03-27 15:59:49","delFlag":0,"status":1,"keyword":"test","toptag":2,"lookStatus":0,"announcementId":1},{"title":"测试公告2","type":1,"content":"测试公告2","createDate":"2018-03-27 16:01:32","delFlag":0,"status":1,"keyword":"test","toptag":1,"lookStatus":0,"announcementId":2},{"title":"充值成功","text":"尊敬的用户,您于2018-06-27 04:56:14充值0.01元,订单号为2120180627165631218670","phoneType":"android","acivity":"SystemNotifaction","createTime":"2018-06-27 16:56:31","updateTime":"2018-06-27 16:56:31","status":0,"userType":1,"userId":5,"type":5,"objectId":1,"objectProperty":0,"saveRedMessage":true,"saveLetter":true,"pushMessageId":13519},{"title":"充值成功","text":"尊敬的用户,您于2018-06-27 04:57:58充值0.01元,订单号为1120180627165805921317","phoneType":"android","acivity":"SystemNotifaction","createTime":"2018-06-27 16:58:05","updateTime":"2018-06-27 16:58:05","status":0,"userType":1,"userId":5,"type":5,"objectId":1,"objectProperty":0,"saveRedMessage":true,"saveLetter":true,"pushMessageId":13520}]
     */

        /**
         * title : 测试公告
         * type : 1
         * content : 测试公告内容
         * createDate : 2018-03-27 15:59:49
         * delFlag : 0
         * status : 1
         * keyword : test
         * toptag : 2
         * lookStatus : 0
         * announcementId : 1
         * text : 尊敬的用户,您于2018-06-27 04:56:14充值0.01元,订单号为2120180627165631218670
         * phoneType : android
         * acivity : SystemNotifaction
         * createTime : 2018-06-27 16:56:31
         * updateTime : 2018-06-27 16:56:31
         * userType : 1
         * userId : 5
         * objectId : 1
         * objectProperty : 0
         * saveRedMessage : true
         * saveLetter : true
         * pushMessageId : 13519
         */

        private String title;
        private int type;
        private String content;
        private String createDate;
        private int delFlag;
        private int status;
        private String keyword;
        private int toptag;
        private int lookStatus = -100;
        private int announcementId;
        private String text;
        private String phoneType;
        private String acivity;
        private String createTime;
        private String updateTime;
        private int userType;
        private int userId;
        private int objectId = -100;
        private int objectProperty;
        private boolean saveRedMessage;
        private boolean saveLetter;
        private int pushMessageId;



        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public int getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(int delFlag) {
            this.delFlag = delFlag;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public int getToptag() {
            return toptag;
        }

        public void setToptag(int toptag) {
            this.toptag = toptag;
        }

        public int getLookStatus() {
            return lookStatus;
        }

        public void setLookStatus(int lookStatus) {
            this.lookStatus = lookStatus;
        }

        public int getAnnouncementId() {
            return announcementId;
        }

        public void setAnnouncementId(int announcementId) {
            this.announcementId = announcementId;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getPhoneType() {
            return phoneType;
        }

        public void setPhoneType(String phoneType) {
            this.phoneType = phoneType;
        }

        public String getAcivity() {
            return acivity;
        }

        public void setAcivity(String acivity) {
            this.acivity = acivity;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
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

        public int getObjectProperty() {
            return objectProperty;
        }

        public void setObjectProperty(int objectProperty) {
            this.objectProperty = objectProperty;
        }

        public boolean isSaveRedMessage() {
            return saveRedMessage;
        }

        public void setSaveRedMessage(boolean saveRedMessage) {
            this.saveRedMessage = saveRedMessage;
        }

        public boolean isSaveLetter() {
            return saveLetter;
        }

        public void setSaveLetter(boolean saveLetter) {
            this.saveLetter = saveLetter;
        }

        public int getPushMessageId() {
            return pushMessageId;
        }

        public void setPushMessageId(int pushMessageId) {
            this.pushMessageId = pushMessageId;
        }
    }
