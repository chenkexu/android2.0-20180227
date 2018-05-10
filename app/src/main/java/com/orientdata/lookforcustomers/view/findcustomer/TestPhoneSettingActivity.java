package com.orientdata.lookforcustomers.view.findcustomer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.model.Response;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.WangrunBaseActivity;
import com.orientdata.lookforcustomers.bean.MessageTaskCacheBean;
import com.orientdata.lookforcustomers.bean.WrResponse;
import com.orientdata.lookforcustomers.network.callback.WrCallback;
import com.orientdata.lookforcustomers.network.util.NetWorkUtils;
import com.orientdata.lookforcustomers.util.CommonUtils;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.widget.MyTitle;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wy on 2017/12/6.
 * 测试号设置，，请求接口
 */

public class TestPhoneSettingActivity extends WangrunBaseActivity {
    @BindView(R.id.ll_cmcc)
    LinearLayout llCmcc;
    @BindView(R.id.ll_cucc)
    LinearLayout llCucc;
    @BindView(R.id.ll_ctc)
    LinearLayout llCtc;
    private MyTitle myTitle;
    private EditText tvMove, tvUnicom, tvTelecom;
    private Button btSave;

    private String testCmPhone = "";//移动测试号
    private String testCuPhone = "";//联通测试号
    private String testCtPhone = "";//电信测试号
    private TextView tv_show_info;
    private MessageTaskCacheBean messageTaskCacheBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_phone_set);
        ButterKnife.bind(this);
        initView();
        initTitle();
        updateView();
    }

    private void initView() {
//        boolean isReCreate = getIntent().getBooleanExtra("isReCreate", false);
//        if (!isReCreate) {
//            messageTaskCacheBean = (MessageTaskCacheBean) SharedPreferencesTool.getInstance().getObjectFromShare(SharedPreferencesTool.MessageTaskCacheBean);
//        }
//
//        if (messageTaskCacheBean!=null) {
//            testCmPhone = messageTaskCacheBean.getTestCmPhone();
//            testCuPhone = messageTaskCacheBean.getTestCuPhone();
//            testCtPhone = messageTaskCacheBean.getTestCtPhone();
//        }

        testCmPhone = getIntent().getStringExtra("testCmPhone");
        testCuPhone = getIntent().getStringExtra("testCuPhone");
        testCtPhone = getIntent().getStringExtra("testCtPhone");


        String mCityName = getIntent().getStringExtra("cityName");
        String cityCode = getIntent().getStringExtra("cityCode");
        int type = getIntent().getIntExtra("type", 0);

        NetWorkUtils.getOperateState(cityCode, type, new WrCallback<WrResponse<String>>() {
            @Override
            public void onSuccess(Response<WrResponse<String>> response) {
                String result = response.body().getResult();
                if (!result.contains("1")) {
                    llCmcc.setVisibility(View.GONE);
                }
                if (!result.contains("2")) {
                    llCucc.setVisibility(View.GONE);
                }
                if (!result.contains("3")) {
                    llCtc.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Response<WrResponse<String>> response) {
                super.onError(response);
                ToastUtils.showShort(response.getException().getMessage());
            }
        });


        tv_show_info = findViewById(R.id.tv_show_info);
        tv_show_info.setText("请添加" + mCityName + "本地号码");
        myTitle = findViewById(R.id.myTitle);
        tvMove = findViewById(R.id.tvMove);
        tvUnicom = findViewById(R.id.tvUnicom);
        tvTelecom = findViewById(R.id.tvTelecom);
        if (!TextUtils.isEmpty(testCmPhone)) {
            tvMove.setText(testCmPhone);
        }
        if (!TextUtils.isEmpty(testCuPhone)) {
            tvUnicom.setText(testCuPhone);
        }
        if (!TextUtils.isEmpty(testCtPhone)) {
            tvTelecom.setText(testCtPhone);
        }
        btSave = findViewById(R.id.btSave);
        btSave.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                testCmPhone = tvMove.getText().toString().trim();
                if (!TextUtils.isEmpty(testCmPhone)) {
                    if (!CommonUtils.isPhoneNum(testCmPhone)) {
                        ToastUtils.showShort("请填写正确的移动号码。");
                        return;
                    }
                }

                testCuPhone = tvUnicom.getText().toString().trim();
                if (!TextUtils.isEmpty(testCuPhone)) {
                    if (!CommonUtils.isPhoneNum(testCuPhone)) {
                        ToastUtils.showShort("请填写正确的联通号码。");
                        return;
                    }
                }
                testCtPhone = tvTelecom.getText().toString().trim();
                if (!TextUtils.isEmpty(testCtPhone)) {
                    if (!CommonUtils.isPhoneNum(testCtPhone)) {
                        ToastUtils.showShort("请填写正确的电信号码。");
                        return;
                    }
                }
                Intent intent = new Intent();
                intent.putExtra("testCmPhone", testCmPhone);
                intent.putExtra("testCuPhone", testCuPhone);
                intent.putExtra("testCtPhone", testCtPhone);
//                if (messageTaskCacheBean==null) { //没有保存过信息
//                    messageTaskCacheBean = new MessageTaskCacheBean();
//                }
//                messageTaskCacheBean.setTestCuPhone(testCuPhone);
//                messageTaskCacheBean.setTestCtPhone(testCtPhone);
//                messageTaskCacheBean.setTestCmPhone(testCmPhone);
//                SharedPreferencesTool.getInstance().setObject(messageTaskCacheBean, SharedPreferencesTool.MessageTaskCacheBean);
                setResult(2, intent);
                finish();
            }
        });

    }

    private void initTitle() {
        myTitle.setTitleName("测试号设置");
        myTitle.setImageBack(this);

    }

    private void updateView() {

    }
}
