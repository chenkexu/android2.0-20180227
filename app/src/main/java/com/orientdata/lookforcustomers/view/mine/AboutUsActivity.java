package com.orientdata.lookforcustomers.view.mine;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.WangrunBaseActivity;
import com.orientdata.lookforcustomers.widget.MyTitle;

import vr.md.com.mdlibrary.AppConfig;

import static com.orientdata.lookforcustomers.app.MyApplication.getContext;

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
        tvVersion.setText("版    本：V"+getVersion(this));
    }

    public static String getVersion(Context context)//获取版本号
    {
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return  "2.1.0";
        }
    }
}
