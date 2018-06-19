package com.orientdata.lookforcustomers.view.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gyf.barlibrary.ImmersionBar;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.AddressCollectInfo;
import com.orientdata.lookforcustomers.bean.InterestCategory;
import com.orientdata.lookforcustomers.bean.InterestTagImportOut;
import com.orientdata.lookforcustomers.bean.OrientationSettingsOut;
import com.orientdata.lookforcustomers.bean.SettingOut;
import com.orientdata.lookforcustomers.presenter.DirectionalSettingPresent;
import com.orientdata.lookforcustomers.util.BitmapUtil;
import com.orientdata.lookforcustomers.util.CommonUtils;
import com.orientdata.lookforcustomers.util.SharedPreferencesTool;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.certification.fragment.ACache;
import com.orientdata.lookforcustomers.view.findcustomer.IDirectionalSettingView;
import com.orientdata.lookforcustomers.widget.FluidLayout;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.orientdata.lookforcustomers.widget.dialog.BusinessInterestDialog;
import com.orientdata.lookforcustomers.widget.dialog.ConfirmDialog;
import com.orientdata.lookforcustomers.widget.dialog.HobbyMultipleSelectDialog;
import com.qiniu.android.common.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vr.md.com.mdlibrary.UserDataManeger;

import static com.orientdata.lookforcustomers.R.id.iv_xunke_now;


/**
 * Created by wy on 2017/11/27.
 * 新的定向设置Activity
 */
public class DirectionalSettingActivity3 extends BaseActivity<IDirectionalSettingView, DirectionalSettingPresent<IDirectionalSettingView>> implements IDirectionalSettingView, View.OnClickListener {
    @BindView(R.id.tv_3c)
    TextView tv3c;
    @BindView(R.id.tv_baihe)
    TextView tvBaihe;
    @BindView(R.id.tv_muying)
    TextView tvMuying;
    @BindView(R.id.tv_hair)
    TextView tvHair;
    @BindView(R.id.tv_flower)
    TextView tvFlower;
    @BindView(R.id.zidingyi)
    TextView zidingyi;

    @BindView(R.id.tv_no_limit)
    TextView nolimit;

    @BindView(R.id.tv_car)
    TextView tvCar;

    @BindView(R.id.directionHobbyInfo)
    FluidLayout directionHobbyInfo; //用户兴趣爱好，自定义标签

    @BindView(R.id.tv_canyin)
    TextView tvCanyin;
    @BindView(R.id.ll_user_hobby)
    LinearLayout llUserHobby;
    @BindView(R.id.bmapView)
    ImageView ivBaiMapPic;
    @BindView(R.id.top_view)
    View topView;
    @BindView(R.id.iv_kefu)
    ImageView ivKefu;

    @BindView(R.id.collect_address)
    ImageView collectAddress;

    @BindView(iv_xunke_now)
    ImageView ivXunkeNow;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_scope)
    TextView tvScope;
    @BindView(R.id.rv_sex)
    RecyclerView rvSex;
    @BindView(R.id.rv_age)
    RecyclerView rvAge;

    @BindView(R.id.age_from)
    TextView ageFrom;
    @BindView(R.id.age_to)
    TextView ageTo;
    @BindView(R.id.ll_age)
    LinearLayout agell;

    @BindView(R.id.seekbar)
    RangeSeekBar seekbar;
    @BindView(R.id.tvLeftText)
    TextView tvLeftText;


    @BindView(R.id.imageView4)
    ImageView imageView4;

    @BindView(R.id.bt_go_orintion)
    ScrollView scrollView;
    @BindView(R.id.tv_person_num)
    TextView tvPersonNum;


    private MyTitle titleDirectional;
    private SettingOut settingOuts = null; //后台返回的数据
    private RelativeLayout age_from;
    private RelativeLayout age_to;//年龄

    private RelativeLayout hobby_from;
    private RelativeLayout hobby_to;//商业兴趣


    boolean isFirstCheckModel = true;
    private TextView tv_mModelOne;//机型 一级
    private FluidLayout directionBaseInfo;


    private TextView tv_mHobbyOne;//商业兴趣 一级
    private ImageView tvSave;//保存按钮
    private TextView tv_mAgeFrom;
    private TextView tv_mAgeTo;
    private TextView tv_mSex;

    private TextView tv_mHobbyTo;//商业兴趣 二级
    private String cityCode = "";
    private ACache aCache = null;//数据缓存

    int hobbyFromPosition = -1; // 代表选择的兴趣名称的位置
    private List<String> hobbyList = new ArrayList<>(); //兴趣点数组集合
    private Map<String, List<String>> hobbyMap = null;//存储兴趣Map(key：兴趣名称，value:二级兴趣点)

    private OrientationSettingsOut orientationSettingsOut = null;//数据缓存，之前的任务的数据
    private boolean isReCreate = false;//是否是再次创建寻客
    private List<String> agesFrom = new ArrayList<>();
    private List<String> agesTo = new ArrayList<>();
    private List<String> sexs = new ArrayList<>();

    private List<TextView> industrys = new ArrayList<>();
    private String industryMark = ""; //是否自定义行业

    private String industryNameStr = ""; //选择的行业
    private String[] industrysStr = {"餐饮", "3C数码", "汽车用品", "母婴用品", "美容美发", "鲜花礼品", "汽车用品", "自定义","不限"};
    private String[] sexsSelects = {"不限", "男", "女"};
    private String[] agesSelects = {"不限", "自定义"};

    private int sexPosition = 0;

    private String industryName;
    private AddressCollectInfo addressCollectInfo;
    private String latitude;
    private String longitude;
    private String address;

    private String ageF = "不限"; //最后的起始年龄
    private String ageB = "不限";  //最后的终止年龄
    private String sex = "不限";   //性别
    private String industryStr = "不限"; //行业
    private  int labelNum = 1;  //标签的数量
    private String mCityName;
    private String currentCircleRadius;
    private String personNumStr;
    private boolean mIsLike = false;

    protected boolean isImmersionBarEnabled() {
        return false;
    }

    @OnClick({R.id.collect_address,R.id.iv_back,R.id.tv_quess,R.id.tv_no_limit,R.id.iv_kefu,
            R.id.hobby_from, R.id.hobby_to, R.id.tv_3c, R.id.tv_baihe, R.id.tv_canyin, R.id.tv_muying, R.id.tv_hair, R.id.tv_flower, R.id.zidingyi, R.id.tv_car})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_quess:
                showDialog();
                break;
            case R.id.iv_kefu:
                final String url = "mqqwpa://im/chat?chat_type=wpa&uin=2280249239";
                if (CommonUtils.checkApkExist(this, "com.tencent.mobileqq")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } else {
                    ToastUtils.showShort("本机未安装QQ应用,请下载安装。");
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.collect_address: //收藏地址
                if (mIsLike) {
                    //取消收藏
                    mPresent.appAddressDelete(addressCollectInfo.getId());
                }else{
                    //收藏
                    mPresent.AddAddressInfo(address, longitude.substring(0,longitude.indexOf(".")+5), latitude.substring(0,latitude.indexOf(".")+5));
                }
                break;
            case R.id.tv_canyin:
                setTextState(0, true);
                break;
            case R.id.tv_3c:
                setTextState(1, true);
                break;
            case R.id.tv_baihe:  //日百
                setTextState(2, true);
                break;
            case R.id.tv_muying: //母婴
                setTextState(3, true);
                break;
            case R.id.tv_hair:  //美容美发
                setTextState(4, true);
                break;
            case R.id.tv_flower://鲜花礼品
                setTextState(5, true);
                break;
            case R.id.tv_car:  //汽车
                setTextState(6, true);
                break;
            case R.id.zidingyi://自定义
                setTextState(7, true);
                llUserHobby.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_no_limit://不限
                setTextState(8, true);
                break;
        }
        if (!industryStr.equals("自定义")) {
            personNumStr = CommonUtils.getPersonNum(currentCircleRadius, mCityName, ageF, ageB, sex, industryStr, labelNum);
            tvPersonNum.setText(personNumStr);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directional_setting_new);
        ButterKnife.bind(this);
        initView();
        initData();

        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .statusBarView(R.id.top_view)
                .fullScreen(true)
                .init();
//        EasyTransition.enter(this);
    }

    private void initData() {
        mPresent.getSelectSetting(cityCode);
        mPresent.getAllAddress();
    }


    //获取后台返回的内容
    @Override
    public void getSelectSetting(SettingOut settingOuts) {
        if (settingOuts != null) {
            this.settingOuts = settingOuts;
        }
        if (settingOuts != null) {
            getData();
        }
    }

    @Override
    public void getAllCollectionAddress(List<AddressCollectInfo> addressCollectInfo) {
        if (addressCollectInfo == null || addressCollectInfo.size() == 0) {
            return;
        }

        for (AddressCollectInfo address : addressCollectInfo){
            if (address.getLatitude().equals(latitude.substring(0,latitude.indexOf(".")+5))
                    && address.getLongitude().equals(longitude.substring(0,longitude.indexOf(".")+5))) {
                this.addressCollectInfo = address;
                collectAddress.setImageResource(R.mipmap.icon_collect_dir);
                mIsLike = true;
                return;
            }else{
                mIsLike = false;
            }

        }
    }


    @Override
    public void AddAddressSucess() {
        collectAddress.setImageResource(R.mipmap.icon_collect_dir);
        ToastUtils.showShort("收藏成功");
    }


    @Override
    public void AddAddressError() {
        collectAddress.setImageResource(R.mipmap.icon_collect_no);
        ToastUtils.showShort("收藏失败");
    }


    private void showDialog() {
        final ConfirmDialog dialog = new ConfirmDialog(this, getString(R.string.dir_quess),"我已了解");
        dialog.show();
        dialog.setClickListenerInterface(new ConfirmDialog.ClickListenerInterface() {
            @Override
            public void doCancel() {
                dialog.dismiss();
            }
            @Override
            public void doConfirm() {

                dialog.dismiss();
            }
        });
        dialog.setCancelVisibility(View.GONE);
    }

    private void initView() {

        cityCode = getIntent().getStringExtra("cityCode");
        industrys.add(tvCanyin);
        industrys.add(tv3c);
        industrys.add(tvBaihe);
        industrys.add(tvMuying);
        industrys.add(tvHair);
        industrys.add(tvFlower);
        industrys.add(tvCar);
        industrys.add(zidingyi);
        industrys.add(nolimit);
        setTextState(8, true);

        String[] stringArrayFrom = getResources().getStringArray(R.array.ages_from);
        agesFrom = Arrays.asList(stringArrayFrom);//年龄集合

        String[] stringArrayTo = getResources().getStringArray(R.array.ages_from);
        agesTo = Arrays.asList(stringArrayTo);//年龄集合

        String[] stringArraySex = getResources().getStringArray(R.array.sex);
        sexs = Arrays.asList(stringArraySex);//性别集合


        hobby_from = (RelativeLayout) findViewById(R.id.hobby_from);
        hobby_to = (RelativeLayout) findViewById(R.id.hobby_to);

        directionHobbyInfo = (FluidLayout) findViewById(R.id.directionHobbyInfo);


        tv_mHobbyOne = hobby_from.findViewById(R.id.tvLeftText);
        tv_mHobbyTo = hobby_to.findViewById(R.id.tvLeftText);

        ivXunkeNow.setOnClickListener(this);
        hobby_from.setOnClickListener(this);
        hobby_to.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvAge.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvSex.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        final Adapter adaptersex = new Adapter(Arrays.asList(sexsSelects),1);
        rvSex.setAdapter(adaptersex);

        final Adapter adapterAge = new Adapter(Arrays.asList(agesSelects),0);

        rvAge.setAdapter(adapterAge);




        adaptersex.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                sexPosition = position;
                sex = sexsSelects[position];
                personNumStr = CommonUtils.getPersonNum(currentCircleRadius, mCityName, ageF, ageB, sex, industryStr, labelNum);
                tvPersonNum.setText(personNumStr);

                adaptersex.notifyDataSetChanged();
            }
        });

        adapterAge.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                sexPosition = position;
                if (position == 0) {    //不限
//                    ageFrom.setText("不限");
//                    ageTo.setText("不限");
                    ageF = "不限";
                    ageB = "不限";
                    seekbar.setVisibility(View.GONE);
                    agell.setVisibility(View.GONE);
                } else {              //自定义
                    ageF = ageFrom.getText().toString();
                    ageB = ageTo.getText().toString();
                    seekbar.setVisibility(View.VISIBLE);
                    agell.setVisibility(View.VISIBLE);
                }
                personNumStr = CommonUtils.getPersonNum(currentCircleRadius, mCityName, ageF, ageB, sex, industryStr, labelNum);
                tvPersonNum.setText(personNumStr);
                adapterAge.notifyDataSetChanged();
            }
        });


        seekbar.setValue(15, 70);
        seekbar.setOnRangeChangedListener(new OnRangeChangedListener() {

            private float rightValue;
            private float leftValue;

            @Override
            public void onRangeChanged(RangeSeekBar view, float min, float max, boolean isFromUser) {
                //min is left seekbar value, max is right seekbar value
                leftValue = min;
                rightValue = max;
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

                Logger.d("左边显示的值：" + leftValue);
                Logger.d("右边显示的值：" + rightValue);

                String leftStr = leftValue + "";
                String lrightValueStr = rightValue + "";

                ageFrom.setText(leftStr.substring(0, 2));
                ageTo.setText(lrightValueStr.substring(0, 2));

                if (leftStr.substring(0, 2).equals("15")) {
                    ageFrom.setText("18及以下");
                }
                if (lrightValueStr.substring(0, 2).equals("70")) {
                    ageTo.setText("65及以上");
                }
                ageF = ageFrom.getText().toString();
                ageB = ageTo.getText().toString();
                personNumStr = CommonUtils.getPersonNum(currentCircleRadius, mCityName, ageF, ageB, sex, industryStr, labelNum);
                tvPersonNum.setText(personNumStr);
            }
        });


        aCache = ACache.get(this);
        Intent intent = getIntent();
        if (intent != null) {
            //上个页面返回cityCode和isReCreate
            isReCreate = intent.getBooleanExtra("isReCreate", false);
            cityCode = intent.getStringExtra("cityCode");
            String path = intent.getStringExtra("path");
            latitude = intent.getStringExtra(Constants.latitude);
            longitude = intent.getStringExtra(Constants.longitude);
            address = intent.getStringExtra("address");
            //半径
            currentCircleRadius = intent.getStringExtra(Constants.CurrentCircleRadius);
            mCityName = intent.getStringExtra(Constants.mCityName);
            address = intent.getStringExtra("address");
            byte[] bitmaps = intent.getByteArrayExtra("bitmap");
            tvAddress.setText(address);
            tvScope.setText(currentCircleRadius);
            ivBaiMapPic.setImageBitmap(BitmapUtil.Bytes2Bitmap(bitmaps));
        }

        if (hobbyMap == null) {
            hobbyMap = new HashMap<>();
        }

    }


    public class Adapter extends BaseQuickAdapter<String, BaseViewHolder> {
        private int type;

        public Adapter(@Nullable List<String> data,int type) {
            super(R.layout.item_categroy_select, data);
            this.type = type;
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            int layoutPosition = helper.getLayoutPosition();
            helper.setText(R.id.tv_title, item);
            if (sexPosition == layoutPosition) {
                helper.setTextColor(R.id.tv_title, getResources().getColor(R.color.white));
                helper.setBackgroundRes(R.id.rl_title, R.drawable.btn_selec);
            } else if(type == 0){
                helper.setTextColor(R.id.tv_title, getResources().getColor(R.color.text_gray_new));
                helper.setBackgroundRes(R.id.rl_title, R.drawable.btn_no_selec_border);
            } else {
                helper.setTextColor(R.id.tv_title, getResources().getColor(R.color.gray_small));
                helper.setBackgroundRes(R.id.rl_title, R.drawable.btn_no_selec);
            }
        }
    }



    /**
     * 获取定向设置 当前用户的缓存数据（缓存中的数据）
     */
    private void getData() {
        if (isReCreate) {
            Logger.d("再次创建任务，用的是之前任务的数据");
            orientationSettingsOut = (OrientationSettingsOut) getIntent().getSerializableExtra("orientationSettingsOut");
            List<String> xingquStr = orientationSettingsOut.getXingqu();
            industryName = getIntent().getStringExtra("industryName");//行业名称
            industryMark = getIntent().getStringExtra("customFlag");//是否自定义
//            updateData();
            setHobby(orientationSettingsOut);
        } else {  //不是点击详情进来，是缓存的数据
            ArrayList<OrientationSettingsOut> list = (ArrayList<OrientationSettingsOut>) aCache.getAsObject(SharedPreferencesTool.DIRECTION_HISTORY);
            if (list != null) {
                Logger.d("读取本地缓存的内容" + list.size());
                for (OrientationSettingsOut direction : list) {
                    if (UserDataManeger.getInstance().getUserId().equals(direction.getUserId() + "")) {
                        orientationSettingsOut = direction; //使用本地中的数据
                        break;
                    }
                }
                industryName = orientationSettingsOut.getIndustryName();//行业名称
                industryMark = orientationSettingsOut.getIndustryMark();//是否自定义

            } else {
                Logger.d("没有任何数据，重新进行输入数据");
            }
            if (settingOuts != null) {
//                updateData();
//                setList(orientationSettingsOut);//
                if (orientationSettingsOut != null) {

                    setHobby(orientationSettingsOut);
                }
            }
        }
    }


    //第一步：更新界面
    private void updateData() {
        boolean visibility = settingOuts.isVisibility();
        if (visibility) { //true
            zidingyi.setVisibility(View.INVISIBLE);
        } else {
            zidingyi.setVisibility(View.VISIBLE);
        }
        if (orientationSettingsOut == null) { //如果没有缓存数据，会拿到后台返回的数据进行更新界面
            Logger.d("没有缓存的数据。。");
            //定向设置 无缓存信息
            hobbyFromPosition = -1;
        } else { //有数据需要显示
            Logger.d("orientationSettingsOut不为空，获取到了缓存的数据。。");
            if (isReCreate) {  //上个任务的任务的详情
                setMapData(); //再次创建任务保留的数据
            }
            //获取年龄性别
            String ageFCache = orientationSettingsOut.getAgeF();
            String ageBCache = orientationSettingsOut.getAgeB();
            String sexCache = orientationSettingsOut.getSex();
            // TODO: 2018/6/7  设置年龄和性别

            if (industryMark.equals("0")) { //行业
                switch (industryName) {
                    case "餐饮":
                        industrys.get(0).setSelected(true);
                        break;
                    case "3C数码":
                        industrys.get(1).setSelected(true);
                        break;
                    case "日用百货":
                        industrys.get(2).setSelected(true);
                        break;
                    case "母婴用品":
                        industrys.get(3).setSelected(true);
                        break;
                    case "美容美发":
                        industrys.get(4).setSelected(true);
                        break;
                    case "鲜花礼品":
                        industrys.get(5).setSelected(true);
                        break;
                    case "汽车用品":
                        industrys.get(6).setSelected(true);
                        break;
                }
            } else { //自定义，显示下面得到兴趣标签
                industrys.get(7).setSelected(true);
                llUserHobby.setVisibility(View.VISIBLE);
            }

        }
    }


    //设置兴趣爱好 ，设置缓存数据
    public void setList(OrientationSettingsOut orientationSettingsOut) {
        Logger.d("setList-orientationSettingsOut:" + orientationSettingsOut);
        List<String> listHobby = orientationSettingsOut.getXingqu();
        if (listHobby != null) {
            listHobby.clear();
        }
        //如果之前缓存中有兴趣爱好，就保存。
        if (orientationSettingsOut.getHobbyMap() != null) {
            for (Map.Entry<String, List<String>> map : orientationSettingsOut.getHobbyMap().entrySet()) {
                String key = map.getKey();
                List<String> listIntrest = map.getValue();
                listHobby.addAll(listIntrest);
            }

        }
        setHobby(orientationSettingsOut);
    }

    /**
     * 设置兴趣的数据 并更新界面 （设置缓存数据）
     */
    private void setHobby(OrientationSettingsOut orientationSettingsOut) {
        Logger.d("setHobby-orientationSettingsOut:" + orientationSettingsOut);
        if (hobbyMap == null) {
            hobbyMap = new HashMap<>();
        }
        if (orientationSettingsOut.getHobbyMap() != null) {
            hobbyMap.putAll(orientationSettingsOut.getHobbyMap());
        }
        if (hobbyList == null) {
            hobbyList = new ArrayList<>();
        }

        //如果设置了兴趣
        if (orientationSettingsOut.getXingqu() != null) {  //设置数据缓存中的兴趣
            hobbyList.addAll(orientationSettingsOut.getXingqu());
            //更新settingOuts.getXingqu
            for (Map.Entry<String, List<String>> map : hobbyMap.entrySet()) {
                String key = map.getKey();
                tv_mHobbyOne.setText(key); //设置用户偏好起始
                List<String> listStr = map.getValue();
                for (int i = 0; i < settingOuts.getIco().size(); i++) {
                    if (settingOuts.getIco().get(i).getIndustryName().equals(key)) {
                        hobbyFromPosition = i;
                        settingOuts.getIco().get(i).setChecked(true);
                        List<InterestCategory> listCategory = settingOuts.getIco().get(i).getEri();
                        if (listCategory != null) {
                            for (int j = 0; j < listCategory.size(); j++) {
                                for (String str : listStr) {
                                    if (str.equals(listCategory.get(j).getIndustryName())) {
                                        listCategory.get(j).setChecked(true);
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
            addHobbyToFrame(hobbyList);//框中内容
        }

    }


    /**
     * 设置orientationSettingsOut对象中商业兴趣hobbyMap 的数据（设置缓存数据）
     */
    private void setMapData() {
        Map<String, List<String>> hobbyMap = new HashMap<>();
        List<InterestTagImportOut> interestTagImportOuts = settingOuts.getIco();
        for (int i = 0; i < interestTagImportOuts.size(); i++) {
            InterestTagImportOut interestTagImportOut = interestTagImportOuts.get(i);

            String name = interestTagImportOut.getIndustryName();//返回兴趣的名称

            for (String str : orientationSettingsOut.getXingqu()) {  //从提交的兴趣名称数组里面查询
                if (name.equals(str)) { //如果找到了一样的名称
                    //查找 相应的List
                    List<String> listIntrest = new ArrayList<>();
                    List<InterestCategory> erps = interestTagImportOut.getEri();
                    if (erps != null) {
                        for (int j = 0; j < erps.size(); j++) {
                            String nameIntrest = erps.get(j).getIndustryName();
                            for (String str1 : orientationSettingsOut.getXingqu()) {
                                if (nameIntrest.equals(str1)) {
                                    listIntrest.add(str1);
                                }
                            }
                            if (j == erps.size() - 1) {
                                hobbyMap.put(name, listIntrest);
                            }
                        }

                    }
                }
            }
        }
        orientationSettingsOut.setHobbyMap(hobbyMap);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hobby_from://兴趣爱好
                if (settingOuts.getIco() == null) {
                    ToastUtils.showShort("请检查网络连接");
                    return;
                }
                showHobbyFromDialog(settingOuts.getIco(), tv_mHobbyOne);
                break;
            case R.id.hobby_to: // 兴趣爱好
                if (settingOuts.getIco() == null || hobbyFromPosition == -1 || hobbyFromPosition > settingOuts.getIco().size() - 1) {
                    return;
                }
                showHobbyToDialog(settingOuts.getIco().get(hobbyFromPosition).getEri(), tv_mHobbyTo);
                break;
            case iv_xunke_now: //保存数据

                // TODO: 2018/6/7 性别，年龄
                if (TextUtils.isEmpty(ageF)&&TextUtils.isEmpty(ageB)) {
                    ageF = ageFrom.getText().toString();
                    ageB = ageTo.getText().toString();
                }

//                if (TextUtils.isEmpty(ageF) || TextUtils.isEmpty(ageB)) {
//                    ToastUtils.showShort("请选择用户年龄");
//                    return;
//                }

                String interestIds = "";//兴趣名称数组,以逗号隔开的 如 "教育,学前教育,东磁教育"
                for (Map.Entry<String, List<String>> map : hobbyMap.entrySet()) {
                    String key = map.getKey(); //查到的兴趣名称
                    List<String> listStr = map.getValue();//查到的兴趣二级标签
                    // TODO: 2018/4/18 这里不添加兴趣名称
//                    interestIds += key + ",";
                    for (String str : listStr) {
                        interestIds += str + ",";
                    }
                }

                if (!TextUtils.isEmpty(interestIds)) {
                    //去掉最后一个逗号
                    interestIds = interestIds.substring(0, interestIds.length() - 1);
                }

                // TODO: 2018/4/20 校验行业是否为空
                if (!getTextState()) {
                    ToastUtils.showShort("请选择行业");
                    return;
                }

                //检查商业兴趣是否为空
                if (checkEmpty(interestIds)) { //保存缓存数据
                    //缓存数据
                    orientationSettingsOut = new OrientationSettingsOut();
                    orientationSettingsOut.setAgeF(ageF);
                    orientationSettingsOut.setAgeB(ageB);
                    orientationSettingsOut.setSex(sex);

                    //按行业投放
                    if (hobbyList != null && hobbyMap != null) {
                        orientationSettingsOut.setXingqu(hobbyList);
                        orientationSettingsOut.setHobbyMap(hobbyMap);
                    }

                    if (industrys.get(7).isSelected()) { //如果选的自定义
                        industryMark = "1";
                        industryNameStr = "自定义";
                        orientationSettingsOut.setIndustryName("自定义");
                        orientationSettingsOut.setIndustryMark("1");
                    } else {
                        industryMark = "0";
                        orientationSettingsOut.setIndustryMark(industryMark);
                        if (industrys.get(0).isSelected()) {
                            industryNameStr = "餐饮";
                        } else if (industrys.get(1).isSelected()) {
                            industryNameStr = "3C数码";
                        } else if (industrys.get(2).isSelected()) {
                            industryNameStr = "日用百货";
                        } else if (industrys.get(3).isSelected()) {
                            industryNameStr = "母婴用品";
                        } else if (industrys.get(4).isSelected()) {
                            industryNameStr = "美容美发";
                        } else if (industrys.get(5).isSelected()) {
                            industryNameStr = "鲜花礼品";
                        } else if (industrys.get(6).isSelected()) {
                            industryNameStr = "汽车用品";
                        }else if (industrys.get(8).isSelected()) {
                            industryNameStr = "不限";
                        }
                        orientationSettingsOut.setIndustryName(industryNameStr);
                    }
                    orientationSettingsOut.setUserId(Integer.parseInt(UserDataManeger.getInstance().getUserId()));
                    ArrayList<OrientationSettingsOut> list = (ArrayList<OrientationSettingsOut>) aCache.getAsObject(SharedPreferencesTool.DIRECTION_HISTORY);

                    if (list != null) { //如果已经有缓存数据
                        List<OrientationSettingsOut> templist = new ArrayList<>();
                        for (OrientationSettingsOut direction : list) {
                            if (UserDataManeger.getInstance().getUserId().equals(direction.getUserId() + "")) {
                                templist.add(direction);
                            }
                        }
                        list.removeAll(templist); //去除重复的同一个用户的定向设置的数据
                    } else {
                        list = new ArrayList<>();
                    }
                    //保存数据到缓存
                    list.add(orientationSettingsOut);

                    aCache.put(SharedPreferencesTool.DIRECTION_HISTORY, list);

                    //传到上个页面定向设置的数据
                    Intent data = new Intent();
                    data.putExtra("ageF", ageF);
                    data.putExtra("ageB", ageB);
                    data.putExtra("sex", sex);

                    Logger.d("最后得到的二级标签为：" + interestIds);
                    data.putExtra("interestIds", interestIds); //兴趣点。
                    data.putExtra("industryMark", industryMark);
                    data.putExtra("industryNameStr", industryNameStr);

                    setResult(RESULT_OK, data);//返回上个界面
                    finish();
                }
                break;
        }

    }


    /**
     * 保存之前 自上而下判空 并提示
     */
    private boolean checkEmpty(String interestIds) {

        Logger.d("interestIds值为：" + interestIds);

        //如果自定义被选中，并且没有选择商业兴趣。
        if (industrys.get(7).isSelected() && TextUtils.isEmpty(interestIds)) { //没有选择商业兴趣
            ToastUtils.showShort("请选择商业兴趣。");
            return false;
        }
        return true;
    }


    /**
     * 设置是否可点
     *
     * @param isEnable
     * @param relativeLayout
     */
    private void setEnable(boolean isEnable, RelativeLayout relativeLayout) {
        relativeLayout.setEnabled(isEnable);
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
    protected DirectionalSettingPresent<IDirectionalSettingView> createPresent() {
        return new DirectionalSettingPresent<>(this);
    }





    /**
     * 选择兴趣动态加入框中
     * @param data
     */
    private void addHobbyToFrame(final List<String> data) {
        if (data!=null) {
            labelNum = data.size();
            personNumStr = CommonUtils.getPersonNum(currentCircleRadius, mCityName, ageF, ageB, sex, industryStr, labelNum);
            tvPersonNum.setText(personNumStr);
        }
        if (data != null && data.size() > 0) {
            directionHobbyInfo.setVisibility(View.VISIBLE);
            directionHobbyInfo.removeAllViews();

            for (int i = 0; i < data.size(); i++) {
                final View view = getLayoutInflater().inflate(R.layout.view_fluid_layout, null);
                TextView textView = view.findViewById(R.id.tv_fiud);
                ImageView imageView = view.findViewById(R.id.iv_fiud);

//                final TextView textView = new TextView(this);
                FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(15, 15, 15, 15);
                textView.setEllipsize(TextUtils.TruncateAt.END);
                //设置最大字数为10，多余的显示...
                textView.setMaxEms(10);
                textView.setTextColor(getResources().getColor(R.color.text_gray));
                textView.setSingleLine(true);
                textView.setPadding(15, 15, 15, 15);
//                Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.close);
//                textView.setCompoundDrawablesWithIntrinsicBounds(null,
//                        null, drawable, null);
//                textView.setCompoundDrawablePadding(10);
//                textView.setBackgroundResource(R.drawable.btn_no_selec_border);

                final String his = data.get(i);
                textView.setText(his);
                imageView.setOnClickListener(new View.OnClickListener() { //点击删除兴趣点
                    @Override
                    public void onClick(View v) {
                        data.remove(his);
                        labelNum = data.size();
                        if (labelNum!=0) {
                            personNumStr = CommonUtils.getPersonNum(currentCircleRadius, mCityName, ageF, ageB, sex, industryStr, labelNum);
                        }

                        tvPersonNum.setText(personNumStr);
                        //将二级置false
                        clearSecondCheked(2, his);
                        if (data == null || data.size() == 0) {
                            directionHobbyInfo.setVisibility(View.GONE);
                        }
                        deleteHobby(his);
                        directionHobbyInfo.removeView(view);
                        directionHobbyInfo.invalidate();
                    }
                });
                directionHobbyInfo.addView(view, params);
            }
        } else {
            directionHobbyInfo.removeAllViews();
            directionHobbyInfo.setVisibility(View.GONE);
        }
    }


    /**
     * 点下面框中 叉删掉的时候 将存的信息删掉
     */
    private void deleteHobby(String deleteStr) {
        for (Map.Entry<String, List<String>> entry : hobbyMap.entrySet()) {
            String key = entry.getKey();
            List<String> list = entry.getValue(); //兴趣点


            if (list.remove(deleteStr)) {  //删除value, 如果删除成功
                if (list == null || list.size() == 0) {
                    hobbyMap.remove(key);
                }
                break;
            }
        }
    }


    /**
     * 清除二级选择
     *
     * @param type
     * @param str
     */
    private void clearSecondCheked(int type, String str) {
        //兴趣
        List<InterestTagImportOut> list = settingOuts.getIco(); //获得所有的兴趣信息列表
        for (int i = 0; i < list.size(); i++) {
            List<InterestCategory> interestCategories = list.get(i).getEri(); //得到所有二级兴趣点

            for (int j = 0; j < interestCategories.size(); j++) {
                if (str.equals(interestCategories.get(j).getIndustryName())) { //二级兴趣点的名称
                    interestCategories.get(j).setChecked(false); //删除之后，将该二级兴趣点设置成false
                    break;
                }
            }

            int numChecked = 0;
            for (int s = 0; s < interestCategories.size(); s++) {
                if (interestCategories.get(s).isChecked()) {
                    numChecked++; //查看二级兴趣点有几个已经被选中了
                }
            }
            if (numChecked == 0) {
                //一级名称设置成没有被选中
                settingOuts.getIco().get(i).setChecked(false);
            }
        }

    }


    /**
     * 兴趣 一级Dialog
     *
     * @param listString
     * @param view
     */
    private void showHobbyFromDialog(List<InterestTagImportOut> listString, final TextView view) {
        Logger.d("返回的兴趣爱好是：" + listString);
        if (listString != null && listString.size() > 0) {
            final BusinessInterestDialog dialog = new BusinessInterestDialog(this, R.style.Theme_Light_Dialog);
            dialog.setOnchangeListener(new BusinessInterestDialog.ChangeListener() {
                @Override
                public void onChangeListener(List<InterestTagImportOut> data, int position) {
                    view.setText(data.get(position).getIndustryName());
                    dialog.dismiss();
                    hobbyFromPosition = position;
                    setEnable(true, hobby_to);
//                    updateHobbyFromData(data, position);
                }
            });
            dialog.setUpData(listString);
            dialog.setSelect(0);
            dialog.show();
        }
    }


    /**
     * 兴趣二级dialog
     * @param listString
     * @param view
     */
    private void showHobbyToDialog(List<InterestCategory> listString, final TextView view) {
        if (listString != null && listString.size() > 0) {

            final HobbyMultipleSelectDialog dialog = new HobbyMultipleSelectDialog(this, R.style.Theme_Light_Dialog, hobbyList);
            dialog.setOnchangeListener(new HobbyMultipleSelectDialog.SelectListener() {
                @Override
                public void onSelectListener(List<InterestCategory> data) {
                    //动态添加框中

                   /* if (hobbyList == null) {
                        hobbyList = new ArrayList<>();
                    }*/

                    List<String> listStr = new ArrayList<>(); //放二级兴趣点
                    int len = 0;//已经选择的项

                    for (int i = 0; i < data.size(); i++) { //遍历后台返回的兴趣信息
                        InterestCategory interestCategory = data.get(i); //得到二级兴趣点
                        String name = interestCategory.getIndustryName(); //得到二级兴趣的名称

                        if (interestCategory.isChecked()) { //如果二级兴趣的名称已经被选过了
                            len++;
                            listStr.add(interestCategory.getIndustryName());
                            // TODO: 2018/4/4 设置兴趣二级的文本
                            Logger.d("选择的兴趣二级是：" + interestCategory.getIndustryName());
                        }

                        if (interestCategory.isChecked() && !hobbyList.contains(name)) { //被选中，并且不包含
                            if (hobbyList.size() == 6) {
                                //将第二项列表超过6个的置未 (i-data.size-1) 未选择 退出循环
                                interestCategory.setChecked(false);
                            } else {
                                hobbyList.add(name);
                            }
                        } else if (!(interestCategory.isChecked()) && hobbyList.contains(name)) {
//                            hobbyList.remove(name);
                        }
                    }
                    //将数据 添加到 map中
                    if (len > 0) { //(hobbyFromPosition代表选择的兴趣名称的位置)
                        //把兴趣点添加到hobbyMap中
                        hobbyMap.put(settingOuts.getIco().get(hobbyFromPosition).getIndustryName(), listStr);
                        addHobbyToFrame(hobbyList);
                        //将后台返回的兴趣点设置成已选择
                        settingOuts.getIco().get(hobbyFromPosition).setChecked(true);
                    }
                    dialog.dismiss();
                }

                @Override
                public void onCancel() {
                    updateHobbyFromData(hobbyFromPosition);
//                    dialog.dismiss();
                }
            });
            dialog.setUpData(listString);
            dialog.show();
        }
    }

    /**
     * 重置--兴趣数据
     * @param position
     */
    private void updateHobbyFromData(int position) {
        //重置 将一级的选择删掉
        InterestTagImportOut interestTagImportOut = settingOuts.getIco().get(position);

        if (interestTagImportOut != null) {
            interestTagImportOut.setChecked(false);
            //将二级相应的true 置城false
            List<InterestCategory> list = interestTagImportOut.getEri();
            for (InterestCategory interestCategory : list) {
                if (interestCategory.isChecked()) {
                    interestCategory.setChecked(false);
                }
                if (hobbyList != null) {
                    for (int i = 0; i < hobbyList.size(); i++) {
                        if (hobbyList.get(i).equals(interestCategory.getIndustryName())) {
                            hobbyList.remove(i);
                        }
                    }
                }
            }
            addHobbyToFrame(hobbyList);
            for (Map.Entry<String, List<String>> map : hobbyMap.entrySet()) {
                String key = map.getKey();
                if (!TextUtils.isEmpty(interestTagImportOut.getIndustryName()) && interestTagImportOut.getIndustryName().equals(key)) {
                    hobbyMap.remove(key);
                    return;
                }
            }
        }

    }




    //设置行业TextView的状态
    private void setTextState(int index, boolean isSelected) {
        for (int i = 0; i < industrys.size(); i++) {
            if (index == i) {
                if (!industrys.get(i).isSelected()) {
                    industrys.get(i).setSelected(true);
                }
            } else {
                if (isSelected) {
                    industrys.get(i).setSelected(false);
                }
            }
        }
        llUserHobby.setVisibility(View.GONE);

        industryStr = industrysStr[index];
    }



    //设置各个行业TextView的状态
    private boolean getTextState() {
        int sum = 0;
        for (int i = 0; i < industrys.size(); i++) {
            if (industrys.get(i).isSelected()) {
                sum++;
            }
        }
        if (sum == 0) {
            return false;
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAfterTransition(this);
//        EasyTransition.exit(this);
    }
}
