package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by wy on 2017/12/10.
 */

public class Report implements Serializable {
    private BigDecimal money;//总消费
    private BigDecimal duanxinmoney;//短信消费
    private int succCount;//短信下发量
    private BigDecimal pagemoney;//页面消费
    private int showCount;//页面展现量
    private int clickCount;//页面点击量
    private String name;//名称

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getDuanxinmoney() {
        return duanxinmoney;
    }

    public void setDuanxinmoney(BigDecimal duanxinmoney) {
        this.duanxinmoney = duanxinmoney;
    }

    public int getSuccCount() {
        return succCount;
    }

    public void setSuccCount(int succCount) {
        this.succCount = succCount;
    }

    public BigDecimal getPagemoney() {
        return pagemoney;
    }

    public void setPagemoney(BigDecimal pagemoney) {
        this.pagemoney = pagemoney;
    }

    public int getShowCount() {
        return showCount;
    }

    public void setShowCount(int showCount) {
        this.showCount = showCount;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
