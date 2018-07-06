package com.orientdata.lookforcustomers.model;

import com.orientdata.lookforcustomers.bean.LoginResultBean;

/**
 * Created by wy on 2017/10/24.
 */

public interface ILoginModel {

    void accountLogin(String account, String password
            , String phoneType, String model,
                      String deviceToken, LoginComplete loginComplete);//登录

    void authCodeLogin(String account, String code, String codeId
            , String phoneType, String model,
                       String deviceToken, LoginComplete loginComplete);//验证码登录

    interface LoginComplete {
        void onSuccess(LoginResultBean response);

        void onError(int code, String message);
    }
}
