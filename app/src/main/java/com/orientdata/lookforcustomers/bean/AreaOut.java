package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wy on 2017/11/19.
 * 区域列表
 */

public class AreaOut extends Area implements Serializable {
    private List<Area> list;

    public List<Area> getList() {
        return list;
    }

    public void setList(List<Area> list) {
        this.list = list;
    }
}
