package com.orientdata.lookforcustomers.view.mine.imple;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.Bh;
import com.orientdata.lookforcustomers.bean.CommissionListBean;
import com.orientdata.lookforcustomers.event.BalanceListEvent;
import com.orientdata.lookforcustomers.presenter.CommissionPresent;
import com.orientdata.lookforcustomers.util.CommonUtils;
import com.orientdata.lookforcustomers.util.DateTool;
import com.orientdata.lookforcustomers.view.mine.BalanceListItemAdapter;
import com.orientdata.lookforcustomers.view.mine.ICommissionView;
import com.orientdata.lookforcustomers.view.mine.MyCommissionListItemAdapter;
import com.orientdata.lookforcustomers.view.xlistview.XListView;
import com.orientdata.lookforcustomers.view.xlistview.XListViewFooter;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.orientdata.lookforcustomers.widget.dialog.DatePickerDialog;

import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户余额--
 */

public class AccountBalanceActivity extends BaseActivity<ICommissionView, CommissionPresent<ICommissionView>> implements ICommissionView, View.OnClickListener, XListView.IXListViewListener {
    private MyTitle title;
    private double balance;
    private int balance1 = -1;
    private double frozenAmount;
    private int frozenAmount1 = -1;
    private TextView tvMoney, tvFrozenMoney;
    private XListView xListView; //交易记录
    private ImageView ivCalendar;
    private int page = 1;
    private int size = 10;
    private String searchDate = "";
    private BalanceListItemAdapter adapter = null;
    private static List<Bh> bhList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_balance);
        initView();
        initTitle();
        updateView();
        mPresent.balanceList(searchDate, page, size);
    }

    @Override
    protected CommissionPresent<ICommissionView> createPresent() {
        return new CommissionPresent<>(this);
    }


    private void initView() {
        balance = getIntent().getDoubleExtra("balance", 0);
      /*  if(balance%1==0){
            balance1 = (int)balance;
        }else{
            BigDecimal b = new BigDecimal(balance);
            balance = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
        }*/
        frozenAmount = getIntent().getDoubleExtra("frozenAmount", 0);
       /* if(frozenAmount%1==0){
            frozenAmount1 = (int)frozenAmount;
        }else{
            BigDecimal b = new BigDecimal(frozenAmount);
            frozenAmount = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
        }*/
        title = findViewById(R.id.my_title);
        tvMoney = findViewById(R.id.tvMoney);
        tvFrozenMoney = findViewById(R.id.tvFrozenMoney);
        xListView = findViewById(R.id.xListView);
        ivCalendar = findViewById(R.id.ivCalendar);
        ivCalendar.setOnClickListener(this);
        xListView.setPullLoadEnable(true);
        xListView.setXListViewListener(this);
        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(bhList!=null){
                    Intent intent = new Intent(getBaseContext(), AccountBalanceDetailActivity.class);
                    intent.putExtra("balanceHistoryId", bhList.get(position - 1).getBalanceHistoryId());
                    startActivity(intent);
                }
            }
        });
    }

    private void updateView() {
        /*if(balance1==-1){
            tvMoney.setText("￥"+balance);
        }else{
            tvMoney.setText("￥"+balance1);
        }
        if(frozenAmount1 == -1){
            tvFrozenMoney.setText("冻结金额(元)："+frozenAmount);
        }else{
            tvFrozenMoney.setText("冻结金额(元)："+frozenAmount1);
        }*/
        tvMoney.setText("￥" + CommonUtils.formatFloatNumber(balance));
        tvFrozenMoney.setText("冻结金额(元)：" + CommonUtils.formatFloatNumber(frozenAmount));
    }


    private void initTitle() {
        title.setImageBack(this);
        title.setTitleName("账户余额");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivCalendar:
                showDatePicker();
                break;
        }

    }

    @Subscribe
    public void balanceList(BalanceListEvent balanceListEvent) {
        CommissionListBean commissionListBean = balanceListEvent.balanceListBean;
        if (commissionListBean != null) {
            if (commissionListBean.isHasMore()) {
                xListView.setLoadState(XListViewFooter.STATE_NORMAL);
            } else {
                xListView.setLoadState(XListViewFooter.STATE_NO_MORE);
            }
            if (page == 1) {
                //refresh
                bhList = commissionListBean.getResult();
            } else {
                //loadMore
                bhList.addAll(bhList.size(), commissionListBean.getResult());
            }
        }
        adapter = new BalanceListItemAdapter(this, bhList);
        xListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        onLoad();
    }

    private void onLoad() {
        xListView.stopRefresh();
        xListView.stopLoadMore();
        xListView.setRefreshTime("刚刚");
    }

    private String mNowDateText = "";
    private String startDateText = "1970-01-01";
    private String endDateText;
    public static final String DEFAULT_DATA = "1970-01-01";
    public static final String DEFAULT_STR = "开始日期";
    public static final String DEFAULT_END = "结束日期";

    /**
     * 时间选择
     */
    private void showDatePicker() {
        final DatePickerDialog dialog = new DatePickerDialog(this, mNowDateText, startDateText, endDateText, DEFAULT_DATA);
        dialog.setOnDateSelectListener(new DatePickerDialog.OnDateSelectListener() {
            @Override
            public void onDateSelect(int year, int monthOfYear, int dayOfMonth) {
                dialog.dismiss();
                if (year == 0 && monthOfYear == 0 && dayOfMonth == 0) {
                    if (mNowDateText.trim().equals("") || mNowDateText.trim().equals(DEFAULT_STR)) {
                        mNowDateText = DateTool.getChinaDate();
                    }
                } else {
                    mNowDateText = DateTool.getChinaDateFromCalendar(year, monthOfYear, dayOfMonth);
                }
                searchDate = mNowDateText;
                page = 1;
                mPresent.balanceList(searchDate, page, size);
            }

            @Override
            public void onDateCancel() {
                dialog.dismiss();
            }
        });
        dialog.show();
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
    public void onRefresh() {
        if (bhList != null) {
            bhList.clear();
        }
        page = 1;
        mPresent.balanceList(searchDate, page, size);
    }

    @Override
    public void onLoadMore() {
        page++;
        mPresent.balanceList(searchDate, page, size);

    }
}
