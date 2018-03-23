package com.orientdata.lookforcustomers.view.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.WangrunBaseActivity;
import com.orientdata.lookforcustomers.bean.CanInvoiceBean;
import com.orientdata.lookforcustomers.bean.InvoiceBean;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.util.DateTool;
import com.orientdata.lookforcustomers.util.SharedPreferencesTool;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.orientdata.lookforcustomers.widget.dialog.ConfirmDialog;
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
 * 发票
 */
public class InvoiceActivity extends WangrunBaseActivity implements View.OnClickListener {
    private MyTitle title;
    private List<InvoiceBean> mInvoiceBeenLists;
    private Map<Integer, Boolean> mMapChecked;
    private List<InvoiceBean> mStatus2InvoiceBeans;

    private ListView lv_show_invoices;
    private TextView tv_show_total;
    private TextView tv_explain;//开票说明
    private CheckBox cb_choose_all;
    private InvoicesListAdapter mAdapter;

    private TextView tv_next_step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        initView();
        initTitle();

    }

    @Override
    protected void onStart() {
        getData();
        super.onStart();
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
                        hideDefaultLoading();
                        cb_choose_all.setFocusable(false);
                        cb_choose_all.setClickable(false);
                        cb_choose_all.setFocusableInTouchMode(false);
                        return;
                    }
                    cb_choose_all.setFocusable(true);
                    cb_choose_all.setClickable(true);
                    cb_choose_all.setFocusableInTouchMode(true);
                    mInvoiceBeenLists = response.getResult();
                    if (mStatus2InvoiceBeans == null) {
                        mStatus2InvoiceBeans = new ArrayList<InvoiceBean>();
                    } else {
                        mStatus2InvoiceBeans.clear();
                    }
                    for (InvoiceBean bean : mInvoiceBeenLists) {
                        if (bean.getInvoiceStatus() == 2) {
                            mStatus2InvoiceBeans.add(bean);
                        }
                    }
                    if (mStatus2InvoiceBeans.size() <= 0) {
                        cb_choose_all.setFocusable(false);
                        cb_choose_all.setClickable(false);
                        cb_choose_all.setFocusableInTouchMode(false);
                    } else {
                        cb_choose_all.setFocusable(true);
                        cb_choose_all.setClickable(true);
                        cb_choose_all.setFocusableInTouchMode(true);
                    }
                    mMapChecked.clear();
                    for (int i = 0; i < mStatus2InvoiceBeans.size(); i++) {
                        mMapChecked.put(i, false);
                    }

                    if (mAdapter == null) {
                        mAdapter = new InvoicesListAdapter(InvoiceActivity.this);
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
        mMapChecked = new HashMap<Integer, Boolean>();
        title = findViewById(R.id.my_title);
        lv_show_invoices = findViewById(R.id.lv_show_invoices);
        Log.e("==", "jjjhj");
        lv_show_invoices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("==", "item click");

                mMapChecked.put(position, !mMapChecked.get(position));
                mAdapter.notifyDataSetChanged();
                int count = 0;
                if (mMapChecked.size() > 0) {
                    BigDecimal total = new BigDecimal(0);
                    for (Map.Entry<Integer, Boolean> entry : mMapChecked.entrySet()) {
                        if (entry.getValue()) {
                            count++;
                            InvoiceBean bean = mStatus2InvoiceBeans.get(entry.getKey());
                            total = total.add(bean.getActualAmount());
                        }
                    }
                    tv_show_total.setText(count + "个任务 共" + total.toString().trim() + "元");
                    if (count == mMapChecked.size()) {
                        if (!cb_choose_all.isChecked()) {
                            cb_choose_all.setChecked(true);
                        }
                    }
                    if (count == 0) {
                        if (cb_choose_all.isChecked()) {
                            cb_choose_all.setChecked(false);
                        }
                    }
                } else {
                    tv_show_total.setText("0个任务 共0元");
                }


            }
        });
        tv_show_total = findViewById(R.id.tv_show_total);
        tv_explain = findViewById(R.id.tv_explain);
        tv_explain.setOnClickListener(this);
        cb_choose_all = findViewById(R.id.cb_choose_all);
        cb_choose_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mStatus2InvoiceBeans == null || mStatus2InvoiceBeans.size() <= 0) {
                    return;
                }
                mMapChecked.clear();
                for (int i = 0; i < mStatus2InvoiceBeans.size(); i++) {
                    mMapChecked.put(i, isChecked);
                }
                mAdapter.notifyDataSetChanged();

                int count = 0;
                if (mMapChecked.size() > 0) {
                    BigDecimal total = new BigDecimal(0);
                    for (Map.Entry<Integer, Boolean> entry : mMapChecked.entrySet()) {
                        if (entry.getValue()) {
                            count++;
                            InvoiceBean bean = mStatus2InvoiceBeans.get(entry.getKey());
                            total = total.add(bean.getActualAmount());
                        }
                    }
                    tv_show_total.setText(count + "个任务 共" + total.toString().trim() + "元");
//                    if (count == mMapChecked.size()) {
//                        if (!cb_choose_all.isChecked()) {
//                            cb_choose_all.setChecked(true);
//                        }
//                    }
//                    if (count == 0) {
//                        if (cb_choose_all.isChecked()) {
//                            cb_choose_all.setChecked(false);
//                        }
//                    }
                } else {
                    tv_show_total.setText("0个任务 共0元");
                }


            }
        });
        tv_next_step = findViewById(R.id.tv_next_step);
        tv_next_step.setOnClickListener(this);

    }

    private void initTitle() {
        title.setTitleName("按任务开发票");
        title.setImageBack(this);
        title.setRightText("开票记录");
        title.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getBaseContext(), InvoiceHistoryActivity.class), 1);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.tv_explain:
                showDialog();
                break;
            case R.id.tv_next_step:
                String taskIds = "";
                BigDecimal money = new BigDecimal(0);
                for (Map.Entry<Integer, Boolean> entry : mMapChecked.entrySet()) {
                    if (entry.getValue()) {
                        InvoiceBean bean = mStatus2InvoiceBeans.get(entry.getKey());
                        if (TextUtils.isEmpty(taskIds)) {
                            taskIds = taskIds + bean.getTaskId();
                        } else {
                            taskIds = taskIds + "," + bean.getTaskId();
                        }
                        money = money.add(bean.getActualAmount());
                    }
                }
                if (TextUtils.isEmpty(taskIds)) {
                    ToastUtils.showShort("请勾选要开发票的任务");
                    return;
                }
                intent = new Intent(this, InvoiceNextStepActivity.class);
                intent.putExtra("money", money);
                intent.putExtra("taskIds", taskIds);
                startActivity(intent);
                break;
        }
    }

    private void showDialog() {
        String str = "1、如您选择开具电子发票，寻客将电子发票推送至您的邮箱。\n" +
                "2、如您选择开具纸质发票，寻客将为您安排发票打印并寄出，邮费到付。我们强烈建议您选择开具环保方便的电子发票。\n" +
                "3、如您开具的增值税普通发票用于企业报销，根据国家税务相关政策要求请填写纳税人识别号；如您的单位是非企业性质或您要求开具个人名义抬头，您可以选择“个人”的开票选项，则无需填写纳税人识别号。\n" +
                "4、开票金额为您的任务实际消费金额，多个任务可以合并开一张发票；根据国家税收相关法规规定，发票内容为“信息服务费”。 \n" +
                "5、使用优惠部分（含：优惠券、抵扣券、赠送金额等）不计入实际开票金额。\n" +
                "6、请您提供真实准确的开票信息，如您错填发票抬头信息需要进行修改，请您及时联系寻客客服人员为您处理；如果发票已经寄出，还请您收到发票后立即跟客服人员联系。\n" +
                "7、如有疑问，请致电寻客客服电话：4000-9222-72。";
        final StringDialog dialog = new StringDialog(this, str);
        dialog.show();
        dialog.setCancelable(true);
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
                convertView = inflater.inflate(R.layout.activity_invoice_list_item, null, true);

                mHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
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
                    case 2:
                        mHolder.tv_status.setText("未开票");
                        break;
                }
                if (null != item.getActualAmount()) {
                    mHolder.tv_money.setText(item.getActualAmount().toString() + "元");
                }
                mHolder.checkBox.setChecked(mMapChecked.get(position));
//                mHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        mMapChecked.put(position, isChecked);
//                        int count = 0;
//                        if (mMapChecked.size() > 0) {
//                            BigDecimal total = new BigDecimal(0);
//                            for (Map.Entry<Integer, Boolean> entry : mMapChecked.entrySet()) {
//                                if (entry.getValue()) {
//                                    count++;
//                                    InvoiceBean bean = mStatus2InvoiceBeans.get(entry.getKey());
//                                    total = total.add(bean.getActualAmount());
//                                }
//                            }
//                            tv_show_total.setText(count + "个任务 共" + total.toString().trim() + "元");
//                            if (count == mMapChecked.size()) {
//                                if (!cb_choose_all.isChecked()) {
//                                    cb_choose_all.setChecked(true);
//                                }
//                            }
//                            if (count == 0) {
//                                if (cb_choose_all.isChecked()) {
//                                    cb_choose_all.setChecked(false);
//                                }
//                            }
//                        } else {
//                            tv_show_total.setText("0个任务 共0元");
//                        }
//
//                    }
//                });

            }
            return convertView;
        }

        class ViewHolder {
            private CheckBox checkBox;
            private TextView tv_name;
            private TextView tv_time;
            private TextView tv_id;
            private TextView tv_status;
            private TextView tv_money;
        }
    }


}
