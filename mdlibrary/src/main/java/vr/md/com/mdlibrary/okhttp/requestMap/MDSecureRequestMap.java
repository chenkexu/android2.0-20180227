package vr.md.com.mdlibrary.okhttp.requestMap;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import vr.md.com.mdlibrary.AppConfig;
import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.utils.MD5;

/**
 * Created by Mr.Z on 16/3/17.
 */
public class MDSecureRequestMap extends MDBasicRequestMap {
    public static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public MDSecureRequestMap(Map map) {
        super(map);
        this.put("timestamp", getTimestamp());
        this.put("rand", getRand(16));
        this.put("app_id", getApp_id());
        this.put("ver", getVer());
        this.put("plat", getPlat());
        this.put("private_key", this.getPrivate_key());
        if (!TextUtils.isEmpty(UserDataManeger.getInstance().getUserId())) {
            this.put("userId",UserDataManeger.getInstance().getUserId());
        }

        if (!TextUtils.isEmpty(UserDataManeger.getInstance().getUserToken())) {
            this.put("token", UserDataManeger.getInstance().getUserToken());
        }
        this.put("sign", getSignString());
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
        map.putAll(this);
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
