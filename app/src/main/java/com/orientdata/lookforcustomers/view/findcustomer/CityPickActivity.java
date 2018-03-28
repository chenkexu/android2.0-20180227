package com.orientdata.lookforcustomers.view.findcustomer;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.AreaOut;
import com.orientdata.lookforcustomers.presenter.CityPickPresent;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.util.map.LocationService;
import com.orientdata.lookforcustomers.view.findcustomer.fragment.ByLetterFragment;
import com.orientdata.lookforcustomers.view.findcustomer.fragment.ByProvinceFragment;
import com.orientdata.lookforcustomers.widget.MyTitle;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建寻客-城市选择
 */
public class CityPickActivity extends BaseActivity<ICityPickView, CityPickPresent<ICityPickView>> implements ICityPickView, View.OnClickListener {
    private MyTitle title;
    private LocationService locationService;
    private String mLocCityName;    //获取城市
    private String mLocCityCode;
    private String mCity;    //获取城市
    private String mProvice;
    private TextView tv_ac_city_pick_autoloc;
    private boolean mIsLocationSuccess = false;//是否完成定位
    private TextView tv_loccity_info;

    private CityPickPresent mCityPickPresent;
    //private List<AreaOut> mAreaOuts;

    private LinearLayout linearCertific;//
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private String[] mTitleList;
    private List<Fragment> mFragmentList;//页卡视图集合
    private int mLocCityStatus = -777;
    private String mLocProvinceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_pick);
        initView();
        //getProvinceCity();
        initTitle();
        initTab();
        initViewPager();
        Intent intent = getIntent();
        if (intent != null) {
            mLocCityName = intent.getStringExtra("locCityName");
            mLocCityCode = intent.getStringExtra("locCityCode");
            mLocCityStatus = intent.getIntExtra("locCityStatus", -777);
            mLocProvinceName = intent.getStringExtra("locProvinceName");
            tv_ac_city_pick_autoloc.setText(mLocCityName);
            tv_ac_city_pick_autoloc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mLocCityStatus == 1) {
                        Intent intent = new Intent();
                        intent.putExtra("cityCode", mLocCityCode);
                        intent.putExtra("cityName", mLocCityName);
                        intent.putExtra("provinceName", mLocProvinceName);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        switch (mLocCityStatus) {
                            case 0:
                                ToastUtils.showShort(mLocCityName + "没有开通业务，请选择其他城市！");
                                break;
                            case -777:
                                ToastUtils.showShort("定位失败！");
                                break;
                            case -666:
                                ToastUtils.showShort("定位失败！");
                                break;
                        }
                    }
                }
            });
            if (mLocCityStatus == 1 || mLocCityStatus == -777 || mLocCityStatus == -666) {
                tv_loccity_info.setText("");
            }
            if(mLocCityStatus ==0){
                tv_loccity_info.setText("(当前城市暂未开通业务，请从下面列表中选择)");
            }
        }

    }

    /**
     * 初始化title
     */
    private void initTitle() {
        title.setTitleName("城市选择");
        title.setImageBack(this);
        /*title.setRightText(getString(R.string.cancel));
        title.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity(EnterpriseCertificationUploadActivity.class, CertificationActivity.class);
            }
        });*/
        title.setLeftImage(R.mipmap.back_white, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected CityPickPresent<ICityPickView> createPresent() {
        return new CityPickPresent<>(this);
    }


    private void initView() {
        title = (MyTitle) findViewById(R.id.title_city_pick_id);
        mCityPickPresent = new CityPickPresent(this);
        linearCertific = (LinearLayout) findViewById(R.id.linearCertific);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.vp_view);
        tv_ac_city_pick_autoloc = (TextView) findViewById(R.id.tv_ac_city_pick_autoloc);
        tv_ac_city_pick_autoloc.setOnClickListener(this);
        tv_ac_city_pick_autoloc.setClickable(false);
        tv_loccity_info = (TextView) findViewById(R.id.tv_loccity_info);
    }

    /**
     * 初始化Tab标签
     */
    private void initTab() {
        mTitleList = new String[]{"按照省市选择", "按照拼音字母选择"};//页卡标题集合
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList[0]));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList[1]));
    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new ByProvinceFragment());
        mFragmentList.add(new ByLetterFragment());
        ViewPagerAdapter mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ac_city_pick_autoloc:
                //定位按钮
//                if (mIsLocationSuccess) {
//                    Intent intent = new Intent();
//                    intent.putExtra("cityCode", findCityCode(mCity));
//                    intent.putExtra("cityName", mCity);
//                    setResult(RESULT_OK, intent);
//                    finish();
//                }
                break;
            default:
                break;
        }

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    /**
     * 获取省市列表
     */
    private void getProvinceCity() {
        mCityPickPresent.getProvinceCityData();
    }

    @Override
    public void getProvinceCity(List<AreaOut> areaOuts) {
        // mAreaOuts = areaOuts;
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
      /*  locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务*/
        super.onStop();
    }
}
