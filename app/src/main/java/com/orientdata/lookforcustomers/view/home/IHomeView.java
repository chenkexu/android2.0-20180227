package com.orientdata.lookforcustomers.view.home;

import com.orientdata.lookforcustomers.base.BaseView;
import com.orientdata.lookforcustomers.bean.CertificationOut;
import com.orientdata.lookforcustomers.bean.SearchListBean;

/**
 * Created by wy on 2017/10/25.
 */

public interface IHomeView extends BaseView {
    void getCertificateMsg(CertificationOut certificationOut, boolean isCertificate);
    void getSearchList(SearchListBean searchListBean);
}
