package com.orientdata.lookforcustomers.view.webview.imple;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.presenter.CommonWebViewPresent;
import com.orientdata.lookforcustomers.view.webview.ICommonWebView;
import com.orientdata.lookforcustomers.widget.CommonTitleBar;

/**
 * Created by wy on 2017/10/31.
 * webView页面
 */
public class CommonWebView extends BaseActivity<ICommonWebView,
        CommonWebViewPresent<ICommonWebView>>
        implements ICommonWebView {

    private CommonTitleBar myTitle;
    private WebView webView;
    private String title = "";
    private String url = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_webview);
        Bundle extras = getIntent().getExtras();
        title = extras.getString("title");
        url = extras.getString("url");
        initView();
    }

    private void initView() {
        myTitle = (CommonTitleBar) findViewById(R.id.my_title);
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
//        WebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //设置自适应屏幕，两者合用
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.getSettings().setUseWideViewPort(true); //将图片调整到适合webview的大小
        webView.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小


        myTitle.setTitle(title);
        myTitle.setOnBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        WebSettings webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
//        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(false);
        //加载需要显示的网页
        webView.loadUrl(url);
    }

    @Override
    protected CommonWebViewPresent<ICommonWebView> createPresent() {
        return new CommonWebViewPresent<>(this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
