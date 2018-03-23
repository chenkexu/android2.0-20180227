package vr.md.com.mdlibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.provider.Settings;
import android.util.Log;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler instance;  //单例引用，这里我们做成单例的，因为我们一个应用程序里面只需要一个UncaughtExceptionHandler实例
    private Context context;

    private CrashHandler() {
    }

    public synchronized static CrashHandler getInstance() {  //同步方法，以免单例多线程环境下出现异常
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    public void init(Context context) {  //初始化，把当前对象设置成UncaughtExceptionHandler处理器
        this.context = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {  //当有未处理的异常发生时，就会来到这里。。
        Log.d("Sandy", "uncaughtException, thread: " + thread
                + " name: " + thread.getName() + " id: " + thread.getId() + "exception: "
                + ex);
        String threadName = thread.getName();
        //Log.d("Sandy",threadName);
        //System.exit(0);
        if ("main".equals(threadName)) {
            Log.d("Sandy", "------------------");
            //System.exit(0);
        }

    }

}