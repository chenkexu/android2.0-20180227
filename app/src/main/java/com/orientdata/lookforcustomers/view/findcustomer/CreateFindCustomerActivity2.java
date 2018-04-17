package com.orientdata.lookforcustomers.view.findcustomer;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.Area;
import com.orientdata.lookforcustomers.bean.AreaOut;
import com.orientdata.lookforcustomers.bean.NextStepCheckBean;
import com.orientdata.lookforcustomers.bean.OrientationSettingsOut;
import com.orientdata.lookforcustomers.bean.PreOut;
import com.orientdata.lookforcustomers.bean.TaskOut;
import com.orientdata.lookforcustomers.bean.TaskTypeBean;
import com.orientdata.lookforcustomers.manager.LbsManager;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;
import com.orientdata.lookforcustomers.presenter.CityPickPresent;
import com.orientdata.lookforcustomers.runtimepermissions.PermissionsManager;
import com.orientdata.lookforcustomers.util.CommonUtils;
import com.orientdata.lookforcustomers.util.SharedPreferencesTool;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.util.map.LocationService;
import com.orientdata.lookforcustomers.view.certification.fragment.ACache;
import com.orientdata.lookforcustomers.view.certification.impl.CertificationActivity;
import com.orientdata.lookforcustomers.view.findcustomer.impl.DirectionalSettingActivity;
import com.orientdata.lookforcustomers.view.findcustomer.impl.MessageTaskActivity;
import com.orientdata.lookforcustomers.view.findcustomer.impl.PageTaskActivity;
import com.orientdata.lookforcustomers.widget.dialog.ConfirmDialog;
import com.orientdata.lookforcustomers.widget.dialog.RemindDialog;
import com.orientdata.lookforcustomers.widget.dialog.SettingStringDialog;
import com.orientdata.lookforcustomers.widget.dialog.TaskTypeDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * 创建寻客页面 (测试页面)
 */
public class CreateFindCustomerActivity2 extends BaseActivity<ICityPickView, CityPickPresent<ICityPickView>> implements ICityPickView, SensorEventListener, View.OnClickListener, OnGetGeoCoderResultListener {

    private LocationService locationService;
    // 定位相关
    LocationClient mLocClient;
    public CreateFindCustomerActivity2.MyLocationListenner myListener = new CreateFindCustomerActivity2.MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private String mCityName = "";
    private String mProvinceName = "";
    private String mCityCode = "";
    private String mLocCityName = "";
    private String mLocProvinceName = "";
    private String mLocCityCode = "";
    private boolean mIsFirstLocation = true;//是否完成定位

    private CityPickPresent mCityPickPresent;


    private List<AreaOut> mAreaOuts; //后台返回的城市列表的信息


    private LinearLayout ll_at_create_find_customer_location;

    private TextView tvMore;
    BitmapDescriptor mCurrentMarker;
    private SensorManager mSensorManager;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;

    MapView mMapView;
    BaiduMap mBaiduMap;
    private RelativeLayout rl_map;

    // UI相关
    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;
    private float direction;
    private ImageView imageView_suoxiao;
    private ImageView imageView_fangda;
    private ImageView imageView_jingzhundingwei;




    private LinearLayout ll_at_create_find_customer_type;//任务类型布局
    private LinearLayout tv_ac_create_find_customer_scope;//半径父布局


    private LinearLayout ll_at_create_find_customer_search; //搜索栏
    private TextView tv_at_create_find_customer_location; //左上角的位置


    private TextView tv_at_create_find_customer_type;//任务类型TextView
    private EditText et_ac_create_find_customer_name;//任务名称
//    private EditText et_ac_create_find_customer_budget; //任务预算
    private TextView tv_ac_create_find_customer_radius;//半径
    private TextView tv_ac_create_find_customer_set; //定向设置
    private TextView tv_at_create_find_customer_putlocation;//投放位置



    private LinearLayout ll_ac_create_find_customer_set;

    private TextView tv_at_create_find_customer_nextstep;




    private TextView tv_name2;//任务名称计数
    private TextView tv_at_create_find_customer_cancel;
    private LatLng mCurrentLatLng;
    //21-3===5m-1000km  21-5m,20-10m,19-20m,18-50m,17-100,16-200m,15-500m,14-1km,13-2km,12.5-3km,12-5km,11-10km,10-20km,9-25km,8-50km,7-100km,6-200km,5-500km,4-1000km
    //float[] mScaleLevel = {11.0f, 12.0f, 12.66f, 13.0f, 14.0f, 15.0f};
    float[] mScaleLevel = {15.0f, 14.0f, 13.0f, 12.66f, 12.0f, 11.0f};
    //int[] mCircleRadius = {10000, 5000, 3000, 2000, 1000, 500};//米
    int[] mCircleRadius = {500, 1000, 2000, 3000, 5000, 10000};//米
    //String[] mCircleRadiusKM = {"10KM", "5KM", "3KM", "2KM", "1KM", "500M"};
    String[] mCircleRadiusKM = {"500M", "1KM", "2KM", "3KM", "5KM", "10KM"};
    String[] mCircleRadiusM = {"500", "1000", "2000", "3000", "5000", "10000"};
    List<String> mCircleRadiusKMLists = new ArrayList<>();//{"10KM", "5KM", "3KM", "2KM", "1KM", "500M"}范围半径数组
    private int mRadiusFromPosition = 0;
    private static final int accuracyCircleFillColor = 0x00FFFF88;
    private static final int accuracyCircleStrokeColor = 0x0000FF00;
    int mCurrentScaleLevelPositon = 0;

    //GEO
    GeoCoder mSearch = null;
    private String mSearchValue;
    private boolean mIsShowDialog = true;  //这个城市是否有业务的flag
    private String mTaskType;//任务类型。
    private String ageF;
    private String ageB;
    private String educationLevelF;
    private String educationLevelB;
    private String sex;
    private String consumptionCapacityF;
    private String consumptionCapacityB;
    private String ascription;
    private String phoneModelIds = "";
    private String interestIds = "";
    private int mFromAction;
    private UiSettings mUiSettings;

    //百度地图事件分发处理
    ScrollView sv_main;
    private String mMoveAddress;
    private String mMoveCityName;
    private String mMoveProvinceName;
    private String mMoveCityCode;
    private Bitmap bm;
    private boolean isReCreate = false;//是否是寻客详情点进来的重新创建,true代表的是不需要重新创建
    private TaskOut mTaskOut = null;//寻客详情的内容
    private String mMapClippath;
    private String mThrowAddress;//投放地址
    private String mStrType;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCityPickPresent = new CityPickPresent(this);
        setContentView(R.layout.activity_create_find_customer);
        initView();
        for (String s : mCircleRadiusKM) {
            mCircleRadiusKMLists.add(s);
        }
        if (!hasMapPermisson()) {
            requestMapPermission();
        }
        initBaiduMap();
    }

    /**
     * 初始化百度地图
     */
    private void initBaiduMap() {        //****************百度地图
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mMapView.showZoomControls(false);//去掉缩放控件
        mMapView.showScaleControl(false);
        mMapView.setLogoPosition(LogoPosition.logoPostionleftTop);
        mBaiduMap = mMapView.getMap();
        mUiSettings = mBaiduMap.getUiSettings();
        //禁止所有手势操作
        mUiSettings.setZoomGesturesEnabled(false);
        mUiSettings.setRotateGesturesEnabled(false);
        mUiSettings.setOverlookingGesturesEnabled(false);
        mCurrentMarker = BitmapDescriptorFactory
                .fromBitmap(Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565));
        MyLocationConfiguration mcf = new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker, accuracyCircleFillColor, accuracyCircleStrokeColor);
        mBaiduMap.setMyLocationConfiguration(mcf);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setCompassEnable(false);


        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);





        getProvinceCity();
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        //解决百度地图和scrollview的冲突
        mMapView.getChildAt(0).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //允许ScrollView截断点击事件，ScrollView可滑动
                    sv_main.requestDisallowInterceptTouchEvent(false);
                } else {
                    //不允许ScrollView截断点击事件，点击事件由子View处理
                    sv_main.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        if (isReCreate) {
            mSearch.geocode(new GeoCodeOption()
                    .city("中国")
                    .address(mThrowAddress));
        } else {
            mLocClient.start();
        }
//        LbsManager.getInstance().getLocation(new BDLocationListener() {
//            @Override
//            public void onReceiveLocation(BDLocation bdLocation) {
//                if (bdLocation!=null){ //定位之后
//                    tv_at_create_find_customer_putlocation.setText(bdLocation.getCity());
//
//                    mLocCityName = bdLocation.getCity();
//                    mLocProvinceName = bdLocation.getProvince();
//                    mLocCityCode = findLocCityCodeByName(mLocProvinceName, mLocCityName);
//                    if (mLocCityName != null && !mLocCityName.equals(mCityName)) {
//                        wipedata();
//                    }
//                    mCityName = bdLocation.getCity();
//                    mProvinceName = bdLocation.getProvince();
//                    mCityCode = findLocCityCodeByName(mProvinceName, mCityName);
//                }
//
//            }
//        });
    }

    /**
     * 初始化view，初始化内容
     */
    private void initView() {
        ll_at_create_find_customer_location = (LinearLayout) findViewById(R.id.ll_at_create_find_customer_location);
        ll_at_create_find_customer_location.setOnClickListener(this);
        tv_at_create_find_customer_location = (TextView) findViewById(R.id.tv_at_create_find_customer_location);
        imageView_fangda = (ImageView) findViewById(R.id.imageView_fangda);
        imageView_fangda.setOnClickListener(this);
        imageView_suoxiao = (ImageView) findViewById(R.id.imageView_suoxiao);
        imageView_suoxiao.setOnClickListener(this);
        imageView_jingzhundingwei = (ImageView) findViewById(R.id.imageView_jingzhundingwei);
        imageView_jingzhundingwei.setOnClickListener(this);
        tv_ac_create_find_customer_radius = (TextView) findViewById(R.id.tv_ac_create_find_customer_radius);
        tvMore = (TextView) findViewById(R.id.tvMore);
        tvMore.setVisibility(View.VISIBLE);
        tv_at_create_find_customer_putlocation = (TextView) findViewById(R.id.tv_at_create_find_customer_putlocation);
        tv_ac_create_find_customer_scope = (LinearLayout) findViewById(R.id.tv_ac_create_find_customer_scope);
        tv_ac_create_find_customer_scope.setOnClickListener(this);
        ll_at_create_find_customer_search = (LinearLayout) findViewById(R.id.ll_at_create_find_customer_search);
        ll_at_create_find_customer_search.setOnClickListener(this);
        ll_at_create_find_customer_type = (LinearLayout) findViewById(R.id.ll_at_create_find_customer_type);
        ll_at_create_find_customer_type.setOnClickListener(this);
        tv_at_create_find_customer_type = (TextView) findViewById(R.id.tv_at_create_find_customer_type);
        ll_ac_create_find_customer_set = (LinearLayout) findViewById(R.id.ll_ac_create_find_customer_set);
        ll_ac_create_find_customer_set.setOnClickListener(this);
        tv_ac_create_find_customer_set = (TextView) findViewById(R.id.tv_ac_create_find_customer_set);
        tv_at_create_find_customer_nextstep = (TextView) findViewById(R.id.tv_at_create_find_customer_nextstep);
        tv_at_create_find_customer_nextstep.setOnClickListener(this);
        et_ac_create_find_customer_name = findViewById(R.id.et_ac_create_find_customer_name);
//        et_ac_create_find_customer_budget = (EditText) findViewById(R.id.et_ac_create_find_customer_budget);
        tv_name2 = (TextView) findViewById(R.id.tv_name2);
        et_ac_create_find_customer_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_name2.setText(s.length() + "/12");
            }
        });
        tv_at_create_find_customer_cancel = (TextView) findViewById(R.id.tv_at_create_find_customer_cancel);
        tv_at_create_find_customer_cancel.setOnClickListener(this);
        sv_main = findViewById(R.id.sv_main);
        rl_map = findViewById(R.id.rl_map);

        if (getIntent() != null) { //点击任务详情页面，再次创建任务，任务数据传过来
            isReCreate = getIntent().getBooleanExtra("isReCreate", false);
            mTaskOut = (TaskOut) getIntent().getSerializableExtra("taskOut");
        }

        if (isReCreate) {
            //不需要再次创建
            mThrowAddress = mTaskOut.getThrowAddress();//上一次的投放地址
            String rangeRadius = mTaskOut.getRangeRadius();//上次的投放半径
            if (!TextUtils.isEmpty(rangeRadius)) {
                for (int i = 0; i < mCircleRadiusKM.length; i++) {
                    if (rangeRadius.equals(mCircleRadiusKM[i])) {
                        mCurrentScaleLevelPositon = i;
                        mRadiusFromPosition = i;
                    }
                }
            }
            tv_ac_create_find_customer_radius.setText(rangeRadius);
            //页面任务
            int type = mTaskOut.getType();
            if (type == 1) {
                tv_at_create_find_customer_type.setText("短信任务");
            } else if (type == 2) {
                tv_at_create_find_customer_type.setText("页面任务");
            }
            //定向设置
            OrientationSettingsOut orientationSettingsOut = mTaskOut.getOrientationSettingsOut();
            ageF = orientationSettingsOut.getAgeF();
            ageB = orientationSettingsOut.getAgeB();
            educationLevelF = orientationSettingsOut.getEducationLevelF();
            educationLevelB = orientationSettingsOut.getEducationLevelB();
            sex = orientationSettingsOut.getSex();
            consumptionCapacityF = orientationSettingsOut.getConsumptionCapacityF();
            consumptionCapacityB = orientationSettingsOut.getConsumptionCapacityB();
            ascription = orientationSettingsOut.getAscription();
            List<String> jixing = orientationSettingsOut.getJixing();//机型数组

            for (int i = 0; i < jixing.size(); i++) {
                if (i == 0) {
                    phoneModelIds = phoneModelIds + jixing.get(i);
                } else {
                    phoneModelIds = phoneModelIds + "," + jixing.get(i);
                }
            }
            List<String> xingqu = orientationSettingsOut.getXingqu();//兴趣数组
            for (int i = 0; i < xingqu.size(); i++) {
                if (i == 0) {
                    interestIds = interestIds + xingqu.get(i);
                } else {
                    interestIds = interestIds + "," + xingqu.get(i);
                }
            }
            // phoneModelIds = o.getJixing();
//            interestIds = data.getStringExtra("interestIds");
            tv_ac_create_find_customer_set.setText("已设置");
            mCityCode = mTaskOut.getCityCode().trim();//城市编码
        }
    }

    /**
     * 方向变化之后
     * @param sensorEvent
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private String smsPrice = "";//短信单价
    private String pagePrice = "";//页面单价

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        Intent intent = null;
        MDBasicRequestMap map = null;
        int status = -777;
        switch (v.getId()) {
            case R.id.ll_at_create_find_customer_location://手动去选择位置
                intent = new Intent(this, CityPickActivity.class);

                if (!TextUtils.isEmpty(mLocCityName)) {
                    intent.putExtra("locCityName", mLocCityName);
                    intent.putExtra("locCityCode", findLocCityCodeByName(mLocProvinceName, mLocCityName));
                    intent.putExtra("locProvinceName", mLocProvinceName);
                    intent.putExtra("locCityStatus", findLocCityStatusByName(mLocProvinceName, mLocCityName));
                } else {
                    intent.putExtra("locCityName", mCityName);
                    intent.putExtra("locCityCode", findLocCityCodeByName(mProvinceName, mCityName));
                    intent.putExtra("locProvinceName", mProvinceName);
                    intent.putExtra("locCityStatus", findLocCityStatusByName(mProvinceName, mCityName));
                }

                startActivityForResult(intent, 1);
                break;
            case R.id.imageView_fangda://地图放大开关

                if (mCurrentScaleLevelPositon == 0) {
                    return;
                }
                mCurrentScaleLevelPositon--;
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(mCurrentLatLng).zoom(mScaleLevel[mCurrentScaleLevelPositon]);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                tv_ac_create_find_customer_radius.setText(mCircleRadiusKM[mCurrentScaleLevelPositon]);
                mIsShowDialog = false;

                //发起反地理编码检索
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(mCurrentLatLng)
                        .newVersion(0));
                break;
            case R.id.imageView_suoxiao:  //地图缩小

                if (mCurrentScaleLevelPositon == mScaleLevel.length - 1) {
                    return;
                }
                mCurrentScaleLevelPositon++;
                MapStatus.Builder builder2 = new MapStatus.Builder();
                builder2.target(mCurrentLatLng).zoom(mScaleLevel[mCurrentScaleLevelPositon]);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder2.build()));
                tv_ac_create_find_customer_radius.setText(mCircleRadiusKM[mCurrentScaleLevelPositon]);
                mIsShowDialog = false;
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(mCurrentLatLng).newVersion(0));
                break;
            case R.id.imageView_jingzhundingwei: //回到当前位置
                isFirstLoc = true;
                mIsShowDialog = true;
                mLocClient.start();
                break;
            case R.id.tv_ac_create_find_customer_scope://范围半径
                showRadiusFromDialog(mCircleRadiusKMLists, tv_ac_create_find_customer_radius);
                break;
            case R.id.ll_at_create_find_customer_search://搜索
                intent = new Intent(this, SearchActivity.class);
                startActivityForResult(intent, 2);
                break;
            case R.id.ll_at_create_find_customer_type://任务类型
                if (TextUtils.isEmpty(mCityCode)) {
                    ToastUtils.showShort("请选择业务城市！");
                    return;
                }
                status = findLocCityStatusByName(mProvinceName, mCityName);
                if (status != 1) {
                    ToastUtils.showShort(mCityName + "没有开通业务！");
                    return;
                }
                //TODO
                map = new MDBasicRequestMap();
                map.put("userId", UserDataManeger.getInstance().getUserId());
                map.put("cityCode", mCityCode);
                OkHttpClientManager.postAsyn(HttpConstant.SELECT_TASK_TYPE, new OkHttpClientManager.ResultCallback<TaskTypeBean>() {
                    @Override
                    public void onError(Exception e) {

                        ToastUtils.showShort(e.getMessage());
                    }

                    @Override
                    public void onResponse(TaskTypeBean response) {
                        if (response.getCode() == 0) {
                            Map<String, String> result = response.getResult();
                            smsPrice = result.get("smsPrice");
                            pagePrice = result.get("pagePrice");
                            boolean isSmsOpen = true;
                            boolean isPageOpen = true;
                            if (smsPrice.equals("0")) {
                                isSmsOpen = false;
                            }
                            if (pagePrice.equals("0")) {
                                isPageOpen = false;
                            }
                            if (!isPageOpen && !isSmsOpen) {
                                ToastUtils.showShort("短信页面和页面任务都未开通");
                                return;
                            }
                            final TaskTypeDialog dialog = new TaskTypeDialog(CreateFindCustomerActivity2.this,
                                    "单价(元):" + smsPrice,
                                    "单价(元):" + pagePrice,
                                    isSmsOpen,
                                    isPageOpen);
                            dialog.setClickListenerInterface(new TaskTypeDialog.ClickListenerInterface() {

                                @Override
                                public void doConfirm(String chooseType) {
                                    mTaskType = chooseType;
                                    tv_at_create_find_customer_type.setText(mTaskType);
                                    dialog.dismiss();
                                }


                            });
                            dialog.show();
                        }

                    }
                }, map);
                break;
            case R.id.ll_ac_create_find_customer_set://定向设置
                if (TextUtils.isEmpty(mCityCode)) {
                    ToastUtils.showShort("请选择业务城市!");
                    return;
                }
                status = findLocCityStatusByName(mProvinceName, mCityName);
                if (status != 1) {
                    ToastUtils.showShort(mCityName + "没有开通业务！");
                    return;
                }
                intent = new Intent(CreateFindCustomerActivity2.this, DirectionalSettingActivity.class);
                intent.putExtra("cityCode", mCityCode);
                intent.putExtra("isReCreate", isReCreate);

                if (isReCreate) {
                    intent.putExtra("orientationSettingsOut", mTaskOut.getOrientationSettingsOut());
                }
                startActivityForResult(intent, 3);
                break;

            case R.id.tv_at_create_find_customer_nextstep://下一步
                //final String cityCode = mCityCode;//城市Code
                //对认证状态进行判断
                int authStatus = Integer.parseInt(ACache.get(this).getAsString(SharedPreferencesTool.CERTIFICATE_STATUS));//1、未认证2审核中 3已认证 4审核未通过
                String cerStatus = "";
                String remindString = "";
                int imgResId = 0;
                String btText = "";

                if (authStatus == 1) {
                    //未认证
                    cerStatus = getString(R.string.no_cer);
                    remindString = getString(R.string.no_certified);
                    imgResId = R.mipmap.no_certified;
                    btText = getString(R.string.go_cer);
                } else if (authStatus == 2) {
                    //审核中
                    cerStatus = getString(R.string.cer_ing);
                    remindString = getString(R.string.cer_waiting);
                    imgResId = R.mipmap.audit;
                    btText = getString(R.string.go_watch);
                } else if (authStatus == 4) {
                    //审核未通过
                    cerStatus = getString(R.string.no_pass);
                    remindString = getString(R.string.not_pass);
                    imgResId = R.mipmap.not_pass;
                    btText = getString(R.string.re_go_cer);
                }
                if (authStatus != 3) {
                    final RemindDialog dialog = new RemindDialog(this, cerStatus, remindString, imgResId, btText);
                    dialog.setClickListenerInterface(new RemindDialog.ClickListenerInterface() {
                        @Override
                        public void doCertificate() {
                            dialog.dismiss();
                            startActivity(new Intent(getBaseContext(), CertificationActivity.class));
                            finish();
                        }
                    });
                    dialog.setCancelable(true);
                    dialog.show();
                } else {
                    //已认证
                    toNext();
                }
                break;
            case R.id.tv_at_create_find_customer_cancel://取消
                finish();
                break;
            default:
                break;

        }
    }

    /**
     * 下一步验证
     */
    private void toNext() {
        if (!TextUtils.isEmpty(mMoveCityName) && !mCityName.equals(mMoveCityName)) {
            //清空数据。
            showDialog();
            wipedata();
            return;
        }
        if (!TextUtils.isEmpty(mMoveAddress)){
            tv_at_create_find_customer_putlocation.setText(mMoveAddress);
        }
        //投放地址
        mThrowAddress = tv_at_create_find_customer_putlocation.getText().toString().trim();
        int type = -666;
        mStrType = tv_at_create_find_customer_type.getText().toString().trim();
        if (mStrType.equals("页面任务")) {
            type = 2;
        } else if (mStrType.equals("短信任务")) {
            type = 1;
        } else {
            ToastUtils.showShort("请选择任务类型");
            return;
        }
        String taskName = et_ac_create_find_customer_name.getText().toString().trim();//任务名称
        if (TextUtils.isEmpty(taskName)) {
            ToastUtils.showShort("请填写任务名称");
            return;
        }
        String rangeRadius = mCircleRadiusM[mCurrentScaleLevelPositon];
//        String budget = et_ac_create_find_customer_budget.getText().toString().trim();
//        if (TextUtils.isEmpty(budget)) {
//            ToastUtils.showShort("请填写任务预算");
//            return;
//        }
//        BigDecimal b_budget = BigDecimal.valueOf(Double.valueOf(budget));

        String longitude = String.valueOf(mCurrentLatLng.longitude);
        if (TextUtils.isEmpty(longitude)) {
            ToastUtils.showShort("定位失败，请选择业务城市");
            return;
        }
        BigDecimal b_longitude = BigDecimal.valueOf(Double.valueOf(longitude));
        String dimension = String.valueOf(mCurrentLatLng.latitude);
        if (TextUtils.isEmpty(dimension)) {
            ToastUtils.showShort("定位失败，请选择业务城市");
            return;
        }
        if (TextUtils.isEmpty(mCityCode)) {
            ToastUtils.showShort("定位失败，请选择业务城市");
            return;
        }
        BigDecimal b_dimension = BigDecimal.valueOf(Double.valueOf(dimension));
        if (TextUtils.isEmpty(ageF)
                || TextUtils.isEmpty(ageB)
                || TextUtils.isEmpty(educationLevelF)
                || TextUtils.isEmpty(educationLevelB)
                || TextUtils.isEmpty(sex)
                || TextUtils.isEmpty(consumptionCapacityF)
                || TextUtils.isEmpty(consumptionCapacityB)
                || TextUtils.isEmpty(ascription)
                || TextUtils.isEmpty(phoneModelIds)
                || TextUtils.isEmpty(interestIds)
                ) {
            ToastUtils.showShort("请设置定向设置");
            return;
        } else {
            MDBasicRequestMap map = new MDBasicRequestMap();
            map.put("userId", UserDataManeger.getInstance().getUserId());
//            map.put("money", b_budget + "");
            map.put("longitude", b_longitude + "");
            map.put("latitude", b_dimension + "");
            map.put("cityCode", mCityCode);

            //通过验证到下一步页面
            final Intent data = new Intent();
            if (type == 1) {
                data.setClass(CreateFindCustomerActivity2.this, MessageTaskActivity.class);
            } else if (type == 2) {
                data.setClass(CreateFindCustomerActivity2.this, PageTaskActivity.class);
            }
            data.putExtra("ageF", ageF);
            data.putExtra("ageB", ageB);
            data.putExtra("educationLevelF", educationLevelF);
            data.putExtra("educationLevelB", educationLevelB);
            data.putExtra("sex", sex);
            data.putExtra("consumptionCapacityF", consumptionCapacityF);
            data.putExtra("consumptionCapacityB", consumptionCapacityB);
            data.putExtra("ascription", ascription);
            data.putExtra("phoneModelIds", phoneModelIds);
            data.putExtra("interestIds", interestIds);
            data.putExtra("cityCode", mCityCode);
            data.putExtra("throwAddress", mThrowAddress);
            data.putExtra("type", type);
            data.putExtra("taskName", taskName);
            data.putExtra("rangeRadius", rangeRadius);
//            data.putExtra("budget", budget);
            data.putExtra("longitude", longitude);
            data.putExtra("dimension", dimension);
            data.putExtra("address", mThrowAddress);
            data.putExtra("cityName", mCityName);
            data.putExtra("pagePrice", pagePrice);
            data.putExtra("smsPrice", smsPrice);
            final int type2 = type;
            //下一步验证
            showDefaultLoading();
            OkHttpClientManager.postAsyn(HttpConstant.SELECT_NEXT_STEP_CHECK, new OkHttpClientManager.ResultCallback<NextStepCheckBean>() {
                @Override
                public void onError(Exception e) {
                    hideDefaultLoading();
                    ToastUtils.showShort(e.getMessage());
                }

                @Override
                public void onResponse(NextStepCheckBean response) {
                    hideDefaultLoading();
                    if (response.getCode() == 0) {
                        PreOut preOut = response.getResult();
                        if (preOut == null) {
                            ToastUtils.showShort("后台错误！");
                            return;
                        }
                        data.putExtra("day", preOut.getDay());//限制天数
                        if (type2 == 1) {//短信
                            startActivity(data);
                        } else if (type2 == 2) {//页面
                            //截图   // 截图，在SnapshotReadyCallback中保存图片到 sd 卡
                            showDefaultLoading();
                            mMapClippath = Environment.getExternalStorageDirectory().getPath() + "/ClipPhoto/cache/";
                            File dir = new File(mMapClippath);
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }
                            mMapClippath = mMapClippath + "map.png";
                            data.putExtra("mapPath", mMapClippath);
                            if (CommonUtils.haveSDCard()) {
                                if (hasPermisson()) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
                                                public void onSnapshotReady(Bitmap snapshot) {
                                                    File file = new File(mMapClippath);
                                                    if (!file.exists()) {
                                                        try {
                                                            file.createNewFile();
                                                        } catch (IOException e) {
                                                            Log.e("截图", e.getMessage());
                                                        }
                                                    }
                                                    FileOutputStream out;
                                                    try {
                                                        out = new FileOutputStream(file);
                                                        if (snapshot.compress(
                                                                Bitmap.CompressFormat.PNG, 100, out)) {
                                                            out.flush();
                                                            out.close();
                                                        }
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                hideDefaultLoading();
                                                            }
                                                        });
                                                        startActivity(data);
                                                        //finish();

                                                        Log.d("截图", "屏幕截图成功，图片存在: " + file.toString());
                                                    } catch (FileNotFoundException e) {
                                                        Log.e("截图", e.getMessage());
                                                    } catch (IOException e) {
                                                        Log.e("截图", e.getMessage());
                                                    }
                                                }
                                            });
                                        }
                                    }).start();
                                } else {
                                    hideDefaultLoading();
                                    requestPermission();
                                }
                            } else {
                                ToastUtils.showShort("没有SD卡!");
                            }
                        }
                    }

                }
            }, map);


            //ToastUtils.showLong("正在截取屏幕图片...");

            //startActivity(data);

        }
    }



    /**
     * 百度地图截图
     * @param path
     */
    private void clipBaiduMap(final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
                    public void onSnapshotReady(Bitmap snapshot) {
                        File file = new File(path);
                        FileOutputStream out;
                        try {
                            out = new FileOutputStream(file);
                            if (snapshot.compress(
                                    Bitmap.CompressFormat.PNG, 100, out)) {
                                out.flush();
                                out.close();
                            }

                            Log.e("截图", "屏幕截图成功，图片存在: " + file.toString());
                            ToastUtils.showLong("屏幕截图成功，图片存在: " + file.toString());
                        } catch (FileNotFoundException e) {
                            Log.e("截图", e.getMessage());
                        } catch (IOException e) {
                            Log.e("截图", e.getMessage());
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * 选择范围半径
     * @param listString
     * @param view
     */
    private void showRadiusFromDialog(List<String> listString, final TextView view) {
        if (listString != null && listString.size() > 0) {
            final SettingStringDialog dialog = new SettingStringDialog(this, R.style.Theme_Light_Dialog);
            dialog.setOnchangeListener(new SettingStringDialog.ChangeListener() {
                @Override
                public void onChangeListener(String data, int position) {
                    view.setText(data);
                    dialog.dismiss();
                    mCurrentScaleLevelPositon = position;
//                    mRadiusFromPosition = position;
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(mCurrentLatLng).zoom(mScaleLevel[mCurrentScaleLevelPositon]);
                    tv_ac_create_find_customer_radius.setText(mCircleRadiusKM[mCurrentScaleLevelPositon]);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }
            });
            dialog.setUpData(listString);
            dialog.setSelect(mCurrentScaleLevelPositon);
            dialog.show();
        }
    }


    private void showDialog() {
        final ConfirmDialog dialog = new ConfirmDialog(this, "您的地域有变动，请重新设置任务", "", "确定");
        dialog.show();
        dialog.setConfirmVisibility(View.GONE);
        dialog.setClickListenerInterface(new ConfirmDialog.ClickListenerInterface() {
            @Override
            public void doCancel() {
                dialog.dismiss();
                mIsShowDialog = true;
                boolean b = requestCityStatus(mMoveCityName);
                mCityName = mMoveCityName;
                mCityCode = mMoveCityCode;
                mProvinceName = mMoveProvinceName;
                if (!TextUtils.isEmpty(mMoveAddress)) {
                    tv_at_create_find_customer_putlocation.setText(mMoveAddress);
                }
                tv_at_create_find_customer_location.setText(mCityName);
            }

            @Override
            public void doConfirm() {
                dialog.dismiss();
            }
        });

    }

    private void wipedata() {
        //// TODO: 2017/12/24
        mTaskType = "";
        tv_at_create_find_customer_type.setText("--未设置--");
        et_ac_create_find_customer_name.setText("");
//        et_ac_create_find_customer_budget.setText("");
        //定向设置清空
        ageF = "";
        ageB = "";
        educationLevelF = "";
        educationLevelB = "";
        sex = "";
        consumptionCapacityF = "";
        consumptionCapacityB = "";
        ascription = "";
        phoneModelIds = "";
        interestIds = "";
        tv_ac_create_find_customer_set.setText("--未设置--");
        ACache.get(this).remove(SharedPreferencesTool.DIRECTION_HISTORY);

    }











    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1://手动选择城市
                // TODO
                if (data != null) {
                    //来自手动选择。
                    mFromAction = 1;

                    String tempCityName = data.getStringExtra("cityName");
                    if (!tempCityName.equals(mCityName)) {
                        //ToastUtils.showShort("");
                        //清空数据。
                        showDialog();
                        wipedata();
                    }
                    mCityCode = data.getStringExtra("cityCode");
                    mCityName = data.getStringExtra("cityName");
                    mProvinceName = data.getStringExtra("provinceName");

                    if (!TextUtils.isEmpty(mCityName)) {
                        tv_at_create_find_customer_location.setText(mCityName.trim().toCharArray(), 0, 3);
                        tvMore.setVisibility(View.GONE);
                        mSearch.geocode(new GeoCodeOption()
                                .city(mCityName).address("人民政府"));
                    }
                }
                break;
            case 2://搜索
                if (data != null) {
                    mSearchValue = data.getStringExtra("searchValue");
                    if (!TextUtils.isEmpty(mCityName)) {
                        mSearch.geocode(new GeoCodeOption()
                                .city("中国")
                                .address(mSearchValue));
                    } else {
                        mSearch.geocode(new GeoCodeOption()
                                .city("中国")
                                .address(mSearchValue));
                    }

                }
                break;
            case 3:
                if (data != null) {
                    ageF = data.getStringExtra("ageF");
                    ageB = data.getStringExtra("ageB");
                    educationLevelF = data.getStringExtra("educationLevelF");
                    educationLevelB = data.getStringExtra("educationLevelB");
                    sex = data.getStringExtra("sex");
                    consumptionCapacityF = data.getStringExtra("consumptionCapacityF");
                    consumptionCapacityB = data.getStringExtra("consumptionCapacityB");
                    ascription = data.getStringExtra("ascription");
                    phoneModelIds = data.getStringExtra("phoneModelIds");
                    interestIds = data.getStringExtra("interestIds");
                    tv_ac_create_find_customer_set.setText("已设置");
                    isReCreate = false;//将重新创建置false 定向 使用缓存的内容 更新界面
                }
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
        if (isReCreate) {
            getPageType();
        }
    }

    /**
     * 获取任务类型
     */
    private void getPageType() {
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());
        map.put("cityCode", mCityCode);
        OkHttpClientManager.postAsyn(HttpConstant.SELECT_TASK_TYPE, new OkHttpClientManager.ResultCallback<TaskTypeBean>() {
            @Override
            public void onError(Exception e) {
                ToastUtils.showShort(e.getMessage());
            }

            @Override
            public void onResponse(TaskTypeBean response) {
                if (response.getCode() == 0) {
                    Map<String, String> result = response.getResult();
                    smsPrice = result.get("smsPrice");
                    pagePrice = result.get("pagePrice");
                }

            }
        }, map);

    }

    /**
     * 获取省市列表
     * @param areaOuts
     */
    @Override
    public void getProvinceCity(List<AreaOut> areaOuts) {
        mAreaOuts = areaOuts;
        requestCityStatus(mCityName);
        mCityCode = findLocCityCodeByName(mProvinceName, mCityName);
        mLocCityCode = findLocCityCodeByName(mLocProvinceName, mLocCityName);
    }




    //地址转坐标的结果
    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(CreateFindCustomerActivity2.this,
                    "抱歉，未能找到结果",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        mCurrentLatLng = new LatLng(result.getLocation().latitude, result.getLocation().longitude);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(mCurrentLatLng)
                .zoom(mScaleLevel[mCurrentScaleLevelPositon]);//21-3===5m-1000km  15-500m,14-1km,13-2km,12.5-3km,12-5km,11,10km
        mBaiduMap.clear();
        tv_ac_create_find_customer_radius.setText(mCircleRadiusKM[mCurrentScaleLevelPositon]);
        final Marker marker = (Marker) mBaiduMap.addOverlay(new MarkerOptions()
                .position(mCurrentLatLng).alpha(0.0f)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.bg_ac_create_find_customer_location)));
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                .newMapStatus(builder.build()));

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
                marker.setPosition(mapStatus.target);
            }


            //地图移动结束之后
            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                mFromAction = 3;//来自地图移动
                mIsShowDialog = false;
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(mapStatus.target)
                        .newVersion(0));

            }
        });
        mFromAction = 4;//来自搜索
        mIsShowDialog = true;
        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(mCurrentLatLng)
                .newVersion(0));
    }



    ////根据经纬度获得地址的回调
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        Logger.d("获得了经纬度转换地址的回调");


        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//            Toast.makeText(CreateFindCustomerActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
//                    .show();
            return;
        }


        ReverseGeoCodeResult.AddressComponent addCom = result.getAddressDetail();
        String address = result.getAddress();
        if (mFromAction == 1) {//手动选择
            tv_at_create_find_customer_putlocation.setText(address);
        } else if (mFromAction == 2) {//定位
            tv_at_create_find_customer_putlocation.setText(address);

            mLocCityName = addCom.city;
            mLocProvinceName = addCom.province;
            mLocCityCode = findLocCityCodeByName(mLocProvinceName, mLocCityName);
            if (mLocCityName != null && !mLocCityName.equals(mCityName)) {
                wipedata();
            }
            mCityName = addCom.city;
            mProvinceName = addCom.province;
            mCityCode = findLocCityCodeByName(mProvinceName, mCityName);
        } else if (mFromAction == 3) {//来自地图平移
            //tv_at_create_find_customer_putlocation.setText(address);
            mMoveAddress = result.getAddress().trim();
            mMoveCityName = addCom.city;
            mMoveProvinceName = addCom.province;
            mMoveCityCode = findLocCityCodeByName(mMoveProvinceName, mMoveCityName);
        } else {
            mCityName = addCom.city;
            mProvinceName = addCom.province;
            mCityCode = findLocCityCodeByName(mProvinceName, mCityName);
            tv_at_create_find_customer_putlocation.setText(address);
        }

        mCurrentLatLng = result.getLocation();

        if (!TextUtils.isEmpty(mCityName)) { //百度定位的结果
            tv_at_create_find_customer_location.setText(mCityName.trim().toCharArray(), 0, 3);
            tvMore.setVisibility(View.GONE);
            requestCityStatus(mCityName);
        }


        //手动拖动地图后的地址
        if (result.getAddressDetail() != null) {
            tv_at_create_find_customer_putlocation.setText(result.getAddress());
        }


       /* Toast.makeText(CreateFindCustomerActivity.this, result.getAddress() + " adcode: " + result.getAdcode(),
                Toast.LENGTH_LONG).show();*/

    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                mCurrentLatLng = ll;
                MapStatus.Builder builder = new MapStatus.Builder();
                mCurrentScaleLevelPositon = 0;
                builder.target(ll).zoom(mScaleLevel[mCurrentScaleLevelPositon]);//21-3===5m-1000km  15-500m,14-1km,13-2km,12.5-3km,12-5km,11,10km
                //
                mBaiduMap.clear();
//                mBaiduMap.addOverlay(new CircleOptions()
//                        .center(mCurrentLatLng)
//                        .fillColor(Color.parseColor("#999999"))//330ab0eb
//                        .radius(mCircleRadius[mCurrentScaleLevelPositon]));
                final Marker marker = (Marker) mBaiduMap.addOverlay(new MarkerOptions()
                        .position(mCurrentLatLng).alpha(0.0f)
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.bg_ac_create_find_customer_location)));
                tv_ac_create_find_customer_radius.setText(mCircleRadiusKM[mCurrentScaleLevelPositon]);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                //// TODO: 2017/12/24
                mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
                    @Override
                    public void onMapStatusChangeStart(MapStatus mapStatus) {

                    }

                    @Override
                    public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

                    }

                    @Override
                    public void onMapStatusChange(MapStatus mapStatus) {
                        marker.setPosition(mapStatus.target);
                    }

                    @Override
                    public void onMapStatusChangeFinish(MapStatus mapStatus) {
                        mFromAction = 3;//来自地图移动
                        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                                .location(mapStatus.target)
                                .newVersion(0));
                        mIsShowDialog = false;

                    }
                });

                mFromAction = 2;//来自定位
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(mCurrentLatLng)
                        .newVersion(0));
            }
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
        super.onResume();
    }

    @Override
    protected void onStop() {
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        //startLocation();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        //GEO destroy
        mSearch.destroy();
        super.onDestroy();
    }

    @Override
    protected CityPickPresent<ICityPickView> createPresent() {
        return new CityPickPresent<>(this);
    }

    @Override
    public void onStart() {
        //startLocation();
        super.onStart();
    }


    //通过城市名称找城市代码
    private String findLocCityCodeByName(String provinceName, String cityName) {
        if (TextUtils.isEmpty(provinceName) || TextUtils.isEmpty(cityName)) {
            Logger.d("provinceName和cityName为空");
            return "";
        }
        if (mAreaOuts != null && mAreaOuts.size() > 0) {
            for (AreaOut areaOut : mAreaOuts) { //如果选手则的城市名和后台返回的对应，就返回城市码，否则返回空
                if (areaOut.getName().equals(provinceName) || provinceName.contains(areaOut.getName())) {
                    for (Area area : areaOut.getList()) {
                        if (area.getName().equals(cityName) || cityName.contains(area.getName())) {
                            return area.getCode().trim();
                        }
                    }
                }
            }
        }
        return "";
    }





    private int findLocCityStatusByName(String provinceName, String cityName) {
        if (TextUtils.isEmpty(provinceName) || TextUtils.isEmpty(cityName)) {
            return -666;
        }
        if (mAreaOuts != null && mAreaOuts.size() > 0) {
            for (AreaOut areaOut : mAreaOuts) {
                if (areaOut.getName().equals(provinceName) || provinceName.contains(areaOut.getName())) {
                    for (Area area : areaOut.getList()) {
                        if (area.getName().equals(cityName) || cityName.contains(area.getName())) {
                            return area.getStatus();
                        }
                    }
                }
            }
        }

        return -666;
    }



    private boolean requestCityStatus(String cityName) {
//        if (!mIsShowDialog) {
//            return;
//        }
        if (mAreaOuts != null && mAreaOuts.size() > 0) {
            for (AreaOut areaOut : mAreaOuts) {
                if (areaOut.getName().equals(mProvinceName)) {
                    for (Area area : areaOut.getList()) {
                        if (area.getName().equals(mCityName)) {
                            if (area.getStatus() == 0) {
                                if (mIsShowDialog) {
                                    final RemindDialog dialog = new RemindDialog(this, "", "抱歉," + cityName + "未开通此业务~", R.mipmap.weikaitongyewu, "知道了");
                                    dialog.setClickListenerInterface(new RemindDialog.ClickListenerInterface() {
                                        @Override
                                        public void doCertificate() {
                                            dialog.dismiss();
                                            //stopLocation();
                                        }
                                    });
                                    dialog.setCancelable(true);
                                    dialog.show();
                                }
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;

    }

















    //确认，生成图片
    public void confirm(View view, String path) {
        bm = loadBitmapFromView(view);
        try {
            bm.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(path));
//            Toast.makeText(this, "图片已保存至：SD卡根目录/imageWithText.jpg", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //mPresent.uploadImg(2,filePath);

    }

    //以图片形式获取View显示的内容（类似于截图）
    public static Bitmap loadBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }


    /**
     * 是否有拍照的权限
     */
    @TargetApi(23)
    public boolean hasPermisson() {
        boolean b1 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean b2 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return b1 && b2;
    }

    /**
     * 请求拍照的权限
     */
    @TargetApi(23)
    public void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                0);
    }

    /**
     * 是否有地图的权限
     */
    @TargetApi(23)
    public boolean hasMapPermisson() {
        boolean b1 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        boolean b2 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        boolean b3 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission.ACCESS_WIFI_STATE);
        boolean b4 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        boolean b5 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission.READ_PHONE_STATE);
        return b1 && b2 && b3 && b4 && b5;
    }

    /**
     * 请求地图的权限
     */
    @TargetApi(23)
    public void requestMapPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.READ_PHONE_STATE},
                0);
    }

    @Override
    public void onBackPressed() {
        //返回的时候 清除 定向缓存
        // TODO: 2018/4/10 确定返回的时候删除定向缓存？？？？？
        ACache.get(this).remove(SharedPreferencesTool.DIRECTION_HISTORY);
        super.onBackPressed();
    }
}
