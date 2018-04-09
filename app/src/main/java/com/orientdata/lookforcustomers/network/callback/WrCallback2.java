package com.orientdata.lookforcustomers.network.callback;

import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.base.Request;
import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.bean.WrResponse;
import com.orientdata.lookforcustomers.network.model.Convert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import okhttp3.Response;
import vr.md.com.mdlibrary.AppConfig;
import vr.md.com.mdlibrary.MyApp;
import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.utils.MD5;


/**
 * Created by chenkexu on 2017/11/14.
 * 描述:
 */

public abstract class WrCallback2 extends StringCallback {
    public static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";


    @Override
    public void onStart(Request<String, ? extends Request> request) {
        super.onStart(request);
        request.params("timestamp",getTimestamp());
        request.params("rand", getRand(16));
        request.params("app_id", getApp_id());
        request.params("ver", getVer());
        request.params("plat",getPlat());
        request.params("private_key", getPrivate_key());
        if (!TextUtils.isEmpty(UserDataManeger.getInstance().getUserToken())) {
            request.params("token", UserDataManeger.getInstance().getUserToken());
        }
        request.params("sign", getSignString());
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
     * 开启登录页面
     */
    private void startError(int code, String msg) {
        Intent intent = new Intent("com.microdreams.timeprints.error");
        intent.putExtra("error_code", code);
        intent.putExtra("msg", msg);
        MyApp.getContext().sendBroadcast(intent);
    }

    /**
     * 获取缓存到本地的 app_id
     *
     * @return
     */
    private String getApp_id() {
        String app_id = AppConfig.APP_ID;
        return app_id;
    }

    /**
     * 获取时间戳
     *
     * @return
     */
    private String getTimestamp() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String timestamp = format.format(new Date());
        return timestamp;
    }

    private String getSignString() {
        Map<String, String> map = new TreeMap<>();
//        map.putAll(this);
        map.put("private_key", this.getPrivate_key());
        StringBuilder sb = new StringBuilder();
        for (String o : map.keySet()) {
            if (sb.length() == 0) {
                sb.append(o + "=" + map.get(o));
            } else {
                sb.append("&").append(o + "=" + map.get(o));
            }
        }

        return MD5.md5(sb.toString());
    }

    private String getVer() {
        return AppConfig.VER;
    }

    private String getPrivate_key() {
        return AppConfig.PRIVATE_KEY;
    }

    private String getPlat() {
        return AppConfig.PLAT;
    }

    /**
     * 返回一个定长的随机字符串(只包含大小写字母、数字)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    private String getRand(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        int maxLen = ALLCHAR.length();
        for (int i = 0; i < length; i++) {
            sb.append(ALLCHAR.charAt(random.nextInt(maxLen * 4) % maxLen));
        }
        return sb.toString();
    }


}
