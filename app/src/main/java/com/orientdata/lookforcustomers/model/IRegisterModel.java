package com.orientdata.lookforcustomers.model;

/**
 * Created by wy on 2017/10/24.
 */

public interface IRegisterModel {

    void register(String account, String password, String code
            , String codeId, String model, String phoneType, String deviceToken
            , RegisterComplete registerComplete);//注册

    interface RegisterComplete {
        void success();

        void onError(int code, String message);
    }
}
