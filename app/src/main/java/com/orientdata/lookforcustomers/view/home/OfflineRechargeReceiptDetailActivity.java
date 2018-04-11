package com.orientdata.lookforcustomers.view.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.WangrunBaseActivity;
import com.orientdata.lookforcustomers.bean.OfflineRechargeBean;
import com.orientdata.lookforcustomers.util.GlideUtil;
import com.orientdata.lookforcustomers.widget.MyTitle;

/**
 * 上传充值凭证详情
 */
public class OfflineRechargeReceiptDetailActivity extends WangrunBaseActivity {
    private MyTitle title;

    private TextView tv_bank_name;
    private TextView tv_serial_number;
    private TextView tv_account_name;
    private TextView tv_account_number;//
    private TextView tv_money;//金额
    private OfflineRechargeBean mBean;
    private ImageView iv_upload;
    private TextView tv_reason_topic;
    private TextView tv_reason;
    private TextView tv_status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_recharge_receipt_detail);

        initView();
        initTitle();
        Intent intent = getIntent();
        if (intent != null) {
            mBean = (OfflineRechargeBean) intent.getSerializableExtra("data");
            /*openAccountBank = intent.getStringExtra("openAccountBank");
            serialNumber = intent.getStringExtra("serialNumber");
            accountName = intent.getStringExtra("accountName");
            accountNumber = intent.getStringExtra("accountNumber");
            money = intent.getStringExtra("money");*/
            if (mBean != null) {
                if (!TextUtils.isEmpty(mBean.getOpenAccountBank().trim())) {
                    tv_bank_name.setText(mBean.getOpenAccountBank().trim());
                }
                if (!TextUtils.isEmpty(mBean.getSerialNumber().trim())) {
                    tv_serial_number.setText(mBean.getSerialNumber().trim());
                }
                if (!TextUtils.isEmpty(mBean.getAccountName().trim())) {
                    tv_account_name.setText(mBean.getAccountName().trim());
                }
                if (!TextUtils.isEmpty(mBean.getAccountNumber().trim())) {
                    tv_account_number.setText(mBean.getAccountNumber().trim());
                }
                if (!TextUtils.isEmpty(mBean.getMoney() + "")) {
                    tv_money.setText(mBean.getMoney() + "");
                }
                if (!TextUtils.isEmpty(mBean.getVoucherImgid().trim())) {
//                    Glide.with(this).load(mBean.getVoucherImgid().trim()).into(iv_upload);
                    GlideUtil.getInstance().loadImage(this,iv_upload,mBean.getVoucherImgid().trim(),R.mipmap.icon_id_error,true);
                }
                if (!TextUtils.isEmpty(mBean.getMoney() + "")) {
                    tv_money.setText(mBean.getMoney() + "");
                }
                switch (mBean.getStatus()) {
                    case 1:
                        tv_status.setText("待处理");
                        tv_status.setTextColor(Color.parseColor("#999999"));
                        tv_reason_topic.setVisibility(View.GONE);
                        tv_reason.setVisibility(View.GONE);
                        break;
                    case 2:
                        tv_status.setText("未到账");
                        tv_status.setTextColor(Color.parseColor("#db010b"));
                        tv_reason_topic.setVisibility(View.GONE);
                        tv_reason.setVisibility(View.GONE);
                        break;
                    case 3:
                        tv_status.setText("已完成");
                        tv_status.setTextColor(Color.parseColor("#09b6f2"));
                        tv_reason_topic.setVisibility(View.GONE);
                        tv_reason.setVisibility(View.GONE);
                        break;
                    case 4:
                        tv_status.setText("已驳回");
                        tv_status.setTextColor(Color.parseColor("#db010b"));
                        tv_reason_topic.setVisibility(View.VISIBLE);
                        tv_reason.setVisibility(View.VISIBLE);
                        if (!TextUtils.isEmpty(mBean.getReason())) {
                            tv_reason.setText(mBean.getReason());
                        }
                        break;
                    default:
                        tv_status.setText("");
                        break;
                }

            }

        }
    }

    private void initView() {
        title = findViewById(R.id.mt_title);
        tv_bank_name = findViewById(R.id.tv_bank_name);
        tv_serial_number = findViewById(R.id.tv_serial_number);
        tv_account_name = findViewById(R.id.tv_account_name);
        tv_account_number = findViewById(R.id.tv_account_number);
        tv_money = findViewById(R.id.tv_money);
        iv_upload = findViewById(R.id.iv_upload);
        tv_reason_topic = findViewById(R.id.tv_reason_topic);
        tv_reason = findViewById(R.id.tv_reason);
        tv_status = findViewById(R.id.tv_status);
    }

    private void initTitle() {
        title.setTitleName("上传凭证");
        title.setImageBack(this);
       /* title.setRightText("上传充值凭证");
        title.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getBaseContext(), UploadOfflineRechargeReceiptActivity.class), 1);
            }
        });*/
    }
}
