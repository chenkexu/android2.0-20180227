package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wy on 2017/11/20.
 * 业务范围
 */

public class BusinessScopeOut implements Serializable {
    private int businessId;//业务范围id
    private int tradeSonId;//二级行业id(一级业务范围才有)
    private String scopeName;//业务范围名称
    private int parentId;//业务范围父id
    private List<BusinessScope> erb;//二级业务范围列表

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

    public List<BusinessScope> getErb() {
        return erb;
    }

    public void setErb(List<BusinessScope> erb) {
        this.erb = erb;
    }
}
