package com.orientdata.lookforcustomers.network.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by Paul on 15/12/3.
 */
public class MyApp extends Application {

    private static Context mAppContext;
    private static MyApp myApp;
    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
        init();
    }

    private void init(){
        mAppContext = getApplicationContext();
    }

    public static Context getMyAppContext(){
        return mAppContext;
    }

    public static MyApp getApp(){
        return myApp;
    }
}
