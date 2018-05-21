package com.orientdata.lookforcustomers.view.findcustomer.impl;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.ErrBean;
import com.orientdata.lookforcustomers.bean.MessageTaskCacheBean;
import com.orientdata.lookforcustomers.bean.MyInfoBean;
import com.orientdata.lookforcustomers.bean.SelectWordBean;
import com.orientdata.lookforcustomers.bean.SettingOut;
import com.orientdata.lookforcustomers.bean.UploadPicBean;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;
import com.orientdata.lookforcustomers.presenter.TaskPresent;
import com.orientdata.lookforcustomers.runtimepermissions.PermissionsManager;
import com.orientdata.lookforcustomers.util.DateTool;
import com.orientdata.lookforcustomers.util.RegexUtils;
import com.orientdata.lookforcustomers.util.SharedPreferencesTool;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.certification.fragment.ACache;
import com.orientdata.lookforcustomers.view.findcustomer.CreateFindCustomerActivity;
import com.orientdata.lookforcustomers.view.findcustomer.ITaskView;
import com.orientdata.lookforcustomers.view.findcustomer.TestPhoneSettingActivity;
import com.orientdata.lookforcustomers.view.home.imple.HomeActivity;
import com.orientdata.lookforcustomers.widget.DateSelectPopupWindow;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.orientdata.lookforcustomers.widget.dialog.ConfirmSubmitDialog;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

import static com.orientdata.lookforcustomers.R.id.et_budget;


/**
 * Created by wy on 2017/11/27.
 * 创建短信任务
 */

public class MessageTaskActivity extends BaseActivity<ITaskView, TaskPresent<ITaskView>> implements ITaskView, View.OnClickListener {


    @BindView(R.id.et_enterprise_signature)
    EditText etEnterpriseSignature; //输入的签名

    @BindView(R.id.tv_message_sign)
    TextView tvMessageSign; //内容签名

    @BindView(R.id.tv_unsubscribe)
    TextView tvUnsubscribe; //退订

//    @BindView(R.id.tv_hint)
//    TextView tvhint; //企业签名hint

    @BindView(R.id.tv_message_content_hint)
    TextView tvMessageContentHint;//短信内容hint
    @BindView(et_budget)
    EditText etBudget;


    private MyTitle titleMsg;
    private EditText etMsgContent;
    private TextView tvCreate;
    private TextView tvNum;
    private RelativeLayout date_from;
    private RelativeLayout date_to;
    private TextView tvDateFrom;
    private TextView tvDateTo;
    //From CreateFindCustomerActivity
    private String ageF;
    private String ageB;
    private String educationLevelF = "";
    private String educationLevelB = "";
    private String sex = "";
    private String consumptionCapacityF = "";
    private String consumptionCapacityB = "";
    private String ascription = "";
    private String phoneModelIds = "";
    private String interestIds = "";
    private String cityCode = "";
    private String throwAddress = "";
    private int type;
    private String taskName;
    private String rangeRadius;

    private String budget;

    private String longitude;
    private String dimension;
    private String testCmPhone = "";//移动测试号
    private String testCuPhone = "";//联通测试号
    private String testCtPhone = "";//电信测试号
    private int day; //限制天数
    private String mCityName;
    private String smsPrice = "";//短信单价
    private TextView tvCoverage;
    private Context mContext;
    private boolean isSubmitting = false;
    private int numCount;//
    private Map<String, Object> mMapInfoBeans;
    private String mProvinceCode; //省Code
    private String content;
    private String enddate;
    private String startdate;
    private String industryMark;
    private String industryNameStr;
    private double minMoney;
    private String signStr;
    private String contentAll;
    private MessageTaskCacheBean messageTaskCacheBean;
    private boolean isReCreate;

    @Override
    public void onBackPressed() {
        //签名
        //短信内容
        //开始时间.结束时间
        //任务预算
        obtainData();
        String remindString = "";
        if (!TextUtils.isEmpty(testCmPhone)||!TextUtils.isEmpty(testCuPhone)||!TextUtils.isEmpty(testCtPhone)) {
            remindString = "测试号码无法保存，再次创建任务需重新输入测试号码";
        }else{
            remindString = "";
        }

        final ConfirmSubmitDialog dialog = new ConfirmSubmitDialog(this, "是否保存当前内容",remindString);
        dialog.setClickListenerInterface(new ConfirmSubmitDialog.ClickListenerInterface() {
            @Override
            public void doCancel() {
                dialog.dismiss();
                SharedPreferencesTool.getInstance().remove(SharedPreferencesTool.MessageTaskCacheBean);
                finish();
            }

            @Override
            public void doConfirm() {
                dialog.dismiss();
                savaCache();
                finish();
            }
        });
        dialog.show();
//        if (!startdate.equals("开始日期")||!enddate.equals("结束日期")||!TextUtils.isEmpty(content)
//                ||!TextUtils.isEmpty(budget)||!TextUtils.isEmpty(signStr) ||!TextUtils.isEmpty(testCmPhone)||!TextUtils.isEmpty(testCuPhone)
//                ||!TextUtils.isEmpty(testCtPhone)) {
//            dialog.show();
//        }else{
//            finish();
//        }
//        final ConfirmSubmitDialog dialog = new ConfirmSubmitDialog(this, "温馨提示", "短信内容确定保存吗");
//        dialog.setClickListenerInterface(new ConfirmSubmitDialog.ClickListenerInterface() {
//            @Override
//            public void doCancel() {
//
//            }
//
//            @Override
//            public void doConfirm() {
//
//            }
//        });

    }



    private void savaCache() {
        // TODO: 2018/5/3 存储到缓存文件中
        MessageTaskCacheBean messageTaskCacheBean  = (MessageTaskCacheBean) SharedPreferencesTool.getInstance().getObjectFromShare(SharedPreferencesTool.MessageTaskCacheBean);
        if (messageTaskCacheBean == null) {
             messageTaskCacheBean = new MessageTaskCacheBean();
        }
        messageTaskCacheBean.setSignStr(signStr);
        messageTaskCacheBean.setStartdate(startdate);
        messageTaskCacheBean.setEnddate(enddate);
        messageTaskCacheBean.setBudget(budget);
        messageTaskCacheBean.setContent(content);
        SharedPreferencesTool.getInstance().setObject(messageTaskCacheBean, SharedPreferencesTool.MessageTaskCacheBean);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_task);
        ButterKnife.bind(this);
        this.mContext = this;
        initIntentData();
        initView();
        initTitle();
        initDate();
        //设置签名信息
        intSignContent();
    }

    private void intSignContent() {

//        if (!isReCreate) { //不是再次创建
            messageTaskCacheBean = (MessageTaskCacheBean) SharedPreferencesTool.getInstance().getObjectFromShare(SharedPreferencesTool.MessageTaskCacheBean);
//        }
        if (messageTaskCacheBean!=null) {
            //设置开始时间
            if (!TextUtils.isEmpty(messageTaskCacheBean.getStartdate())) {
                tvDateFrom.setText(messageTaskCacheBean.getStartdate());
            }
            //设置结束时间
            if (!TextUtils.isEmpty(messageTaskCacheBean.getEnddate())) {
                tvDateTo.setText(messageTaskCacheBean.getEnddate());
            }
            if (!TextUtils.isEmpty(messageTaskCacheBean.getBudget())) {
                etBudget.setText(messageTaskCacheBean.getBudget());
            }
        }

            showDefaultLoading();
            MDBasicRequestMap map = new MDBasicRequestMap();
            map.put("userId", UserDataManeger.getInstance().getUserId());
            map.put("provincecode", mProvinceCode);
            OkHttpClientManager.postAsyn(HttpConstant.GET_SIGN_AND_TD, new OkHttpClientManager.ResultCallback<MyInfoBean>() {
                @Override
                public void onError(Exception e) {
                    ToastUtils.showShort(e.getMessage());
                    hideDefaultLoading();
                }
                @Override
                public void onResponse(MyInfoBean response) {
                    if (response.getCode() == 0) {
                        if (response.getResult() == null || response.getResult().size() <= 0) {
                            hideDefaultLoading();
                            return;
                        }

                        double id = (double) response.getResult().get("id");
                        String provincecode = (String) response.getResult().get("provincecode");
                        double signstate = (double) response.getResult().get("signstate");
                        String tdcontent = (String) response.getResult().get("tdcontent");
                        tvUnsubscribe.setText(tdcontent); //设置回复退订
                        tvNum.setText(tdcontent.length() + "");

                        // TODO: 2018/5/3 设置缓存信息
                        if ((int) signstate == 0) { //自定义
                            if (messageTaskCacheBean!=null) { //有缓存信息
                                Logger.d(messageTaskCacheBean.toString());
                                if (!TextUtils.isEmpty(messageTaskCacheBean.getSignStr())) {
                                    etEnterpriseSignature.setText(messageTaskCacheBean.getSignStr());
                                    tvMessageSign.setText(etEnterpriseSignature.getText().toString());
                                }
                                if (!TextUtils.isEmpty(messageTaskCacheBean.getContent())) {
                                    etMsgContent.setText(messageTaskCacheBean.getContent());
                                    tvMessageSign.setText(etEnterpriseSignature.getText().toString());
                                }
                            }
                        } else { //固定签名
                            String sign = (String) response.getResult().get("sign");
                            etEnterpriseSignature.setText(sign);
                            setEnable();
                            tvMessageSign.setText(etEnterpriseSignature.getText().toString());
                        }
                        Logger.d("-----" + provincecode + signstate + tdcontent);

                    }
                    hideDefaultLoading();
                }
            }, map);

    }

    private void setEnable() {
        etEnterpriseSignature.setEnabled(false);
        etEnterpriseSignature.setFocusable(false);
        etEnterpriseSignature.setKeyListener(null);//重点
    }


    private void initIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            ageF = intent.getStringExtra("ageF");
            ageB = intent.getStringExtra("ageB");
//            educationLevelF = intent.getStringExtra("educationLevelF");
//            educationLevelB = intent.getStringExtra("educationLevelB");
            sex = intent.getStringExtra("sex");
//            consumptionCapacityF = intent.getStringExtra("consumptionCapacityF");
//            consumptionCapacityB = intent.getStringExtra("consumptionCapacityB");
//            ascription = intent.getStringExtra("ascription");
//            phoneModelIds = intent.getStringExtra("phoneModelIds");
            interestIds = intent.getStringExtra("interestIds");
            cityCode = intent.getStringExtra("cityCode");
            throwAddress = intent.getStringExtra("throwAddress");
            type = intent.getIntExtra("type", 1);
            taskName = intent.getStringExtra("taskName");
            rangeRadius = intent.getStringExtra("rangeRadius");

//            budget = intent.getStringExtra("budget");

            longitude = intent.getStringExtra("longitude");
            smsPrice = intent.getStringExtra("smsPrice");
            dimension = intent.getStringExtra("dimension");
            day = intent.getIntExtra("day", 0);
            mCityName = intent.getStringExtra("cityName");
            mProvinceCode = intent.getStringExtra("mProvinceCode");

            industryMark = intent.getStringExtra("industryMark");
            industryNameStr = intent.getStringExtra("industryNameStr");
            isReCreate = intent.getBooleanExtra("isReCreate",false);


            minMoney = intent.getDoubleExtra("minMoney",1000);
            // TODO: 2018/4/23 测试号也得传过来
            Logger.d("省Code:---" + mProvinceCode);
            // TODO: 2018/4/7 这个地方先去掉单价 
//            updateView();//后台传过来的数据
        }
    }


    private void updateView() {
        //预算／单价
//        tvCoverage.setText("预计最大可覆盖人数(人)：" + (int) (Integer.parseInt(budget) / Double.parseDouble(smsPrice)));
    }

    private void initView() {
        titleMsg = (MyTitle) findViewById(R.id.titleMsg);
        tvCreate = (TextView) findViewById(R.id.tvCreate);
        etMsgContent = findViewById(R.id.etMsgContent);
        tvNum = (TextView) findViewById(R.id.tvNum);
        date_from = (RelativeLayout) findViewById(R.id.date_from);
        date_to = (RelativeLayout) findViewById(R.id.date_to);
        tvDateFrom = date_from.findViewById(R.id.tvLeftText);
        tvDateTo = date_to.findViewById(R.id.tvLeftText);
        tvCoverage = findViewById(R.id.tvCoverage);
        date_from.setOnClickListener(this);
        date_to.setOnClickListener(this);
        tvCreate.setOnClickListener(this);


        etBudget.setHint("请输入任务预算"+minMoney+"元起");

        etEnterpriseSignature.setOnFocusChangeListener(new View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    Logger.d("获取焦点时：");
                    etMsgContent.setEnabled(true);
                } else {
                    Logger.d("失去焦点时焦点时：");
                    // 此处为失去焦点时的处理内容
                    int length = etEnterpriseSignature.getText().length();
                    if (length < 4 || length > 6) {
                        etMsgContent.setEnabled(false);
                        ToastUtils.showShort("企业签名只能输入2-4位汉字");
                    }else{
                        etMsgContent.setEnabled(true);
                    }
                }
            }
        });


        //输入企业签名时
        etEnterpriseSignature.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Logger.d("count:" + count);
                String signContent = s.toString();
                Logger.d("返回企业签名字符串:" + signContent);

                etEnterpriseSignature.removeTextChangedListener(this);
                if (!signContent.startsWith("【") && signContent.endsWith("】") && !signContent.contains("【")) {
                    Logger.d("没有【");
                    etEnterpriseSignature.setText("【"+signContent);
                } else if (!signContent.startsWith("【") && !signContent.endsWith("】")) {
                    Logger.d("没有【】");
                    if (signContent.length() > 4) {
                        signContent.substring(0, 4);
                        etEnterpriseSignature.setText("【" + signContent.substring(0, 4) + "】");
                    } else {
                        etEnterpriseSignature.setText("【" + signContent + "】");
                    }

                } else if (signContent.startsWith("【") && !signContent.endsWith("】") && !signContent.contains("】")) {
                    Logger.d("没有】");
                    etEnterpriseSignature.setText(signContent + "】");
                } else if(signContent.equals("【】")){
                    etEnterpriseSignature.setText("");
                } else {
                    Logger.d("都有");
                    signContent = signContent.replace("【", "");
                    signContent = signContent.replace("】", "");
                    if (signContent.length() > 4) {
                        signContent.substring(0, 4);
                        etEnterpriseSignature.setText("【" + signContent.substring(0, 4) + "】");
                    } else {
                        etEnterpriseSignature.setText("【" + signContent + "】");
                    }
                }
                if (etEnterpriseSignature.getText().toString().length()>1) {
                    etEnterpriseSignature.setSelection(etEnterpriseSignature.getText().toString().length() - 1);
                }
//                设置只能输入2-4位汉字
//                String str = stringFilter1(signContent);
//                if (!signContent.equals(str)) {
//                    //设置内容
//                    etEnterpriseSignature.setText(str);
//                    etEnterpriseSignature.setSelection(str.length());
//                }
                if (etEnterpriseSignature.getText().toString().equals("")) {
                    Logger.d("企业签名为空");
                    tvMessageSign.setText("【请输入企业签名2-4个汉字】");
                    String content = etMsgContent.getText().toString();
                    if (content.contains("【")) { //短信有内容
                        String s2 = content.split("】")[1]; //后面的短信正文内容
                        String s1 = tvMessageSign.getText().toString() + s2.toString();
                        etMsgContent.setText(s1);
                        numCount = s2.length() + tvUnsubscribe.getText().length();
                    }else{ //短信没内容
                        numCount = tvUnsubscribe.getText().length();
                    }
                } else {
                    Logger.d("企业签名不为空");
                    tvMessageSign.setText(etEnterpriseSignature.getText().toString());
                    // TODO: 2018/4/8 bug :只能先输入企业签名再输入短信内容
                    String content = etMsgContent.getText().toString();
                    if (content.contains("【")) { //短信有内容
                        String[] split = content.split("】");
                        String s2 = split[1];
                        String s1 = etEnterpriseSignature.getText().toString() + s2.toString();
                        etMsgContent.setText(s1);
                        numCount = s1.length() + tvUnsubscribe.getText().length();
                    }else { //短信没内容
                        numCount = tvMessageSign.getText().length() + tvUnsubscribe.getText().length();
                    }
                }

                //设置输入短信的长度
//                numCount = tvMessageSign.getText().length() + tvUnsubscribe.getText().length()+etMsgContent.getText().length() ;
                // TODO: 2018/4/17 这里字数统计有bug
                Logger.d("字数为：" + numCount);
                tvNum.setText(numCount + "");
                etMsgContent.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
                etEnterpriseSignature.addTextChangedListener(this);

                if (numCount>70){
                    ToastUtils.showShort("您输入的短信内容超出字数限制");
                }else{

                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });



        //输入短信内容时
        etMsgContent.addTextChangedListener(new TextWatcher() {
            private String preStr = "";
            private int temp;

            @Override
            public void beforeTextChanged(CharSequence s/*之前的文字内容*/, int start/*添加文字的位置(从0开始)*/, int count, int after/*添加的文字总数*/) {

                if (!TextUtils.isEmpty(s.toString())){
                    preStr = s.toString();
                    Logger.d("变化之前的内容："+s.toString());
                }
            }

            @Override
            public void onTextChanged(CharSequence s/*之后的文字内容 */, int start/*添加文字的位置(从0开始)*/, int before/*之前的文字总数*/, int count) {
                if (s.toString() != null) {
                    String messageContent = s.toString();
                    Logger.d("返回的短信的内容：" + messageContent);
                    int length = s.toString().length();
                    tvMessageContentHint.setVisibility(View.GONE);
                    tvMessageSign.setVisibility(View.GONE);
                    int selectionStart = etMsgContent.getSelectionStart();
                    int index = messageContent.indexOf("】");
                    Logger.d("selectionStart:"+selectionStart+"index:"+index);
                    etMsgContent.removeTextChangedListener(this);


                    // TODO: 2018/4/9 删除 []存在bug
                    boolean b = messageContent.contains("】") && !messageContent.contains("【");
                    boolean a = messageContent.contains("【") && !messageContent.contains("】");
                    if ((selectionStart!=0 && selectionStart <= index) || (a) || (b)) { //不可以编辑
                            Logger.d("不可以编辑");
                            if (!TextUtils.isEmpty(preStr)) {
                                if (preStr.contains(etEnterpriseSignature.getText().toString())) {
                                    etMsgContent.setText(preStr);
                                }
                            }
                        }else{
                            Logger.d("可以编辑");
                        // TODO: 2018/5/3  tvMessageSign改成了etEnterpriseSignature
                            if (messageContent.equals(etEnterpriseSignature.getText().toString())) { //如果只有企业签名
                                tvMessageContentHint.setVisibility(View.VISIBLE);
                                tvMessageSign.setVisibility(View.VISIBLE);
                                etMsgContent.setText("");
//                                temp = numCount;
                            } else if (!s.toString().contains(etEnterpriseSignature.getText().toString())) { //如果不包含企业签名（第一次输入）
                                etMsgContent.setText(etEnterpriseSignature.getText().toString() + s.toString());
                                etMsgContent.setSelection(etMsgContent.getText().toString().length());
//                                temp = length + numCount;
                            } else { //如果包含企业签名和其他的内容
                                etMsgContent.setText(s.toString());
                                // TODO: 2018/4/28 这里修改了一下显示的光标
                                etMsgContent.setSelection(start+count);
//                                etMsgContent.setSelection(etMsgContent.getText().toString().length());
//                                temp = length + numCount;
                            }
                    }

                    // TODO: 2018/4/17 为了统计字数
                    if (etEnterpriseSignature.getText().toString().equals("")) {
                        Logger.d("企业签名为空");
                        String content = etMsgContent.getText().toString();
                        if (content.contains("【")) { //短信有内容
                            String s2 = content.split("】")[1]; //后面的短信正文内容
                            numCount = s2.length() + tvUnsubscribe.getText().length();
                        }else{ //短信没内容
                            numCount = tvUnsubscribe.getText().length();
                        }
                    } else {
                        Logger.d("企业签名不为空");
                        // TODO: 2018/4/8 bug :只能先输入企业签名再输入短信内容
                        String content = etMsgContent.getText().toString();
                        if (content.contains("【")) { //短信有内容
                            String[] split = content.split("】");
                            String s2 = split[1];
                            String s1 = etEnterpriseSignature.getText().toString() + s2.toString();
                            numCount = s1.length() + tvUnsubscribe.getText().length();
                        } else { //短信没内容
                            numCount = tvMessageSign.getText().length() + tvUnsubscribe.getText().length();
                        }
                    }
                    etMsgContent.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s/*之后的文字内容*/) {
                if (numCount>70){
                    ToastUtils.showShort("您输入的短信内容超出字数限制");
                }else{

                }
                tvNum.setText("" + numCount);
            }
        });

        //输入任务预算时：
        etBudget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                budget = s.toString();
                if (!TextUtils.isEmpty(budget)) {
                    tvCoverage.setText("预计最大可覆盖人数(人)：" + (int) (Integer.parseInt(budget) / Double.parseDouble(smsPrice)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }



    private boolean isChinese(String signContent){
        String substring = signContent.substring(1, signContent.length() - 1);
        //只允许汉字
        String regEx = "[^\u4E00-\u9FA5]{2,4}";

        String regEx2 = "/^([\\u4e00-\\u9fa5]){2,7}$";
        boolean isMatch = Pattern.matches(regEx, substring);
        if (isMatch) {
            return true;
        }else{
            return false;
        }
    }


    private String stringFilter1(String signContent) {
        //只允许汉字
        String regEx = "[^\u4E00-\u9FA5]{2,7}";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(signContent);
        boolean matches = m.matches();

        return m.replaceAll("").trim();
    }

    private void initTitle() {
        titleMsg.setTitleName(getString(R.string.msg_task_setting));
        titleMsg.setLeftImage(R.drawable.bg_back_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        titleMsg.setRightText(R.string.test);
        titleMsg.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                Intent intent = new Intent(getBaseContext(), TestPhoneSettingActivity.class);
                intent.putExtra("testCmPhone", testCmPhone);
                intent.putExtra("testCuPhone", testCuPhone);
                intent.putExtra("testCtPhone", testCtPhone);
                intent.putExtra("cityName", mCityName);
                intent.putExtra("type", type);
                intent.putExtra("cityCode", cityCode);
                intent.putExtra("isReCreate", isReCreate);

                startActivityForResult(intent, 1);
            }
        });

    }

    /**
     * 新增寻客
     */
    private void submit() {
        if (!isSubmitting) {
            isSubmitting = true;
            MDBasicRequestMap map = new MDBasicRequestMap();
            startdate = tvDateFrom.getText().toString().trim();
            enddate = tvDateTo.getText().toString().trim();
            // TODO: 2018/4/9 加上退订 
            content = etMsgContent.getText().toString().trim()+tvUnsubscribe.getText().toString();

            if (TextUtils.isEmpty(etEnterpriseSignature.getText().toString())) {
                ToastUtils.showShort("企业签名只能输入2-4位汉字");
                return;
            }

            if (TextUtils.isEmpty(startdate)
                    || startdate.equals("开始日期")
                    || TextUtils.isEmpty(enddate)
                    || enddate.equals("结束日期")
                    ) {
                ToastUtils.showShort("请填写日期");
                isSubmitting = false;
                return;
            }



            map.put("userId", UserDataManeger.getInstance().getUserId());
            map.put("ageF", ageF);
            map.put("ageB", ageB);
//            map.put("educationLevelF", educationLevelF);
//            map.put("educationLevelB", educationLevelB);
            map.put("sex", sex);
//            map.put("consumptionCapacityF", consumptionCapacityF);
//            map.put("consumptionCapacityB", consumptionCapacityB);
//            map.put("ascription", ascription);
//            map.put("phoneModelIds", phoneModelIds);
            map.put("interestIds", interestIds);
            map.put("cityCode", cityCode);
            map.put("throwAddress", throwAddress);
            map.put("type", type + "");
            map.put("taskName", taskName);
            map.put("rangeRadius", rangeRadius);
            map.put("budget", BigDecimal.valueOf(Double.valueOf(budget)) + "");
            map.put("longitude", BigDecimal.valueOf(Double.valueOf(longitude)) + "");
            map.put("dimension", BigDecimal.valueOf(Double.valueOf(dimension)) + "");
            map.put("testCmPhone", testCmPhone);
            map.put("testCuPhone",testCuPhone);
            map.put("testCtPhone",testCtPhone);
            map.put("startdate", startdate);
            map.put("enddate", enddate);
            map.put("content", content);
            map.put("industryMark",industryMark ); //是否自定义行业
            map.put("industry", industryNameStr); //行业名称



            File[] submitfiles = new File[1];
            String[] submitFileKeys = new String[1];
            String path = Environment.getExternalStorageDirectory().getPath() + "/ClipPhoto/cache/";// + System.currentTimeMillis() + ".png";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            path = path + System.currentTimeMillis() + ".png";
            File file = new File(path);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            submitfiles[0] = file;
            submitFileKeys[0] = 1 + "";
            Logger.d("submitfiles:"+submitfiles);


            //上传短信任务
            showDefaultLoading();
            OkHttpClientManager.postAsyn(HttpConstant.INSERT_CREATE_TASK2, new OkHttpClientManager.ResultCallback<ErrBean>() {
                @Override
                public void onError(Exception e) {
                    hideDefaultLoading();
                    isSubmitting = false;
                    ToastUtils.showShort(e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
                }

                @Override
                public void onResponse(ErrBean response) {
                    hideDefaultLoading();
                    isSubmitting = false;
                    ToastUtils.showShort("创建成功");
                    ACache.get(mContext).remove(SharedPreferencesTool.DIRECTION_HISTORY);

                    Intent intent = new Intent(MessageTaskActivity.this, HomeActivity.class);
                    intent.putExtra("task", "task");
                    startActivity(intent);
//                      finish();
                    closeActivity(CreateFindCustomerActivity.class, MessageTaskActivity.class);
                    //showSubmitFeedbackDialog(response.getCode());
                }
            }, map);


        } else {
            ToastUtils.showShort("请勿重复提交");
        }
    }



    private void obtainData(){
        startdate = tvDateFrom.getText().toString().trim();
        enddate = tvDateTo.getText().toString().trim();
        content = etMsgContent.getText().toString().trim();
        budget = etBudget.getText().toString().trim();
        signStr = etEnterpriseSignature.getText().toString().trim();
        contentAll = content + tvUnsubscribe.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCreate:
                obtainData();
                if (TextUtils.isEmpty(signStr)) {
                    ToastUtils.showShort("请输入企业签名");
                    isSubmitting = false;
                    return;
                }
                if (signStr.length()>2){
                    String substring = signStr.substring(1, signStr.length() - 1);
                    Logger.d("substring:"+substring);
                    int length = etEnterpriseSignature.getText().length();

                    if (!RegexUtils.isZh(substring)||length < 4 || length > 6) {
                        ToastUtils.showShort("企业签名只能输入2-4位汉字");
                        isSubmitting = false;
                        return;
                    }
                }
                if (contentAll.length() > 70 ) {
                    ToastUtils.showShort("您输入的短信内容超出字数限制");
                    return;
                }
//
//                String substring = signStr.substring(1, signStr.length() - 1);
//                Logger.d("substring:"+substring);
//                int length = etEnterpriseSignature.getText().length();

                if (TextUtils.isEmpty(content)) {
                    ToastUtils.showShort("请输入短信内容");
                    isSubmitting = false;
                    return;
                }

                if (startdate.equals("开始日期")) {
                    ToastUtils.showShort("请输入开始日期");
                    isSubmitting = false;
                    return;
                }
                if (enddate.equals("结束日期")) {
                    ToastUtils.showShort("请输入结束日期");
                    isSubmitting = false;
                    return;
                }
                if (TextUtils.isEmpty(budget)) {
                    ToastUtils.showShort("请输入任务预算");
                    isSubmitting = false;
                    return;
                }
                if (Double.parseDouble(budget)<minMoney) {
                    ToastUtils.showShort("任务预算需"+minMoney+"起");
                    isSubmitting = false;
                    return;
                }
                content = etMsgContent.getText().toString().trim()+tvUnsubscribe.getText().toString();

                // TODO: 2018/5/9 比较一下开始时间是否在有效期内
                if (!DateTool.isBefore(startdate, enddate)) {
                    ToastUtils.showShort("结束日期不能小于开始日期");
                    return;
                }
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, day);
                startDateText = DateTool.parseCalendar2Str(calendar, "yyyy-MM-dd");
                Logger.d("当前的开始时间："+startDateText);
                if (!DateTool.isBefore(startDateText, startdate)) {
                    ToastUtils.showShort("日期失效，请重新选择日期");
                    return;
                }



                // TODO: 2018/4/8 返回敏感字符接口
                showDefaultLoading();
                MDBasicRequestMap map = new MDBasicRequestMap();
                map.put("userId", UserDataManeger.getInstance().getUserId());
                map.put("content", content);

                OkHttpClientManager.postAsyn(HttpConstant.selectWord, new OkHttpClientManager.ResultCallback<SelectWordBean>() {
                    @Override
                    public void onError(Exception e) {
                        hideDefaultLoading();
                        isSubmitting = false;
                        ToastUtils.showShort(e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
                    }

                    @Override
                    public void onResponse(SelectWordBean response) {
                        isSubmitting = false;
                        hideDefaultLoading();
                        Logger.d(response.getErr().getMsg());
                        if (response.getResult()==0) {
                            ToastUtils.showShort(response.getErr().getMsg());
                            return;
                        }else{
                            showRemindDialog();
                        }
                    }
                },map);

                break;

            case R.id.date_from:
                if (getCurrentFocus() != null) {
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                }
                showDateFromDialog(date_from);
                break;
            case R.id.date_to:
                if (getCurrentFocus() != null) {
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                }
                showDateToDialog(date_to);
                break;
        }
    }

    private String mNowDateText;
    private String startDateText; //开始时间的文本
    private String endDateText;

    /**
     * 开始日期
     * @param v
     */
    private void showDateFromDialog(View v) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, day);
        startDateText = DateTool.parseCalendar2Str(calendar, "yyyy-MM-dd");
        DateSelectPopupWindow myPopupwindow = new DateSelectPopupWindow(this, mNowDateText, startDateText, endDateText, DEFAULT_DATA);
        myPopupwindow.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        myPopupwindow.setOnDateSelectListener(new DateSelectPopupWindow.OnDateSelectListener() {
            @Override
            public void onDateSelect(int year, int monthOfYear, int dayOfMonth) {
                // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                if (year == 0 && monthOfYear == 0 && dayOfMonth == 0) {
                    if (mNowDateText.trim().equals("") || mNowDateText.trim().equals(DEFAULT_STR)) {
                        mNowDateText = startDateText;
                    }
                } else {
                    //设置成当前日期
                    mNowDateText = DateTool.getChinaDateFromCalendar(year, monthOfYear, dayOfMonth);
                }
                tvDateFrom.setText(mNowDateText);
            }
        });
    }



    private String mNowDateText1; //当前时间
    private String startDateText1; //开始时间
    private String endDateText1; //结束时间
    /**
     * 结束日期
     *
     * @param v
     */
    private void showDateToDialog(View v) {
        if (tvDateFrom.getText().equals("开始日期")) {
            ToastUtils.showShort("请先选择开始时间。");
            return;
        }
        //Calendar calendar1 = Calendar.getInstance();
        //calendar1.add(Calendar.DAY_OF_MONTH, day);
        //startDateText1 = DateTool.parseCalendar2Str(calendar1, "yyyy-MM-dd");
        // TODO: 2018/5/9   再次获取时间
        mNowDateText = tvDateFrom.getText().toString().trim();
        startDateText1 = mNowDateText;
//        Calendar calendar = Calendar.getInstance();
        Calendar calendar = DateTool.parseDate2Calendar(DateTool.parseStr2Date(mNowDateText, "yyyy-MM-dd"));
        calendar.add(Calendar.YEAR, 1);
        //calendar.add(Calendar.DAY_OF_YEAR, day);
        endDateText1 = DateTool.parseCalendar2Str(calendar, "yyyy-MM-dd");
        DateSelectPopupWindow myPopupwindow = new DateSelectPopupWindow(this, mNowDateText1, startDateText1, endDateText1, DEFAULT_DATA);
        myPopupwindow.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        myPopupwindow.setOnDateSelectListener(new DateSelectPopupWindow.OnDateSelectListener() {
            @Override
            public void onDateSelect(int year, int monthOfYear, int dayOfMonth) {
                // SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                if (year == 0 && monthOfYear == 0 && dayOfMonth == 0) {
                    if (mNowDateText1.trim().equals("") || mNowDateText1.trim().equals(DEFAULT_END)) {
                        mNowDateText1 = startDateText1;
                    }
                } else {
                    mNowDateText1 = DateTool.getChinaDateFromCalendar(year, monthOfYear, dayOfMonth);
                }
                tvDateTo.setText(mNowDateText1);
            }
        });

    }

    /**
     * 确认提示框dialog
     */
    private void showRemindDialog() {
        final ConfirmSubmitDialog dialog = new ConfirmSubmitDialog(this, "确认提交？", getResources().getString(R.string.submit_remind));
        dialog.setClickListenerInterface(new ConfirmSubmitDialog.ClickListenerInterface() {
            @Override
            public void doCancel() {
                dialog.dismiss();
            }

            @Override
            public void doConfirm() {
                dialog.dismiss();
                submit();
            }
        });
        dialog.show();
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
    protected TaskPresent<ITaskView> createPresent() {
        return new TaskPresent<>(this);
    }

    @Override
    public void createCustomer(SettingOut settingOuts) {

    }

    @Override
    public void uploadPicSuc(UploadPicBean uploadPicBean) {

    }

    /**
     * @param nowDataText
     */
    public void setupDateText(String nowDataText) {
        setupDateText(nowDataText, null, null);
        setupDateText1(nowDataText, null, null);
    }

    public static final String DEFAULT_DATA = "1970-01-01";
    public static final String DEFAULT_STR = "开始日期";
    public static final String DEFAULT_END = "结束日期";

    /**
     * @param nowDateText
     * @param startDateText
     * @param endDateText
     */
    public void setupDateText(String nowDateText, String startDateText, String endDateText) {
        if (nowDateText == null || nowDateText.trim().equals("") || nowDateText.trim().equals("null")
                || nowDateText.contains(DEFAULT_DATA) || nowDateText.equals(DEFAULT_STR)) {
            mNowDateText = DEFAULT_STR;
        } else {
            //先转
            Date date = DateTool.parseStr2Date(nowDateText, DateTool.FORMAT_DATE);
            if (date != null) {
                mNowDateText = DateTool.parseDate2Str(date, DateTool.FORMAT_DATE);
            } else {
                mNowDateText = DEFAULT_STR;
            }
            if (mNowDateText.contains(DEFAULT_DATA)) {
                mNowDateText = DEFAULT_STR;
            }
        }
        tvDateFrom.setText(mNowDateText); //初始化显示的内容
        //
        this.startDateText = startDateText;
        this.endDateText = endDateText;
    }

    public void setupDateText1(String nowDateText, String startDateText, String endDateText) {
        if (nowDateText == null || nowDateText.trim().equals("") || nowDateText.trim().equals("null")
                || nowDateText.contains(DEFAULT_DATA) || nowDateText.equals(DEFAULT_END)) {
            mNowDateText1 = DEFAULT_END;
        } else {
            //先转
            Date date = DateTool.parseStr2Date(nowDateText, DateTool.FORMAT_DATE);
            if (date != null) {
                mNowDateText1 = DateTool.parseDate2Str(date, DateTool.FORMAT_DATE);
            } else {
                mNowDateText1 = DEFAULT_END;
            }
            if (mNowDateText1.contains(DEFAULT_DATA)) {
                mNowDateText1 = DEFAULT_END;
            }
        }
        tvDateTo.setText(mNowDateText1);
        //
        this.startDateText1 = startDateText;
        this.endDateText1 = endDateText;
    }

    private void initDate() {
        setupDateText(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            if (data != null) {
                testCmPhone = data.getStringExtra("testCmPhone");
                testCuPhone = data.getStringExtra("testCuPhone");
                testCtPhone = data.getStringExtra("testCtPhone");
            }
        }
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
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                },
                0);
    }


}

class EmojiExcludeFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        for (int i = start; i < end; i++) {
            int type = Character.getType(source.charAt(i));
            if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                return "";
            }
        }
        return null;
    }

}


