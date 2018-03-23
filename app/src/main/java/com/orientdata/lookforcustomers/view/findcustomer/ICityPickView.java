package com.orientdata.lookforcustomers.view.findcustomer;

import com.orientdata.lookforcustomers.base.BaseView;
import com.orientdata.lookforcustomers.bean.AreaOut;
import com.orientdata.lookforcustomers.bean.CertificationOut;
import com.orientdata.lookforcustomers.bean.TaskTypeBean;

import java.util.List;

/**
 * Created by wy on 2017/11/18.
 * 城市选择接口
 */

public interface ICityPickView extends BaseView {
    void getProvinceCity(List<AreaOut> areaOuts);

    //void getCertificateMsg(CertificationOut certificationOut);

}
