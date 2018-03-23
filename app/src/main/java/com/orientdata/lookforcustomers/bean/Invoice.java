package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wy
 * on 2017-12-03.
 */

public class Invoice implements Serializable {
    Date createDate;
    //    是 Date 创建时间
    BigDecimal price;
    //    是 bigdecimal 钱数
    int type;
    //    是 int 1纸质企业普票 2纸质企业专票 3纸质个人普票 4电子企业5电子个人
    int invoiceId;// 是 int 自增id

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }
}
