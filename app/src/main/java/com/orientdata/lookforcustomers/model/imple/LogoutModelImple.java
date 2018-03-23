package com.orientdata.lookforcustomers.model.imple;

import com.orientdata.lookforcustomers.bean.ErrBean;
import com.orientdata.lookforcustomers.bean.LoginResultBean;
import com.orientdata.lookforcustomers.model.ILogOutModel;
import com.orientdata.lookforcustomers.network.HttpConstant;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.OkHttpClientManager;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * Created by wy on 2017/12/20.
 * 退出
 */

public class LogoutModelImple implements ILogOutModel {
    @Override
    public void logOut(final Complete complete) {
        String url = HttpConstant.LOGOUT_ACCOUNT;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("token", UserDataManeger.getInstance().getUserToken());
        map.put("userId", UserDataManeger.getInstance().getUserId());
        map.put("deviceToken", "");
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<ErrBean>() {
            @Override
            public void onError(Exception e) {
                complete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(ErrBean response) {
                complete.onSuccess(response);
            }
        }, map);
    }
}
