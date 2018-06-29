package com.orientdata.lookforcustomers.view.findcustomer.impl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.WangrunBaseActivity;
import com.orientdata.lookforcustomers.bean.PayBean;
import com.orientdata.lookforcustomers.bean.TaskCountBean;
import com.orientdata.lookforcustomers.bean.TaskOut;
import com.orientdata.lookforcustomers.bean.WrResponse;
import com.orientdata.lookforcustomers.network.api.ApiManager;
import com.orientdata.lookforcustomers.network.api.BaseObserver;
import com.orientdata.lookforcustomers.network.api.ParamsUtil;
import com.orientdata.lookforcustomers.network.util.RxUtil;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.findcustomer.OrderDetailView;
import com.orientdata.lookforcustomers.view.findcustomer.TaskDetailActivity;
import com.orientdata.lookforcustomers.view.home.MainHomeActivity;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.orientdata.lookforcustomers.widget.dialog.ChargePopWindow;
import com.orientdata.lookforcustomers.widget.dialog.RechargeDialog;
import com.orientdata.lookforcustomers.wxapi.WXPayEntryActivity;
import com.qiniu.android.common.Constants;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 创建寻客-城市选择
 */
public class OrderConfirmActivity extends WangrunBaseActivity {


    @BindView(R.id.title)
    MyTitle title;
    @BindView(R.id.orderView)
    OrderDetailView orderView;
    @BindView(R.id.btn_submit)


    Button btnSubmit;
    @BindView(R.id.rl_content)
    RelativeLayout rlContent;
    private HashMap map;
    private double balance;//余额
    private String budget;


    //微信支付的回调
    //在ui线程执行
    @Subscribe
    public void onWeChatCharge(WXPayEntryActivity.wxPayResult payResultResult) {
        Logger.d("收到了微信支付的回调"+payResultResult);
        if (this == null)
            return;
        switch (payResultResult) {
            case success:
                submit();
                ToastUtils.showShort("支付成功");
                break;
            case cancle:
                ToastUtils.showShort("取消支付");
                break;
            case fail:
                ToastUtils.showShort("支付错误");
                break;
            case error:
                ToastUtils.showShort("支付错误");
                break;
            default:
                break;
        }
    }


    @Subscribe
    public void onZhifubaoCharge(PayBean payBean){
        Logger.d("收到支付宝支付成功的回调");
        //支付宝支付成功
        submit();
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
        ButterKnife.bind(this);
        title.setTitleName("订单确认");
        title.setImageBack(this);
        Intent intent = getIntent();

        map = (HashMap) intent.getSerializableExtra("map");

        budget = (String) map.get("budget");
        balance = intent.getDoubleExtra(Constants.balance, 0);

        TaskOut taskOut = (TaskOut) intent.getSerializableExtra("taskOut");

        orderView.hideTitle();
        orderView.setData(taskOut);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }


    public void showRechargeDialog() {
        ChargePopWindow chargePopWindow = new ChargePopWindow(this, (Double.parseDouble(budget) - balance));
        chargePopWindow.show();
    }


    //上传短信任务
    private void submit() {
        showDefaultLoading();
        HashMap<String, Object> map2 = ParamsUtil.getMap();
        map2.putAll(map);
        ApiManager.getInstence().getApiService().insertTask(ParamsUtil.getParams(map2))
                .compose(RxUtil.<WrResponse<TaskCountBean>>rxSchedulerHelper())
                .subscribe(new BaseObserver<TaskCountBean>() {
                    @Override
                    protected void onSuccees(WrResponse<TaskCountBean> t) {
                        hideDefaultLoading();
                        ToastUtils.showShort("创建成功");
                        showCreateTaskSucDialog(t.getResult().getTaskId());
                    }

                    @Override
                    protected void onCodeError(WrResponse<TaskCountBean> t) {
                        hideDefaultLoading();
                        super.onCodeError(t);
                        Logger.d("余额不足");
                        if (t.getErr().getCode() == -606) { //余额不足
                            showRechargeDialog();
                        }
                    }

                    @Override
                    protected void onFailure(String errorInfo, boolean isNetWorkError) {
                        hideDefaultLoading();
                        ToastUtils.showShort(errorInfo);
                    }
                });


    }


    //寻客订单创建成功
    private void showCreateTaskSucDialog(final int taskId) {
        final RechargeDialog rechargeDialog = new RechargeDialog(this, "寻客订单创建成功", "返回首页", "查看订单", R.mipmap.icon_dialog_success);
        rechargeDialog.setClickListenerInterface(new RechargeDialog.ClickListenerInterface() {
            @Override
            public void doCertificate() { //返回首页
                rechargeDialog.dismiss();
                Intent intent = new Intent(OrderConfirmActivity.this, MainHomeActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void doCancel() {  //查看订单
                rechargeDialog.dismiss();
                Intent intent = new Intent(OrderConfirmActivity.this, TaskDetailActivity.class);

                intent.putExtra("taskId",taskId);

                startActivity(intent);
                finish();
                // TODO: 2018/6/26  跳转到订单详情页面

            }
        });
        rechargeDialog.show();
    }


}
