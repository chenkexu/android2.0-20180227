package com.orientdata.lookforcustomers.model;

import com.orientdata.lookforcustomers.bean.Area;
import com.orientdata.lookforcustomers.bean.AreaOut;
import com.orientdata.lookforcustomers.bean.SelectSettingBean;
import com.orientdata.lookforcustomers.bean.SettingOut;

import java.util.List;

/**
 * Created by wy on 2017/11/18.
 * 获取定向设置
 */

public interface ISelectSettingModel {
    void getSelectSetting(Complete complete,String code);//获取设置
    interface Complete {
        void onSuccess(SettingOut selectSettingBeans);
        void onError(int code, String message);
    }
}
