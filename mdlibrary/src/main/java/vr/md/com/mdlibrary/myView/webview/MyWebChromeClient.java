package vr.md.com.mdlibrary.myView.webview;

import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import vr.md.com.mdlibrary.BaseActivity;


/**
 * Created by liuboyu on 2015/7/14.
 *
 * 自定义 WebChromeClient ，用于控制 进度条的更新，以及加载提示框的显示与消失
 */
public class MyWebChromeClient extends android.webkit.WebChromeClient {

    private ProgressBar progressbar;//加载进度条
    private BaseActivity baseActivity;// 上下文

    public MyWebChromeClient(ProgressBar progressbar, BaseActivity baseActivity) {
        super();
        this.baseActivity = baseActivity;
        this.progressbar = progressbar;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress == 100) {
            progressbar.setVisibility(View.GONE);
        } else {
            if (progressbar.getVisibility() == View.GONE)
                progressbar.setVisibility(View.VISIBLE);
            progressbar.setProgress(newProgress);
        }
        super.onProgressChanged(view, newProgress);
    }


}