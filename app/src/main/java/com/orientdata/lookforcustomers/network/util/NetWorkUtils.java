package com.orientdata.lookforcustomers.network.util;


import com.lzy.okgo.OkGo;
import com.orientdata.lookforcustomers.bean.MessageTypeBean;
import com.orientdata.lookforcustomers.bean.TaskTypeBean;
import com.orientdata.lookforcustomers.bean.WrResponse;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.callback.WrCallback;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.OkHttpClientManager;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;


/**
 * Created by 陈柯旭 on 2018/4/4.
 */

public class NetWorkUtils {





    public static void getSignAndTd(String provincecode,OkHttpClientManager.ResultCallback<TaskTypeBean> callback) {
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());
        map.put("provincecode",provincecode);
        OkHttpClientManager.postAsyn(HttpConstant.GET_SIGN_AND_TD, callback, map);
    }



    public static void getSignAndId2(String provincecode,WrCallback<WrResponse<MessageTypeBean>> callback){

        OkGo.<WrResponse<MessageTypeBean>>post(HttpConstant.GET_SIGN_AND_TD)
                .params("userId",UserDataManeger.getInstance().getUserId())
                .params("provincecode",provincecode)
                .execute(callback);
    }



}
