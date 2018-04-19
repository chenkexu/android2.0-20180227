package com.orientdata.lookforcustomers.presenter;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.webkit.CookieManager;

import com.orientdata.lookforcustomers.bean.ErrBean;
import com.orientdata.lookforcustomers.event.LoginResultEvent;
import com.orientdata.lookforcustomers.event.LogoutResultEvent;
import com.orientdata.lookforcustomers.event.RegisterResultEvent;
import com.orientdata.lookforcustomers.event.ResetPasswordResultEvent;
import com.orientdata.lookforcustomers.event.UpdateSmsStateEvent;
import com.orientdata.lookforcustomers.model.ILogOutModel;
import com.orientdata.lookforcustomers.model.ILoginModel;
import com.orientdata.lookforcustomers.model.IRegisterModel;
import com.orientdata.lookforcustomers.model.IResetPasswordModel;
import com.orientdata.lookforcustomers.model.ISendSmsModel;
import com.orientdata.lookforcustomers.model.IUserModel;
import com.orientdata.lookforcustomers.model.imple.LoginModelImple;
import com.orientdata.lookforcustomers.model.imple.LogoutModelImple;
import com.orientdata.lookforcustomers.model.imple.RegisterModel;
import com.orientdata.lookforcustomers.model.imple.ResetPasswordModelImple;
import com.orientdata.lookforcustomers.model.imple.SendSmsModelImple;
import com.orientdata.lookforcustomers.model.imple.UserModelImple;
import com.orientdata.lookforcustomers.util.CommonUtils;
import com.orientdata.lookforcustomers.util.SharedPreferencesTool;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.login.ILoginAndRegisterView;
import org.greenrobot.eventbus.EventBus;
import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.utils.MD5;

/**
 * Created by wy on 2017/10/24.
 */

public class LoginAndRegisterPresent<T> extends BasePresenter<ILoginAndRegisterView> {
    private ILoginAndRegisterView mLoginAndRegisterView;
    private ILoginModel mLoginModel = new LoginModelImple();
    private IRegisterModel mRegisterModel = new RegisterModel();
    private ISendSmsModel mSendSmsModel = new SendSmsModelImple();
    private IUserModel mUserModel = new UserModelImple();
    private ILogOutModel mLogOutModel = new LogoutModelImple();
    private IResetPasswordModel mResetPasswordModel = new ResetPasswordModelImple();
    //验证码Id
    private String codeId = "";
    private long totalTime = 60000;
    private long progress = 0;
    private CountDownTimer timer;

    public LoginAndRegisterPresent(ILoginAndRegisterView iLoginAndRegisterView) {
        this.mLoginAndRegisterView = iLoginAndRegisterView;
    }

    /**
     * 验证码登录
     *
     * @param account
     * @param code
     */
    public void authCodeLogin(String account, String code) {
        if (TextUtils.isEmpty(account)) {
            ToastUtils.showShort("请输入账号");
            return;
        }
        if (!CommonUtils.isPhoneNum(account)) {
            ToastUtils.showShort("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showShort("请输入验证码");
            return;
        }
        if (mUserModel != null && mLoginModel != null
                && mLoginAndRegisterView != null) {
            mLoginAndRegisterView.showLoading();
            mLoginModel.authCodeLogin(account, code, codeId, mUserModel.getPhoneType()
                    , mUserModel.getModel(),
                    mUserModel.getDeviceToken(), new ILoginModel.LoginComplete() {
                        @Override
                        public void onSuccess(boolean isNewUser) {
                            codeId = "";
                            mLoginAndRegisterView.hideLoading();
                            LoginResultEvent loginResultEvent = new LoginResultEvent();
                            loginResultEvent.isLogin = true;
                            loginResultEvent.isNewUser = isNewUser;
                            EventBus.getDefault().post(loginResultEvent);
                        }

                        @Override
                        public void onError(int code, String message) {
                            mLoginAndRegisterView.hideLoading();
                            ToastUtils.showShort(message);
                        }
                    });
        }
    }

    /**
     * 账号登录
     *
     * @param account
     * @param password
     */
    public void accountLogin(String account, String password, boolean isSave) {
        if (TextUtils.isEmpty(account)) {
            ToastUtils.showShort("请输入账号");
            return;
        }
        if (!CommonUtils.isPhoneNum(account)) {
            ToastUtils.showShort("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showShort("请输入密码");
            return;
        }

        if (isSave) {
            UserDataManeger.getInstance().setAccount(account);
            UserDataManeger.getInstance().setPassword(password);
        } else {
            UserDataManeger.getInstance().setAccount("");
            UserDataManeger.getInstance().setPassword("");
        }
        if (mUserModel != null && mLoginModel != null && mLoginAndRegisterView != null) {
            mLoginAndRegisterView.showLoading();
            password = MD5.md5(password);
            mLoginModel.accountLogin(account, password, mUserModel.getPhoneType()
                    , mUserModel.getModel(),
                    mUserModel.getDeviceToken()
                    , new ILoginModel.LoginComplete() {
                        @Override
                        public void onSuccess(boolean isNewUser) {
                            mLoginAndRegisterView.hideLoading();
                            LoginResultEvent loginResultEvent = new LoginResultEvent();
                            loginResultEvent.isLogin = true;
                            loginResultEvent.isNewUser = isNewUser;
                            EventBus.getDefault().post(loginResultEvent);
                        }

                        @Override
                        public void onError(int code, String message) {
                            mLoginAndRegisterView.hideLoading();
                            ToastUtils.showShort(message);
                        }
                    });
        }
    }

    /**
     * 进行注册
     *
     * @param account
     * @param password
     * @param code
     */
    public void register(String account, String password, String code) {
        if (TextUtils.isEmpty(account)) {
            ToastUtils.showShort("请输入正确的手机号");
            return;
        }
        if (!CommonUtils.isPhoneNum(account)) {
            ToastUtils.showShort("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showShort("请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showShort("请输入密码");
            return;
        }
        if (!CommonUtils.checkPassword(password)) {
            ToastUtils.showShort("密码格式错误");
            return;
        }
        if (mLoginAndRegisterView != null && mRegisterModel != null &&
                mUserModel != null) {
            mLoginAndRegisterView.showLoading();
            password = MD5.md5(password);
            mRegisterModel.register(account, password, code, codeId
                    , mUserModel.getPhoneType()
                    , mUserModel.getModel(),
                    mUserModel.getDeviceToken(), new IRegisterModel.RegisterComplete() {
                        @Override
                        public void success() {
                            mLoginAndRegisterView.hideLoading();
                            codeId = "";
                            RegisterResultEvent registerResultEvent = new RegisterResultEvent();
                            registerResultEvent.isRegister = true;
                            EventBus.getDefault().post(registerResultEvent);
                        }

                        @Override
                        public void onError(int code, String message) {
                            mLoginAndRegisterView.hideLoading();
                            ToastUtils.showShort(message);
                        }
                    });
        }
    }

    /**
     * 发送短信
     *
     * @param phoneNo
     */
    public void sendSms(String phoneNo) {
        if (TextUtils.isEmpty(phoneNo)) {
            ToastUtils.showShort("请输入手机号");
            return;
        }
        if (!CommonUtils.isPhoneNum(phoneNo)) {
            ToastUtils.showShort("请输入正确的手机号");
            return;
        }
        if (mLoginAndRegisterView != null && mSendSmsModel != null) {
            mLoginAndRegisterView.showLoading();
            mSendSmsModel.sendSms(phoneNo, codeId, new ISendSmsModel.SendSmsComplete() {
                @Override
                public void success(String id) {
                    mLoginAndRegisterView.hideLoading();
                    codeId = id;
                    countDown();
                }

                @Override
                public void onError(int code, String message) {
                    mLoginAndRegisterView.hideLoading();
                    ToastUtils.showShort(message);
                }
            });
        }
    }

    /**
     * 退出登录
     */
    public void logOut(){
        if (mLoginAndRegisterView != null && mLogOutModel != null) {
            mLoginAndRegisterView.showLoading();
            mLogOutModel.logOut(new ILogOutModel.Complete(){
                @Override
                public void onSuccess(ErrBean errBean) {

                    //记录已经退出登录了
                    SharedPreferencesTool.getInstance().putBoolean(SharedPreferencesTool.USER_LOGOUT, true);

                    mLoginAndRegisterView.hideLoading();
                    LogoutResultEvent logoutResultEvent = new LogoutResultEvent();
                    logoutResultEvent.errBean = errBean;
                    //发出广播，返回到登录界面
                    EventBus.getDefault().post(logoutResultEvent);
                }

                @Override
                public void onError(int code, String message) {
                    mLoginAndRegisterView.hideLoading();
                    ToastUtils.showShort(message);
                }
            });
        }
    }

    public void resetPassword(String phone, final String code, String password) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort("请输入手机号");
            return;
        }
        if (!CommonUtils.isPhoneNum(phone)) {
            ToastUtils.showShort("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showShort("请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showShort("请输入密码");
            return;
        }
        if (!CommonUtils.checkPassword(password)) {
            ToastUtils.showShort("密码格式错误");
            return;
        }
        if (mLoginAndRegisterView != null && mResetPasswordModel != null) {
            mLoginAndRegisterView.showLoading();
            password = MD5.md5(password);
            mResetPasswordModel.resetPassword(phone, code, codeId, password, new IResetPasswordModel.ResetPasswordComplete() {
                @Override
                public void onSuccess() {
                    mLoginAndRegisterView.hideLoading();
                    codeId = "";
                    ResetPasswordResultEvent resetPasswordResultEvent = new ResetPasswordResultEvent();
                    resetPasswordResultEvent.isResetPassword = true;
                    EventBus.getDefault().post(resetPasswordResultEvent);

                }

                @Override
                public void onError(int code, String message) {
                    mLoginAndRegisterView.hideLoading();
                    ToastUtils.showShort(message);
                }
            });
        }
    }

    //倒计时开始
    private void countDown() {
        progress = totalTime;
        timer = new CountDownTimer(totalTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //不可再次获取验证码
                progress -= 1000;
                String currentTime = progress / 1000 + "s";
                UpdateSmsStateEvent updateSmsStateEvent = new UpdateSmsStateEvent();
                updateSmsStateEvent.isCall = false;
                updateSmsStateEvent.surplusTime = currentTime;
                EventBus.getDefault().post(updateSmsStateEvent);
            }

            @Override
            public void onFinish() {
                //可以再次获取验证码
                progress = totalTime;
                UpdateSmsStateEvent updateSmsStateEvent = new UpdateSmsStateEvent();
                updateSmsStateEvent.isCall = true;
                updateSmsStateEvent.surplusTime = "0s";
                EventBus.getDefault().post(updateSmsStateEvent);
            }
        };
        timer.start();
    }

    public void cancle() {
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void fecth() {

    }

}
