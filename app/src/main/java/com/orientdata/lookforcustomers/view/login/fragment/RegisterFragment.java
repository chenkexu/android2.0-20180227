package com.orientdata.lookforcustomers.view.login.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
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

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseFragment;
import com.orientdata.lookforcustomers.event.RegisterResultEvent;
import com.orientdata.lookforcustomers.event.UpdateSmsStateEvent;
import com.orientdata.lookforcustomers.presenter.LoginAndRegisterPresent;
import com.orientdata.lookforcustomers.util.MyOpenActivityUtils;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.login.imple.LoginAndRegisterActivity;
import com.orientdata.lookforcustomers.widget.CommonTitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by wy on 2017/10/25.
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
        String agreement = tvAgreement.getText().toString();
        SpannableString ss = new SpannableString(agreement);
        ss.setSpan(new AgreementClick(0), 0, "东方网润产品注册协议".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(new AgreementClick(1), "东方网润产品注册协议与".length(), agreement.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvAgreement.setText(ss);
        tvAgreement.setHighlightColor(Color.parseColor("#00FFFFFF"));
        tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());

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
                        "注册协议", "http://www.orientdata.cn/protocol.html");
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
            case R.id.tv_call_code:
                String phone = etAccount.getText().toString().trim();
                mLoginAndRegisterPresent.sendSms(phone);
                break;
            case R.id.btn_submit:
                if (!tvAgree.isSelected()) {
                    ToastUtils.showShort("请先同意协议");
                    return;
                }
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
            getActivity().onBackPressed();
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
