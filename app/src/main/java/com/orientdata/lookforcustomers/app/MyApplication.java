package com.orientdata.lookforcustomers.app;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

import com.baidu.mapapi.SDKInitializer;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.util.SharedPreferencesTool;
import com.orientdata.lookforcustomers.util.map.LocationService;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import vr.md.com.mdlibrary.MyApp;

/**
 * Created by wy on 2017/9/12.
 */

public class MyApplication extends MyApp {

    private static MyApplication instance;
    public LocationService locationService;
    public Vibrator mVibrator;
    public boolean isFirstLocation = true;

    {
        PlatformConfig.setWeixin("wx94a5977e9ef0661b", "98011640b84874ed1eae20d790166aed");
        PlatformConfig.setQQZone("1106072736", "IJGYc4O9aW2RSKdM");
        PlatformConfig.setSinaWeibo("1331485422", "3cee748646dce776b55a7ef416472244", "http://sns.whalecloud.com");
        Config.DEBUG = true;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        //PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        //UMShareAPI.get(this);

        /***
         * 初始化定位sdk
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
        SharedPreferencesTool.getInstance().init(getApplicationContext());

        UMShareAPI.get(this);
        //打印日志初始化
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }
}
