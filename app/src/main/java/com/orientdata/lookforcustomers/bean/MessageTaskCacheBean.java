package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;

/**
 * Created by 陈柯旭 on 2018/5/3.
 */

public class MessageTaskCacheBean implements Serializable {

    //签名
    //短信内容
    //开始时间.结束时间
    //任务预算
//    startdate = tvDateFrom.getText().toString().trim();
//    enddate = tvDateTo.getText().toString().trim();
//    content = etMsgContent.getText().toString().trim();
//    budget = etBudget.getText().toString().trim();
//    String signStr = etEnterpriseSignature.getText().toString().trim();
//    String contentAll = content + tvUnsubscribe.getText().toString();
    private String signStr;
    private String budget;
    private String startdate;
    private String enddate;
    private String testCmPhone;
    private String testCuPhone;
    private String testCtPhone;
    private String content;

    public String getTestCmPhone() {
        return testCmPhone == null ? "" : testCmPhone;
    }

    public void setTestCmPhone(String testCmPhone) {
        this.testCmPhone = testCmPhone;
    }

    public String getTestCuPhone() {
        return testCuPhone == null ? "" : testCuPhone;
    }

    public void setTestCuPhone(String testCuPhone) {
        this.testCuPhone = testCuPhone;
    }

    public String getTestCtPhone() {
        return testCtPhone == null ? "" : testCtPhone;
    }

    public void setTestCtPhone(String testCtPhone) {
        this.testCtPhone = testCtPhone;
    }

    public String getSignStr() {
        return signStr == null ? "" : signStr;
    }

    public void setSignStr(String signStr) {
        this.signStr = signStr;
    }

    public String getBudget() {
        return budget == null ? "" : budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getStartdate() {
        return startdate == null ? "" : startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate == null ? "" : enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    //    public String getBudget() {
//        return budget;
//    }
//
//    public void setBudget(String budget) {
//        this.budget = budget;
//    }
//
//    public String getSignStr() {
//        return signStr;
//    }
//
//    public void setSignStr(String signStr) {
//        this.signStr = signStr;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public String getStartdate() {
//        return startdate;
//    }
//
//    public void setStartdate(String startdate) {
//        this.startdate = startdate;
//    }
//
//    public String getEnddate() {
//        return enddate;
//    }
//
//    public void setEnddate(String enddate) {
//        this.enddate = enddate;
//    }


    @Override
    public String toString() {
        return "MessageTaskCacheBean{" +
                "signStr='" + signStr + '\'' +
                ", budget='" + budget + '\'' +
                ", startdate='" + startdate + '\'' +
                ", enddate='" + enddate + '\'' +
                ", testCmPhone='" + testCmPhone + '\'' +
                ", testCuPhone='" + testCuPhone + '\'' +
                ", testCtPhone='" + testCtPhone + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
