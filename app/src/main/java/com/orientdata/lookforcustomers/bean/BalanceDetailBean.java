package com.orientdata.lookforcustomers.bean;

/**
 * Created by wy on 2017/12/19.
 * 余额详情
 */

public class BalanceDetailBean extends ErrBean{
    private BalanceHistory result;

    public BalanceHistory getResult() {
        return result;
    }

    public void setResult(BalanceHistory result) {
        this.result = result;
    }
}
