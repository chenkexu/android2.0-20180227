package com.orientdata.lookforcustomers.view.login.imple;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.presenter.LoginAndRegisterPresent;
import com.orientdata.lookforcustomers.view.login.ILoginAndRegisterView;
import com.orientdata.lookforcustomers.view.login.fragment.LoginFragment;
import com.orientdata.lookforcustomers.view.login.fragment.RegisterFragment;
import com.orientdata.lookforcustomers.view.login.fragment.ResetPasswordFragment;

import cn.jpush.android.api.JPushInterface;

import static com.orientdata.lookforcustomers.push.TagAliasOperatorHelper.sequence;

/**
 * 登录注册
 */
public class LoginAndRegisterActivity extends BaseActivity<ILoginAndRegisterView, LoginAndRegisterPresent<ILoginAndRegisterView>> implements ILoginAndRegisterView {

    private FrameLayout flContent;
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private FragmentManager fragmentManager;

    private int type = 0;//0登录，1注册，3重置密码
    private boolean isReLogin = false;//是不是重新登录
    private boolean isNoBack = false;//是否禁掉返回键


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        setImmersiveStatusBar(true, R.color.white);
//        ImmersionBar.with(this)
//                .fitsSystemWindows(true)
//                .statusBarDarkFont(true, 0.2f)
//                  //使用该属性,必须指定状态栏颜色
//                .statusBarColor(R.color.bg_white).init();
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    private void initView() {
        isReLogin = getIntent().getBooleanExtra("isReLogin",false);
        isNoBack = getIntent().getBooleanExtra("isNoBack",false);
        type = getIntent().getIntExtra("type",0);
        flContent = (FrameLayout) findViewById(R.id.fl_content);
        initFragment();

    }

    private void initFragment(){
        if(type == 0){  //进入登陆界面
            JPushInterface.deleteAlias(this,sequence);
            loginFragment = LoginFragment.newInstance(isReLogin);
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.fl_content, loginFragment).commit();
        }else if(type == 3){  //进入修改密码界面
            ResetPasswordFragment resetPasswordFragment = ResetPasswordFragment.newInstance();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fl_content, resetPasswordFragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    protected LoginAndRegisterPresent<ILoginAndRegisterView> createPresent() {
        return new LoginAndRegisterPresent<>(this);
    }

    public LoginAndRegisterPresent<ILoginAndRegisterView> getPresent() {
        return mPresent;
    }


    @Override
    public void showLoading() {
        showDefaultLoading();
    }

    @Override
    public void hideLoading() {
        hideDefaultLoading();
    }

    @Override
    public void onPause() {
        super.onPause();
        isNoBack = false;
    }



//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && isNoBack) {
//            return true;
//        }
//        return false;
//    }



//    private long exitTime;
//    @Override
//    public void onBackPressed() {
//        long nowTime = System.currentTimeMillis();
//        if((nowTime - exitTime) <= 2000){
//            finish();
//            AppManager.getAppManager().AppExit(this);
//        }else{
//            ToastUtils.showLong("再按一次后退键退出程序！");
//            exitTime = nowTime;
//        }
//    }


}
