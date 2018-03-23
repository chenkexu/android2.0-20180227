package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;

/**
 * Created by wy on 2017/11/20.
 * 二级行业
 */

public class BusinessScope implements Serializable{
    private int businessId;//业务范围id
    private int tradeSonId;//二级行业id(一级业务范围才有)
    private String scopeName;//业务范围名称
    private int parentId;//业务范围父id

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public int getTradeSonId() {
        return tradeSonId;
    }

    public void setTradeSonId(int tradeSonId) {
        this.tradeSonId = tradeSonId;
    }

    public String getScopeName() {
        return scopeName;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
