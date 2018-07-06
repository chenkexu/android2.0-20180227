package com.orientdata.lookforcustomers.view.login.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gabrielsamojlo.keyboarddismisser.KeyboardDismisser;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseFragment;
import com.orientdata.lookforcustomers.bean.WrResponse;
import com.orientdata.lookforcustomers.event.LoginResultEvent;
import com.orientdata.lookforcustomers.event.ReLoginEvent;
import com.orientdata.lookforcustomers.event.UpdateSmsStateEvent;
import com.orientdata.lookforcustomers.network.callback.WrCallback;
import com.orientdata.lookforcustomers.network.util.NetWorks;
import com.orientdata.lookforcustomers.presenter.LoginAndRegisterPresent;
import com.orientdata.lookforcustomers.push.TagAliasOperatorHelper;
import com.orientdata.lookforcustomers.util.MyOpenActivityUtils;
import com.orientdata.lookforcustomers.util.SharedPreferencesTool;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.login.imple.LoginAndRegisterActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import vr.md.com.mdlibrary.UserDataManeger;

import static com.orientdata.lookforcustomers.push.TagAliasOperatorHelper.ACTION_SET;
import static com.orientdata.lookforcustomers.push.TagAliasOperatorHelper.sequence;

/**
 * Created by wy on 2017/10/25.
 */

public class LoginFragment extends BaseFragment implements View.OnClickListener, TextView.OnEditorActionListener {

    private LoginAndRegisterPresent mLoginAndRegisterPresent;
    private TextView mRecodePassword;
    private View mPasswordLabel;
    private View mAuthCodeLabel;
    private int type = 0;//0代表当前是账号登陆，1代表是验证码登录
    private TextView mLoginChange;
    private TextView mForgetPassword;
    private RelativeLayout rlPasswordHint;
    private ImageView ivPasswordHint;
    private ImageView ivClear;
    private TextView mCallCode;
    private Button mLogin;
    private EditText etAccount;
    private EditText etPassword;
    private EditText etAuthCode;
    private TextView tvRegister;
    private static boolean reLogin = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        init(view);
        initEvent();
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        KeyboardDismisser.useWith(this);
    }

    private void initEvent() {
        mRecodePassword.setOnClickListener(this);
        mLoginChange.setOnClickListener(this);
        mForgetPassword.setOnClickListener(this);
        ivPasswordHint.setOnClickListener(this);
        ivClear.setOnClickListener(this);
        mCallCode.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        etAccount.setOnEditorActionListener(this);
    }

    private void init(View view) {
        mLoginAndRegisterPresent = ((LoginAndRegisterActivity) getActivity()).getPresent();
        mRecodePassword = view.findViewById(R.id.tv_record_password);
        mRecodePassword.setSelected(true);
        mPasswordLabel = view.findViewById(R.id.password_label);
        mAuthCodeLabel = view.findViewById(R.id.auth_code_label);
        mLoginChange = view.findViewById(R.id.tv_login_change);
        mForgetPassword = view.findViewById(R.id.tv_forget_password);
        rlPasswordHint = view.findViewById(R.id.rl_password_hint);
        ivPasswordHint = view.findViewById(R.id.iv_password_hint);
        ivClear = view.findViewById(R.id.iv_clear);
        mCallCode = view.findViewById(R.id.tv_call_code);
        mLogin = view.findViewById(R.id.btn_login);
        etAccount = view.findViewById(R.id.et_account);
        etPassword = view.findViewById(R.id.et_password);
        etAuthCode = view.findViewById(R.id.et_auth_code);
        tvRegister = view.findViewById(R.id.tv_register);
        etAccount.addTextChangedListener(etAccountWatch);

        String account = UserDataManeger.getInstance().getAccount();
        String password = UserDataManeger.getInstance().getPassword();

        if (!TextUtils.isEmpty(account)) {
            etAccount.setText(account);
            etPassword.setText(password);
        }
    }

    public static LoginFragment newInstance(boolean isReLogin) {
        reLogin = isReLogin;
        Logger.d("打开登录界面。。。。");
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_record_password:
                if (mRecodePassword.isSelected()) {
                    mRecodePassword.setSelected(false);
                } else {
                    mRecodePassword.setSelected(true);
                }
                break;
            case R.id.tv_login_change:
                if (type == 0) {
                    type = 1;
                    mLoginChange.setText("密码登录");
                    rlPasswordHint.setVisibility(View.INVISIBLE);
                    mPasswordLabel.setVisibility(View.GONE);
                    mAuthCodeLabel.setVisibility(View.VISIBLE);
                } else {
                    type = 0;
                    mLoginChange.setText("验证码登录");
                    mPasswordLabel.setVisibility(View.VISIBLE);
                    rlPasswordHint.setVisibility(View.VISIBLE);
                    mAuthCodeLabel.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_forget_password:
                toResetPasswordFragment();
                break;
            case R.id.iv_password_hint:
                if (ivPasswordHint.isSelected()) {
                    ivPasswordHint.setSelected(false);
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    ivPasswordHint.setSelected(true);
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                break;
            case R.id.tv_call_code:
                final String phone = etAccount.getText().toString().trim();
                showDefaultLoading();
                NetWorks.phoneIsRegiste(phone, new WrCallback<WrResponse<Integer>>() {
                    @Override
                    public void onSuccess(Response<WrResponse<Integer>> response) {
                        if (response!=null) {
                            int result = response.body().getResult();
                            Logger.d(result==0);
                            if (result==0) { //已经注册
                                mLoginAndRegisterPresent.sendSms(phone);
                            }else{  //没有注册
                                ToastUtils.showShort("该手机号未注册，请先注册");
                                return;
                            }
                        }else{
                            ToastUtils.showShort("服务器异常");
                        }
                    }

                    @Override
                    public void onError(Response<WrResponse<Integer>> response) {
                        super.onError(response);
                        ToastUtils.showShort(response.getException().getMessage());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        hideDefaultLoading();
                    }
                });


                break;
            case R.id.btn_login:
                if (type == 0) {//账号登录
                    String account = etAccount.getText().toString();
                    String password = etPassword.getText().toString();
                    mLoginAndRegisterPresent.accountLogin(account, password, mRecodePassword.isSelected());
                } else {//验证码登录
                    String account = etAccount.getText().toString();
                    String code = etAuthCode.getText().toString();
                    mLoginAndRegisterPresent.authCodeLogin(account, code);
                }
                break;
            case R.id.tv_register:
                toRegister();
                break;
            case R.id.iv_clear:
                etAccount.setText("");
                etPassword.setText("");
                break;
        }
    }

    /**
     * 忘记密码
     */
    private void toResetPasswordFragment() {
        ResetPasswordFragment resetPasswordFragment = ResetPasswordFragment.newInstance();

        FragmentTransaction fragmentTransaction = getActivity()
                .getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_from_bottom
                , R.anim.enter_from_bottom, R.anim.exit_from_bottom);

        fragmentTransaction.replace(R.id.fl_content, resetPasswordFragment);

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * 跳转到注册页面
     */
    private void toRegister() {
        RegisterFragment registerFragment = RegisterFragment.newInstance();

        FragmentTransaction fragmentTransaction = getActivity()
                .getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left
                , R.anim.enter_from_left, R.anim.exit_from_right);

        fragmentTransaction.replace(R.id.fl_content, registerFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }

    //设置Jpush别名
    private void setJpushAlias(String alias){
        if(TextUtils.isEmpty(alias)){
            return;
        }
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = ACTION_SET;
        sequence++;
        tagAliasBean.alias = alias;
        tagAliasBean.isAliasAction = true;
        TagAliasOperatorHelper.getInstance().handleAction(getActivity(),sequence,tagAliasBean);
    }

    @Subscribe
    public void loginResult(LoginResultEvent loginResultEvent) {
        if (!reLogin && loginResultEvent.isLogin) {//正常登录成功打开主页   重新登录不需要
            Logger.d("登录成功。");
            setJpushAlias(loginResultEvent.userId);
            MyOpenActivityUtils.openHomeActivity(getActivity(),loginResultEvent.isNewUser);
            //移除是否退出的标志位
            SharedPreferencesTool.getInstance().putBoolean(SharedPreferencesTool.USER_LOGOUT, false);
        }else if(reLogin){
            ReLoginEvent reLoginEvent = new ReLoginEvent();
            reLoginEvent.reLogin = true;
            EventBus.getDefault().post(reLoginEvent);
        }
        getActivity().finish();
    }

    @Subscribe
    public void updateSmsState(UpdateSmsStateEvent updateSmsStateEvent) {
        if (updateSmsStateEvent.isCall) {
            mCallCode.setText("获取验证码");
            mCallCode.setTextColor(Color.parseColor("#ffffff"));
            mCallCode.setClickable(true);
        } else {
            mCallCode.setText(updateSmsStateEvent.surplusTime);
            mCallCode.setTextColor(Color.parseColor("#d9d9d9"));
            mCallCode.setClickable(false);
        }
    }


    TextWatcher etAccountWatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String content = s.toString();
            if (TextUtils.isEmpty(content)) {
                ivClear.setVisibility(View.INVISIBLE);
                ivClear.setOnClickListener(null);
            } else {
                ivClear.setVisibility(View.VISIBLE);
                ivClear.setOnClickListener(LoginFragment.this);
            }
        }
    };

    @Override
    public void onDestroyView() {
//        EventBus.getDefault().unregister(this);
        etAccount.removeTextChangedListener(etAccountWatch);
        super.onDestroyView();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT || event.getAction() == KeyEvent.KEYCODE_ENTER) {
            switch (v.getId()) {
                case R.id.et_account:
                    if (type == 0) {
                        etPassword.requestFocus();
                    } else {
                        etAuthCode.requestFocus();
                    }
                    break;
            }
            return true;
        }
        return false;
    }
}
