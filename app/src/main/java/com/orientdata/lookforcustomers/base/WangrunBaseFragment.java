package com.orientdata.lookforcustomers.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.event.ImgClipResultEvent;
import com.orientdata.lookforcustomers.presenter.BasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by wy on 2017/10/24.
 */

public abstract class WangrunBaseFragment<V, T extends BasePresenter<V>> extends Fragment {
    protected T mPresent;
    private Dialog progressDialog;
    private BaseActivity baseActivity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        baseActivity = (BaseActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresent = createPresent();
        mPresent.attachView((V) this);
        EventBus.getDefault().register(this);
    }


    @Override
    public void onDestroy() {
        mPresent.detach();
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    /**
     * 显示默认的进度条
     */
    protected void showDefaultLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        } else {
            progressDialog = null;
        }

        progressDialog = new Dialog(baseActivity, R.style.loadingDialog);
        View view = LayoutInflater.from(baseActivity).inflate(R.layout.content__roll_loading, null);
        progressDialog.setContentView(view);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    /**
     * 隐藏默认的进度条
     */
    protected void hideDefaultLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    /**
     * 构建具体的Presenter
     *
     * @return
     */
    protected abstract T createPresent();
    @Subscribe
    public void imgClipResult(ImgClipResultEvent imgClipResultEvent) {
    }

}
