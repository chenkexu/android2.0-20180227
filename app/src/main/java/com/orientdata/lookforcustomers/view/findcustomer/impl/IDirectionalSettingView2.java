package com.orientdata.lookforcustomers.view.findcustomer.impl;

import com.orientdata.lookforcustomers.base.BaseView;
import com.orientdata.lookforcustomers.bean.AddressCollectInfo;
import com.orientdata.lookforcustomers.bean.SettingOut;

/**
 * Created by wy on 2017/11/18.
 * 定向设置接口
 */

public interface IDirectionalSettingView2 extends BaseView {
    void getSelectSetting(SettingOut settingOuts);
    void getAllCollectionAddress(AddressCollectInfo addressCollectInfo);
    void AddAddressSucess();
    void AddAddressError();
}
