package com.orientdata.lookforcustomers.view.findcustomer.impl;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidkun.xtablayout.XTabLayout;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.PicListBean;
import com.orientdata.lookforcustomers.bean.SettingOut;
import com.orientdata.lookforcustomers.bean.TradeSelfout;
import com.orientdata.lookforcustomers.bean.UploadPicBean;
import com.orientdata.lookforcustomers.presenter.ImgPresent;
import com.orientdata.lookforcustomers.presenter.LoginAndRegisterPresent;
import com.orientdata.lookforcustomers.view.findcustomer.IImgView;
import com.orientdata.lookforcustomers.view.findcustomer.fragment.LocalUploadFragment;
import com.orientdata.lookforcustomers.view.findcustomer.fragment.PictureLibraryFragment;
import com.orientdata.lookforcustomers.view.findcustomer.fragment.TemplateMakingFragment;
import com.orientdata.lookforcustomers.view.login.ILoginAndRegisterView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wy on 2017/11/27.
 * 添加广告位
 */

public class AddAdvertiseImgActivity extends BaseActivity<IImgView, ImgPresent<IImgView>> implements IImgView, View.OnClickListener {
    private XTabLayout mTabLayout;
    private ViewPager mViewPager;
    private String[] mTitleList;
    private ImageView imgBack;
    private List<Fragment> mFragmentList;//页卡视图集合
    private TextView tv_cancel;//垃圾桶


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_advertise);
        initView();
        initTab();
        initViewPager();
    }

    private void initView() {
        mTabLayout = findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.vp_view);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        imgBack.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);

    }

    /**
     * 初始化Tab标签
     */
    private void initTab() {
        mTitleList = new String[]{getString(R.string.local_upload), getString(R.string.template_making), getString(R.string.picture_library)};//页卡标题集合
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList[0]));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList[1]));
    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new LocalUploadFragment());
        mFragmentList.add(new TemplateMakingFragment());
        mFragmentList.add(new PictureLibraryFragment());
        ViewPagerAdapter mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        //setCurrentItem(1);
        mViewPager.setOffscreenPageLimit(3);
    }

    public void setCurrentItem(int itemPosition) {
        mViewPager.setCurrentItem(itemPosition);
    }

    @Override
    public void uploadPicSuc(UploadPicBean uploadPicBean) {

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
    public void showLoading() {
        showDefaultLoading();
    }

    @Override
    public void hideLoading() {
        hideDefaultLoading();
    }

    @Override
    protected ImgPresent<IImgView> createPresent() {
        return new ImgPresent<>(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.tv_cancel:
                clickCancel.onClickCancel();
                break;
        }

    }

    public ImgPresent<IImgView> getPresent() {
        return mPresent;
    }

    ClickCancel clickCancel;

    public void setDeleteInterFace(ClickCancel clickCancel) {
        this.clickCancel = clickCancel;
    }

    public interface ClickCancel {
        void onClickCancel();
    }

    public void setCancelVisible(int visible) {
        tv_cancel.setVisibility(visible);
    }
}
