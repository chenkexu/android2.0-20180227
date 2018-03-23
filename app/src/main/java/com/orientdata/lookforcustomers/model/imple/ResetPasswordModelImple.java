package com.orientdata.lookforcustomers.model.imple;

import android.util.Log;

import com.orientdata.lookforcustomers.bean.CommonResultBean;
import com.orientdata.lookforcustomers.bean.LoginResultBean;
import com.orientdata.lookforcustomers.model.IResetPasswordModel;
import com.orientdata.lookforcustomers.network.HttpConstant;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.OkHttpClientManager;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * Created by wy on 2017/10/27.
 * 重置密码
 */

public class ResetPasswordModelImple implements IResetPasswordModel {
    @Override
    public void resetPassword(String phone, String code, String codeId, String password, final ResetPasswordComplete resetPasswordComplete) {
        String url = HttpConstant.RESET_PASSWORD;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("phone", phone);
        map.put("password", password);
        map.put("code", code);
        map.put("codeId", codeId);
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<CommonResultBean>() {
            @Override
            public void onError(Exception e) {
                resetPasswordComplete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(CommonResultBean response) {
                if (response.getErr().getCode() == 0) {
                    resetPasswordComplete.onSuccess();
                }
            }
        }, map);
    }
}
