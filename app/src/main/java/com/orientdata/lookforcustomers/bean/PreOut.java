package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by wy
 * on 2017-12-03.
 */

public class PreOut implements Serializable {
    private int day;
    private BigDecimal longitude;
    private BigDecimal latitude;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }
}
