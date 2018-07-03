package com.orientdata.lookforcustomers.view.findcustomer;

import android.content.Context;
import android.os.CountDownTimer;
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
import com.orientdata.lookforcustomers.bean.OrderDeliveryBean;
import com.orientdata.lookforcustomers.bean.WrResponse;
import com.orientdata.lookforcustomers.network.api.ApiManager;
import com.orientdata.lookforcustomers.network.api.BaseObserver;
import com.orientdata.lookforcustomers.network.api.ParamsUtil;
import com.orientdata.lookforcustomers.network.util.RxUtil;
import com.orientdata.lookforcustomers.util.CountDownTimerUtils;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.DigitalGroupView;
import com.wx.goodview.GoodView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;

import static com.orientdata.lookforcustomers.util.CommonUtils.getRandom;

/**
 * Created by ckx on 2018/6/21.
 */

public class TaskDeliveryView extends FrameLayout {


    @BindView(R.id.iv_close_task)
    ImageView ivCloseTask;

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

    @BindView(R.id.tv_text)
    TextView tv_text;

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
    private int deliveryNum = 0;
    private int expireDate;

    @BindView(R.id.tv_remain_time)
    CountdownView tvRemainTime;

    private CountDownTimer mCountDownTimer;
    private CountDownTimerUtils countDownTimer;

    private String accelerateValueS; //10
    private String criticalValue; //60
    private int estimatePeoplerno;
    private double rate;
    private int expediteId;


    private int[] images = {R.mipmap.main_lighting,R.mipmap.main_lighting2,
            R.mipmap.main_lighting3,R.mipmap.main_lighting4,R.mipmap.main_lighting5,R.mipmap.main_lighting6,
            R.mipmap.main_lighting7,R.mipmap.main_lighting8,R.mipmap.main_lighting9,};
    private int surplusDate;


    public CountDownTimer getmCountDownTimer() {
        return mCountDownTimer;
    }

    public void setmCountDownTimer(CountDownTimer mCountDownTimer) {
        this.mCountDownTimer = mCountDownTimer;
    }

    public CountDownTimerUtils getCountDownTimer() {
        return countDownTimer;
    }

    public void setCountDownTimer(CountDownTimerUtils countDownTimer) {
        this.countDownTimer = countDownTimer;
    }

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
        countDownTimer = CountDownTimerUtils.getCountDownTimer();
    }


    public void setTaskFinish(OrderDeliveryBean orderDeliveryBean){
        ivSpeed.setImageResource(R.mipmap.icon_task_over);
        ivSpeed.setEnabled(false);
        tv_text.setText("寻客订单投放结束");
        tvRemainTime.start(0);
    }




    public void setData(OrderDeliveryBean orderDeliveryBean){
        if (orderDeliveryBean==null) {
            return;
        }
        expediteId = orderDeliveryBean.getExpediteId();
        rate = orderDeliveryBean.getRate();
        expireDate = orderDeliveryBean.getExpireDate(); //从这个数开始倒计时
        accelerateValueS = orderDeliveryBean.getAccelerateValueS();
        criticalValue = orderDeliveryBean.getCriticalValue();
        surplusDate = orderDeliveryBean.getSurplusDate(); //剩余的时间
        int num = (int) (surplusDate / rate);  //剩余条数
        estimatePeoplerno = orderDeliveryBean.getTask().getEstimatePeoplerno(); //总条数
        deliveryNum = estimatePeoplerno - num; //投放条数

        Logger.d("剩余："+num+",投放了："+deliveryNum);

        if (orderDeliveryBean.getTask().getStatus() == 8) { //运营商真正返回投放结束
            digital.setDigits(deliveryNum);
            setTaskDeliveryNum(deliveryNum, 1.0);
            setTaskFinish(orderDeliveryBean);

        }else{
            if (deliveryNum <= 0) {
                deliveryNum = 0;
            }
            if (deliveryNum == estimatePeoplerno) { //投放结束
                digital.setDigits(estimatePeoplerno);
                setTaskDeliveryNum(estimatePeoplerno,1.0);
            }else{  //正在投放中
                tvRemainTime.start(surplusDate * 1000);
                digital.setDigits(deliveryNum);
                setTaskDeliveryNum(estimatePeoplerno, rate);
                startCountDownTimer(expireDate * 1000);
            }

        }


    }

    @OnClick({R.id.iv_speed, R.id.tv_time,R.id.tv_show_detail,R.id.iv_close_task,R.id.tv_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_text:
                digital.setDigits(estimatePeoplerno);
                break;
            case R.id.iv_speed: //戳我加速
                int random = getRandom(1, 9);//取到随机数
                final GoodView goodView = new GoodView(context);
                goodView.setImage(getResources().getDrawable(images[random-1]));
                goodView.setDistance(120);
                goodView.setDuration(1200);

                int currentTime =Integer.parseInt(tvtime.getText().toString().replace("s",""));
                if (currentTime < Integer.parseInt(criticalValue)) { //小于60
                    goodView.show(ivSpeed);
                    currentTime = currentTime + Integer.parseInt(accelerateValueS); //当前时间
                    startCountDownTimer(currentTime * 1000);
                    int mDigits = digital.getmDigits();

                    if ((random + mDigits)  > estimatePeoplerno) {  //大于总条数，不能再投放了
                        digital.setDigits(estimatePeoplerno);
                        deliveryFinish();
                        ToastUtils.showShort("订单推送成功，系统结算中，请勿重复点击戳我加速哦！");
                    }else{  //继续投放
                        //设置投放的数量
                        digital.setDigits(mDigits + random - 1);
                        setTaskDeliveryNum(estimatePeoplerno,rate);
                        // TODO: 2018/7/3 设置时间
                        double v = ((estimatePeoplerno - (random + mDigits)) * rate) * 1000;
                        tvRemainTime.start(Math.round(v));
                    }
                    // TODO: 2018/6/28 增加条数请求接口
                    spokeSpeedUp(expediteId,10);

                }else{
                    ToastUtils.showShort("让我休息一会儿吧");
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


    private void deliveryFinish(){
        mCountDownTimer.cancel();
        countDownTimer.cancel();
        tvRemainTime.start(0);
        getTaskDeliveryInfo();
    }





    //设置投放的数量
    private void setTaskDeliveryNum(final int estimatePeoplerno, double rate){

        countDownTimer.cancel();
        countDownTimer.setMillisInFuture((long)(estimatePeoplerno*rate*1000))
                .setCountDownInterval((long)(rate*1000))
                .setTickDelegate(new CountDownTimerUtils.TickDelegate() {
                    @Override
                    public void onTick(long pMillisUntilFinished) {
                        int getmDigits = digital.getmDigits(); //当前条数

                        if (getmDigits < estimatePeoplerno) { //当前条数小于总条数
                            digital.setDigits(getmDigits+1);
                        }else if(getmDigits == estimatePeoplerno ){  //投放完成
                            digital.setDigits(estimatePeoplerno);
//                            deliveryFinish();
                        }

                    }
                })
                .setFinishDelegate(new CountDownTimerUtils.FinishDelegate() {
                    @Override
                    public void onFinish() {

                    }
                }).start();
    }







    private void spokeSpeedUp(int expediteId,int expediteValue){
        HashMap<String, Object> map = ParamsUtil.getMap();
        map.put("expediteId", expediteId+"");
        map.put("expediteValue", expediteValue+"");
        ApiManager.getInstence().getApiService().pokeMeSpeedUp(ParamsUtil.getParams(map))
                .compose(RxUtil.<WrResponse<Object>>rxSchedulerHelper())
                .subscribe(new BaseObserver<Object>() {
                    @Override
                    protected void onSuccees(WrResponse t) {

                    }

                    @Override
                    protected void onFailure(String errorInfo, boolean isNetWorkError) {
                            ToastUtils.showShort(errorInfo);
                    }
                });
    }




    //设置冷却值
    public void startCountDownTimer(final long millisInFuture) {
        if (mCountDownTimer!=null) {
            mCountDownTimer.cancel();
        }
        mCountDownTimer = new CountDownTimer(millisInFuture, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                    tvtime.setText(millisUntilFinished /1000 +"s");
            }

            @Override
            public void onFinish() {

            }
        };
        mCountDownTimer.start();

    }





    public void getTaskDeliveryInfo(){

        HashMap<String, Object> map = ParamsUtil.getMap();
        ApiManager.getInstence().getApiService().getNewestTaskThrowExpedite(ParamsUtil.getParams(map))
                .compose(RxUtil.<WrResponse<OrderDeliveryBean>>rxSchedulerHelper())
                .subscribe(new BaseObserver<OrderDeliveryBean>() {
                    @Override
                    protected void onSuccees(WrResponse<OrderDeliveryBean> t) {
                        if (t.getResult()!=null) {
                            setData(t.getResult());
                        }
                    }

                    @Override
                    protected void onFailure(String errorInfo, boolean isNetWorkError) {
                        ToastUtils.showShort(errorInfo);
                    }
                });


    }


















    public interface OnClickListener {
        void showTaskDetail();
        void hideTaskDelivery();
    }


    public void setOnItemClickListener(OnClickListener listener) {
        onItemClickListener = listener;
    }
    public void hideView(){
        ivCloseTask.setVisibility(View.GONE);
        tvShowDetail.setVisibility(View.GONE);
    }






}
