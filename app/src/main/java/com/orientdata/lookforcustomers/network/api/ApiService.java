package com.orientdata.lookforcustomers.network.api;

import com.orientdata.lookforcustomers.bean.AddressCollectInfo;
import com.orientdata.lookforcustomers.bean.MessageTypeBean;
import com.orientdata.lookforcustomers.bean.WrResponse;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by huang on 2018/4/16.
 */

public interface ApiService {


    @FormUrlEncoded
    @POST("pub/v1.1/getSignAndTd")
    Observable<WrResponse<MessageTypeBean>> getSignAndTd(@Field("provincecode") String provincecode);


    @FormUrlEncoded
    @POST("app/task/selectWord")
    Observable<WrResponse<Integer>> selectWord(@FieldMap HashMap<String, Object> map);


    //收藏地址
    @FormUrlEncoded
    @POST("app/address")
    Observable<WrResponse<Object>> appAddressPost(@FieldMap HashMap<String, Object> map);

    //删除地址
    @FormUrlEncoded
    @DELETE("app/address")
    Observable<WrResponse<Object>> appAddressDelete(@FieldMap HashMap<String, Object> map);




    //查询收藏的地址
    @GET("app/address")
    Observable<WrResponse<List<AddressCollectInfo>>> appAddressGet(@QueryMap HashMap<String, Object> map);


}
