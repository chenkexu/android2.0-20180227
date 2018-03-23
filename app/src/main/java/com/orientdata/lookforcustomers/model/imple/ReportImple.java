package com.orientdata.lookforcustomers.model.imple;

import com.orientdata.lookforcustomers.bean.ReportListBean;
import com.orientdata.lookforcustomers.bean.ReportUrlBean;
import com.orientdata.lookforcustomers.model.IReportModel;
import com.orientdata.lookforcustomers.network.HttpConstant;
import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.OkHttpClientManager;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * Created by wy on 2017/11/18.
 * 报表
 */
public class ReportImple implements IReportModel {

    @Override
    public void getReportData(final DataComplete complete, int type, String today) {
        String url = HttpConstant.REPORT_DATA;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());
        map.put("type", type+"");
        if(type == 6){
            map.put("today", today);
        }
        map.put("excType",1+"");
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<ReportListBean>() {
            @Override
            public void onError(Exception e) {
//                complete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
                complete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(ReportListBean response) {
                complete.onSuccess(response);
            }
        }, map);
    }

    @Override
    public void getReportUrl(final UrlComplete complete, int type, String today) {
        String url = HttpConstant.REPORT_DATA;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());
        map.put("type", type+"");
        if(type == 6){
            map.put("today", today);
        }
        map.put("excType",2+"");
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<ReportUrlBean>() {
            @Override
            public void onError(Exception e) {
                complete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(ReportUrlBean response) {
                complete.onSuccess(response);
            }
        }, map);
    }
}
