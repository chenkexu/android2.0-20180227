package com.orientdata.lookforcustomers.network.okhttp.bean;


import com.orientdata.lookforcustomers.network.okhttp.callback.IResponseCallback;

/**
 * Created by Paul on 15/12/10.
 */
public class OkResult {
    private OkTag tag;
    private Object object;
    private IResponseCallback iResponseCallback;
    public OkResult() {
    }

    public OkResult(OkTag tag, Object object, IResponseCallback iResponseCallback) {
        this.tag = tag;
        this.object = object;
        this.iResponseCallback = iResponseCallback;
    }

    public OkTag getTag() {
        return tag;
    }

    public void setTag(OkTag tag) {
        this.tag = tag;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public IResponseCallback getResponseCallback() {
        return iResponseCallback;
    }

    public void setResponseCallback(IResponseCallback iResponseCallback) {
        this.iResponseCallback = iResponseCallback;
    }
}
