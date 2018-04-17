package com.orientdata.lookforcustomers.view.mine.imple;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.Bh;
import com.orientdata.lookforcustomers.bean.CommissionListBean;
import com.orientdata.lookforcustomers.event.CommissionListEvent;
import com.orientdata.lookforcustomers.presenter.CommissionPresent;
import com.orientdata.lookforcustomers.util.DateTool;
import com.orientdata.lookforcustomers.view.home.fragment.MyAdapter;
import com.orientdata.lookforcustomers.view.mine.ICommissionView;
import com.orientdata.lookforcustomers.view.mine.MyCommissionListItemAdapter;
import com.orientdata.lookforcustomers.view.xlistview.XListView;
import com.orientdata.lookforcustomers.view.xlistview.XListViewFooter;
import com.orientdata.lookforcustomers.widget.DateSelectPopupWindow;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.orientdata.lookforcustomers.widget.dialog.DatePickerDialog;

import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;
import java.util.List;

/**
 * 佣金明细
 */
public class CommissionListActivity extends BaseActivity<ICommissionView, CommissionPresent<ICommissionView>> implements ICommissionView,XListView.IXListViewListener{
    private MyTitle title;
    private int page = 1;
    private int size = 10;
    private XListView xListView;
    private static List<Bh> bhList = null;
    private MyCommissionListItemAdapter adapter = null;
    private String date = "";
    private LinearLayout ll_no_content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission_list);
        initView();
        initTitle();
        mPresent.commissionList(date,page,size);
    }

    private void initView() {
        title = findViewById(R.id.my_title);
        xListView = findViewById(R.id.xListView);
        ll_no_content = findViewById(R.id.ll_no_content);
        xListView.setPullLoadEnable(true);
        xListView.setXListViewListener(this);
    }

    private void initTitle() {
        title.setTitleName("佣金明细");
        title.setImageBack(this);
        title.setRightImage(R.mipmap.blue_calendar, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择日期的dialog
                showDateFromDialog(v);
            }
        });
    }

    private String mNowDateText = "";
    private String startDateText = "1970-01-01";
    private String endDateText ;
    public static final String DEFAULT_DATA = "1970-01-01";
    public static final String DEFAULT_STR = "开始日期";
    public static final String DEFAULT_END = "结束日期";

    /**
     *日期选择
     * @param v
     */
    private void showDateFromDialog(View v) {
        final DatePickerDialog dialog = new DatePickerDialog(this,mNowDateText, startDateText, endDateText,DEFAULT_DATA);
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
                date = mNowDateText;
                page = 1;
                mPresent.commissionList(date,page,size);
            }

            @Override
            public void onDateCancel() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Subscribe
    public void commissionList(CommissionListEvent commissionListEvent){
        CommissionListBean commissionListBean = commissionListEvent.commissionListBean;
        // TODO: 2018/4/17 如果列表为空，就显示空内容状态
        if (commissionListBean.getResult()==null || commissionListBean.getResult().size()<=0){
            ll_no_content.setVisibility(View.VISIBLE);
            xListView.setVisibility(View.GONE);
        }else{
            ll_no_content.setVisibility(View.GONE);
            xListView.setVisibility(View.VISIBLE);
        }



        if(commissionListBean!=null ) {
            if(commissionListBean.isHasMore()){
                xListView.setLoadState(XListViewFooter.STATE_NORMAL);
            }else{
                xListView.setLoadState(XListViewFooter.STATE_NO_MORE);
            }
            if(page == 1){
                //refresh
                bhList = commissionListBean.getResult();
            }else{
                //loadMore
                bhList.addAll(bhList.size(),commissionListBean.getResult());
            }
        }

        adapter = new MyCommissionListItemAdapter(this,bhList);
        xListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        onLoad();
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
    protected CommissionPresent<ICommissionView> createPresent() {
        return new CommissionPresent<>(this);
    }

    @Override
    public void onRefresh() {
        if(bhList!=null){
            bhList.clear();
        }
        page = 1;
        mPresent.commissionList(date,page,size);
    }
    private void onLoad() {
        xListView.stopRefresh();
        xListView.stopLoadMore();
        xListView.setRefreshTime("刚刚");
    }

    @Override
    public void onLoadMore() {
        page++;
        mPresent.commissionList(date,page,size);
    }
}
