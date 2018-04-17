package com.orientdata.lookforcustomers.model.imple;

import com.orientdata.lookforcustomers.bean.LoginResultBean;
import com.orientdata.lookforcustomers.model.IRegisterModel;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * Created by wy on 2017/10/25.
 * 注册
 */

public class RegisterModel implements IRegisterModel {
    @Override
    public void register(String account, String password
            , String code, String codeId, String model
            , String phoneType, String deviceToken
            , final RegisterComplete registerComplete) {
        String url = HttpConstant.REGISTER_ACCOUNT;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("phone", account);
        map.put("password", password);
        map.put("code", code);
        map.put("codeId", codeId);
        map.put("phoneType", phoneType);
        map.put("model", model);
        map.put("deviceToken", deviceToken);
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<LoginResultBean>() {
            @Override
            public void onError(Exception e) {
                registerComplete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(LoginResultBean response) {
                if (response.getErr().getCode() == 0) {
                    registerComplete.success();
                }
            }
        }, map);
    }
}
