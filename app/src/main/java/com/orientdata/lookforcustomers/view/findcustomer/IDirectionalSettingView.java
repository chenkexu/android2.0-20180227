package com.orientdata.lookforcustomers.view.findcustomer;

import com.orientdata.lookforcustomers.base.BaseView;
import com.orientdata.lookforcustomers.bean.AreaOut;
import com.orientdata.lookforcustomers.bean.CertificationOut;
import com.orientdata.lookforcustomers.bean.SettingOut;

import java.util.List;

/**
 * Created by wy on 2017/11/18.
 * 定向设置接口
 */

public interface IDirectionalSettingView extends BaseView {
    void getSelectSetting(SettingOut settingOuts);



}
