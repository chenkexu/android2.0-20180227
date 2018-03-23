package com.orientdata.lookforcustomers.base;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import com.orientdata.lookforcustomers.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.ArrayList;


/**
 * Created by wy on 2017/9/11.
 * T 是Presenter
 * V 是视图接口
 */

public abstract class WangrunBaseActivity extends Activity {
    private Dialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
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
