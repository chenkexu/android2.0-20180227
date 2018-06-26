package com.orientdata.lookforcustomers.view.home.imple;

import com.orientdata.lookforcustomers.base.BaseView;
import com.orientdata.lookforcustomers.bean.CertificationOut;
import com.orientdata.lookforcustomers.bean.TaskCountBean;
import com.orientdata.lookforcustomers.bean.UserInfoBean;
import com.orientdata.lookforcustomers.event.MyMoneyEvent;

/**
 * Created by wy on 2017/10/25.
 */

public interface IMeView extends BaseView {
    void getCertificateMsg(CertificationOut certificationOut, boolean isCertificate);
    void getUserData(UserInfoBean userInfoBean);
    void getMyMoney(MyMoneyEvent MyMoneyEvent);

    void getTaskCount(TaskCountBean MyMoneyEvent);

}
