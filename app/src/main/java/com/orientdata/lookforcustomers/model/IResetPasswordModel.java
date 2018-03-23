package com.orientdata.lookforcustomers.model;

/**
 * Created by wy on 2017/10/27.
 */

public interface IResetPasswordModel {
    void resetPassword(String phone, String code
            , String codeId, String password
            , ResetPasswordComplete resetPasswordComplete);//重置密码

    interface ResetPasswordComplete {
        void onSuccess();

        void onError(int code, String message);
    }
}
