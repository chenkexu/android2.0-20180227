package com.orientdata.lookforcustomers.presenter;

import com.orientdata.lookforcustomers.bean.AddressCollectInfo;
import com.orientdata.lookforcustomers.bean.NextStepCheckBean;
import com.orientdata.lookforcustomers.bean.SettingOut;
import com.orientdata.lookforcustomers.bean.WrResponse;
import com.orientdata.lookforcustomers.model.ISelectSettingModel;
import com.orientdata.lookforcustomers.model.imple.SelectSettingModelImple;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;
import com.orientdata.lookforcustomers.network.api.ApiManager;
import com.orientdata.lookforcustomers.network.api.BaseObserver;
import com.orientdata.lookforcustomers.network.api.ParamsUtil;
import com.orientdata.lookforcustomers.network.util.RxUtil;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.findcustomer.impl.IDirectionalSettingView3;

import java.util.HashMap;
import java.util.List;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * Created by wy on 2017/11/18.
 * 定向设置
 */

public class DirectionalSettingPresentNew<T> extends BasePresenter<IDirectionalSettingView3> {
    private IDirectionalSettingView3 mDirectionSetting;
    private ISelectSettingModel mSelectSettingModel = new SelectSettingModelImple();

    public DirectionalSettingPresentNew(IDirectionalSettingView3 mDirectionSetting) {
        this.mDirectionSetting = mDirectionSetting;
    }


    public void getSelectSetting(String code){
        if(mSelectSettingModel!=null){
            mDirectionSetting.showLoading();
            mSelectSettingModel.getSelectSetting(new ISelectSettingModel.Complete() {
                @Override
                public void onSuccess(SettingOut selectSettingBeans) {
                    mDirectionSetting.hideLoading();
                    mDirectionSetting.getSelectSetting(selectSettingBeans);
                }

                @Override
                public void onError(int code, String message) {
                    mDirectionSetting.hideLoading();
                    ToastUtils.showShort(message);
                }
            },code);
        }
    }


    //取消收藏
    public void appAddressDelete(int addressId) {
        mDirectionSetting.showLoading();
        HashMap<String, Object> map = ParamsUtil.getMap();
        map.put("addressId",addressId+"");
        ApiManager.getInstence().getApiService().appAddressDelete(ParamsUtil.getParams(map))
            .compose(RxUtil.<WrResponse<Object>>rxSchedulerHelper())
            .subscribe(new BaseObserver<Object>() {
        @Override
        protected void onSuccees(WrResponse<Object> t) {
            mDirectionSetting.hideLoading();
            mDirectionSetting.deleteAddressSucess();

        }

        @Override
        protected void onFailure(String errorInfo, boolean isNetWorkError) {
            mDirectionSetting.hideLoading();
            mDirectionSetting.deleteAddressError();
        }
    });
}


    //添加收藏
    public void AddAddressInfo(String address,String longitude,String latitude) {
        mDirectionSetting.showLoading();
        HashMap<String, Object> map = ParamsUtil.getMap();
        map.put("address", address);
        map.put("longitude", longitude);
        map.put("latitude", latitude);
        ApiManager.getInstence().getApiService().appAddressPost(ParamsUtil.getParams(map))
                .compose(RxUtil.<WrResponse<Object>>rxSchedulerHelper())
                .subscribe(new BaseObserver<Object>() {
                    @Override
                    protected void onSuccees(WrResponse<Object> t) {
                        mDirectionSetting.hideLoading();
                        mDirectionSetting.AddAddressSucess();
                    }

                    @Override
                    protected void onFailure(String errorInfo, boolean isNetWorkError) {
                        mDirectionSetting.hideLoading();
                        ToastUtils.showShort(errorInfo);
                        mDirectionSetting.AddAddressError();
                    }
                });
    }


    //查询
    public void getAllAddress() {
        mDirectionSetting.showLoading();
        HashMap<String, Object> map = ParamsUtil.getMap();

        ApiManager.getInstence().getApiService().appAddressGet(ParamsUtil.getParams(map))
                .compose(RxUtil.<WrResponse<List<AddressCollectInfo>>>rxSchedulerHelper())
                .subscribe(new BaseObserver<List<AddressCollectInfo>>() {
                    @Override
                    protected void onSuccees(WrResponse<List<AddressCollectInfo>> t) {
                        mDirectionSetting.hideLoading();
                        List<AddressCollectInfo> result = t.getResult();
                        mDirectionSetting.getAllCollectionAddress(result);
                    }

                    @Override
                    protected void onFailure(String errorInfo, boolean isNetWorkError) {
                        ToastUtils.showShort(errorInfo);
                        mDirectionSetting.hideLoading();
                    }
                });
    }



    public void selectPretask(String b_latitude,String b_longitude,String mCityCode) {

        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());
        map.put("longitude", b_longitude + "");
        map.put("latitude", b_latitude + "");
        map.put("cityCode", mCityCode);

        mDirectionSetting.showLoading();
        // TODO: 2018/4/4 接口改了，参数少传了一个
        OkHttpClientManager.postAsyn(HttpConstant.SELECT_NEXT_STEP_CHECK, new OkHttpClientManager.ResultCallback<NextStepCheckBean>() {
            @Override
            public void onError(Exception e) {
                mDirectionSetting.hideLoading();
                ToastUtils.showShort(e.getMessage());
            }

            @Override
            public void onResponse(NextStepCheckBean response) {
                mDirectionSetting.hideLoading();
                if (response.getResult()!=null) {
                    mDirectionSetting.getPretask(response.getResult());
                }
            }
        }, map);
    }





    @Override
    public void fecth() {

    }




}
