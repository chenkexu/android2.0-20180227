package com.orientdata.lookforcustomers.view.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.WangrunBaseActivity;
import com.orientdata.lookforcustomers.bean.CanInvoiceBean;
import com.orientdata.lookforcustomers.bean.InvoiceBean;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.share.ShareEntity;
import com.orientdata.lookforcustomers.share.ShareManager;
import com.orientdata.lookforcustomers.util.MyOpenActivityUtils;
import com.orientdata.lookforcustomers.util.QRCodeUtil;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.home.InvoiceHistoryActivity;
import com.orientdata.lookforcustomers.widget.MyTitle;

import java.io.File;
import java.util.List;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * 分享赚取佣金
 */

public class ShareToGetCommissionActivity extends WangrunBaseActivity implements View.OnClickListener {
    private MyTitle title;
    private TextView tvActivityDescription;
    private ImageView ivQRCode;
    String url = "";
    private TextView tvWechat, tvWechatFriend, tvQQ, tvWeibo, tvQQSpace;
    private ShareEntity shareEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_to_get_commission);
        initView();
        initTitle();
        getQRCode();
    }

    private void getQRCode() {
        final String filePath = getFileRoot(this) + File.separator + "qr_" + System.currentTimeMillis() + ".jpg";
        boolean success = QRCodeUtil.createQRImage(url, 348, 348, null, filePath);
        if (success) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ivQRCode.setImageBitmap(BitmapFactory.decodeFile(filePath));
                }
            });
        }
    }

    //文件存储根目录
    private String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }
        return context.getFilesDir().getAbsolutePath();
    }

    private void initView() {
        url = getIntent().getStringExtra("url");
        title = findViewById(R.id.my_title);
        tvActivityDescription = findViewById(R.id.tvActivityDescription);
        ivQRCode = findViewById(R.id.ivQRCode);
        tvWechat = findViewById(R.id.tvWechat);
        tvWechatFriend = findViewById(R.id.tvWechatFriend);
        tvQQ = findViewById(R.id.tvQQ);
        tvQQSpace = findViewById(R.id.tvQQSpace);
        tvWeibo = findViewById(R.id.tvWeibo);
        tvWechat.setOnClickListener(this);
        tvWechatFriend.setOnClickListener(this);
        tvQQ.setOnClickListener(this);
        tvQQSpace.setOnClickListener(this);
        tvWeibo.setOnClickListener(this);
        tvActivityDescription.setOnClickListener(this);

    }

    private void initTitle() {
        title.setTitleName("分享赚取佣金");
        title.setImageBack(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvWechat:
                initShareEntity();
                ShareManager.getInstance().shareWX(this, shareEntity);
                break;
            case R.id.tvWechatFriend:
                initShareEntity();
                ShareManager.getInstance().shareWXQ(this, shareEntity);
                break;
            case R.id.tvQQ:
                initShareEntity();
                ShareManager.getInstance().shareQQ(this, shareEntity);
                break;
            case R.id.tvWeibo:
                initShareEntity();
                ShareManager.getInstance().shareWEIBO(this, shareEntity);
                break;
            case R.id.tvQQSpace:
                initShareEntity();
                ShareManager.getInstance().shareQQZONE(this, shareEntity);
                break;
            case R.id.tvActivityDescription:
                MyOpenActivityUtils.openCommonWebView(this, "活动说明", "http://www.orientdata.cn/activity.html");
                break;
        }

    }

    private void initShareEntity() {
        if (shareEntity == null) {
            shareEntity = new ShareEntity();
        }
        if (TextUtils.isEmpty(url)) {
            ToastUtils.showShort("后台分享链接为空！");
            //shareEntity.setShareUrl("http://www.baidu.com");
        } else {
            shareEntity.setShareUrl(url);
        }
        shareEntity.setTitle("用户是谁？用户在哪？如何触达用户？");
        String versionName = AppUtils.getAppVersionName();
        shareEntity.setContent("寻客V"+versionName+"，基于实时LBS的场景触达，解决商户的三大核心痛点！");
    }
}
