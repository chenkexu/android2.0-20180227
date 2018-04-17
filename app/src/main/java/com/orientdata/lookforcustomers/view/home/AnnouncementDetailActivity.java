package com.orientdata.lookforcustomers.view.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.WangrunBaseActivity;
import com.orientdata.lookforcustomers.bean.AnnouncementBean;
import com.orientdata.lookforcustomers.bean.AnnouncementBeanOut;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;
import com.orientdata.lookforcustomers.util.DateTool;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.widget.MyTitle;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * 公告详情页面
 */
public class AnnouncementDetailActivity extends WangrunBaseActivity {

    private AnnouncementBean mTopAnnouncementId;
    private AnnouncementBean mTopAnnouncement;
    private MyTitle title;
    private TextView tv_topic;
    private TextView tv_content;
    private TextView tv_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_detail);
        initIntentData();
        initView();
        initTitle();
        //initData();
        getData();
    }

    private void getData() {
        showDefaultLoading();
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());
        if(mTopAnnouncementId!=null){
            map.put("announcementId", mTopAnnouncementId.getAnnouncementId() + "");
        }


        OkHttpClientManager.postAsyn(HttpConstant.SELECT_ANNOUNCEMENT_INFO, new OkHttpClientManager.ResultCallback<AnnouncementBeanOut>() {
            @Override
            public void onError(Exception e) {
                ToastUtils.showShort(e.getMessage());
                hideDefaultLoading();
            }

            @Override
            public void onResponse(AnnouncementBeanOut response) {
                hideDefaultLoading();
                if (response == null) {
                    ToastUtils.showShort("获取失败");
                    return;
                }
                if (response.getCode() == 0) {
                    mTopAnnouncement = response.getResult();

                    initData();

                } else {
                    ToastUtils.showShort("获取失败");
                }
            }
        }, map);
    }

    private void initData() {
        if (mTopAnnouncement != null) {
            tv_time.setText(DateTool.parseDate2Str(mTopAnnouncement.getCreateDate(), "yyyy-MM-dd HH:mm"));
            tv_topic.setText(mTopAnnouncement.getTitle());
            tv_content.setText(mTopAnnouncement.getContent());
        }
    }

    private void initView() {
        title = findViewById(R.id.my_title);
        tv_content = findViewById(R.id.tv_content);
        tv_time = findViewById(R.id.tv_time);
        tv_topic = findViewById(R.id.tv_topic);
    }

    private void initIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            mTopAnnouncementId = (AnnouncementBean) intent.getSerializableExtra("mTopAnnouncement");
        }
    }

    private void initTitle() {
        title.setTitleName("公告详情");
        title.setImageBack(this);
       /* title.setRightText(R.string.test);
        title.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getBaseContext(), TestPhoneSettingActivity.class), 1);
            }
        });*/
    }
}
