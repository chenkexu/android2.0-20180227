package com.orientdata.lookforcustomers.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.presenter.BasePresenter;
import com.orientdata.lookforcustomers.util.OsUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;


/**
 * Created by wy on 2017/9/11.
 * T 是Presenter
 * V 是视图接口
 */

public abstract class  BaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity {
    protected T mPresent;
    private Dialog progressDialog;
    protected ImmersionBar mImmersionBar;
    private InputMethodManager imm;
    private FrameLayout mFrameLayoutContent;
    private View mViewStatusBarPlace;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresent = createPresent();
        mPresent.attachView((V) this);
        EventBus.getDefault().register(this);
        //初始化沉浸式
        if (isImmersionBarEnabled())
            initImmersionBar();
    }



    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    protected void initImmersionBar() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this).
                 keyboardEnable(true)
                .statusBarDarkFont(true, 0.2f)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(R.color.bg_white);
        mImmersionBar.init();
    }



    @Override
    protected void onDestroy() {
        mPresent.detach();
        EventBus.getDefault().unregister(this);//反注册EventBus
        super.onDestroy();
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
     * 构建具体的Presenter
     *
     * @return
     */
    protected abstract T createPresent();


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
















    /**
     * 设置沉浸式状态栏
     *
     * @param fontIconDark 状态栏字体和图标颜色是否为深色
     */
    protected void setImmersiveStatusBar(boolean fontIconDark, int statusBarPlaceColor) {
        setTranslucentStatus();
        if (fontIconDark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    || OsUtil.isMIUI()
                    || OsUtil.isFlyme()) {
                setStatusBarFontIconDark(true);
            } else {
                if (statusBarPlaceColor == Color.WHITE) {
                    statusBarPlaceColor = 0xffcccccc;
                }
            }
        }
        setStatusBarPlaceColor(statusBarPlaceColor);
    }

    private void setStatusBarPlaceColor(int statusColor) {
        if (mViewStatusBarPlace != null) {
            mViewStatusBarPlace.setBackgroundColor(statusColor);
        }
    }

    /**
     * 设置状态栏透明
     */
    private void setTranslucentStatus() {

        // 5.0以上系统状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    /**
     * 设置Android状态栏的字体颜色，状态栏为亮色的时候字体和图标是黑色，状态栏为暗色的时候字体和图标为白色
     *
     * @param dark 状态栏字体是否为深色
     */
    private void setStatusBarFontIconDark(boolean dark) {
        // 小米MIUI
        try {
            Window window = getWindow();
            Class clazz = getWindow().getClass();
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            if (dark) {    //状态栏亮色且黑色字体
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
            } else {       //清除黑色字体
                extraFlagField.invoke(window, 0, darkModeFlag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 魅族FlymeUI
        try {
            Window window = getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (dark) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // android6.0+系统
        // 这个设置和在xml的style文件中用这个<item name="android:windowLightStatusBar">true</item>属性是一样的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (dark) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


}
