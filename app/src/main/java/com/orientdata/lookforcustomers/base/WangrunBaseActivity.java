package com.orientdata.lookforcustomers.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.gyf.barlibrary.ImmersionBar;
import com.orientdata.lookforcustomers.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.ArrayList;


/**
 * Created by wy on 2017/9/11.
 * T 是Presenter
 * V 是视图接口
 */

public abstract class WangrunBaseActivity extends AppCompatActivity {
    private Dialog progressDialog;
    protected ImmersionBar mImmersionBar;
    private InputMethodManager imm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        //初始化沉浸式
        if (isImmersionBarEnabled())
            initImmersionBar();
    }


    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    protected void initImmersionBar() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(R.color.bg_white);
        mImmersionBar.init();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
        this.imm = null;
        if (mImmersionBar != null)
            mImmersionBar.destroy();  //在BaseActivity里销毁
    }

    public void finish() {
        super.finish();
        hideSoftKeyBoard();
    }

    public void hideSoftKeyBoard() {
        View localView = getCurrentFocus();
        if (this.imm == null) {
            this.imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        }
        if ((localView != null) && (this.imm != null)) {
            this.imm.hideSoftInputFromWindow(localView.getWindowToken(), 2);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
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

        progressDialog = new Dialog(this, R.style.loadingDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.content__roll_loading, null);
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
     * 根据收到的广播信息关闭，判断自己是否在要被关闭的列表中

     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(CloseEvent event) {
        if (event.getCloseType() == CloseEvent.CLOSE_IN_LIST_ACTIVITY) {
            closeActivity(event);
        }

    }
    /**
     * 关闭在列表中的activity
     * 如果关闭列表为空，则表示需求关闭全部的activity
     * @param event
     */
    private void closeActivity(CloseEvent event) {
        ArrayList<String> nameList = event.getMsg();
        if (nameList == null || nameList.size() == 0) {
            finish();
            return;
        }
        String name = this.getClass().getName();
        for (int i = 0; i < nameList.size(); i++) {
            String nameTmp = nameList.get(i);
            if (TextUtils.equals(name, nameTmp)) {
                finish();
            }
        }
    }
    /**
     * 关闭指定的activity
     *
     * @param c
     */
    public void closeActivity(Class... c) {
        EventBus.getDefault().post(new CloseEvent(CloseEvent.CLOSE_IN_LIST_ACTIVITY,c));
    }
}
