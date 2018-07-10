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
import android.widget.TextView;

import com.gabrielsamojlo.keyboarddismisser.KeyboardDismisser;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseFragment;
import com.orientdata.lookforcustomers.base.CloseEvent;
import com.orientdata.lookforcustomers.bean.WrResponse;
import com.orientdata.lookforcustomers.event.ResetPasswordResultEvent;
import com.orientdata.lookforcustomers.event.UpdateSmsStateEvent;
import com.orientdata.lookforcustomers.network.callback.WrCallback;
import com.orientdata.lookforcustomers.network.util.NetWorks;
import com.orientdata.lookforcustomers.presenter.LoginAndRegisterPresent;
import com.orientdata.lookforcustomers.util.CommonUtils;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.home.MainHomeActivity;
import com.orientdata.lookforcustomers.view.login.imple.LoginAndRegisterActivity;
import com.orientdata.lookforcustomers.view.mine.imple.SettingActivity;
import com.orientdata.lookforcustomers.widget.CommonTitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.jpush.android.api.JPushInterface;

import static com.orientdata.lookforcustomers.push.TagAliasOperatorHelper.sequence;

/**
 * Created by wy on 2017/10/27.
 * 忘记密码
 */

public class ResetPasswordFragment extends BaseFragment implements View.OnClickListener, TextView.OnEditorActionListener {

    private CommonTitleBar titleBar;
    private EditText etAccount;
    private EditText etPassword;
    private EditText etAuthCode;
    private ImageView ivClear;
    private TextView mCallCode;
    private LoginAndRegisterPresent mLoginAndRegisterPresent;
    private ImageView ivPasswordHint;
    private Button btnSubmit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        initView(view);
        initEvent();
        return view;
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        KeyboardDismisser.useWith(this);
    }
    private void initEvent() {
        ivClear.setOnClickListener(this);
        etAccount.addTextChangedListener(etAccountWatch);
        mCallCode.setOnClickListener(this);
        ivPasswordHint.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        etAccount.setOnEditorActionListener(this);
        etAuthCode.setOnEditorActionListener(this);
    }

    private void initView(View view) {
        mLoginAndRegisterPresent = ((LoginAndRegisterActivity) getActivity()).getPresent();
        titleBar = view.findViewById(R.id.title_bar);
        titleBar.setTitle("重置密码");
        titleBar.setOnBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        etAccount = view.findViewById(R.id.et_account);
        etPassword = view.findViewById(R.id.et_password);
        etAuthCode = view.findViewById(R.id.et_auth_code);
        ivClear = view.findViewById(R.id.iv_clear);
        mCallCode = view.findViewById(R.id.tv_call_code);
        ivPasswordHint = view.findViewById(R.id.iv_password_hint);
        btnSubmit = view.findViewById(R.id.btn_submit);
    }

    public static ResetPasswordFragment newInstance() {

        Bundle args = new Bundle();

        ResetPasswordFragment fragment = new ResetPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clear:
                etAccount.setText("");
                break;
            case R.id.tv_call_code:
                final String phone = etAccount.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.showShort("请输入手机号");
                    return;
                }
                if (!CommonUtils.isPhoneNum(phone)) {
                    ToastUtils.showShort("请输入正确的手机号");
                    return;
                }
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
            case R.id.iv_password_hint:
                if (ivPasswordHint.isSelected()) {
                    ivPasswordHint.setSelected(false);
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    ivPasswordHint.setSelected(true);
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                break;
            case R.id.btn_submit:
                String phoneNo = etAccount.getText().toString().trim();
                String code = etAuthCode.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                mLoginAndRegisterPresent.resetPassword(phoneNo, code, password);
                break;
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
                ivClear.setOnClickListener(ResetPasswordFragment.this);
            }
        }
    };

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

    @Subscribe
    public void resetPasswordResult(ResetPasswordResultEvent resetPasswordResultEvent) {
        if (resetPasswordResultEvent.isResetPassword) {
            ToastUtils.showShort("修改成功");
            //关闭掉 前面的homeactivity 和 settingactivity
            EventBus.getDefault().post(new CloseEvent(CloseEvent.CLOSE_IN_LIST_ACTIVITY, MainHomeActivity.class, SettingActivity.class));

            JPushInterface.deleteAlias(getActivity(),sequence);
            //登录
            LoginFragment loginFragment = LoginFragment.newInstance(false);
            FragmentTransaction fragmentTransaction = getActivity()
                    .getSupportFragmentManager().beginTransaction();

            fragmentTransaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_from_bottom
                    , R.anim.enter_from_bottom, R.anim.exit_from_bottom);
            fragmentTransaction.replace(R.id.fl_content, loginFragment);

            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }


    @Override
    public void onDestroyView() {
        etAccount.removeTextChangedListener(etAccountWatch);
        super.onDestroyView();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT || event.getAction() == KeyEvent.KEYCODE_ENTER) {
            switch (v.getId()) {
                case R.id.et_account:
                    etAuthCode.requestFocus();
                    break;
                case R.id.et_auth_code:
                    etPassword.requestFocus();
                    break;
            }
            return true;
        }

        return false;
    }
}
