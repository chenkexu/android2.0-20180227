package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wy on 2017/11/20.
 * 线下充值
 */

public class OfflineRechargeBean implements Serializable {

    private int offlineRechargeId;
    //是 int 自增id
    private String openAccountBank;
    //是 String 开户行
    private String serialNumber;
    //是 String 流水号
    private String accountName;
    //是 String 账户名
    private String accountNumber;
    // 是 String 账号
    private Double money;
    //是 bigdecimal 钱数
    private String voucherImgid;
    //是 String 图片路径
    private Date createDate;
    //是 Date 创建时间
    private int status;
    //是 int 状态 1待处理 2未到账 3已充值 4 已驳回
    private String reason;
    //是 String 拒绝理由


    public int getOfflineRechargeId() {
        return offlineRechargeId;
    }

    public void setOfflineRechargeId(int offlineRechargeId) {
        this.offlineRechargeId = offlineRechargeId;
    }

    public String getOpenAccountBank() {
        return openAccountBank;
    }

    public void setOpenAccountBank(String openAccountBank) {
        this.openAccountBank = openAccountBank;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getVoucherImgid() {
        return voucherImgid;
    }

    public void setVoucherImgid(String voucherImgid) {
        this.voucherImgid = voucherImgid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
