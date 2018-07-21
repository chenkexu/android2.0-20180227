package com.orientdata.lookforcustomers.view.home;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.ExcelUrl;
import com.orientdata.lookforcustomers.bean.Report;
import com.orientdata.lookforcustomers.bean.ReportListBean;
import com.orientdata.lookforcustomers.event.ReportDataEvent;
import com.orientdata.lookforcustomers.event.ReportUrlEvent;
import com.orientdata.lookforcustomers.presenter.ReportPresent;
import com.orientdata.lookforcustomers.share.ShareEntity;
import com.orientdata.lookforcustomers.share.ShareManager;
import com.orientdata.lookforcustomers.util.CommonUtils;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.home.fragment.ReportStatus;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.orientdata.lookforcustomers.widget.graph.MyChartView;

import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import vr.md.com.mdlibrary.utils.DisplayUtil;

/**
 * Created by wy on 2017/10/30.
 * 报表Fragment
 */
public class ReportActivity extends BaseActivity<IReportView, ReportPresent<IReportView>> implements IReportView, MyChartView.ClickListener, View.OnClickListener {
    private TabLayout tabs;
    private String[] mTitleList;
    String chooseText = "最近七天";
    private String today = "";
    private MyTitle title;

    //所有数据
    private List<Report> reportYesterDay = null; //昨天
    private List<Report> reportLatestSeven = null; //最近七天
    private List<Report> reportLastWeek = null; //上周
    private List<Report> reportMonth = null; //本月
    private List<Report> reportLastMonth = null;//上个月

    //昨日
    private String[] yesterDayX = null; //横轴上显示的数字
    private String[] yesterDayXText = null;

    private int[] yesterDayMoneyY = null;
    private int[] yesterDayPageConY = null;
    private int[] yesterDayPageDisplayY = null;
    private int[] yesterDayPageClickY = null;
    private int[] yesterDayMsgConY = null;
    private int[] yesterDayMsgIssuedY = null;

    //最近7天
    private String[] latestSevenMoneyX = null;
    private String[] latestSevenMoneyXText = null;

    private int[] latestSevenMoneyY = null;
    private int[] latestSevenPageConY = null;
    private int[] latestSevenPageDisplayY = null;
    private int[] latestSevenPageClickY = null;
    private int[] latestSevenMsgConY = null;
    private int[] latestSevenMsgIssuedY = null;

    //上周
    private String[] lastWeekMoneyX = null;
    private String[] lastWeekMoneyXText = null;

    private int[] lastWeekMoneyY = null;
    private int[] lastWeekPageConY = null;
    private int[] lastWeekPageDisplayY = null;
    private int[] lastWeekPageClickY = null;
    private int[] lastWeekMsgConY = null;
    private int[] lastWeekMsgIssuedY = null;

    //本月
    private String[] monthMoneyX = null;
    private String[] monthMoneyXAllText = null;
    private List<String> monthMoneyXList = null;
    private List<String> monthMoneyXAllList = null; //本月的日期

    private int[] monthMoneyY = null;
    private int[] monthPageConY = null;
    private int[] monthPageDisplayY = null;
    private int[] monthPageClickY = null;
    private int[] monthMsgConY = null;
    private int[] monthMsgIssuedY = null;

    //上个月
    private String[] lastMonthMoneyX = null;
    private String[] lastMonthMoneyXAllText = null;
    private List<String> lastMonthMoneyXList = null;
    private List<String> lastMonthMoneyXAllList = null;

    private int[] lastMonthMoneyY = null;
    private int[] lastMonthPageConY = null;
    private int[] lastMonthPageDisplayY = null;
    private int[] lastMonthPageClickY = null;
    private int[] lastMonthMsgConY = null;
    private int[] lastMonthMsgIssuedY = null;

    private RelativeLayout curveRl;
    private LinearLayout linearPageCon, linearPageDisplay, linearPageClick, linearMsgCon, linearMsgIssued, linearCurTotalData;
    private TextView tvPageCon, tvPageDisplayNum, tvPageClickNum, tvMsgConNum, tvMsgIssuedNum;
    private TextView tvPageCon1, tvPageDisplayNum1, tvPageClickNum1, tvMsgConNum1, tvMsgIssuedNum1;
    private TextView tvTotal, tvMsgConsumption, tvMsgIssued, tvPageConsumption, tvPageDisplay, tvPageClick, tvDate;
    private int chooseType = 2;//默认最近七天
    private String shareUrl = "";

    private ShareEntity shareEntity;
    private int screenWidth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.fragment_report,null);
        setContentView(view);
        initView(view);
        initTab();
        initTitle();
        updateData();
        screenWidth = DisplayUtil.getPortraitScreenWidth(this);
    }

//    @Override
//    public View onCreate(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        initView(view);
//        initTab();
//        initTitle();
////        ImmersionBar.setTitleBar(getActivity(), title);
//        updateData();
//        screenWidth = DisplayUtil.getPortraitScreenWidth(getActivity());
//        return view;
//    }

    public void updateData(boolean isReset) {
        tabs.getTabAt(1).select();
        updateData();
        updateView();

    }
    public void updateData(){
        getData(1);
        getData(2);
        getData(3);
        getData(4);
        getData(5);
    }

    private void getData(int type) {
        getData(type, false);
    }

    private void initView(View view) {
        tabs = view.findViewById(R.id.tabs);
        title = view.findViewById(R.id.title);
        curveRl = view.findViewById(R.id.curveRl);
        linearPageCon = view.findViewById(R.id.linearPageCon);
        linearPageDisplay = view.findViewById(R.id.linearPageDisplay);
        linearCurTotalData = view.findViewById(R.id.linearCurTotalData);
        linearPageClick = view.findViewById(R.id.linearPageClick);
        linearMsgCon = view.findViewById(R.id.linearMsgCon);
        linearMsgIssued = view.findViewById(R.id.linearMsgIssued);
        tvPageCon = view.findViewById(R.id.tvPageCon);
        tvPageCon1 = view.findViewById(R.id.tvPageCon1);
        tvPageDisplayNum = view.findViewById(R.id.tvPageDisplayNum);
        tvPageDisplayNum1 = view.findViewById(R.id.tvPageDisplayNum1);
        tvPageClickNum = view.findViewById(R.id.tvPageClickNum);
        tvPageClickNum1 = view.findViewById(R.id.tvPageClickNum1);
        tvMsgConNum = view.findViewById(R.id.tvMsgConNum);
        tvMsgConNum1 = view.findViewById(R.id.tvMsgConNum1);
        tvMsgIssuedNum = view.findViewById(R.id.tvMsgIssuedNum);
        tvMsgIssuedNum1 = view.findViewById(R.id.tvMsgIssuedNum1);
        tvTotal = view.findViewById(R.id.tvTotal);
        tvMsgConsumption = view.findViewById(R.id.tvMsgConsumption);
        tvMsgIssued = view.findViewById(R.id.tvMsgIssued);
        tvPageConsumption = view.findViewById(R.id.tvPageConsumption);
        tvPageDisplay = view.findViewById(R.id.tvPageDisplay);
        tvPageClick = view.findViewById(R.id.tvPageClick);
        tvDate = view.findViewById(R.id.tvDate);

        linearPageCon.setOnClickListener(this);
        linearPageDisplay.setOnClickListener(this);
        linearPageClick.setOnClickListener(this);
        linearMsgCon.setOnClickListener(this);
        linearMsgIssued.setOnClickListener(this);
    }

    MyChartView latestSevenMoneyView = null;
    MyChartView latestSevenPageConView = null;
    MyChartView latestSevenPageDisplayView = null;
    MyChartView latestSevenPageClickView = null;
    MyChartView latestSevenMsgConView = null;
    MyChartView latestSevenMsgIssuedView = null;

    MyChartView lastWeekMoneyView = null;
    MyChartView lastWeekPageConView = null;
    MyChartView lastWeekPageDisplayView = null;
    MyChartView lastWeekPageClickView = null;
    MyChartView lastWeekMsgConView = null;
    MyChartView lastWeekMsgIssuedView = null;

    MyChartView monthMoneyView = null;
    MyChartView monthPageConView = null;
    MyChartView monthPageDisplayView = null;
    MyChartView monthPageClickView = null;
    MyChartView monthMsgConView = null;
    MyChartView monthMsgIssuedView = null;

    MyChartView lastMonthMoneyView = null;
    MyChartView lastMonthPageConView = null;
    MyChartView lastMonthPageDisplayView = null;
    MyChartView lastMonthPageClickView = null;
    MyChartView lastMonthMsgConView = null;
    MyChartView lastMonthMsgIssuedView = null;

    MyChartView yesturdayMoneyView = null;
    MyChartView yesturdayPageConView = null;
    MyChartView yesturdayPageDisplayView = null;
    MyChartView yesturdayPageClickView = null;
    MyChartView yesturdayMsgConView = null;
    MyChartView yesturdayMsgIssuedView = null;

    private int moneyColor = R.color.c_18C8E6;
    private int pageConColor = R.color.c_53CAC3;
    private int pageDisplayColor = R.color.c_FFA6A8;
    private int pageClickColor = R.color.c_888888;
    private int msgConColor = R.color.c_FDA32C;
    private int msgIssuedColor = R.color.c_C3ABDF;

    private Context getContext(){
        return this;
    }


    private void initChartView() {
        curveRl.removeAllViews();
        if (chooseType == 1) {  //昨日
            int maxY = maxResult(yesterDayMoneyY, yesterDayPageConY, yesterDayPageDisplayY, yesterDayPageClickY, yesterDayMsgConY, yesterDayMsgIssuedY);
            Logger.d("more:"+maxY);
            yesturdayMoneyView = new MyChartView(getContext(), yesterDayX, "", yesterDayMoneyY, moneyColor,maxY);
            yesturdayMoneyView.setClickListener(this);
            curveRl.addView(yesturdayMoneyView);
            yesturdayMoneyView.startDrawLine(0);

            yesturdayPageConView = new MyChartView(getContext(), yesterDayX, "", yesterDayPageConY, pageConColor,maxY);
            yesturdayPageConView.setClickListener(this);
            yesturdayPageConView.startDrawLine(0);
            yesturdayPageConView.setId(R.id.yesterday_line_pagecon);

            yesturdayPageDisplayView = new MyChartView(getContext(), yesterDayX, "", yesterDayPageDisplayY, pageDisplayColor,maxY);
            yesturdayPageDisplayView.setClickListener(this);
            yesturdayPageDisplayView.startDrawLine(0);
            yesturdayPageDisplayView.setId(R.id.yesterday_line_pagedisplay);

            yesturdayPageClickView = new MyChartView(getContext(), yesterDayX, "", yesterDayPageClickY, pageClickColor,maxY);
            yesturdayPageClickView.setClickListener(this);
            yesturdayPageClickView.startDrawLine(0);
            yesturdayPageClickView.setId(R.id.yesterday_line_pageclick);

            yesturdayMsgConView = new MyChartView(getContext(), yesterDayX, "", yesterDayMsgConY, msgConColor,maxY);
            yesturdayMsgConView.setClickListener(this);
            yesturdayMsgConView.startDrawLine(0);
            yesturdayMsgConView.setId(R.id.yesterday_line_msgcon);

            yesturdayMsgIssuedView = new MyChartView(getContext(), yesterDayX, "", yesterDayMsgIssuedY, msgIssuedColor,maxY);
            yesturdayMsgIssuedView.setClickListener(this);
            yesturdayMsgIssuedView.startDrawLine(0);
            yesturdayMsgIssuedView.setId(R.id.yesterday_line_msgissued);
        } else if (chooseType == 2) { //最近七天
            int maxY = maxResult(latestSevenMoneyY, latestSevenPageConY, latestSevenPageDisplayY, latestSevenPageClickY, latestSevenMsgConY, latestSevenMsgIssuedY);
            Logger.d("more:"+maxY);
            latestSevenMoneyView = new MyChartView(getContext(), latestSevenMoneyX, "", latestSevenMoneyY, moneyColor,maxY);
            latestSevenMoneyView.setClickListener(this);
            curveRl.addView(latestSevenMoneyView);
            latestSevenMoneyView.startDrawLine(0);

            latestSevenPageConView = new MyChartView(getContext(), latestSevenMoneyX, "", latestSevenPageConY, pageConColor,maxY);
            latestSevenPageConView.setClickListener(this);
            latestSevenPageConView.startDrawLine(0);
            latestSevenPageConView.setId(R.id.latestseven_line_pagecon);

            latestSevenPageDisplayView = new MyChartView(getContext(), latestSevenMoneyX, "", latestSevenPageDisplayY, pageDisplayColor,maxY);
            latestSevenPageDisplayView.setClickListener(this);
            latestSevenPageDisplayView.startDrawLine(0);
            latestSevenPageDisplayView.setId(R.id.latestseven_line_pagedisplay);

            latestSevenPageClickView = new MyChartView(getContext(), latestSevenMoneyX, "", latestSevenPageClickY, pageClickColor,maxY);
            latestSevenPageClickView.setClickListener(this);
            latestSevenPageClickView.startDrawLine(0);
            latestSevenPageClickView.setId(R.id.latestseven_line_pageclick);

            latestSevenMsgConView = new MyChartView(getContext(), latestSevenMoneyX, "", latestSevenMsgConY, msgConColor,maxY);
            latestSevenMsgConView.setClickListener(this);
            latestSevenMsgConView.startDrawLine(0);
            latestSevenMsgConView.setId(R.id.latestseven_line_msgcon);

            latestSevenMsgIssuedView = new MyChartView(getContext(), latestSevenMoneyX, "", latestSevenMsgIssuedY, msgIssuedColor,maxY);
            latestSevenMsgIssuedView.setClickListener(this);
            latestSevenMsgIssuedView.startDrawLine(0);
            latestSevenMsgIssuedView.setId(R.id.latestseven_line_msgissued);

        } else if (chooseType == 3) {  //上周
            int maxY = maxResult(lastWeekMoneyY, lastWeekPageConY, lastWeekPageDisplayY, lastWeekPageClickY, lastWeekMsgConY, lastWeekMsgIssuedY);
            Logger.d("more:"+maxY);
            lastWeekMoneyView = new MyChartView(getContext(), lastWeekMoneyX, "", lastWeekMoneyY, moneyColor,maxY);
            lastWeekMoneyView.setClickListener(this);
            curveRl.addView(lastWeekMoneyView);
            lastWeekMoneyView.startDrawLine(0);

            lastWeekPageConView = new MyChartView(getContext(), lastWeekMoneyX, "", lastWeekPageConY, pageConColor,maxY);
            lastWeekPageConView.setClickListener(this);
            lastWeekPageConView.startDrawLine(0);
            lastWeekPageConView.setId(R.id.lastweek_line_pagecon);

            lastWeekPageDisplayView = new MyChartView(getContext(), lastWeekMoneyX, "", lastWeekPageDisplayY, pageDisplayColor,maxY);
            lastWeekPageDisplayView.setClickListener(this);
            lastWeekPageDisplayView.startDrawLine(0);
            lastWeekPageDisplayView.setId(R.id.lastweek_line_pagedisplay);

            lastWeekPageClickView = new MyChartView(getContext(), lastWeekMoneyX, "", lastWeekPageClickY, pageClickColor,maxY);
            lastWeekPageClickView.setClickListener(this);
            lastWeekPageClickView.startDrawLine(0);
            lastWeekPageClickView.setId(R.id.lastweek_line_pageclick);

            lastWeekMsgConView = new MyChartView(getContext(), lastWeekMoneyX, "", lastWeekMsgConY, msgConColor,maxY);
            lastWeekMsgConView.setClickListener(this);
            lastWeekMsgConView.startDrawLine(0);
            lastWeekMsgConView.setId(R.id.lastweek_line_msgcon);

            lastWeekMsgIssuedView = new MyChartView(getContext(), lastWeekMoneyX, "", lastWeekMsgIssuedY, msgIssuedColor,maxY);
            lastWeekMsgIssuedView.setClickListener(this);
            lastWeekMsgIssuedView.startDrawLine(0);
            lastWeekMsgIssuedView.setId(R.id.lastweek_line_msgissued);

        } else if (chooseType == 4) {  //本月
            int maxY = maxResult(monthMoneyY, monthPageConY, monthPageDisplayY, monthPageClickY, monthMsgConY, monthMsgIssuedY);
            Logger.d("more:"+maxY);
            monthMoneyView = new MyChartView(getContext(), monthMoneyX, "", monthMoneyY, moneyColor,maxY);
            monthMoneyView.setClickListener(this);
            curveRl.addView(monthMoneyView);
            monthMoneyView.startDrawLine(0);

            monthPageConView = new MyChartView(getContext(), monthMoneyX, "", monthPageConY, pageConColor,maxY);
            monthPageConView.setClickListener(this);
            monthPageConView.startDrawLine(0);
            monthPageConView.setId(R.id.month_line_pagecon);

            monthPageDisplayView = new MyChartView(getContext(), monthMoneyX, "", monthPageDisplayY, pageDisplayColor,maxY);
            monthPageDisplayView.setClickListener(this);
            monthPageDisplayView.startDrawLine(0);
            monthPageDisplayView.setId(R.id.month_line_pagedisplay);

            monthPageClickView = new MyChartView(getContext(), monthMoneyX, "", monthPageClickY, pageClickColor,maxY);
            monthPageClickView.setClickListener(this);
            monthPageClickView.startDrawLine(0);
            monthPageClickView.setId(R.id.month_line_pageclick);

            monthMsgConView = new MyChartView(getContext(), monthMoneyX, "", monthMsgConY, msgConColor,maxY);
            monthMsgConView.setClickListener(this);
            monthMsgConView.startDrawLine(0);
            monthMsgConView.setId(R.id.month_line_msgcon);

            monthMsgIssuedView = new MyChartView(getContext(), monthMoneyX, "", monthMsgIssuedY, msgIssuedColor,maxY);
            monthMsgIssuedView.setClickListener(this);
            monthMsgIssuedView.startDrawLine(0);
            monthMsgIssuedView.setId(R.id.month_line_msgissued);

        } else if (chooseType == 5) {  //上个月
            //数组合并
            int maxY = maxResult(lastMonthMoneyY, lastMonthPageConY, lastMonthPageDisplayY, lastMonthPageClickY, lastMonthMsgConY, lastMonthMsgIssuedY);
            Logger.d("more:"+maxY);


            lastMonthMoneyView = new MyChartView(getContext(), lastMonthMoneyX, "", lastMonthMoneyY, moneyColor,maxY);
            lastMonthMoneyView.setClickListener(this);
            curveRl.addView(lastMonthMoneyView); //
            lastMonthMoneyView.startDrawLine(0);



            lastMonthPageConView = new MyChartView(getContext(), lastMonthMoneyX, "", lastMonthPageConY, pageConColor,maxY);
            lastMonthPageConView.setClickListener(this);
            lastMonthPageConView.startDrawLine(0);
            lastMonthPageConView.setId(R.id.lastmonth_line_pagecon);

            lastMonthPageDisplayView = new MyChartView(getContext(), lastMonthMoneyX, "", lastMonthPageDisplayY, pageDisplayColor,maxY);
            lastMonthPageDisplayView.setClickListener(this);
            lastMonthPageDisplayView.startDrawLine(0);
            lastMonthPageDisplayView.setId(R.id.lastmonth_line_pagedisplay);

            lastMonthPageClickView = new MyChartView(getContext(), lastMonthMoneyX, "", lastMonthPageClickY, pageClickColor,maxY);
            lastMonthPageClickView.setClickListener(this);
            lastMonthPageClickView.startDrawLine(0);
            lastMonthPageClickView.setId(R.id.lastmonth_line_pageclick);

            lastMonthMsgConView = new MyChartView(getContext(), lastMonthMoneyX, "", lastMonthMsgConY, msgConColor,maxY);
            lastMonthMsgConView.setClickListener(this);
            lastMonthMsgConView.startDrawLine(0);
            lastMonthMsgConView.setId(R.id.lastmonth_line_msgcon);


//            Logger.d(lastMonthMsgIssuedY);
            lastMonthMsgIssuedView = new MyChartView(getContext(), lastMonthMoneyX, "", lastMonthMsgIssuedY, msgIssuedColor,maxY);
            lastMonthMsgIssuedView.setClickListener(this);
            lastMonthMsgIssuedView.startDrawLine(0);
            lastMonthMsgIssuedView.setId(R.id.lastmonth_line_msgissued);

        }

    }


    //求数组的最大值和最小值
    private int maxY(int[] A){
        int i,min,max;
        min=max=A[0];
        System.out.print("数组A的元素包括：");
        for(i=0;i<A.length;i++) {
            System.out.print(A[i]+" ");
            if(A[i]>max)   // 判断最大值
                max=A[i];
            if(A[i]<min)   // 判断最小值
                min=A[i];
        }
        Logger.e("Y轴最大值为："+max);
        return max;
    }

    /**
     * 初始化Tab标签
     */
    private void initTab() {
        mTitleList = new String[]{getString(R.string.yesterday), getString(R.string.latest_seven), getString(R.string.last_week)
                , getString(R.string.this_month), getString(R.string.last_month)};//页卡标题集合
        tabs.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        tabs.addTab(tabs.newTab().setText(mTitleList[0]));
        tabs.addTab(tabs.newTab().setText(mTitleList[1]));
        tabs.addTab(tabs.newTab().setText(mTitleList[2]));
        tabs.addTab(tabs.newTab().setText(mTitleList[3]));
        tabs.addTab(tabs.newTab().setText(mTitleList[4]));
        setTabPadding();
        tabs.getTabAt(1).select();
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                chooseText = tab.getText().toString();
                chooseType = ReportStatus.getIndex(chooseText);
                //清空按钮选中状态 重新获取数据
                clearBtnClick();
                if (isNeedGetData(chooseType)) {
                    getData(chooseType, true);
                } else {
                    //更新数据汇总
                    initChartView();
                    updateTotalData();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * 更新下面的总数据
     */
    private void updateTotalData() {
        List<Report> reports = null;
        if (chooseType == 1) {
            reports = reportYesterDay;
        } else if (chooseType == 2) {
            reports = reportLatestSeven;
        } else if (chooseType == 3) {
            reports = reportLastWeek;
        } else if (chooseType == 4) {
            reports = reportMonth;
        } else if (chooseType == 5) {
            reports = reportLastMonth;
        }
        if (reports != null) {
            int size = reports.size();
            tvPageCon.setText(reports.get(size - 1).getPagemoney() + "");
            tvPageDisplayNum.setText(reports.get(size - 1).getShowCount() + "");
            tvPageClickNum.setText(reports.get(size - 1).getClickCount() + "");
            tvMsgConNum.setText(reports.get(size - 1).getDuanxinmoney() + "");
            //设置汇总数据
            tvMsgIssuedNum.setText(reports.get(size - 1).getSuccCount() + "");
        }
    }

    private void initTitle() {
        title.setTitleName("报表");
        title.setRightText("导出报表");
        title.setImageBack(this);
        title.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // 分享
                mPresent.getReportUrl(chooseType, "", 2);
            }
        });
    }



    private boolean isNeedGetData(int type) {
        if (type == 1 && reportYesterDay == null) {
            return true;
        } else if (type == 2 && reportLatestSeven == null) {
            return true;
        } else if (type == 3 && reportLastWeek == null) {
            return true;
        } else if (type == 4 && reportMonth == null) {
            return true;
        } else if (type == 5 && reportLastMonth == null) {
            return true;
        }
        return false;
    }

    private void getData(int type, boolean isShowLoading) {
        mPresent.getReportData(type, today, isShowLoading);
    }

    /**
     * 清除tab的内间距，避免因为固定内间距导致长的文字被压缩
     * 但是在字体大的时候会明显看出tab之间的间距不一致。所以用这个方法将tab文字变成大小一致后，字体大小酌情设置。
     */
    public void setTabPadding() {
        Class tablayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tablayout.getDeclaredField("mTabStrip");
            tabStrip.setAccessible(true);
            LinearLayout ll_tab = (LinearLayout) tabStrip.get(tabs);
            for (int i = 0; i < ll_tab.getChildCount(); i++) {
                View child = ll_tab.getChildAt(i);
                child.setPadding(0, 0, 0, 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                child.setLayoutParams(params);
                child.invalidate(); // 这个方法是重画
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    //报表数据返回
    @Subscribe
    public void reportListResult(ReportDataEvent reportDataEvent) {
        ReportListBean reportListBean = reportDataEvent.reportListBean;
        if(reportListBean!=null){
            if(reportListBean.getCode() == 500 || reportListBean.getResult()==null || isOnlyTotalData(reportListBean.getResult())){
                //未认证 黑名单用户、list是空、只返回汇总数据 造假数据
                List<Report> list = new ArrayList<>();
                for(int i=0;i<8;i++){
                    Report report = new Report();
                    report.setMoney(new BigDecimal(0));
                    report.setDuanxinmoney(new BigDecimal(0));
                    report.setSuccCount(0);
                    report.setPagemoney(new BigDecimal(0));
                    report.setShowCount(0);
                    report.setClickCount(0);
                    report.setName(getPastDate(7-i));
                    list.add(report);
                }
                setData(reportDataEvent.type,list,true);
            }else{
                setData(reportDataEvent.type, reportListBean.getResult(),false);
            }
        }
    }

    /**
     * 是否只有汇总数据
     * @return
     */
    private boolean isOnlyTotalData(List<Report> list){
        if(list!= null && list.size() == 1){
            Report report = list.get(0);
            if(!TextUtils.isEmpty(report.getName()) && report.getName().contains("汇总数据")){
                return true;
            }
        }

        return false;
    }

    private void setData(int type, List<Report> list,boolean isErro) {
        if (type == 1) { //昨天的数据
            reportYesterDay = list;
        } else if (type == 2) { //最近七天的数据
            reportLatestSeven = list;
        } else if (type == 3) { //上周的数据
            reportLastWeek = list;
        } else if (type == 4) { //本月的数据
            reportMonth = list;
        } else if (type == 5) { //上个月的数据
            reportLastMonth = list;
        }
        setData1(type, list,isErro);
        if (chooseType == type) {
            updateTotalData();
            initChartView();
        }
    }



    private void setData1(int type, List<Report> list,boolean isErro) {
        if (list != null) {
            initString(type, list.size() - 1, list,isErro);
        }
    }

    private void initString(int type, int size, List<Report> list,boolean isErro) {
        if (type == 1) { // //昨天的数据
            if (yesterDayX == null) {
                yesterDayX = new String[size];
                yesterDayXText = new String[size];

                yesterDayMoneyY = new int[size];
                yesterDayPageConY = new int[size];
                yesterDayPageDisplayY = new int[size];
                yesterDayPageClickY = new int[size];
                yesterDayMsgConY = new int[size];
                yesterDayMsgIssuedY = new int[size];
            }
        } else if (type == 2) {  //最近七天的数据
            if (latestSevenMoneyX == null) {
                latestSevenMoneyX = new String[size];
                latestSevenMoneyXText = new String[size];

                latestSevenMoneyY = new int[size];
                latestSevenPageConY = new int[size];
                latestSevenPageDisplayY = new int[size];
                latestSevenPageClickY = new int[size];
                latestSevenMsgConY = new int[size];
                latestSevenMsgIssuedY = new int[size];
            }

        } else if (type == 3) {  //上周的数据
            if (lastWeekMoneyX == null) {
                lastWeekMoneyX = new String[size];
                lastWeekMoneyXText = new String[size];

                lastWeekMoneyY = new int[size];
                lastWeekPageConY = new int[size];
                lastWeekPageDisplayY = new int[size];
                lastWeekPageClickY = new int[size];
                lastWeekMsgConY = new int[size];
                lastWeekMsgIssuedY = new int[size];
            }

        } else if (type == 4) {  //本月的数据
            if (monthMoneyXList == null) {
                monthMoneyXList = new ArrayList<>();
                monthMoneyXAllList = new ArrayList<>();

                monthMoneyY = new int[size];
                monthPageConY = new int[size];
                monthPageDisplayY = new int[size];
                monthPageClickY = new int[size];
                monthMsgConY = new int[size];
                monthMsgIssuedY = new int[size];
            }

        } else if (type == 5) { //上个月
            if (lastMonthMoneyXList == null) {
                lastMonthMoneyXList = new ArrayList<>();
                lastMonthMoneyXAllList = new ArrayList<>();

                lastMonthMoneyY = new int[size];
                lastMonthPageConY = new int[size];
                lastMonthPageDisplayY = new int[size];
                lastMonthPageClickY = new int[size];
                lastMonthMsgConY = new int[size];
                lastMonthMsgIssuedY = new int[size];
            }

        }
        if(monthMoneyXList!=null){
            monthMoneyXList.clear();
        }
        if(lastMonthMoneyXList != null){
            lastMonthMoneyXList.clear();
        }
        if(monthMoneyXAllList!=null){
            monthMoneyXAllList.clear();
        }
        if(lastMonthMoneyXAllList != null){
            lastMonthMoneyXAllList.clear();
        }
        for (int i = 0; i < size; i++) {
            Report report = list.get(i);
            if(!isErro){
                setReportData(type, report, i, size);
//                getDataString(type);
            }else{
                setReportData1(type, report, i, size);
            }
        }
        getDataString(type);
    }

    private void getDataString(int type) {
        if (type == 4) {
            if (monthMoneyXList != null) {
                int size = monthMoneyXList.size();
                int sizeAll = monthMoneyXAllList.size();
                if(monthMoneyX !=null){
                    monthMoneyX = null;
                }
                if(monthMoneyXAllText !=null){
                    monthMoneyXAllText = null;
                }
                monthMoneyX = new String[size];
                monthMoneyXAllText = new String[sizeAll];
                for (int i = 0; i < size; i++) {
                    monthMoneyX[i] = getData(monthMoneyXList.get(i));
                }
                for (int i = 0; i < sizeAll; i++) {
//                    monthMoneyXAllText[i] = getDataText(monthMoneyXAllList.get(i));
                    monthMoneyXAllText[i] = monthMoneyXAllList.get(i);
                }
            }
        } else if (type == 5) {
            if (lastMonthMoneyXList != null) {
                int size = lastMonthMoneyXList.size();
                int sizeAll = lastMonthMoneyXAllList.size();
                lastMonthMoneyX = new String[size];
                lastMonthMoneyXAllText = new String[sizeAll];
                for (int i = 0; i < size; i++) {
                    lastMonthMoneyX[i] = getData(lastMonthMoneyXList.get(i));
                }
                for (int i = 0; i < sizeAll; i++) {
//                    lastMonthMoneyXAllText[i] = getDataText(lastMonthMoneyXAllList.get(i));
                    lastMonthMoneyXAllText[i] = lastMonthMoneyXAllList.get(i);
                }
            }
        }
    }

    private void setReportData(int type, Report report, int index, int size) {
        if (type == 1) {  //昨日
            yesterDayX[index] = getData(report.getName());
//            yesterDayXText[index] = getDataText(report.getName());
            yesterDayXText[index] = report.getName();
            yesterDayMoneyY[index] = getIntNum(report.getMoney());
            yesterDayPageConY[index] = getIntNum(report.getPagemoney());
            yesterDayPageDisplayY[index] = report.getShowCount();
            yesterDayPageClickY[index] = report.getClickCount();
            yesterDayMsgConY[index] = getIntNum(report.getDuanxinmoney());
            yesterDayMsgIssuedY[index] = report.getSuccCount();
        } else if (type == 2) {
            latestSevenMoneyX[index] = getData(report.getName());
//            latestSevenMoneyXText[index] = getDataText(report.getName());
            latestSevenMoneyXText[index] = report.getName();
            latestSevenMoneyY[index] = getIntNum(report.getMoney());
            latestSevenPageConY[index] = getIntNum(report.getPagemoney());
            latestSevenPageDisplayY[index] = report.getShowCount();
            latestSevenPageClickY[index] = report.getClickCount();
            latestSevenMsgConY[index] = getIntNum(report.getDuanxinmoney());
            // TODO: 2018/4/20 写错了把，短信下发量。。
            latestSevenMsgIssuedY[index] = report.getSuccCount();
//            latestSevenMsgIssuedY[index] = getIntNum(report.getDuanxinmoney());

        } else if (type == 3) {
            lastWeekMoneyX[index] = getData(report.getName());
//            lastWeekMoneyXText[index] = getDataText(report.getName());
            lastWeekMoneyXText[index] = report.getName();
            lastWeekMoneyY[index] = getIntNum(report.getMoney());
            lastWeekPageConY[index] = getIntNum(report.getPagemoney());
            lastWeekPageDisplayY[index] = report.getShowCount();
            lastWeekPageClickY[index] = report.getClickCount();
            lastWeekMsgConY[index] = getIntNum(report.getDuanxinmoney());
            lastWeekMsgIssuedY[index] = report.getSuccCount();
        } else if (type == 4) {
            if (index == 0 || index == 14 || index == size - 1) {
                monthMoneyXList.add(report.getName());
            }
            monthMoneyXAllList.add(report.getName());

            monthMoneyY[index] = getIntNum(report.getMoney());
            monthPageConY[index] = getIntNum(report.getPagemoney());
            monthPageDisplayY[index] = report.getShowCount();
            monthPageClickY[index] = report.getClickCount();
            monthMsgConY[index] = getIntNum(report.getDuanxinmoney());
            monthMsgIssuedY[index] = report.getSuccCount();
        } else if (type == 5) {
            if (index == 0 || index == 14 || index == size - 1) {
                lastMonthMoneyXList.add(report.getName());
            }
            lastMonthMoneyXAllList.add(report.getName());
            lastMonthMoneyY[index] = getIntNum(report.getMoney());
            lastMonthPageConY[index] = getIntNum(report.getPagemoney());
            lastMonthPageDisplayY[index] = report.getShowCount();
            lastMonthPageClickY[index] = report.getClickCount();
            lastMonthMsgConY[index] = getIntNum(report.getDuanxinmoney());
            lastMonthMsgIssuedY[index] = report.getSuccCount();
        }
    }
    private void setReportData1(int type, Report report, int index, int size) {
        if (type == 1) {
            yesterDayX[index] = getData(report.getName());
//            yesterDayXText[index] = getDataText(report.getName());
            yesterDayXText[index] = report.getName();
            yesterDayMoneyY[index] = getIntNum(report.getMoney());
            yesterDayPageConY[index] = getIntNum(report.getPagemoney());
            yesterDayPageDisplayY[index] = report.getShowCount();
            yesterDayPageClickY[index] = report.getClickCount();
            yesterDayMsgConY[index] = getIntNum(report.getDuanxinmoney());
            yesterDayMsgIssuedY[index] = report.getSuccCount();
        } else if (type == 2) {
            latestSevenMoneyX[index] = getData(report.getName());
//            latestSevenMoneyXText[index] = getDataText(report.getName());
            latestSevenMoneyXText[index] = report.getName();
            latestSevenMoneyY[index] = getIntNum(report.getMoney());
            latestSevenPageConY[index] = getIntNum(report.getPagemoney());
            latestSevenPageDisplayY[index] = report.getShowCount();
            latestSevenPageClickY[index] = report.getClickCount();
            latestSevenMsgConY[index] = getIntNum(report.getDuanxinmoney());
            latestSevenMsgIssuedY[index] = getIntNum(report.getDuanxinmoney());
        } else if (type == 3) {
            lastWeekMoneyX[index] = getData(report.getName());
//            lastWeekMoneyXText[index] = getDataText(report.getName());
            lastWeekMoneyXText[index] = report.getName();
            lastWeekMoneyY[index] = getIntNum(report.getMoney());
            lastWeekPageConY[index] = getIntNum(report.getPagemoney());
            lastWeekPageDisplayY[index] = report.getShowCount();
            lastWeekPageClickY[index] = report.getClickCount();
            lastWeekMsgConY[index] = getIntNum(report.getDuanxinmoney());
            lastWeekMsgIssuedY[index] = report.getSuccCount();
        } else if (type == 4) {
//            monthMoneyX[index] = getData(report.getName());
////            lastWeekMoneyXText[index] = getDataText(report.getName());
//            monthMoneyXText[index] = report.getName();
//            monthMoneyY[index] = getIntNum(report.getMoney());
//            monthPageConY[index] = getIntNum(report.getPagemoney());
//            monthPageDisplayY[index] = report.getShowCount();
//            monthPageClickY[index] = report.getClickCount();
//            monthMsgConY[index] = getIntNum(report.getDuanxinmoney());
//            monthMsgIssuedY[index] = report.getSuccCount();

            monthMoneyXList.add(report.getName());
            monthMoneyXAllList.add(report.getName());
            monthMoneyY[index] = getIntNum(report.getMoney());
            monthPageConY[index] = getIntNum(report.getPagemoney());
            monthPageDisplayY[index] = report.getShowCount();
            monthPageClickY[index] = report.getClickCount();
            monthMsgConY[index] = getIntNum(report.getDuanxinmoney());
            monthMsgIssuedY[index] = report.getSuccCount();
        } else if (type == 5) {
            lastMonthMoneyXList.add(report.getName());
            lastMonthMoneyXAllList.add(report.getName());
            lastMonthMoneyY[index] = getIntNum(report.getMoney());
            lastMonthPageConY[index] = getIntNum(report.getPagemoney());
            lastMonthPageDisplayY[index] = report.getShowCount();
            lastMonthPageClickY[index] = report.getClickCount();
            lastMonthMsgConY[index] = getIntNum(report.getDuanxinmoney());
            lastMonthMsgIssuedY[index] = report.getSuccCount();
        }
    }

    private String getData(String date) {
        return CommonUtils.getTimeInterval1(date, "MM-dd");
    }

    private String getDataText(String date) {

        return CommonUtils.getTimeInterval1(date, "yyyy年MM月dd日");
    }

    private int getIntNum(BigDecimal bigDecimal) {
        return bigDecimal.intValue();
    }



    @Subscribe
    public void reportUrlResult(ReportUrlEvent reportUrlEvent) {
        if (reportUrlEvent.reportUrlBean.getCode() == 0) {
            String startTime = "";
            String endTime = "";
            String uid = "";
            if(reportUrlEvent.reportUrlBean.getResult()!=null){
                ExcelUrl excelUrl = reportUrlEvent.reportUrlBean.getResult();
                shareUrl = excelUrl.getExcUrl();
                startTime = excelUrl.getStartTime().replace("-","");
                endTime = excelUrl.getEndTime().replace("-","");
                uid = excelUrl.getUserId();
            }
            if (!TextUtils.isEmpty(shareUrl)) {
                showShareDialog(startTime+"-"+endTime,uid);
            } else {
                ToastUtils.showShort("后台分享链接为空！");
            }
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

    @Override
    protected ReportPresent<IReportView> createPresent() {
        return new ReportPresent<>(this);
    }

    @Override
    public void onClickLine(int touchIndex) {
        //显示
        updateCurTotalData(touchIndex);
    }



    /**
     * 更新黄色框里的内容
     */
    private void updateCurTotalData(int index) {
        if (index == -1) {
            linearCurTotalData.setVisibility(View.INVISIBLE);
        } else {
            linearCurTotalData.setVisibility(View.VISIBLE);
            int total = 0; //总消费
            int msgConsumption = 0; //短信消费
            int msgIssued = 0; //短信下发量
            int pageConsumption = 0; //页面消费
            int pageDisplay = 0; //页面展示量
            int pageClick = 0; //页面点击量
            String date = "";
            if (chooseType == 1) {
                total = yesterDayMoneyY[index];
                msgConsumption = yesterDayMsgConY[index];
                msgIssued = yesterDayMsgIssuedY[index];
                pageConsumption = yesterDayPageConY[index];
                pageDisplay = yesterDayPageDisplayY[index];
                pageClick = yesterDayPageClickY[index];
                date = yesterDayXText[index];
            } else if (chooseType == 2) {
                total = latestSevenMoneyY[index];
                msgConsumption = latestSevenMsgConY[index];
                msgIssued = latestSevenMsgIssuedY[index];
                pageConsumption = latestSevenPageConY[index];
                pageDisplay = latestSevenPageDisplayY[index];
                pageClick = latestSevenPageClickY[index];
                date = latestSevenMoneyXText[index];
            } else if (chooseType == 3) {
                total = lastWeekMoneyY[index];
                msgConsumption = lastWeekMsgConY[index];
                msgIssued = lastWeekMsgIssuedY[index];
                pageConsumption = lastWeekPageConY[index];
                pageDisplay = lastWeekPageDisplayY[index];
                pageClick = lastWeekPageClickY[index];
                date = lastWeekMoneyXText[index];
            } else if (chooseType == 4) {
                total = monthMoneyY[index];
                msgConsumption = monthMsgConY[index];
                msgIssued = monthMsgIssuedY[index];
                pageConsumption = monthPageConY[index];
                pageDisplay = monthPageDisplayY[index];
                pageClick = monthPageClickY[index];
                date = monthMoneyXAllText[index];
            } else if (chooseType == 5) {
                total = lastMonthMoneyY[index];
                msgConsumption = lastMonthMsgConY[index];
                msgIssued = lastMonthMsgIssuedY[index];
                pageConsumption = lastMonthPageConY[index];
                pageDisplay = lastMonthPageDisplayY[index];
                pageClick = lastMonthPageClickY[index];
                date = lastMonthMoneyXAllText[index];
            }
            tvTotal.setText("总消费： " + total);
            tvMsgConsumption.setText("短信消费： " + msgConsumption);
            tvMsgIssued.setText("短信下发量： " + msgIssued);
            tvPageConsumption.setText("页面消费： " + pageConsumption);
            tvPageDisplay.setText("页面展示量： " + pageDisplay);
            tvPageClick.setText("页面点击量： " + pageClick);
            tvDate.setText(date);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearPageCon:     //消费元
                clickPageCon();
                break;
            case R.id.linearPageDisplay: //页面展示量
                clickPageDisplay();
                break;
            case R.id.linearPageClick: //页面点击量
                clickPageClick();
                break;
            case R.id.linearMsgCon: //短信消费元
                clickMsgCon();
                break;
            case R.id.linearMsgIssued://短信下发量
                clickMsgIssued();
                break;
        }
    }

    private void clickPageCon() {
        int id = getPageConRemoveViewId();
        View v = getPageConRemoveView();
        for (int i = 0; i < curveRl.getChildCount(); i++) {
            View view = curveRl.getChildAt(i);
            if (view.getId() == id) {
                curveRl.removeView(v);
                updatePageConBtn(false);
                return;
            }
        }
        curveRl.addView(v);
        updatePageConBtn(true);
    }

    /**
     * 添加线
     */
    private void addPageCon(){
        View v = getPageConRemoveView();
        curveRl.removeView(v);
        curveRl.addView(v);
        curveRl.invalidate();
    }
    private void clearBtnClick(){
        updatePageConBtn(false);
        updatePageDisplayBtn(false);
        updatePageClickBtn(false);
        updateMsgConBtn(false);
        updateMsgIssuedBtn(false);
    }

    /**
     * 刷新界面的时候更新 显示的其他线
     */
    private void updateView(){
//        if(tvPageCon.getCurrentTextColor() == getResources().getColor(R.color.c_ffffff)){
////            updatePageConBtn(false);
//            clickPageCon();
//        }
//        if(tvPageDisplayNum.getCurrentTextColor() == getResources().getColor(R.color.c_ffffff)){
////            updatePageDisplayBtn(false);
//            clickPageDisplay();
//        }
//        if(tvPageClickNum.getCurrentTextColor() == getResources().getColor(R.color.c_ffffff)){
////            updatePageClickBtn(false);
//            clickPageClick();
//        }
//        if(tvMsgConNum.getCurrentTextColor() == getResources().getColor(R.color.c_ffffff)){
////            updateMsgConBtn(false);
//            clickMsgCon();
//        }
//        if(tvMsgIssuedNum.getCurrentTextColor() == getResources().getColor(R.color.c_ffffff)){
////            updateMsgIssuedBtn(false);
//            clickMsgIssued();
//        }
        clearBtnClick();
    }

    private void updatePageConBtn(boolean isClick) {
        if (isClick) {
            linearPageCon.setBackground(getResources().getDrawable(R.drawable.round_border4));
            tvPageCon.setTextColor(getResources().getColor(R.color.c_ffffff));
            tvPageCon1.setTextColor(getResources().getColor(R.color.c_ffffff));
        } else {
            linearPageCon.setBackground(getResources().getDrawable(R.drawable.round_border));
            tvPageCon.setTextColor(getResources().getColor(R.color.c_414141));
            tvPageCon1.setTextColor(getResources().getColor(R.color.c_414141));
        }
    }

    private void updatePageDisplayBtn(boolean isClick) {
        if (isClick) {
            linearPageDisplay.setBackground(getResources().getDrawable(R.drawable.round_border4));
            tvPageDisplayNum.setTextColor(getResources().getColor(R.color.c_ffffff));
            tvPageDisplayNum1.setTextColor(getResources().getColor(R.color.c_ffffff));
        } else {
            linearPageDisplay.setBackground(getResources().getDrawable(R.drawable.round_border));
            tvPageDisplayNum.setTextColor(getResources().getColor(R.color.c_414141));
            tvPageDisplayNum1.setTextColor(getResources().getColor(R.color.c_414141));
        }
    }

    private void updatePageClickBtn(boolean isClick) {
        if (isClick) {
            linearPageClick.setBackground(getResources().getDrawable(R.drawable.round_border4));
            tvPageClickNum.setTextColor(getResources().getColor(R.color.c_ffffff));
            tvPageClickNum1.setTextColor(getResources().getColor(R.color.c_ffffff));
        } else {
            linearPageClick.setBackground(getResources().getDrawable(R.drawable.round_border));
            tvPageClickNum.setTextColor(getResources().getColor(R.color.c_414141));
            tvPageClickNum1.setTextColor(getResources().getColor(R.color.c_414141));
        }
    }

    private void updateMsgConBtn(boolean isClick) {
        if (isClick) {
            linearMsgCon.setBackground(getResources().getDrawable(R.drawable.round_border4));
            tvMsgConNum.setTextColor(getResources().getColor(R.color.c_ffffff));
            tvMsgConNum1.setTextColor(getResources().getColor(R.color.c_ffffff));
        } else {
            linearMsgCon.setBackground(getResources().getDrawable(R.drawable.round_border));
            tvMsgConNum.setTextColor(getResources().getColor(R.color.c_414141));
            tvMsgConNum1.setTextColor(getResources().getColor(R.color.c_414141));
        }
    }

    private void updateMsgIssuedBtn(boolean isClick) {
        if (isClick) {
            linearMsgIssued.setBackground(getResources().getDrawable(R.drawable.round_border4));
            tvMsgIssuedNum.setTextColor(getResources().getColor(R.color.c_ffffff));
            tvMsgIssuedNum1.setTextColor(getResources().getColor(R.color.c_ffffff));
        } else {
            linearMsgIssued.setBackground(getResources().getDrawable(R.drawable.round_border));
            tvMsgIssuedNum.setTextColor(getResources().getColor(R.color.c_414141));
            tvMsgIssuedNum1.setTextColor(getResources().getColor(R.color.c_414141));
        }
    }

    private int getPageConRemoveViewId() {
        if (chooseType == 1) {
            return R.id.yesterday_line_pagecon;
        } else if (chooseType == 2) {
            return R.id.latestseven_line_pagecon;
        } else if (chooseType == 3) {
            return R.id.lastweek_line_pagecon;
        } else if (chooseType == 4) {
            return R.id.month_line_pagecon;
        } else if (chooseType == 5) {
            return R.id.lastmonth_line_pagecon;
        }
        return -1;
    }

    private View getPageConRemoveView() {
        if (chooseType == 1) {
            return yesturdayPageConView;
        } else if (chooseType == 2) {
            return latestSevenPageConView;
        } else if (chooseType == 3) {
            return lastWeekPageConView;
        } else if (chooseType == 4) {
            return monthPageConView;
        } else if (chooseType == 5) {
            return lastMonthPageConView;
        }
        return null;
    }

    private void clickPageClick() {
        int id = getPageClickRemoveViewId();
        View v = getPageClickRemoveView();
        for (int i = 0; i < curveRl.getChildCount(); i++) {
            View view = curveRl.getChildAt(i);
            if (view.getId() == id) {
                curveRl.removeView(v);
                updatePageClickBtn(false);
                return;
            }
        }
        curveRl.addView(v);
        updatePageClickBtn(true);
    }

    private int getPageClickRemoveViewId() {
        if (chooseType == 1) {
            return R.id.yesterday_line_pageclick;
        } else if (chooseType == 2) {
            return R.id.latestseven_line_pageclick;
        } else if (chooseType == 3) {
            return R.id.lastweek_line_pageclick;
        } else if (chooseType == 4) {
            return R.id.month_line_pageclick;
        } else if (chooseType == 5) {
            return R.id.lastmonth_line_pageclick;
        }
        return -1;
    }

    private View getPageClickRemoveView() {
        if (chooseType == 1) {
            return yesturdayPageClickView;
        } else if (chooseType == 2) {
            return latestSevenPageClickView;
        } else if (chooseType == 3) {
            return lastWeekPageClickView;
        } else if (chooseType == 4) {
            return monthPageClickView;
        } else if (chooseType == 5) {
            return lastMonthPageClickView;
        }
        return null;
    }

    private void clickMsgCon() {
        int id = getMsgConRemoveViewId();
        View v = getMsgConRemoveView();
        for (int i = 0; i < curveRl.getChildCount(); i++) {
            View view = curveRl.getChildAt(i);
            if (view.getId() == id) {
                curveRl.removeView(v);
                updateMsgConBtn(false);
                return;
            }
        }
        curveRl.addView(v);
        updateMsgConBtn(true);
    }

    private int getMsgConRemoveViewId() {
        if (chooseType == 1) {
            return R.id.yesterday_line_msgcon;
        } else if (chooseType == 2) {
            return R.id.latestseven_line_msgcon;
        } else if (chooseType == 3) {
            return R.id.lastweek_line_msgcon;
        } else if (chooseType == 4) {
            return R.id.month_line_msgcon;
        } else if (chooseType == 5) {
            return R.id.lastmonth_line_msgcon;
        }
        return -1;
    }

    private View getMsgConRemoveView() {
        if (chooseType == 1) {
            return yesturdayMsgConView;
        } else if (chooseType == 2) {
            return latestSevenMsgConView;
        } else if (chooseType == 3) {
            return lastWeekMsgConView;
        } else if (chooseType == 4) {
            return monthMsgConView;
        } else if (chooseType == 5) {
            return lastMonthMsgConView;
        }
        return null;
    }

    private void clickMsgIssued() {
        int id = getMsgIssuedRemoveViewId();
        View v = getMsgIssuedRemoveView();
        for (int i = 0; i < curveRl.getChildCount(); i++) {
            View view = curveRl.getChildAt(i);
            if (view.getId() == id) {
                curveRl.removeView(v);
                updateMsgIssuedBtn(false);
                return;
            }
        }
        curveRl.addView(v);
        updateMsgIssuedBtn(true);
    }

    private int getMsgIssuedRemoveViewId() {
        if (chooseType == 1) {
            return R.id.yesterday_line_msgissued;
        } else if (chooseType == 2) {
            return R.id.latestseven_line_msgissued;
        } else if (chooseType == 3) {
            return R.id.lastweek_line_msgissued;
        } else if (chooseType == 4) {
            return R.id.month_line_msgissued;
        } else if (chooseType == 5) {
            return R.id.lastmonth_line_msgissued;
        }
        return -1;
    }

    private View getMsgIssuedRemoveView() {
        if (chooseType == 1) {
            return yesturdayMsgIssuedView;
        } else if (chooseType == 2) {
            return latestSevenMsgIssuedView;
        } else if (chooseType == 3) {
            return lastWeekMsgIssuedView;
        } else if (chooseType == 4) {
            return monthMsgIssuedView;
        } else if (chooseType == 5) {
            return lastMonthMsgIssuedView;
        }
        return null;
    }

    private void clickPageDisplay() {
        int id = getPageDisplayRemoveViewId();
        View v = getPageDisplayRemoveView();
        for (int i = 0; i < curveRl.getChildCount(); i++) {
            View view = curveRl.getChildAt(i);
            if (view.getId() == id) {
                curveRl.removeView(v);
                updatePageDisplayBtn(false);
                return;
            }
        }
        curveRl.addView(v);
        updatePageDisplayBtn(true);
    }


    private void addPageDisplay(){
        View v = getPageDisplayRemoveView();

    }

    private int getPageDisplayRemoveViewId() {
        if (chooseType == 1) {
            return R.id.yesterday_line_pagedisplay;
        } else if (chooseType == 2) {
            return R.id.latestseven_line_pagedisplay;
        } else if (chooseType == 3) {
            return R.id.lastweek_line_pagedisplay;
        } else if (chooseType == 4) {
            return R.id.month_line_pagedisplay;
        } else if (chooseType == 5) {
            return R.id.lastmonth_line_pagedisplay;
        }
        return -1;
    }

    private View getPageDisplayRemoveView() {
        if (chooseType == 1) {
            return yesturdayPageDisplayView;
        } else if (chooseType == 2) {
            return latestSevenPageDisplayView;
        } else if (chooseType == 3) {
            return lastWeekPageDisplayView;
        } else if (chooseType == 4) {
            return monthPageDisplayView;
        } else if (chooseType == 5) {
            return lastMonthPageDisplayView;
        }
        return null;
    }





    //分享的dialog
    private void showShareDialog(final String time,final String uid) {
        View view = LayoutInflater.from(ReportActivity.this).inflate(R.layout.report_share, null);
        //二维码
        //ImageView ivCode = (ImageView) view.findViewById(R.id.iv_code);
        //getCode(ivCode);
        // 设置style 控制默认dialog带来的边距问题
        final Dialog dialog = new Dialog(ReportActivity.this, R.style.common_dialog);

        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (shareEntity == null) {
                    shareEntity = new ShareEntity();
                }
                shareEntity.setTitle(uid+"用户寻客报表");
                shareEntity.setContent(uid+"用户"+time+ "数据报表");
                shareEntity.setShareUrl(shareUrl);

                switch (v.getId()) {
                    case R.id.ll_share_wx_friend:
                        // 分享到微信
                        ShareManager.getInstance().shareWX(ReportActivity.this, shareEntity);
                        break;
//                    case R.id.ll_share_wx_circle:
//                        ShareManager.getInstance().shareWXQ(ReportActivity.this, shareEntity);
//                        // 分享到朋友圈
//                        break;
//                    case R.id.ll_share_sina:
//                        ShareManager.getInstance().shareWEIBO(ReportActivity.this, shareEntity);
//                        break;
                    case R.id.ll_share_qq_friend:
                        ShareManager.getInstance().shareQQ(ReportActivity.this, shareEntity);
                        break;
//                    case R.id.ll_share_qq_circle:
//                        ShareManager.getInstance().shareQQZONE(ReportActivity.this, shareEntity);
//                        break;
                    case R.id.tv_cancel:
                        // 取消
                        break;
                }
                dialog.dismiss();
            }

        };

        view.setOnClickListener(listener);

        dialog.setContentView(view);
        dialog.show();

        // 分享到几个平台的点击监听
        view.findViewById(R.id.ll_share_wx_friend).setOnClickListener(listener);
        // view.findViewById(R.id.ll_share_wx_circle).setOnClickListener(listener);
        //view.findViewById(R.id.ll_share_sina).setOnClickListener(listener);
        view.findViewById(R.id.ll_share_qq_friend).setOnClickListener(listener);
        //view.findViewById(R.id.ll_share_qq_circle).setOnClickListener(listener);
        view.findViewById(R.id.tv_cancel).setOnClickListener(listener);

        // 设置相关位置，一定要在 show()之后
        /*Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        //使用window管理器动态创建布局管理
        WindowManager.LayoutParams params = window.getAttributes();

        params.width = screenWidth - DisplayUtil.dip2px(getActivity(), 17) * 2;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);*/


        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        //设置一个弹出动画
        window.setWindowAnimations(R.style.popwin_anim_style);
        window.getDecorView().setPadding(0, 0, 0, 0);

    }



    /**
     * 获取过去第几天的日期
     * @param past
     * @return
     */
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        Log.e(null, result);
        return result;
    }






    private  int  maxResult(int[] aa, int[] bb, int[] cc,int[] dd,int[] ee,int[] ff){
        int maxAll[] = {maxY(aa),maxY(bb),maxY(cc),maxY(dd),maxY(ee),maxY(ff)};
        int maxResult = maxY(maxAll);
//        Logger.d("maxresult: "+maxResult);
        return maxResult;
    }





        //数组合并




   /* //分享的dialog
    private void showShareDialog() {
        Activity activity = getActivity();
        if (activity == null) return;
        View view = LayoutInflater.from(activity).inflate(R.layout.view_share, null);
        //二维码
        ImageView ivCode = (ImageView) view.findViewById(R.id.iv_code);
        //getCode(ivCode);
        // 设置style 控制默认dialog带来的边距问题
        final Dialog dialog = new Dialog(activity, R.style.common_dialog);

        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (shareEntity == null) {
                    shareEntity = new ShareEntity();
                    shareEntity.setTitle("在恰当的时间，将合适的广告，精准的投放给用户！");
                    shareEntity.setContent("寻客，让营销更简单！");
                    shareEntity.setShareUrl(shareUrl);

                }

                switch (v.getId()) {
                    case R.id.ll_share_wx_friend:
                        // 分享到微信
                        ShareManager.getInstance().shareWX(getActivity(), shareEntity);
                        break;
                    case R.id.ll_share_wx_circle:
                        ShareManager.getInstance().shareWXQ(getActivity(), shareEntity);
                        // 分享到朋友圈
                        break;
                    case R.id.ll_share_sina:
                        ShareManager.getInstance().shareWEIBO(getActivity(), shareEntity);
                        break;
                    case R.id.ll_share_qq_friend:
                        ShareManager.getInstance().shareQQ(getActivity(), shareEntity);
                        break;
                    case R.id.ll_share_qq_circle:
                        ShareManager.getInstance().shareQQZONE(getActivity(), shareEntity);
                        break;
                    case R.id.img_bottom:
                        // 取消
                        break;
                }
                dialog.dismiss();
            }

        };

        view.setOnClickListener(listener);

        dialog.setContentView(view);
        dialog.show();

        // 分享到几个平台的点击监听
        view.findViewById(R.id.ll_share_wx_friend).setOnClickListener(listener);
        view.findViewById(R.id.ll_share_wx_circle).setOnClickListener(listener);
        view.findViewById(R.id.ll_share_sina).setOnClickListener(listener);
        view.findViewById(R.id.ll_share_qq_friend).setOnClickListener(listener);
        view.findViewById(R.id.ll_share_qq_circle).setOnClickListener(listener);

        // 设置相关位置，一定要在 show()之后
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        //使用window管理器动态创建布局管理
        WindowManager.LayoutParams params = window.getAttributes();

        params.width = screenWidth - DisplayUtil.dip2px(getActivity(), 17) * 2;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);

    }*/

}

