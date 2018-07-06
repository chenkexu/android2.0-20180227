package com.orientdata.lookforcustomers.view.findcustomer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.bean.AgePushBean;
import com.orientdata.lookforcustomers.bean.TaskOut;
import com.orientdata.lookforcustomers.bean.TaskThrowInfo;
import com.orientdata.lookforcustomers.util.AnimationUtil;
import com.orientdata.lookforcustomers.widget.dialog.PushErrorDialog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ckx on 2018/6/21.
 */

public class PushResultReport extends FrameLayout {


    @BindView(R.id.cd_order_report)
    CardView cdOrderReport;
    @BindView(R.id.tv_push_person)
    TextView tvPushPerson;
    @BindView(R.id.ll_account_balance)
    LinearLayout llAccountBalance;
    @BindView(R.id.tv_push_person_error)
    TextView tvPushPersonError;
    @BindView(R.id.iv_error_que)
    ImageView ivErrorQue;
    @BindView(R.id.ll_account_froze)
    LinearLayout llAccountFroze;
    @BindView(R.id.tv_push_person_ratio)
    TextView tvPushPersonRatio;
    @BindView(R.id.textView23)
    TextView textView23;
    @BindView(R.id.ll_account_commission)
    LinearLayout llAccountCommission;
    @BindView(R.id.man)
    ImageView man;
    @BindView(R.id.woman)
    ImageView woman;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    @BindView(R.id.tv_man_ratio)
    TextView tvManRatio;
    @BindView(R.id.tv_push_report)
    TextView tvPushReport;

    @BindView(R.id.tv_woman_ratio)
    TextView tvWomanRatio;
    @BindView(R.id.rv_age_push)
    RecyclerView rvAgePush;
    @BindView(R.id.rv_test_phone_push)
    RecyclerView rvTestPhonePush;
    @BindView(R.id.rv_push_person_list)
    RecyclerView rvPushPersonList;

    @BindView(R.id.ll_report)
    CardView llReport;
    Drawable image_up, img_right;
    private Context context;
    private boolean isShow = true;
    private String[] agesArray = {"18及以下-20","20-25","25-30","30-35","35-40","40-45","45-50","50-55","55-60","60-65及以上"};
    private List<AgePushBean> agePushBeans;


    public PushResultReport(@NonNull Context context) {
        super(context, null);
        this.context = context;
    }

    public PushResultReport(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = LayoutInflater.from(context)
                .inflate(R.layout.include_push_report, this);
        ButterKnife.bind(view);
        rvAgePush.setItemAnimator(new DefaultItemAnimator());

        rvAgePush.setLayoutManager(new LinearLayoutManager(context){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        rvPushPersonList.setItemAnimator(new DefaultItemAnimator());
        rvPushPersonList.setLayoutManager(new LinearLayoutManager(context){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        rvTestPhonePush.setItemAnimator(new DefaultItemAnimator());
        rvTestPhonePush.setLayoutManager(new LinearLayoutManager(context){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        Resources res = getResources();

        image_up = res.getDrawable(R.mipmap.order_down);
        img_right = res.getDrawable(R.mipmap.order_right);
        image_up.setBounds(0, 0, image_up.getMinimumWidth(), image_up.getMinimumHeight());
        img_right.setBounds(0, 0, img_right.getMinimumWidth(), img_right.getMinimumHeight());

    }

    public void setData(TaskOut taskOut) {
        List<String> phones = taskOut.getPhone();
        if (phones==null||phones.size()>0) {
            rvPushPersonList.setAdapter(new PhoneAdapter(phones));
        }
        TaskThrowInfo taskThrowInfo = taskOut.getTaskThrowInfo();
        tvPushPerson.setText(taskOut.getIssuedCount()+"人");
        tvPushPersonError.setText((taskOut.getEstimatePeoplerno() - taskOut.getIssuedCount())+"人");

        BigDecimal b1 = new BigDecimal(taskOut.getIssuedCount()+"");
        BigDecimal b2 = new BigDecimal(taskOut.getEstimatePeoplerno()+"");
        double doubleValue = b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();


        tvPushPersonRatio.setText(doubleValue+"%");
        progressbar.setProgress(taskThrowInfo.getMan());
        tvManRatio.setText(taskThrowInfo.getMan()+"%");
        tvWomanRatio.setText(taskThrowInfo.getWoman()+"%");


        List<Double> ages2 = new ArrayList<>();
        ages2.add(taskThrowInfo.getAge0_20());
        ages2.add(taskThrowInfo.getAge20_25());
        ages2.add(taskThrowInfo.getAge25_30());
        ages2.add(taskThrowInfo.getAge30_35());
        ages2.add(taskThrowInfo.getAge35_40());
        ages2.add(taskThrowInfo.getAge40_45());
        ages2.add(taskThrowInfo.getAge45_50());
        ages2.add(taskThrowInfo.getAge50_55());
        ages2.add(taskThrowInfo.getAge55_60());
        ages2.add(taskThrowInfo.getAge60_());



        agePushBeans = new ArrayList<>();
        for (int i=0;i<agesArray.length;i++){
            double v = new BigDecimal(ages2.get(i)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            AgePushBean agePushBean = new AgePushBean(v, agesArray[i], (int)(ages2.get(i)*100));
            agePushBeans.add(agePushBean);
        }

        rvAgePush.setAdapter(new AgeAdapter(agePushBeans));

        List<String> testPhones = new ArrayList<>();

        if (!TextUtils.isEmpty(taskOut.getTestCmPhone())) {
            testPhones.add(taskOut.getTestCmPhone());
        }
        if (!TextUtils.isEmpty(taskOut.getTestCuPhone())) {
            testPhones.add(taskOut.getTestCuPhone());
        }
        if (!TextUtils.isEmpty(taskOut.getTestCtPhone())) {
            testPhones.add(taskOut.getTestCtPhone());
        }

//        if (testPhones.size()!=0) {
//            rvTestPhonePush.setAdapter(new PhoneAdapter(testPhones));
//        }
    }



    @OnClick({R.id.tv_push_person_ratio, R.id.textView23,R.id.iv_error_que,R.id.cd_order_report})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_error_que:
                showDialog();
                break;
            case R.id.cd_order_report:
                if (isShow) {
                    tvPushReport.setCompoundDrawables(null, null, img_right, null);
                    AnimationUtil.with().showAndHiddenAnimation(llReport, AnimationUtil.AnimationState.STATE_HIDDEN,1000);
                    isShow = false;
                }else{
                    tvPushReport.setCompoundDrawables(null, null, image_up, null);
                    AnimationUtil.with().showAndHiddenAnimation(llReport, AnimationUtil.AnimationState.STATE_SHOW,1000);
                    isShow = true;
                }
                break;
        }
    }


    private void showDialog() {
        PushErrorDialog pushErrorDialog = new PushErrorDialog(context);
        pushErrorDialog.show();
    }

    class PhoneAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

        public PhoneAdapter(@Nullable List<String> data) {
            super(R.layout.item_gv_push_person, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.tv_phone_num, item);
        }
    }


    class AgeAdapter extends BaseQuickAdapter<AgePushBean,BaseViewHolder> {

        public AgeAdapter(@Nullable List<AgePushBean> data) {
            super(R.layout.item_gv_push_age, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, AgePushBean item) {
            helper.setProgress(R.id.progressBar_age, item.getProgressAge());
            helper.setText(R.id.tv_age_ratio, item.getAgeRatio());
            helper.setText(R.id.tv_age, item.getAgeStr());
        }
    }
}
