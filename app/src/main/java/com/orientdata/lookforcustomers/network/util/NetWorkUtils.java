package com.orientdata.lookforcustomers.network.util;


import com.lzy.okgo.OkGo;
import com.orientdata.lookforcustomers.bean.MessageTypeBean;
import com.orientdata.lookforcustomers.bean.TaskTypeBean;
import com.orientdata.lookforcustomers.bean.WrResponse;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;
import com.orientdata.lookforcustomers.network.callback.WrCallback;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;


/**
 * Created by 陈柯旭 on 2018/4/4.
 */

public class NetWorkUtils {


    //获取要展示的测试号
    public static void getOperateState(String cityCode,int type,WrCallback<WrResponse<String>> callback){

        OkGo.<WrResponse<String>>post(HttpConstant.getOperateState)
                .params("userId",UserDataManeger.getInstance().getUserId())
                .params("cityCode",cityCode)
                .params("type",type)
                .execute(callback);
    }



    //是否注册
    public static void phoneIsRegiste(String phone,WrCallback<WrResponse<Integer>> callback){

        OkGo.<WrResponse<Integer>>post(HttpConstant.phone_is_register)
                .params("userId",UserDataManeger.getInstance().getUserId())
                .params("phone",phone)
                .execute(callback);
    }



    //获取敏感词
    public static void getSelectWord(String content,WrCallback<WrResponse<Integer>> callback){
        OkGo.<WrResponse<Integer>>post(HttpConstant.selectWord)
                .params("content",content)
                .execute(callback);
    }

    public static void getSignAndId2(String provincecode,WrCallback<WrResponse<MessageTypeBean>> callback){
        OkGo.<WrResponse<MessageTypeBean>>post(HttpConstant.GET_SIGN_AND_TD)
                .params("userId",UserDataManeger.getInstance().getUserId())
                .params("provincecode",provincecode)
                .execute(callback);
    }


    public static void addAddress(String provincecode,WrCallback<WrResponse<Object>> callback){
        OkGo.<WrResponse<Object>>post(HttpConstant.GET_SIGN_AND_TD)
                .params("userId",UserDataManeger.getInstance().getUserId())
                .params("provincecode",provincecode)
                .execute(callback);
    }




    public static void getSignAndTd(String provincecode,OkHttpClientManager.ResultCallback<TaskTypeBean> callback) {
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());
        map.put("provincecode",provincecode);
        OkHttpClientManager.postAsyn(HttpConstant.GET_SIGN_AND_TD, callback, map);
    }



}
