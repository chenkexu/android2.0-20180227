package com.orientdata.lookforcustomers.view.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.WangrunBaseActivity;
import com.orientdata.lookforcustomers.bean.ErrBean;
import com.orientdata.lookforcustomers.bean.Invoice;
import com.orientdata.lookforcustomers.bean.InvoiceDetail;
import com.orientdata.lookforcustomers.bean.InvoiceList;
import com.orientdata.lookforcustomers.bean.InvoiceOut;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.util.DateTool;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.widget.MyTitle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.OkHttpClientManager;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * 发票详情
 */
public class InvoiceDetailActivity extends WangrunBaseActivity implements View.OnClickListener {
    private MyTitle title;
    private TextView tv_name;
    private TextView tv_address;
    private TextView tv_phone;
    private TextView tv_id;
    private TextView tv_title;
    private TextView tv_time;
    private TextView tv_content;
    private TextView tv_money;
    private RelativeLayout rl_tasks;
    private Invoice mInvoice;
    private TextView tv_tasks;
    private TextView tv_email;
    private TextView tv_send;

    private RelativeLayout rl_e_invoice;//电子发票的框。
    private LinearLayout ll_p_invoice_name;
    private LinearLayout ll_p_invoice_address;
    private LinearLayout ll_p_invoice_phone;
    private InvoiceDetail mInvoiceDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_detail);
        if (getIntent() != null) {
            mInvoice = (Invoice) getIntent().getSerializableExtra("data");
        }
        initView();
        initTitle();
        //getData();


    }

    @Override
    protected void onStart() {
        super.onStart();
        //getData(mPage, mSize);、
        getData();
    }


    private void getData() {
        showDefaultLoading();
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId() + "");
        map.put("invoiceId", mInvoice.getInvoiceId() + "");


        OkHttpClientManager.postAsyn(HttpConstant.SELECT_USER_INVOICE_INFO, new OkHttpClientManager.ResultCallback<InvoiceDetail>() {
            @Override
            public void onError(Exception e) {
                ToastUtils.showShort(e.getMessage());
                hideDefaultLoading();
            }

            @Override
            public void onResponse(InvoiceDetail res) {
                hideDefaultLoading();
                if (res == null) {
                    ToastUtils.showShort("后台错误");
                    return;
                }
                if (res.getCode() == 0) {
                    mInvoiceDetail = res;
                    InvoiceOut bean = res.getResult();
                    if (bean == null) {
                        return;
                    }
                    if (bean.getType() == 1 || bean.getType() == 2 || bean.getType() == 3) {
                        ll_p_invoice_address.setVisibility(View.VISIBLE);
                        ll_p_invoice_name.setVisibility(View.VISIBLE);
                        ll_p_invoice_phone.setVisibility(View.VISIBLE);
                        rl_e_invoice.setVisibility(View.GONE);
                        tv_name.setText(bean.getAddresser());
                        tv_address.setText(bean.getAddress());
                        tv_phone.setText(bean.getPhone());
                        //纸质发票
                    } else {
                        //电子发票
                        ll_p_invoice_address.setVisibility(View.GONE);
                        ll_p_invoice_name.setVisibility(View.GONE);
                        ll_p_invoice_phone.setVisibility(View.GONE);
                        rl_e_invoice.setVisibility(View.VISIBLE);
                        tv_email.setText(bean.getEmail());
                        tv_send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showDefaultLoading();
                                MDBasicRequestMap map = new MDBasicRequestMap();
                                map.put("userId", UserDataManeger.getInstance().getUserId() + "");
                                map.put("invoiceId", mInvoice.getInvoiceId() + "");


                                OkHttpClientManager.postAsyn(HttpConstant.UPDATE_INVOICE_AGAIN, new OkHttpClientManager.ResultCallback<ErrBean>() {
                                    @Override
                                    public void onError(Exception e) {
                                        ToastUtils.showShort(e.getMessage());
                                        hideDefaultLoading();
                                    }

                                    @Override
                                    public void onResponse(ErrBean bean) {
                                        hideDefaultLoading();
                                        if (bean == null) {
                                            ToastUtils.showShort("后台错误");
                                            return;
                                        }
                                        if (bean.getCode() == 0) {
                                            ToastUtils.showShort("发送成功");
                                        }
                                    }
                                }, map);
                            }
                        });

                    }
                    tv_title.setText(bean.getLookUp());
                    tv_id.setText(bean.getTaxpayerIdentificationNo());
                    tv_money.setText(bean.getPrice() + "");
                    tv_content.setText(bean.getContent());
                    if (bean.getCreateDate() != null) {
                        tv_time.setText(DateTool.parseDate2Str(bean.getCreateDate(), "yyyy.MM.dd HH:mm"));
                    }
                    if (bean.getList() != null) {
                        tv_tasks.setText("1张发票，含" + bean.getList().size() + "个任务");
                    }


                }
            }
        }, map);
    }


    private void initView() {
        title = findViewById(R.id.my_title);
        tv_address = findViewById(R.id.tv_address);
        tv_name = findViewById(R.id.tv_name);
        tv_phone = findViewById(R.id.tv_phone);
        tv_title = findViewById(R.id.tv_title);
        tv_id = findViewById(R.id.tv_id);
        tv_content = findViewById(R.id.tv_content);
        tv_money = findViewById(R.id.tv_money);
        tv_time = findViewById(R.id.tv_time);
        rl_tasks = findViewById(R.id.rl_tasks);
        rl_tasks.setOnClickListener(this);
        tv_tasks = findViewById(R.id.tv_tasks);
        tv_email = findViewById(R.id.tv_email);
        tv_send = findViewById(R.id.tv_send);

        ll_p_invoice_address = findViewById(R.id.ll_p_invoice_address);
        ll_p_invoice_name = findViewById(R.id.ll_p_invoice_name);
        ll_p_invoice_phone = findViewById(R.id.ll_p_invoice_phone);
        rl_e_invoice = findViewById(R.id.rl_e_invoice);


    }

    private void initTitle() {
        title.setTitleName("开票记录");
        title.setImageBack(this);
       /* title.setRightText("开票记录");
        title.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivityForResult(new Intent(getBaseContext(), InvoiceHistoryActivity.class), 1);
            }
        });*/
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_tasks:
                Intent intent = new Intent(this, InvoiceTaskListActivity.class);
                intent.putExtra("data", mInvoiceDetail);
                startActivity(intent);
                break;
        }

    }
}
