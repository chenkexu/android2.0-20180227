package com.orientdata.lookforcustomers.view.findcustomer.impl;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.BaseTagImportOut;
import com.orientdata.lookforcustomers.bean.InterestCategory;
import com.orientdata.lookforcustomers.bean.InterestTagImportOut;
import com.orientdata.lookforcustomers.bean.OrientationSettingsOut;
import com.orientdata.lookforcustomers.bean.PhoneModelTag;
import com.orientdata.lookforcustomers.bean.SettingOut;
import com.orientdata.lookforcustomers.presenter.DirectionalSettingPresent;
import com.orientdata.lookforcustomers.util.SharedPreferencesTool;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.certification.fragment.ACache;
import com.orientdata.lookforcustomers.view.findcustomer.IDirectionalSettingView;
import com.orientdata.lookforcustomers.widget.FluidLayout;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.orientdata.lookforcustomers.widget.dialog.BusinessInterestDialog;
import com.orientdata.lookforcustomers.widget.dialog.HobbyMultipleSelectDialog;
import com.orientdata.lookforcustomers.widget.dialog.SettingStringDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vr.md.com.mdlibrary.UserDataManeger;



/**
 * Created by wy on 2017/11/27.
 * 定向设置Activity
 */

public class DirectionalSettingActivity2 extends BaseActivity<IDirectionalSettingView, DirectionalSettingPresent<IDirectionalSettingView>> implements IDirectionalSettingView, View.OnClickListener {
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

    @BindView(R.id.tv_car)
    TextView tvCar;
    @BindView(R.id.directionHobbyInfo)
    FluidLayout directionHobbyInfo; //用户兴趣爱好，自定义标签
    @BindView(R.id.tv_canyin)
    TextView tvCanyin;
    @BindView(R.id.ll_user_hobby)
    LinearLayout llUserHobby;


    private MyTitle titleDirectional;
    private SettingOut settingOuts = null; //后台返回的数据
    private RelativeLayout age_from;
    private RelativeLayout age_to;//年龄
    private RelativeLayout sex;//性别


    private RelativeLayout hobby_from;
    private RelativeLayout hobby_to;//商业兴趣



    boolean isFirstCheckModel = true;
    private TextView tv_mModelOne;//机型 一级

    private FluidLayout directionBaseInfo;


    private TextView tv_mHobbyOne;//商业兴趣 一级



    private TextView tvSave;//保存按钮
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
    private String industryMark = "0"; //是否自定义行业

    private String industryNameStr = ""; //选择的行业
    private String[] industrysStr = {"餐饮","3C数码","汽车用品","母婴用品","美容美发","鲜花礼品","汽车用品","自定义"};
    private String industryName;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directional_setting2);
        ButterKnife.bind(this);
        initView();
        initTitle();
        //获取缓存的数据
        mPresent.getSelectSetting(cityCode);

        //获取后台数据
    }



    //获取后台返回的内容
    @Override
    public void getSelectSetting(SettingOut settingOuts) {
        if (settingOuts!=null) {
            this.settingOuts = settingOuts;
        }
        if (settingOuts!=null) {
            getData();
        }
    }


    private void initView() {
        industrys.add(tvCanyin);
        industrys.add(tv3c);
        industrys.add(tvBaihe);
        industrys.add(tvMuying);
        industrys.add(tvHair);
        industrys.add(tvFlower);
        industrys.add(tvCar);
        industrys.add(zidingyi);
        String[] stringArrayFrom = getResources().getStringArray(R.array.ages_from);
        agesFrom = Arrays.asList(stringArrayFrom);//年龄集合

        String[] stringArrayTo = getResources().getStringArray(R.array.ages_from);
        agesTo = Arrays.asList(stringArrayTo);//年龄集合


        String[] stringArraySex = getResources().getStringArray(R.array.sex);
        sexs = Arrays.asList(stringArraySex);//性别集合
        titleDirectional = (MyTitle) findViewById(R.id.titleDirectional);
        age_from = (RelativeLayout) findViewById(R.id.age_from);
        age_to = (RelativeLayout) findViewById(R.id.age_to);
        sex = (RelativeLayout) findViewById(R.id.sex);
        tvSave = (TextView) findViewById(R.id.tvSave);
        tv_mAgeFrom = age_from.findViewById(R.id.tvLeftText);
        tv_mAgeTo = age_to.findViewById(R.id.tvLeftText);
        tv_mSex = sex.findViewById(R.id.tvLeftText);

        hobby_from = (RelativeLayout) findViewById(R.id.hobby_from);
        hobby_to = (RelativeLayout) findViewById(R.id.hobby_to);

        directionHobbyInfo = (FluidLayout) findViewById(R.id.directionHobbyInfo);

        tv_mHobbyOne = hobby_from.findViewById(R.id.tvLeftText);
        tv_mHobbyTo = hobby_to.findViewById(R.id.tvLeftText);

        tv_mAgeFrom.setText("不限");
        tv_mAgeTo.setText("不限");
        tv_mSex.setText("不限");

        /**
         * 设置是否可点
         */
        setEnable(false, age_to);
//        setEnable(false, edu_to);
//        setEnable(false, consumption_to);
//        setEnable(false, model_to);
//        setEnable(false, hobby_to);
        age_from.setOnClickListener(this);
        age_to.setOnClickListener(this);
        sex.setOnClickListener(this);

        hobby_from.setOnClickListener(this);
        hobby_to.setOnClickListener(this);
        tvSave.setOnClickListener(this);


        aCache = ACache.get(this);
        if (getIntent() != null) {
            //上个页面返回cityCode和isReCreate
            isReCreate = getIntent().getBooleanExtra("isReCreate", false);
            Logger.d("isReCreate:---"+isReCreate);
            cityCode = getIntent().getStringExtra("cityCode");
        }

        if (hobbyMap == null) {
            hobbyMap = new HashMap<>();
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
            updateData();
            setHobby(orientationSettingsOut);
        } else {  //不是点击详情进来，是缓存的数据
            ArrayList<OrientationSettingsOut> list = (ArrayList<OrientationSettingsOut>) aCache.getAsObject(SharedPreferencesTool.DIRECTION_HISTORY);
            if (list != null) {
                Logger.d("读取本地缓存的内容"+list.size());
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
                updateData();
//                setList(orientationSettingsOut);//
                if (orientationSettingsOut!=null) {

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
        }else {
            zidingyi.setVisibility(View.VISIBLE);
        }
        if (orientationSettingsOut == null) { //如果没有缓存数据，会拿到后台返回的数据进行更新界面
            Logger.d("没有缓存的数据。。");
            //定向设置 无缓存信息
            ageFromPosition = -1;
            ageToPosition = -1;
            sexPosition = -1;
//            modelFrom = -1;
            hobbyFromPosition = -1;
        } else { //有数据需要显示
            Logger.d("orientationSettingsOut不为空，获取到了缓存的数据。。");
            if (isReCreate) {  //上个任务的任务的详情
                setMapData(); //再次创建任务保留的数据
            }

            //获取年龄性别
            String ageF = orientationSettingsOut.getAgeF();
            String ageB = orientationSettingsOut.getAgeB();
            String sex = orientationSettingsOut.getSex();

            if (industryMark.equals("0")) { //行业
                switch (industryName){
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
            }else{ //自定义，显示下面得到兴趣标签
                industrys.get(7).setSelected(true);
                llUserHobby.setVisibility(View.VISIBLE);
            }

            ageFromPosition = getIndex(agesFrom, ageF); //获取年龄在集合中的位置
            ageToPosition = getIndex(agesTo, ageB) - ageFromPosition;
            sexPosition = getIndex(sexs, sex);

            //设置年龄和性别
            tv_mAgeFrom.setText(ageFromPosition == -1 ? "" : ageF);
            tv_mAgeTo.setText(ageToPosition == -1 ? "" : ageB);
            tv_mSex.setText(sexPosition == -1 ? "" : sex);

        }
//        setEnable(hobbyFromPosition == -1 ? false : true, hobby_to);

    }


    //设置兴趣爱好 ，设置缓存数据
    public void setList(OrientationSettingsOut orientationSettingsOut) {
        Logger.d("setList-orientationSettingsOut:"+orientationSettingsOut);
        List<String> listHobby = orientationSettingsOut.getXingqu();
        if (listHobby != null) {
            listHobby.clear();
        }
        //如果之前缓存中有兴趣爱好，就保存。
        if (orientationSettingsOut.getHobbyMap()!=null) {
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
        Logger.d("setHobby-orientationSettingsOut:"+orientationSettingsOut);
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
        if (orientationSettingsOut.getXingqu()!=null) {  //设置数据缓存中的兴趣
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










    private void initTitle() {
        titleDirectional.setTitleName(getString(R.string.direction_setting));
        titleDirectional.setImageBack(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.age_from: //选择用户起始年龄
                showAgeFromDialog(agesFrom, tv_mAgeFrom);
                break;
            case R.id.age_to:  //选择用户结束年龄
                if (tv_mAgeFrom.getText().toString().equals("不限")) {
                    tv_mAgeTo.setText("不限");
                    return;
                }
                showAgeToDialog(agesTo, tv_mAgeTo);
                break;
            case R.id.sex:
                showSexDialog(sexs, tv_mSex);
                break;
            case R.id.hobby_from://兴趣爱好
                if (settingOuts.getIco()==null) {
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
            case R.id.tvSave: //保存数据
                String ageF = tv_mAgeFrom.getText().toString().trim();
                String ageB = tv_mAgeTo.getText().toString().trim();
                String sex = tv_mSex.getText().toString().trim();


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
                    if (hobbyList!=null && hobbyMap!=null) {
                        orientationSettingsOut.setXingqu(hobbyList);
                        orientationSettingsOut.setHobbyMap(hobbyMap);
                    }

                    if (industrys.get(7).isSelected()) { //如果选的自定义
                        industryMark = "1";
                        industryNameStr = "自定义";
                        orientationSettingsOut.setIndustryName("自定义");
                        orientationSettingsOut.setIndustryMark("1");
                    }else{
                        industryMark = "0";
                        orientationSettingsOut.setIndustryMark(industryMark);
                        if (industrys.get(0).isSelected()) {
                            industryNameStr = "餐饮";
                        }else if(industrys.get(1).isSelected()){
                            industryNameStr = "3C数码";
                        }else if(industrys.get(2).isSelected()){
                            industryNameStr = "日用百货";
                        }else if(industrys.get(3).isSelected()){
                            industryNameStr = "母婴用品";
                        }else if(industrys.get(4).isSelected()){
                            industryNameStr = "美容美发";
                        }else if(industrys.get(5).isSelected()){
                            industryNameStr = "鲜花礼品";
                        }else if(industrys.get(6).isSelected()){
                            industryNameStr = "汽车用品";
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

                    Logger.d("最后得到的二级标签为："+interestIds);
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

        Logger.d("interestIds值为："+interestIds);

        //如果自定义被选中，并且没有选择商业兴趣。
        if (industrys.get(7).isSelected()&&TextUtils.isEmpty(interestIds)) { //没有选择商业兴趣
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

    int ageFromPosition = -1;


    /**
     * 显示第一个年龄的选择dialog
     * @param listString
     * @param view
     */
    private void showAgeFromDialog(List<String> listString, final TextView view) {
        if (listString != null && listString.size() > 0) { //年龄有数据
            listString = listString.subList(0, listString.size() - 1);
            final SettingStringDialog dialog = new SettingStringDialog(this, R.style.Theme_Light_Dialog);
            dialog.setOnchangeListener(new SettingStringDialog.ChangeListener() {
                @Override
                public void onChangeListener(String data, int position) {
                    view.setText(data);
                    dialog.dismiss();
                    if (updateToView(position, ageToPosition, ageFromPosition, tv_mAgeTo) != -2) {
                        ageToPosition = updateToView(position, ageToPosition, ageFromPosition, tv_mAgeTo);
                    }
                    ageFromPosition = position;
                    setEnable(true, age_to);
                }
            });
            dialog.setUpData(listString);
            dialog.setSelect(ageFromPosition);
            dialog.show();
        }
    }




    /**
     * 第一项选择变化 导致第二项内容变化 更新第二项的position 和 textview内的内容
     * @param curPosition  第一项当前选择的position
     * @param toPosition   第二项前一次选择的position
     * @param fromPosition 第一项前一次选择的position
     * @param tv           第二项 textview
     * @return -2无效 不改变值
     */
    private int updateToView(int curPosition, int toPosition, int fromPosition, TextView tv) {
        if (toPosition != -1) {
            int curAgeToPostion = toPosition + fromPosition;//当前所选的年龄第二项 在list中的position
            if (curPosition > curAgeToPostion) {
                //第二项选择的不在范围内 置空
                tv.setText("");
                return -1;
            } else {
                //年龄二级选择增加了项   更新ageToPosition的位置
                return (fromPosition - curPosition) + toPosition;
            }
        }
        return -2;
    }

    int ageToPosition = -1;


    /**
     * 显示第二个年龄的选择dialog
     * @param listString
     * @param view
     */
    private void showAgeToDialog(List<String> listString, final TextView view) {
        if (listString != null) {
            //显示 （ageFromPosition - size-1）
            String tv_mAgeFromStr = tv_mAgeFrom.getText().toString().trim();

            if (tv_mAgeFromStr.equals("0岁")) {
                listString = listString.subList(2, listString.size() - 1);
            } else {
                listString = listString.subList(ageFromPosition, listString.size());
            }
            final SettingStringDialog dialog = new SettingStringDialog(this, R.style.Theme_Light_Dialog);
            dialog.setOnchangeListener(new SettingStringDialog.ChangeListener() {
                @Override
                public void onChangeListener(String data, int position) {
                    view.setText(data);
                    dialog.dismiss();
                    ageToPosition = position;
                }
            });
            dialog.setUpData(listString);
            dialog.setSelect(ageToPosition);
            dialog.show();
        }
    }


    private int getIndex(List<String> list, String str) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (str.equals(list.get(i))) {
                    return i;
                }
            }
        }
        return -1;
    }


    private int getIndex1(List<BaseTagImportOut> listString, String str) {
        if (listString != null) {
            for (int i = 0; i < listString.size(); i++) {
                if (str.equals(listString.get(i).getTagName())) {
                    return i;
                }
            }
        }
        return -1;
    }


    int sexPosition = -1;






    /**
     * 性别选择
     * @param listString
     * @param view
     */
    private void showSexDialog(List<String> listString, final TextView view) {
        if (listString != null && listString.size() > 0) {
            final SettingStringDialog dialog = new SettingStringDialog(this, R.style.Theme_Light_Dialog);
            dialog.setOnchangeListener(new SettingStringDialog.ChangeListener() {
                @Override
                public void onChangeListener(String data, int position) {
                    view.setText(data);
                    dialog.dismiss();
                    sexPosition = position;
                }
            });
            dialog.setUpData(listString);
            dialog.setSelect(sexPosition);
            dialog.show();
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
    protected DirectionalSettingPresent<IDirectionalSettingView> createPresent() {
        return new DirectionalSettingPresent<>(this);
    }


    @OnClick({R.id.hobby_from,R.id.hobby_to,R.id.tv_3c, R.id.tv_baihe, R.id.tv_canyin, R.id.tv_muying, R.id.tv_hair, R.id.tv_flower, R.id.zidingyi, R.id.tv_car})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_canyin:
                setTextState(0,true);
                break;
            case R.id.tv_3c:
                setTextState(1,true);
                break;
            case R.id.tv_baihe://日百
                setTextState(2,true);
                break;
            case R.id.tv_muying: //母婴
                setTextState(3,true);
                break;
            case R.id.tv_hair: //美容美发
                setTextState(4,true);
                break;
            case R.id.tv_flower://鲜花礼品
                setTextState(5,true);
                break;
            case R.id.tv_car://汽车
                setTextState(6,true);
                break;
            case R.id.zidingyi://自定义
                setTextState(7,true);
                llUserHobby.setVisibility(View.VISIBLE);
                break;
        }
    }









    /**
     * 选择兴趣动态加入框中
     * @param data
     */
    private void addHobbyToFrame(final List<String> data) {
        if (data != null && data.size() > 0) {
            directionHobbyInfo.setVisibility(View.VISIBLE);
            directionHobbyInfo.removeAllViews();
            for (int i = 0; i < data.size(); i++) {
                final TextView textView = new TextView(this);
                FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(15, 15, 15, 15);
                textView.setEllipsize(TextUtils.TruncateAt.END);
                //设置最大字数为10，多余的显示...
                textView.setMaxEms(10);
                textView.setTextColor(getResources().getColor(R.color.text_gray));
                textView.setSingleLine(true);
                textView.setPadding(15, 15, 15, 15);
                Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.close);
                textView.setCompoundDrawablesWithIntrinsicBounds(null,
                        null, drawable, null);
                textView.setCompoundDrawablePadding(10);
                textView.setBackgroundResource(R.drawable.shape_bg_gray_round);

                final String his = data.get(i);
                textView.setText(his);
                textView.setOnClickListener(new View.OnClickListener() { //点击删除兴趣点
                    @Override
                    public void onClick(View v) {
                        data.remove(his);
                        //将二级置false
                        clearSecondCheked(2, his);
                        if (data == null || data.size() == 0) {
                            directionHobbyInfo.setVisibility(View.GONE);
                        }
                        deleteHobby(his);
                        directionHobbyInfo.removeView(v);
                        directionHobbyInfo.invalidate();
                    }
                });
                directionHobbyInfo.addView(textView, params);
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
            List<String> list = entry.getValue();
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
     * @param listString
     * @param view
     */
    private void showHobbyFromDialog(List<InterestTagImportOut> listString, final TextView view) {
        Logger.d("返回的兴趣爱好是："+listString);
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
                            Logger.d("选择的兴趣二级是："+interestCategory.getIndustryName());
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

        if(interestTagImportOut!=null){
            interestTagImportOut.setChecked(false);
            //将二级相应的true 置城false
            List<InterestCategory> list = interestTagImportOut.getEri();
            for (InterestCategory interestCategory : list) {
                if (interestCategory.isChecked()) {
                    interestCategory.setChecked(false);
                }
                if(hobbyList!=null){
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
                if(!TextUtils.isEmpty(interestTagImportOut.getIndustryName()) && interestTagImportOut.getIndustryName().equals(key)){
                    hobbyMap.remove(key);
                    return;
                }
            }
        }

    }






    //设置行业TextView的状态
    private void setTextState(int index , boolean isSelected){
        for (int i=0;i<industrys.size();i++) {
            if (index == i) {
                if (!industrys.get(i).isSelected()) {
                    industrys.get(i).setSelected(true);
                }
            }else{
                if (isSelected) {
                    industrys.get(i).setSelected(false);
                }
            }
        }
        llUserHobby.setVisibility(View.GONE);

    }

    //设置行业TextView的状态
    private boolean getTextState(){
        int sum =0;
        for (int i=0;i<industrys.size();i++) {
            if (industrys.get(i).isSelected()) {
                sum++;
            }
        }
        if (sum==0) {
            return false;
        }
        return true;
    }



}
