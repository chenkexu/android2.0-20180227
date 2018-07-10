package com.orientdata.lookforcustomers.view.home.imple;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.MessageAndNoticeBean;
import com.orientdata.lookforcustomers.event.MsgInfoEvent;
import com.orientdata.lookforcustomers.presenter.MsgPresent;
import com.orientdata.lookforcustomers.view.home.IMsgView;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.qiniu.android.common.Constants;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by wy on 2017/12/19.
 */

public class MsgDetailActivity extends BaseActivity<IMsgView, MsgPresent<IMsgView>> implements IMsgView, View.OnClickListener{
    private TextView tvContent;
    private MyTitle title;
    private TextView tvDate;
    private String content = "";
    private String date = "";
    private int pushMessageId = -1;
    private TextView tvTitle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_detail);
        initView();
        initTitle();
    }

    private void initView(){
//        pushMessageId = getIntent().getIntExtra("pushMessageId",-1);
//
        tvDate = findViewById(R.id.tvDate);
        tvTitle = findViewById(R.id.tvTitle);
        title = findViewById(R.id.title);
        tvContent = findViewById(R.id.tvContent);


        Intent intent = getIntent();
        MessageAndNoticeBean  messageAndNoticeBean  = (MessageAndNoticeBean) intent.getSerializableExtra(Constants.MessageAndNoticeBean);
        if (messageAndNoticeBean.getPushMessageId() !=0) {
            mPresent.msgInfo(messageAndNoticeBean.getPushMessageId());
        }
        if (messageAndNoticeBean.getAnnouncementId() !=0) {
            mPresent.updateUserAnnStatus(messageAndNoticeBean.getAnnouncementId());
        }
    }




    private void initTitle(){
        title.setTitleName("消息详情");
        title.setImageBack(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPause() {
//        setResult(1);
        super.onPause();
    }

    @Override
    public void showLoading() {
        showDefaultLoading();
    }

    @Override
    public void hideLoading() {
        hideDefaultLoading();
    }




    @Subscribe
    public void msgInfo(MsgInfoEvent msgInfoEvent){


//        if(msgInfoEvent!=null){
//            ResultBean result = msgInfoEvent.result;
//            if(result!=null && result.getCode() == 0){
//                Result result1 = result.getResult();
//                String date = "";
//                if(!TextUtils.isEmpty(result1.getCreateTime())){
//                    date = result1.getCreateTime();
//                }else if(!TextUtils.isEmpty(result1.getUpdateTime())){
//                    date = result1.getUpdateTime();
//                }
//                updateView(result1.getTitle(),result1.getText(),date);
//            }
//        }
    }




    @Override
    public void selectMsgAndAnnouncement(MessageAndNoticeBean messageAndNoticeBean) {

        tvTitle.setText(messageAndNoticeBean.getTitle());
        if (messageAndNoticeBean.getCreateDate() != null) {
            tvDate.setText(messageAndNoticeBean.getCreateDate());
        }
        if (messageAndNoticeBean.getCreateTime() != null) {
            tvDate.setText(messageAndNoticeBean.getCreateTime());
        }

        if (messageAndNoticeBean.getContent() != null) {
            tvContent.setText(messageAndNoticeBean.getContent());
        }
        if (messageAndNoticeBean.getText() != null) {
            tvContent.setText(messageAndNoticeBean.getText());
        }

    }


    @Override
    protected MsgPresent<IMsgView> createPresent() {
        return new MsgPresent<>(this);
    }
}
