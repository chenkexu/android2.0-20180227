package com.orientdata.lookforcustomers.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.widget.Button;

import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.util.CountDownTimerUtils;
import com.orientdata.lookforcustomers.view.DigitalGroupView;

public class TestActivity2 extends AppCompatActivity {

    Button buttonPlay, buttonAddView;
    AppCompatSeekBar seekInterval, seekFigureCount, seekSize;

    DigitalGroupView digitalGroupView;

    private int num = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity2);

        buttonPlay = (Button) findViewById(R.id.button_play);
        digitalGroupView = (DigitalGroupView) findViewById(R.id.digital);



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
