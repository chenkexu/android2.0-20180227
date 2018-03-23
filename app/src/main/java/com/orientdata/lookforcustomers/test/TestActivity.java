package com.orientdata.lookforcustomers.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.view.certification.CustomizeCredentialUploadView;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        LinearLayout ll = (LinearLayout) findViewById(R.id.test_activity_container);
        CustomizeCredentialUploadView cv = new CustomizeCredentialUploadView(this);
        cv.initRadioGroup(this,new String[]{"nihao1","nihao2"});
        ll.addView(cv);
    }
}
