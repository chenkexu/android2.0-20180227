package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wy on 2017/12/3.
 */

public class TradeSelfout implements Serializable{
    private int tradeSelfId;//自增id
    private String name;//行业名称
    private List<IndustryTemplate> itl;//行业图片

    public int getTradeSelfId() {
        return tradeSelfId;
    }

    public void setTradeSelfId(int tradeSelfId) {
        this.tradeSelfId = tradeSelfId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IndustryTemplate> getItl() {
        return itl;
    }

    public void setItl(List<IndustryTemplate> itl) {
        this.itl = itl;
    }
}
