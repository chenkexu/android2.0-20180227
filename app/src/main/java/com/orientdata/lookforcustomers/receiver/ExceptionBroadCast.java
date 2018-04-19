package com.orientdata.lookforcustomers.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.login.imple.LoginAndRegisterActivity;

/**
 * Created by Mr.Z on 16/7/12.
 * 错误接受 例如：重新登录
 */
public class ExceptionBroadCast extends BroadcastReceiver {
    /**
     * 返回状态码
     */
    private int error_code;

    /**
     * 返回错误信息
     */
    private String msg;
    private Intent intent;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.d("接收到了重新登录的返回信息");
        this.context = context;
        this.intent = intent;
        error_code = intent.getIntExtra("error_code", 0);
        msg = intent.getStringExtra("msg");
//        handleError(error_code);
        if(error_code == 101){
            reStartLogin();
        }

    }

    /**
     * 根据状态码，处理异常
     */
    private void handleError(int code) {
        ToastUtils.showShort(msg);
    }

    /**
     * 跳转登录页面
     */
    private void reStartLogin() {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
        String shortClassName = info.topActivity.getShortClassName(); //类名
        if (!".ui.login.LoginActivity".equals(shortClassName)) {
            intent.setClass(context, LoginAndRegisterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("isReLogin",true);
            intent.putExtra("isNoBack",true);
            context.startActivity(intent);
        }
    }
}
