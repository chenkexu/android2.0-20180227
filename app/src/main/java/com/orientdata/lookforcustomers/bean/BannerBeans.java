package com.orientdata.lookforcustomers.bean;

import java.util.List;

/**
 * Created by wy
 * on 2017-12-03.
 */

public class BannerBeans extends ErrBean {
    List<BannerBean> result;

    public List<BannerBean> getResult() {
        return result;
    }

    public void setResult(List<BannerBean> result) {
        this.result = result;
    }
}
