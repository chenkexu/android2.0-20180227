package com.orientdata.lookforcustomers.bean;

import java.util.List;

/**
 * Created by wy
 * on 2017-12-03.
 *
 */

public class AdPagesBean extends ErrBean {
    List<AdPage> result;

    public List<AdPage> getResult() {
        return result;
    }

    public void setResult(List<AdPage> result) {
        this.result = result;
    }
}
