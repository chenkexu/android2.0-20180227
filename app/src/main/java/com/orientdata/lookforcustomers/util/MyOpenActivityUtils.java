package com.orientdata.lookforcustomers.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.orientdata.lookforcustomers.view.home.imple.HomeActivity;
import com.orientdata.lookforcustomers.view.webview.imple.CommonWebView;

/**
 * Created by wy on 2017/10/25.
 */

public class MyOpenActivityUtils {

    /**
     * 跳转到主界面
     */
    public static void openHomeActivity(Activity activity) {
        Intent intent = new Intent(activity, HomeActivity.class);
        activity.startActivity(intent);
    }
    public static void openHomeActivity(Activity activity,boolean isNewUser) {
        Intent intent = new Intent(activity, HomeActivity.class);
        intent.putExtra("isNewUser",isNewUser);
        activity.startActivity(intent);
    }

    /**
     * 打开通用的web页面
     *
     * @param activity
     * @param title
     * @param url
     */
    public static void openCommonWebView(Activity activity
            , String title, String url) {
        Intent intent = new Intent(activity, CommonWebView.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("url", url);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }
}
