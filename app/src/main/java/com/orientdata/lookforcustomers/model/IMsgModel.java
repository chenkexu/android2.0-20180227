package com.orientdata.lookforcustomers.model;

import com.orientdata.lookforcustomers.bean.ErrBean;
import com.orientdata.lookforcustomers.bean.MsgListBean;
import com.orientdata.lookforcustomers.bean.ReportListBean;
import com.orientdata.lookforcustomers.bean.ReportUrlBean;
import com.orientdata.lookforcustomers.bean.Result;
import com.orientdata.lookforcustomers.bean.ResultBean;

/**
 * Created by wy on 2017/12/18.
 * 消息
 */

public interface IMsgModel {
    void MsgList( MsgListComplete complete);//消息列表
    void updateMsg(Complete complete,String ids);//更新消息状态
    void msgInfo(MsgInfoComplete msgInfoComplete,int pushMessageId);//消息详情


    interface Complete {
        void onSuccess(ErrBean errBean);
        void onError(int code, String message);
    }
    interface MsgListComplete {
        void onSuccess(MsgListBean msgListBean);
        void onError(int code, String message);
    }
    interface MsgInfoComplete {
        void onSuccess(ResultBean result);
        void onError(int code, String message);
    }
}
