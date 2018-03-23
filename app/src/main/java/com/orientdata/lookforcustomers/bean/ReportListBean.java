package com.orientdata.lookforcustomers.bean;

import java.util.List;

/**
 * Created by wy on 2017/12/10.
 */

public class ReportListBean extends ErrBean{

    private List<Report> result;

    public List<Report> getResult() {
        return result;
    }

    public void setResult(List<Report> result) {
        this.result = result;
    }
}
