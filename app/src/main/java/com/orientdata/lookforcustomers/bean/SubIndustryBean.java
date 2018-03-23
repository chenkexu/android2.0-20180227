package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 二级行业
 */
public class SubIndustryBean extends ErrBean implements Serializable {

    public List<TradeCategoryOut> result;

    public List<TradeCategoryOut> getResult() {
        return result;
    }

    public void setResult(List<TradeCategoryOut> result) {
        this.result = result;
    }
}
