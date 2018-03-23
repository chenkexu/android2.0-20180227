package com.orientdata.lookforcustomers.network.okhttp.manager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.orientdata.lookforcustomers.network.okhttp.bean.OkError;
import com.orientdata.lookforcustomers.network.okhttp.bean.OkResult;
import com.orientdata.lookforcustomers.network.okhttp.bean.OkTag;
import com.orientdata.lookforcustomers.network.okhttp.bean.RequestParam;
import com.orientdata.lookforcustomers.network.okhttp.callback.IResponseCallback;
import com.orientdata.lookforcustomers.network.okhttp.progress.ProgressListener;
import com.orientdata.lookforcustomers.network.okhttp.progress.ProgressRequestBody;
import com.orientdata.lookforcustomers.network.util.AppConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 网络管理页面
 */
public class NetManager {
    public static final int GET = 100;
    public static final int POST = 101;
    public static final int PUT = 102;
    public static final int DELETE = 103;
    public static final int DOWNLOAD = 104;
    public static final int UPLOAD = 105;

    private static final String TAG = NetManager.class.getSimpleName();
    private static final int DEFAULT_TIME_OUT = 15000;
    private static final int CODE_SUCCESS = 0;
    private static final int CODE_FAILED = 1;
    private static final int CODE_TOKEN_ERROR = 2;

    private static NetManager sInstance;
    //handler the result of request
    private OkHttpClient mOkHttpClient;
    private InternalHandler mHandler;

    //request callbacks
    private ConcurrentHashMap<OkTag, IResponseCallback> mCallbacks = new ConcurrentHashMap<>();
    //request calls
    private ConcurrentHashMap<OkTag, Call> mAsyncCalls = new ConcurrentHashMap<>();

    private NetManager() {
        if (mOkHttpClient == null) {
            synchronized (NetManager.class) {
                mOkHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS)
                        .writeTimeout(DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS)
                        .readTimeout(DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS)
                        .build();
            }
        }
    }

    public static NetManager getInstance() {
        if (null == sInstance) {
            synchronized (NetManager.class) {
                sInstance = new NetManager();
            }
        }
        return sInstance;
    }

    /**
     * @param tag
     * @param requestParam
     * @param cls
     * @param callback
     */
    public void request(int type, final OkTag tag, final RequestParam requestParam, final Class<?> cls, final IResponseCallback callback, final ProgressListener progressListener) {
        addCallback(tag, callback);
        if (requestParam == null) {
            //almost tell the developer that he/she forget the RequestBean
            sendFailedMessage(tag, getOkError("参数错误"));
            return;
        }
//        if (NetUtil.isNetAvailable()) {
            try {
                switch (requestParam.getMethod()) {
                    case GET:
                        doGet(tag, requestParam.getUrl(), requestParam.getHeaders(),
                                requestParam.getParams(), cls);
                        break;
                    case POST:
                        doPost(tag, requestParam.getUrl(), requestParam.getHeaders(),
                                requestParam.getParams(), cls);
                        break;
                    case DOWNLOAD:
                        doDownload(type,tag, requestParam.getUrl(), callback, progressListener);
                        break;
                    case UPLOAD:
                        doUpload(tag, requestParam.getUrl(), requestParam.getParams(),
                                requestParam.getFiles(), cls, callback, progressListener);

                        break;
                }
            } catch (Exception e) {
                //unexpected error
                e.printStackTrace();
                sendFailedMessage(tag, new OkError("请求失败"));
            }
//        } else {
//            sendFailedMessage(tag, new OkError("当前网络已断开,请检查网络后重试"));
//        }
    }


    /**
     * get request
     *
     * @param tag
     * @param url
     * @param headers
     * @param params
     * @param cls
     * @throws Exception
     */
    private void doGet(final OkTag tag, String url, final Map<String, String> headers,
                       final Map<String, String> params, final Class<?> cls) throws Exception {
        String requestUrl = null;
        // 如果是GET请求，则请求参数在URL中
        if (params != null && !params.isEmpty() && params.size() != 0) {
            String param = urlEncode(params);
            requestUrl = url + "?" + param;
        } else {
            requestUrl = url;
        }
        Request.Builder requestBuilder = new Request.Builder();
        if (null != headers && !headers.isEmpty() && headers.size() != 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    requestBuilder.header(entry.getKey(), entry.getValue());
                }
            }
        }
        Request request = requestBuilder
                .url(requestUrl)
                .build();
        deliveryRequest(tag, request, cls);
    }

    /**
     * post request
     *
     * @param tag
     * @param url
     * @param headers
     * @param params
     * @param cls
     */
    private void doPost(final OkTag tag, String url, final Map<String, String> headers,
                        final Map<String, String> params, final Class<?> cls) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && !params.isEmpty() && params.size() != 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    builder.add(entry.getKey(), entry.getValue());
                }
            }
        }
        RequestBody formBody = builder.build();
        //build request header
        Request.Builder requestBuilder = new Request.Builder();
        if (null != headers && !headers.isEmpty() && headers.size() != 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    requestBuilder.header(entry.getKey(), entry.getValue());
                }
            }
        }
        Request request = requestBuilder
                .url(url)
                .post(formBody)
                .build();
        deliveryRequest(tag, request, cls);
    }

    //fileupload
    private void doUpload(final OkTag tag, String url, Map<String, String> params, Map<String, File> files, final Class<?> cls, IResponseCallback callback, ProgressListener progressListener) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        if (params != null && !params.isEmpty() && params.size() != 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
                            RequestBody.create(null, entry.getValue()));
                }
            }
        }
        if (files != null && !files.isEmpty() && files.size() != 0) {
            for (Map.Entry<String, File> entry : files.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    RequestBody fileBody = null;
                    fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), entry.getValue());
                    builder.addPart(Headers.of("Content-Disposition",
                                    "form-data; name=\"" + entry.getKey() + "\"; filename=\"" + entry.getValue().getName() + "\""),
                            fileBody);
                }
            }
        }

        RequestBody requestBody = builder.build();
        Request request;

        if (progressListener != null) {//带进度条
            request = new Request.Builder()
                    .url(url)
                    .post(new ProgressRequestBody(requestBody, progressListener))
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
        }
        deliveryRequest(tag, request, cls);
    }

    /**
     * 下载
     *
     * @param tag
     * @param callback
     * @param progressListener
     */
    private void doDownload(final int type, final OkTag tag, final String url, final IResponseCallback callback, final ProgressListener progressListener) {
        Request request = new Request.Builder()
                .url(url)
                .build();
//        OkHttpClient client = new OkHttpClient.Builder()
//                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS)
//                .writeTimeout(DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS)
//                .readTimeout(DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS)
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        //拦截
//                        Response originalResponse = chain.proceed(chain.request());
//                        //包装响应体并返回
//                        return originalResponse.newBuilder()
//                                .body(new ProgressResponseBody(originalResponse.body(), progressListener))
//                                .build();
//                    }
//                })
//                .build();
        Call call = mOkHttpClient.newCall(request);
        addCall(tag, call);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                OkError OkError = getOkError("服务器连接异常，请稍后再试");
                sendFailedMessage(tag, OkError);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                File file = new File(AppConfig.getLocalProductDownloadPath(type), getFileName(url));
                //此处可以增加判断文件是否存在的逻辑

                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    long contentLen = response.body().contentLength();
                    long downloadLen = 0;
                    fos = new FileOutputStream(file);
                    long currentPercent = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        downloadLen += len;
                        if (progressListener != null) {

                            if (downloadLen * 100 / contentLen - currentPercent >= 1) {
                                currentPercent = downloadLen * 100 / contentLen;
                                progressListener.onProgress(downloadLen, contentLen, currentPercent);
                            }

                        }
                    }
                    fos.flush();
                    sendSuccessMessage(tag, file);

                } catch (Exception e) {
                    e.printStackTrace();
                    sendFailedMessage(tag, getOkError("下载失败"));
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    /**
     * 处理post get upload返回结果  download单独处理
     *
     * @param tag
     * @param request
     * @param cls
     */
    private void deliveryRequest(final OkTag tag, final Request request, final Class<?> cls) {

        Call call = mOkHttpClient.newCall(request);
        addCall(tag, call);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                OkError OkError = getOkError("服务器连接异常，请稍后再试");
                sendFailedMessage(tag, OkError);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    String strResult = response.body().string();
                    Object result;
                    if (cls != null) {
//                        Gson gson = new Gson();
//                        result = gson.fromJson(strResult, cls);
                    } else { //没有指定类型，直接返回string
                        result = strResult;
                    }
//                    if (result == null) {
//                        sendFailedMessage(tag, getOkError("数据为空"));
//                    } else {
//                        sendSuccessMessage(tag, result);
//                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    sendFailedMessage(tag, getOkError("解析错误"));
                }


            }
        });
    }

    /**
     * 获得文件名
     *
     * @param path
     * @return
     */
    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    /**
     * package params
     *
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    private String urlEncode(Map<String, String> params)
            throws UnsupportedEncodingException {
        StringBuffer sb2 = new StringBuffer();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                sb2.append(entry.getKey());
                sb2.append("=");
                sb2.append(URLEncoder.encode(entry.getValue(), "utf-8").toString());
                sb2.append("&");
            }
        }
        String s = "";
        if (sb2.length() != 0) {
            s = sb2.substring(0, sb2.length() - 1);
        }
        return s;
    }

    /**
     * the IResponseCallback matches the specified tag
     *
     * @param iResponseCallback
     * @param tag
     * @return void
     * @throw
     */
    private void addCallback(OkTag tag, IResponseCallback iResponseCallback) {
        if (iResponseCallback == null) {
            mCallbacks.put(tag, new IResponseCallback() {
                @Override
                public void onSuccess(int tag, Object object) {

                }

                @Override
                public void onError(int tag, OkError object) {

                }

            });
            return;
        }
        mCallbacks.put(tag, iResponseCallback);

    }

    private void addCall(OkTag tag, Call call) {
        mAsyncCalls.put(tag, call);
    }

    private Call getAndRemoveCall(OkTag tag) {
        if (mAsyncCalls != null && mAsyncCalls.size() != 0 && mAsyncCalls.containsKey(tag)) {
            Call call = mAsyncCalls.get(tag);
            mAsyncCalls.remove(tag);
            return call;
        }
        return null;
    }

    /**
     * get and remove IResponseCallback threw the specified tag
     *
     * @param tag
     * @return IResponseCallback
     * @throw
     */
    private IResponseCallback getAndRemoveCallback(OkTag tag) {

        if (mCallbacks != null && mCallbacks.size() != 0 && mCallbacks.containsKey(tag)) {
            IResponseCallback iResponseCallback = mCallbacks.get(tag);
            mCallbacks.remove(tag);
            return iResponseCallback;
        }
        return null;
    }

    /**
     * @param tag
     * @return void
     * @throw
     */
    private void removeCallback(OkTag tag) {
        if (mCallbacks.containsKey(tag)) {
            mCallbacks.remove(tag);
        }
    }

    private void removeCall(OkTag tag) {
        if (mAsyncCalls.containsKey(tag)) {
            mAsyncCalls.remove(tag);
        }
    }

    /**
     * sendFailedMessage
     *
     * @param tag
     * @param object
     */
    private void sendSuccessMessage(OkTag tag, Object object) {
        //remove call while you request success
        getAndRemoveCall(tag);
        IResponseCallback iResponseCallback = getAndRemoveCallback(tag);
        if (iResponseCallback != null) {
            Message message = getHandler().obtainMessage();
            OkResult handlerBean = new OkResult(tag, object, iResponseCallback);
            message.obj = handlerBean;
            message.arg1 = CODE_SUCCESS;
            message.sendToTarget();
        }
    }

    /**
     * sendSuccessMessage
     *
     * @param tag
     * @param OkError
     */
    private void sendFailedMessage(OkTag tag, OkError OkError) {
        //remove call while you request failed
        getAndRemoveCall(tag);
        IResponseCallback iResponseCallback = getAndRemoveCallback(tag);
        if (iResponseCallback != null) {
            OkResult handlerBean = new OkResult(tag, OkError, iResponseCallback);
            Message message = getHandler().obtainMessage();
            message.obj = handlerBean;
            message.arg1 = CODE_FAILED;
            message.sendToTarget();
        }
    }


    private Handler getHandler() {
        synchronized (NetManager.class) {
            if (mHandler == null) {
                mHandler = new InternalHandler();
            }
            return mHandler;
        }
    }


//    private Gson getGson() {
//        if (mGson == null) {
//            synchronized (NetManager.class) {
//                GsonBuilder builder = new GsonBuilder();
//                builder.registerTypeAdapter(JsonInteger.class, new JsonIntegerTypeAdapter());
//                builder.registerTypeAdapter(JsonFloat.class, new JsonFloatTypeAdapter());
//                builder.registerTypeAdapter(JsonLong.class, new JsonLongTypeAdapter());
//                mGson = builder.create();
//            }
//        }
//        return mGson;
//    }

    private OkError getOkError(String msg) {
        OkError OkError = new OkError("请求失败");
        return OkError;
    }

    /**
     * check weather tag has removed
     * <p>if true exist  else removedx
     *
     * @param tag
     * @return
     */
    private boolean checkTag(OkTag tag) {
        if (mCallbacks != null && mCallbacks.size() != 0) {
            if (mCallbacks.containsKey(tag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * cancel request
     * <p> if calls contain the request ,remove it
     *
     * @param tags
     */
    public void cancelRequest(int... tags) {
        if (tags != null && tags.length != 0) {
            List<OkTag> cancelCalls = new ArrayList<>();
            List<OkTag> cancelCallbacks = new ArrayList<>();
            for (int i = 0; i < tags.length; i++) {
                int tag = tags[i];
                if (mAsyncCalls != null && mAsyncCalls.size() != 0) {
                    for (Map.Entry<OkTag, Call> entry : mAsyncCalls.entrySet()) {
                        if (entry.getKey().getTag() == tag) {
                            cancelCalls.add(entry.getKey());
                        }
                    }
                }
                if (mCallbacks != null && mCallbacks.size() != 0) {
                    for (Map.Entry<OkTag, IResponseCallback> entry : mCallbacks.entrySet()) {
                        if (entry.getKey().getTag() == tag) {
                            cancelCallbacks.add(entry.getKey());
                        }
                    }
                }
            }

            for (int i = 0; i < cancelCalls.size(); i++) {
                Call call = mAsyncCalls.get(cancelCalls.get(i));
                call.cancel();
                mAsyncCalls.remove(cancelCalls.get(i));
            }
            for (int i = 0; i < cancelCallbacks.size(); i++) {
                mCallbacks.remove(cancelCallbacks.get(i));
            }
        }
    }

    public void cancelAllRequest() {
        mAsyncCalls.clear();
        mCallbacks.clear();
    }

    //handle the result of the request
    private static class InternalHandler extends Handler {
        public InternalHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            OkResult handlerBean = (OkResult) msg.obj;
            OkTag tag = handlerBean.getTag();
            IResponseCallback iResponseCallback = handlerBean.getResponseCallback();
            switch (msg.arg1) {
                case CODE_SUCCESS:
                    Object object = handlerBean.getObject();
                    if (tag != null) {
                        iResponseCallback.onSuccess(tag.getTag(), object);
                    }
                    break;
                case CODE_FAILED:
                    OkError OkError = (OkError) handlerBean.getObject();
                    if (tag != null) {
                        iResponseCallback.onError(tag.getTag(), OkError);
                    }
                    break;
            }
        }
    }
}
