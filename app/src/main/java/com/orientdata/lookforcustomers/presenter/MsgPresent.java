package com.orientdata.lookforcustomers.presenter;

import com.orientdata.lookforcustomers.bean.ErrBean;
import com.orientdata.lookforcustomers.bean.MessageAndNoticeBean;
import com.orientdata.lookforcustomers.bean.MsgListBean;
import com.orientdata.lookforcustomers.bean.WrResponse;
import com.orientdata.lookforcustomers.event.MsgListEvent;
import com.orientdata.lookforcustomers.event.MsgUpdateEvent;
import com.orientdata.lookforcustomers.model.IMsgModel;
import com.orientdata.lookforcustomers.model.imple.MsgImple;
import com.orientdata.lookforcustomers.network.api.ApiManager;
import com.orientdata.lookforcustomers.network.api.BaseObserver;
import com.orientdata.lookforcustomers.network.api.ParamsUtil;
import com.orientdata.lookforcustomers.network.util.RxUtil;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.home.IMsgView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

/**
 * Created by wy on 2017/12/5.
 * 消息
 */

public class MsgPresent<T> extends BasePresenter<IMsgView> {
    private IMsgView mMsgView;
    private IMsgModel mMsgModel = new MsgImple();

    public MsgPresent(IMsgView mMsgView) {
        this.mMsgView = mMsgView;
    }

    @Override
    public void fecth() {

    }


    public void msgList(){
        if(mMsgModel!=null){
            mMsgView.showLoading();
            mMsgModel.MsgList(new IMsgModel.MsgListComplete() {
                @Override
                public void onSuccess(MsgListBean msgListBean) {
                    mMsgView.hideLoading();
                    MsgListEvent msgListEvent = new MsgListEvent();
                    msgListEvent.msgListBean = msgListBean;
                    EventBus.getDefault().post(msgListEvent);
                }

                @Override
                public void onError(int code, String message) {
                    mMsgView.hideLoading();
                    ToastUtils.showShort(message);
                }
            });
        }
    }



    public void updateMsg(String ids){
        if(mMsgModel!=null){
            mMsgView.showLoading();
            //请求更新消息
            mMsgModel.updateMsg(new IMsgModel.Complete() {
                @Override
                public void onSuccess(ErrBean errBean) {
                    mMsgView.hideLoading();
                    //消息更新成功之后，发送广播
                    MsgUpdateEvent msgUpdateEvent = new MsgUpdateEvent();
                    msgUpdateEvent.errBean = errBean;
                    EventBus.getDefault().post(msgUpdateEvent);
                }

                @Override
                public void onError(int code, String message) {
                    mMsgView.hideLoading();
                    ToastUtils.showShort(message);
                }
            },ids);
        }
    }



    public void updateUserAnnStatus(int announcementId){

        HashMap<String, Object> map = ParamsUtil.getMap();
        map.put("announcementId", announcementId + "");
        ApiManager.getInstence().getApiService().updateUserAnnStatus(ParamsUtil.getParams(map))
                .compose(RxUtil.<WrResponse<MessageAndNoticeBean>>rxSchedulerHelper())
                .subscribe(new BaseObserver<MessageAndNoticeBean>() {
                    @Override
                    protected void onSuccees(WrResponse<MessageAndNoticeBean> t) {
                        if (t.getResult()!=null) {
                            mMsgView.selectMsgAndAnnouncement(t.getResult());
                        }
                    }

                    @Override
                    protected void onFailure(String errorInfo, boolean isNetWorkError) {
                        ToastUtils.showShort(errorInfo);
                    }
//                    @Override
//                    protected void onSuccees(WrResponse t) {
//                        mMsgView.selectMsgAndAnnouncement(t.getResult());
//                    }
//
//                    @Override
//                    protected void onFailure(String errorInfo, boolean isNetWorkError) {
//                        ToastUtils.showShort(errorInfo);
//                    }
                });
    }





    //更新消息小红点
    public void msgInfo(int pushMessageId){
        HashMap<String, Object> map = ParamsUtil.getMap();
        map.put("pushMessageId", pushMessageId + "");
        ApiManager.getInstence().getApiService().selectMsgInfo(ParamsUtil.getParams(map))
                .compose(RxUtil.<WrResponse<MessageAndNoticeBean>>rxSchedulerHelper())
                .subscribe(new BaseObserver<MessageAndNoticeBean>() {
                    @Override
                    protected void onSuccees(WrResponse<MessageAndNoticeBean> t) {
                        if (t.getResult()!=null) {
                            mMsgView.selectMsgAndAnnouncement(t.getResult());
                        }

                    }

                    @Override
                    protected void onFailure(String errorInfo, boolean isNetWorkError) {

                    }
                });

    }













}
