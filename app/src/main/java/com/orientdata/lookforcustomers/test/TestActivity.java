package com.orientdata.lookforcustomers.test;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.bean.AddressCollectInfo;
import com.orientdata.lookforcustomers.bean.WrResponse;
import com.orientdata.lookforcustomers.network.api.ApiManager;
import com.orientdata.lookforcustomers.network.api.BaseObserver;
import com.orientdata.lookforcustomers.network.api.ParamsUtil;
import com.orientdata.lookforcustomers.network.util.RxUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends AppCompatActivity {


    @BindView(R.id.seekbar5)
    RangeSeekBar seekbar5;
    @BindView(R.id.tv_no_limit)
    TextView tvNoLimit;
    @BindView(R.id.age_from)
    TextView ageFrom;
    @BindView(R.id.age_to)
    TextView ageTo;
    @BindView(R.id.bottom_sheet)
    NestedScrollView bottomSheet;

//    @BindView(R.id.dragView)
//    LinearLayout dragView;
//    @BindView(R.id.sliding_layout)
//    SlidingUpPanelLayout mLayout;






    BottomSheetBehavior behavior;


    @OnClick(R.id.tv_no_limit)
    public void onViewClicked() {
        seekbar5.setValue(15, 70);
        seekbar5.invalidate();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .statusBarView(R.id.top_view)
                .fullScreen(true)
                .init();
        ButterKnife.bind(this);

//        mLayout.setAnchorPoint(0.7f);
//        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);


        seekbar5.setValue(15, 70);
        seekbar5.setOnRangeChangedListener(new OnRangeChangedListener() {

            private float rightValue;
            private float leftValue;

            @Override
            public void onRangeChanged(RangeSeekBar view, float min, float max, boolean isFromUser) {
                //min is left seekbar value, max is right seekbar value
                leftValue = min;
                rightValue = max;
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
                //start tracking touch
            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

                Logger.d("左边显示的值：" + leftValue);
                Logger.d("右边显示的值：" + rightValue);

                String leftStr = leftValue + "";
                String lrightValueStr = rightValue + "";

                ageFrom.setText(leftStr.substring(0, 2));
                ageTo.setText(lrightValueStr.substring(0, 2));

                if (leftStr.substring(0, 2).equals("15")) {
                    ageFrom.setText("18及以下");
                }
                if (lrightValueStr.substring(0, 2).equals("70")) {
                    ageFrom.setText("65及以下");
                }
            }
        });


        HashMap<String, Object> map = ParamsUtil.getMap();
        ApiManager.getInstence().getApiService().appAddressGet(ParamsUtil.getParams(map))
                .compose(RxUtil.<WrResponse<List<AddressCollectInfo>>>rxSchedulerHelper())
                .subscribe(new BaseObserver<List<AddressCollectInfo>>() {
                    @Override
                    protected void onSuccees(WrResponse<List<AddressCollectInfo>> t) {
                        List<AddressCollectInfo> result = t.getResult();

                    }

                    @Override
                    protected void onFailure(String errorInfo, boolean isNetWorkError) {

                    }
                });

        behavior = BottomSheetBehavior.from(bottomSheet);

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                //这里是bottomSheet 状态的改变
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    dimBackground(1.0f,0.5f);
                }else{

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //这里是拖拽中的回调，根据slideOffset可以做一些动画
            }
        });

        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
    }

    private void dimBackground(final float from, final float to) {


        final Window window = getWindow();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.alpha = (Float) animation.getAnimatedValue();
                window.setAttributes(params);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });

        valueAnimator.start();
    }
}
