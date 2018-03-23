package com.orientdata.lookforcustomers.view.findcustomer;

import com.orientdata.lookforcustomers.base.BaseView;
import com.orientdata.lookforcustomers.bean.SettingOut;
import com.orientdata.lookforcustomers.bean.UploadPicBean;

/**
 * Created by wy on 2017/11/18.
 * 页面任务接口
 */

public interface ITaskView extends BaseView {
    void createCustomer(SettingOut settingOuts);
    void uploadPicSuc(UploadPicBean uploadPicBean);

}
