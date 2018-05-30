package com.orientdata.lookforcustomers.network.api;

import com.orientdata.lookforcustomers.bean.MessageTypeBean;
import com.orientdata.lookforcustomers.bean.WrResponse;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
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
    Observable<WrResponse<Integer>> selectWord(@FieldMap HashMap<String, Object> map);


    @FormUrlEncoded
    @DELETE("app/address")
    Observable<WrResponse<Object>> delteAddress(@Field("addressId") String addressId);



    @FormUrlEncoded
    @POST("app/address")
    Observable<WrResponse<Object>> appAddressPost2(
                                                  @Field("address") String address,
                                                  @Field("longitude") String longitude,
                                                  @Field("latitude") String latitude);

    @FormUrlEncoded
    @POST("app/address")
    Observable<WrResponse<Object>> appAddressPost(@FieldMap HashMap<String, Object> map);

//    @FormUrlEncoded
//    @PUT("app/address")
//    Observable<WrResponse<Object>> getAddress(@Field("userId") String userId);

}
