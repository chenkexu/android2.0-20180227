package com.orientdata.lookforcustomers.network.api;

import com.orientdata.lookforcustomers.bean.MessageTypeBean;
import com.orientdata.lookforcustomers.bean.WrResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by huang on 2018/4/16.
 */

public interface ApiService {


    @FormUrlEncoded
    @POST("pub/v1.1/getSignAndTd")
    Observable<WrResponse<MessageTypeBean>> getSignAndTd(@Field("provincecode") String provincecode);


    @FormUrlEncoded
    @POST("app/task/selectWord")
    Observable<WrResponse<Integer>> selectWord(@Field("content") String content);



}
