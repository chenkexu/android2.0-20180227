package com.orientdata.lookforcustomers.bean;

/**
 * Created by wy
 * on 2017-12-03.
 */

public class SelectWordBean extends ErrBean {


    private ErrBean err;
    private int result;

    public ErrBean getErr() {
        return err;
    }

    public void setErr(ErrBean err) {
        this.err = err;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
