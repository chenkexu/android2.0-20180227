package com.orientdata.lookforcustomers.model.imple;

import com.orientdata.lookforcustomers.bean.SelectSettingBean;
import com.orientdata.lookforcustomers.model.ISelectSettingModel;
import com.orientdata.lookforcustomers.network.HttpConstant;
import vr.md.com.mdlibrary.okhttp.OkHttpClientManager;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * Created by wy on 2017/11/18.
 * 定向设置
 */

public class SelectSettingModelImple implements ISelectSettingModel {

    @Override
    public void getSelectSetting(final Complete complete,String code) {
        String url = HttpConstant.SELECT_SETTING2;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("code",code);
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<SelectSettingBean>() {
            @Override
            public void onError(Exception e) {
                complete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(SelectSettingBean response) {
                if (response.getCode() == 0) {
                    complete.onSuccess(response.getResult());
                }
            }
        }, map);
    }

}
