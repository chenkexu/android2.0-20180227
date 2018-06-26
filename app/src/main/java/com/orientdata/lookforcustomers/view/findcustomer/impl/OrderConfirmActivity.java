package com.orientdata.lookforcustomers.view.findcustomer.impl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.WangrunBaseActivity;
import com.orientdata.lookforcustomers.bean.ErrBean;
import com.orientdata.lookforcustomers.bean.TaskOut;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.findcustomer.OrderDetailView;
import com.orientdata.lookforcustomers.view.home.MainHomeActivity;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.orientdata.lookforcustomers.widget.dialog.RechargeDialog;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
        ButterKnife.bind(this);
        title.setTitleName("订单确认");
        title.setImageBack(this);
        Intent intent = getIntent();
        final HashMap map = (HashMap) intent.getSerializableExtra("map");

        MDBasicRequestMap mdBasicRequestMap = new MDBasicRequestMap();
        mdBasicRequestMap.putAll(map);

        TaskOut taskOut = (TaskOut) intent.getSerializableExtra("taskOut");

        orderView.hideTitle();
        orderView.setData(taskOut);





        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //上传短信任务
                showDefaultLoading();
                OkHttpClientManager.postAsyn(HttpConstant.INSERT_CREATE_TASK2, new OkHttpClientManager.ResultCallback<ErrBean>() {
                    @Override
                    public void onError(Exception e) {
                        hideDefaultLoading();
                        ToastUtils.showShort(e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
                    }

                    @Override
                    public void onResponse(ErrBean response) {
                        hideDefaultLoading();
                        ToastUtils.showShort("创建成功");
                        showCreateTaskSucDialog();

                    }
                }, map);

            }
        });
    }



    //寻客订单创建成功
    private void showCreateTaskSucDialog() {
        final RechargeDialog rechargeDialog = new RechargeDialog(this, "寻客订单创建成功", "返回首页","查看订单",R.mipmap.icon_dialog_success);
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
                // TODO: 2018/6/26  跳转到订单详情页面

            }
        });

        rechargeDialog.show();

    }




}
