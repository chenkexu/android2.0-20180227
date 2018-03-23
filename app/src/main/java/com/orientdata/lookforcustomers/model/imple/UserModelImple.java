package com.orientdata.lookforcustomers.model.imple;

import com.orientdata.lookforcustomers.model.IUserModel;

/**
 * Created by wy on 2017/10/25.
 * 用户
 */

public class UserModelImple implements IUserModel {
    @Override
    public String getPhoneType() {
        return "android";
    }

    @Override
    public String getModel() {

        return "xiaomi";
    }

    @Override
    public String getDeviceToken() {
        return "1312312";
    }
}
