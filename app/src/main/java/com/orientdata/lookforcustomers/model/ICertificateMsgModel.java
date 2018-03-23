package com.orientdata.lookforcustomers.model;

import com.orientdata.lookforcustomers.bean.AreaOut;
import com.orientdata.lookforcustomers.bean.CertificationOut;

import java.util.List;

/**
 * Created by wy on 2017/11/18.
 * 认证--我的认证信息
 */

public interface ICertificateMsgModel {
    void getCertificateMsg(int userId,String token,Complete complete);
    interface Complete {
        void onSuccess(CertificationOut certificationOut);
        void onError(int code, String message);
    }
}
