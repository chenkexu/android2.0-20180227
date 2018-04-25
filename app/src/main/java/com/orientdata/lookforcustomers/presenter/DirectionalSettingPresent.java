package com.orientdata.lookforcustomers.presenter;

import com.orientdata.lookforcustomers.bean.SettingOut;
import com.orientdata.lookforcustomers.model.ISelectSettingModel;
import com.orientdata.lookforcustomers.model.imple.SelectSettingModelImple;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.findcustomer.IDirectionalSettingView;

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


    @Override
    public void fecth() {

    }
}
