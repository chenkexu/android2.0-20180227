package com.orientdata.lookforcustomers.network.api;

import android.accounts.NetworkErrorException;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.app.MyApplication;
import com.orientdata.lookforcustomers.bean.WrResponse;
import com.orientdata.lookforcustomers.view.login.imple.LoginAndRegisterActivity;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by huang on 2018/4/16.
 */

public  abstract class BaseObserver<T> implements Observer<WrResponse<T>> {

    protected Context mContext;

    public BaseObserver(Context cxt) {
        this.mContext = cxt;
    }

    public BaseObserver() {
    }

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(WrResponse<T> gdResponse) {
        if (gdResponse.getCode() == 0) {
                onSuccees(gdResponse);
        } else {
                onCodeError(gdResponse);
        }
    }

    @Override
    public void onError(Throwable e) {
        Logger.e(e.getMessage());
        if (e instanceof ConnectException
                || e instanceof TimeoutException
                || e instanceof NetworkErrorException
                || e instanceof UnknownHostException
                || e instanceof SocketTimeoutException) {
            onFailure("服务器错误，请检查网络设置",true);
        } else {
            onFailure(e.getMessage(),false);
        }
    }

    @Override
    public void onComplete() {
    }


    // 返回成功了,但是code错误
    protected void onCodeError(WrResponse<T> t){
        if (t.getCode() == -100) {
            startError(101, t.getMsg());
        }else{
            onFailure(t.getMsg()+"",false);
        }
    }

   //返回成功
    protected abstract void onSuccees(WrResponse<T> t);

    //返回失败
    protected abstract void onFailure(String errorInfo, boolean isNetWorkError);



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
        if(code == 101){
            reStartLogin();
        }


//        Logger.d("需要重新登录");
//        Intent intent = new Intent("com.microdreams.timeprints.error");
//        intent.putExtra("error_code", code);
//        intent.putExtra("msg", msg);
//        MyApplication.getContext().sendBroadcast(intent);
    }


    /**
     * 跳转登录页面
     */
    private void reStartLogin() {
        Intent intent = new Intent(MyApplication.getContext(), LoginAndRegisterActivity.class);
        ActivityManager manager = (ActivityManager) MyApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
        String shortClassName = info.topActivity.getShortClassName(); //类名
        if (!".ui.login.LoginActivity".equals(shortClassName)) {
            intent.setClass(MyApplication.getContext(), LoginAndRegisterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("isReLogin",true);
            intent.putExtra("isNoBack",true);
            MyApplication.getContext().startActivity(intent);
        }
    }

}
