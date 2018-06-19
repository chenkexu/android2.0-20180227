package com.orientdata.lookforcustomers.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orientdata.lookforcustomers.bean.CertificationBean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * SharedPreference工具类
 */
public class SharedPreferencesTool {

    private static SharedPreferences sp;


    public static final String  CERTIFICATE_KEY ="certificate_enterprise";//存储企业认证信息的
    public static final String CERTIFICATE_KEY_PER ="certificate_per";//存储个人认证信息的
    private SharedPreferences.Editor editor;
    private static SharedPreferencesTool INSTANCE = null;
    private static final String PREFERENCES_FILE_NAME = "config";
    public static final String ENTERPRISE_INDUSTRY = "enterprise_industry";//存储获取企业一级行业
    public static final String SEARCH_HISTORY="search_history";//业务选择搜索记录
    public static final String DIRECTION_HISTORY="direction_set_history";//缓存的定向设置
    public static final String CERTIFICATE_STATUS="certificate_status";//认证状态
    public static final String USER_STATUS="user_status";//认证状态




    /**是否是第一次进入**/    //存储在SharedPreferences
    public static final String ISFIRSTENTER="isfirstenter";
    public static final String USER_LOGOUT="user_logout";//退出登录之后，再次进来app，需要先进入登录界面）
    public static final String AREA_KEY ="Area";//存储获取的省市信息

    public static final String MessageTaskCacheBean="MessageTaskCacheBean";//保存的短信的信息


    public static final String user ="user";//保存的短信的信息




    private Context context;



    public static final synchronized SharedPreferencesTool getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SharedPreferencesTool();
        }
        return INSTANCE;
    }
    public void init(Context context) {
        this.context = context;
        this.sp = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_MULTI_PROCESS);
        this.editor = sp.edit();
    }
    /**
     * Push string value
     *
     * @param key
     * @param value
     */
    public void putStringValue(String key, String value) {
        editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Get string value by key
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getStringValue(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    /**
     * Push integer value
     *
     * @param key
     * @param value
     */
    public void putIntValue(String key, int value) {
        editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * Get integer value by key
     *
     * @param key
     * @param defaultValue
     *
     * @return
     */
    public int getIntValue(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    /**
     * Push long value
     *
     * @param key
     * @param value
     */
    public void putLongValue(String key, long value) {
        editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * Get long value by key
     *
     * @param key
     * @param defaultValue
     *
     * @return
     */
    public long getLongValue(String key, long defaultValue) {
        return sp.getLong(key, defaultValue);
    }



    /**
     *
     * @param key
     * @return
     */
    public boolean contains(String key) {
        return sp.contains(key);
    }

    /**
     *
     * @param key
     */
    public void remove(String key) {
        editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     *
     */
    public void clear() {
        editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * Get boolean value by key
     *
     * @param key
     * @param defaultValue
     *
     * @return
     */
    public boolean getBooleanValue(String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }


    /**
     * 保存boolean值信息
     */
    public void putBoolean(String key, boolean value) {
        editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();

    }


    public  boolean setObject(Object object, String key) {
        editor = sp.edit();

        if (object == null) {
            editor.remove(key);
            return editor.commit();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        // 将对象放到OutputStream中
        // 将对象转换成byte数组，并将其进行base64编码
        String objectStr = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
        try {
            baos.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 将编码后的字符串写到base64.xml文件中
        editor.putString(key, objectStr);
        return editor.commit();
    }
    public static Object getObjectFromShare(String key) {
        try {
            String wordBase64 = sp.getString(key, "");
            // 将base64格式字符串还原成byte数组
            if (wordBase64 == null || wordBase64.equals("")) {
                return null;
            }
            byte[] objBytes = Base64.decode(wordBase64.getBytes(), Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(objBytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            // 将byte数组转换成product对象
            Object obj = ois.readObject();
            bais.close();
            ois.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 保存List
     * @param tag
     * @param datalist
     */
    public <T> void setDataList(String tag, List<T> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.clear();
        editor.putString(tag, strJson);
        editor.commit();
    }

    /**
     * 获取List
     * @param tag
     * @return
     */
    public <T> List<T> getDataList(String tag) {
        List<T> datalist=new ArrayList<T>();
        String strJson = sp.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<T>>() {}.getType());
        return datalist;
    }


    public List<CertificationBean> getEnterCertificationList(String tag) {


        List<CertificationBean> datalist = new ArrayList<>();
        String strJson = sp.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        JSONArray jsonArray = JSONArray.parseArray(strJson);
        Iterator<Object> it = jsonArray.iterator();
        while (it.hasNext()) {
            JSONObject ob = (JSONObject) it.next();
            CertificationBean model = null;
            if (ob.getInteger("userId") != null) {
                model = new CertificationBean();
                model.setUserId(ob.getInteger("userId"));
                if(ob.getInteger("type")!=null){
                    model.setType(ob.getInteger("type"));
                }
                if(ob.getInteger("tradeTwoId")!=null){
                    model.setTradeTwoId(ob.getInteger("tradeTwoId"));
                }
                if(ob.getInteger("tradeOneId")!=null){
                    model.setTradeOneId(ob.getInteger("tradeOneId"));
                }
                if(ob.getInteger("provincePosition")!=null){
                    model.setProvincePosition(ob.getInteger("provincePosition"));
                }
                if(ob.getInteger("cityPosition")!=null){
                    model.setCityPosition(ob.getInteger("cityPosition"));
                }
                if(ob.getString("provinceCode")!=null){
                    model.setProvinceCode(ob.getString("provinceCode"));
                }
                if(ob.getString("province")!=null){
                    model.setProvince(ob.getString("province"));
                }
                if(ob.getString("name")!=null){
                    model.setName(ob.getString("name"));
                }
                if(ob.getString("contactPhone")!=null){
                    model.setContactPhone(ob.getString("contactPhone"));
                }
                if(ob.getString("contactPerson")!=null){
                    model.setContactPerson(ob.getString("contactPerson"));
                }
                if(ob.getString("contactCard")!=null){
                    model.setContactCard(ob.getString("contactCard"));
                }
                if(ob.getString("cityCode")!=null){
                    model.setCityCode(ob.getString("cityCode"));
                }
                if(ob.getString("city")!=null){
                    model.setCity(ob.getString("city"));
                }
                if(ob.getString("businessLicenseNo")!=null){
                    model.setBusinessLicenseNo(ob.getString("businessLicenseNo"));
                }
                if(ob.getString("address")!=null){
                    model.setAddress(ob.getString("address"));
                }
                datalist.add(model);
            }



        }
        return datalist;
    }

}
