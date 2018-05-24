package com.orientdata.lookforcustomers.network.api;

import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.network.HttpConstant;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import vr.md.com.mdlibrary.AppConfig;
import vr.md.com.mdlibrary.MyApp;
import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.utils.MD5;

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
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder newRequest = chain.request().newBuilder();
                newRequest.addHeader("rand", getRand(16));
                newRequest.addHeader("app_id", getApp_id());
                newRequest.addHeader("ver", getVer());
                newRequest.addHeader("plat",getPlat());
                newRequest.addHeader("private_key", getPrivate_key());
                if (!TextUtils.isEmpty(UserDataManeger.getInstance().getUserId())) {
                    newRequest.addHeader("userId",UserDataManeger.getInstance().getUserId());
                }
                if (!TextUtils.isEmpty(UserDataManeger.getInstance().getUserToken())) {
                    newRequest.addHeader("token", UserDataManeger.getInstance().getUserToken());
                }
                newRequest.addHeader("sign", getSignString());
                return chain.proceed(newRequest.build());
            }
        });
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




    /**
     * 检查是否需要登录
     */
    private boolean checkNeedLogin(Map<String, String> map) {
        for (String key : map.keySet()) {
            if (key.equals("userId")) {
                String value = map.get(key);
                if (TextUtils.isEmpty(value)) {
                    startError(101, "请重新登陆");
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * 开启登录页面
     */
    private void startError(int code, String msg) {
        Intent intent = new Intent("com.microdreams.timeprints.error");
        intent.putExtra("error_code", code);
        intent.putExtra("msg", msg);
        MyApp.getContext().sendBroadcast(intent);
    }

    /**
     * 获取缓存到本地的 app_id
     *
     * @return
     */
    private String getApp_id() {
        String app_id = AppConfig.APP_ID;
        return app_id;
    }

    /**
     * 获取时间戳
     *
     * @return
     */
    private String getTimestamp() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String timestamp = format.format(new Date());
        return timestamp;
    }

    private String getSignString() {
        Map<String, String> map = new TreeMap<>();
//        map.putAll(this);
        map.put("private_key", this.getPrivate_key());
        StringBuilder sb = new StringBuilder();
        for (String o : map.keySet()) {
            if (sb.length() == 0) {
                sb.append(o + "=" + map.get(o));
            } else {
                sb.append("&").append(o + "=" + map.get(o));
            }
        }

        return MD5.md5(sb.toString());
    }

    private String getVer() {
        return AppConfig.VER;
    }

    private String getPrivate_key() {
        return AppConfig.PRIVATE_KEY;
    }

    private String getPlat() {
        return AppConfig.PLAT;
    }

    /**
     * 返回一个定长的随机字符串(只包含大小写字母、数字)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    private String getRand(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        int maxLen = ALLCHAR.length();
        for (int i = 0; i < length; i++) {
            sb.append(ALLCHAR.charAt(random.nextInt(maxLen * 4) % maxLen));
        }
        return sb.toString();
    }


}
