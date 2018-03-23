package com.orientdata.lookforcustomers.view.findcustomer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.WangrunBaseActivity;
import com.orientdata.lookforcustomers.bean.OrientationSettingsOut;
import com.orientdata.lookforcustomers.widget.MyTitle;

import java.util.List;

/**
 * Created by wy on 2017/12/6.
 * 定向设置详情页
 */

public class DirectionDetailActivity extends WangrunBaseActivity{
    private MyTitle myTitle;
    private TextView userAge,sex,edu,ascription,consumptionCapacity,model,hobby;
    private OrientationSettingsOut orientationSetting = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction_detail);
        initView();
        initTitle();
        updateView();
    }
    private void initView(){
        orientationSetting = (OrientationSettingsOut) getIntent().getSerializableExtra("orientationSetting");
        myTitle = findViewById(R.id.myTitle);
        userAge = findViewById(R.id.userAge);
        sex = findViewById(R.id.sex);
        edu = findViewById(R.id.edu);
        ascription = findViewById(R.id.ascription);
        consumptionCapacity = findViewById(R.id.consumptionCapacity);
        model = findViewById(R.id.model);
        hobby = findViewById(R.id.hobby);
    }
    private void initTitle(){
        myTitle.setTitleName("定向设置");
        myTitle.setImageBack(this);
    }
    private void updateView(){
        if(orientationSetting!=null){
            userAge.setText(orientationSetting.getAgeF()+"-"+orientationSetting.getAgeB());
            sex.setText(orientationSetting.getSex());
            edu.setText(orientationSetting.getEducationLevelF()+"-"+orientationSetting.getEducationLevelB());
            ascription.setText(orientationSetting.getAscription());
            consumptionCapacity.setText(orientationSetting.getConsumptionCapacityF()+"-"+orientationSetting.getConsumptionCapacityB());
            StringBuilder modelStr = new StringBuilder();
            List<String> modelList = orientationSetting.getJixing();
            if(modelList!=null){
                for(int i=0;i<modelList.size();i++){
                    modelStr.append((i==modelList.size()-1)?modelList.get(i):modelList.get(i)+",");
                }
            }
            model.setText(modelStr.toString());
            StringBuilder hobbyStr = new StringBuilder();
            List<String> hobbyList = orientationSetting.getXingqu();
            if(hobbyList!=null){
                for(int i=0;i<hobbyList.size();i++){
                    hobbyStr.append((i==hobbyList.size()-1)?hobbyList.get(i):hobbyList.get(i)+"、");
                }
            }
            hobby.setText(hobbyStr.toString());
        }
    }
}
