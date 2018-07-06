package com.orientdata.lookforcustomers.presenter;

import com.orientdata.lookforcustomers.bean.MessageAndNoticeBean;
import com.orientdata.lookforcustomers.bean.ResultBean;
import com.orientdata.lookforcustomers.bean.WrResponse;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;
import com.orientdata.lookforcustomers.network.api.ApiManager;
import com.orientdata.lookforcustomers.network.api.BaseObserver;
import com.orientdata.lookforcustomers.network.api.ParamsUtil;
import com.orientdata.lookforcustomers.network.util.RxUtil;
import com.orientdata.lookforcustomers.view.home.imple.IMsgAndNoticeView;

import java.util.HashMap;
import java.util.List;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * Created by wy on 2017/12/5.
 * 消息
 */

public class MsgAndNoticePresent<T> extends BasePresenter<IMsgAndNoticeView> {
    private IMsgAndNoticeView mMsgView;

    public MsgAndNoticePresent(IMsgAndNoticeView mMsgView) {
        this.mMsgView = mMsgView;
    }

    @Override
    public void fecth() {

    }




    //获取消息和公告
    public void selectMsgAndAnnouncement(){
        mMsgView.showLoading();
        HashMap<String, Object> map = ParamsUtil.getMap();

        ApiManager.getInstence().getApiService().selectMsgAndAnnouncement(ParamsUtil.getParams(map))
                .compose(RxUtil.<WrResponse<List<MessageAndNoticeBean>>>rxSchedulerHelper())
                .subscribe(new BaseObserver<List<MessageAndNoticeBean>>() {


                    @Override
                    protected void onSuccees(WrResponse<List<MessageAndNoticeBean>> t) {
                        mMsgView.hideLoading();
                        List<MessageAndNoticeBean> messageAndNoticeBeanList = t.getResult();
                        mMsgView.selectMsgAndAnnouncement(messageAndNoticeBeanList);
                    }

                    @Override
                    protected void onFailure(String errorInfo, boolean isNetWorkError) {
                        mMsgView.hideLoading();
                    }
                });
    }



    public void deleteMessageRedPoint(int pushMessageId){
        String url = HttpConstant.MSG_DETAIL;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("pushMessageId", pushMessageId+"");
        map.put("userId", UserDataManeger.getInstance().getUserId());
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<ResultBean>() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(ResultBean result) {

            }
        }, map);
    }








}
