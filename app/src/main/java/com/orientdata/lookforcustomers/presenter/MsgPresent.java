package com.orientdata.lookforcustomers.presenter;

import com.orientdata.lookforcustomers.bean.ErrBean;
import com.orientdata.lookforcustomers.bean.MsgListBean;
import com.orientdata.lookforcustomers.bean.ResultBean;
import com.orientdata.lookforcustomers.event.MsgInfoEvent;
import com.orientdata.lookforcustomers.event.MsgListEvent;
import com.orientdata.lookforcustomers.event.MsgUpdateEvent;
import com.orientdata.lookforcustomers.model.IMsgModel;
import com.orientdata.lookforcustomers.model.imple.MsgImple;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.home.IMsgView;

import org.greenrobot.eventbus.EventBus;

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


    public void msgInfo(int pushMessageId){
        if(mMsgModel!=null){
            mMsgView.showLoading();
            mMsgModel.msgInfo(new IMsgModel.MsgInfoComplete() {
                @Override
                public void onSuccess(ResultBean result) {
                    mMsgView.hideLoading();
                    MsgInfoEvent msgInfoEvent = new MsgInfoEvent();
                    msgInfoEvent.result = result;
                    EventBus.getDefault().post(msgInfoEvent);
                }

                @Override
                public void onError(int code, String message) {
                    mMsgView.hideLoading();
                    ToastUtils.showShort(message);
                }
            },pushMessageId);
        }
    }













}
