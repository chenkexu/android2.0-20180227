package com.orientdata.lookforcustomers.view.findcustomer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.SettingOut;
import com.orientdata.lookforcustomers.bean.TaskOut;
import com.orientdata.lookforcustomers.bean.UploadPicBean;
import com.orientdata.lookforcustomers.event.DeleteTaskEvent;
import com.orientdata.lookforcustomers.event.TaskInfoEvent;
import com.orientdata.lookforcustomers.presenter.TaskPresent;
import com.orientdata.lookforcustomers.util.CommonUtils;
import com.orientdata.lookforcustomers.util.GlideUtil;
import com.orientdata.lookforcustomers.util.SharedPreferencesTool;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.certification.fragment.ACache;
import com.orientdata.lookforcustomers.widget.MyTitle;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by wy on 2017/11/25.
 * 寻客管理-任务详情
 */

public class TaskDetailActivity extends BaseActivity<ITaskView, TaskPresent<ITaskView>> implements ITaskView,View.OnClickListener{
    private MyTitle myTitle;
    private TextView tvType;
    private TextView tvDirection;
    private ImageView ivDirection;
    private TextView taskId,taskName,taskBudget,runTime,runAddress,runRadius,tvImgUrl;
    private ImageView ivAd;
    private TextView searchMoney,displayNum,clickNum,tvTestNum,tvDelete;
    private int task_id = 0;
    private TaskOut taskOut;
    private LinearLayout linearMsg,linearAd;
    private TextView tvMsgContent;
    private TextView tvStatus;
    private LinearLayout linearPage,linearMessage;
    private TextView issuNum,msgMoney;
    private ACache aCache = null;//数据缓存


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        initView();
        initTitle();
        mPresent.getTaskDetail(task_id);
    }
    private void initView(){
        aCache = ACache.get(this);
        task_id = getIntent().getIntExtra("taskId",0);
        myTitle = findViewById(R.id.myTitle);
        tvType = findViewById(R.id.tvType);
        taskId = findViewById(R.id.taskId);
        tvDirection = findViewById(R.id.tvDirection);
        ivDirection = findViewById(R.id.ivDirection);
        taskName = findViewById(R.id.taskName);
        taskBudget = findViewById(R.id.taskBudget);
        runTime = findViewById(R.id.runTime);
        runAddress = findViewById(R.id.runAddress);
        runRadius = findViewById(R.id.runRadius);
        tvImgUrl = findViewById(R.id.tvImgUrl);
        tvStatus = findViewById(R.id.tvStatus);
        ivAd = findViewById(R.id.ivAd);
        msgMoney = findViewById(R.id.msgMoney);
        linearPage = findViewById(R.id.linearPage);
        linearMessage = findViewById(R.id.linearMessage);
        issuNum = findViewById(R.id.issuNum);
        searchMoney = findViewById(R.id.searchMoney);
        displayNum = findViewById(R.id.displayNum);
        clickNum = findViewById(R.id.clickNum);
        tvTestNum = findViewById(R.id.tvTestNum);
        tvDelete = findViewById(R.id.tvDelete);
        linearAd = findViewById(R.id.linearAd);
        linearMsg = findViewById(R.id.linearMsg);
        tvMsgContent = findViewById(R.id.tvMsgContent);
        tvDirection.setOnClickListener(this);
        ivDirection.setOnClickListener(this);
        tvTestNum.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
    }
    private void initTitle(){
        myTitle.setRightText("再次创建");
        myTitle.setTitleName("寻客管理");
        myTitle.setImageBack(this);
        myTitle.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double userStatus = Double.parseDouble(aCache.getAsString(SharedPreferencesTool.USER_STATUS));
                if(userStatus == 2.0){
                    //黑名单
                    ToastUtils.showShort("账户异常，请联系客服");
                }else{
                    Intent intent = new Intent(getBaseContext(),CreateFindCustomerActivity.class);
                    intent.putExtra("isReCreate",true);
                    intent.putExtra("taskOut",taskOut);
                    startActivity(intent);
                    closeActivity(TaskDetailActivity.class);
                }
            }
        });
    }
    private void updateView(){
        if(taskOut != null){
            if(taskOut.getType()==1){
                linearAd.setVisibility(View.GONE);
                linearMsg.setVisibility(View.VISIBLE);
                tvType.setText("任务类型：短信");
                tvMsgContent.setText(taskOut.getContent());
                linearMessage.setVisibility(View.VISIBLE);
                linearPage.setVisibility(View.GONE);
                issuNum.setText(taskOut.getIssuedCount()+"");
                msgMoney.setText(taskOut.getXunMoney()+"");
            }else{
                linearAd.setVisibility(View.VISIBLE);
                linearMsg.setVisibility(View.GONE);
                tvType.setText("任务类型：页面");
                if(taskOut.getTemplateUrl() == null){
                    tvImgUrl.setText("图片链接：");
                }else{
                    tvImgUrl.setText("图片链接："+taskOut.getTemplateUrl());
                }
                if(!TextUtils.isEmpty(taskOut.getAdImgid())){
//                    Glide.with(this).load(taskOut.getAdImgid()).into(ivAd);
                    GlideUtil.getInstance().loadAdImage(this,ivAd,taskOut.getAdImgid(),true);
                }
                linearMessage.setVisibility(View.GONE);
                linearPage.setVisibility(View.VISIBLE);
                searchMoney.setText(taskOut.getXunMoney()+"");
                displayNum.setText(taskOut.getIssuedCount()+"");
                clickNum.setText(taskOut.getClickCount()+"");
            }
            taskId.setText(taskOut.getTaskId()+"");
            tvStatus.setText(getStatus(taskOut.getStatus()));
            taskName.setText(taskOut.getTaskName());
            taskBudget.setText(taskOut.getBudget().intValue()+"元");
            runTime.setText(CommonUtils.getDateStr(taskOut.getThrowStartdate(),"yyyy/MM/dd")+"-"+CommonUtils.getDateStr(taskOut.getThrowEnddate(),"yyyy/MM/dd"));
            runAddress.setText(taskOut.getThrowAddress());
            runRadius.setText(taskOut.getRangeRadius());

        }
    }

    /**
     *任务状态
     * @param status
     * @return
     */
    private String getStatus(int status){
        String type = "";
        if(status == 1 || status ==3 ||status == 5 ){
            type = "审核中";
        }else if(status == 2 || status == 4){
            type = "审核失败";
        }else if(status == 6){
            type = "待投放";
        }else if(status == 7){
            type = "投放中";
        }else if(status == 8){
            type = "投放结束";
        }
        return type;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivDirection:
            case R.id.tvDirection:
                //定向设置页面
                Intent intent = new Intent(this,DirectionDetailActivity.class);
//                intent.putExtra("orientationSetting",taskOut.getOrientationSettingsOut());
                intent.putExtra("taskOut",taskOut);

                startActivity(intent);
                break;
            case R.id.tvTestNum:
                //测试号
                Intent intent1 = new Intent(this,TestPhoneListActivity.class);
                intent1.putExtra("testCmPhone",taskOut.getTestCmPhone());
                intent1.putExtra("testCuPhone",taskOut.getTestCuPhone());
                intent1.putExtra("testCtPhone",taskOut.getTestCtPhone());
                startActivity(intent1);
                break;
            case R.id.tvDelete:
                mPresent.deletTask(task_id);
                break;
        }

    }

    @Override
    public void showLoading() {
        showDefaultLoading();
    }

    @Override
    public void hideLoading() {
        hideDefaultLoading();
    }

    @Override
    public void createCustomer(SettingOut settingOuts) {

    }

    @Override
    public void uploadPicSuc(UploadPicBean uploadPicBean) {

    }

    @Override
    protected TaskPresent<ITaskView> createPresent() {
        return new TaskPresent<>(this);
    }

    @Subscribe
    public void taskInfoReslt(TaskInfoEvent taskInfoEvent){
        if(taskInfoEvent.taskInfoBean.getCode() == 0){
            taskOut = taskInfoEvent.taskInfoBean.getResult();
            updateView();
        }

    }
    @Subscribe
    public void deleteTask(DeleteTaskEvent deleteTaskEvent){
        if(deleteTaskEvent.errBean.getCode() == 0){
            ToastUtils.showShort("删除成功！");
            closeActivity(TaskDetailActivity.class);
        }

    }
}
