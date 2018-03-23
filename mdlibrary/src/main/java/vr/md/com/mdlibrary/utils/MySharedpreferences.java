package vr.md.com.mdlibrary.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import vr.md.com.mdlibrary.MyApp;
import vr.md.com.mdlibrary.UserDataManeger;

/**
 * Created by Mr.Z on 15/12/11.
 */
public class MySharedpreferences {


    /**
     * 存储数据
     *
     * @param key
     * @param value
     */
    public static void putString(String key, String value) {
        SharedPreferences sharedPreferences =
                MyApp.getContext().getSharedPreferences(getName(), Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(String key) {
        SharedPreferences sharedPreferences =
                MyApp.getContext().getSharedPreferences(getName(), Activity.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    /**
     * 向 SharedPreferences 中存入 int 数据
     *
     * @param key
     * @param value
     */
    public static void putFloat(String key, float value) {
        SharedPreferences sharedPreferences =
                MyApp.getContext().getSharedPreferences(getName(), Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     * 从 SharedPreferences 中 获取 int 数据
     *
     * @param key
     * @return
     */
    public static Float getFloat(String key) {
        SharedPreferences sharedPreferences =
                MyApp.getContext().getSharedPreferences(getName(), Activity.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, 0);
    }

    /**
     * 向 SharedPreferences 中存入 Boolean 数据
     *
     * @param key
     * @param value
     */
    public static void putBoolean(String key, boolean value) {

        SharedPreferences sharedPreferences =
                MyApp.getContext().getSharedPreferences(getName(), Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 从 SharedPreferences 中 获取 Boolean 数据
     *
     * @param key
     * @return
     */
    public static boolean getBoolean(String key) {
        SharedPreferences sharedPreferences =
                MyApp.getContext().getSharedPreferences(getName(), Activity.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    /**
     * 向 SharedPreferences 中存入 long 数据
     *
     * @param key
     * @param value
     */
    public static void putLong(String key, long value) {

        SharedPreferences sharedPreferences =
                MyApp.getContext().getSharedPreferences(getName(), Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 从 SharedPreferences 中 获取 long 数据
     *
     * @param key
     * @return
     */
    public static long getLong(String key) {
        SharedPreferences sharedPreferences =
                MyApp.getContext().getSharedPreferences(getName(), Activity.MODE_PRIVATE);
        return sharedPreferences.getLong(key, 0);
    }

    /**
     * 获得文件名字
     *
     * @return
     */
    private static String getName() {

        return "bidchance.md.com.bidchance";
    }

    /**
     * 清空数据
     */
    public static void clearData() {
//        SharedPreferences sharedPreferences =
//                MyApp.getContext().getSharedPreferences(getName(), Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
//        editor.commit();
        MySharedpreferences.putString("TOKEN", "");
    }

    /**
     * 全部删除
     */
    public static void clearData1() {
        SharedPreferences sharedPreferences =
                MyApp.getContext().getSharedPreferences(getName(), Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 保存list
     */
    public static void putListJson(String key, List<String> list) {
        Gson gson = new Gson();
        putString(key, "");
        putString(key, gson.toJson(list));
    }

    /**
     * 获取list
     *
     * @param key
     */
    public static List<String> getListFromJson(String key) {
        Gson gson = new Gson();
        List<String> list = new ArrayList<>();
        String json = getString(key);
        if (!TextUtils.isEmpty(json)) {
            list = gson.fromJson(json,
                    new TypeToken<List<String>>() {
                    }.getType());
        }
        return list;
    }
}
