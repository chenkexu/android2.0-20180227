package vr.md.com.mdlibrary.okhttp;

/**
 * Created by Mr.Z on 16/3/17.
 */
public abstract class MDBaseResponseCallBack<T> {

    public abstract void onError(Exception e);

    public abstract void onResponse(T response);
}
