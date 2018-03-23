package com.orientdata.lookforcustomers.network.okhttp.manager;


import com.orientdata.lookforcustomers.network.okhttp.bean.OkTag;
import com.orientdata.lookforcustomers.network.okhttp.bean.RequestParam;
import com.orientdata.lookforcustomers.network.okhttp.callback.IResponseCallback;
import com.orientdata.lookforcustomers.network.okhttp.progress.ProgressListener;

/**
 * Created by Paul on 15/12/8.
 */
public class OkClient {
    private static String TAG = OkClient.class.getSimpleName();
    public static OkTag okTag;
    private OkClient() {
    }

    /**
     * 普通post和get请求
     *
     * @param param    参数封装，不能为空
     * @param cls      返回的数据封装的类型，如果为null，则返回String
     * @param callback 回调接口，每一个回调接口与tag绑定
     */
    public static void request(int type, RequestParam param, Class<?> cls, IResponseCallback callback) {
        okTag= new OkTag(param.getTag());
        NetManager.getInstance().request(type,okTag, param, cls, callback, null);
    }

    /**
     * 普通post和get请求
     *
     * @param param    参数封装，不能为空
     * @param cls      返回的数据封装的类型，如果为null，则返回String
     * @param callback 回调接口，每一个回调接口与tag绑定
     */
    public static void upload(int type,RequestParam param, Class<?> cls, IResponseCallback callback) {
        okTag= new OkTag(param.getTag());
        NetManager.getInstance().request(type,okTag, param, cls, callback, null);
    }

    /**
     * 文件上传
     *
     * @param param    参数封装，不能为空
     * @param cls      返回的数据封装的类型，如果为null，则返回String
     * @param callback 回调接口，每一个回调接口与tag绑定
     * @param listener 上传进度监听  可为空
     */
    public static void upload(int type,RequestParam param, Class<?> cls, IResponseCallback callback, ProgressListener listener) {
        okTag= new OkTag(param.getTag());
        NetManager.getInstance().request(type,okTag, param, cls, callback, listener);
    }

    /**
     * 文件下载
     *
     * @param param    参数封装，不能为空
     * @param cls      返回的数据封装的类型，如果为null，则返回String
     * @param callback 回调接口，每一个回调接口与tag绑定
     */
    public static void download(int type,RequestParam param, Class<?> cls, IResponseCallback callback) {
        OkTag OkTag = new OkTag(param.getTag());
        NetManager.getInstance().request(type,OkTag, param, cls, callback, null);
    }

    /**
     * 文件下载
     * @param type      下载的类型
     * @param param    参数封装，不能为空
     * @param cls      返回的数据封装的类型，如果为null，则返回String
     * @param callback 回调接口，每一个回调接口与tag绑定
     * @param listener 下载进度监听
     */
    public static void download(int type,RequestParam param, Class<?> cls, IResponseCallback callback, ProgressListener listener) {
        okTag = new OkTag(param.getTag());
        NetManager.getInstance().request(type,okTag, param, cls, callback, listener);
    }

    /**
     * 取消请求
     *
     * @param tag
     */
    public static void cancelRequest(int... tag) {
        NetManager.getInstance().cancelRequest(tag);
    }

    public static void cancelAllRequest() {
        NetManager.getInstance().cancelAllRequest();

    }
}
