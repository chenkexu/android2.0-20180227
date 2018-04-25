package com.orientdata.lookforcustomers.view.certification.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseFragment;
import com.orientdata.lookforcustomers.bean.Area;
import com.orientdata.lookforcustomers.bean.AreaOut;
import com.orientdata.lookforcustomers.bean.CertificationBean;
import com.orientdata.lookforcustomers.bean.CertificationOut;
import com.orientdata.lookforcustomers.presenter.CertificatePresent;
import com.orientdata.lookforcustomers.util.RegexUtils;
import com.orientdata.lookforcustomers.util.SharedPreferencesTool;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.certification.EnterpriseCertificationUploadActivity;
import com.orientdata.lookforcustomers.view.certification.ICertificateView;
import com.orientdata.lookforcustomers.widget.EditTextView;
import com.orientdata.lookforcustomers.widget.EditTextViewIdCode;
import com.orientdata.lookforcustomers.widget.EditTextViewNumBer;
import com.orientdata.lookforcustomers.widget.dialog.CityDialog;
import com.orientdata.lookforcustomers.widget.dialog.ProvinceCityDialog;

import java.util.ArrayList;
import java.util.List;

import vr.md.com.mdlibrary.UserDataManeger;

/**
 * Created by wy on 2017/11/16.
 * 企业认证
 */

public class EnterpriseCertificationFragment extends BaseFragment implements View.OnClickListener,ICertificateView{

    private CertificatePresent mEnterpriseCertificatePresent;
    private TextView tvChooseProvince;
    private ImageView ivChooseProvince;
    private TextView tvChooseCity;
    private ImageView ivChooseCity;
    private TextView province_position;
    private TextView city_position;
    private Button btNext;
    private TextView province_code;
    private TextView city_code;
    private EditTextView enterpriseName;
    private EditTextView businessNum;
    private EditTextView contact;
    private EditTextViewNumBer contactPhone;
    private EditTextViewIdCode contactIdNum;
    private EditTextView enterpriseAddress;

    private List<AreaOut> areaOuts;
    private List<Area> areas;
    private int provincePosition = -1;
    private int cityPosition = -1;

    String name;
    String businessLicenseNo;
    String contactPerson;
    String phone;
    String contactCard;
    String cityCode;
    String provinceCode;
    String address;
    String province;
    String city;
    ACache aCache = null;//数据缓存
    private String path = "";//缓存数据的位置
    private LinearLayout linearProvince,linearCity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enterprise_certification,container,false);
        initView(view);
        initEvent();
        getData();
        getProvinceCity();

//        ImmersionBar.with(this).
//                keyboardEnable(true).init();

        return view;
    }
    private void initEvent() {
//        tvChooseCity.setOnClickListener(this);
//        ivChooseCity.setOnClickListener(this);
//        tvChooseProvince.setOnClickListener(this);
//        ivChooseProvince.setOnClickListener(this);
        linearProvince.setOnClickListener(this);
        linearCity.setOnClickListener(this);
        btNext.setOnClickListener(this);
    }
    ArrayList<CertificationBean> dataListCatch = null;//认证缓存的信息
    private int curCach = -1;

    /**
     * 获取缓存的认证数据 和 省市
     */
    private void getData(){
        areaOuts = (List<AreaOut>) SharedPreferencesTool.getInstance().getObjectFromShare(SharedPreferencesTool.AREA_KEY);

        //使用getAsObject()，直接进行强转
        dataListCatch = (ArrayList<CertificationBean>) aCache.getAsObject(SharedPreferencesTool.CERTIFICATE_KEY);

        if(dataListCatch!=null){
            for(int i=0;i<dataListCatch.size();i++){
                String id = dataListCatch.get(i).getUserId()+"";
                //id 并且是企业认证
                if(UserDataManeger.getInstance().getUserId().equals(id) && dataListCatch.get(i).getType() == 1){
                    curCach = i;
                    CertificationBean certificationBean = dataListCatch.get(i);
                    getInputMsg(certificationBean);
                }
            }
        }

    }

    /**
     * 更新页面
     */
    private void updateView(){
        enterpriseName.setText(name);
        businessNum.setText(businessLicenseNo);
        contact.setText(contactPerson);
        contactPhone.setText(phone);
        contactPhone.setType(InputType.TYPE_CLASS_PHONE);
        contactIdNum.setText(contactCard);
        enterpriseAddress.setText(address);
        if(!TextUtils.isEmpty(city)){
            tvChooseCity.setText(city);
            city_position.setText(cityPosition+"");
        }
        if(!TextUtils.isEmpty(province)){
            tvChooseProvince.setText(province);
            province_position.setText(provincePosition+"");
        }
        province_code.setText(provinceCode);
        city_code.setText(cityCode);
    }
    public static EnterpriseCertificationFragment newInstance() {
        Bundle args = new Bundle();
        EnterpriseCertificationFragment fragment = new EnterpriseCertificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 省选择是否发生变化
     * @param position
     * @return
     */
    private boolean isChange(int position){
        String str = province_position.getText().toString();
        if(!TextUtils.isEmpty(str)){
            if(Integer.parseInt(str)!=position){
                return true;
            }
        }
        return false;
    }

    /**
     * 获取认证信息
     */
    private void getInputMsg(){
        name = enterpriseName.getText().toString();//企业名称
        businessLicenseNo = businessNum.getText().toString(); //营业执照编号
        contactPerson = contact.getText().toString(); //联系人
        phone = contactPhone.getText().toString();//是 联系人电话
        contactCard = contactIdNum.getText().toString();//是  //联系人身份证号
        cityCode = city_code.getText().toString();//是
        provinceCode = province_code.getText().toString();//是
        address = enterpriseAddress.getText().toString();//是  企业通信地址
        String strProvince = province_position.getText().toString();
        if(!TextUtils.isEmpty(strProvince)){
            provincePosition = Integer.parseInt(strProvince);
        }
        String strCity = city_position.getText().toString();
        if(!TextUtils.isEmpty(strCity)){
            cityPosition = Integer.parseInt(strCity);
        }
        province = tvChooseProvince.getText().toString();
        city = tvChooseCity.getText().toString();
    }

    /**
     * 获取认证信息
     * @param certificationBean
     */
    private void getInputMsg(CertificationBean certificationBean){
        if(certificationBean!=null){
            name = certificationBean.getName();//是
            businessLicenseNo = certificationBean.getBusinessLicenseNo();
            contactPerson = certificationBean.getContactPerson();
            phone = certificationBean.getContactPhone();//是
            contactCard = certificationBean.getContactCard();//是
            cityCode = certificationBean.getCityCode();//是
            provinceCode = certificationBean.getProvinceCode();//是
            address = certificationBean.getAddress();//是
            provincePosition = certificationBean.getProvincePosition();
            cityPosition = certificationBean.getCityPosition();
            province = certificationBean.getProvince();
            city = certificationBean.getCity();
            updateView();
        }
    }
    /**
     * 下一步
     */
    private void next(){
        //存储信息
        getInputMsg();
        if (TextUtils.isEmpty(name)){
            ToastUtils.showShort("企业名称不能为空");
            return;
        }
        if (TextUtils.isEmpty(businessLicenseNo)) {
            ToastUtils.showShort("营业执照编号不能为空");
            return;
        }
        if (TextUtils.isEmpty(contactPerson)) {
            ToastUtils.showShort("联系人不能为空");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort("联系人电话不能为空");
            return;
        }
        if (!RegexUtils.isIDCard18(contactCard)) {
            ToastUtils.showShort("身份证号不合法");
            return;
        }
        if (TextUtils.isEmpty(provinceCode)) {
            ToastUtils.showShort("省不能为空");
            return;
        }
        if (TextUtils.isEmpty(cityCode)) {
            ToastUtils.showShort("市不能为空");
            return;
        }
        if (TextUtils.isEmpty(address) ){
            ToastUtils.showShort("通信地址不能为空");
            return;
        }

//
//        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)
//                || TextUtils.isEmpty(contactCard)
//                || TextUtils.isEmpty(cityCode)
//                || TextUtils.isEmpty(provinceCode)
//                || TextUtils.isEmpty(address)){
//            ToastUtils.showShort(R.string.input_remind);
//            return;
//        }
//        if(!TextUtils.isEmpty(phone)&&!CommonUtils.isPhoneNum(phone)){
//            ToastUtils.showShort(R.string.input_remind1);
//            return;
//        }
//        if(!TextUtils.isEmpty(contactCard)&&!CommonUtils.isCertificate(contactCard)){
//            ToastUtils.showShort(R.string.input_remind2);
//            return;
//        }

        saveMsg(name,businessLicenseNo,contactPerson,phone,contactCard,cityCode,provinceCode,address,province,city,provincePosition,cityPosition);
        //跳到下一页
        startActivity(new Intent(getContext(),EnterpriseCertificationUploadActivity.class));
    }

    /**
     * 存储认证信息
     * @param name
     * @param businessLicenseNo
     * @param contactPerson
     * @param phone
     * @param contactCard
     * @param cityCode
     * @param provinceCode
     * @param address
     */
    private void saveMsg(String name,String businessLicenseNo,String contactPerson,String phone,
                         String contactCard,String cityCode,String provinceCode,String address,String province,String city,int provincePosition,int cityPosition){
        CertificationBean certificationBean = new CertificationBean();
        certificationBean.setName(name);
        certificationBean.setBusinessLicenseNo(businessLicenseNo);
        certificationBean.setContactPerson(contactPerson);
        certificationBean.setContactPhone(phone);
        certificationBean.setContactCard(contactCard);
        certificationBean.setCityCode(cityCode);
        certificationBean.setProvinceCode(provinceCode);
        certificationBean.setAddress(address);
        certificationBean.setType(1);
        certificationBean.setUserId(Integer.parseInt(UserDataManeger.getInstance().getUserId()));
        certificationBean.setProvince(province);
        certificationBean.setCity(city);
        certificationBean.setCityPosition(cityPosition);
        certificationBean.setProvincePosition(provincePosition);

        //只能使用List的子类
        //注意：一定要序列化
        if(dataListCatch == null){
            dataListCatch = new ArrayList<>();
        }
        if(curCach != -1){
            dataListCatch.remove(curCach);
        }
        dataListCatch.add(certificationBean);
        aCache.put(SharedPreferencesTool.CERTIFICATE_KEY, dataListCatch);
    }

    @Override
    public void onPause() {
        super.onPause();
        getInputMsg();
        saveMsg(name,businessLicenseNo,contactPerson,phone,contactCard,cityCode,provinceCode,address,province,city,provincePosition,cityPosition);
    }
    private void initView(View view){
        aCache = ACache.get(getContext());
//        path = getExternalCacheDir().getAbsolutePath();
//        aCache = ACache.get(new File(path));//设置存储路径用于手动清空缓存使用，
        mEnterpriseCertificatePresent = new CertificatePresent(this);
        tvChooseProvince = view.findViewById(R.id.tvChooseProvince);
        ivChooseProvince = view.findViewById(R.id.ivChooseProvince);
        tvChooseCity = view.findViewById(R.id.tvChooseCity);
        province_position = view.findViewById(R.id.province_position);
        city_position = view.findViewById(R.id.city_position);
        btNext = view.findViewById(R.id.btNext);
        enterpriseName = view.findViewById(R.id.enterpriseName);
        businessNum = view.findViewById(R.id.businessNum);
        contact = view.findViewById(R.id.contact);
        contactPhone = view.findViewById(R.id.contactPhone);
        contactIdNum = view.findViewById(R.id.contactIdNum);
        enterpriseAddress = view.findViewById(R.id.enterpriseAddress);
        province_code = view.findViewById(R.id.province_code);
        city_code = view.findViewById(R.id.city_code);
        enterpriseName = view.findViewById(R.id.enterpriseName);
        businessNum = view.findViewById(R.id.businessNum);
        contact = view.findViewById(R.id.contact);
        contactPhone = view.findViewById(R.id.contactPhone);
        contactIdNum = view.findViewById(R.id.contactIdNum);
        enterpriseAddress = view.findViewById(R.id.enterpriseAddress);
        ivChooseCity = view.findViewById(R.id.ivChooseCity);
        linearProvince = view.findViewById(R.id.linearProvince);
        linearCity = view.findViewById(R.id.linearCity);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linearProvince:
                showProvinceWheel(areaOuts);
                break;
            case R.id.linearCity:
                if(provincePosition==-1){
                    ToastUtils.showShort("请先选择省");
                }else{
                    showCityWheel();
                }
                break;
            case R.id.btNext:
                next();
                break;
        }
    }
    /**
     * 获取省市列表
     */
    private void getProvinceCity(){
        if(areaOuts == null || areaOuts.size()<= 0){
            mEnterpriseCertificatePresent.getProvinceCityData();
        }
    }


    @Override
    public void getProvinceCity(List<AreaOut> areaOuts) {
        if(areaOuts!=null && areaOuts.size()>0){
            this.areaOuts = areaOuts;
            SharedPreferencesTool.getInstance().setObject(areaOuts,SharedPreferencesTool.AREA_KEY);
//            showProvinceWheel(areaOuts);
        }
    }

    @Override
    public void getCertificateMsg(CertificationOut certificationOut) {

    }

    /**
     * 选择省
     */
    private void showProvinceWheel(List<AreaOut> areaOutList){
        final ProvinceCityDialog dialog = new ProvinceCityDialog(getContext(), R.style.Theme_Light_Dialog);
        dialog.setUpData(areaOutList);
        dialog.show();
        dialog.setOnchangeListener(new ProvinceCityDialog.ChangeListener() {
            @Override
            public void onChangeListener(AreaOut area,int position) {
                if(isChange(position)){
                    city_position.setText("");
                    cityPosition = -1;
                    city_code.setText("");
                    tvChooseCity.setText(getString(R.string.choose_city));
                }
                tvChooseProvince.setText(area.getName());
                province_position.setText(position+"");
                province_code.setText(area.getCode());
                provincePosition = position;
                dialog.dismiss();
            }
        });
        String str = province_position.getText().toString();
        if(!TextUtils.isEmpty(str)){
            dialog.setSelect(Integer.parseInt(str));
        }else{
            tvChooseProvince.setText(getString(R.string.choose_province));
        }
    }

    /**
     * 选择市
     */
    private void showCityWheel(){
        areas = areaOuts.get(provincePosition).getList();
        if(areas!=null){
            final CityDialog dialog = new CityDialog(getContext(), R.style.Theme_Light_Dialog);
            dialog.setUpData(areas);
            dialog.show();
            dialog.setOnchangeListener(new CityDialog.ChangeListener() {
                @Override
                public void onChangeListener(Area area, int position) {
                    tvChooseCity.setText(area.getName());
                    city_position.setText(position+"");
                    city_code.setText(area.getCode());
                    cityPosition = position;
                    dialog.dismiss();
                }
            });
            String str = city_position.getText().toString();
            if(!TextUtils.isEmpty(str)){
                dialog.setSelect(Integer.parseInt(str));
            }else{
                tvChooseCity.setText(getString(R.string.choose_city));
            }
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
    @Override
    public void onDestroyView() {
//        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
