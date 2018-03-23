package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by wy on 2017/11/20.
 * 认证
 */

public class CertificationOut implements Serializable{
    private int  certificationId;//	是	int	认证id
    private Date createDate;//是	Date	创建时间
    private String name;	//是	String	名称
    private int type;//	是	int	1-企业 2-个人
    private String businessLicenseNo;//	是	String	营业执照编号
    private String contactPerson;//	是	String	联系人
    private String contactPhone;//	是	String	联系人手机号
    private String contactCard;//	是	String	联系人身份证号
    private String cityCode;//	是	String	市code
    private String cityName;//	是	String	市名称
    private String provinceName;//	是	String	省名称
    private String provinceCode;//	是	String	省code
    private String tradeOneName;//	一级名
    private String tradeTwoName;//	二级名
    private String address;//	是	String	地址
    private int tradeOneId;//	是	int	一级id
    private int tradeTwoId;//	是	int	二级id
    private int authStatus;//	是	int	认证状态 1审核中 2审核通过 3审核拒绝 4审核中
    private List<QualificationCertificationUser> ulist;//	是	list<QualificationCertificationUser>	认证图片

    public int getCertificationId() {
        return certificationId;
    }

    public void setCertificationId(int certificationId) {
        this.certificationId = certificationId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBusinessLicenseNo() {
        return businessLicenseNo;
    }

    public void setBusinessLicenseNo(String businessLicenseNo) {
        this.businessLicenseNo = businessLicenseNo;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactCard() {
        return contactCard;
    }

    public void setContactCard(String contactCard) {
        this.contactCard = contactCard;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTradeOneId() {
        return tradeOneId;
    }

    public void setTradeOneId(int tradeOneId) {
        this.tradeOneId = tradeOneId;
    }

    public int getTradeTwoId() {
        return tradeTwoId;
    }

    public void setTradeTwoId(int tradeTwoId) {
        this.tradeTwoId = tradeTwoId;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    public List<QualificationCertificationUser> getUlist() {
        return ulist;
    }

    public void setUlist(List<QualificationCertificationUser> ulist) {
        this.ulist = ulist;
    }

    public String getTradeOneName() {
        return tradeOneName;
    }

    public void setTradeOneName(String tradeOneName) {
        this.tradeOneName = tradeOneName;
    }

    public String getTradeTwoName() {
        return tradeTwoName;
    }

    public void setTradeTwoName(String tradeTwoName) {
        this.tradeTwoName = tradeTwoName;
    }


}
