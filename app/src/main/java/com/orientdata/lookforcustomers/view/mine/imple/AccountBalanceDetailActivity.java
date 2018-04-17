package com.orientdata.lookforcustomers.view.mine.imple;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.BalanceDetailBean;
import com.orientdata.lookforcustomers.bean.BalanceHistory;
import com.orientdata.lookforcustomers.event.BalanceDetailEvent;
import com.orientdata.lookforcustomers.presenter.CommissionPresent;
import com.orientdata.lookforcustomers.util.CommonUtils;
import com.orientdata.lookforcustomers.view.home.RechargeActivity;
import com.orientdata.lookforcustomers.view.mine.ICommissionView;
import com.orientdata.lookforcustomers.view.mine.TransactionType;
import com.orientdata.lookforcustomers.widget.MyTitle;

import org.greenrobot.eventbus.Subscribe;

/**
 * 账户余额详情页
 */

public class AccountBalanceDetailActivity extends BaseActivity<ICommissionView, CommissionPresent<ICommissionView>> implements ICommissionView,View.OnClickListener {
    private MyTitle title;
    private int balanceHistoryId;
    private TextView tvType,tvMoney,tvStatus,tvCreateDate,tvOrderNo,tvChannel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_balance_detail);
        initView();
        initTitle();
        mPresent.balanceDetail(balanceHistoryId);
    }

    @Override
    protected CommissionPresent<ICommissionView> createPresent() {
        return new CommissionPresent<>(this);
    }


    private void initView() {
        balanceHistoryId = getIntent().getIntExtra("balanceHistoryId",0);
        title = findViewById(R.id.my_title);
        tvType = findViewById(R.id.tvType);
        tvMoney = findViewById(R.id.tvMoney);
        tvStatus = findViewById(R.id.tvStatus);
        tvCreateDate = findViewById(R.id.tvCreateDate);
        tvOrderNo = findViewById(R.id.tvOrderNo);
        tvChannel = findViewById(R.id.tvChannel);
    }


    private void initTitle() {
        title.setImageBack(this);
        title.setTitleName("交易记录");
    }

    @Subscribe
    public void balanceDetail(BalanceDetailEvent balanceDetailEvent){
        BalanceDetailBean balanceDetailBean = balanceDetailEvent.balanceDetailBean;
        if(balanceDetailBean!=null) {
            //更新界面
            BalanceHistory bh = balanceDetailBean.getResult();
            if(bh!=null){
               updateView(bh);
            }

        }
    }
    private void updateView(BalanceHistory bh){
        String plus = "";
        if(bh.getTransactionType() == 201){
            tvType.setText("支出");
            tvStatus.setText("余额支出");
            plus = "-";
        }else{
            tvType.setText("充值");
            tvStatus.setText("已充入账户余额");
            tvType.setOnClickListener(this);
            plus = "+";
        }
        tvMoney.setText(plus+bh.getOccurMoney());
        tvCreateDate.setText(CommonUtils.getDateStr(bh.getCreateDate(),"yyyy年MM月dd日 HH:mm:ss"));
        tvOrderNo.setText(bh.getDetailNo());
        tvChannel.setText(TransactionType.getName(bh.getTransactionType()));
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvType:
                Intent intent = new Intent(this, RechargeActivity.class);
                startActivity(intent);
                break;
        }
    }
}
