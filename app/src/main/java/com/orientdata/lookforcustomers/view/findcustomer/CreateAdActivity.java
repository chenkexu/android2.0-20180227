package com.orientdata.lookforcustomers.view.findcustomer;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidkun.xtablayout.XTabLayout;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.WangrunBaseActivity;
import com.orientdata.lookforcustomers.view.findcustomer.fragment.CreateAdFragment;
import com.orientdata.lookforcustomers.view.findcustomer.fragment.ImageWarehouseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建落地页
 */
public class CreateAdActivity extends WangrunBaseActivity implements View.OnClickListener {
    private LinearLayout linearCertific;//
    private XTabLayout mTabLayout;
    private ViewPagerCompat mViewPager;
    private String[] mTitleList;
    private List<Fragment> mFragmentList;//页卡视图集合
    private String baiduMapPath;//百度地图截图路径
    private String longitude;//经度
    private String dimension;//维度
    private String address;
    private String adImagePath;

    private Dialog progressDialog;

    private ImageView imgBack;
    private ViewPagerAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ad);
        Intent intent = getIntent();
        if (intent != null) {
            baiduMapPath = intent.getStringExtra("mapPath");
            longitude = intent.getStringExtra("longitude");
            dimension = intent.getStringExtra("dimension");
            address = intent.getStringExtra("address");
            //adImagePath = intent.getStringExtra("adImagePath");
        }
        initView();
        //getProvinceCity();
        initTab();
        initViewPager();

    }


    private void initView() {
        linearCertific = (LinearLayout) findViewById(R.id.linearCertific);
        mTabLayout =  findViewById(R.id.tabs);
        mViewPager = (ViewPagerCompat) findViewById(R.id.vp_view);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
    }

    /**
     * 初始化Tab标签
     */
    private void initTab() {
        mTitleList = new String[]{"落地页制作", "页面库"};//页卡标题集合
        //mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList[0]));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList[1]));
    }

    public void getImageWareHouseData() {
        ((ImageWarehouseFragment) mAdapter.getItem(1)).getData();
    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        mFragmentList = new ArrayList<>();
        CreateAdFragment adFragment = new CreateAdFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mapPath", baiduMapPath);
        bundle.putString("longitude", longitude);
        bundle.putString("dimension", dimension);
        bundle.putString("address", address);
        //bundle.putString("adImagePath", adImagePath);
        adFragment.setArguments(bundle);
        mFragmentList.add(adFragment);
        mFragmentList.add(new ImageWarehouseFragment());
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mViewPager.setViewTouchMode(true);//设置事件冲突
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                break;
            default:
                break;
        }

    }

    public void setCurrentItem(int i) {
        mViewPager.setCurrentItem(i);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * 显示默认的进度条
     */
    public void showDefaultLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        } else {
            progressDialog = null;
        }

        progressDialog = new Dialog(this, R.style.loadingDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.content__roll_loading, null);
        progressDialog.setContentView(view);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    /**
     * 隐藏默认的进度条
     */
    public void hideDefaultLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}
