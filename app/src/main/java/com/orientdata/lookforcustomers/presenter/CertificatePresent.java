package com.orientdata.lookforcustomers.presenter;

import com.orientdata.lookforcustomers.bean.Area;
import com.orientdata.lookforcustomers.bean.AreaOut;
import com.orientdata.lookforcustomers.bean.CertificationOut;
import com.orientdata.lookforcustomers.model.ICertificateModel;
import com.orientdata.lookforcustomers.model.IProvinceCityModel;
import com.orientdata.lookforcustomers.model.imple.CertificateModelImple;
import com.orientdata.lookforcustomers.model.imple.ProvinceCityModelImple;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.certification.ICertificateView;
import java.util.List;
/**
 * Created by wy on 2017/11/18.
 * 认证
 */

public class CertificatePresent<T> extends BasePresenter<ICertificateView> {
    private ICertificateView mCertificateView;
    private IProvinceCityModel mProvinceModel = new ProvinceCityModelImple();
    private ICertificateModel mCertificateModel = new CertificateModelImple();

    public CertificatePresent(ICertificateView mCertificateView) {
        this.mCertificateView = mCertificateView;
    }
    public void getProvinceCityData1(){
        if(mProvinceModel!=null){
            mCertificateView.showLoading();
            mProvinceModel.getProvinceCity(new IProvinceCityModel.Complete() {
                @Override
                public void onSuccess(List<AreaOut> areaOuts) {
                    mCertificateView.hideLoading();
                    mCertificateView.getProvinceCity(areaOuts);
                }

                @Override
                public void onProvinceOrCitySuc(List<Area> areas) {

                }

                @Override
                public void onError(int code, String message) {
                    mCertificateView.hideLoading();
                    ToastUtils.showShort(message);
                }
            });
        }
    }
    public void getProvinceCityData(){
        if(mProvinceModel!=null){
            mCertificateView.showLoading();
            mProvinceModel.getProvinceCity(new IProvinceCityModel.Complete() {
                @Override
                public void onSuccess(List<AreaOut> areaOuts) {
                    mCertificateView.hideLoading();
                    mCertificateView.getProvinceCity(areaOuts);
                }

                @Override
                public void onProvinceOrCitySuc(List<Area> areas) {

                }

                @Override
                public void onError(int code, String message) {
                    mCertificateView.hideLoading();
                    ToastUtils.showShort(message);
                }
            });
        }
    }
    public void getCertificateMsg(){
        if(mCertificateModel!=null){
            mCertificateView.showLoading();
            mCertificateModel.getCertificateMsg(new ICertificateModel.GetMsgComplete() {
                @Override
                public void onSuccess(CertificationOut certificationOut) {
                    mCertificateView.hideLoading();
                    mCertificateView.getCertificateMsg(certificationOut);
                }

                @Override
                public void onError(int code, String message) {
                    mCertificateView.hideLoading();
                    ToastUtils.showShort(message);
                }
            });
        }

    }

    @Override
    public void fecth() {

    }
}
