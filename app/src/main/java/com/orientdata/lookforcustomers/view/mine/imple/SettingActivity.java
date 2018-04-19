package com.orientdata.lookforcustomers.view.mine.imple;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.event.LogoutResultEvent;
import com.orientdata.lookforcustomers.network.util.AppConfig;
import com.orientdata.lookforcustomers.presenter.LoginAndRegisterPresent;
import com.orientdata.lookforcustomers.view.home.imple.HomeActivity;
import com.orientdata.lookforcustomers.view.login.ILoginAndRegisterView;
import com.orientdata.lookforcustomers.view.login.imple.LoginAndRegisterActivity;
import com.orientdata.lookforcustomers.view.mine.AboutUsActivity;
import com.orientdata.lookforcustomers.view.mine.CleanMessageUtil;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.orientdata.lookforcustomers.widget.dialog.ConfirmDialog;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;

/**
 * 设置
 */

public class SettingActivity extends BaseActivity<ILoginAndRegisterView, LoginAndRegisterPresent<ILoginAndRegisterView>> implements ILoginAndRegisterView,View.OnClickListener{
    private MyTitle title;

    private RelativeLayout relaResetPwd;
    private RelativeLayout relaAboutUs;
    private RelativeLayout relaScavengingCaching;
    private RelativeLayout relaPushNotice;
    private RelativeLayout relaFeedback;
    private TextView tvLogOut;
    private Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        setContentView(R.layout.activity_set);
        initView();
        initTitle();
        updateView();
    }

    @Override
    protected LoginAndRegisterPresent<ILoginAndRegisterView> createPresent() {
        return new LoginAndRegisterPresent<>(this);
    }


    private void initView() {
        title = findViewById(R.id.my_title);
        relaResetPwd = findViewById(R.id.relaResetPwd);
        relaAboutUs = findViewById(R.id.relaAboutUs);
        tvLogOut = findViewById(R.id.tvLogOut);
        relaScavengingCaching = findViewById(R.id.relaScavengingCaching);
        relaPushNotice = findViewById(R.id.relaPushNotice);
        relaFeedback = findViewById(R.id.relaFeedback);
        relaResetPwd.setOnClickListener(this);
        relaAboutUs.setOnClickListener(this);
        tvLogOut.setOnClickListener(this);
        relaPushNotice.setOnClickListener(this);
        relaScavengingCaching.setOnClickListener(this);
        relaFeedback.setOnClickListener(this);
    }
    private void updateView(){

    }


    private void initTitle() {
        title.setImageBack(this);
        title.setTitleName("设置");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relaResetPwd:
                //重置密码
                toResetLogin();
                break;
            case R.id.relaAboutUs:
                //关于我们
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.tvLogOut:
                //退出
                showLogoutDialog();
                break;
            case R.id.relaScavengingCaching:
                //清除缓存
                showConfirmDialog();

                break;
            case R.id.relaPushNotice:
                //推送通知

                break;
            case R.id.relaFeedback:
                //意见反馈
                final String url = "mqqwpa://im/chat?chat_type=wpa&uin=2280249239";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                break;
        }

    }

    /**
     * 重置密码
     */
    private void toResetLogin(){
        Intent intent = new Intent(this, LoginAndRegisterActivity.class);
        intent.putExtra("type",3);
        startActivity(intent);
    }

    /**
     * 清除缓存dialog提示
     */
    private void showConfirmDialog(){
        final ConfirmDialog dialog;
        try {
            String size = CleanMessageUtil.getTotalCacheSize(this);
            String str = "";
            boolean isClear;
            if(size.contains("0")){
                str = "缓存数据为零，不需要清除";
                isClear = false;
            }else{
                str = "确定清除"+ size+"缓存数据？";
                isClear = true;
            }
            dialog = new ConfirmDialog(this,str);
            dialog.show();
            dialog.setConfirmVisibility(isClear?View.VISIBLE:View.GONE);
            dialog.setClickListenerInterface(new ConfirmDialog.ClickListenerInterface() {
                @Override
                public void doCancel() {
                    dialog.dismiss();
                }

                @Override
                public void doConfirm() {
                    dialog.dismiss();
                    CleanMessageUtil.clearAllCache(context);
                    //清除图片
                    CleanMessageUtil.deleteDir(new File(Environment.getExternalStorageDirectory() + "/ClipPhoto/cache/"));//资质
                    CleanMessageUtil.deleteDir(new File(Environment.getExternalStorageDirectory() + File.separator +"tempAd.jpg"));//添加图片
                    CleanMessageUtil.deleteDir(new File(Environment.getExternalStorageDirectory() + File.separator + "imageWithText.jpg"));//模板制作
                    CleanMessageUtil.deleteDir(new File(Environment.getExternalStorageDirectory() +AppConfig.LOCAL_PRODUCT_DOWNLOAD_PHOTOPATH));//图库下载的图片
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出确定框
     */
    private void showLogoutDialog(){
        final ConfirmDialog dialog = new ConfirmDialog(this,"确定退出登录吗？","确定");
        dialog.show();
        dialog.setClickListenerInterface(new ConfirmDialog.ClickListenerInterface() {
            @Override
            public void doCancel() {
                dialog.dismiss();
            }

            @Override
            public void doConfirm() {
                dialog.dismiss();
                mPresent.logOut();
            }
        });
    }
    @Subscribe
    public void logoutResult(LogoutResultEvent logoutResultEvent){
        if(logoutResultEvent!=null && logoutResultEvent.errBean.getCode() == 0){
            Intent intent = new Intent(this,LoginAndRegisterActivity.class);
            startActivity(intent);
            closeActivity(SettingActivity.class, HomeActivity.class);
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

}
