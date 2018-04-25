package com.orientdata.lookforcustomers.view.mine;

import android.os.Bundle;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.WangrunBaseActivity;
import com.orientdata.lookforcustomers.widget.MyTitle;

/**
 * 说明
 */
public class DescriptionActivity extends WangrunBaseActivity {
    private MyTitle title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        initView();
        initTitle();

    }


    private void initView() {
        title = findViewById(R.id.my_title);

    }

    private void initTitle() {
        title.setTitleName("活动说明");
        title.setImageBack(this);
    }


}
