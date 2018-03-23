package vr.md.com.mdlibrary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.socks.library.KLog;
import com.umeng.socialize.UMShareAPI;

import vr.md.com.mdlibrary.myView.dialog.DialogManager;
import vr.md.com.mdlibrary.myView.dialog.MyBasicDialog;
import vr.md.com.mdlibrary.myView.dialog.WaitDialog;
import vr.md.com.mdlibrary.swipbacklayout.SwipeBackActivity;


/**
 * Created by Mr.Z on 16/3/28.
 */
public class BaseActivity extends FragmentActivity {
    //拍照的权限
    private static final int MY_PERMISSIONS_REQUEST = 0;
    private MyBasicDialog myBasicDialog;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);

    }

    /**
     * 方法描述 : 通过toast显示信息
     * 创建时间： 2015年1月16日 下午2:33:38
     *
     * @param str
     */
    public void showToast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 输入日志
     *
     * @param str
     */
    public void log(String str) {
        if (AppConfig.isShowLog) {
            KLog.e(str);
        }
    }


    /**
     * 输入日志
     *
     * @param str
     */
    public void logJoson(String str) {
        if (AppConfig.isShowLog) {
            KLog.json(str);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideWaitDialog();
    }

    /**
     * 显示等待框
     */
    public void showWaitDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        } else {
            progressDialog = null;
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
//        WaitDialog waitDialog = new WaitDialog(this);
//        showDialog(waitDialog);
//
//        waitDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
//        waitDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        waitDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    /**
     * 隐藏等待框
     */
    public void hideWaitDialog() {
//        hideDialog();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    /**
     * 显示对话框
     *
     * @param myBasicDialog
     */
    public void showDialog(MyBasicDialog myBasicDialog) {
        this.myBasicDialog = myBasicDialog;
        myHandler.sendEmptyMessage(2);
    }

    /**
     * 隐藏对话框
     */
    public void hideDialog() {
        myHandler.sendEmptyMessage(1);
    }


    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://隐藏对话框
                    DialogManager.getInstance().hideDialog();
                    break;
                case 2://显示对话框
                    if (this != null && !isFinishing()) {
                        DialogManager.getInstance().showDialog(myBasicDialog);
                    }
                    break;
            }
        }
    };

    /**
     * 点击键盘以外部分，键盘消失
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(BaseActivity.this, "您的权限已被禁止，请手动打开该权限", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
}
