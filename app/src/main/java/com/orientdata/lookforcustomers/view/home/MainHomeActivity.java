package com.orientdata.lookforcustomers.view.home;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.baidu.mapapi.map.Marker;
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
import com.gyf.barlibrary.ImmersionBar;
import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.AddressSearchRecode;
import com.orientdata.lookforcustomers.bean.Area;
import com.orientdata.lookforcustomers.bean.AreaOut;
import com.orientdata.lookforcustomers.bean.NextStepCheckBean;
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
import com.orientdata.lookforcustomers.view.certification.fragment.ACache;
import com.orientdata.lookforcustomers.view.findcustomer.ICityPickView;
import com.orientdata.lookforcustomers.view.findcustomer.SearchActivity;
import com.orientdata.lookforcustomers.view.findcustomer.impl.MessageTaskActivity;
import com.orientdata.lookforcustomers.view.findcustomer.impl.PageTaskActivity;
import com.orientdata.lookforcustomers.widget.abslistview.CommonAdapter;
import com.orientdata.lookforcustomers.widget.abslistview.ViewHolder;
import com.orientdata.lookforcustomers.widget.dialog.ConfirmDialog;
import com.orientdata.lookforcustomers.widget.dialog.RemindDialog;
import com.orientdata.lookforcustomers.widget.dialog.SettingStringDialog;
import com.qiniu.android.common.Constants;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;
import vr.md.com.mdlibrary.utils.ImageUtils2;


/**
 * 创建寻客页面
 */
public class MainHomeActivity extends BaseActivity<ICityPickView, CityPickPresent<ICityPickView>> implements ICityPickView, SensorEventListener, OnGetGeoCoderResultListener {

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


    private MyLocationConfiguration.LocationMode mCurrentMode;

    private String mProvinceName = "";

    private String mLocCityName = "";
    private String mLocProvinceName = "";
    private String mLocCityCode = "";

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
    private ImageView imageView_suoxiao;
    private ImageView imageView_fangda;
    private ImageView imageView_jingzhundingwei;

    private LinearLayout ll_at_create_find_customer_search; //搜索栏
    private TextView tv_at_create_find_customer_location; //要选择的具体的城市


    private int locationFlag;

    float[] mScaleLevel = {15.0f, 14.0f, 13.0f, 12.66f, 12.0f, 11.0f};
    int[] mCircleRadius = {500, 1000, 2000, 3000, 5000, 10000};//米
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
    private Bitmap bm;
    private boolean isReCreate = false;//是否是寻客详情点进来的重新创建
    private TaskOut mTaskOut = null;//寻客详情的内容(上次常见的任务详情)
    private String mMapClippath;

    private String mStrType;

    private String industryMark;
    private String industryNameStr;

    //重要的变量
    private String mCityCode = "";  //创建任务的cityCode
    private String mThrowAddress;   //投放地址
    private LatLng mCurrentLatLng; //当前的位置
    private String mCityName = ""; //城市名称
    private String mProvinceCode;  //省code
    private TextView tv_at_create_find_customer_putlocation;//最上面显示的投放位置


    private List<TextView> rudioss = new ArrayList<>();
    private OverlayOptions oCircle;
    private String path;
    private int mCurrentRadiuPos;
    private CommonAdapter<String> strngCommonAdapter;
    private Intent intent;
    private SlidingUpPanelLayout mLayout;


    protected boolean isImmersionBarEnabled() {
        return false;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCityPickPresent = new CityPickPresent(this);
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
                .fullScreen(true)
                .init();

//        GlideUtil.getInstance().loadAdImage(this,mIcon,imagerUrls.get(position),true);


        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setAnchorPoint(0.7f);
        mLayout.setPanelState(PanelState.COLLAPSED);

        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
                if (newState==PanelState.COLLAPSED) {  //折叠

                }else if(newState == PanelState.ANCHORED){ //展开

                }
            }
        });


        //点击外部折叠
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //折叠
                mLayout.setPanelState(PanelState.COLLAPSED);
            }
        });


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
        getProvinceCity();
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
                mCurrentLatLng = latLng;
                moveMapTo(latLng.latitude, latLng.longitude, true);
                setMapCenterInfo(latLng, mCircleRadius[0]);
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
                mCurrentLatLng = mapStatus.target;
                setMapCenterInfo(mapStatus.target, mCircleRadius[0]);
                //开始坐标转地址
                mFromAction = 3;//来自地图移动
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(mapStatus.target)
                        .newVersion(0));
                mIsShowDialog = false;
                Logger.d("地图移动结束");
            }
        });
    }

    /**
     * 初始化view，初始化内容
     */
    private void initView() {
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
                // 刷新listview
                strngCommonAdapter.notifyDataSetChanged();
                setMapCenterInfo(mCurrentLatLng, mCircleRadius[position]);
            }
        });

    }




    //设置地图中心点的信息
    private void setMapCenterInfo(LatLng latLng, int radius) {
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
                .radius(radius);

        //绘制中心图标
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.icon_center);
        MarkerOptions options = new MarkerOptions();
        options.position(mCurrentLatLng)        // 位置
                .icon(icon)            // 图标
                .anchor(0.5f, 0.5f);//设置 marker 覆盖物的锚点比例，默认（0.5f, 1.0f）水平居中，垂直下对齐

        // 掉下动画
        //options.animateType(MarkerOptions.MarkerAnimateType.drop);
        mBaiduMap.addOverlay(oCircle);
        mBaiduMap.addOverlay(options);

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
                    AddressSearchRecode addressInfo = (AddressSearchRecode) data.getSerializableExtra("searchValue");
                    mCurrentLatLng = new LatLng(addressInfo.getLatitude(), addressInfo.getLongitude());
                    moveMapTo(addressInfo.getLatitude(), addressInfo.getLongitude(), true);
                    setMapCenterInfo(mCurrentLatLng, mCircleRadius[0]);
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

    private String smsPrice = "";//短信单价
    private String pagePrice = "";//页面单价




    @OnClick({R.id.imageView_jingzhundingwei,R.id.iv_task, R.id.bt_go_orintion, R.id.iv_service,
            R.id.image2, R.id.image3,R.id.image1,
            R.id.ll_at_create_find_customer_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image1:
                Logger.d("image1");
                break;
            case R.id.image2:
                Logger.d("image2");
                break;
            case R.id.image3:
                Logger.d("image3");
                break;
            case R.id.iv_task:
//                mCityPickPresent.AddAddressInfo("123","123","123");
                break;
            case R.id.imageView_jingzhundingwei:
                isFirstLoc = true;
                mIsShowDialog = true;
                LbsManager.getInstance().getLocation(myListener);
                break;
            case R.id.bt_go_orintion: //打开定向设置
                intent = new Intent(MainHomeActivity.this, DirectionalSettingActivity3.class);
                intent.putExtra(Constants.latitude, mCurrentLatLng.latitude+"");
                intent.putExtra(Constants.longitude, mCurrentLatLng.longitude+"");
                intent.putExtra("address", tv_at_create_find_customer_putlocation.getText().toString().trim());

                if (CommonUtils.haveSDCard()) {
                    mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
                        @Override
                        public void onSnapshotReady(Bitmap bitmap) {
                            String baiduMapPath = ImageUtils2.saveBitmap(MainHomeActivity.this, bitmap);
                            intent.putExtra("path", baiduMapPath);
                            Logger.d("百度地图截图路径为：" + baiduMapPath);
                            startActivity(intent);
                        }
                    });
                } else {
                    ToastUtils.showShort("没有SD卡!");
                }
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
                intent = new Intent(this, SearchActivity.class);
                startActivityForResult(intent, 2);
                break;
        }
    }




    /**
     * 下一步验证
     */
    private void toNext() {
        //投放地址
        mThrowAddress = tv_at_create_find_customer_putlocation.getText().toString().trim();
        int type = -666;
        //覆盖范围
        String rangeRadius = mCircleRadiusM[mCurrentScaleLevelPositon];

        String longitude = String.valueOf(mCurrentLatLng.longitude);

        if (TextUtils.isEmpty(longitude)) {
            ToastUtils.showShort("定位失败，请选择业务城市");
            return;
        }
        BigDecimal b_longitude = BigDecimal.valueOf(Double.valueOf(longitude));
        String latitude = String.valueOf(mCurrentLatLng.latitude);

        if (TextUtils.isEmpty(latitude)) {
            ToastUtils.showShort("定位失败，请选择业务城市");
            return;
        }
        if (TextUtils.isEmpty(mCityCode)) {
            ToastUtils.showShort("定位失败，请选择业务城市");
            return;
        }
        BigDecimal b_latitude = BigDecimal.valueOf(Double.valueOf(latitude));
        if (TextUtils.isEmpty(ageF)
                || TextUtils.isEmpty(ageB)
                || TextUtils.isEmpty(sex)) {
            ToastUtils.showShort("请设置定向设置");
            return;
        } else {
            MDBasicRequestMap map = new MDBasicRequestMap();
            map.put("userId", UserDataManeger.getInstance().getUserId());
            // TODO: 2018/4/4 去掉了预算参数
            map.put("longitude", b_longitude + "");
            map.put("latitude", b_latitude + "");
            map.put("cityCode", mCityCode);

            //通过验证到下一步页面
            final Intent intent = new Intent();
            if (type == 1) { //创建短信任务
                intent.setClass(MainHomeActivity.this, MessageTaskActivity.class);
            } else if (type == 2) { //创建页面任务
                intent.setClass(MainHomeActivity.this, PageTaskActivity.class);
            }

            intent.putExtra("ageF", ageF);
            intent.putExtra("ageB", ageB);
            intent.putExtra("sex", sex);
            intent.putExtra("interestIds", interestIds);
            intent.putExtra("cityCode", mCityCode);
            intent.putExtra("throwAddress", mThrowAddress);
            intent.putExtra("type", type);
//            intent.putExtra("taskName", taskName);
            intent.putExtra("rangeRadius", rangeRadius);
            intent.putExtra("longitude", longitude);
            intent.putExtra("dimension", latitude);
            intent.putExtra("address", mThrowAddress);
            intent.putExtra("cityName", mCityName);
            intent.putExtra("pagePrice", pagePrice);
            intent.putExtra("smsPrice", smsPrice);
            intent.putExtra("mProvinceCode", mProvinceCode);
            intent.putExtra("industryMark", industryMark);
            intent.putExtra("industryNameStr", industryNameStr);
            intent.putExtra("isReCreate", isReCreate);


            final int type2 = type;

            //下一步验证
            showDefaultLoading();
            // TODO: 2018/4/4 接口改了，参数少传了一个
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
                        intent.putExtra("day", preOut.getDay());//限制天数
                        intent.putExtra("minMoney", preOut.getMinMoney());//限制钱数

                        if (type2 == 1) {//短信
                            startActivity(intent);
                        } else if (type2 == 2) {//页面
                            //截图   // 截图，在SnapshotReadyCallback中保存图片到 sd 卡
                            mMapClippath = Environment.getExternalStorageDirectory().getPath() + "/ClipPhoto/cache/";
                            File dir = new File(mMapClippath);
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }
                            mMapClippath = mMapClippath + "map.png";
                            if (CommonUtils.haveSDCard()) {
                                mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
                                    @Override
                                    public void onSnapshotReady(Bitmap bitmap) {
                                        String baiduMapPath = ImageUtils2.saveBitmap(MainHomeActivity.this, bitmap);
                                        intent.putExtra("mapPath", baiduMapPath);
                                        Logger.d("百度地图截图路径为：" + baiduMapPath);
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                ToastUtils.showShort("没有SD卡!");
                            }
                        }

                    }

                }
            }, map);
        }
    }


    /**
     * 选择范围半径
     *
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
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(mCurrentLatLng).zoom(mScaleLevel[mCurrentScaleLevelPositon]);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }
            });
            dialog.setUpData(listString);
            dialog.setSelect(mCurrentScaleLevelPositon);
            dialog.show();
        }
    }


    private void showDialog() {
        final ConfirmDialog dialog = new ConfirmDialog(this, "您的地域有变动，请重新设置任务", "确定", "确定");
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
//                tv_at_create_find_customer_location.setText(mCityName);
            }

            @Override
            public void doConfirm() {
                dialog.dismiss();
            }
        });
    }

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
    public void addAddress(Object object) {

    }


    //地址转坐标的结果(手动选择城市，点击详情，搜索)
    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MainHomeActivity.this,
                    "抱歉，未能找到结果",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        mCurrentLatLng = new LatLng(result.getLocation().latitude, result.getLocation().longitude);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(mCurrentLatLng)
                .zoom(mScaleLevel[mCurrentScaleLevelPositon]);
        mBaiduMap.clear();
        final Marker marker = (Marker) mBaiduMap.addOverlay(new MarkerOptions()
                .position(mCurrentLatLng).alpha(0.0f)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.bg_ac_create_find_customer_location)));
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

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
                //开始坐标转地址
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(mapStatus.target)
                        .newVersion(0));
            }
        });
        mFromAction = 4;//来自搜索
        mIsShowDialog = true;

        //开始坐标转地址
        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(mCurrentLatLng)
                .newVersion(0));
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
            tv_at_create_find_customer_putlocation.setText(address);
            mCityName = addCom.city;
            mProvinceName = addCom.province;
            mCityCode = findLocCityCodeByName(mProvinceName, mCityName);
            mProvinceCode = findLocProviceCodeByName(mProvinceName, mCityName);
            locationFlag = 100;
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
        } else if (mFromAction == 4) {
            Logger.d("获得了经纬度转换地址的回调（来自搜索）城市为：" + addCom.city);
            mCityName = addCom.city;
            mProvinceName = addCom.province;
            mCityCode = findLocCityCodeByName(mProvinceName, mCityName);
            mProvinceCode = findLocProviceCodeByName(mProvinceName, mCityName);
            tv_at_create_find_customer_putlocation.setText(address);
        }
        mCurrentLatLng = result.getLocation();
        if (!TextUtils.isEmpty(mCityName)) {
            Logger.d("最终获得的mCityName是:" + mCityName);
//            tv_at_create_find_customer_location.setText(mCityName.trim().toCharArray(), 0, 3);
            requestCityStatus(mCityName);
        }
        //手动拖动地图后的地址
        if (result.getAddressDetail() != null) {
            tv_at_create_find_customer_putlocation.setText(result.getAddress());
        }
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
                moveMapTo(ll.latitude, ll.longitude, true, mScaleLevel[mCurrentScaleLevelPositon]);
                setMapCenterInfo(mCurrentLatLng, mCircleRadius[0]);

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
    }



    @Override
    protected CityPickPresent<ICityPickView> createPresent() {
        return new CityPickPresent<>(this);
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
                        if (area.getName().equals(mCityName)) {
                            if (area.getStatus() == 0) {
                                if (mIsShowDialog) {
                                    final RemindDialog dialog = new RemindDialog(this, "", "抱歉," + cityName + "未开通此业务~", R.mipmap.null_pass, "已了解");
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
        ACache.get(this).remove(SharedPreferencesTool.DIRECTION_HISTORY);
//        SharedPreferencesTool.getInstance().remove(SharedPreferencesTool.MessageTaskCacheBean);
        //展开 ，固定
        if (mLayout != null &&
                (mLayout.getPanelState() == PanelState.EXPANDED || mLayout.getPanelState() == PanelState.ANCHORED)) {
            mLayout.setPanelState(PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

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

}
