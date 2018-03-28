package com.orientdata.lookforcustomers.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.CertificationBean;
import com.orientdata.lookforcustomers.event.LoginResultEvent;
import com.orientdata.lookforcustomers.presenter.LoginAndRegisterPresent;
import com.orientdata.lookforcustomers.util.MyOpenActivityUtils;
import com.orientdata.lookforcustomers.util.SharedPreferencesTool;
import com.orientdata.lookforcustomers.view.home.imple.HomeActivity;
import com.orientdata.lookforcustomers.view.login.ILoginAndRegisterView;
import com.orientdata.lookforcustomers.view.login.imple.LoginAndRegisterActivity;
import com.tencent.connect.UserInfo;

import org.greenrobot.eventbus.Subscribe;
import org.w3c.dom.Text;

import vr.md.com.mdlibrary.AppConfig;
import vr.md.com.mdlibrary.UserDataManeger;

/**
 * 欢迎界面、快闪页面
 */
public class GuideActivity extends BaseActivity<ILoginAndRegisterView, LoginAndRegisterPresent<ILoginAndRegisterView>>
        implements ILoginAndRegisterView{
//    private TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_guide);
//        tvVersion = findViewById(R.id.tvVersion);
//        tvVersion.setText("V"+AppConfig.VER);

        boolean isfirstenter = SharedPreferencesTool.getInstance().getBooleanValue(SharedPreferencesTool.ISFIRSTENTER, true);
        if (isfirstenter) {
            //进入引导页面
            startActivity(new Intent(GuideActivity.this, LaunchActivity.class));
            finish();
        } else {
            setContentView(R.layout.activity_guide);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(GuideActivity.this, HomeActivity.class));
                    finish();
                }
            }, 2000);

        }



      /*
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                boolean isfirstenter = SharedPreferencesTool.getInstance().getBooleanValue(SharedPreferencesTool.ISFIRSTENTER, true);
                if (isfirstenter) {
                    //进入引导页面
                    startActivity(new Intent(GuideActivity.this, LaunchActivity.class));
                } else {
//                    String userId = UserDataManeger.getInstance().getUserId();
//                    String userToken = UserDataManeger.getInstance().getUserToken();
//                    String pwd = UserDataManeger.getInstance().getPassword();
//                    String account = UserDataManeger.getInstance().getAccount();
//                    if(!TextUtils.isEmpty(userId)){
//                        mPresent.accountLogin(account, pwd, true);
//                    }else{
//                        startActivity(new Intent(GuideActivity.this, LoginAndRegisterActivity.class));
//                    }
                    setContentView(R.layout.activity_guide);
                    startActivity(new Intent(GuideActivity.this, HomeActivity.class));
                }
                finish();
            }

        }.sendEmptyMessageDelayed(1, 2000);*/
    }

    @Override
    protected LoginAndRegisterPresent<ILoginAndRegisterView> createPresent() {
        return new LoginAndRegisterPresent<>(this);
    }

    @Override
    public void showLoading() {
//        showDefaultLoading();
    }

    @Override
    public void hideLoading() {
//        hideDefaultLoading();
    }
//    @Subscribe
//    public void loginResult(LoginResultEvent loginResultEvent) {
//        if (loginResultEvent.isLogin) {//登录成功
//            MyOpenActivityUtils.openHomeActivity(this,loginResultEvent.isNewUser);
//            deleteOtherCerInfo();
//        }
//        finish();
//    }

}
