package com.orientdata.lookforcustomers.bean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by wy
 * on 2017-12-03.
 */

public class InvoiceDetail extends ErrBean {
    private InvoiceOut result;

    public InvoiceOut getResult() {
        return result;
    }

    public void setResult(InvoiceOut result) {
        this.result = result;
    }
}
