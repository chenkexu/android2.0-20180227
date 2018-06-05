package com.orientdata.lookforcustomers.view.home;

import com.orientdata.lookforcustomers.bean.Area;
import com.orientdata.lookforcustomers.bean.AreaOut;
import com.orientdata.lookforcustomers.bean.BannerBean;
import com.orientdata.lookforcustomers.bean.BannerBeans;
import com.orientdata.lookforcustomers.model.IProvinceCityModel;
import com.orientdata.lookforcustomers.model.imple.ProvinceCityModelImple;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;
import com.orientdata.lookforcustomers.presenter.BasePresenter;
import com.orientdata.lookforcustomers.util.ToastUtils;

import java.util.List;

import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * Created by ckx on 2018/6/5.
 */

public class MainHomePresenter<T> extends BasePresenter<IHomeMainView> {


    private IHomeMainView iCityPickView;
    private IProvinceCityModel mProvinceModel = new ProvinceCityModelImple();


    public MainHomePresenter(IHomeMainView iHomeMainView){
        this.iCityPickView = iHomeMainView;
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


    public void getBannerPic(){
        MDBasicRequestMap map;
        iCityPickView.showLoading();
        //获取banner信息
        map = new MDBasicRequestMap();
        OkHttpClientManager.postAsyn(HttpConstant.SELECT_BANNER_INFO, new OkHttpClientManager.ResultCallback<BannerBeans>() {
            @Override
            public void onError(Exception e) {
                ToastUtils.showShort(e.getMessage());
                iCityPickView.hideLoading();
            }

            @Override
            public void onResponse(BannerBeans response) {
                iCityPickView.hideLoading();
                if (response.getCode() == 0) {
                    if (response.getResult() == null) {
                        return;
                    }
                    List<BannerBean> list = response.getResult();
                    iCityPickView.getBannerSuc(list);
//                    addSucessful(list);
                } else {
//                    addDefultUrl();
                }
            }
        }, map);
    }





    @Override
    public void fecth() {

    }
}
