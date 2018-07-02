package com.orientdata.lookforcustomers.view.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.CertificationOut;
import com.orientdata.lookforcustomers.bean.SearchListBean;
import com.orientdata.lookforcustomers.presenter.HomePresent;
import com.orientdata.lookforcustomers.view.home.IHomeView;
import com.orientdata.lookforcustomers.view.home.fragment.TaskListFragment;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.qiniu.android.common.Constants;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.orientdata.lookforcustomers.R.id.vp;


public class TaskListActivity extends BaseActivity<IHomeView, HomePresent<IHomeView>> implements IHomeView {

    @BindView(R.id.titleSearch)
    MyTitle titleSearch;

    @BindView(R.id.tl_5)
    SlidingTabLayout tl5;
    @BindView(vp)
    ViewPager viewPager;




    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "全部", "审核中", "审核失败"
            , "待投放", "投放中", "投放结束"
    };



    private MyPagerAdapter mAdapter;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_task_list, null);
        setContentView(view);
        ButterKnife.bind(this);
        initView();
        initData();
        ButterKnife.bind(this);
        for (String title : mTitles) {
            TaskListFragment fragment = TaskListFragment.getInstance();
            fragment.setTitle(title);
            mFragments.add(fragment);
        }
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(0);
        tl5.setViewPager(viewPager);
        Intent intent = getIntent();
        String tasktype = intent.getStringExtra(Constants.taskType);
        if (tasktype!=null) {
            int positon = Arrays.binarySearch(mTitles, tasktype);
            viewPager.setCurrentItem(positon);
        }else{
            viewPager.setCurrentItem(0);
        }


    }


    private void initView() {
        titleSearch.setTitleName("我的寻客");
        titleSearch.setImageBack(this);
    }


    private void initData() {

    }


    /*下面没有用*/
    @Override
    protected HomePresent<IHomeView> createPresent() {
        return new HomePresent<>(this);
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void getCertificateMsg(CertificationOut certificationOut, boolean isCertificate) {

    }

    @Override
    public void getSearchList(SearchListBean searchListBean) {

    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
