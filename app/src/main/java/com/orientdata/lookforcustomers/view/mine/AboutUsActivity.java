package com.orientdata.lookforcustomers.view.mine;

import android.os.Bundle;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.WangrunBaseActivity;
import com.orientdata.lookforcustomers.widget.MyTitle;

import vr.md.com.mdlibrary.AppConfig;

/**
 * 关于我们
 */
public class AboutUsActivity extends WangrunBaseActivity {
    private MyTitle title;
    private TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initView();
        initTitle();

    }


    private void initView() {
        title = findViewById(R.id.my_title);
        tvVersion = findViewById(R.id.tvVersion);

    }

    private void initTitle() {
        title.setTitleName("关于我们");
        title.setImageBack(this);
        tvVersion.setText("版    本：V"+AppConfig.VER);
    }


}
