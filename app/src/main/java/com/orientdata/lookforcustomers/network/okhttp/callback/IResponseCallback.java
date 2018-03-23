package com.orientdata.lookforcustomers.network.okhttp.callback;


import com.orientdata.lookforcustomers.network.okhttp.bean.OkError;

public interface IResponseCallback {

	public void onSuccess(int tag, Object object);

	public void onError(int tag, OkError error);

}
