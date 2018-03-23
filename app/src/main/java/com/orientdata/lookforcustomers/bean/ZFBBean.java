package com.orientdata.lookforcustomers.bean;

/**
 * Created by wy
 * on 2017-12-03.
 */

public class ZFBBean {

    private String seller;//收款时的支付宝账号
    private String partner;//  合作身份者ID
    private String productName;// 商品标题
    private String productDescription;//  商品描述
    private String amount;// 金额
    private String notifyURL;// 回调URL
    private String signedString;// 签名

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNotifyURL() {
        return notifyURL;
    }

    public void setNotifyURL(String notifyURL) {
        this.notifyURL = notifyURL;
    }

    public String getSignedString() {
        return signedString;
    }

    public void setSignedString(String signedString) {
        this.signedString = signedString;
    }
}
