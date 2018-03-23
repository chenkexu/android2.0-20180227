package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wy on 2017/11/19.
 * 认证
 */

public class CertificationBean implements Serializable{
//    userId	是	int	用户id
//    name
//            是
//    string	名称/姓名
//            type
//    是
//    int	1企业  2 个人
//    businessLicenseNo	否	String	营业执照编号
//    contactPerson	否	String 	联系人
//    contactPhone 	是	String 	联系电话
//    contactCard 	是	String 	联系人身份证号码
//    cityCode 	是	String 	城市code
//    provinceCode	是	String  	省code
//    address	是	String	通讯地址
//    tradeOneId 	是	int	 一级行业id
//    tradeTwoId	是	int	二级行业id
//    file	否
    private int userId;//用户id
    private String name;//名称/姓名
    private int type;//1企业  2 个人
    private String businessLicenseNo;//营业执照编号
    private String contactPerson;//联系人
    private String contactPhone;//联系人电话
    private String contactCard;//联系人身份证号码
    private String cityCode;//城市code
    private String provinceCode;//省code
    private String address;//通讯地址
    private int tradeOneId;//一级行业id
    private int tradeTwoId;//二级行业id
    private List<String> file;//二级行业id
    private int cityPosition;
    private int provincePosition;
    private String city;
    private String province;

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

    public List<String> getFile() {
        return file;
    }

    public void setFile(List<String> file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCityPosition() {
        return cityPosition;
    }

    public void setCityPosition(int cityPosition) {
        this.cityPosition = cityPosition;
    }

    public int getProvincePosition() {
        return provincePosition;
    }

    public void setProvincePosition(int provincePosition) {
        this.provincePosition = provincePosition;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public String toString() {
        return "CertificationBean{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", businessLicenseNo='" + businessLicenseNo + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", contactCard='" + contactCard + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", address='" + address + '\'' +
                ", tradeOneId=" + tradeOneId +
                ", tradeTwoId=" + tradeTwoId +
                ", file=" + file +
                '}';
    }
}
