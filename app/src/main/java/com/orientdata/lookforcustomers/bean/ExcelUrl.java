package com.orientdata.lookforcustomers.bean;

/**
 * Created by wy on 2017/12/21.
 * 报表导出链接
 */

public class ExcelUrl {
    private String excUrl;
    private String startTime;
    private String endTime;
    private String userId;

    public String getExcUrl() {
        return excUrl;
    }

    public void setExcUrl(String excUrl) {
        this.excUrl = excUrl;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
