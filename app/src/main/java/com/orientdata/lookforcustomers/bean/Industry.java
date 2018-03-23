package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;

/**
 * Created by wy on 2017/11/19.
 * 商业兴趣
 */

public class Industry implements Serializable {
//    {
//            "parentId": 0,
//            "name": "电商平台",
//            "tradeId": 1
//    },
//    {
//            "parentId": 0,
//            "name": "寡妇规划局",
//            "tradeId": 4
//    }
    private int parentId;
    private String name;
    private int type;
    private int tradeId;

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTradeId() {
        return tradeId;
    }

    public void setTradeId(int tradeId) {
        this.tradeId = tradeId;
    }
}
