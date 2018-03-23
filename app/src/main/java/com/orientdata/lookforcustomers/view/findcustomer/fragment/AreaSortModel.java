package com.orientdata.lookforcustomers.view.findcustomer.fragment;

import com.orientdata.lookforcustomers.bean.Area;

/**
 *城市选择- 按字母显示
 */

public class AreaSortModel {

    private Area area;//显示的数据
    private String sortLetters;//显示数据拼音的首字母
    //private int staus;//是否开通

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
