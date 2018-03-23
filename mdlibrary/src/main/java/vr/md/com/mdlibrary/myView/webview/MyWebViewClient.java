package vr.md.com.mdlibrary.myView.webview;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by LiuBoYu on 2015/9/9.
 */
public class MyWebViewClient extends WebViewClient {
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
            //用javascript隐藏系统定义的404页面信息
            String data = "网络异常，请稍后再试！";
            view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
//        view.loadData("", "text/html", "UTF-8");
//        Log.i("MyWebViewClient","断网了");
    }
}
