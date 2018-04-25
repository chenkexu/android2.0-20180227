package com.orientdata.lookforcustomers.model.imple;

import com.orientdata.lookforcustomers.bean.ErrBean;
import com.orientdata.lookforcustomers.bean.MsgListBean;
import com.orientdata.lookforcustomers.bean.ResultBean;
import com.orientdata.lookforcustomers.model.IMsgModel;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * Created by wy on 2017/11/18.
 * 消息
 */
public class MsgImple implements IMsgModel {
    @Override
    public void MsgList(final MsgListComplete complete) {
        String url = HttpConstant.MSG_LIST;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());

        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<MsgListBean>() {
            @Override
            public void onError(Exception e) {
                complete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(MsgListBean response) {
                complete.onSuccess(response);
            }
        }, map);
    }

    @Override
    public void updateMsg(final Complete complete,String ids) {
        String url = HttpConstant.UPDATE_MSG;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("pushMessageIds", ids);
        map.put("userId",UserDataManeger.getInstance().getUserId());
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<ErrBean>() {
            @Override
            public void onError(Exception e) {
                complete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(ErrBean errBean) {
                complete.onSuccess(errBean);
            }
        }, map);
    }

    @Override
    public void msgInfo(final MsgInfoComplete msgInfoComplete, int pushMessageId) {
        String url = HttpConstant.MSG_DETAIL;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("pushMessageId", pushMessageId+"");
        map.put("userId",UserDataManeger.getInstance().getUserId());
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<ResultBean>() {
            @Override
            public void onError(Exception e) {
                msgInfoComplete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(ResultBean result) {
                msgInfoComplete.onSuccess(result);
            }
        }, map);
    }
}
