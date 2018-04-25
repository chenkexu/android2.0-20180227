package com.orientdata.lookforcustomers.view.findcustomer;

import com.orientdata.lookforcustomers.base.BaseView;
import com.orientdata.lookforcustomers.bean.PicListBean;

/**
 * Created by wy on 2017/11/18.
 * 图片库接口
 */

public interface IPicView extends BaseView {
    void picListSuc(PicListBean picListBean);
}
