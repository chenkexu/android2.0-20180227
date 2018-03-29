package vr.md.com.mdlibrary.okhttp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.$Gson$Types;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.FileNameMap;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import vr.md.com.mdlibrary.MyApp;
import vr.md.com.mdlibrary.okhttp.requestMap.MDSecureRequestMap;


/**
 * Created by Mr.Z on 16/3/17.
 */
public class OkHttpClientManager {
    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Gson mGson;

    private static final String TAG = "OkHttpClientManager";

    private OkHttpClientManager() {
        mOkHttpClient = new OkHttpClient();

        /**
         * 设置连接的超时时间
         */
        mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        /**
         * 设置响应的超时时间
         */
        mOkHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);
        /**
         * 请求的超时时间
         */
        mOkHttpClient.setReadTimeout(30, TimeUnit.SECONDS);


//        OkHttpClient httpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LogInterceptor())
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(10, TimeUnit.SECONDS)
//                .writeTimeout(10, TimeUnit.SECONDS)
//                .retryOnConnectionFailure(false)
//                .build();


        //cookie enabled
        ProxySelector proxySelector = new ProxySelector() {
            @Override
            public List<Proxy> select(URI uri) {
                List<Proxy> list = new ArrayList<>();
                list.add(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.6.42", 8888)));
                return list;
            }

            @Override
            public void connectFailed(URI uri, SocketAddress address, IOException failure) {

            }
        };
//        ProxySelector.setDefault(proxySelector);
//        mOkHttpClient.setProxySelector(ProxySelector.getDefault());
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mDelivery = new Handler(Looper.getMainLooper());
        //mGson = new Gson();
        mGson=  new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    public static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 同步的Get请求
     *
     * @param url
     * @return Response
     */
    private Response _getAsyn(String url) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        Response execute = call.execute();
        return execute;
    }


    /**
     * 异步的get请求
     *
     * @param url
     * @param callback
     */
    private void _getAsyn(String url, final ResultCallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        deliveryResult(callback, request);
    }


    /**
     * 同步的Post请求
     *
     * @param url
     * @param params post的参数
     * @return
     */
    private Response _post(String url, Param... params) throws IOException {
        Request request = buildPostRequest(url, params);
        Response response = mOkHttpClient.newCall(request).execute();
        return response;
    }


    /**
     * 同步的Post请求
     *
     * @param url
     * @param params post的参数
     * @return 字符串
     */
    private String _postAsString(String url, Param... params) throws IOException {
        Response response = _post(url, params);
        return response.body().string();
    }

    /**
     * 同步的Post请求
     *
     * @param url
     * @param params post的参数
     * @return
     */
    public String _postAsResponse(String url, Map<String, String> params) throws IOException {
        if (params == null)
            return null;
        MDSecureRequestMap mdSecureRequestMap = new MDSecureRequestMap(params);
        if (checkNeedLogin(mdSecureRequestMap)) {
            int size = mdSecureRequestMap.size();
            Param[] res = new Param[size];
            Set<Map.Entry<String, String>> entries = mdSecureRequestMap.entrySet();
            int i = 0;
            for (Map.Entry<String, String> entry : entries) {
                res[i++] = new Param(entry.getKey(), entry.getValue());
            }
            Request request = buildPostRequest(url, res);
            Response response = mOkHttpClient.newCall(request).execute();
            return response.body().string();
        }
        return null;
    }

    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param params
     */
    private void _postAsyn(String url, final ResultCallback callback, Param... params) {
        Request request = buildPostRequest(url, params);
        deliveryResult(callback, request);
    }

    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param params
     */
    private void _postAsyn(String url, final ResultCallback callback, Map<String, String> params) {
        if (params == null)
            return;
        MDSecureRequestMap mdSecureRequestMap = new MDSecureRequestMap(params);
        if (checkNeedLogin(mdSecureRequestMap)) {
            Logger.d("url地址是: "+url);
            int size = mdSecureRequestMap.size();
            Param[] res = new Param[size];
            Set<Map.Entry<String, String>> entries = mdSecureRequestMap.entrySet();
            int i = 0;
            for (Map.Entry<String, String> entry : entries) {
                res[i++] = new Param(entry.getKey(), entry.getValue());
            }
            Logger.d(res);
            Request request = buildPostRequest(url, res);
            deliveryResult(callback, request);
        }

    }

    /**
     * 同步基于post的文件上传
     *
     * @param params
     * @return
     */
    private Response _post(String url, File[] files, String[] fileKeys, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
        return mOkHttpClient.newCall(request).execute();
    }

    private Response _post(String url, File file, String fileKey) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, null);
        return mOkHttpClient.newCall(request).execute();
    }

    private Response _post(String url, File file, String fileKey, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, params);
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 异步基于post的文件上传
     *
     * @param url
     * @param callback
     * @param files
     * @param fileKeys
     * @throws IOException
     */
    private void _postAsyn(String url, ResultCallback callback, File[] files, String[] fileKeys, Param... params) throws IOException {
        MDSecureRequestMap mdSecureRequestMap = new MDSecureRequestMap(new HashMap());
        if (checkNeedLogin(mdSecureRequestMap)) {
            int size = mdSecureRequestMap.size();
            Param[] res = new Param[size +  params.length];
            Set<Map.Entry<String, String>> entries = mdSecureRequestMap.entrySet();
            int i = 0;
            for (Map.Entry<String, String> entry : entries) {
                res[i++] = new Param(entry.getKey(), entry.getValue());
            }
            for(Param param:params){
                res[i++] = param;
            }
            Request request = buildMultipartFormRequest(url, files, fileKeys, res);
            deliveryResult(callback, request);
        }
    }
    private void _postAsyn(String url, ResultCallback callback, File[] files, String[] fileKeys, Map<String,String> params) throws IOException {
        MDSecureRequestMap mdSecureRequestMap = new MDSecureRequestMap(params);
        if (checkNeedLogin(mdSecureRequestMap)) {
            int size = mdSecureRequestMap.size();
            Param[] res = new Param[size];
            Set<Map.Entry<String, String>> entries = mdSecureRequestMap.entrySet();
            int i = 0;
            for (Map.Entry<String, String> entry : entries) {
                res[i++] = new Param(entry.getKey(), entry.getValue());
            }
            Request request = buildMultipartFormRequest(url, files, fileKeys, res);
            deliveryResult(callback, request);
        }
    }
    private void _postAsyn(String url, ResultCallback callback, File[] files, String fileKeys, Map<String,String> params) throws IOException {
        MDSecureRequestMap mdSecureRequestMap = new MDSecureRequestMap(params);
        if (checkNeedLogin(mdSecureRequestMap)) {
            int size = mdSecureRequestMap.size();
            Param[] res = new Param[size];
            Set<Map.Entry<String, String>> entries = mdSecureRequestMap.entrySet();
            int i = 0;
            for (Map.Entry<String, String> entry : entries) {
                res[i++] = new Param(entry.getKey(), entry.getValue());
            }
            Request request = buildMultipartFormRequest(url, files, fileKeys, res);
            deliveryResult(callback, request);
        }
    }

    /**
     * 异步基于post的文件上传，单文件不带参数上传
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     * @throws IOException
     */
    private void _postAsyn(String url, ResultCallback callback, File file, String fileKey) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, null);
        deliveryResult(callback, request);
    }

    /**
     * 异步基于post的文件上传，单文件且携带其他form参数上传
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     * @param map
     * @throws IOException
     */
    private void _postAsyn(String url, ResultCallback callback, File file, String fileKey, Map<String, String> map) throws IOException {
        MDSecureRequestMap mdSecureRequestMap = new MDSecureRequestMap(map);
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[mdSecureRequestMap.size()];
        int i = 0;
        for (Object key : mdSecureRequestMap.keySet()) {
            params[i] = new OkHttpClientManager.Param(key + "", mdSecureRequestMap.get(key) + "");
            i++;
        }

        if (checkNeedLogin(mdSecureRequestMap)) {
            Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, params);
            deliveryResult(callback, request);
        }

    }

    /**
     * 异步下载文件
     *
     * @param url
     * @param destFileDir 本地文件存储的文件夹
     * @param callback
     */
    private void _downloadAsyn(final String url, final String destFileDir, final ResultCallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(Response response) {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File file = new File(destFileDir, getFileName(url));
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    //如果下载文件成功，第一个参数为文件的绝对路径
                    sendSuccessResultCallback(file.getAbsolutePath(), callback);
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
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

    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    /**
     * 加载图片
     *
     * @param view
     * @param url
     * @throws IOException
     */
    private void _displayImage(final ImageView view, final String url, final int errorResId) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                setErrorResId(view, errorResId);
            }

            @Override
            public void onResponse(Response response) {
                InputStream is = null;
                try {
                    is = response.body().byteStream();
                    ImageUtils.ImageSize actualImageSize = ImageUtils.getImageSize(is);
                    ImageUtils.ImageSize imageViewSize = ImageUtils.getImageViewSize(view);
                    int inSampleSize = ImageUtils.calculateInSampleSize(actualImageSize, imageViewSize);
                    try {
                        is.reset();
                    } catch (IOException e) {
                        response = _getAsyn(url);
                        is = response.body().byteStream();
                    }

                    BitmapFactory.Options ops = new BitmapFactory.Options();
                    ops.inJustDecodeBounds = false;
                    ops.inSampleSize = inSampleSize;
                    final Bitmap bm = BitmapFactory.decodeStream(is, null, ops);
                    mDelivery.post(new Runnable() {
                        @Override
                        public void run() {
                            view.setImageBitmap(bm);
                        }
                    });
                } catch (Exception e) {
                    setErrorResId(view, errorResId);

                } finally {
                    if (is != null) try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    private void setErrorResId(final ImageView view, final int errorResId) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                view.setImageResource(errorResId);
            }
        });
    }


    //*************对外公布的方法************


    public static Response getAsyn(String url) throws IOException {
        return getInstance()._getAsyn(url);
    }


    public static void getAsyn(String url, ResultCallback callback) {
        getInstance()._getAsyn(url, callback);
    }
    public static void getAsyn(String url, final ResultCallback callback, Map<String, String> params) {
        getInstance()._getAsyn(url, callback, params);
    }

    public static Response post(String url, Param... params) throws IOException {
        return getInstance()._post(url, params);
    }

    public static String postAsString(String url, Param... params) throws IOException {
        return getInstance()._postAsString(url, params);
    }

    public static void postAsyn(String url, final ResultCallback callback, Param... params) {
        getInstance()._postAsyn(url, callback, params);
    }


    public static void postAsyn(String url, final ResultCallback callback, Map<String, String> params) {
        getInstance()._postAsyn(url, callback, params);
    }


    public static Response post(String url, File[] files, String[] fileKeys, Param... params) throws IOException {
        return getInstance()._post(url, files, fileKeys, params);
    }

    public static Response post(String url, File file, String fileKey) throws IOException {
        return getInstance()._post(url, file, fileKey);
    }

    public static Response post(String url, File file, String fileKey, Param... params) throws IOException {
        return getInstance()._post(url, file, fileKey, params);
    }

    public static void postAsyn(String url, ResultCallback callback, File[] files, String[] fileKeys, Param... params) throws IOException {
        getInstance()._postAsyn(url, callback, files, fileKeys, params);
    }
    public static void postAsyn(String url, ResultCallback callback, File[] files, String[] fileKeys, Map<String, String> params) throws IOException {
        getInstance()._postAsyn(url, callback, files, fileKeys, params);
    }



    //图片上传
    public static void postAsyn(String url, ResultCallback callback, File[] files, String fileKeys, Map<String, String> params) throws IOException {
        getInstance()._postAsyn(url, callback, files, fileKeys, params);
    }


    public static void postAsyn(String url, ResultCallback callback, File file, String fileKey) throws IOException {
        getInstance()._postAsyn(url, callback, file, fileKey);
    }
    private void _getAsyn(String url, final ResultCallback callback, Map<String, String> params) {
        if (params == null)
            return;
        MDSecureRequestMap mdSecureRequestMap = new MDSecureRequestMap(params);
        if (checkNeedLogin(mdSecureRequestMap)) {
            int size = mdSecureRequestMap.size();
            Param[] res = new Param[size];
            Set<Map.Entry<String, String>> entries = mdSecureRequestMap.entrySet();
            int i = 0;
            for (Map.Entry<String, String> entry : entries) {
                res[i++] = new Param(entry.getKey(), entry.getValue());
            }
            Request request = buildGetRequest(url, res);
            deliveryResult(callback, request);
        }
    }
    private Request buildGetRequest(String url, Param[] params) {
        if (params == null) {
            params = new Param[0];
        }
        StringBuilder tempParams = new StringBuilder();
        Request request = null;
        try {
            //处理参数
            int pos = 0;
            for (Param temp : params) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                //对参数进行URLEncoder
                tempParams.append(String.format("%s=%s", params[pos].key, URLEncoder.encode(params[pos].value, "utf-8")));
                pos++;
            }
            //补全请求地址
            String requestUrl = String.format("%s?%s", url, tempParams.toString());
            //创建一个请求
            request = new Request.Builder().url(requestUrl).get().build();
            Log.e("==","requestUrl = "+requestUrl);
            Logger.d("==","requestUrl = "+requestUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }


    /**
     * 上传文件
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     * @param map
     * @throws IOException
     */
    public static void postAsyn(String url, ResultCallback callback, File file, String fileKey, Map<String, String> map) throws IOException {
        getInstance()._postAsyn(url, callback, file, fileKey, map);
    }

    public static void displayImage(final ImageView view, String url, int errorResId) throws IOException {
        getInstance()._displayImage(view, url, errorResId);
    }


    public static void displayImage(final ImageView view, String url) {
        getInstance()._displayImage(view, url, -1);
    }

    public static void downloadAsyn(String url, String destDir, ResultCallback callback) {
        getInstance()._downloadAsyn(url, destDir, callback);
    }

    //****************************


    //文件上传
    private Request buildMultipartFormRequest(String url, File[] files, String fileKeys, Param[] params) {
        params = validateParam(params);
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""), RequestBody.create(null, param.value));
        }
        if (files != null) {
            RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + fileKeys + "\"; filename=\"" + fileName + "\""), fileBody);
            }
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }
    private Request buildMultipartFormRequest(String url, File[] files, String[] fileKeys, Param[] params) {
        params = validateParam(params);
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""), RequestBody.create(null, param.value));
        }
        if (files != null) {
            RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }

        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }
    private Request buildMultipartFormRequest2(String url, File[] files,
                                              String[] fileKeys, Param[] params) {
        params = validateParam(params);

        MultipartBuilder builder = new MultipartBuilder()
                .type(MultipartBuilder.FORM);


        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""),
                    RequestBody.create(null, param.value));
        }
        if (files != null) {
            RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
//                builder.addPart(Headers.of("Content-Disposition",
//                        "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
//                        fileBody);
                builder.addPart(Headers.of(
                        "Content-Type","multipart/form-data; boundary=----WebKitFormBoundaryEx86y06jpjRIzUI9",
                        "Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
                        "Content-Disposition", "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


    private Param[] validateParam(Param[] params) {
        if (params == null)
            return new Param[0];
        else return params;
    }

    private Param[] map2Params(Map<String, String> params) {
        if (params == null) return new Param[0];
        MDSecureRequestMap mdSecureRequestMap = new MDSecureRequestMap(params);
        if (checkNeedLogin(mdSecureRequestMap)) {

        }
        int size = mdSecureRequestMap.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = mdSecureRequestMap.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }

        return res;
    }

    private static final String SESSION_KEY = "Set-Cookie";
    private static final String mSessionKey = "JSESSIONID";

    private Map<String, String> mSessions = new HashMap<String, String>();

    private void deliveryResult(final ResultCallback callback, final Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailedStringCallback(request, e, callback);
                startError(102, "");
            }

            @Override
            public void onResponse(final Response response) {
                try {
                    final String string = response.body().string();
                    Log.d("请求返回参数",string);
                    Logger.d("请求返回参数:"+string);
                    if (callback.mType == String.class) {
                        sendSuccessResultCallback(string, callback);
                    } else {
                        if (analyzeResponse(string)) {
                            if(getResult(string,request.urlString()) == 1){
                                sendSuccessResultCallback(null, callback);
                            }else{
                                Object o = mGson.fromJson(string, callback.mType);
                                sendSuccessResultCallback(o, callback);
                            }
                        } else {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(string);
                                JSONObject error = jsonObject.getJSONObject("err");
                                if (error != null) {
                                    int error_code = error.getInt("code");
                                    String msg = error.getString("msg");
                                    RuntimeException runtimeException = new RuntimeException(msg);
                                    sendFailedStringCallback(response.request(), runtimeException, callback);
                                    if(error_code == -100){
                                        //重新登陆
                                        startError(101, msg);
                                    }
                                }


                            } catch (Exception e) {
                                sendFailedStringCallback(null, e, callback);
                            }
                        }
                    }


                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                } catch (com.google.gson.JsonParseException e)//Json解析的错误
                {
                    sendFailedStringCallback(response.request(), e, callback);
                }
            }
        });
    }

    /**
     * 查询个人认证接口 result 有两种情况的返回值 单独处理
     * result是
     * int/CertificationOut	用户认证信息 ,备注 如果result中只 有一个数字1 代表 未认证
     * @return
     */
    private int getResult(String str,String url){
        int result = 0;
        if(!TextUtils.isEmpty(url) && url.contains("app/user/selectCertificate")){
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(str);
                result = jsonObject.optInt("result");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }
        return result;
    }

    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onError(e);
            }
        });
    }

    private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponse(object);
                }
            }
        });
    }


    private Request buildPostRequest(String url, Param[] params) {
        if (params == null) {
            params = new Param[0];
        }
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (Param param : params) {
            builder.add(param.key + "", param.value + "");
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }


    public static abstract class ResultCallback<T> {
        Type mType;

        public ResultCallback() {
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public abstract void onError(Exception e);

        public abstract void onResponse(T response);
    }


    public static class Param {
        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

        String key;
        String value;
    }

    /**
     * 解析访问网络的返回参数
     */
    private boolean analyzeResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject error = jsonObject.getJSONObject("err");
            if (error != null) {
                int error_code = error.getInt("code");
                String msg = error.getString("msg");
                if (error_code == 0) {
                    return true;
                } else {
//                    startError(error_code, msg);
                    return false;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 开启登录页面
     */
    private void startError(int code, String msg) {
        Intent intent = new Intent("com.microdreams.timeprints.error");
        intent.putExtra("error_code", code);
        intent.putExtra("msg", msg);
        MyApp.getContext().sendBroadcast(intent);
    }

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
     * json 请求
     *
     * @param url
     * @param callback
     */
    public void _postJsonWithUrlEncodedType(String url, RequestBody body, ResultCallback callback) {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        deliveryResult(callback, request);
    }
}

