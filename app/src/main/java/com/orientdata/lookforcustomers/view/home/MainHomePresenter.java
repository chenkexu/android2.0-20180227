package com.orientdata.lookforcustomers.view.home;

import com.orientdata.lookforcustomers.bean.Area;
import com.orientdata.lookforcustomers.bean.AreaOut;
import com.orientdata.lookforcustomers.bean.BannerBean;
import com.orientdata.lookforcustomers.bean.BannerBeans;
import com.orientdata.lookforcustomers.bean.OrderDeliveryBean;
import com.orientdata.lookforcustomers.bean.TaskCountBean;
import com.orientdata.lookforcustomers.bean.WrResponse;
import com.orientdata.lookforcustomers.model.IProvinceCityModel;
import com.orientdata.lookforcustomers.model.imple.ProvinceCityModelImple;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;
import com.orientdata.lookforcustomers.network.api.ApiManager;
import com.orientdata.lookforcustomers.network.api.BaseObserver;
import com.orientdata.lookforcustomers.network.api.ParamsUtil;
import com.orientdata.lookforcustomers.network.util.RxUtil;
import com.orientdata.lookforcustomers.presenter.BasePresenter;
import com.orientdata.lookforcustomers.util.ToastUtils;

import java.util.HashMap;
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


    //获取任务投递的细节
    public void getTaskDeliveryInfo(){

        HashMap<String, Object> map = ParamsUtil.getMap();
        ApiManager.getInstence().getApiService().getNewestTaskThrowExpedite(ParamsUtil.getParams(map))
                .compose(RxUtil.<WrResponse<OrderDeliveryBean>>rxSchedulerHelper())
                .subscribe(new BaseObserver<OrderDeliveryBean>() {
                    @Override
                    protected void onSuccees(WrResponse<OrderDeliveryBean> t) {
                            iCityPickView.getTaskDeliveryInfo(t.getResult());
                    }

                    @Override
                    protected void onFailure(String errorInfo, boolean isNetWorkError) {
                        ToastUtils.showShort(errorInfo);
                    }
                });


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


    //显示小红点的接口
    public void showRedPoint(){
        HashMap<String, Object> map = ParamsUtil.getMap();
        ApiManager.getInstence().getApiService().getUnReadMsgAndUnReadAnnouncement(ParamsUtil.getParams(map))
                .compose(RxUtil.<WrResponse<TaskCountBean>>rxSchedulerHelper())
                .subscribe(new BaseObserver<TaskCountBean>() {
                    @Override
                    protected void onSuccees(WrResponse<TaskCountBean> t) {
                        if (t.getResult()!=null){
                            iCityPickView.showRedPoint(t.getResult());
                        }
                    }

                    @Override
                    protected void onFailure(String errorInfo, boolean isNetWorkError) {
                            ToastUtils.showShort(errorInfo);
                    }
                });
    }




    @Override
    public void fecth() {

    }
}
