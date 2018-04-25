package com.orientdata.lookforcustomers.model.imple;

import com.orientdata.lookforcustomers.bean.SendSmsResultBean;
import com.orientdata.lookforcustomers.model.ISendSmsModel;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;

import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * Created by wy on 2017/10/25.
 * 发短信
 */

public class SendSmsModelImple implements ISendSmsModel {
    @Override
    public void sendSms(String phoneNo, String codeId
            , final SendSmsComplete sendSmsComplete) {
        String url = HttpConstant.SEND_SMS;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("phoneNo", phoneNo);
        map.put("codeId", codeId);
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<SendSmsResultBean>() {
            @Override
            public void onError(Exception e) {
                if (e != null) {
                    sendSmsComplete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
                }
            }

            @Override
            public void onResponse(SendSmsResultBean response) {
                if (response.getErr().getCode() == 0) {
                    sendSmsComplete.success(response.getCodeId() + "");
                }
            }
        }, map);
    }
}
