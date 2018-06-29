package com.orientdata.lookforcustomers.view.home.imple;

import com.orientdata.lookforcustomers.base.BaseView;
import com.orientdata.lookforcustomers.bean.MessageAndNoticeBean;

import java.util.List;

/**
 * Created by wy on 2017/12/10.
 */

public interface IMsgAndNoticeView extends BaseView {
   void selectMsgAndAnnouncement(List<MessageAndNoticeBean> messageAndNoticeBeanList);
}
