package com.orientdata.lookforcustomers.bean;

import java.math.BigDecimal;

/**
 * Created by wy
 * on 2017-12-03.
 */

public class WXBean {

    private String appid;//调用接口提交的公众账号ID
    private String nonceStr;//随机字符串
    private String _package;// 返回Sign=WXPay
    private String partnerId;//调用接口提交的商户号
    private String prepayId;// 预支付交易会话标识
    private String sign;//签名
    private String timeStamp;// 时间戳

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String get_package() {
        return _package;
    }

    public void set_package(String _package) {
        this._package = _package;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
