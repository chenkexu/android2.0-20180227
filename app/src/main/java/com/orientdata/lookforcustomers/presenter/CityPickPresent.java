package com.orientdata.lookforcustomers.presenter;

import com.orientdata.lookforcustomers.bean.Area;
import com.orientdata.lookforcustomers.bean.AreaOut;
import com.orientdata.lookforcustomers.bean.CertificationOut;
import com.orientdata.lookforcustomers.model.ICertificateModel;
import com.orientdata.lookforcustomers.model.IProvinceCityModel;
import com.orientdata.lookforcustomers.model.imple.CertificateModelImple;
import com.orientdata.lookforcustomers.model.imple.ProvinceCityModelImple;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.findcustomer.ICityPickView;

import java.util.List;

/**
 * Created by wy on 2017/11/18.
 * 城市
 */

public class CityPickPresent<T> extends BasePresenter<ICityPickView> {
    private ICityPickView iCityPickView;
    private IProvinceCityModel mProvinceModel = new ProvinceCityModelImple();
//    private ICertificateModel mCertificateModel = new CertificateModelImple();

    public CityPickPresent(ICityPickView cityPickView) {
        this.iCityPickView = cityPickView;
    }

    public void getProvinceCityData1() {
        if (mProvinceModel != null) {
            iCityPickView.showLoading();
            mProvinceModel.getProvinceCity(new IProvinceCityModel.Complete() {
                @Override
                public void onSuccess(List<AreaOut> areaOuts) {
                    iCityPickView.hideLoading();
                    iCityPickView.getProvinceCity(areaOuts);
                }

                @Override
                public void onProvinceOrCitySuc(List<Area> areas) {

                }

                @Override
                public void onError(int code, String message) {
                    iCityPickView.hideLoading();
                    ToastUtils.showShort(message);
                }
            });
        }
    }

    public void getProvinceCityData() {
        if (mProvinceModel != null) {
            iCityPickView.showLoading();
            mProvinceModel.getProvinceCity(new IProvinceCityModel.Complete() {
                @Override
                public void onSuccess(List<AreaOut> areaOuts) {
                    iCityPickView.hideLoading();
                    iCityPickView.getProvinceCity(areaOuts);
                }

                @Override
                public void onProvinceOrCitySuc(List<Area> areas) {

                }

                @Override
                public void onError(int code, String message) {
                    iCityPickView.hideLoading();
                    ToastUtils.showShort(message);
                }
            });
        }
    }

    @Override
    public void fecth() {

    }
}
