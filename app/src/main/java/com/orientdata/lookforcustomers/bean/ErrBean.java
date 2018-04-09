package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;

/**
 * Created by wy on 2017/11/21.
 */

public class ErrBean implements Serializable{
    /**
     * code : 0
     * msg : 正常
     * eventId : xxxx-xxxx-xxxx
     */

    private int code;
    private String msg;
    private String eventid;

    public String getEventid() {
        return eventid;

    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

//    public String getEventId() {
//        return eventId;
//    }
//
//    public void setEventId(String eventId) {
//        this.eventId = eventId;
//    }
}
