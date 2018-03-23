package com.orientdata.lookforcustomers.view.certification;

import com.orientdata.lookforcustomers.base.BaseView;
import com.orientdata.lookforcustomers.bean.AreaOut;
import com.orientdata.lookforcustomers.bean.CertificationOut;

import java.util.List;

/**
 * Created by wy on 2017/11/18.
 * 认证接口
 */

public interface ICertificateView extends BaseView {
    void getProvinceCity(List<AreaOut> areaOuts);
    void getCertificateMsg(CertificationOut certificationOut);

}
