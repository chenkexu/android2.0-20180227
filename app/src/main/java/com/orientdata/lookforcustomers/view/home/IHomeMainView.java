package com.orientdata.lookforcustomers.view.home;

import com.orientdata.lookforcustomers.base.BaseView;
import com.orientdata.lookforcustomers.bean.AreaOut;
import com.orientdata.lookforcustomers.bean.BannerBean;
import com.orientdata.lookforcustomers.bean.OrderDeliveryBean;
import com.orientdata.lookforcustomers.bean.TaskCountBean;

import java.util.List;

/**
 * Created by ckx on 2018/6/5.
 */

public interface IHomeMainView extends BaseView {

    void getProvinceCity(List<AreaOut> areaOuts);
    void getBannerSuc(List<BannerBean> list);

    void getTaskDeliveryInfo(OrderDeliveryBean orderDeliveryBean);
    void showRedPoint(TaskCountBean orderDeliveryBean);
}
