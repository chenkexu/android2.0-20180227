package com.orientdata.lookforcustomers.view.findcustomer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.bean.TaskOut;
import com.orientdata.lookforcustomers.util.AnimationUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ckx on 2018/6/21.
 */

public class OrderDetailView extends FrameLayout {


    @BindView(R.id.cd_task_order)
    CardView cdTaskOrder;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_user_hobby)
    TextView tvUserHobby;
    @BindView(R.id.cd_task_order_person)
    CardView cdTaskOrderPerson;
    @BindView(R.id.tv_order_address)
    TextView tvOrderAddress;
    @BindView(R.id.tv_order_radius)
    TextView tvOrderRadius;
    @BindView(R.id.cd_task_order_area)
    CardView cdTaskOrderArea;
    @BindView(R.id.tv_order_name)
    TextView tvOrderName;
    @BindView(R.id.tv_order_sigture)
    TextView tvOrderSigture;
    @BindView(R.id.tv_order_content)
    TextView tvOrderContent;
    @BindView(R.id.cd_task_order_content)
    CardView cdTaskOrderContent;
    @BindView(R.id.tv_order_push_person)
    TextView tvOrderPushPerson;
    @BindView(R.id.tv_order_push_time)
    TextView tvOrderPushTime;
    @BindView(R.id.tv_order_test_phone)
    TextView tvOrderTestPhone;
    @BindView(R.id.cd_task_order_push)
    CardView cdTaskOrderPush;

    @BindView(R.id.layout_title)
    LinearLayout layoutTitle;

    @BindView(R.id.tv_order_detail)
    TextView tvOrderde;
    Drawable image_up, img_right;

    private TranslateAnimation mShowAction;
    private TranslateAnimation mHiddenAction;

    private boolean isShow = true;

    public OrderDetailView(@NonNull Context context) {
        super(context, null);
    }



    public OrderDetailView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context)
                .inflate(R.layout.include_order_detail, this);
        ButterKnife.bind(view);

        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);

        mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f);
        mHiddenAction.setDuration(500);
        Resources res = getResources();
        image_up = res.getDrawable(R.mipmap.order_down);
        img_right = res.getDrawable(R.mipmap.order_right);
        image_up.setBounds(0, 0, image_up.getMinimumWidth(), image_up.getMinimumHeight());
        img_right.setBounds(0, 0, img_right.getMinimumWidth(), img_right.getMinimumHeight());
    }



    public void hideTitle(){
        cdTaskOrder.setVisibility(GONE);
    }



    public void setData(TaskOut taskOut) {
        if (taskOut != null) {
            tvSex.setText("性别：" + taskOut.getOrientationSettingsOut().getSex());
            tvAge.setText("年龄：" + taskOut.getOrientationSettingsOut().getAgeF() + "-" + taskOut.getOrientationSettingsOut().getAgeB());

            int customFlag = taskOut.getCustomFlag();

            StringBuilder hobbyStr = new StringBuilder();
            List<String> hobbyList = taskOut.getOrientationSettingsOut().getXingqu();
            if (hobbyList != null) {
                for (int i = 0; i < hobbyList.size(); i++) {
                    hobbyStr.append((i == hobbyList.size() - 1) ? hobbyList.get(i) : hobbyList.get(i) + "、");
                }
            }
            if (customFlag == 0) {  //使用行业
                tvUserHobby.setText("用户偏好：" +taskOut.getIndustryName());
            } else { //使用自定义
                tvUserHobby.setText("用户偏好：" +"自定义：" + hobbyStr.toString());
            }

            tvOrderRadius.setText("投放范围：" + taskOut.getRangeRadius());
            tvOrderAddress.setText("投放地址：" + taskOut.getThrowAddress());
            tvOrderName.setText("订单名称：" + taskOut.getTaskName());
            tvOrderContent.setText("短信内容：" + taskOut.getContent());
            tvOrderSigture.setText("短信签名：" + taskOut.getContent().substring(0, taskOut.getContent().indexOf("】") + 1));
            tvOrderPushTime.setText("推送时间：" + taskOut.getThrowStartdate());

            tvOrderPushPerson.setText("推送人数：" + taskOut.getEstimatePeoplerno()+"人");
            if (!TextUtils.isEmpty(taskOut.getTestCmPhone()) && !TextUtils.isEmpty(taskOut.getTestCtPhone())
                    && !TextUtils.isEmpty(taskOut.getTestCuPhone())) {
                tvOrderTestPhone.setText("测试号码：" + "" + taskOut.getTestCmPhone() +
                        "," + taskOut.getTestCuPhone() + "," + taskOut.getTestCtPhone());
            }else{
                tvOrderTestPhone.setVisibility(View.GONE);
            }

        }
    }



    @OnClick({R.id.cd_task_order, R.id.tv_sex})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cd_task_order:
                if (isShow) {
                    tvOrderde.setCompoundDrawables(null, null, img_right, null);
                    AnimationUtil.with().showAndHiddenAnimation(cdTaskOrderArea, AnimationUtil.AnimationState.STATE_HIDDEN,1000);
                    AnimationUtil.with().showAndHiddenAnimation(cdTaskOrderPerson, AnimationUtil.AnimationState.STATE_HIDDEN,1000);
                    AnimationUtil.with().showAndHiddenAnimation(cdTaskOrderContent, AnimationUtil.AnimationState.STATE_HIDDEN,1000);
                    AnimationUtil.with().showAndHiddenAnimation(cdTaskOrderPush, AnimationUtil.AnimationState.STATE_HIDDEN,1000);
                    isShow = false;
                }else{
                    tvOrderde.setCompoundDrawables(null, null, image_up, null);
                    AnimationUtil.with().showAndHiddenAnimation(cdTaskOrderArea, AnimationUtil.AnimationState.STATE_SHOW,1000);
                    AnimationUtil.with().showAndHiddenAnimation(cdTaskOrderPerson, AnimationUtil.AnimationState.STATE_SHOW,1000);
                    AnimationUtil.with().showAndHiddenAnimation(cdTaskOrderContent, AnimationUtil.AnimationState.STATE_SHOW,1000);
                    AnimationUtil.with().showAndHiddenAnimation(cdTaskOrderPush, AnimationUtil.AnimationState.STATE_SHOW,1000);
                    isShow = true;
                }

                break;

        }
    }
}
