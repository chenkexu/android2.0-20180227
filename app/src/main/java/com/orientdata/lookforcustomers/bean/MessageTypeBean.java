package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;

/**
 * Created by 陈柯旭 on 2018/4/7.
 */

public class MessageTypeBean implements Serializable{



    private int id;
    private int provincecode;
    private int signstate;
    private String tdcontent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvincecode() {
        return provincecode;
    }

    public void setProvincecode(int provincecode) {
        this.provincecode = provincecode;
    }

    public int getSignstate() {
        return signstate;
    }

    public void setSignstate(int signstate) {
        this.signstate = signstate;
    }

    public String getTdcontent() {
        return tdcontent;
    }

    public void setTdcontent(String tdcontent) {
        this.tdcontent = tdcontent;
    }
}
