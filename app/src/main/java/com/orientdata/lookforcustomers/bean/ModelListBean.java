package com.orientdata.lookforcustomers.bean;

import java.util.List;

/**
 * Created by wy on 2017/12/2.
 */

public class ModelListBean extends ErrBean{
    private List<TradeSelfout> result;

    public List<TradeSelfout> getResult() {
        return result;
    }

    public void setResult(List<TradeSelfout> result) {
        this.result = result;
    }
}
