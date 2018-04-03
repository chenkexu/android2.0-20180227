package com.orientdata.lookforcustomers.view.findcustomer.impl;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import com.orientdata.lookforcustomers.bean.TaskOut;
import com.orientdata.lookforcustomers.presenter.DirectionalSettingPresent;
import com.orientdata.lookforcustomers.util.SharedPreferencesTool;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.certification.fragment.ACache;
import com.orientdata.lookforcustomers.view.findcustomer.IDirectionalSettingView;
import com.orientdata.lookforcustomers.widget.FluidLayout;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.orientdata.lookforcustomers.widget.dialog.BusinessInterestDialog;
import com.orientdata.lookforcustomers.widget.dialog.HobbyMultipleSelectDialog;
import com.orientdata.lookforcustomers.widget.dialog.ModelDialog;
import com.orientdata.lookforcustomers.widget.dialog.MultipleSelectDialog;
import com.orientdata.lookforcustomers.widget.dialog.SettingStringDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import vr.md.com.mdlibrary.UserDataManeger;

/**
 * Created by wy on 2017/11/27.
 * 定向设置Activity
 */

public class DirectionalSettingActivity extends BaseActivity<IDirectionalSettingView, DirectionalSettingPresent<IDirectionalSettingView>> implements IDirectionalSettingView, View.OnClickListener {
    private MyTitle titleDirectional;
    private SettingOut settingOuts = null;
    private RelativeLayout age_from;
    private RelativeLayout age_to;//年龄
    private RelativeLayout sex;//性别
    private RelativeLayout edu_from;
    private RelativeLayout edu_to;//教育
    private RelativeLayout loc;//归属地
    private RelativeLayout consumption_from;
    private RelativeLayout consumption_to;//消费能力
    private RelativeLayout model_from;
    private RelativeLayout model_to;//机型
    private RelativeLayout hobby_from;
    private RelativeLayout hobby_to;//商业兴趣
    boolean isFirstCheckModel = true;
    private TextView tv_mModelOne;//机型 一级
    private FluidLayout directionBaseInfo;
    private FluidLayout directionHobbyInfo;
    private TextView tv_mHobbyOne;//商业兴趣 一级
    private TextView tvSave;//保存按钮
    private TextView tv_mAgeFrom;
    private TextView tv_mAgeTo;
    private TextView tv_mSex;
    private TextView tv_mEduFrom;
    private TextView tv_mEduTo;
    private TextView tv_mLoc;
    private TextView tv_mConsumptionFrom;
    private TextView tv_mConsumptionTo;
    private TextView tv_mModelTo;
    private TextView tv_mHobbyTo;
    int modelFrom = -1;
    private List<String> modelList;//选中的机型
    int hobbyFromPosition = -1;
    private List<String> hobbyList;
    private String cityCode = "";
    private ACache aCache = null;//数据缓存
    private OrientationSettingsOut orientationSettingsOut = null;//数据缓存
    private Map<String, List<String>> modeMap = null;//存储机型
    private Map<String, List<String>> hobbyMap = null;//存储商业兴趣
    private boolean isReCreate = false;//是否是再次创建寻客


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directional_setting);
        ButterKnife.bind(this);
        initView();
        initTitle();
        mPresent.getSelectSetting(cityCode);
    }

    private void initView() {
        aCache = ACache.get(this);
        isReCreate = getIntent().getBooleanExtra("isReCreate", false);
        getData();
        cityCode = getIntent().getStringExtra("cityCode");
        if (modeMap == null) {
            modeMap = new HashMap<>();
        }
        if (hobbyMap == null) {
            hobbyMap = new HashMap<>();
        }
        titleDirectional = (MyTitle) findViewById(R.id.titleDirectional);
        age_from = (RelativeLayout) findViewById(R.id.age_from);
        age_to = (RelativeLayout) findViewById(R.id.age_to);
        sex = (RelativeLayout) findViewById(R.id.sex);
        edu_from = (RelativeLayout) findViewById(R.id.edu_from);
        edu_to = (RelativeLayout) findViewById(R.id.edu_to);
        consumption_from = (RelativeLayout) findViewById(R.id.consumption_from);
        consumption_to = (RelativeLayout) findViewById(R.id.consumption_to);
        model_from = (RelativeLayout) findViewById(R.id.model_from);
        loc = (RelativeLayout) findViewById(R.id.loc);
        model_to = (RelativeLayout) findViewById(R.id.model_to);
        hobby_from = (RelativeLayout) findViewById(R.id.hobby_from);
        hobby_to = (RelativeLayout) findViewById(R.id.hobby_to);
        tv_mModelOne = model_from.findViewById(R.id.tvLeftText);
        directionBaseInfo = (FluidLayout) findViewById(R.id.directionBaseInfo);
        directionHobbyInfo = (FluidLayout) findViewById(R.id.directionHobbyInfo);
        tv_mHobbyOne = hobby_from.findViewById(R.id.tvLeftText);
        tvSave = (TextView) findViewById(R.id.tvSave);
        tv_mAgeFrom = age_from.findViewById(R.id.tvLeftText);
        tv_mAgeTo = age_to.findViewById(R.id.tvLeftText);
        tv_mSex = sex.findViewById(R.id.tvLeftText);
        tv_mEduFrom = edu_from.findViewById(R.id.tvLeftText);
        tv_mEduTo = edu_to.findViewById(R.id.tvLeftText);
        tv_mConsumptionFrom = consumption_from.findViewById(R.id.tvLeftText);
        tv_mConsumptionTo = consumption_to.findViewById(R.id.tvLeftText);
        tv_mLoc = loc.findViewById(R.id.tvLeftText);
        tv_mHobbyTo = hobby_to.findViewById(R.id.tvLeftText);
        tv_mModelTo = model_to.findViewById(R.id.tvLeftText);

        /**
         * 设置是否可点
         */
        setEnable(false, age_to);
        setEnable(false, edu_to);
        setEnable(false, consumption_to);
        setEnable(false, model_to);
        setEnable(false, hobby_to);

        age_from.setOnClickListener(this);
        age_to.setOnClickListener(this);
        sex.setOnClickListener(this);
        edu_from.setOnClickListener(this);
        edu_to.setOnClickListener(this);
        loc.setOnClickListener(this);
        consumption_from.setOnClickListener(this);
        consumption_to.setOnClickListener(this);
        model_from.setOnClickListener(this);
        model_to.setOnClickListener(this);
        hobby_from.setOnClickListener(this);
        hobby_to.setOnClickListener(this);
        tvSave.setOnClickListener(this);
    }

    /**
     * 获取定向设置 当前用户的缓存数据
     */
    private void getData() {
        if (isReCreate) {
            Logger.d("点击详情的定向设置数据");
            //再次创建 后台数据
            orientationSettingsOut = (OrientationSettingsOut) getIntent().getSerializableExtra("orientationSettingsOut");
            //想办法将 多出的list 修改
        } else {
            //缓存的数据
            ArrayList<OrientationSettingsOut> list = (ArrayList<OrientationSettingsOut>) aCache.getAsObject(SharedPreferencesTool.DIRECTION_HISTORY);
            if (list != null) {
                Logger.d("缓存的定向设置的数据："+list.size());
                for (OrientationSettingsOut direction : list) {
                    if (UserDataManeger.getInstance().getUserId().equals(direction.getUserId() + "")) {
                        orientationSettingsOut = direction;
                        break;
                    }
                }
            }else {
                Logger.d("没有任何数据，再次进行选择");
            }
        }
    }

    /**
     * 设置orientationSettingsOut对象中机型modeMap 商业兴趣hobbyMap 的数据
     */
    private void setMapData() {
        Map<String, List<String>> modeMap = new HashMap<>();
        Map<String, List<String>> hobbyMap = new HashMap<>();
        List<BaseTagImportOut> baseTagImportOuts = settingOuts.getJixing();
        List<InterestTagImportOut> interestTagImportOuts = settingOuts.getIco();

        for (int i = 0; i < baseTagImportOuts.size(); i++) {
            BaseTagImportOut base = baseTagImportOuts.get(i);
            String name = base.getTagName();
            if ("不限".equals(name)) {
                //不限单独处理
                List<String> list = new ArrayList<>();
                for (String str : orientationSettingsOut.getJixing()) {
                    if ("不限".equals(str)) {
                        list.add("不限");
                        modeMap.put("不限", list);
                        break;
                    }
                }
            } else {
                for (String str : orientationSettingsOut.getJixing()) {
                    if (name.equals(str)) {
                        //查找 相应的List
                        List<String> listMode = new ArrayList<>();
                        List<PhoneModelTag> erps = base.getErp();
                        if (erps != null) {
                            for (int j = 0; j < erps.size(); j++) {
                                String phoneTag = erps.get(j).getTagName();
                                for (String str1 : orientationSettingsOut.getJixing()) {
                                    if (phoneTag.equals(str1)) {
                                        listMode.add(str1);
                                    }
                                }
                                if (j == erps.size() - 1) {
                                    modeMap.put(name, listMode);
                                }
                            }

                        }
                    }
                }
            }
        }
        for (int i = 0; i < interestTagImportOuts.size(); i++) {
            InterestTagImportOut interestTagImportOut = interestTagImportOuts.get(i);
            String name = interestTagImportOut.getIndustryName();
            for (String str : orientationSettingsOut.getXingqu()) {
                if (name.equals(str)) {
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
        orientationSettingsOut.setModeMap(modeMap);
    }

    private void initTitle() {
        titleDirectional.setTitleName(getString(R.string.direction_setting));
        titleDirectional.setImageBack(this);
    }


    @Override
    public void onClick(View v) {
        if (settingOuts == null) {
            ToastUtils.showShort("获取设置信息失败！");
            return;
        }
        switch (v.getId()) {
            case R.id.age_from:
                showAgeFromDialog(settingOuts.getAge(), tv_mAgeFrom);
                break;
            case R.id.age_to:
                showAgeToDialog(settingOuts.getAge(), tv_mAgeTo);
                break;
            case R.id.sex:
                showSexDialog(settingOuts.getSex(), tv_mSex);
                break;
            case R.id.edu_from:
                showEduFromDialog(settingOuts.getEducation(), tv_mEduFrom);
                break;
            case R.id.edu_to:
                showEduToDialog(settingOuts.getEducation(), tv_mEduTo);
                break;
            case R.id.loc:
                showLocDialog(settingOuts.getBelong(), tv_mLoc);
                break;
            case R.id.consumption_from:
                showConsumptionFromDialog(settingOuts.getXiaofei(), tv_mConsumptionFrom);
                break;
            case R.id.consumption_to:
                showConsumptionToDialog(settingOuts.getXiaofei(), tv_mConsumptionTo);
                break;
            case R.id.model_from:
                showModelFromDialog(settingOuts.getJixing(), tv_mModelOne);
                break;
            case R.id.model_to:
                if (settingOuts.getJixing() == null || modelFrom == -1 || modelFrom > settingOuts.getJixing().size() - 1) {
                    return;
                }
                showModelToDialog(settingOuts.getJixing().get(modelFrom).getErp(), tv_mModelTo);
                break;
            case R.id.hobby_from:
                showHobbyFromDialog(settingOuts.getIco(), tv_mHobbyOne);
                break;
            case R.id.hobby_to:
                if (settingOuts.getIco() == null || hobbyFromPosition == -1 || hobbyFromPosition > settingOuts.getIco().size() - 1) {
                    return;
                }
                showHobbyToDialog(settingOuts.getIco().get(hobbyFromPosition).getEri(), tv_mHobbyTo);
                break;
            case R.id.tvSave:
                String ageF = tv_mAgeFrom.getText().toString().trim();
                String ageB = tv_mAgeTo.getText().toString().trim();
                String educationLevelF = tv_mEduFrom.getText().toString().trim();
                String educationLevelB = tv_mEduTo.getText().toString().trim();
                String sex = tv_mSex.getText().toString().trim();
                String consumptionCapacityF = tv_mConsumptionFrom.getText().toString().trim();//消费能力一
                String consumptionCapacityB = tv_mConsumptionTo.getText().toString().trim();
                String ascription = tv_mLoc.getText().toString().trim();//归属地
                String phoneModelIds = "";//机型名称数组,以逗号隔开 如"苹果,iphone6,iphoneX"
                for (Map.Entry<String, List<String>> map : modeMap.entrySet()) {
                    String key = map.getKey();
                    List<String> listStr = map.getValue();
                    phoneModelIds += key + ",";
                    for (String str : listStr) {
                        phoneModelIds += str + ",";
                    }
                }
                if (!TextUtils.isEmpty(phoneModelIds)) {
                    phoneModelIds = phoneModelIds.substring(0, phoneModelIds.length() - 1);

                }

                String interestIds = "";//兴趣名称数组,以逗号隔开的 如 "教育,学前教育,东磁教育"

                for (Map.Entry<String, List<String>> map : hobbyMap.entrySet()) {
                    String key = map.getKey();
                    List<String> listStr = map.getValue();
                    interestIds += key + ",";
                    for (String str : listStr) {
                        interestIds += str + ",";
                    }
                }
                if (!TextUtils.isEmpty(interestIds)) {
                    //去掉最后一个,
                    interestIds = interestIds.substring(0, interestIds.length() - 1);
                }

                if (checkEmpty(phoneModelIds, interestIds)) {
                    //缓存数据
                    orientationSettingsOut = new OrientationSettingsOut();
                    orientationSettingsOut.setAgeF(ageF);
                    orientationSettingsOut.setAgeB(ageB);
                    orientationSettingsOut.setSex(sex);
                    orientationSettingsOut.setEducationLevelF(educationLevelF);
                    orientationSettingsOut.setEducationLevelB(educationLevelB);
                    orientationSettingsOut.setAscription(ascription);
                    orientationSettingsOut.setConsumptionCapacityF(consumptionCapacityF);
                    orientationSettingsOut.setConsumptionCapacityB(consumptionCapacityB);
                    orientationSettingsOut.setJixing(modelList);
                    orientationSettingsOut.setXingqu(hobbyList);
                    orientationSettingsOut.setUserId(Integer.parseInt(UserDataManeger.getInstance().getUserId()));
                    orientationSettingsOut.setModeMap(modeMap);
                    orientationSettingsOut.setHobbyMap(hobbyMap);
                    ArrayList<OrientationSettingsOut> list = (ArrayList<OrientationSettingsOut>) aCache.getAsObject(SharedPreferencesTool.DIRECTION_HISTORY);
                    if (list != null) {
                        List<OrientationSettingsOut> templist = new ArrayList<OrientationSettingsOut>();
                        for (OrientationSettingsOut direction : list) {
                            if (UserDataManeger.getInstance().getUserId().equals(direction.getUserId() + "")) {
                                templist.add(direction);
                            }
                        }
                        list.removeAll(templist);
                    } else {
                        list = new ArrayList<>();
                    }
                    list.add(orientationSettingsOut);
                    aCache.put(SharedPreferencesTool.DIRECTION_HISTORY, list);

                    Intent data = new Intent();
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
                    setResult(RESULT_OK, data);
                    finish();
                }
                break;

        }

    }

    /**
     * 保存之前 自上而下判空 并提示
     */
    private boolean checkEmpty(String phoneModelIds, String interestIds) {
        if (ageFromPosition == -1 || ageToPosition == -1) {
            ToastUtils.showShort("请选择用户年龄。");
            return false;
        }
        if (sexPosition == -1) {
            ToastUtils.showShort("请选择性别。");
            return false;
        }
        if (eduFromPosition == -1 || eduToPosition == -1) {
            ToastUtils.showShort("请选择教育程度。");
            return false;
        }
        if (locPosition == -1) {
            ToastUtils.showShort("请选择归属地。");
            return false;
        }
        if (consumptionFromPosition == -1 || consumptionToPosition == -1) {
            ToastUtils.showShort("请选择消费能力。");
            return false;
        }
        if (TextUtils.isEmpty(phoneModelIds)) {
            ToastUtils.showShort("请选择机型。");
            return false;
        }
        if (TextUtils.isEmpty(interestIds)) {
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
     *
     * @param listString
     * @param view
     */
    private void showAgeFromDialog(final List<String> listString, final TextView view) {
        if (listString != null && listString.size() > 0) {
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
     *
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
     *
     * @param listString
     * @param view
     */
    private void showAgeToDialog(List<String> listString, final TextView view) {
        if (listString != null) {
            //显示 （ageFromPosition - size-1）
            listString = listString.subList(ageFromPosition, listString.size());
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

    /**
     * 返回"不限"的index
     *
     * @return
     */
    private int getIndex(List<String> list) {
        return getIndex(list, "不限");
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

    /**
     * 返回"不限"的index 机型
     *
     * @return
     */
    private int getIndex1(List<BaseTagImportOut> listString) {
        return getIndex1(listString, "不限");
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

    /**
     * 返回"不限"的index 兴趣
     *
     * @return
     */
    private int getIndex2(List<InterestTagImportOut> listString) {
        return getIndex2(listString, "不限");
    }

    private int getIndex2(List<InterestTagImportOut> listString, String str) {
        if (listString != null) {
            for (int i = 0; i < listString.size(); i++) {
                if (str.equals(listString.get(i).getIndustryName())) {
                    return i;
                }
            }
        }
        return -1;
    }

    int sexPosition = -1;

    /**
     * 性别选择
     *
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

    int eduFromPosition = -1;

    /**
     * 学历 第一项选择
     *
     * @param listString
     * @param view
     */
    private void showEduFromDialog(List<String> listString, final TextView view) {
        if (listString != null && listString.size() > 0) {
            final SettingStringDialog dialog = new SettingStringDialog(this, R.style.Theme_Light_Dialog);
            dialog.setOnchangeListener(new SettingStringDialog.ChangeListener() {
                @Override
                public void onChangeListener(String data, int position) {
                    view.setText(data);
                    dialog.dismiss();
                    if (updateToView(position, eduToPosition, eduFromPosition, tv_mEduTo) != -2) {
                        eduToPosition = updateToView(position, eduToPosition, eduFromPosition, tv_mEduTo);
                    }
                    eduFromPosition = position;
                    setEnable(true, edu_to);
                }
            });
            dialog.setUpData(listString);
            dialog.setSelect(eduFromPosition);
            dialog.show();
        }
    }

    int eduToPosition = -1;

    /**
     * 学历 第二项选择
     *
     * @param listString
     * @param view
     */
    private void showEduToDialog(List<String> listString, final TextView view) {
        if (listString != null && listString.size() > 0) {
            //显示 （eduFromPosition - size-1）
            listString = listString.subList(eduFromPosition, listString.size());
            final SettingStringDialog dialog = new SettingStringDialog(this, R.style.Theme_Light_Dialog);
            dialog.setOnchangeListener(new SettingStringDialog.ChangeListener() {
                @Override
                public void onChangeListener(String data, int position) {
                    view.setText(data);
                    dialog.dismiss();
                    eduToPosition = position;
                }
            });
            dialog.setUpData(listString);
            dialog.setSelect(eduToPosition);
            dialog.show();
        }
    }

    int locPosition = -1;

    /**
     * 所在地
     *
     * @param listString
     * @param view
     */
    private void showLocDialog(List<String> listString, final TextView view) {
        if (listString != null && listString.size() > 0) {
            final SettingStringDialog dialog = new SettingStringDialog(this, R.style.Theme_Light_Dialog);
            dialog.setOnchangeListener(new SettingStringDialog.ChangeListener() {
                @Override
                public void onChangeListener(String data, int position) {
                    view.setText(data);
                    dialog.dismiss();
                    locPosition = position;
                }
            });
            dialog.setUpData(listString);
            dialog.setSelect(locPosition);
            dialog.show();
        }
    }

    int consumptionFromPosition = -1;

    /**
     * 消费一级
     *
     * @param listString
     * @param view
     */
    private void showConsumptionFromDialog(List<String> listString, final TextView view) {
        if (listString != null && listString.size() > 0) {
            final SettingStringDialog dialog = new SettingStringDialog(this, R.style.Theme_Light_Dialog);
            dialog.setOnchangeListener(new SettingStringDialog.ChangeListener() {
                @Override
                public void onChangeListener(String data, int position) {
                    view.setText(data);
                    dialog.dismiss();
                    if (updateToView(position, consumptionToPosition, consumptionFromPosition, tv_mConsumptionTo) != -2) {
                        consumptionToPosition = updateToView(position, consumptionToPosition, consumptionFromPosition, tv_mConsumptionTo);
                    }
                    consumptionFromPosition = position;
                    setEnable(true, consumption_to);
                }
            });
            dialog.setUpData(listString);
            dialog.setSelect(consumptionFromPosition);
            dialog.show();
        }
    }

    int consumptionToPosition = 0;

    /**
     * 消费二级
     *
     * @param listString
     * @param view
     */
    private void showConsumptionToDialog(List<String> listString, final TextView view) {
        if (listString != null && listString.size() > 0) {
            //显示 （consumptionFromPosition - size-1）
            listString = listString.subList(consumptionFromPosition, listString.size());
            final SettingStringDialog dialog = new SettingStringDialog(this, R.style.Theme_Light_Dialog);
            dialog.setOnchangeListener(new SettingStringDialog.ChangeListener() {
                @Override
                public void onChangeListener(String data, int position) {
                    view.setText(data);
                    dialog.dismiss();
                    consumptionToPosition = position;
                }
            });
            dialog.setUpData(listString);
            dialog.setSelect(consumptionToPosition);
            dialog.show();
        }
    }

    /**
     * 机型 一级
     *
     * @param listString
     * @param view
     */
    private void showModelFromDialog(List<BaseTagImportOut> listString, final TextView view) {
        if (listString != null && listString.size() > 0) {
            final ModelDialog dialog = new ModelDialog(this, R.style.Theme_Light_Dialog);
            dialog.setOnchangeListener(new ModelDialog.ChangeListener() {
                @Override
                public void onChangeListener(List<BaseTagImportOut> data, int position) {
                    view.setText(data.get(position).getTagName());
                    dialog.dismiss();
                    modelFrom = position;
                    setEnable(true, model_to);
//                    updateModelFromData(data, position);
                }
            });
            dialog.setUpData(listString);
            dialog.setSelect(modelFrom);
            dialog.show();
        }
    }

    private void updateModelFromData(List<BaseTagImportOut> data, int position) {
        settingOuts.getJixing().get(position).setChecked(data.get(position).isChecked());
    }

    private void updateHobbyFromData(List<InterestTagImportOut> data, int position) {
        settingOuts.getIco().get(position).setChecked(data.get(position).isChecked());
    }

    /**
     * 更新兴趣数据
     *
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

    private void updateHobbyFromData1(int position) {
        //重置 将一级的选择删掉
        settingOuts.getIco().get(position).setChecked(true);

    }

    /**
     * 更新机型数据
     *
     * @param position
     */
    private void updateModelFromData(int position) {
        //重置 将一级的选择删掉
        BaseTagImportOut baseTagImportOut = settingOuts.getJixing().get(position);
        if(baseTagImportOut!=null){
            baseTagImportOut.setChecked(false);
            //将二级相应的true 置城false
            List<PhoneModelTag> list = baseTagImportOut.getErp();
            for (PhoneModelTag phone : list) {
                if (phone.isChecked()) {
                    phone.setChecked(false);
                }
                for (int i = 0; i < modelList.size(); i++) {
                    if (modelList.get(i).equals(phone.getTagName())) {
                        modelList.remove(i);
                    }
                }
            }
            addModelToFrame(modelList);
            for (Map.Entry<String, List<String>> map : modeMap.entrySet()) {
                String key = map.getKey();
                if(!TextUtils.isEmpty(baseTagImportOut.getTagName()) && baseTagImportOut.getTagName().equals(key)){
                    modeMap.remove(key);
                    return;
                }
            }
        }
    }

    int modelTo = -1;

    //jixing de position

    /**
     * 机型二级
     *
     * @param listString
     * @param view
     */
    private void showModelToDialog(List<PhoneModelTag> listString, final TextView view) {
        if (listString != null && listString.size() > 0) {
            final MultipleSelectDialog dialog = new MultipleSelectDialog(this, R.style.Theme_Light_Dialog, modelList);
            dialog.setOnchangeListener(new MultipleSelectDialog.SelectListener() {
                @Override
                public void onSelectListener(List<PhoneModelTag> data) {
                    //动态添加框中
                    if (modelList == null) {
                        modelList = new ArrayList<>();
                    }
                    List<String> listStr = new ArrayList<>();
                    int len = 0;//cheked的个数
                    for (int i = 0; i < data.size(); i++) {
                        PhoneModelTag phoneModelTag = data.get(i);
                        String name = phoneModelTag.getTagName();
                        if (phoneModelTag.isChecked()) {
                            len++;
                            listStr.add(phoneModelTag.getTagName());
                        }

                        if (phoneModelTag.isChecked() && !modelList.contains(name)) {
                            if (modelList.size() == 6) {
                                //将第二项列表超过6个的置未 (i-data.size-1) 未选择
                                phoneModelTag.setChecked(false);
                            } else {
                                modelList.add(name);
                            }
                        } else if (!(phoneModelTag.isChecked()) && modelList.contains(name)) {
                            modelList.remove(name);
                        }
                    }
                    if (len > 0) {
                        //将数据 添加到 map中
                        modeMap.put(settingOuts.getJixing().get(modelFrom).getTagName(), listStr);
                        addModelToFrame(modelList);
                        //将modelFrom true
                        settingOuts.getJixing().get(modelFrom).setChecked(true);
                    }
                    dialog.dismiss();
                }

                @Override
                public void onCancel() {
                    updateModelFromData(modelFrom);
//                    dialog.dismiss();
                }
            });
            dialog.setUpData(listString);
            dialog.show();
        }
    }

    /**
     * 机型动态加到框中
     *
     * @param data
     */

    private void addModelToFrame(final List<String> data) {
        if (data != null && data.size() > 0) {
            directionBaseInfo.setVisibility(View.VISIBLE);
            directionBaseInfo.removeAllViews();
            for (int i = 0; i < data.size(); i++) {
                final TextView textView = new TextView(this);
                FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(15, 15, 15, 15);
                textView.setEllipsize(TextUtils.TruncateAt.END);
                //设置最大字数为10，多余的显示...
                textView.setMaxEms(10);
                textView.setSingleLine(true);
                textView.setPadding(15, 15, 15, 15);
                Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.close);
                textView.setCompoundDrawablesWithIntrinsicBounds(null,
                        null, drawable, null);
                textView.setCompoundDrawablePadding(10);
                textView.setBackgroundResource(R.drawable.shape_bg_gray_round);

                final String his = data.get(i);
                textView.setText(his);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data.remove(his);
                        //将二级置false
                        clearSecondCheked(1, his);
                        if (data == null || data.size() == 0) {
                            directionBaseInfo.setVisibility(View.GONE);
                        }
                        deleteModel(his);
                        directionBaseInfo.removeView(v);
                        directionBaseInfo.invalidate();
                    }
                });
                directionBaseInfo.addView(textView, params);
            }
        } else {
            directionBaseInfo.removeAllViews();
            directionBaseInfo.setVisibility(View.GONE);
        }
    }

    /**
     * 点下面框中 叉删掉的时候 将存的信息删掉
     */
    private void deleteModel(String deleteStr) {
        for (Map.Entry<String, List<String>> entry : modeMap.entrySet()) {
            String key = entry.getKey();
            List<String> list = entry.getValue();
            if (list.remove(deleteStr)) {
                if (list == null || list.size() == 0) {
                    modeMap.remove(key);
                }
                break;
            }
        }
    }

    /**
     * 点下面框中 叉删掉的时候 将存的信息删掉
     */
    private void deleteHobby(String deleteStr) {
        for (Map.Entry<String, List<String>> entry : hobbyMap.entrySet()) {
            String key = entry.getKey();
            List<String> list = entry.getValue();
            if (list.remove(deleteStr)) {
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
        if (type == 1) {
            //机型
            List<BaseTagImportOut> list = settingOuts.getJixing();
            for (int i = 0; i < list.size(); i++) {
                List<PhoneModelTag> phoneModelTags = list.get(i).getErp();
                for (int j = 0; j < phoneModelTags.size(); j++) {
                    if (str.equals(phoneModelTags.get(j).getTagName())) {
                        phoneModelTags.get(j).setChecked(false);
                        break;
                    }
                }
                int numChecked = 0;
                for (int s = 0; s < phoneModelTags.size(); s++) {
                    if (phoneModelTags.get(s).isChecked()) {
                        numChecked++;
                    }
                }
                if (numChecked == 0) {
                    //删掉一级的
                    settingOuts.getJixing().get(i).setChecked(false);
                }
            }
        } else {
            //兴趣
            List<InterestTagImportOut> list = settingOuts.getIco();
            for (int i = 0; i < list.size(); i++) {
                List<InterestCategory> interestCategories = list.get(i).getEri();
                for (int j = 0; j < interestCategories.size(); j++) {
                    if (str.equals(interestCategories.get(j).getIndustryName())) {
                        interestCategories.get(j).setChecked(false);
                        break;
                    }
                }
                int numChecked = 0;
                for (int s = 0; s < interestCategories.size(); s++) {
                    if (interestCategories.get(s).isChecked()) {
                        numChecked++;
                    }
                }
                if (numChecked == 0) {
                    //删掉一级的
                    settingOuts.getIco().get(i).setChecked(false);
                }
            }
        }
    }

    /**
     * 兴趣 一级
     *
     * @param listString
     * @param view
     */

    private void showHobbyFromDialog(List<InterestTagImportOut> listString, final TextView view) {
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
     * 兴趣二级
     *
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
                    if (hobbyList == null) {
                        hobbyList = new ArrayList<>();
                    }
                    List<String> listStr = new ArrayList<>();
                    int len = 0;//已经选择的项
                    for (int i = 0; i < data.size(); i++) {
                        InterestCategory interestCategory = data.get(i);
                        String name = interestCategory.getIndustryName();
                        if (interestCategory.isChecked()) {
                            len++;
                            listStr.add(interestCategory.getIndustryName());
                        }
                        if (interestCategory.isChecked() && !hobbyList.contains(name)) {
                            if (hobbyList.size() == 6) {
                                //将第二项列表超过6个的置未 (i-data.size-1) 未选择 退出循环
                                interestCategory.setChecked(false);
                            } else {
                                hobbyList.add(name);
                            }
                        } else if (!(interestCategory.isChecked()) && hobbyList.contains(name)) {
                            hobbyList.remove(name);
                        }
                    }
                    //将数据 添加到 map中
                    //将modelFrom true 防止重置后再选择 确定时一级类型不选中
                    if (len > 0) {
                        hobbyMap.put(settingOuts.getIco().get(hobbyFromPosition).getIndustryName(), listStr);
                        addHobbyToFrame(hobbyList);
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
     * 选择兴趣动态加入框中
     *
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
                textView.setSingleLine(true);
                textView.setPadding(15, 15, 15, 15);
                Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.close);
                textView.setCompoundDrawablesWithIntrinsicBounds(null,
                        null, drawable, null);
                textView.setCompoundDrawablePadding(10);
                textView.setBackgroundResource(R.drawable.shape_bg_gray_round);

                final String his = data.get(i);
                textView.setText(his);
                textView.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void getSelectSetting(SettingOut settingOuts) {
        if (settingOuts == null) {
            ToastUtils.showShort("获取设置信息失败！");
            return;
        }
        this.settingOuts = settingOuts;
        if (settingOuts != null) {
            updateData();
            updateView();
        }
    }

    private void updateData() {
        if (orientationSettingsOut == null) {
            //定向设置 无缓存信息
            ageFromPosition = -1;
            ageToPosition = -1;
            sexPosition = -1;
            eduFromPosition = -1;
            eduToPosition = -1;
            locPosition = -1;
            consumptionFromPosition = -1;
            consumptionToPosition = -1;
            modelFrom = -1;
            hobbyFromPosition = -1;
            if(settingOuts!=null){
                List<String> ageList = settingOuts.getAge();
                if(ageList!=null && ageList.size()> -1){
                    ageFromPosition = 0;
                    ageToPosition = 0;
                    tv_mAgeFrom.setText(ageList.get(0));
                    tv_mAgeTo.setText(ageList.get(0));
                }
                List<String> sexList = settingOuts.getSex();
                if(sexList!=null && sexList.size()> -1){
                    sexPosition = 0;
                    tv_mSex.setText(sexList.get(0));
                }
                List<String> eduList = settingOuts.getEducation();
                if(eduList!=null && eduList.size()> -1){
                    eduFromPosition = 0;
                    eduToPosition = 0;
                    tv_mEduFrom.setText(eduList.get(0));
                    tv_mEduTo.setText(eduList.get(0));
                }
                List<String> locList = settingOuts.getBelong();
                if(locList!=null && locList.size()> -1){
                    locPosition = 0;
                    tv_mLoc.setText(locList.get(0));
                }
                List<String> consumptionList = settingOuts.getXiaofei();
                if(consumptionList!=null && consumptionList.size()> -1){
                    consumptionFromPosition = 0;
                    consumptionToPosition = 0;
                    tv_mConsumptionFrom.setText(consumptionList.get(0));
                    tv_mConsumptionTo.setText(consumptionList.get(0));
                }
                List<BaseTagImportOut> models = settingOuts.getJixing();
                if (modelList == null) {
                    modelList = new ArrayList<>();
                }
                if(models!=null && models.size()>-1){
                    modelFrom = 0;
                    String tagName = models.get(0).getTagName();
                    tv_mModelOne.setText(tagName);
                    if("不限".equals(tagName)){
                        //只有不限 显示 二级此项对应的第一项 并且 二级此项对应的第一项是不限
                        List<PhoneModelTag> phoneModelTagList = models.get(0).getErp();
                        if(phoneModelTagList!=null && phoneModelTagList.size()>-1){
                            String phoneName = phoneModelTagList.get(0).getTagName();
                            if("不限".equals(phoneName)){
                                tv_mModelTo.setText(phoneName);
                                modelList.add(phoneName);
                                if (modeMap != null) {
                                    List<String> list = new ArrayList<>();
                                    list.add("不限");
                                    modeMap.put("不限", list);
                                }
                            }
                        }

                    }
                }
            }
            setModelNormal();
        } else {
            if (isReCreate) {
                setMapData();
            }
            setList();
            //有缓存信息
            String ageF = orientationSettingsOut.getAgeF();
            String ageB = orientationSettingsOut.getAgeB();
            String sex = orientationSettingsOut.getSex();
            String educationLevelF = orientationSettingsOut.getEducationLevelF();
            String educationLevelB = orientationSettingsOut.getEducationLevelB();
            String ascription = orientationSettingsOut.getAscription();
            String consumptionCapacityF = orientationSettingsOut.getConsumptionCapacityF();
            String consumptionCapacityB = orientationSettingsOut.getConsumptionCapacityB();

            ageFromPosition = getIndex(settingOuts.getAge(), ageF);
            ageToPosition = getIndex(settingOuts.getAge(), ageB) - ageFromPosition;
            sexPosition = getIndex(settingOuts.getSex(), sex);
            eduFromPosition = getIndex(settingOuts.getEducation(), educationLevelF);
            eduToPosition = getIndex(settingOuts.getEducation(), educationLevelB) - eduFromPosition;
            locPosition = getIndex(settingOuts.getBelong(), ascription);
            consumptionFromPosition = getIndex(settingOuts.getXiaofei(), consumptionCapacityF);
            consumptionToPosition = getIndex(settingOuts.getXiaofei(), consumptionCapacityB) - consumptionFromPosition;
            setModel();
            setHobby();

            tv_mAgeFrom.setText(ageFromPosition == -1 ? "" : ageF);
            tv_mAgeTo.setText(ageToPosition == -1 ? "" : ageB);
            tv_mSex.setText(sexPosition == -1 ? "" : sex);
            tv_mEduFrom.setText(eduFromPosition == -1 ? "" : educationLevelF);
            tv_mEduTo.setText(eduToPosition == -1 ? "" : educationLevelB);
            tv_mLoc.setText(locPosition == -1 ? "" : ascription);
            tv_mConsumptionFrom.setText(consumptionFromPosition == -1 ? "" : consumptionCapacityF);
            tv_mConsumptionTo.setText(consumptionToPosition == -1 ? "" : consumptionCapacityB);
        }
        setEnable(ageFromPosition == -1 ? false : true, age_to);
        setEnable(eduFromPosition == -1 ? false : true, edu_to);
        setEnable(consumptionFromPosition == -1 ? false : true, consumption_to);
        setEnable(modelFrom == -1 ? false : true, model_to);
        setEnable(hobbyFromPosition == -1 ? false : true, hobby_to);

    }

    public void setList() {
        List<String> list = orientationSettingsOut.getJixing();
        if (list != null) {
            list.clear();
        }
        List<String> listHobby = orientationSettingsOut.getXingqu();
        if (listHobby != null) {
            listHobby.clear();
        }
        for (Map.Entry<String, List<String>> map : orientationSettingsOut.getModeMap().entrySet()) {
            String key = map.getKey();
            List<String> listMode = map.getValue();
            list.addAll(listMode);
        }

        for (Map.Entry<String, List<String>> map : orientationSettingsOut.getHobbyMap().entrySet()) {
            String key = map.getKey();
            List<String> listIntrest = map.getValue();
            listHobby.addAll(listIntrest);
        }
    }

    /**
     * 设置机型的数据 并更新界面
     */
    private void setModel() {
        if (modeMap == null) {
            modeMap = new HashMap<>();
        }
        if (orientationSettingsOut.getModeMap() != null) {
            modeMap.putAll(orientationSettingsOut.getModeMap());
        }
        if (modelList == null) {
            modelList = new ArrayList<>();
        }
        modelList.addAll(orientationSettingsOut.getJixing());
        //更新settingOuts.getJixing()
        for (Map.Entry<String, List<String>> map : modeMap.entrySet()) {
            String key = map.getKey();
            tv_mModelOne.setText(key);
            List<String> listStr = map.getValue();
            for (int i = 0; i < settingOuts.getJixing().size(); i++) {
                if (settingOuts.getJixing().get(i).getTagName().equals(key)) {
                    modelFrom = i;
                    settingOuts.getJixing().get(i).setChecked(true);
                    List<PhoneModelTag> listPhone = settingOuts.getJixing().get(i).getErp();
                    if (listPhone != null) {
                        for (int j = 0; j < listPhone.size(); j++) {
                            for (String str : listStr) {
                                if (str.equals(listPhone.get(j).getTagName())) {
                                    listPhone.get(j).setChecked(true);
                                    break;
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
        addModelToFrame(modelList);//框中内容
    }

    /**
     * 默认的
     */
    private void setModelNormal() {
        //更新settingOuts.getJixing()
        for (Map.Entry<String, List<String>> map : modeMap.entrySet()) {
            String key = map.getKey();
            tv_mModelOne.setText(key);
            List<String> listStr = map.getValue();
            for (int i = 0; i < settingOuts.getJixing().size(); i++) {
                if (settingOuts.getJixing().get(i).getTagName().equals(key)) {
                    modelFrom = i;
                    settingOuts.getJixing().get(i).setChecked(true);
                    List<PhoneModelTag> listPhone = settingOuts.getJixing().get(i).getErp();
                    if (listPhone != null) {
                        for (int j = 0; j < listPhone.size(); j++) {
                            for (String str : listStr) {
                                if (str.equals(listPhone.get(j).getTagName())) {
                                    listPhone.get(j).setChecked(true);
                                    break;
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
        addModelToFrame(modelList);//框中内容
    }

    /**
     * 设置兴趣的数据 并更新界面
     */
    private void setHobby() {
        if (hobbyMap == null) {
            hobbyMap = new HashMap<>();
        }
        if (orientationSettingsOut.getHobbyMap() != null) {
            hobbyMap.putAll(orientationSettingsOut.getHobbyMap());
        }
        if (hobbyList == null) {
            hobbyList = new ArrayList<>();
        }
        hobbyList.addAll(orientationSettingsOut.getXingqu());
        //更新settingOuts.getXingqu
        for (Map.Entry<String, List<String>> map : hobbyMap.entrySet()) {
            String key = map.getKey();
            tv_mHobbyOne.setText(key);
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

    private void updateView() {

    }
}
