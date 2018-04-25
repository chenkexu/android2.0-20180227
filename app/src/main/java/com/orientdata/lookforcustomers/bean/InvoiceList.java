package com.orientdata.lookforcustomers.bean;

import java.util.List;

/**
 * Created by wy
 * on 2017-12-03.
 */

public class InvoiceList extends ErrBean {
    List<Invoice> result;

    public List<Invoice> getResult() {
        return result;
    }

    public void setResult(List<Invoice> result) {
        this.result = result;
    }
}
