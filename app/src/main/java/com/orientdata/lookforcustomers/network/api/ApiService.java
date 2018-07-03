package com.orientdata.lookforcustomers.network.api;

import com.orientdata.lookforcustomers.bean.AddressCollectInfo;
import com.orientdata.lookforcustomers.bean.MessageAndNoticeBean;
import com.orientdata.lookforcustomers.bean.MessageTypeBean;
import com.orientdata.lookforcustomers.bean.OrderDeliveryBean;
import com.orientdata.lookforcustomers.bean.TaskBasicInfo;
import com.orientdata.lookforcustomers.bean.TaskCountBean;
import com.orientdata.lookforcustomers.bean.WrResponse;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
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
    @POST("app/address_delete")
    Observable<WrResponse<Object>> appAddressDelete(@FieldMap HashMap<String, Object> map);



    //查询收藏的地址
    @GET("app/address")
    Observable<WrResponse<List<AddressCollectInfo>>> appAddressGet(@QueryMap HashMap<String, Object> map);

    //查询任务数量
    @GET("app/task/getTaskCount")
    Observable<WrResponse<TaskCountBean>> getTaskCount(@QueryMap HashMap<String, Object> map);

    //获取创建任务的基本信息, 进入创建任务页面时获取创建任务的基本信息，目前为支
    @FormUrlEncoded
    @POST("app/getCreateTaskBasicInfo")
    Observable<WrResponse<TaskBasicInfo>> getCreateTaskBasicInfo(@FieldMap HashMap<String, Object> map);



    //查询任务投递信息
    @GET("app/taskThrowExpedite")
    Observable<WrResponse<Object>> getTaskThrowExpedite(@QueryMap HashMap<String, Object> map);


    //上传短信任务
    @FormUrlEncoded
    @POST("app/v1.1/task/insertTask")
    Observable<WrResponse<TaskCountBean>> insertTask(@FieldMap HashMap<String, Object> map);


    //戳我加速接口
    @FormUrlEncoded
    @POST("app/taskThrowExpedite")
    Observable<WrResponse<Object>> pokeMeSpeedUp(@FieldMap HashMap<String, Object> map);




    //首页请求任务投放接口
    @GET("app/task/getNewestTaskThrowExpedite")
    Observable<WrResponse<OrderDeliveryBean>> getNewestTaskThrowExpedite(@QueryMap HashMap<String, Object> map);



    //获取消息和公告列表
    @GET("app/msg/announcement/selectMsgAndAnnouncement")
    Observable<WrResponse<List<MessageAndNoticeBean>>> selectMsgAndAnnouncement(@QueryMap HashMap<String, Object> map);




    //显示小红点
    @GET("app/getUnReadMsgAndUnReadAnnouncement")
    Observable<WrResponse<TaskCountBean>> getUnReadMsgAndUnReadAnnouncement(@QueryMap HashMap<String, Object> map);





    //获取任务单价和任务几天之后可以创建
    Observable<WrResponse<Object>> selectPriceAndDate(@QueryMap HashMap<String, Object> map);





}
