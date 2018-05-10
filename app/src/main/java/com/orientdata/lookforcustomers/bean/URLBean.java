package com.orientdata.lookforcustomers.bean;

/**
 * Created by wy
 * on 2017-12-03.
 */

public class URLBean extends ErrBean {
    private String result;

    public String getResult() {
        return result == null ? "" : result;
    }

    public void setResult(String result) {
        this.result = result;
    }


}
