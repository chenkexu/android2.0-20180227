package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;

/**
 * Created by ckx on 2018/6/22.
 */

public class AgePushBean implements Serializable {
    private String ageRatio;
    private String ageStr;
    private int progressAge;


    public AgePushBean(double ageRatio, String ageStr, int progressAge) {
        this.ageRatio = ageRatio + "%";
        this.ageStr = ageStr;
        this.progressAge = progressAge;
    }

    public String getAgeRatio() {
        return ageRatio;
    }

    public void setAgeRatio(double ageRatio) {
        this.ageRatio = ageRatio + "%";
    }


    public String getAgeStr() {
        return ageStr == null ? "" : ageStr;
    }

    public void setAgeStr(String ageStr) {
        this.ageStr = ageStr;
    }

    public int getProgressAge() {
        return progressAge;
    }

    public void setProgressAge(int progressAge) {
        this.progressAge = progressAge;
    }
}
