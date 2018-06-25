package com.orientdata.lookforcustomers.view.findcustomer.impl;

import com.orientdata.lookforcustomers.base.BaseView;
import com.orientdata.lookforcustomers.bean.MyInfoBean;
import com.orientdata.lookforcustomers.bean.SettingOut;

/**
 * Created by wy on 2017/11/18.
 * 页面任务接口
 */

public interface ITaskViewNew extends BaseView {
    void createCustomer(SettingOut settingOuts);
    void getSignAndTd(MyInfoBean response);
}
