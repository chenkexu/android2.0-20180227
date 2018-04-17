package com.orientdata.lookforcustomers.view.findcustomer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.WangrunBaseActivity;
import com.orientdata.lookforcustomers.widget.MyTitle;

/**
 * Created by wy on 2017/12/6.
 * 测试号
 */

public class TestPhoneListActivity extends WangrunBaseActivity{
    private MyTitle myTitle;
    private TextView tvMove,tvUnicom,tvTelecom;
    private String testCmPhone = "";
    private String testCuPhone = "";
    private String testCtPhone = "";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_phone);
        initView();
        initTitle();
        updateView();
    }
    private void initView(){
        testCmPhone = getIntent().getStringExtra("testCmPhone");
        testCuPhone = getIntent().getStringExtra("testCuPhone");
        testCtPhone = getIntent().getStringExtra("testCtPhone");
        myTitle = findViewById(R.id.myTitle);
        tvMove = findViewById(R.id.tvMove);
        tvUnicom = findViewById(R.id.tvUnicom);
        tvTelecom = findViewById(R.id.tvTelecom);
        tvMove.setText(testCmPhone);
        tvUnicom.setText(testCuPhone);
        tvTelecom.setText(testCtPhone);


    }
    private void initTitle(){
        myTitle.setTitleName("测试号查看");
        myTitle.setImageBack(this);

    }
    private void updateView(){

    }
}
