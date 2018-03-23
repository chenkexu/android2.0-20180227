package com.orientdata.lookforcustomers.model;

import com.orientdata.lookforcustomers.bean.CertificationOut;
import com.orientdata.lookforcustomers.bean.SearchListBean;

/**
 * Created by wy on 2017/11/18.
 * 认证
 */

public interface ICertificateModel {
    //获取认证信息
    void getCertificateMsg(ICertificateModel.GetMsgComplete complete);
    //获取寻客列表
    void getSearchList(GetSearchListComplete complete,int type,int status,int page,int size);
    interface GetMsgComplete {
        void onSuccess(CertificationOut certificationOut);
        void onError(int code, String message);
    }
    interface GetSearchListComplete {
        void onSuccess(SearchListBean searchListBean);
        void onError(int code, String message);
    }
}
