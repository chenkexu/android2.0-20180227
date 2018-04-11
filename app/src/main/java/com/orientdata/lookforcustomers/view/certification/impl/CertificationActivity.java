package com.orientdata.lookforcustomers.view.certification.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.AreaOut;
import com.orientdata.lookforcustomers.bean.CertificationOut;
import com.orientdata.lookforcustomers.presenter.CertificatePresent;
import com.orientdata.lookforcustomers.view.certification.ICertificateView;
import com.orientdata.lookforcustomers.view.certification.WathchCertificationActivity;
import com.orientdata.lookforcustomers.view.certification.fragment.EnterpriseCertificationFragment;
import com.orientdata.lookforcustomers.view.certification.fragment.PersonalCertificationFragment;
import com.orientdata.lookforcustomers.widget.MyTitle;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人/企业认证信息查看页面
 * Created by wy on 2017/11/16.
 */

public class CertificationActivity extends BaseActivity<ICertificateView, CertificatePresent<ICertificateView>> implements ICertificateView,View.OnClickListener {
    private MyTitle titleCertificate;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private LinearLayout linearCertificMsg;//认证信息
    private LinearLayout linearCertific;//认证
    private TextView tvCertificateType;
    private TextView tvNameRemind;
    private TextView tvName;
    private TextView tvNumRemind;
    private TextView tvContactRemind;
    private TextView tvCardRemind;
    private LinearLayout linearEnterprise;
    private TextView tvAddressRemind;
    private TextView tvNum;
    private TextView tvPhoneRemind;
    private TextView tvContact;
    private TextView tvPhone;
    private TextView tvAddress;
    private TextView tvEnterpriseAddress;
    private TextView tvCertificateStatus;
    private TextView tvCard;
    private LinearLayout linearContact;
    private List<Fragment> mFragmentList;//页卡视图集合
    private String[] mTitleList;
    private TextView tvWatchImg;
    private CertificationOut certificationOutFromNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        initView();
        initTitle();
        mPresent.getCertificateMsg();
    }

    /**
     * 初始化view
     */
    private void initView() {
        titleCertificate = (MyTitle) findViewById(R.id.titleCertificate);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.vp_view);
        linearCertificMsg = (LinearLayout) findViewById(R.id.linearCertifiMsg);
        linearCertific = (LinearLayout) findViewById(R.id.linearCertific);
        titleCertificate = (MyTitle) findViewById(R.id.titleCertificate);
        tvCertificateType = (TextView) findViewById(R.id.tvCertificateType);
        tvNameRemind = (TextView) findViewById(R.id.tvNameRemind);
        tvName = (TextView) findViewById(R.id.tvName);
        tvNumRemind = (TextView) findViewById(R.id.tvNumRemind);
        tvNum = (TextView) findViewById(R.id.tvNum);
        tvContactRemind = (TextView) findViewById(R.id.tvContactRemind);
        tvCardRemind = (TextView) findViewById(R.id.tvCardRemind);
        linearContact =  findViewById(R.id.linearContact);
        tvCard = (TextView) findViewById(R.id.tvCard);
        linearEnterprise = (LinearLayout) findViewById(R.id.linearEnterprise);
        tvAddressRemind = (TextView) findViewById(R.id.tvAddressRemind);
        tvCertificateStatus = (TextView) findViewById(R.id.tvCertificateStatus);
        tvEnterpriseAddress = (TextView) findViewById(R.id.tvEnterpriseAddress);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvPhoneRemind = (TextView) findViewById(R.id.tvPhoneRemind);
        tvContact = (TextView) findViewById(R.id.tvContact);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvWatchImg = (TextView) findViewById(R.id.tvWatchImg);
        tvWatchImg.setOnClickListener(this);
        linearCertific.setVisibility(View.GONE);
        linearCertificMsg.setVisibility(View.GONE);
    }

    /**
     * 初始化title
     */
    private void initTitle() {
        titleCertificate.setTitleName(getString(R.string.certificate1));
        titleCertificate.setImageBack(this);
    }

    /**
     * 更新认证信息
     */
    private void updateCertificMsg(CertificationOut certificationOut) {
        int type = certificationOut.getType();
        int status = certificationOut.getAuthStatus();
//        认证状态 1审核中 2审核通过 3审核拒绝
        if(status == 1){
            tvCertificateStatus.setText(R.string.cer_ing);
        }else if(status == 2){
            tvCertificateStatus.setText(R.string.cer_pass1);
        }

        if (type == 1) {
            //企业
            linearContact.setVisibility(View.VISIBLE);
            tvCertificateType.setText(R.string.business_authentication);
            tvNameRemind.setText(R.string.enterprise_name);
            tvNumRemind.setText(R.string.business_license_num);
            tvContactRemind.setText(R.string.contact_name);
            tvCardRemind.setText(R.string.contact_id_num);
            tvAddressRemind.setText(R.string.enterprise_address);
            tvPhoneRemind.setText(R.string.contact_phone);
            tvEnterpriseAddress.setText(certificationOut.getProvinceName() + certificationOut.getCityName());
            linearEnterprise.setVisibility(View.VISIBLE);
            tvAddress.setText(certificationOut.getAddress());
            tvNum.setText(certificationOut.getBusinessLicenseNo());
            tvCard.setText(certificationOut.getContactCard());
        } else {
            //个人
            linearContact.setVisibility(View.GONE);
            tvCertificateType.setText(R.string.personal_authentication);
            tvNameRemind.setText(R.string.per_name);
            tvNumRemind.setText(R.string.contact_id_num);
            tvPhoneRemind.setText(R.string.per_phone);
            tvCardRemind.setText(R.string.per_address);
            linearEnterprise.setVisibility(View.GONE);
            tvAddressRemind.setText(R.string.enterprise_locate);
            tvAddress.setText(certificationOut.getProvinceName() + certificationOut.getCityName());
            tvCard.setText(certificationOut.getAddress());
            tvNum.setText(certificationOut.getContactCard());
        }
        tvName.setText(certificationOut.getName());
        tvContact.setText(certificationOut.getContactPerson());
        tvPhone.setText(certificationOut.getContactPhone());
        certificationOutFromNet = certificationOut;
    }

    /**
     * 初始化Tab标签
     */
    private void initTab() {
        mTitleList = new String[]{getString(R.string.business_authentication), getString(R.string.personal_authentication)};//页卡标题集合
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList[0]));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList[1]));
    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new EnterpriseCertificationFragment());
        mFragmentList.add(new PersonalCertificationFragment());
        ViewPagerAdapter mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
    }

    @Override
    public void getProvinceCity(List<AreaOut> areaOuts) {

    }

    /**
     * 得到认证信息 更新界面
     * @param certificationOut
     */
    @Override
    public void getCertificateMsg(CertificationOut certificationOut) {
        // TODO: 2018/4/11 测试，一会删除
        certificationOut.setAuthStatus(3);
        if (certificationOut == null || certificationOut.getAuthStatus() == 3) {

            //未认证 拒绝
            linearCertific.setVisibility(View.VISIBLE);
            linearCertificMsg.setVisibility(View.GONE);
            tvCertificateStatus.setText(R.string.no_cer);
            //只有此状态显示 跳过(不显示)
//            titleCertificate.setRightText(getString(R.string.skip));
//            titleCertificate.setRightTextClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//                }
//            });
            initTab();
            initViewPager();
        } else {
            linearCertific.setVisibility(View.GONE);
            linearCertificMsg.setVisibility(View.VISIBLE);
            if (certificationOut != null)
                updateCertificMsg(certificationOut);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvWatchImg:
                Intent intent = new Intent(this, WathchCertificationActivity.class);
                intent.putExtra("CertificationOut", certificationOutFromNet);
                startActivity(intent);
                break;
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        //重写这个方法，将设置每个Tab的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList[position];
        }
    }
    @Override
    protected CertificatePresent<ICertificateView> createPresent() {
        return new CertificatePresent<>(this);
    }

    public CertificatePresent<ICertificateView> getPresent() {
        return mPresent;
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
