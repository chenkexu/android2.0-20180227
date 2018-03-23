package com.orientdata.lookforcustomers.view.home.imple;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.CertificationOut;
import com.orientdata.lookforcustomers.presenter.HomePresent;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.certification.impl.CertificationActivity;
import com.orientdata.lookforcustomers.view.home.IHomeView;
import com.orientdata.lookforcustomers.view.home.fragment.HomeFragment;
import com.orientdata.lookforcustomers.view.home.fragment.MessageFragment;
import com.orientdata.lookforcustomers.view.home.fragment.MineFragment;
import com.orientdata.lookforcustomers.view.home.fragment.ReportFragment;
import com.orientdata.lookforcustomers.view.home.fragment.SearchFragment;
import com.orientdata.lookforcustomers.widget.dialog.RemindDialog;

//主界面
public class HomeActivity extends BaseActivity<IHomeView, HomePresent<IHomeView>>
        implements IHomeView, View.OnClickListener {

    private HomeFragment mHomeFragment;
    private SearchFragment mSearchFragment;
    private ReportFragment mReportFragment;
    private MessageFragment mMessageFragment;
    private MineFragment mMineFragment;

    private RelativeLayout rlHome;
    private RelativeLayout rlSearch;
    private RelativeLayout rlReport;
    private RelativeLayout rlMessage;
    private RelativeLayout rlMine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //initFragments();
        if (mFragmentBefor != null) {
            if (mHomeFragment != null && mFragmentBefor.getClass() == mHomeFragment.getClass()) {
                mHomeFragment.getUserData();
            } else if (mSearchFragment != null && mFragmentBefor.getClass() == mSearchFragment.getClass()) {
                mSearchFragment.updateData();
            } else if (mMessageFragment != null && mFragmentBefor.getClass() == mMessageFragment.getClass()) {
                mMessageFragment.updateData();
            } else if (mReportFragment != null && mFragmentBefor.getClass() == mReportFragment.getClass()) {
                mReportFragment.updateData(true);
            } else if (mMineFragment != null && mFragmentBefor.getClass() == mMineFragment.getClass()) {
                mMineFragment.getData();
            }
        }

    }

    private void initView() {
        rlHome = (RelativeLayout) findViewById(R.id.rl_main_home);
        rlSearch = (RelativeLayout) findViewById(R.id.rl_main_search);
        rlReport = (RelativeLayout) findViewById(R.id.rl_main_report);
        rlMessage = (RelativeLayout) findViewById(R.id.rl_main_message);
        rlMine = (RelativeLayout) findViewById(R.id.rl_main_me);
        rlHome.setOnClickListener(this);
        rlHome.setSelected(true);
        rlSearch.setOnClickListener(this);
        rlReport.setOnClickListener(this);
        rlMessage.setOnClickListener(this);
        rlMine.setOnClickListener(this);
        initFragments();
//        if (getIntent() != null) {
//            boolean isNewUser = getIntent().getBooleanExtra("isNewUser", false);
//            if (isNewUser) {
//                //查看用户的认证信息
//            }
//        }
        mPresent.getCertificateMsg(true);
    }

    private Fragment mFragmentBefor;
    private FragmentManager mManager;

    private void initFragments() {
        mManager = getSupportFragmentManager();
        FragmentTransaction transaction = mManager.beginTransaction();
        mHomeFragment = new HomeFragment();
        mFragmentBefor = mHomeFragment;
        transaction.add(R.id.container, mHomeFragment).commit();
    }

    private void switchFragment(Fragment to) {
        try {
            if (mFragmentBefor != to) {
                FragmentTransaction transaction = mManager.beginTransaction();
                if (!to.isAdded()) {
                    transaction.hide(mFragmentBefor).add(R.id.container, to).commit();
                } else {
                    transaction.hide(mFragmentBefor).show(to).commit();
                }
                mFragmentBefor = to;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public HomePresent<IHomeView> createPresent() {
        return new HomePresent<>(this);
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private long clickTime = 0; //记录第一次点击的时间

    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            ToastUtils.showShort("再按一次后退键退出程序");
            clickTime = System.currentTimeMillis();
        } else {
            this.finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_main_home:
                rlSearch.setSelected(false);
                rlReport.setSelected(false);
                rlMessage.setSelected(false);
                rlMine.setSelected(false);
                rlHome.setSelected(true);
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                } else if(!mHomeFragment.isVisible()){
                    mHomeFragment.getUserData();
                }
                switchFragment(mHomeFragment);
                break;
            case R.id.rl_main_search:
                rlHome.setSelected(false);
                rlReport.setSelected(false);
                rlMessage.setSelected(false);
                rlMine.setSelected(false);
                rlSearch.setSelected(true);
                if (mSearchFragment == null) {
                    mSearchFragment = new SearchFragment();
                } else if(!mSearchFragment.isVisible()){
                    mSearchFragment.updateData();
                }
                switchFragment(mSearchFragment);
                break;
            case R.id.rl_main_report:
                rlHome.setSelected(false);
                rlSearch.setSelected(false);
                rlMessage.setSelected(false);
                rlMine.setSelected(false);
                rlReport.setSelected(true);
                if (mReportFragment == null) {
                    mReportFragment = new ReportFragment();
                } else if(!mReportFragment.isVisible()) {
                    mReportFragment.updateData(true);
                }
                switchFragment(mReportFragment);
                break;
            case R.id.rl_main_message:
                rlHome.setSelected(false);
                rlSearch.setSelected(false);
                rlMine.setSelected(false);
                rlReport.setSelected(false);
                rlMessage.setSelected(true);
                if (mMessageFragment == null) {
                    mMessageFragment = new MessageFragment();
                } else if(!mMessageFragment.isVisible()){
                    mMessageFragment.updateData();
                }
                switchFragment(mMessageFragment);
                break;
            case R.id.rl_main_me:
                rlHome.setSelected(false);
                rlSearch.setSelected(false);
                rlReport.setSelected(false);
                rlMessage.setSelected(false);
                rlMine.setSelected(true);
                if (mMineFragment == null) {
                    mMineFragment = new MineFragment();
                } else if(!mMineFragment.isVisible()){
                    mMineFragment.getData();
                }
                switchFragment(mMineFragment);
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
    public void getCertificateMsg(CertificationOut certificationOut, boolean isCertificate) {
        String cerStatus = "";
        String remindString = "";
        int imgResId = 0;
        String btText = "";
        int authStatus = -1;
        if (certificationOut == null) {
            //未认证
            cerStatus = getString(R.string.no_cer);
            remindString = getString(R.string.no_certified);
            imgResId = R.mipmap.no_certified;
            btText = getString(R.string.go_cer);
        } else {
            //认证状态 1审核中 2审核通过 3审核拒绝
            authStatus = certificationOut.getAuthStatus();
            if (authStatus == 1 || authStatus == 4) {
                //审核中
                cerStatus = getString(R.string.cer_ing);
                remindString = getString(R.string.cer_waiting);
                imgResId = R.mipmap.audit;
                btText = getString(R.string.go_watch);
            } else if (authStatus == 3) {
                //审核拒绝
                cerStatus = getString(R.string.no_pass);
                remindString = getString(R.string.not_pass);
                imgResId = R.mipmap.not_pass;
                btText = getString(R.string.re_go_cer);
            }
        }
        if (authStatus != 2) {
            final RemindDialog dialog = new RemindDialog(this, cerStatus, remindString, imgResId, btText);
            dialog.setClickListenerInterface(new RemindDialog.ClickListenerInterface() {
                @Override
                public void doCertificate() {
                    dialog.dismiss();
                    startActivity(new Intent(getBaseContext(), CertificationActivity.class));
                }
            });
            dialog.setCancelable(true);
            dialog.show();
        }
    }

    public HomePresent<IHomeView> getPresent() {
        return mPresent;
    }
}
