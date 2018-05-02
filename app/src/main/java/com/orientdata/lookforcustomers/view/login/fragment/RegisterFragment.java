package com.orientdata.lookforcustomers.view.login.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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
import com.orientdata.lookforcustomers.bean.WrResponse;
import com.orientdata.lookforcustomers.event.RegisterResultEvent;
import com.orientdata.lookforcustomers.event.UpdateSmsStateEvent;
import com.orientdata.lookforcustomers.network.callback.WrCallback;
import com.orientdata.lookforcustomers.network.util.NetWorkUtils;
import com.orientdata.lookforcustomers.presenter.LoginAndRegisterPresent;
import com.orientdata.lookforcustomers.util.MyOpenActivityUtils;
import com.orientdata.lookforcustomers.util.RxUtils;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.login.imple.LoginAndRegisterActivity;
import com.orientdata.lookforcustomers.widget.CommonTitleBar;

import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by wy on 2017/10/25.
 * 注册界面
 */

public class RegisterFragment extends BaseFragment implements View.OnClickListener, TextView.OnEditorActionListener {

    private EditText etPassword;
    private CommonTitleBar titleBar;
    private LoginAndRegisterPresent mLoginAndRegisterPresent;
    private ImageView ivClear;
    private EditText etAccount;
    private TextView mCallCode;
    private Button btnSubmit;
    private EditText etAuthCode;
    private ImageView ivPasswordHint;
    private TextView tvAgree;
    //协议
    private TextView tvAgreement;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
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
        btnSubmit.setOnClickListener(this);
        ivPasswordHint.setOnClickListener(this);
        etAccount.setOnEditorActionListener(this);
        etAuthCode.setOnEditorActionListener(this);
        tvAgree.setOnClickListener(this);
    }

    private void initView(View view) {
        mLoginAndRegisterPresent = ((LoginAndRegisterActivity) getActivity())
                .getPresent();
        etPassword = view.findViewById(R.id.et_password);
        etPassword.setHint("设置密码:包含字母和数字，6-8位");
        titleBar = view.findViewById(R.id.title_bar);
        titleBar.setTitle("注册");
        titleBar.setOnBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        ivClear = view.findViewById(R.id.iv_clear);
        etAccount = view.findViewById(R.id.et_account);
        mCallCode = view.findViewById(R.id.tv_call_code);
        btnSubmit = view.findViewById(R.id.btn_submit);
        etAuthCode = view.findViewById(R.id.et_auth_code);
        ivPasswordHint = view.findViewById(R.id.iv_password_hint);
        tvAgree = view.findViewById(R.id.tv_agree);
        tvAgree.setSelected(true);
        tvAgreement = view.findViewById(R.id.tv_agreement);
        tvAgreement.setOnClickListener(this);
        String agreement = tvAgreement.getText().toString();
        SpannableString ss = new SpannableString(agreement);
//        ss.setSpan(new AgreementClick(0), 0, "东方网润产品注册协议".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(new AgreementClick(1), "东方网润产品注册协议与".length(), agreement.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvAgreement.setText(ss);
        tvAgreement.setHighlightColor(Color.parseColor("#00FFFFFF"));
        tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());


        //防暴击
        RxUtils.clickView(tvAgreement)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        MyOpenActivityUtils.openCommonWebView(getActivity(),
                                "网润寻客APP用户协议", "http://www.orientdata.cn/protocol.html");
                    }
                });

    }

    class AgreementClick extends ClickableSpan {
        private int type = 0;//0是注册协议，1是推广服务合同

        public AgreementClick(int type) {
            this.type = type;
        }

        @Override
        public void onClick(View widget) {
            if (type == 0) {
                MyOpenActivityUtils.openCommonWebView(getActivity(),
                        "网润寻客APP用户协议", "http://www.orientdata.cn/protocol.html");
            } else {
                MyOpenActivityUtils.openCommonWebView(getActivity(),
                        "推广合同", "http://www.orientdata.cn/contract.html");
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(getResources().getColor(R.color.color_838383));
            ds.setUnderlineText(true);
        }
    }

    public static RegisterFragment newInstance() {
        Bundle args = new Bundle();
        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clear:
                etAccount.setText("");
                break;
//            case R.id.tv_agreement:
//
//                break;
            case R.id.tv_call_code:
                final String phone = etAccount.getText().toString().trim();
                showDefaultLoading();
                NetWorkUtils.phoneIsRegiste(phone, new WrCallback<WrResponse<Integer>>() {
                    @Override
                    public void onSuccess(Response<WrResponse<Integer>> response) {
                        if (response!=null) {
                            int result = response.body().getResult();
                            Logger.d(result==0);
                            if (result==0) { //已经注册
                                ToastUtils.showShort("该手机号已注册，请直接登录");
                                return;
                            }else{  //没有注册
                                mLoginAndRegisterPresent.sendSms(phone);
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
            case R.id.btn_submit:
//                if (!tvAgree.isSelected()) {
//                    ToastUtils.showShort("请先同意协议");
//                    return;
//                }
                String account = etAccount.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String code = etAuthCode.getText().toString().trim();
                mLoginAndRegisterPresent.register(account, password, code);
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
            case R.id.tv_agree:
                if (tvAgree.isSelected()) {
                    tvAgree.setSelected(false);
                } else {
                    tvAgree.setSelected(true);
                }
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
                ivClear.setOnClickListener(RegisterFragment.this);
            }
        }
    };

    @Subscribe
    public void registerResult(RegisterResultEvent registerResultEvent) {
        if (registerResultEvent.isRegister) {
            //注册成功
            // TODO: 2018/4/25 注册成功后，保存新用户的信息
            //登录
            LoginFragment loginFragment = LoginFragment.newInstance(false);
            FragmentTransaction fragmentTransaction = getActivity()
                    .getSupportFragmentManager().beginTransaction();

            fragmentTransaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_from_bottom
                    , R.anim.enter_from_bottom, R.anim.exit_from_bottom);
            fragmentTransaction.replace(R.id.fl_content, loginFragment);

            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
//            getActivity().onBackPressed();
        }
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

    @Override
    public void onDestroyView() {
        etAccount.removeTextChangedListener(etAccountWatch);
        etAccount.setOnEditorActionListener(null);
        etAuthCode.setOnEditorActionListener(null);
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
