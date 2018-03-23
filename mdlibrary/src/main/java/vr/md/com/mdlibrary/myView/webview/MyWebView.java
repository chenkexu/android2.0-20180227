package vr.md.com.mdlibrary.myView.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.ProgressBar;

import vr.md.com.mdlibrary.BaseActivity;
import vr.md.com.mdlibrary.utils.DisplayUtil;


/**
 * Created by liuboyu on 2015/7/14.
 * <p>
 * 自定义WebView ：添加进度条显示
 */

public class MyWebView extends WebView {

    private ProgressBar progressbar;//进度条
    private MyWebChromeClient myWebChromeClient;

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, DisplayUtil.dip2px(context, 3), 0, 0));
        addView(progressbar);
        myWebChromeClient = new MyWebChromeClient(progressbar, (BaseActivity) context);
        setWebChromeClient(myWebChromeClient);
        setWebViewClient(new MyWebViewClient());
        // 控制所有页面都在当前 webView 中显示，而不是通过第三方浏览器显示
//        setWebViewClient(new WebViewClient());
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}