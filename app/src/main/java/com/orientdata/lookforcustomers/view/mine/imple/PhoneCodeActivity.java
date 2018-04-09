package com.orientdata.lookforcustomers.view.mine.imple;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.event.UpdateSmsTimeEvent;
import com.orientdata.lookforcustomers.event.UpdateToBalanceEvent;
import com.orientdata.lookforcustomers.presenter.CommissionPresent;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.mine.ICommissionView;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.orientdata.lookforcustomers.widget.dialog.SubmitFeedBackDialog;

import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;

import vr.md.com.mdlibrary.UserDataManeger;

/**
 * 获取手机验证码
 */

public class PhoneCodeActivity extends BaseActivity<ICommissionView, CommissionPresent<ICommissionView>> implements ICommissionView,View.OnClickListener {
    private MyTitle title;
    private TextView tvGetCode,tvPhone,tvConfirm;
    private EditText tvCode;
    private String phone = "";
    private String aliPayAccount = "";
    private String commission = "";
    private String codeId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_code);
        initView();
        initTitle();


    }

    @Override
    protected CommissionPresent<ICommissionView> createPresent() {
        return new CommissionPresent<>(this);
    }


    private void initView() {
        phone = UserDataManeger.getInstance().getAccount();
        aliPayAccount = getIntent().getStringExtra("zhiFu");
        commission = getIntent().getStringExtra("commission");

        title = findViewById(R.id.my_title);
        tvGetCode = findViewById(R.id.tvGetCode);
        tvPhone = findViewById(R.id.tvPhone);
        tvConfirm = findViewById(R.id.tvConfirm);
        tvCode = findViewById(R.id.tvCode);
        tvGetCode.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        updateView();
    }
    private void updateView(){
        tvPhone.setText(getPhone(phone));
    }

    private void initTitle() {
        title.setTitleName("佣金提现");
        title.setImageBack(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvConfirm:
                String code = tvCode.getText().toString();
                if(TextUtils.isEmpty(code)){
                    ToastUtils.showShort("请输入验证码！");
                    return;
                }
                commission = commission.replace("元","");
                mPresent.updateUserCommission(BigDecimal.valueOf(Double.parseDouble(commission)),aliPayAccount,code,codeId);
                break;
            case R.id.tvGetCode:
                if(TextUtils.isEmpty(phone)){
                    ToastUtils.showShort("获取用户手机失败");
                    return;
                }
                mPresent.sendSms(phone);
                break;
        }
    }

    @Override
    public void showLoading() {
        showDefaultLoading();
    }

    @Override
    public void hideLoading() {
        hideDefaultLoading();
    }

    /**
     * 手机中间是****
     * @param pNumber
     * @return
     */
    private String getPhone(String pNumber){
        if(!TextUtils.isEmpty(pNumber) && pNumber.length() > 6 ){
            StringBuilder sb  =new StringBuilder();
            for (int i = 0; i < pNumber.length(); i++) {
                char c = pNumber.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }
        return null;
    }
    @Subscribe
    public void updateSmsState(UpdateSmsTimeEvent updateSmsTimeEvent) {
        codeId = updateSmsTimeEvent.codeId;
        if (updateSmsTimeEvent.isCall) {
            tvGetCode.setText("再次获取(60)");
            tvGetCode.setClickable(true);
            tvGetCode.setBackground(getResources().getDrawable(R.drawable.bg_tv_round_gradient));
//            tvCode.setBackground(getResources().getDrawable(R.drawable.bg_tv_round_gradient));
        } else {
            tvGetCode.setText(updateSmsTimeEvent.surplusTime);
            tvGetCode.setBackground(getResources().getDrawable(R.drawable.round_border_code));
//            tvCode.setBackground(getResources().getDrawable(R.drawable.round_border1));
            tvGetCode.setClickable(false);
        }
    }
    @Subscribe
    public void updateUserCommission(UpdateToBalanceEvent updateToBalanceEvent){
        if(updateToBalanceEvent.err.getCode() == 0){
            mPresent.getCommission();
            final SubmitFeedBackDialog submitFeedBackDialog = new SubmitFeedBackDialog(this,"操作成功！",
                    "确定",R.mipmap.submit_suc,"温馨提示：款项会在1～3个工作日内到账。");
            submitFeedBackDialog.setClickListenerInterface(new SubmitFeedBackDialog.ClickListenerInterface() {
                @Override
                public void doCertificate() {
                    submitFeedBackDialog.dismiss();
                    closeActivity(PhoneCodeActivity.class,CommissionWithDrawActivity.class);
                }
            });
            submitFeedBackDialog.show();
        }else{
            ToastUtils.showShort("佣金提现失败！");
        }
    }
}
