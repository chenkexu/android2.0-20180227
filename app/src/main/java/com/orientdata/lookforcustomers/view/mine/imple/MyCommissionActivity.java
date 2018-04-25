package com.orientdata.lookforcustomers.view.mine.imple;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.URLBean;
import com.orientdata.lookforcustomers.event.MyMoneyEvent;
import com.orientdata.lookforcustomers.event.UpdateToBalanceEvent;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;
import com.orientdata.lookforcustomers.presenter.CommissionPresent;
import com.orientdata.lookforcustomers.runtimepermissions.PermissionsManager;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.mine.ICommissionView;
import com.orientdata.lookforcustomers.view.mine.ShareToGetCommissionActivity;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.orientdata.lookforcustomers.widget.dialog.ConfirmDialog;

import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * 我的佣金
 */

public class MyCommissionActivity extends BaseActivity<ICommissionView, CommissionPresent<ICommissionView>> implements ICommissionView, View.OnClickListener {
    private MyTitle title;
    private TextView tvRemindWithdraw;
    private TextView tvCommissionWithdraw, tvEarnCommission, tvTransferTobalance;
    private String moreMoney;
    private double commission;
    private int commission1 = -1;
    private String subCount;
    private TextView tvCommission;
    private double myCommission;
    private String upMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_commission);
        initView();
        initTitle();


    }

    @Override
    protected CommissionPresent<ICommissionView> createPresent() {
        return new CommissionPresent<>(this);
    }


    private void initView() {
        commission = getIntent().getDoubleExtra("commission", 0);
        if(commission%1 == 0){
            commission1 = (int)commission;
        }else{
            BigDecimal b = new BigDecimal(commission);
            commission = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        subCount = getIntent().getStringExtra("subCount");
        moreMoney = getIntent().getStringExtra("moreMoney");
        upMoney = getIntent().getStringExtra("upMoney");

        title = findViewById(R.id.my_title);
        tvCommission = findViewById(R.id.tvCommission);
        tvRemindWithdraw = findViewById(R.id.tvRemindWithdraw);
        tvCommissionWithdraw = findViewById(R.id.tvCommissionWithdraw);
        tvEarnCommission = findViewById(R.id.tvEarnCommission);
        tvTransferTobalance = findViewById(R.id.tvTransferTobalance);
        tvCommissionWithdraw.setOnClickListener(this);
        tvEarnCommission.setOnClickListener(this);
        tvTransferTobalance.setOnClickListener(this);
        updateView();
    }

    private void updateView() {
        String str = "佣金超过" + moreMoney + "元可提现，今日还可提现" + subCount + "次";
        tvRemindWithdraw.setText(str);
        if(commission1 == -1){
            tvCommission.setText(commission + "元");
        }else{
            tvCommission.setText(commission1 + "元");
        }
        //如果佣金余额为0则“转入余额”置灰不可点击
        tvTransferTobalance.setEnabled(true);
//        if(commission == 0){
//            tvTransferTobalance.setEnabled(false);
//        }
        //如佣金余额小于最小可提现金额或可提现次数为“0”则“佣金提现”置灰不可点击
        tvCommissionWithdraw.setEnabled(true);

        if(subCount == null || (subCount!=null && Integer.parseInt(subCount) == 0) || moreMoney==null ){
            tvCommissionWithdraw.setEnabled(false);
        }
    }

    private void initTitle() {
        title.setTitleName("我的佣金");
        title.setImageBack(this);
        title.setRightText("查看明细");
        title.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), CommissionListActivity.class));
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCommissionWithdraw:
                //佣金提现
//                if(commission<Integer.parseInt(moreMoney)){
//                    ToastUtils.showShort("余额不足,无法提现！");
//                    return;
//                }
                Intent intent = new Intent(this, CommissionWithDrawActivity.class);
                intent.putExtra("moreMoney", moreMoney);
                intent.putExtra("commission", commission);
                intent.putExtra("subCount", subCount);
                intent.putExtra("myCommission", myCommission);

                intent.putExtra("upMoney", upMoney);

                startActivity(intent);
                break;
            case R.id.tvEarnCommission:
                if (hasSharePermisson()){
                    //赚取佣金 获取Url
                    showDefaultLoading();
                    MDBasicRequestMap map = new MDBasicRequestMap();
                    map.put("userId", UserDataManeger.getInstance().getUserId());
                    OkHttpClientManager.postAsyn(HttpConstant.CREATE_URL, new OkHttpClientManager.ResultCallback<URLBean>() {
                        @Override
                        public void onError(Exception e) {
                            ToastUtils.showShort(e.getMessage());
                            hideDefaultLoading();
                        }

                        @Override
                        public void onResponse(URLBean re) {
                            if (re.getCode() == 0) {
                                String url = re.getResult();
                                if (!TextUtils.isEmpty(url)){
                                    Intent intent1 = new Intent(MyCommissionActivity.this, ShareToGetCommissionActivity.class);
                                    intent1.putExtra("url", url);
                                    startActivity(intent1);
                                }else {
                                    ToastUtils.showShort("获取分享链接失败");
                                }
                            }
                            hideDefaultLoading();
                        }
                    }, map);
                }else {
                    requestSharePermission();
                }

                break;
            case R.id.tvTransferTobalance:
                //转入余额
                showConfirmDialog();
                break;
        }

    }

    private void showConfirmDialog() {
        final ConfirmDialog confirmDialog = new ConfirmDialog(this, "确定转入余额？", "确定");
        confirmDialog.setClickListenerInterface(new ConfirmDialog.ClickListenerInterface() {
            @Override
            public void doCancel() {
                confirmDialog.dismiss();
            }

            @Override
            public void doConfirm() {
                confirmDialog.dismiss();
                //转入金额
                mPresent.updateToBalance();
            }
        });
        confirmDialog.show();
    }
    @Subscribe
    public void refreshResult(MyMoneyEvent moneyEvent){
        if(moneyEvent!=null){
            commission = moneyEvent.commission;
            subCount = moneyEvent.subCount;
            tvCommission.setText(getNum(commission)+"元");
            String str = "佣金超过" + moreMoney + "元可提现，今日还可提现" + subCount + "次";
            tvRemindWithdraw.setText(str);
        }
    }
    public String getNum(double num){
        if(num % 1 == 0){
            return (int)num+"";
        }else{
            BigDecimal b = new BigDecimal(num);
            return (b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue())+"";
        }
    }

    @Subscribe
    public void updateToBalance(UpdateToBalanceEvent updateToBalanceEvent) {
        if (updateToBalanceEvent.err.getCode() == 0) {
            mPresent.getCommission();
            ToastUtils.showShort("提现成功");
        } else {
            ToastUtils.showShort("提现失败");
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
     * 是否有拍照的权限
     */
    @TargetApi(23)
    public boolean hasSharePermisson() {
        boolean b1 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        boolean b2 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission.ACCESS_WIFI_STATE);
        boolean b3 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission.READ_PHONE_STATE);
        boolean b4 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean b5 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        boolean b6 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission.INTERNET);
        boolean b7 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        boolean b8 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        boolean b9 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
        return b1 && b2 && b3 && b4 && b5 && b6 && b7 && b8 && b9;
    }

    /**
     * 请求拍照的权限
     */
    @TargetApi(23)
    public void requestSharePermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS},
                0);
    }
}
