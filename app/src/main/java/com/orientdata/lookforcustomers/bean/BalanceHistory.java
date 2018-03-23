package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wy on 2017/12/19.
 * 交易记录
 */

public class BalanceHistory implements Serializable {
    private BigDecimal occurMoney;//金额
    private int balanceHistoryId;//自增id
    private Date createDate;//交易时间
    private String detailNo;//订单号
    private int transactionType;//int	交易渠道 101支付宝充值 102微信充值 103线下充值 104佣金转余额 201余额支出
    private int userId;
    private int type;
    private BigDecimal afterMoney;
    private String remark;
    private int bigTaskId;
    private int smallTaskId;
    private int ofId;
    private int delFlag;
    private String zhifu;
    private String orderNo;

    public BigDecimal getOccurMoney() {
        return occurMoney;
    }

    public void setOccurMoney(BigDecimal occurMoney) {
        this.occurMoney = occurMoney;
    }

    public int getBalanceHistoryId() {
        return balanceHistoryId;
    }

    public void setBalanceHistoryId(int balanceHistoryId) {
        this.balanceHistoryId = balanceHistoryId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getDetailNo() {
        return detailNo;
    }

    public void setDetailNo(String detailNo) {
        this.detailNo = detailNo;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
