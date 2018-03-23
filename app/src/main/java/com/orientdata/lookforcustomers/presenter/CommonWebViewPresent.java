package com.orientdata.lookforcustomers.presenter;

import com.orientdata.lookforcustomers.view.webview.ICommonWebView;

/**
 * Created by wy on 2017/10/31.
 */

public class CommonWebViewPresent<T> extends BasePresenter<ICommonWebView> {
    private ICommonWebView mCommonWebView;

    public CommonWebViewPresent(ICommonWebView mCommonWebView) {
        this.mCommonWebView = mCommonWebView;
    }

    @Override
    public void fecth() {

    }
}
