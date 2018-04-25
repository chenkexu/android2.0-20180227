package com.orientdata.lookforcustomers.view.findcustomer;

import com.orientdata.lookforcustomers.base.BaseView;
import com.orientdata.lookforcustomers.bean.UploadPicBean;

/**
 * Created by wy on 2017/11/18.
 * 图片库接口
 */

public interface IImgView extends BaseView {
    void uploadPicSuc(UploadPicBean uploadPicBean);
}
