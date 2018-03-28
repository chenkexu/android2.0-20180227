package com.orientdata.lookforcustomers.model.imple;

import com.orientdata.lookforcustomers.bean.ProvinceBean;
import com.orientdata.lookforcustomers.bean.ProvinceCityBean;
import com.orientdata.lookforcustomers.model.IProvinceCityModel;
import com.orientdata.lookforcustomers.network.HttpConstant;
import vr.md.com.mdlibrary.okhttp.OkHttpClientManager;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * Created by wy on 2017/11/18.
 * 省市
 */

public class ProvinceCityModelImple implements IProvinceCityModel {

    @Override
    public void getProvinceCity(final Complete complete) {
        String url = HttpConstant.SELECT_CITY;
        MDBasicRequestMap map = new MDBasicRequestMap();
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<ProvinceCityBean>() {
            @Override
            public void onError(Exception e) {
                complete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(ProvinceCityBean response) {
                if (response.getCode() == 0) {
                    complete.onSuccess(response.getResult());
                }
            }
        }, map);

    }

    @Override
    public void getProvince(final Complete complete) {
        String url = HttpConstant.SELECT_PROVINCE_CITY;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("type","1");//type=1获取省
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<ProvinceBean>() {
            @Override
            public void onError(Exception e) {
                complete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(ProvinceBean response) {
                if (response.getCode() == 0) {
                    complete.onProvinceOrCitySuc(response.getResult());
                }
            }
        }, map);

    }

    @Override
    public void getCity(int code, int type, final Complete complete) {
        String url = HttpConstant.SELECT_PROVINCE_CITY;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("type",type+"");//2 市 3 区
        map.put("code",code+"");
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<ProvinceBean>() {
            @Override
            public void onError(Exception e) {
                complete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(ProvinceBean response) {
                if (response.getCode() == 0) {
                    complete.onProvinceOrCitySuc(response.getResult());
                }
            }
        }, map);

    }
}
