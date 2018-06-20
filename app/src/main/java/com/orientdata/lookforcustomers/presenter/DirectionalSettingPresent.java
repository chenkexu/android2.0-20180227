package com.orientdata.lookforcustomers.presenter;

import com.orientdata.lookforcustomers.bean.AddressCollectInfo;
import com.orientdata.lookforcustomers.bean.SettingOut;
import com.orientdata.lookforcustomers.bean.WrResponse;
import com.orientdata.lookforcustomers.model.ISelectSettingModel;
import com.orientdata.lookforcustomers.model.imple.SelectSettingModelImple;
import com.orientdata.lookforcustomers.network.api.ApiManager;
import com.orientdata.lookforcustomers.network.api.BaseObserver;
import com.orientdata.lookforcustomers.network.api.ParamsUtil;
import com.orientdata.lookforcustomers.network.util.RxUtil;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.findcustomer.IDirectionalSettingView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by wy on 2017/11/18.
 * 定向设置
 */

public class DirectionalSettingPresent<T> extends BasePresenter<IDirectionalSettingView> {
    private IDirectionalSettingView mDirectionSetting;
    private ISelectSettingModel mSelectSettingModel = new SelectSettingModelImple();

    public DirectionalSettingPresent(IDirectionalSettingView mDirectionSetting) {
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





    @Override
    public void fecth() {

    }




}
