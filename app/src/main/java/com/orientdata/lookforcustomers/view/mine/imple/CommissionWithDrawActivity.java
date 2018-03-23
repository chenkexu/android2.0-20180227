package com.orientdata.lookforcustomers.view.mine.imple;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.base.WangrunBaseActivity;
import com.orientdata.lookforcustomers.event.CommissionVertificateEvent;
import com.orientdata.lookforcustomers.event.MyMoneyEvent;
import com.orientdata.lookforcustomers.presenter.CommissionPresent;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.mine.ICommissionView;
import com.orientdata.lookforcustomers.view.mine.imple.PhoneCodeActivity;
import com.orientdata.lookforcustomers.widget.MyTitle;

import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;

/**
 * 佣金提现
 */
public class CommissionWithDrawActivity extends BaseActivity<ICommissionView, CommissionPresent<ICommissionView>> implements ICommissionView,View.OnClickListener {
    private MyTitle title;

    private  String moreMoney;
    private double commission;
    private int commission1 = -1;
    private String subCount;
    private TextView tvCommissionMoney,tvAllMoneyCommission,tvNext,tvRemind;
    private EditText etMoneyCommission,etPayAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission_withdraw);
        initView();
        initTitle();
        updateView();
    }

    @Override
    protected CommissionPresent<ICommissionView> createPresent() {
        return new CommissionPresent<>(this);
    }


    private void initView() {
        commission = getIntent().getDoubleExtra("commission",0);
        if(commission%1 == 0){
            commission1 = (int)commission;
        }else{
            BigDecimal b = new BigDecimal(commission);
            commission = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        subCount = getIntent().getStringExtra("subCount");
        moreMoney = getIntent().getStringExtra("moreMoney");
        title = findViewById(R.id.my_title);
        tvRemind = findViewById(R.id.tvRemind);
        tvCommissionMoney = findViewById(R.id.tvCommissionMoney);
        tvAllMoneyCommission = findViewById(R.id.tvAllMoneyCommission);
        tvNext = findViewById(R.id.tvNext);
        etMoneyCommission = findViewById(R.id.etMoneyCommission);
        etPayAccount = findViewById(R.id.etPayAccount);
        tvNext.setOnClickListener(this);
        tvAllMoneyCommission.setOnClickListener(this);
    }
    private void updateView(){
        String str = "佣金超过"+moreMoney+"元可提现，今日还可提现"+subCount+"次";
        if(commission1 == -1){
            tvCommissionMoney.setText(commission + "元");
        }else{
            tvCommissionMoney.setText(commission1 + "元");
        }
        tvRemind.setText(str);
        tvNext.setEnabled(true);
        if(moreMoney!=null && subCount != null){
            if( Integer.parseInt(moreMoney) > commission || Integer.parseInt(subCount) <1){
                tvNext.setEnabled(false);
            }
        }
    }

    private void initTitle() {
        title.setTitleName("佣金提现");
        title.setImageBack(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvNext:
                goToNext();
                break;
            case R.id.tvAllMoneyCommission:
                if(commission1 == -1){
                    etMoneyCommission.setText(commission+"");
                }else{
                    etMoneyCommission.setText(commission1+"");
                }
                break;
        }
    }
    String payAccount = "";
    String moneyCommission = "";

    /**
     * 下一步验证
     */
    private void goToNext(){
        moneyCommission = etMoneyCommission.getText().toString();
        payAccount = etPayAccount.getText().toString();

        if(TextUtils.isEmpty(moneyCommission)){
            ToastUtils.showShort("请输入金额");
            return;
        }
        if(TextUtils.isEmpty(payAccount)){
            ToastUtils.showShort("请输入支付宝账号");
            return;
        }

        if(Double.parseDouble(moneyCommission)<Integer.parseInt(moreMoney)){
            ToastUtils.showShort("提现金额需大于"+moreMoney+"元");
            return;
        }
        if(Double.parseDouble(moneyCommission) > commission){
            ToastUtils.showShort("所输金额大于佣金金额！");
            return;
        }
        vertificate();
    }

    /**
     * 下一步验证 验证成功跳转验证码页
     */
    private void vertificate(){
        mPresent.commissionVertificate(BigDecimal.valueOf(Double.parseDouble(moneyCommission)),payAccount);
    }
    @Subscribe
    public void commissionVertificate(CommissionVertificateEvent commissionVertificateEvent){
        if(commissionVertificateEvent.err.getCode() == 0){
            Intent intent = new Intent(this, PhoneCodeActivity.class);
            intent.putExtra("zhiFu",payAccount);
            intent.putExtra("commission",moneyCommission);
            intent.putExtra("myCommission",tvCommissionMoney.getText().toString());
            startActivity(intent);
        }
    }
    @Subscribe
    public void refreshResult(MyMoneyEvent moneyEvent){
        if(moneyEvent!=null){
            Log.e("==","佣金提现收到了 更新");
            tvCommissionMoney.setText(getNum(moneyEvent.commission)+"元");
        }
    }
    public String getNum(double num){
        if(commission % 1 == 0){
            return (int)commission+"";
        }else{
            BigDecimal b = new BigDecimal(commission);
            return (b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue())+"";

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
}
