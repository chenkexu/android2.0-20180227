package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 一级行业
 */
public class IndustryBean extends ErrBean implements Serializable {

    public List<Industry> result;


    public List<Industry> getResult() {
        return result;
    }

    public void setResult(List<Industry> result) {
        this.result = result;
    }
}
