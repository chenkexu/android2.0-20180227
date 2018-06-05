package com.orientdata.lookforcustomers.network.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.network.HttpConstant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by huang on 2018/4/16.
 */

public class ApiManager {
    public static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";


    private static ApiManager mRetrofitManager;
    private Retrofit mRetrofit;
    private ApiService mApiService;
    private ApiManager() {
        initRetrofit();
    }

    public static ApiManager getInstence(){
        if (mRetrofitManager==null){
            synchronized (ApiManager.class) {
                if (mRetrofitManager == null)
                    mRetrofitManager = new ApiManager();
            }
        }
        return mRetrofitManager;
    }

    private final Gson mGson  = new GsonBuilder().setLenient().create();  // 设置GSON的非严格模式setLenient()


    private void initRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.writeTimeout(1000, TimeUnit.SECONDS);
        builder.connectTimeout(1000, TimeUnit.SECONDS);
        builder.addInterceptor(getHttpLoggingInterceptor());
        builder.retryOnConnectionFailure(true);
        OkHttpClient client = builder.build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(HttpConstant.HOST_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .client(client)
                .build() ;
        mApiService = mRetrofit.create(ApiService.class);
    }

    /**
     * 日志输出
     * 自行判定是否添加
     * @return
     */
    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        //日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Logger.d(message);
            }
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }




    public ApiService getApiService() {
        return mApiService;
    }








}
