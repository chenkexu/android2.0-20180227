package com.orientdata.lookforcustomers.view.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.WangrunBaseActivity;
import com.orientdata.lookforcustomers.bean.CanInvoiceBean;
import com.orientdata.lookforcustomers.bean.InvoiceBean;
import com.orientdata.lookforcustomers.bean.InvoiceDetail;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.util.DateTool;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.orientdata.lookforcustomers.widget.dialog.StringDialog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.OkHttpClientManager;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * 按照任务开发票-列表
 */
public class InvoiceTaskListActivity extends WangrunBaseActivity implements View.OnClickListener {
    private MyTitle title;
    private List<InvoiceBean> mInvoiceBeenLists;

    private ListView lv_show_invoices;
    private InvoicesListAdapter mAdapter;
    private InvoiceDetail mInvoiceDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_task_list);
        initView();
        initTitle();
        if (getIntent() != null) {
            mInvoiceDetail = (InvoiceDetail) getIntent().getSerializableExtra("data");
            mInvoiceBeenLists = mInvoiceDetail.getResult().getList();
            if (mAdapter == null) {
                mAdapter = new InvoicesListAdapter(InvoiceTaskListActivity.this);
                lv_show_invoices.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //getData();
    }

    private void getData() {
        showDefaultLoading();
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());


        OkHttpClientManager.postAsyn(HttpConstant.SELECT_CAN_INVOICES, new OkHttpClientManager.ResultCallback<CanInvoiceBean>() {
            @Override
            public void onError(Exception e) {
                ToastUtils.showShort(e.getMessage());
                hideDefaultLoading();
            }

            @Override
            public void onResponse(CanInvoiceBean response) {
                if (response.getCode() == 0) {

                    if (response.getResult() == null || response.getResult().size() <= 0) {
                        return;
                    }
                    mInvoiceBeenLists = response.getResult();


                    if (mAdapter == null) {
                        mAdapter = new InvoicesListAdapter(InvoiceTaskListActivity.this);
                        lv_show_invoices.setAdapter(mAdapter);
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }


                }
                hideDefaultLoading();
            }
        }, map);
    }


    private void initView() {
        title = findViewById(R.id.my_title);
        lv_show_invoices = findViewById(R.id.lv_show_invoices);
    }

    private void initTitle() {
        title.setTitleName("按任务开发票");
        title.setImageBack(this);
       /* title.setRightText("开票记录");
        title.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getBaseContext(), InvoiceHistoryActivity.class), 1);
            }
        });*/
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
        }
    }


    class InvoicesListAdapter extends BaseAdapter {

        private Context context = null;

        public InvoicesListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return mInvoiceBeenLists.size();
        }

        @Override
        public Object getItem(int position) {
            return mInvoiceBeenLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mHolder;
            if (convertView == null) {
                mHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.activity_invoice_list_item2, null, true);

                mHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                mHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                mHolder.tv_id = (TextView) convertView.findViewById(R.id.tv_id);
                mHolder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
                mHolder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            InvoiceBean item = mInvoiceBeenLists.get(position);
            //TODO
            if (item != null) {
                if (!TextUtils.isEmpty(item.getTaskName())) {
                    mHolder.tv_name.setText(item.getTaskName());
                }
                if (item.getThrowEnddate() != null && item.getThrowStartdate() != null) {
                    String format = "yyyy.MM.dd";
                    mHolder.tv_time.setText(DateTool.parseDate2Str(item.getThrowStartdate(), format)
                            + "-"
                            + DateTool.parseDate2Str(item.getThrowEnddate(), format));
                }

                if (!TextUtils.isEmpty(item.getTaskNo())) {
                    mHolder.tv_id.setText(item.getTaskNo());
                }
                switch (item.getInvoiceStatus()) {
                    case 1:
                        mHolder.tv_status.setText("已开票");
                        break;
                    case 3:
                        mHolder.tv_status.setText("申请中");
                        break;
                }
                if (null != item.getActualAmount()) {
                    mHolder.tv_money.setText(item.getActualAmount().toString() + "元");
                }

            }
            return convertView;
        }

        class ViewHolder {
            private TextView tv_name;
            private TextView tv_time;
            private TextView tv_id;
            private TextView tv_status;
            private TextView tv_money;
        }
    }


}
