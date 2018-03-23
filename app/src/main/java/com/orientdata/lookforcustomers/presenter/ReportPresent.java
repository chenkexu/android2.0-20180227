package com.orientdata.lookforcustomers.presenter;

import android.util.Log;

import com.orientdata.lookforcustomers.bean.CertificationOut;
import com.orientdata.lookforcustomers.bean.ReportListBean;
import com.orientdata.lookforcustomers.bean.ReportUrlBean;
import com.orientdata.lookforcustomers.bean.SearchListBean;
import com.orientdata.lookforcustomers.event.ReportDataEvent;
import com.orientdata.lookforcustomers.event.ReportUrlEvent;
import com.orientdata.lookforcustomers.event.SearchListEvent;
import com.orientdata.lookforcustomers.model.ICertificateModel;
import com.orientdata.lookforcustomers.model.IReportModel;
import com.orientdata.lookforcustomers.model.imple.CertificateModelImple;
import com.orientdata.lookforcustomers.model.imple.ReportImple;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.home.IHomeView;
import com.orientdata.lookforcustomers.view.home.IReportView;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by wy on 2017/12/5.
 * 报表
 */

public class ReportPresent<T> extends BasePresenter<IReportView> {
    private IReportView mReportView;
    private IReportModel mReportModel = new ReportImple();

    public ReportPresent(IReportView mReportView) {
        this.mReportView = mReportView;
    }

    @Override
    public void fecth() {

    }
    public void getReportData(final int type, String today,final boolean isShowLoading){
        if(mReportModel!=null){
            if(type == 1 || isShowLoading){
                mReportView.showLoading();
            }
            mReportModel.getReportData(new IReportModel.DataComplete() {
                @Override
                public void onSuccess(ReportListBean reportListBean) {
                    if(type == 1||isShowLoading){
                        mReportView.hideLoading();
                    }
                    ReportDataEvent reportDataEvent = new ReportDataEvent();
                    reportDataEvent.reportListBean = reportListBean;
                    reportDataEvent.type = type;
                    EventBus.getDefault().post(reportDataEvent);
                }

                @Override
                public void onError(int code, String message) {
                    Log.e("==","code == "+code);
                    if(type == 1){
                        mReportView.hideLoading();
                    }
                    ReportDataEvent reportDataEvent = new ReportDataEvent();
                    ReportListBean reportListBean= new ReportListBean();
                    reportListBean.setCode(code);
                    reportListBean.setMsg(message);
                    reportDataEvent.reportListBean = reportListBean;
                    reportDataEvent.type = type;
                    EventBus.getDefault().post(reportDataEvent);
                    ToastUtils.showShort(message);
                }
            },type,today);
        }
    }
    public void getReportUrl(int type, String today, int excType){
        if(mReportModel!=null){
            mReportView.showLoading();
            mReportModel.getReportUrl(new IReportModel.UrlComplete() {
                @Override
                public void onSuccess(ReportUrlBean reportUrlBean) {
                    mReportView.hideLoading();
                    ReportUrlEvent reportUrlEvent = new ReportUrlEvent();
                    reportUrlEvent.reportUrlBean = reportUrlBean;
                    EventBus.getDefault().post(reportUrlEvent);
                }

                @Override
                public void onError(int code, String message) {
                    mReportView.hideLoading();
                    ToastUtils.showShort(message);
                }
            },type,today);
        }
    }
}
