package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wy on 2017/12/19.
 */

public class Bh implements Serializable{
    private int balanceHistoryId;//自增id
    private int userId;//用户id
    private String detailNo;//明细编号
    private int type;//1余额 2佣金 3冻结金额
    private BigDecimal occurMoney;//发生金额
    private BigDecimal afterMoney;
    private String remark;
    private int bigTaskId;
    private int smallTaskId;
    private int ofId;
    private int delFlag;
    private String zhifu;
    private int orderNo;
    //101,102,103,104,201
    // 余额列表 备注 :101,102,103,104 是加钱操作 201 是 减钱操作 101支付宝充值102微信充值 103线下充值 104佣金转余额 201余额支出
    private int transactionType;//105,203,204 备注 :105 是加钱操作 203 204 是 减钱操作（佣金列表）
    private Date createDate;//创建时间

    public int getBalanceHistoryId() {
        return balanceHistoryId;
    }

    public void setBalanceHistoryId(int balanceHistoryId) {
        this.balanceHistoryId = balanceHistoryId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDetailNo() {
        return detailNo;
    }

    public void setDetailNo(String detailNo) {
        this.detailNo = detailNo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BigDecimal getOccurMoney() {
        return occurMoney;
    }

    public void setOccurMoney(BigDecimal occurMoney) {
        this.occurMoney = occurMoney;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public BigDecimal getAfterMoney() {
        return afterMoney;
    }

    public void setAfterMoney(BigDecimal afterMoney) {
        this.afterMoney = afterMoney;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getBigTaskId() {
        return bigTaskId;
    }

    public void setBigTaskId(int bigTaskId) {
        this.bigTaskId = bigTaskId;
    }

    public int getSmallTaskId() {
        return smallTaskId;
    }

    public void setSmallTaskId(int smallTaskId) {
        this.smallTaskId = smallTaskId;
    }

    public int getOfId() {
        return ofId;
    }

    public void setOfId(int ofId) {
        this.ofId = ofId;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public String getZhifu() {
        return zhifu;
    }

    public void setZhifu(String zhifu) {
        this.zhifu = zhifu;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }
}
