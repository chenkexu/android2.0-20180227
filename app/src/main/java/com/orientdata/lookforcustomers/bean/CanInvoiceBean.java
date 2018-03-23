package com.orientdata.lookforcustomers.bean;

import java.util.List;

/**
 * Created by wy
 * on 2017-12-03.
 */

public class CanInvoiceBean extends ErrBean {
    List<InvoiceBean> result;

    public List<InvoiceBean> getResult() {
        return result;
    }

    public void setResult(List<InvoiceBean> result) {
        this.result = result;
    }
}
