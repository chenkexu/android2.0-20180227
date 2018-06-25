package com.orientdata.lookforcustomers.view.findcustomer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.DigitalGroupView;
import com.wx.goodview.GoodView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;

/**
 * Created by ckx on 2018/6/21.
 */

public class TaskDeliveryView extends FrameLayout {


    @BindView(R.id.iv_close_task)
    ImageView ivCloseTask;
    @BindView(R.id.tv_remain_time)
    CountdownView tvRemainTime;
    @BindView(R.id.tv_show_detail)
    TextView tvShowDetail;
    @BindView(R.id.ll_text)
    LinearLayout llText;
    @BindView(R.id.digital)
    DigitalGroupView digital;
    @BindView(R.id.iv_speed)
    ImageView ivSpeed;

    @BindView(R.id.tv_cooling_time)
    CountdownView tvCoolingTime;
    @BindView(R.id.tv_time)
    TextView tvtime;

    @BindView(R.id.ll_jiasu)
    LinearLayout llJiasu;
    @BindView(R.id.cd_task_delivery)
    CardView cdTaskDelivery;


    private OnClickListener onItemClickListener;

    private long coolingTime = 0; //单位毫秒
    private boolean clickFlag = true;
    private Context context;


    public TaskDeliveryView(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public TaskDeliveryView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = LayoutInflater.from(context)
                .inflate(R.layout.activity_task_delivery, this);
        ButterKnife.bind(view);
        long time4 = (long)2 * 24 * 60 * 60;
        tvRemainTime.start(time4);
    }


    public void hideView(){
        ivCloseTask.setVisibility(View.GONE);
        tvShowDetail.setVisibility(View.GONE);
    }


    @OnClick({R.id.iv_speed, R.id.tv_time,R.id.tv_show_detail,R.id.iv_close_task})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_speed: //戳我加速
                final GoodView goodView = new GoodView(context);
                goodView.setImage(getResources().getDrawable(R.mipmap.main_lighting));
                goodView.setDistance(120);

                if (coolingTime >= 60 && clickFlag) {
                    tvCoolingTime.setVisibility(View.VISIBLE);
                    tvtime.setVisibility(View.GONE);
                    clickFlag = false;
                    tvCoolingTime.start(60000);
                    tvCoolingTime.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                        @Override
                        public void onEnd(CountdownView cv) {
                            Logger.d("倒计时结束了");
                            clickFlag = true;
                            coolingTime = 0;
                        }
                    });
                } else if(coolingTime >= 60 && !clickFlag){
                    ToastUtils.showShort("让我休息一下吧！");
                } else if(clickFlag){
                    goodView.show(ivSpeed);
                    coolingTime = coolingTime + 10;
                    tvCoolingTime.setVisibility(View.GONE);
                    tvtime.setVisibility(View.VISIBLE);
                    tvtime.setText(coolingTime+" s");
                    // TODO: 2018/6/14 投递条数和剩余天数开始变化。
                }
                break;
            case R.id.tv_show_detail://查看详情
                onItemClickListener.showTaskDetail();
                break;
            case R.id.iv_close_task://关闭
                onItemClickListener.hideTaskDelivery();
                break;

        }
    }


    public interface OnClickListener {
        void showTaskDetail();
        void hideTaskDelivery();
    }


    public void setOnItemClickListener(OnClickListener listener) {
        onItemClickListener = listener;
    }


}
