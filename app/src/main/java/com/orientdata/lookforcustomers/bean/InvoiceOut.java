package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by wy
 * on 2017-12-03.
 */

public class InvoiceOut implements Serializable {

    int userType;
    int type;
    int status;
    int invoiceId;
    int delFlag;
    String invoiceCode;
    //是 int 发票id
    String email;
    //否 String 电子邮件
    String addresser;
    //否 String 收件人
    String phone;
    //否 String 手机号
    String address;
    //否 String 地址
    String lookUp;
    //是 String 发票抬头
    String taxpayerIdentificationNo;
    //是 String 纳税人识别号
    String content;
    //是 String 发票内容
    BigDecimal price;
    //是 bigdecimal 发票金额
    Date createDate;
    //是 Date 创建时间
    List<InvoiceBean> list;// 是 list<Task> 任务列表

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddresser() {
        return addresser;
    }

    public void setAddresser(String addresser) {
        this.addresser = addresser;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLookUp() {
        return lookUp;
    }

    public void setLookUp(String lookUp) {
        this.lookUp = lookUp;
    }

    public String getTaxpayerIdentificationNo() {
        return taxpayerIdentificationNo;
    }

    public void setTaxpayerIdentificationNo(String taxpayerIdentificationNo) {
        this.taxpayerIdentificationNo = taxpayerIdentificationNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public List<InvoiceBean> getList() {
        return list;
    }

    public void setList(List<InvoiceBean> list) {
        this.list = list;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
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

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }
}
