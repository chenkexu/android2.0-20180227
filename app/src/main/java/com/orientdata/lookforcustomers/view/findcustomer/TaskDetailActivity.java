package com.orientdata.lookforcustomers.view.findcustomer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.SettingOut;
import com.orientdata.lookforcustomers.bean.TaskOut;
import com.orientdata.lookforcustomers.bean.UploadPicBean;
import com.orientdata.lookforcustomers.event.DeleteTaskEvent;
import com.orientdata.lookforcustomers.event.TaskInfoEvent;
import com.orientdata.lookforcustomers.presenter.TaskPresent;
import com.orientdata.lookforcustomers.util.SharedPreferencesTool;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.ImagePagerActivity;
import com.orientdata.lookforcustomers.view.certification.fragment.ACache;
import com.orientdata.lookforcustomers.widget.MyTitle;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wy on 2017/11/25.
 * 寻客管理-任务详情
 */
public class TaskDetailActivity extends BaseActivity<ITaskView, TaskPresent<ITaskView>> implements ITaskView {
    @BindView(R.id.myTitle)
    MyTitle myTitle;
    @BindView(R.id.iv_order_image1)
    ImageView ivOrderImage1;
    @BindView(R.id.iv_order_image2)
    ImageView ivOrderImage2;
    @BindView(R.id.tv_order_id)
    TextView tvOrderId;
    @BindView(R.id.tv_order_create_time)
    TextView tvOrderCreateTime;

    @BindView(R.id.tv_order_err_descr)
    TextView tv_order_err_descr;

    @BindView(R.id.orderView)
    OrderDetailView orderDetailView;

    @BindView(R.id.report_result)
    PushResultReport reportResult;

    @BindView(R.id.task_delivery)
    TaskDeliveryView taskDeliveryView;


    @BindView(R.id.cd_task_delivery2)
    CardView cardView;

    private TextView tvType;
    private TextView tvDirection;
    private ImageView ivDirection;
    private TextView taskId, taskName, taskBudget, runTime, runAddress, runRadius, tvImgUrl;
    private ImageView ivAd;
    private TextView searchMoney, displayNum, clickNum, tvTestNum, tvDelete;
    private int task_id = 0;
    private TaskOut taskOut;
    private LinearLayout linearMsg, linearAd;
    private TextView tvMsgContent;
    private TextView tvStatus;
    private LinearLayout linearPage, linearMessage;
    private TextView issuNum, msgMoney;
    private ACache aCache = null;//数据缓存
    private String imagePath;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        ButterKnife.bind(this);
        initTitle();
        initView();
        mPresent.getTaskDetail(task_id);
    }

    private void initView() {
        task_id = getIntent().getIntExtra("taskId",0);
        taskDeliveryView.hideView();
    }




    private void initTitle() {
        myTitle.setRightText("再次创建");
        myTitle.setTitleName("订单详情");
        myTitle.setImageBack(this);
        myTitle.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double userStatus = Double.parseDouble(aCache.getAsString(SharedPreferencesTool.USER_STATUS));
                if (userStatus == 2.0) {
                    //黑名单
                    ToastUtils.showShort("账户异常，请联系客服");
                } else {
                    Intent intent = new Intent(getBaseContext(), CreateFindCustomerActivity.class);
                    intent.putExtra("isReCreate", true);
                    intent.putExtra("taskOut", taskOut);
                    startActivity(intent);
                    closeActivity(TaskDetailActivity.class);
                }
            }
        });

    }




    /**
     * 任务状态
     *
     * @param status
     * @return
     */
    private String getStatus(int status) {
        String type = "";
        if (status == 1 || status == 3 || status == 5) {
            type = "审核中";
        } else if (status == 2 || status == 4) {
            type = "审核失败";
        } else if (status == 6) {
            type = "待投放";
        } else if (status == 7) {
            type = "投放中";
        } else if (status == 8) {
            type = "投放结束";
        }
        return type;
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
    public void taskInfoReslt(TaskInfoEvent taskInfoEvent) {
        if (taskInfoEvent.taskInfoBean.getCode() == 0) {
            taskOut = taskInfoEvent.taskInfoBean.getResult();
            tvOrderId.setText("订单ID："+taskOut.getTaskId());
            tvOrderCreateTime.setText("创建时间："+taskOut.getCreateDate());
            String status = getStatus(taskOut.getStatus());
            orderDetailView.setData(taskOut);
            reportResult.setData(taskOut);

            switch (status){
                case "审核中":
                    ivOrderImage1.setImageResource(R.mipmap.order_detail_ing);
                    ivOrderImage2.setImageResource(R.mipmap.order_big_ing);
                    break;
                case "审核失败":
                    ivOrderImage1.setImageResource(R.mipmap.order_detail_error);
                    ivOrderImage2.setImageResource(R.mipmap.order_big_error);
                    break;
                case "待投放":
                    cardView.setVisibility(View.VISIBLE);
                    ivOrderImage1.setImageResource(R.mipmap.order_detail_pre);
                    ivOrderImage2.setImageResource(R.mipmap.order_big_suc);
                    break;
                case "投放中":
                    ivOrderImage1.setImageResource(R.mipmap.order_detail_suc);
                    ivOrderImage2.setVisibility(View.GONE);
                    break;
                case "投放结束":
                    reportResult.setVisibility(View.VISIBLE);
                    ivOrderImage1.setImageResource(R.mipmap.order_detail_over);
                    ivOrderImage2.setVisibility(View.GONE);
                    break;
            }

        }

    }


    @Subscribe
    public void deleteTask(DeleteTaskEvent deleteTaskEvent) {
        if (deleteTaskEvent.errBean.getCode() == 0) {
            ToastUtils.showShort("删除成功！");
            closeActivity(TaskDetailActivity.class);
        }

    }

    private void showBigPhoto(ImageView view, String path) {
        ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
        List<String> photoUrls = new ArrayList<>();
        photoUrls.add(path);
        ImagePagerActivity.startImagePagerActivity(this, photoUrls, 0, imageSize);
    }
}
