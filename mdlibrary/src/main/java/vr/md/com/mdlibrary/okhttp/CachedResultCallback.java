package vr.md.com.mdlibrary.okhttp;

/**
 * 需要缓存的页面，需要调用此回调
 * @param <T>
 */
public abstract class CachedResultCallback<T> extends MDBaseResponseCallBack<T> {

    /**
     * 调用该方法，会从本地取出缓存数据
     * @param response
     */
    public abstract void onCacheResponse(T response);
}