package com.orientdata.lookforcustomers.model;

import com.orientdata.lookforcustomers.bean.CommissionListBean;
import com.orientdata.lookforcustomers.bean.ErrBean;

/**
 * Created by wy on 2017/11/18.
 * 我的
 */

public interface IMineModel {
    void myInfo(CommissionListComplete complete);//我的信息
    interface Complete {
        void onSuccess(ErrBean errBean);

        void onError(int code, String message);
    }
    interface CommissionListComplete {
        void onSuccess(CommissionListBean errBean);

        void onError(int code, String message);
    }
    
}
