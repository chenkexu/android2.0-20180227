package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;

/**
 * Created by wy on 2017/11/20.
 */

public class MustQualify implements Serializable {
    private int qualifyId;//资质id
    private int tradeSonId;//二级行业id
    private String qualifyName;//必选资质名称

    public int getQualifyId() {
        return qualifyId;
    }

    public void setQualifyId(int qualifyId) {
        this.qualifyId = qualifyId;
    }

    public int getTradeSonId() {
        return tradeSonId;
    }

    public void setTradeSonId(int tradeSonId) {
        this.tradeSonId = tradeSonId;
    }

    public String getQualifyName() {
        return qualifyName;
    }

    public void setQualifyName(String qualifyName) {
        this.qualifyName = qualifyName;
    }
}
