package com.orientdata.lookforcustomers.model;

import com.orientdata.lookforcustomers.bean.Area;
import com.orientdata.lookforcustomers.bean.AreaOut;

import java.util.List;

/**
 * Created by wy on 2017/11/18.
 * 认证--获取省市
 */

public interface IProvinceCityModel {
    void getProvinceCity(Complete complete);//获取省市

    void getProvince(Complete complete);//获取省市

    void getCity(int code, int type, Complete complete);//获取省市

    interface Complete {
        void onSuccess(List<AreaOut> areaOuts);

        void onProvinceOrCitySuc(List<Area> areas);

        void onError(int code, String message);
    }
    
}
