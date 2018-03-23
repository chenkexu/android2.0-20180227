package com.orientdata.lookforcustomers.model;

/**
 * Created by wy on 2017/10/24.
 */

public interface ISendSmsModel {

    void sendSms(String phoneNo, String codeId, SendSmsComplete sendSmsComplete);//发送短信

    interface SendSmsComplete {
        void success(String codeId);

        void onError(int code, String message);
    }
}
