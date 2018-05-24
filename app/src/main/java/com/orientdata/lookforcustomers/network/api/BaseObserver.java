package com.orientdata.lookforcustomers.network.api;

import android.accounts.NetworkErrorException;
import android.content.Context;

import com.orientdata.lookforcustomers.bean.WrResponse;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
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
        if (gdResponse.getCode()==0) {
                onSuccees(gdResponse);
        } else {
                onCodeError(gdResponse);
        }
    }

    @Override
    public void onError(Throwable e) {

        if (e instanceof ConnectException
                || e instanceof TimeoutException
                || e instanceof NetworkErrorException
                || e instanceof UnknownHostException
                || e instanceof SocketTimeoutException) {
//            onFailure(UIUtils.getString(R.string.no_network),true);
        } else {
//            onFailure(UIUtils.getString(R.string.no_network),false);
        }
    }

    @Override
    public void onComplete() {
    }


    // 返回成功了,但是code错误
    protected void onCodeError(WrResponse<T> t){
     //   onFailure("msgText="+t.getMsgText(),false);
        onFailure(t.getMsg()+"",false);

    }

   //返回成功
    protected abstract void onSuccees(WrResponse<T> t);

    //返回失败
    protected abstract void onFailure(String errorInfo, boolean isNetWorkError);


}
