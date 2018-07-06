package com.orientdata.lookforcustomers.view.home;

import com.orientdata.lookforcustomers.base.BaseView;
import com.orientdata.lookforcustomers.bean.MessageAndNoticeBean;

/**
 * Created by wy on 2017/12/10.
 */

public interface IMsgView extends BaseView {
    void selectMsgAndAnnouncement(MessageAndNoticeBean messageAndNoticeBean);
}
