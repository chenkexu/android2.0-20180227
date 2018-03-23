package com.orientdata.lookforcustomers.view.findcustomer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.WangrunBaseActivity;
import com.orientdata.lookforcustomers.util.CommonUtils;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.widget.MyTitle;

/**
 * Created by wy on 2017/12/6.
 * 测试号设置
 */

public class TestPhoneSettingActivity extends WangrunBaseActivity {
    private MyTitle myTitle;
    private EditText tvMove, tvUnicom, tvTelecom;
    private Button btSave;

    private String testCmPhone = "";//移动测试号
    private String testCuPhone = "";//联通测试号
    private String testCtPhone = "";//电信测试号

    private TextView tv_show_info;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_phone_set);
        initView();
        initTitle();
        updateView();
    }

    private void initView() {
        testCmPhone = getIntent().getStringExtra("testCmPhone");
        testCuPhone = getIntent().getStringExtra("testCuPhone");
        testCtPhone = getIntent().getStringExtra("testCtPhone");
        String mCityName = getIntent().getStringExtra("cityName");
        tv_show_info = findViewById(R.id.tv_show_info);
        tv_show_info.setText("请添加" + mCityName + "本地号码");
        myTitle = findViewById(R.id.myTitle);
        tvMove = findViewById(R.id.tvMove);
        tvUnicom = findViewById(R.id.tvUnicom);
        tvTelecom = findViewById(R.id.tvTelecom);
        if (!TextUtils.isEmpty(testCmPhone)) {
            tvMove.setText(testCmPhone);
        }
        if (!TextUtils.isEmpty(testCuPhone)) {
            tvUnicom.setText(testCuPhone);
        }
        if (!TextUtils.isEmpty(testCtPhone)) {
            tvTelecom.setText(testCtPhone);
        }
        btSave = findViewById(R.id.btSave);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testCmPhone = tvMove.getText().toString().trim();
                if (!TextUtils.isEmpty(testCmPhone)) {
                    if (!CommonUtils.isPhoneNum(testCmPhone)) {
                        ToastUtils.showShort("请填写正确的移动号码。");
                        return;
                    }
                }

                testCuPhone = tvUnicom.getText().toString().trim();
                if (!TextUtils.isEmpty(testCuPhone)) {
                    if (!CommonUtils.isPhoneNum(testCuPhone)) {
                        ToastUtils.showShort("请填写正确的联通号码。");
                        return;
                    }
                }
                testCtPhone = tvTelecom.getText().toString().trim();
                if (!TextUtils.isEmpty(testCtPhone)) {
                    if (!CommonUtils.isPhoneNum(testCtPhone)) {
                        ToastUtils.showShort("请填写正确的电信号码。");
                        return;
                    }
                }
                /*if (TextUtils.isEmpty(testCmPhone)
                        && TextUtils.isEmpty(testCuPhone)
                        && TextUtils.isEmpty(testCtPhone)) {
                    ToastUtils.showShort("请完善信息");
                    return;
                }*/
                Intent intent = new Intent();
                intent.putExtra("testCmPhone", testCmPhone);
                intent.putExtra("testCuPhone", testCuPhone);
                intent.putExtra("testCtPhone", testCtPhone);
                setResult(2, intent);
                finish();
            }
        });

    }

    private void initTitle() {
        myTitle.setTitleName("测试号设置");
        myTitle.setImageBack(this);

    }

    private void updateView() {

    }
}
