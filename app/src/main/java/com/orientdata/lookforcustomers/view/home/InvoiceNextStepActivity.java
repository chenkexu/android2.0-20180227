package com.orientdata.lookforcustomers.view.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.WangrunBaseActivity;
import com.orientdata.lookforcustomers.bean.Area;
import com.orientdata.lookforcustomers.bean.ErrBean;
import com.orientdata.lookforcustomers.bean.InvoiceAreaOut;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;
import com.orientdata.lookforcustomers.util.CommonUtils;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.orientdata.lookforcustomers.widget.dialog.InvoiceConfirmSubmitDialog;
import com.orientdata.lookforcustomers.widget.dialog.ProvinceCityCountyDialog;
import com.orientdata.lookforcustomers.widget.dialog.SubmitFeedBackDialog;

import java.math.BigDecimal;
import java.util.List;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

import static com.orientdata.lookforcustomers.R.array.city;

/**
 * 发票下一步
 */
public class InvoiceNextStepActivity extends WangrunBaseActivity implements View.OnClickListener {
    private MyTitle title;
    private RelativeLayout rl_e_invoice;
    private TextView tv_e_invoice;
    private ImageView iv_e_invoice;
    private RelativeLayout rl_p_invoice;
    private TextView tv_p_invoice;
    private ImageView iv_p_invoice;
    private int isEOrP = 1;//1电子发票，2，纸质发票。默认选择电子发票

    private LinearLayout ll_p_consignee_information;//纸质发票收件信息
    private LinearLayout ll_e_consignee_information;//电子发票收件信息
    private LinearLayout ll_main;
    private LinearLayout ll_button;
    List<Area> mProvinces = null;
    List<Area> mCitys = null;
    List<Area> mCountys = null;
    private int provincePosition = -1;
    private int cityPosition = -1;
    private int countyPosition = -1;
    private TextView tv_province;
    private TextView tv_city;
    private TextView tv_county;
    private RelativeLayout rl_provice;
    private RelativeLayout rl_city;
    private RelativeLayout rl_county;

    private TextView province_position;
    private TextView city_position;
    private TextView county_position;
    private TextView province_code;
    private TextView city_code;
    private TextView county_code;

    //    抬头类型
    private RelativeLayout rl_invoice_title_enterprise;
    private ImageView iv_invoice_title_enterprise;
    private TextView tv_invoice_title_enterprise;
    private RelativeLayout rl_invoice_title_personal;
    private ImageView iv_invoice_title_personal;
    private TextView tv_invoice_title_personal;
    private int isEnterpriseOrpersonal = 1;//1 企业 2 个人 ，默认选择公司抬头

    private EditText et_title;//发票抬头
    private EditText et_id;//纳税人识别号
    private TextView tv_content;//发票内容
    private TextView tv_money;//发票金额
    private EditText et_email;//电子邮件
    private EditText et_person_name;//收件人
    private EditText et_person_phone;//收件人电话
    private EditText et_address;//地址
    private TextView tv_submit;
    private BigDecimal money;
    private String taskIds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_next_step);
        if (getIntent() != null) {
            money = (BigDecimal) getIntent().getSerializableExtra("money");
            taskIds = getIntent().getStringExtra("taskIds");
        }
        initView();
        initTitle();
        getData(1, "");


    }

    //type是 int 1-省 2市 3区
    private void getData(final int type, String code) {
        showDefaultLoading();
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());
        if (type != 1) {
            map.put("code", code);
        }
        map.put("type", type + "");


        OkHttpClientManager.postAsyn(HttpConstant.SELECT_AREA_LISTS, new OkHttpClientManager.ResultCallback<InvoiceAreaOut>() {
            @Override
            public void onError(Exception e) {
                ToastUtils.showShort(e.getMessage());
                hideDefaultLoading();
            }

            @Override
            public void onResponse(InvoiceAreaOut response) {
                if (response.getCode() == 0) {
                    if (response.getResult() == null) {
                        return;
                    }
                    switch (type) {
                        case 1:
                            mProvinces = response.getResult();
                            break;
                        case 2:
                            mCitys = response.getResult();
                            break;
                        case 3:
                            mCountys = response.getResult();
                            break;
                    }
                }
                hideDefaultLoading();
            }
        }, map);
    }

    /**
     * 省
     */
    private void showProvinceWheel(List<Area> areas) {
        final ProvinceCityCountyDialog dialog = new ProvinceCityCountyDialog(this, R.style.Theme_Light_Dialog);
        dialog.setUpData(areas);
        dialog.show();
        dialog.setOnchangeListener(new ProvinceCityCountyDialog.ChangeListener() {
            @Override
            public void onChangeListener(Area area, int position) {
                if (isProvinceChange(position)) {
                    city_position.setText("");
                    cityPosition = -1;
                    city_code.setText("");
                    tv_city.setText("市");
                }
                tv_province.setText(area.getName());
                province_position.setText(position + "");
                province_code.setText(area.getCode());
                provincePosition = position;
                dialog.dismiss();
                getData(2, mProvinces.get(provincePosition).getCode().trim());
            }
        });
        String str = province_position.getText().toString();
        if (!TextUtils.isEmpty(str)) {
            dialog.setSelect(Integer.parseInt(str));
        } else {
            tv_province.setText("省");
        }
    }

    private void showCityWheel() {

        //areas = areaOuts.get(provincePosition).getList();
        if (mCitys != null) {
            final ProvinceCityCountyDialog dialog = new ProvinceCityCountyDialog(this, R.style.Theme_Light_Dialog);
            dialog.setUpData(mCitys);
            dialog.show();
            dialog.setOnchangeListener(new ProvinceCityCountyDialog.ChangeListener() {
                @Override
                public void onChangeListener(Area area, int position) {
                    if (isCityChange(position)) {
                        county_position.setText("");
                        countyPosition = -1;
                        county_code.setText("");
                        tv_county.setText("区");
                    }
                    tv_city.setText(area.getName());
                    city_position.setText(position + "");
                    city_code.setText(area.getCode());
                    cityPosition = position;
                    dialog.dismiss();
                    getData(3, mCitys.get(cityPosition).getCode().trim());
                }
            });
            String str = city_position.getText().toString();
            if (!TextUtils.isEmpty(str)) {
                dialog.setSelect(Integer.parseInt(str));
            } else {
                tv_city.setText("市");
            }
        }
    }

    private void showCountyWheel() {

        //areas = areaOuts.get(provincePosition).getList();
        if (mCountys != null) {
            final ProvinceCityCountyDialog dialog = new ProvinceCityCountyDialog(this, R.style.Theme_Light_Dialog);
            dialog.setUpData(mCountys);
            dialog.show();
            dialog.setOnchangeListener(new ProvinceCityCountyDialog.ChangeListener() {
                @Override
                public void onChangeListener(Area area, int position) {

                    tv_county.setText(area.getName());
                    county_position.setText(position + "");
                    county_code.setText(area.getCode());
                    countyPosition = position;
                    dialog.dismiss();
                    //getData(3,mCitys.get(cityPosition).getCode().trim());
                }
            });
            String str = county_position.getText().toString();
            if (!TextUtils.isEmpty(str)) {
                dialog.setSelect(Integer.parseInt(str));
            } else {
                tv_county.setText("区");
            }
        }
    }

    /**
     * 省选择是否发生变化
     *
     * @param position
     * @return
     */
    private boolean isProvinceChange(int position) {
        String str = province_position.getText().toString();
        if (!TextUtils.isEmpty(str)) {
            if (Integer.parseInt(str) != position) {
                return true;
            }
        }
        return false;
    }

    /**
     * 省选择是否发生变化
     *
     * @param position
     * @return
     */
    private boolean isCityChange(int position) {
        String str = city_position.getText().toString();
        if (!TextUtils.isEmpty(str)) {
            if (Integer.parseInt(str) != position) {
                return true;
            }
        }
        return false;
    }


    private void initView() {
        title = findViewById(R.id.my_title);
        rl_e_invoice = findViewById(R.id.rl_e_invoice);
        rl_e_invoice.setOnClickListener(this);
        tv_e_invoice = findViewById(R.id.tv_e_invoice);
        iv_e_invoice = findViewById(R.id.iv_e_invoice);
        rl_p_invoice = findViewById(R.id.rl_p_invoice);
        rl_p_invoice.setOnClickListener(this);
        tv_p_invoice = findViewById(R.id.tv_p_invoice);
        iv_p_invoice = findViewById(R.id.iv_p_invoice);
        ll_e_consignee_information = findViewById(R.id.ll_e_consignee_information);
        ll_p_consignee_information = findViewById(R.id.ll_p_consignee_information);
        ll_main = findViewById(R.id.ll_main);
        ll_button = findViewById(R.id.ll_button);
        province_position = findViewById(R.id.province_position);
        province_code = findViewById(R.id.province_code);
        city_code = findViewById(R.id.city_code);
        city_position = findViewById(R.id.city_position);
        county_code = findViewById(R.id.county_code);
        county_position = findViewById(R.id.county_position);
        tv_province = findViewById(R.id.tv_province);
        tv_city = findViewById(R.id.tv_city);
        tv_county = findViewById(R.id.tv_county);
        rl_provice = findViewById(R.id.rl_province);
        rl_provice.setOnClickListener(this);
        rl_city = findViewById(R.id.rl_city);
        rl_city.setOnClickListener(this);
        rl_county = findViewById(R.id.rl_county);
        rl_county.setOnClickListener(this);
        //    抬头类型
        rl_invoice_title_enterprise = findViewById(R.id.rl_invoice_title_enterprise);
        rl_invoice_title_enterprise.setOnClickListener(this);
        iv_invoice_title_enterprise = findViewById(R.id.iv_invoice_title_enterprise);
        tv_invoice_title_enterprise = findViewById(R.id.tv_invoice_title_enterprise);
        rl_invoice_title_personal = findViewById(R.id.rl_invoice_title_personal);
        rl_invoice_title_personal.setOnClickListener(this);
        iv_invoice_title_personal = findViewById(R.id.iv_invoice_title_personal);
        tv_invoice_title_personal = findViewById(R.id.tv_invoice_title_personal);

        //填写信息
        et_title = findViewById(R.id.et_title);//发票抬头
        et_id = findViewById(R.id.et_id);//纳税人识别号
        tv_content = findViewById(R.id.tv_content);//发票内容
        tv_money = findViewById(R.id.tv_money);//发票金额
        if (money != null) {
            tv_money.setText("" + money);
        }
        et_email = findViewById(R.id.et_email);//电子邮件
        et_person_name = findViewById(R.id.et_person_name);//收件人
        et_person_phone = findViewById(R.id.et_person_phone);//收件人电话
        et_address = findViewById(R.id.et_address);//地址
        tv_submit = findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(this);
    }

    private void initTitle() {
        title.setTitleName("按任务开发票");
        title.setImageBack(this);
       /* title.setRightText("开票记录");
        title.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivityForResult(new Intent(getBaseContext(), InvoiceHistoryActivity.class), 1);
            }
        });*/
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_e_invoice: //选择电子发票
                isEOrP = 1;
                rl_e_invoice.setBackground(getResources().getDrawable(R.drawable.bac_rec_border));
                iv_e_invoice.setBackgroundResource(R.mipmap.check_invoice);
                tv_e_invoice.setTextColor(getResources().getColor(R.color.colorPrimary));

                rl_p_invoice.setBackgroundResource(R.mipmap.kuang_invoice_unchecked);
                iv_p_invoice.setBackgroundResource(R.mipmap.no_check_invoice);
                tv_p_invoice.setTextColor(getResources().getColor(R.color.text_gray));

                ll_main.setVisibility(View.VISIBLE);
                ll_e_consignee_information.setVisibility(View.VISIBLE);
                ll_p_consignee_information.setVisibility(View.GONE);
                ll_button.setVisibility(View.VISIBLE);
                break;
            case R.id.rl_p_invoice: //选择纸质发票
                isEOrP = 2;
                rl_p_invoice.setBackground(getResources().getDrawable(R.drawable.bac_rec_border));
                iv_p_invoice.setBackgroundResource(R.mipmap.check_invoice);
                tv_p_invoice.setTextColor(getResources().getColor(R.color.colorPrimary));

                rl_e_invoice.setBackgroundResource(R.mipmap.kuang_invoice_unchecked);
                iv_e_invoice.setBackgroundResource(R.mipmap.no_check_invoice);
                tv_e_invoice.setTextColor(getResources().getColor(R.color.text_gray));

                ll_main.setVisibility(View.VISIBLE);
                ll_e_consignee_information.setVisibility(View.GONE);
                ll_p_consignee_information.setVisibility(View.VISIBLE);
                ll_button.setVisibility(View.VISIBLE);
                break;
            case R.id.rl_province:
                if (mProvinces != null && mProvinces.size() > 0) {
                    showProvinceWheel(mProvinces);
                }
                break;
            case R.id.rl_city:
                if (mCitys != null && mCitys.size() > 0) {
                    showCityWheel();
                }
                break;
            case R.id.rl_county:
                if (mCountys != null && mCountys.size() > 0) {
                    showCountyWheel();
                }
                break;
            case R.id.rl_invoice_title_personal: //选择个人非企业单位
                isEnterpriseOrpersonal = 2;
                iv_invoice_title_personal.setBackgroundResource(R.mipmap.check_invoice);
                tv_invoice_title_personal.setTextColor(getResources().getColor(R.color.colorPrimary));
                iv_invoice_title_enterprise.setBackgroundResource(R.mipmap.no_check_invoice);
                tv_invoice_title_enterprise.setTextColor(getResources().getColor(R.color.text_gray));
                break;
            case R.id.rl_invoice_title_enterprise: //公司抬头
                isEnterpriseOrpersonal = 1;
                iv_invoice_title_personal.setBackgroundResource(R.mipmap.no_check_invoice);
                tv_invoice_title_personal.setTextColor(getResources().getColor(R.color.text_gray));
                iv_invoice_title_enterprise.setBackgroundResource(R.mipmap.check_invoice);
                tv_invoice_title_enterprise.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.tv_submit:
                showRemindDialog();
                break;
        }

    }

    private void showRemindDialog() {
        String lookUp = et_title.getText().toString().trim();
        if (TextUtils.isEmpty(lookUp)) {
            ToastUtils.showShort("发票抬头不能为空");
            return;
        }
        String taxpayerIdentificationNo = et_id.getText().toString().trim();
        if (isEnterpriseOrpersonal == 1) {
            //企业
            if (TextUtils.isEmpty(taxpayerIdentificationNo)) {
                ToastUtils.showShort("纳税人识别号不能为空");
                return;
            }
        }
        String email = "";
        String adresser = "";
        String phone = "";
        String adress = "";
        String invoiceInfo2 = "";
        if (isEOrP == 1) {//电子发票
            email = et_email.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                ToastUtils.showShort("电子邮件不能为空");
                return;
            }
            if (!CommonUtils.isEmail(email)){
                ToastUtils.showShort("请输入正确的电子邮箱地址");
                return;
            }
            invoiceInfo2 = "电子邮箱：" + email + "";

        } else if (isEOrP == 2) {


            adresser = et_person_name.getText().toString().trim();
            if (TextUtils.isEmpty(adresser)) {
                ToastUtils.showShort("收件人人不能为空");
                return;
            }
            phone = et_person_phone.getText().toString().trim();
            if (TextUtils.isEmpty(phone)) {
                ToastUtils.showShort("联系电话不能为空");
                return;
            }

            String province = tv_province.getText().toString().trim();
            if (province.equals("省")) {
                ToastUtils.showShort("请选择省");
                return;
            }
            String city = tv_city.getText().toString().trim();
            if (city.equals("市")) {
                ToastUtils.showShort("请选择市");
                return;
            }
            String county = tv_county.getText().toString().trim();
            if (county.equals("区")) {
                ToastUtils.showShort("请选择区");
                return;
            }
            String addressDetail = et_address.getText().toString().trim();
            adress = province + city + county + addressDetail;
            if (TextUtils.isEmpty(addressDetail)) {
                ToastUtils.showShort("详细地址不能为空");
                return;
            }

            invoiceInfo2 = "收件人：" + adresser + "\n"
                    + "电话：" + phone + "\n"
                    + "地址：" + adress + "\n"
                    + "运费：到付"
            ;
        }
        String invoiceInfo = "发票类型：" + (isEOrP == 1 ? "电子发票" : "纸质发票") + "\n"
                + "发票抬头：" + lookUp + "\n"
                + "纳税人识别号：" + taxpayerIdentificationNo + "";

        final MDBasicRequestMap map = new MDBasicRequestMap();


        map.put("userId", UserDataManeger.getInstance().getUserId());
        map.put("invoiceType", isEOrP + "");
        map.put("userType", isEnterpriseOrpersonal + "");
        map.put("lookUp", lookUp);
        map.put("taxpayerIdentificationNo", taxpayerIdentificationNo);
        if (isEOrP == 1) {
            map.put("email", email);
        } else if (isEOrP == 2) {
            map.put("adresser", adresser);
            map.put("phone", phone);
            map.put("adress", adress);
        }
        map.put("taskIds", taskIds);

        final InvoiceConfirmSubmitDialog dialog = new InvoiceConfirmSubmitDialog(this, invoiceInfo, invoiceInfo2, isEOrP);
        dialog.setClickListenerInterface(new InvoiceConfirmSubmitDialog.ClickListenerInterface() {
            @Override
            public void doCancel() {
                dialog.dismiss();
            }

            @Override
            public void doConfirm() {
                dialog.dismiss();
                submit(map);
            }
        });
        dialog.show();
    }

    private void submit(MDBasicRequestMap map) {
        showDefaultLoading();

        OkHttpClientManager.postAsyn(HttpConstant.UPDATE_INVOICE, new OkHttpClientManager.ResultCallback<ErrBean>() {
            @Override
            public void onError(Exception e) {
                hideDefaultLoading();
                ToastUtils.showShort(e.getMessage());
            }

            @Override
            public void onResponse(ErrBean response) {
                hideDefaultLoading();
                if (response == null) {
                    return;
                }

                showSubmitFeedbackDialog(response.getCode());

            }
        }, map);


    }

    private void showSubmitFeedbackDialog(final int code) {
        String submitStatus = "";
        String confirmText = "";
        int imgRes = 0;
        if (code == 0) {
            //成功
            submitStatus = getString(R.string.sub_suc);
            confirmText = getString(R.string.sub_confirm);
            imgRes = R.mipmap.icon_dialog_success;

        } else {
            //失败
            submitStatus = getString(R.string.sub_fail);
            confirmText = getString(R.string.sub_fail_txt);
            imgRes = R.mipmap.icon_dialog_error;

        }
        final SubmitFeedBackDialog submitFeedBackDialog = new SubmitFeedBackDialog(this, submitStatus, confirmText, imgRes);
        submitFeedBackDialog.show();
        submitFeedBackDialog.setClickListenerInterface(new SubmitFeedBackDialog.ClickListenerInterface() {
            @Override
            public void doCertificate() {
                //消失所有的页面 到首页
                if (code == 0) {
                    submitFeedBackDialog.dismiss();
                    finish();
                } else {
                    submitFeedBackDialog.dismiss();
                }

                //closeActivity(UploadOfflineRechargeReceiptActivity.class,CertificationActivity.class);
            }
        });
    }
}
