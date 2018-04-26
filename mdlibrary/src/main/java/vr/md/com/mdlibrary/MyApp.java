package vr.md.com.mdlibrary;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

/**
 * Created by Lenovo on 2016/4/11.
 */
public class MyApp extends MultiDexApplication {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        // CrashHandler handler = CrashHandler.getInstance();
        // handler.init(getContext());
        //initUmengKey();
    }

    /**
     * 各个平台的配置，建议放在全局Application或者程序入口
     */
  /*  private void initUmengKey() {
        //微信    wx12342956d1cab4f9,a5ae111de7d9ea137e88a5e02c07c94d
        PlatformConfig.setWeixin("wxe8e7aa428cb7f811", "4411b5dcfc36fafcfd7b4251facb9c49");
        //新浪微博
        //PlatformConfig.setSinaWeibo("1820778010", "a7b9bce7d2b722b9b81b3f3f0123d3e2");
        //qq
        PlatformConfig.setQQZone("1104880441", "ZDWAQKzriJExQMrq");

        Config.DEBUG = false;
    }*/


    public static Context getContext() {
        return context;
    }


}