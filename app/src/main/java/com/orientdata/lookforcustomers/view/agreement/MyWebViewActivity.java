package com.orientdata.lookforcustomers.view.agreement;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.WangrunBaseActivity;
import com.orientdata.lookforcustomers.widget.MyTitle;

import java.net.URISyntaxException;

/**
 * Created by wy on 2017/12/20.
 * web页面
 */
public class MyWebViewActivity extends WangrunBaseActivity {
    private WebView webView;
    private String url = "";
    private TextView actionbar_title;
    private String title = "";
    private LinearLayout layout_back;
    private ProgressBar myProgressBar;
    private MyTitle myTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initView();
        updateView();
        initTitle();
    }

    public void initView() {

        myProgressBar = (ProgressBar) findViewById(R.id.myProgressBar);
        webView = (WebView) findViewById(R.id.webView);
        myTitle = (MyTitle) findViewById(R.id.my_title);

        WebSettings webSettings = webView.getSettings();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        //设置自适应屏幕，两者合用
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.getSettings().setUseWideViewPort(true); //将图片调整到适合webview的大小
        webView.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小


    }

    private void initTitle() {
        myTitle.setTitleName(title);
        myTitle.setImageBack(this);
    }

    public void updateView() {
        if (getIntent() != null) {
            url = getIntent().getStringExtra("url");
            title = getIntent().getStringExtra("title");
            if (!TextUtils.isEmpty(url)) {
                webView.loadUrl(url);
            }
            webView.setWebViewClient(webViewClient);
            webView.setWebChromeClient(webChromeClient);
//            webView.setWebChromeClient(new WebChromeClient() {
//                @Override
//                public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
//                    callback.invoke(origin, true, true);
//                    super.onGeolocationPermissionsShowPrompt(origin, callback);
//                }
//            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();//返回上一页面
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("http:") || url.startsWith("https:")) {
                view.loadUrl(url);
                return false;
            } else if (url.contains("map")) {
                Intent intent = null;
                try {
                    intent = Intent.getIntent("intent://map/direction?mode=driving&origin=name:起点|latlng:39.518611,116.703602&destination=name:终点|latlng:31.850000,121.200000&src=webapp.car.carroutelistmappg.drivenavibtn FromType=0xffffffff }");
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
                return true;
            } else {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("==", "" + e.getMessage());
                }
                return true;
            }

//        public void onGeolocationPermissionsShowPrompt(String origin,
//                GeolocationPermissions.Callback callback) {
//                callback.invoke(origin, true, false);
//                super.onGeolocationPermissionsShowPrompt(origin, callback);
//             }
        }

    }


    //===================匿名内部类==========================//
    private WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                myProgressBar.setVisibility(View.INVISIBLE);
            } else {
                if (View.INVISIBLE == myProgressBar.getVisibility()) {
                    myProgressBar.setVisibility(View.VISIBLE);
                }
                myProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    };

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            if (url == null) return false;
//            try {
//                if (url.startsWith("weixin://") || url.startsWith("baidumap://") ||
//                        url.startsWith("mailto://") || url.startsWith("tel://")
//                    //其他自定义的scheme
//                        ) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(intent);
//                    return true;
//                }
//            } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
//                return false;
//            }
//            //处理http和https开头的url
//            view.loadUrl(url);
//            return true;
            if(url.startsWith("tel:")){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    };
}
