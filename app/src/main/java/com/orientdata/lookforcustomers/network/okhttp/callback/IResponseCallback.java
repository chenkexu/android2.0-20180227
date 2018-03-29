package com.orientdata.lookforcustomers.network.okhttp.callback;


import com.orientdata.lookforcustomers.network.okhttp.bean.OkError;

public interface IResponseCallback {

	 void onSuccess(int tag, Object object);

	 void onError(int tag, OkError error);

}
