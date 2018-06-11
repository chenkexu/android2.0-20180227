package com.orientdata.lookforcustomers.bean;

/**
 * Created by ckx on 2018/6/11.
 */

public class HomeBean {
    private String name;
    private int pic;
    private int state;

    public HomeBean(String name, int pic) {
        this.name = name;
        this.pic = pic;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
