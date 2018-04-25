package com.orientdata.lookforcustomers.view.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.WangrunBaseActivity;
import com.orientdata.lookforcustomers.bean.Invoice;
import com.orientdata.lookforcustomers.bean.InvoiceList;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;
import com.orientdata.lookforcustomers.util.DateTool;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.widget.MyTitle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

public class InvoiceHistoryActivity extends WangrunBaseActivity implements View.OnClickListener {
    private MyTitle title;
    private ListView mListView;
    private List<Invoice> mInvoiceLists;
    private MyInvoiceHistoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_history);
        mInvoiceLists = new ArrayList<Invoice>();
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
        map.put("userId", UserDataManeger.getInstance().getUserId());


        OkHttpClientManager.postAsyn(HttpConstant.SELECT_INVOICE_LIST, new OkHttpClientManager.ResultCallback<InvoiceList>() {
            @Override
            public void onError(Exception e) {
                ToastUtils.showShort(e.getMessage());
                hideDefaultLoading();
            }

            @Override
            public void onResponse(InvoiceList response) {
                hideDefaultLoading();
                if (response == null) {
                    ToastUtils.showShort("后台错误");
                    return;
                }
                if (response.getCode() == 0) {
                    if (response.getResult() == null) {
                        hideDefaultLoading();
                        return;
                    }

                    mInvoiceLists = response.getResult();
                    if (mAdapter == null) {

                        mAdapter = new MyInvoiceHistoryAdapter(getBaseContext());
                        mListView.setAdapter(mAdapter);
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        }, map);
    }


    private void initView() {
        title = findViewById(R.id.my_title);
        mListView = findViewById(R.id.lv_main);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(InvoiceHistoryActivity.this, InvoiceDetailActivity.class);
                intent.putExtra("data",mInvoiceLists.get(position));
                startActivity(intent);
            }
        });


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

    }

    class MyInvoiceHistoryAdapter extends BaseAdapter {
        private Context mContext;

        public MyInvoiceHistoryAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            int count = 0;
            if (null != mInvoiceLists) {
                count = mInvoiceLists.size();
            }
            return count;
        }

        @Override
        public Invoice getItem(int position) {
            Invoice item = null;

            if (null != mInvoiceLists) {
                item = mInvoiceLists.get(position);
            }

            return item;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (null == convertView) {
                viewHolder = new ViewHolder();
                LayoutInflater mInflater = LayoutInflater.from(mContext);
                convertView = mInflater.inflate(R.layout.item_invoice_history_list, null, false);
                viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                viewHolder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
                viewHolder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Invoice item = getItem(position);
            if (null != item) {
                viewHolder.tv_money.setText(item.getPrice() + "");
                //viewHolder.tv_status.setText(item.getStatus());
                //1纸质企业普票 2纸质企业专票 3纸质个人普票 4电子企业5电子个人
                switch (item.getType()) {
                    case 1:
                        viewHolder.tv_type.setText("纸质发票");
                        break;
                    case 2:
                        viewHolder.tv_type.setText("纸质发票");
                        break;
                    case 3:
                        viewHolder.tv_type.setText("纸质发票");
                        break;
                    case 4:
                        viewHolder.tv_type.setText("电子发票");
                        break;
                    case 5:
                        viewHolder.tv_type.setText("电子发票");
                        break;
                    default:
                        viewHolder.tv_type.setText("");
                        break;
                }
                Date date = item.getCreateDate();
                if (date != null) {
                    viewHolder.tv_time.setText(DateTool.parseDate2Str(date, "yyyy.MM.dd HH:mm"));
                } else {
                    viewHolder.tv_time.setText("");
                }
            }

            return convertView;
        }

        class ViewHolder {
            public TextView tv_money;
            public TextView tv_type;
            public TextView tv_time;
        }

    }


}
