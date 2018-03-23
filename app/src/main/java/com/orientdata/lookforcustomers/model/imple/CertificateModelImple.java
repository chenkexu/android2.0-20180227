package com.orientdata.lookforcustomers.model.imple;

import com.orientdata.lookforcustomers.bean.CertificateMsgBean;
import com.orientdata.lookforcustomers.bean.ProvinceCityBean;
import com.orientdata.lookforcustomers.bean.SearchListBean;
import com.orientdata.lookforcustomers.model.ICertificateModel;
import com.orientdata.lookforcustomers.model.IProvinceCityModel;
import com.orientdata.lookforcustomers.network.HttpConstant;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.OkHttpClientManager;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * Created by wy on 2017/11/18.
 * 认证
 */
public class CertificateModelImple implements ICertificateModel {
    @Override
    public void getCertificateMsg(final GetMsgComplete complete) {
        String url = HttpConstant.QUERY_CERTIFICATION_MSG;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());
        map.put("token", UserDataManeger.getInstance().getUserToken());
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<CertificateMsgBean>() {
            @Override
            public void onError(Exception e) {
                complete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(CertificateMsgBean response) {
                if(response == null){
                    //未认证
                    complete.onSuccess(null);
                }else if(response.getCode() == 0){
                    complete.onSuccess(response.getResult());
                }

            }
        }, map);
    }

    @Override
    public void getSearchList(final GetSearchListComplete complete,int type,int status,int page,int size) {
        String url = HttpConstant.USER_TASK_LIST;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());
        if(type != -1 && type != 0){
            map.put("type", type+"");
        }
        if(status != -1 && status != 0){
            map.put("status", status+"");
        }
        if(page!=-1) {
            map.put("page", page+"");
            map.put("size", size+"");
        }
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<SearchListBean>() {
            @Override
            public void onError(Exception e) {
                complete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(SearchListBean response) {
                if(response == null){
                    //未认证
                    complete.onSuccess(null);
                }else if(response.getCode() == 0){
                    complete.onSuccess(response);
                }
            }
        }, map);
    }
}
