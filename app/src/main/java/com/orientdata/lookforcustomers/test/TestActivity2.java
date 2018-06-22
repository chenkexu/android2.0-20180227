package com.orientdata.lookforcustomers.test;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.util.CountDownTimerUtils;
import com.orientdata.lookforcustomers.view.DigitalGroupView;
import com.orientdata.lookforcustomers.widget.ShadowDrawable;

public class TestActivity2 extends AppCompatActivity {

    Button buttonPlay, buttonAddView;
    AppCompatSeekBar seekInterval, seekFigureCount, seekSize;

    DigitalGroupView digitalGroupView;

    private int num = 0;


    private int dpToPx(int dp) {
        return (int) (Resources.getSystem().getDisplayMetrics().density * dp + 0.5f);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity2);

        buttonPlay = (Button) findViewById(R.id.button_play);
        digitalGroupView = (DigitalGroupView) findViewById(R.id.digital);
        ImageView imageView =  findViewById(R.id.imageView);

        ShadowDrawable.setShadowDrawable(imageView, new int[] {
                        Color.parseColor("#536DFE"), Color.parseColor("#7C4DFF")}, dpToPx(8),
                Color.parseColor("#997C4DFF"), dpToPx(5), dpToPx(5), dpToPx(5));






        CountDownTimerUtils countDownTimer = CountDownTimerUtils.getCountDownTimer();
        countDownTimer.setMillisInFuture(100000000)
                .setCountDownInterval(5000)
                .setTickDelegate(new CountDownTimerUtils.TickDelegate() {
                    @Override
                    public void onTick(long pMillisUntilFinished) {

                        digitalGroupView.setDigits(num);
                        num = num + 5;
                        Logger.v("pMillisUntilFinished = " + pMillisUntilFinished);
                    }
                })
                .setFinishDelegate(new CountDownTimerUtils.FinishDelegate() {
                    @Override
                    public void onFinish() {
                        Logger.v("CountDownTimerTest", "onFinish");
                    }
                }).start();


        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = digitalGroupView.getmDigits();
                Logger.d("当前的数字为：" + i);
                digitalGroupView.setDigits(i + 9);
                num = digitalGroupView.getmDigits();
            }
        });

    }
}
