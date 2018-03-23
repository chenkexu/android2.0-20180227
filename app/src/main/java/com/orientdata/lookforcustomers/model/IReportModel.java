package com.orientdata.lookforcustomers.model;

import com.orientdata.lookforcustomers.bean.ErrBean;
import com.orientdata.lookforcustomers.bean.MsgListBean;
import com.orientdata.lookforcustomers.bean.ReportListBean;
import com.orientdata.lookforcustomers.bean.ReportUrlBean;

/**
 * Created by wy on 2017/12/18.
 * 消息
 */

public interface IReportModel {
    void getReportData(DataComplete complete, int type, String today);//报表数据
    void getReportUrl(UrlComplete complete, int type, String today);//导出报表链接
    interface DataComplete {
        void onSuccess(ReportListBean reportListBean);
        void onError(int code, String message);
    }
    interface UrlComplete {
        void onSuccess(ReportUrlBean reportUrlBean);
        void onError(int code, String message);
    }
}
