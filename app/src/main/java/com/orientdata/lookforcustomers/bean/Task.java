package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wy on 2017/12/5.
 * 任务
 */

public class Task implements Serializable {
    private int taskId;//任务id
    private String taskNo;//任务编号
    private String taskName;//任务名称
    private int userId;//用户id
    private int type;//1 短信 2页面
    //1 审核中(平台) 2审核未通过(平台) 3准备中(运营商) 4审核未通过(运营商) 5准备中 6待投放 7投放中 8投放完 9暂停任务 10恢复任务 11终止任务
    private int status;
    private String rangeRadius;//半径范围
    private BigDecimal budget;//任务预算
    private BigDecimal actualAmount;//	实际钱数
    private int invoiceStatus;//1 未开发票 2已开发票
    private int orientationSettingsId;//定向设置id
    private Date throwStartdate;//开始日期
    private Date throwEnddate;//结束日期
    private int estimatePeoplerno;//预计覆盖人数
    private String content;//短信内容
    private String provinceCode;//省code
    private String cityCode;//区域code
    private String throwAddress;//投放地址
    private Date createDate;//创建日期
    private int delFlag;//删除状态 0正常 1 删除
    private BigDecimal longitude;//经度
    private BigDecimal dimension;//纬度
    private String adImgid;//广告图片
    private String adLink;//网页链接
    private String templateUrl;//落地页图片访问路径
    private int templateId;//落地页id

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRangeRadius() {
        return rangeRadius;
    }

    public void setRangeRadius(String rangeRadius) {
        this.rangeRadius = rangeRadius;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public int getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(int invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public int getOrientationSettingsId() {
        return orientationSettingsId;
    }

    public void setOrientationSettingsId(int orientationSettingsId) {
        this.orientationSettingsId = orientationSettingsId;
    }

    public Date getThrowStartdate() {
        return throwStartdate;
    }

    public void setThrowStartdate(Date throwStartdate) {
        this.throwStartdate = throwStartdate;
    }

    public Date getThrowEnddate() {
        return throwEnddate;
    }

    public void setThrowEnddate(Date throwEnddate) {
        this.throwEnddate = throwEnddate;
    }

    public int getEstimatePeoplerno() {
        return estimatePeoplerno;
    }

    public void setEstimatePeoplerno(int estimatePeoplerno) {
        this.estimatePeoplerno = estimatePeoplerno;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getThrowAddress() {
        return throwAddress;
    }

    public void setThrowAddress(String throwAddress) {
        this.throwAddress = throwAddress;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getDimension() {
        return dimension;
    }

    public void setDimension(BigDecimal dimension) {
        this.dimension = dimension;
    }

    public String getAdImgid() {
        return adImgid;
    }

    public void setAdImgid(String adImgid) {
        this.adImgid = adImgid;
    }

    public String getAdLink() {
        return adLink;
    }

    public void setAdLink(String adLink) {
        this.adLink = adLink;
    }

    public String getTemplateUrl() {
        return templateUrl;
    }

    public void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }
}
