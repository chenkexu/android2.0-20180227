package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wy on 2017/11/19.
 */

public class InvoiceAreaOut extends ErrBean implements Serializable {
    private List<Area> result;

    public List<Area> getResult() {
        return result;
    }

    public void setResult(List<Area> result) {
        this.result = result;
    }
}
