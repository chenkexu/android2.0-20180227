package com.orientdata.lookforcustomers.view.home;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.blankj.utilcode.util.NetworkUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.AddressSearchRecode;
import com.orientdata.lookforcustomers.bean.Area;
import com.orientdata.lookforcustomers.bean.AreaOut;
import com.orientdata.lookforcustomers.bean.BannerBean;
import com.orientdata.lookforcustomers.bean.OrderDeliveryBean;
import com.orientdata.lookforcustomers.bean.TaskCountBean;
import com.orientdata.lookforcustomers.manager.LbsManager;
import com.orientdata.lookforcustomers.runtimepermissions.PermissionsManager;
import com.orientdata.lookforcustomers.util.AppManager;
import com.orientdata.lookforcustomers.util.BuglyUtil;
import com.orientdata.lookforcustomers.util.CommonUtils;
import com.orientdata.lookforcustomers.util.GlideUtil;
import com.orientdata.lookforcustomers.util.SharedPreferencesTool;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.agreement.MyWebViewActivity;
import com.orientdata.lookforcustomers.view.certification.fragment.ACache;
import com.orientdata.lookforcustomers.view.findcustomer.SearchActivity;
import com.orientdata.lookforcustomers.view.findcustomer.TaskDeliveryView;
import com.orientdata.lookforcustomers.view.findcustomer.TaskDetailActivity;
import com.orientdata.lookforcustomers.view.home.fragment.MeActivity;
import com.orientdata.lookforcustomers.widget.MyListView;
import com.orientdata.lookforcustomers.widget.abslistview.CommonAdapter;
import com.orientdata.lookforcustomers.widget.abslistview.ViewHolder;
import com.orientdata.lookforcustomers.widget.dialog.RemindDialog;
import com.qiniu.android.common.Constants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import q.rorbin.badgeview.QBadgeView;
import vr.md.com.mdlibrary.utils.image.ImageUtil;
import zhy.com.highlight.HighLight;
import zhy.com.highlight.interfaces.HighLightInterface;
import zhy.com.highlight.position.OnLeftPosCallback;
import zhy.com.highlight.position.OnRightPosCallback;
import zhy.com.highlight.position.OnTopPosCallback;
import zhy.com.highlight.shape.CircleLightShape;
import zhy.com.highlight.shape.RectLightShape;

import static com.orientdata.lookforcustomers.util.CommonUtils.getRandom2;
import static com.qiniu.android.common.Constants.latitude;
import static com.qiniu.android.common.Constants.longitude;


/**
 * 创建寻客页面
 */
public class MainHomeActivity extends BaseActivity<IHomeMainView, MainHomePresenter<IHomeMainView>> implements IHomeMainView, SensorEventListener, OnGetGeoCoderResultListener {

    private static final String TAG = "DemoActivity";
    // 定位相关
    public MyLocationListenner myListener;
    @BindView(R.id.title)
    LinearLayout title;
    @BindView(R.id.bt_go_orintion)
    Button btGoOrintion;
    @BindView(R.id.iv_service)
    ImageView ivService;
    @BindView(R.id.tv_person_num)
    TextView tvPersonNum;

    @BindView(R.id.top_view)
    View top_view;
    @BindView(R.id.lv_radius)
    ListView lvRadius;
    @BindView(R.id.lv_banner)
    MyListView lvBanner;
    @BindView(R.id.ll_down)
    LinearLayout llDown;
    @BindView(R.id.ll_banner_scroll)
    LinearLayout llBannerScroll;
    @BindView(R.id.iv_me)
    ImageView ivMe;

//
//    @BindView(R.id.iv_close_task)
//    ImageView ivCloseTask;
//    @BindView(R.id.tv_remain_time)
//    CountdownView tvRemainTime;
//    @BindView(R.id.tv_show_detail)
//    TextView tvShowDetail;
//    @BindView(R.id.iv_speed)
//    ImageView ivSpeed;
//    @BindView(R.id.tv_cooling_time)
//    CountdownView tvCoolingTime;
//    @BindView(R.id.cd_task_delivery)
//    CardView cdTaskDelivery;
//

    @BindView(R.id.iv_task)
    ImageView ivTask;


    @BindView(R.id.tv_time)
    TextView tvtime;


    @BindView(R.id.task_delivery)
    TaskDeliveryView taskDeliveryView;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.ll_radius)
    LinearLayout llRadius;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;


    private MyLocationConfiguration.LocationMode mCurrentMode;

    private String mProvinceName = "";

    private String mLocCityName = "";
    private String mLocProvinceName = "";
    private String mLocCityCode = "";

    private MainHomePresenter mCityPickPresent;
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


    private int locationFlag;

    //    float[] mScaleLevel = {15.0f, 14.0f, 13.0f, 12.66f, 12.0f, 11.0f};
    float[] mScaleLevel = {15.6f, 15.0f, 14.0f, 14.00f, 13.0f, 12.0f};


    int[] mCircleRadius = {500, 1000, 2000, 3000, 5000, 10000};//米
    String[] mCircleRadiusKM = {"500M", "1KM", "2KM", "3KM", "5KM", "10KM"};
    String[] mCircleRadiusM = {"500", "1000", "2000", "3000", "5000", "10000"};
    private String CurrentCircleRadius = "500";


    List<String> mCircleRadiusKMLists = new ArrayList<>();//{"10KM", "5KM", "3KM", "2KM", "1KM", "500M"}范围半径数组
    private static final int accuracyCircleFillColor = 0x00FFFF88;
    private static final int accuracyCircleStrokeColor = 0x0000FF00;
    int mCurrentScaleLevelPositon = 0;

    //GEO
    GeoCoder mSearch = null;
    private boolean mIsShowDialog = true;  //这个城市是否有业务的flag
    private String mTaskType;//任务类型。
    private String ageF;
    private String ageB;
    private String sex;
    private String interestIds = "";
    private int mFromAction;
    private UiSettings mUiSettings;

    //百度地图事件分发处理
//    ScrollView sv_main;
    private String mMoveAddress;
    private String mMoveCityName;
    private String mMoveProvinceName;
    private String mMoveCityCode;
    private boolean isReCreate = false;//是否是寻客详情点进来的重新创建



    private String industryMark;
    private String industryNameStr;

    //重要的变量
    private String mCityCode = "";  //创建任务的cityCode
    private String mThrowAddress;   //投放地址
    private LatLng mCurrentLatLng; //当前的经纬度
    private String mCityName = ""; //城市名称
    private String mProvinceCode;  //省code
    private TextView tv_at_create_find_customer_putlocation;//最上面显示的投放位置


    private List<TextView> rudioss = new ArrayList<>();
    private OverlayOptions oCircle;
    private String path;
    private int mCurrentRadiuPos = 0;
    private CommonAdapter<String> strngCommonAdapter;
    private Intent intent;
    private BottomSheetBehavior behavior;
    private HashMap<String, Integer> cityMap;
    private AddressSearchRecode addressInfo;

    private OrderDeliveryBean orderDeliveryBean;

    boolean showRedPoint = false;
    private BigDecimal bg;
    private float zoom;
    protected boolean isImmersionBarEnabled() {
        return false;
    }
    private boolean isSearch = false;

    private HighLight mHightLight;

    @OnClick({R.id.imageView_jingzhundingwei, R.id.iv_task,
            R.id.bt_go_orintion, R.id.iv_service,
            R.id.ll_at_create_find_customer_search, R.id.ll_down, R.id.iv_me})
    public void onViewClicked(View view) {
        int status = -777;
        switch (view.getId()) {
            case R.id.iv_task: //显示任务进行中的布局
                ivTask.setVisibility(View.GONE);

                // 初始化需要加载的动画资源
                Animation animation = AnimationUtils
                        .loadAnimation(this, R.anim.push_in);
                taskDeliveryView.startAnimation(animation);
                taskDeliveryView.setVisibility(View.VISIBLE);
                break;
//            case R.id.iv_close_task: //关闭任务
//                ivTask.setVisibility(View.VISIBLE);
//                cdTaskDelivery.setVisibility(View.GONE);
//                break;

//            case R.id.iv_speed: //加速
//
//                break;

            case R.id.iv_me:
                Intent intent = new Intent(this, MeActivity.class);
                intent.putExtra(Constants.showRedPoint, showRedPoint);
                startActivity(intent);
                break;
            case R.id.ll_down:
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.imageView_jingzhundingwei:
                isFirstLoc = true;
                mIsShowDialog = true;
                LbsManager.getInstance().getLocation(myListener);
                break;
            case R.id.bt_go_orintion: //打开定向设置
                status = findLocCityStatusByName(mProvinceName, mCityName);
                if (!NetworkUtils.isConnected()) {
                    ToastUtils.showShort("请检查网络连接");
                    return;
                }
                if (status != 1) {
                    ToastUtils.showShort(mCityName + "该城市没有开通业务！");
                    return;
                }
                if (mCurrentLatLng == null || TextUtils.isEmpty(mCityCode)) {
                    ToastUtils.showShort("网络错误，请重新定位");
                    return;
                }
                boolean b = requestCityStatus(mCityName);
                this.intent = new Intent(MainHomeActivity.this, DirectionalSettingActivity3.class);
                this.intent.putExtra(latitude, mCurrentLatLng.latitude + ""); //精度
                this.intent.putExtra(longitude, mCurrentLatLng.longitude + ""); //维度
                this.intent.putExtra("cityCode", mCityCode); //城市编码
                this.intent.putExtra(Constants.mProvinceCode, mProvinceCode); //省编码

                //投放变径
                this.intent.putExtra(Constants.CurrentCircleRadius, CurrentCircleRadius);
                //投放的城市名
                this.intent.putExtra(Constants.mCityName, mCityName);

                //地点名称
                this.intent.putExtra("address", tv_at_create_find_customer_putlocation.getText().toString().trim());


                mBaiduMap.snapshotScope(null, new BaiduMap.SnapshotReadyCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSnapshotReady(Bitmap bitmap) {
                        if (bitmap != null) {
                            hideDefaultLoading();
                            byte[] bytes = ImageUtil.compressImage(bitmap);
                            MainHomeActivity.this.intent.putExtra("bitmap", bytes);
                        }
//                            String baiduMapPath = ImageUtils2.saveBitmap(MainHomeActivity.this, bitmap);
//                            Logger.d(baiduMapPath);
//                            if (baiduMapPath != null) {
//                                hideDefaultLoading();
//                                intent.putExtra("path", baiduMapPath);
//                                Logger.d("百度地图截图路径为：" + baiduMapPath);
//
//                            }
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainHomeActivity.this, btGoOrintion, getString(R.string.share_view));
                        if (modelFiltering()) {
                            startActivity(MainHomeActivity.this.intent, options.toBundle());
                        } else {
                            startActivity(MainHomeActivity.this.intent);
                        }
//                            EasyTransitionOptions options =
//                                        EasyTransitionOptions.makeTransitionOptions(
//                                                MainHomeActivity.this,
//                                                findViewById(R.id.bt_go_orintion),
//                                                findViewById(R.id.bmapView));
//                                EasyTransition.startActivity(intent, options);
                    }
                });
                showDefaultLoading();
                break;
            case R.id.iv_service:
                final String url = "mqqwpa://im/chat?chat_type=wpa&uin=2280249239";
                if (CommonUtils.checkApkExist(this, "com.tencent.mobileqq")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } else {
                    ToastUtils.showShort("本机未安装QQ应用,请下载安装。");
                }
                break;
            case R.id.ll_at_create_find_customer_search: //打开搜索页面
                this.intent = new Intent(this, SearchActivity.class);
                startActivityForResult(this.intent, 2);
                break;
        }
    }


    public void showNextTipViewOnCreated() {
        mHightLight = new HighLight(this)//
                .autoRemove(false)
                .intercept(true)
                .enableNext()
                .setOnLayoutCallback(new HighLightInterface.OnLayoutCallback() {
                    @Override
                    public void onLayouted() {
                        mHightLight
                                .addHighLight(R.id.ll_radius, R.layout.info_gravity_left_down2, new OnLeftPosCallback(50), new RectLightShape())
                                .addHighLight(R.id.bt_go_orintion, R.layout.guide_info, new OnTopPosCallback(30), new RectLightShape())
                                .addHighLight(R.id.iv_me, R.layout.info_gravity_left_down, new OnRightPosCallback(15), new CircleLightShape());
                        //界面布局完成添加tipview
                        //然后显示高亮布局
                        mHightLight.show();
                    }
                })
                .setClickCallback(new HighLight.OnClickCallback() {
                    @Override
                    public void onClick() {
                        mHightLight.next();
                    }
                });
        SharedPreferencesTool.getInstance().putBoolean(SharedPreferencesTool.guide_main, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCityPickPresent = new MainHomePresenter(this);
        setContentView(R.layout.activity_main_home);
        ButterKnife.bind(this);
        initView();
        for (String s : mCircleRadiusKM) {
            mCircleRadiusKMLists.add(s);
        }
        if (!hasMapPermisson()) {
            requestMapPermission();
        }
        initBaiduMap();
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .statusBarView(R.id.top_view)
                .init();
        //检查升级
        BuglyUtil.checkUpdate();
        initData();
        boolean booleanValue = SharedPreferencesTool.getInstance().getBooleanValue(SharedPreferencesTool.guide_main, true);
        if (booleanValue) {
            showNextTipViewOnCreated();
        }
    }



    private void initData() {
//        long time4 = (long)2 * 24 * 60 * 60;
//        tvRemainTime.start(time4);
        getProvinceCity();
    }


    /**
     * 初始化百度地图
     */
    private void initBaiduMap() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mMapView.showZoomControls(false);//去掉缩放控件
        mMapView.showScaleControl(false);
        mMapView.setLogoPosition(LogoPosition.logoPostionleftTop);
        mBaiduMap = mMapView.getMap();
        mUiSettings = mBaiduMap.getUiSettings();
        mCurrentMarker = BitmapDescriptorFactory
                .fromBitmap(Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565));
        MyLocationConfiguration mcf = new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker, accuracyCircleFillColor, accuracyCircleStrokeColor);


        //隐藏百度地图的logo
        View baiduLogo = mMapView.getChildAt(1);
        if (baiduLogo != null && (baiduLogo instanceof ImageView || baiduLogo instanceof ZoomControls)) {
            baiduLogo.setVisibility(View.GONE);
        }

        mBaiduMap.setMyLocationConfiguration(mcf);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setCompassEnable(false);


        myListener = new MyLocationListenner();

        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);

        Logger.d("首次进来创建寻客——------");
        //去定位
        LbsManager.getInstance().getLocation(myListener);

        //地图的单击事件
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                moveMapTo(latLng.latitude, latLng.longitude, true);
                setMapCenterInfo(latLng, mCircleRadiusM[mCurrentRadiuPos]);
                mCurrentLatLng = latLng;
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                float zoom = mapStatus.zoom;
                if (Math.abs(MainHomeActivity.this.zoom - zoom) > 0.000001) {
                    // 你的代码...
                    MainHomeActivity.this.zoom = zoom;
                    Logger.d("zoom", "缩放起了变化，现在缩放等级为" + zoom);
                } else {
                    if (!isSearch) { //如果不是搜索
                        mCurrentLatLng = mapStatus.target;
                        //开始坐标转地址
                        mFromAction = 3;//来自地图移动
                        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                                .location(mapStatus.target)
                                .newVersion(0));
                        mIsShowDialog = true;
                    }else{ //如果是搜索
                        isSearch = false;
                    }
                }
                Logger.d("地图移动结束");
            }
        });
    }


    /**
     * 初始化view，初始化内容
     */
    private void initView() {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.sliding_layout);
        View bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        tv_at_create_find_customer_putlocation = (TextView) findViewById(R.id.tv_at_create_find_customer_putlocation);
        rl_map = findViewById(R.id.rl_map);

        //初始化选择半径
        strngCommonAdapter = new CommonAdapter<String>(this, R.layout.item_lv_radio, Arrays.asList(mCircleRadiusKM)) {
            @Override
            protected void convert(ViewHolder viewHolder, String item, int position) {
                viewHolder.setText(R.id.tv_radio, item);
                if (mCurrentRadiuPos == position) {
                    viewHolder.setBackgroundRes(R.id.ll_radius, R.drawable.round_border_radius2);
                    viewHolder.setTextColor(R.id.tv_radio, getResources().getColor(R.color.white));
                } else {
                    viewHolder.setBackgroundRes(R.id.ll_radius, R.drawable.round_border_radius);
                    viewHolder.setTextColor(R.id.tv_radio, getResources().getColor(R.color.black));
                }

            }
        };


        lvRadius.setAdapter(strngCommonAdapter);

        //半径的点击事件
        lvRadius.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentRadiuPos = position;
                CurrentCircleRadius = mCircleRadiusM[position];
                // 刷新listview
                strngCommonAdapter.notifyDataSetChanged();
                setMapCenterInfo(mCurrentLatLng, mCircleRadiusM[mCurrentRadiuPos]);
                moveMapTo(mCurrentLatLng.latitude, mCurrentLatLng.longitude, true, mScaleLevel[mCurrentRadiuPos]);

            }
        });


        //底部banner的拖动事件
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                //这里是bottomSheet 状态的改变，根据slideOffset可以做一些动画
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) { //折叠
                    frameLayout.setForeground(new ColorDrawable(0xFFFFFF));
                } else { //展开
                    frameLayout.setForeground(new ColorDrawable(0x70000000));
//                    backgroundAlpha(MainHomeActivity.this,0.1f);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //这里是拖拽中的回调，根据slideOffset可以做一些动画
                if (slideOffset == 1.0) {
                    llDown.setVisibility(View.VISIBLE);
                } else {
                    llDown.setVisibility(View.INVISIBLE);
//                    llBannerScroll.setBackground();

                }
            }
        });

        //点击事件
        taskDeliveryView.setOnItemClickListener(new TaskDeliveryView.OnClickListener() {
            @Override
            public void showTaskDetail() {
                Intent intent = new Intent(MainHomeActivity.this, TaskDetailActivity.class);
                if (orderDeliveryBean != null) {
                    intent.putExtra("taskId", orderDeliveryBean.getTask().getTaskId());
                }
                startActivity(intent);
            }

            @Override
            public void hideTaskDelivery() {
                ivTask.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils
                        .loadAnimation(MainHomeActivity.this, R.anim.push_out);
                taskDeliveryView.startAnimation(animation);
                taskDeliveryView.setVisibility(View.GONE);
            }
        });

    }


    /**
     * @param latitude
     * @param longitude
     * @param isAnimate
     * @param zoomLevel 调整地图的缩放比例
     */
    private void moveMapTo(double latitude, double longitude, boolean isAnimate, float zoomLevel) {
        // needRefreshAddress = true;
        MapStatus mMapStatus = new MapStatus.Builder().target(new LatLng(latitude, longitude)).zoom(zoomLevel).build();
        MapStatusUpdate msu = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        if (null != msu) {
            if (isAnimate) {
                // 设置中心点,移动到中心点
                mBaiduMap.animateMapStatus(msu);
            } else {
                mBaiduMap.setMapStatus(msu);
            }
        }
    }

    private void moveMapTo(double latitude, double longitude, boolean isAnimate) {
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(new LatLng(latitude, longitude));
        if (mBaiduMap == null) {
            return;
        }
        if (isAnimate) {
            mBaiduMap.animateMapStatus(msu);
        } else {
            mBaiduMap.setMapStatus(msu);
        }
    }

    //设置地图中心点的信息
    private void setMapCenterInfo(LatLng latLng, String radius) {
        int personNum = getRandom2(radius, mCityName);
        tvPersonNum.setText("当前范围约有" + personNum + "人");

        if (mBaiduMap == null || latLng == null) {
            Logger.e("mBaiduMap为空。。。。。");
            return;
        }
        mBaiduMap.clear();
        /**
         * 绘制圆形
         */
        CircleOptions oCircle = new CircleOptions().fillColor(0x260077FF)
                .center(latLng).stroke(new Stroke(5, 0x00000000))
                .radius(Integer.parseInt(radius));

        //绘制中心图标
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.icon_center);
        MarkerOptions options = new MarkerOptions();
        options.position(latLng)        // 位置
                .icon(icon)            // 图标
                .anchor(0.5f, 0.5f);//设置 marker 覆盖物的锚点比例，默认（0.5f, 1.0f）水平居中，垂直下对齐

        // 掉下动画
        //options.animateType(MarkerOptions.MarkerAnimateType.drop);
        mBaiduMap.addOverlay(oCircle);
        mBaiduMap.addOverlay(options);
//        MapStatus mMapStatus = new MapStatus.Builder().target(latLng).zoom(zoomLevel).build();
//        MapStatusUpdate msu = MapStatusUpdateFactory.newMapStatus(mMapStatus);
//        mBaiduMap.animateMapStatus(msu);
    }


    //设置任务的投放半径
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1://手动选择城市
                // TODO
                if (data != null) {
                }
                break;
            case 2://搜索
                if (data != null) {
                    addressInfo = (AddressSearchRecode) data.getSerializableExtra("searchValue");
                    moveMapTo(addressInfo.getLatitude(), addressInfo.getLongitude(), true);
                    setMapCenterInfo(new LatLng(addressInfo.getLatitude(), addressInfo.getLongitude()), mCircleRadiusM[mCurrentRadiuPos]);
                    mCurrentLatLng = new LatLng(addressInfo.getLatitude(), addressInfo.getLongitude());
                    //开始坐标转地址
                    mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                            .location(mCurrentLatLng)
                            .newVersion(0));
                    mFromAction = 4;

                }
                break;
            case 3: //定向设置
                if (data != null) {
                    // TODO: 2018/4/4 界面显示定向设置的内容
                    ageF = data.getStringExtra("ageF");//年龄
                    ageB = data.getStringExtra("ageB");
                    sex = data.getStringExtra("sex");//性别
                    interestIds = data.getStringExtra("interestIds");//二级兴趣点
                    industryMark = data.getStringExtra("industryMark");
                    industryNameStr = data.getStringExtra("industryNameStr");
                    isReCreate = false;//将重新创建置false 定向 使用缓存的内容 更新界面
                }
                break;
            default:
                break;
        }
    }


    /**
     * 方向变化之后
     *
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


    private boolean showErrorDialog = true;


    private void wipedata() {
        // TODO: 2017/12/24
        Logger.d("区域有变化，重新选择");
        mTaskType = "";
        //定向设置清空
        ageF = "";
        ageB = "";
        sex = "";
        interestIds = "";
        // TODO: 2018/5/3 清除短信内容缓存
        //清除定向设置缓存
        ACache.get(this).remove(SharedPreferencesTool.DIRECTION_HISTORY);
    }


    /**
     * 获取省市列表
     */
    private void getProvinceCity() {
        mCityPickPresent.getProvinceCityData();
        mCityPickPresent.getBannerPic();
    }


    /**
     * 获取省市列表
     *
     * @param areaOuts
     */
    @Override
    public void getProvinceCity(List<AreaOut> areaOuts) {
        mAreaOuts = areaOuts;
        requestCityStatus(mCityName);
        mCityCode = findLocCityCodeByName(mProvinceName, mCityName);
        mProvinceCode = findLocProviceCodeByName(mProvinceName, mCityName);
    }


    @Override
    public void getBannerSuc(final List<BannerBean> list) {
        //设置
        lvBanner.setAdapter(new CommonAdapter<BannerBean>(this, R.layout.item_lv_banner_home, list) {
            @Override
            protected void convert(ViewHolder viewHolder, BannerBean item, int position) {
                ImageView imageView = viewHolder.getView(R.id.iv_banner);
                GlideUtil.getInstance().loadAdImage(MainHomeActivity.this, imageView, item.getImageId(), true);
            }

        });
        lvBanner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) { //折叠
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else{
                    Intent intent = new Intent(MainHomeActivity.this, MyWebViewActivity.class);
                    intent.putExtra("url", list.get(position).getImageUrl());
                    intent.putExtra("title", list.get(position).getTitle());
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public void getTaskDeliveryInfo(OrderDeliveryBean orderDeliveryBean) {
        if (orderDeliveryBean == null) {
            taskDeliveryView.setVisibility(View.GONE);
        } else if (orderDeliveryBean.getTask() == null) {
            taskDeliveryView.setVisibility(View.GONE);
        } else {
            ivTask.setVisibility(View.GONE);
            this.orderDeliveryBean = orderDeliveryBean;
            taskDeliveryView.setVisibility(View.VISIBLE);
            taskDeliveryView.setData(orderDeliveryBean);
        }
    }


    @Override
    public void showRedPoint(TaskCountBean redCountBean) {

        if (redCountBean.getUnReadAnnouncementNum() == 0 && redCountBean.getUnReadMsgNum() == 0) {
            showRedPoint = false;
            new QBadgeView(this).bindTarget(ivMe).setBadgePadding(3, true).setBadgeGravity(Gravity.END | Gravity.TOP).setBadgeNumber(0);
        } else {
            showRedPoint = true;
            new QBadgeView(this).bindTarget(ivMe).setBadgePadding(3, true).setBadgeGravity(Gravity.END | Gravity.TOP).setBadgeNumber(-1);
        }
    }



    //地址转坐标的结果(手动选择城市，点击详情，搜索)
    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        Logger.d("地址转坐标的结果");
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MainHomeActivity.this,
                    "抱歉，未能找到结果",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
    }


    //根据经纬度获得地址的回调()
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            return;
        }
        ReverseGeoCodeResult.AddressComponent addCom = result.getAddressDetail();
        String address = result.getAddress();

        if (mFromAction == 1) {//手动选择

        } else if (locationFlag == 2) {//定位
            Logger.d("获得了经纬度转换地址的回调（定位获得的）城市为：" + addCom.city);
            mCityName = addCom.city;
            mProvinceName = addCom.province;
            mCityCode = findLocCityCodeByName(mProvinceName, mCityName);
            mProvinceCode = findLocProviceCodeByName(mProvinceName, mCityName);
            locationFlag = 100;

            moveMapTo(result.getLocation().latitude, result.getLocation().longitude, true, mScaleLevel[mCurrentRadiuPos]);

            setMapCenterInfo(result.getLocation(), mCircleRadiusM[mCurrentRadiuPos]);
            if (result.getAddressDetail() != null) {
                tv_at_create_find_customer_putlocation.setText(result.getAddress());
            }
        } else if (mFromAction == 3) {
            Logger.d("获得了经纬度转换地址的回调（来自地图平移）城市为：" + addCom.city);
            //来自地图平移
            mMoveAddress = result.getAddress().trim();
            mMoveCityName = addCom.city;
            mMoveProvinceName = addCom.province;
            if (mCityName != null && mMoveCityName != null && !mMoveCityName.equals(mCityName)) {
                wipedata();
            }
            mCityName = addCom.city;
            mProvinceName = addCom.province;
            mCityCode = findLocCityCodeByName(mProvinceName, mCityName);
            mProvinceCode = findLocProviceCodeByName(mProvinceName, mCityName);
            if (result.getAddressDetail() != null) {
                tv_at_create_find_customer_putlocation.setText(result.getAddress());
            }
            setMapCenterInfo(result.getLocation(), mCircleRadiusM[mCurrentRadiuPos]);
        } else if (mFromAction == 4) {
            Logger.d("获得了经纬度转换地址的回调（来自搜索）城市为：" + addCom.city);
            mCityName = addCom.city;
            mProvinceName = addCom.province;
            mCityCode = findLocCityCodeByName(mProvinceName, mCityName);
            mProvinceCode = findLocProviceCodeByName(mProvinceName, mCityName);
            moveMapTo(result.getLocation().latitude, result.getLocation().longitude, true);
            setMapCenterInfo(result.getLocation(), mCircleRadiusM[mCurrentRadiuPos]);
            if (result.getAddressDetail() != null) {
                tv_at_create_find_customer_putlocation.setText(addressInfo.getAddress());
            }
            isSearch = true;
        }

        mCurrentLatLng = result.getLocation();
        if (!TextUtils.isEmpty(mCityName)) {
            int status = findLocCityStatusByName(mProvinceName, mCityName);
            if (status != 1 && mAreaOuts != null && mAreaOuts.size() > 0) {
                ToastUtils.showShort(mCityName + "该城市没有开通业务！");
            }
        }
    }

    private void showErrorDialog() {
        final RemindDialog dialog = new RemindDialog(this, "", "抱歉," + mCityName + "未开通此业务~", R.mipmap.null_pass, "已了解");
        dialog.setClickListenerInterface(new RemindDialog.ClickListenerInterface() {
            @Override
            public void doCertificate() {
                dialog.dismiss();
                //stopLocation();
            }
        });
        dialog.setCancelable(true);
        dialog.show();
        showErrorDialog = false;
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            Logger.d("定位了" + location.getProvince() + location.getCity());
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                Logger.d("定位的Location为空....");
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
//            mFromAction = 2;//来自定位
            // TODO: 2018/4/17 加上关于定位的Flag
            locationFlag = 2;
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            mCurrentLatLng = ll;
            //开始坐标转地址
            mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(mCurrentLatLng)
                    .newVersion(0));

            if (isFirstLoc) {
                Logger.d("首次定位。。。");
                isFirstLoc = false;
                mCurrentScaleLevelPositon = 0;

            }
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
        mCityPickPresent.getTaskDeliveryInfo();
        mCityPickPresent.showRedPoint();
    }


    @Override
    protected void onStop() {
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        //GEO destroy
        mSearch.destroy();

        if (taskDeliveryView.getmCountDownTimer() != null) {
            taskDeliveryView.getmCountDownTimer().cancel();
        }
        if (taskDeliveryView.getCountDownTimer() != null) {
            taskDeliveryView.getCountDownTimer().cancel();
        }


    }


    @Override
    protected MainHomePresenter<IHomeMainView> createPresent() {
        return new MainHomePresenter<>(this);
    }


    @Override
    public void onStart() {
        super.onStart();
    }


    //通过城市名称和省名找城市代码
    private String findLocCityCodeByName(String provinceName, String cityName) {
        if (TextUtils.isEmpty(provinceName) || TextUtils.isEmpty(cityName)) {
            return "";
        }
        if (mAreaOuts != null && mAreaOuts.size() > 0) {
            for (AreaOut areaOut : mAreaOuts) {
                if (areaOut.getName().equals(provinceName) || provinceName.contains(areaOut.getName())) {
                    for (Area area : areaOut.getList()) {
                        if (area.getName().equals(cityName) || cityName.contains(area.getName())) {
                            Logger.d("市名称：" + area.getName() + "市Code值：" + area.getCode());
                            return area.getCode().trim();
                        }
                    }

                }
            }
        }
        return "";
    }


    //通过城市名称和省名获得省代码
    private String findLocProviceCodeByName(String provinceName, String cityName) {
        if (TextUtils.isEmpty(provinceName) || TextUtils.isEmpty(cityName)) {
            return "";
        }
        if (mAreaOuts != null && mAreaOuts.size() > 0) {
            for (AreaOut areaOut : mAreaOuts) {
                if (areaOut.getName().equals(provinceName) || provinceName.contains(areaOut.getName())) {
                    Logger.d("省名称：" + areaOut.getName() + "省Code值：" + areaOut.getCode());
                    return areaOut.getCode().trim();
                }
            }
        }
        return "";
    }


    //根据城市名和省名获取该城市是否开通业务。
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
        if (mAreaOuts != null && mAreaOuts.size() > 0) {
            for (AreaOut areaOut : mAreaOuts) {
                if (areaOut.getName().equals(mProvinceName)) {
                    for (Area area : areaOut.getList()) {
                        if (area.getName().equals(cityName)) {
                            if (area.getStatus() == 0) {
                                if (mIsShowDialog) {
                                    showErrorDialog();
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
    public void showLoading() {
        showDefaultLoading();
    }

    @Override
    public void hideLoading() {
        hideDefaultLoading();
    }


    /**
     * 机型适配
     *
     * @return 返回true表示非三星机型且Android 6.0以上
     */
    private boolean modelFiltering() {
        return !Build.MANUFACTURER.contains(Constants.SAMSUNG) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private long exitTime; //记录第一次点击的时间

    private void exit() {
        //显示在：发现Fragment
        long nowTime = System.currentTimeMillis();
        if ((nowTime - exitTime) <= 2000) {
            AppManager.getAppManager().AppExit(this);
        } else {
            ToastUtils.showShort("再按一次退出程序！");
            exitTime = nowTime;
        }
    }


}
