package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wy on 2017/12/6.
 * 任务详情
 */

public class TaskOut implements Serializable{
    private BigDecimal xunMoney;//	是	 	消费寻币
    private int showCount;//	是	 	展现量
    private int issuedCount;//	是	 	下发成功量
    private int clickCount;//	是	 	点击量
    private OrientationSettingsOut orientationSettingsOut;//	是		定向设置信息输出
    private int taskId;//	是	int	任务自增id
    private String taskNo;//	是	String	任务编号
    private String taskName;//	是	String	任务名称
    private int userId;//	是	int	用户id
    private int type;//	是	int	1短信 2页面
    private int status;//	是	int任务状态 1 审核中(平台)  2审核未通过(平台) 3准备中(运营商) 4审核未通过(运营商)5准备中 6待投放 7投放中 8投放完 9暂停任务 10恢复任务 11终止任务
    private String rangeRadius;//	是	String	半径范围
    private BigDecimal budget;//	是	bigdecimal	任务预算
    private BigDecimal actualAmount;//	是	bigdecimal	实际支出
    private int invoiceStatus;//	是	int	开发票状态 1未开 2已开
    private int orientationSettingsId;//	是	int	定向设置id
    private Date throwStartdate;//	是	Date	任务开始日期
    private Date throwEnddate;//	是	Date	任务结束日期
    private int estimatePeoplerno;//	是	int	预计投放人数
    private String content;//	是	String	短信内容
    private String provinceCode;//	是	String	省code
    private String cityCode;//	是	String	城市code
    private String throwAddress;//	是	String	投放地址
    private Date createDate;//	是	Date	创建日期
    private int delFlag;//	是	int	删除状态 0正常 1删除
    private BigDecimal longitude;//	是	bigdecimal	经度
    private BigDecimal dimension;//	是	bigdecimal	纬度
    private String adImgid;//	是	String	广告图片id
    private String adLink;//	是	String	广告链接
    private String templateUrl;//  	是	String	落地页图片路径
    private int templateId;//	是	int	落地页id
    private String plat;//	是	String	ios /Android /web 设备标识
    private String testCmPhone;
    private String testCuPhone;
    private String testCtPhone;

    public BigDecimal getXunMoney() {
        return xunMoney;
    }

    public void setXunMoney(BigDecimal xunMoney) {
        this.xunMoney = xunMoney;
    }

    public int getShowCount() {
        return showCount;
    }

    public void setShowCount(int showCount) {
        this.showCount = showCount;
    }

    public int getIssuedCount() {
        return issuedCount;
    }

    public void setIssuedCount(int issuedCount) {
        this.issuedCount = issuedCount;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public OrientationSettingsOut getOrientationSettingsOut() {
        return orientationSettingsOut;
    }

    public void setOrientationSettingsOut(OrientationSettingsOut orientationSettingsOut) {
        this.orientationSettingsOut = orientationSettingsOut;
    }

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

    public String getPlat() {
        return plat;
    }

    public void setPlat(String plat) {
        this.plat = plat;
    }

    public String getTestCmPhone() {
        return testCmPhone;
    }

    public void setTestCmPhone(String testCmPhone) {
        this.testCmPhone = testCmPhone;
    }

    public String getTestCuPhone() {
        return testCuPhone;
    }

    public void setTestCuPhone(String testCuPhone) {
        this.testCuPhone = testCuPhone;
    }

    public String getTestCtPhone() {
        return testCtPhone;
    }

    public void setTestCtPhone(String testCtPhone) {
        this.testCtPhone = testCtPhone;
    }
}
