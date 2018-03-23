package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wy on 2017/11/19.
 */

public class TradeCategoryOut implements Serializable {
    private String name;
    private int tradeId;//二级行业id
    private int parentId;//父id
    private List<MustQualify> mu;//二级行业对应的资质列表
    private List<BusinessScopeOut> yib;//二级行业对应的业务范围列表

    public int getTradeId() {
        return tradeId;
    }

    public void setTradeId(int tradeId) {
        this.tradeId = tradeId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public List<MustQualify> getMu() {
        return mu;
    }

    public void setMu(List<MustQualify> mu) {
        this.mu = mu;
    }

    public List<BusinessScopeOut> getYib() {
        return yib;
    }

    public void setYib(List<BusinessScopeOut> yib) {
        this.yib = yib;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
