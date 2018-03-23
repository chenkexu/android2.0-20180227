package com.orientdata.lookforcustomers.bean;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by wy
 * on 2017-12-03.
 * 订单
 */

public class Order {
    private int orderId; //是 int 自增id
    private String orderNo;//是 String 订单编号
    private int userId;//是 int 用户id
    private int payType;// 是 int 1 微信 2支付宝
    private int paymentId;//是 int 支付id
    private BigDecimal payMoney;//是 bigdecimal 支付钱数
    private String demo;//是 String 备注
    private int status;//是 int 1 待付款 2 成功 3 失败 4退款

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
