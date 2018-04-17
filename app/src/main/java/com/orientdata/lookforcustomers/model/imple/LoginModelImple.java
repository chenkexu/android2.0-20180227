package com.orientdata.lookforcustomers.model.imple;

import com.orientdata.lookforcustomers.bean.LoginResultBean;
import com.orientdata.lookforcustomers.model.ILoginModel;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * Created by wy on 2017/10/25.
 * 登录
 */

public class LoginModelImple implements ILoginModel {
    @Override
    public void accountLogin(String account, String password
            , String phoneType, String model
            , String deviceToken, final LoginComplete loginComplete) {
        String url = HttpConstant.LOGIN_ACCOUNT;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("phone", account);
        map.put("password", password);
        map.put("phoneType", phoneType);
        map.put("model", model);
        map.put("deviceToken", deviceToken);
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<LoginResultBean>() {
            @Override
            public void onError(Exception e) {
                loginComplete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(LoginResultBean response) {
                if (response.getErr().getCode() == 0) {
                    UserDataManeger.getInstance().setUserId(response.getUser().getUserId());
                    UserDataManeger.getInstance().saveUserToken(response.getToken());
                    loginComplete.onSuccess(response.isNewUser());
                }
            }
        }, map);
    }

    @Override
    public void authCodeLogin(String account, String code
            , String codeId, String phoneType,
                              String model, String deviceToken, final LoginComplete loginComplete) {
        String url = HttpConstant.CODE_LOGIN_ACCOUNT;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("phoneNo", account);
        map.put("code", code);
        map.put("codeId", codeId);
        map.put("phoneType", phoneType);
        map.put("model", model);
        map.put("deviceToken", deviceToken);
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<LoginResultBean>() {
            @Override
            public void onError(Exception e) {
                loginComplete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(LoginResultBean response) {
                if (response.getErr().getCode() == 0) {
                    UserDataManeger.getInstance().setUserId(response.getUser().getUserId());
                    UserDataManeger.getInstance().saveUserToken(response.getToken());
                    loginComplete.onSuccess(response.isNewUser());
                }
            }
        }, map);
    }
}
