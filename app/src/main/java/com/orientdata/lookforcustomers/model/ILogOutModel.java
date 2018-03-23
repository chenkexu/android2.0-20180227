package com.orientdata.lookforcustomers.model;

import com.orientdata.lookforcustomers.bean.ErrBean;

/**
 * 退出登录
 */

public interface ILogOutModel {

    void logOut(Complete complete);//退出

    interface Complete {
        void onSuccess(ErrBean errBean);

        void onError(int code, String message);
    }
}
