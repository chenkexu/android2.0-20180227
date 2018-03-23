package com.orientdata.lookforcustomers.util;

import android.widget.Toast;

import com.orientdata.lookforcustomers.app.MyApplication;

/**
 * Toast统一管理类
 */
public class ToastUtils {
    private static Toast mToast;

    private ToastUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

//    public static boolean isShow = true;

    private static void isShowing() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }

    /**
     * 短时间显示Toast
     */
    public static void showShort(CharSequence message) {
        isShowing();
        mToast = Toast.makeText(MyApplication.getContext(), message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * 短时间显示Toast
     */
    public static void showShort(int message) {
        isShowing();
        mToast = Toast.makeText(MyApplication.getContext(), message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * 长时间显示Toast
     */
    public static void showLong(CharSequence message) {
        isShowing();
        mToast = Toast.makeText(MyApplication.getContext(), message, Toast.LENGTH_LONG);
        mToast.show();
    }

    /**
     * 长时间显示Toast
     */
    public static void showLong(int message) {
        isShowing();
        mToast = Toast.makeText(MyApplication.getContext(), message, Toast.LENGTH_LONG);
        mToast.show();
    }

    /**
     * 自定义显示Toast时间
     */
    public static void show(CharSequence message, int duration) {
        isShowing();
        mToast = Toast.makeText(MyApplication.getContext(), message, duration);
        mToast.show();
    }

    /**
     * 自定义显示Toast时间
     */
    public static void show(int message, int duration) {
        isShowing();
        mToast = Toast.makeText(MyApplication.getContext(), message, duration);
        mToast.show();
    }
}