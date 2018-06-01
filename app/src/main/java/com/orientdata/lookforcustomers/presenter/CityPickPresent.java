package com.orientdata.lookforcustomers.presenter;

import com.orientdata.lookforcustomers.bean.Area;
import com.orientdata.lookforcustomers.bean.AreaOut;
import com.orientdata.lookforcustomers.bean.WrResponse;
import com.orientdata.lookforcustomers.model.IProvinceCityModel;
import com.orientdata.lookforcustomers.model.imple.ProvinceCityModelImple;
import com.orientdata.lookforcustomers.network.api.ApiManager;
import com.orientdata.lookforcustomers.network.api.BaseObserver;
import com.orientdata.lookforcustomers.network.api.ParamsUtil;
import com.orientdata.lookforcustomers.network.util.RxUtil;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.findcustomer.ICityPickView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by wy on 2017/11/18.
 * 选择城市的presenter
 */

public class CityPickPresent<T> extends BasePresenter<ICityPickView> {
    private ICityPickView iCityPickView;
    private IProvinceCityModel mProvinceModel = new ProvinceCityModelImple();



//    private ICertificateModel mCertificateModel = new CertificateModelImple();

    public CityPickPresent(ICityPickView cityPickView) {
        this.iCityPickView = cityPickView;
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

    public void AddAddressInfo(String address,String longitude,String latitude) {
        iCityPickView.showLoading();
        HashMap<String, Object> map = ParamsUtil.getMap();
        map.put("address", address);
        map.put("longitude", longitude);
        map.put("latitude", latitude);
        ApiManager.getInstence().getApiService().appAddressPost(ParamsUtil.getParams(map))
                .compose(RxUtil.<WrResponse<Object>>rxSchedulerHelper())
                .subscribe(new BaseObserver<Object>() {
                    @Override
                    protected void onSuccees(WrResponse<Object> t) {
                        iCityPickView.hideLoading();
                        iCityPickView.addAddress(t);
                    }

                    @Override
                    protected void onFailure(String errorInfo, boolean isNetWorkError) {
                        iCityPickView.hideLoading();
                    }
                });
    }




    @Override
    public void fecth() {

    }
}
