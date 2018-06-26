package com.orientdata.lookforcustomers.view.findcustomer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.MessageTaskCacheBean;
import com.orientdata.lookforcustomers.bean.MyInfoBean;
import com.orientdata.lookforcustomers.bean.OrientationSettingsOut;
import com.orientdata.lookforcustomers.bean.SelectWordBean;
import com.orientdata.lookforcustomers.bean.SettingOut;
import com.orientdata.lookforcustomers.bean.TaskBasicInfo;
import com.orientdata.lookforcustomers.bean.TaskOut;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;
import com.orientdata.lookforcustomers.presenter.TaskPresentNew;
import com.orientdata.lookforcustomers.util.CommonUtils;
import com.orientdata.lookforcustomers.util.DateTool;
import com.orientdata.lookforcustomers.util.RegexUtils;
import com.orientdata.lookforcustomers.util.SharedPreferencesTool;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.findcustomer.impl.ITaskViewNew;
import com.orientdata.lookforcustomers.view.findcustomer.impl.OrderConfirmActivity;
import com.orientdata.lookforcustomers.widget.DateSelectPopupWindow;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.orientdata.lookforcustomers.widget.dialog.ConfirmDialog;
import com.orientdata.lookforcustomers.widget.dialog.ConfirmSubmitDialog;
import com.orientdata.lookforcustomers.widget.toggleButton.zcw.togglebutton.ToggleButton;
import com.qiniu.android.common.Constants;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;


/**
 * Created by wy on 2017/11/27.
 * 创建短信任务
 */
public class MessageTaskNewActivity extends BaseActivity<ITaskViewNew, TaskPresentNew<ITaskViewNew>> implements ITaskViewNew {


    @BindView(R.id.tv_person)
    TextView tvPerson;
    @BindView(R.id.et_push_person)
    EditText etPushPerson;
    @BindView(R.id.tv_balance_account)
    TextView tvBalanceAccount;
    @BindView(R.id.progressBar)
    SeekBar progressBar;
    @BindView(R.id.tv_progress_min_num)
    TextView tvProgressMinNum;
    @BindView(R.id.tv_progress_max_num)
    TextView tvProgressMaxNum;
    @BindView(R.id.tv_message_price)
    TextView tvMessagePrice;
    @BindView(R.id.et_enterprise_signature)
    EditText etEnterpriseSignature; //企业签名
    @BindView(R.id.iv_ques)
    ImageView ivQues;
    @BindView(R.id.tv_unsubscribe)
    TextView tvUnsubscribe;
    @BindView(R.id.tv_push_time)
    TextView tvPushTime; //推送时间
    @BindView(R.id.toggleBold)
    ToggleButton toggleBold;
    @BindView(R.id.rt_task_name)
    EditText etTaskName;
    @BindView(R.id.ll_test_phone)
    LinearLayout llTestPhone;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.et_test_cm)
    EditText etTestCm;
    @BindView(R.id.et_test_cu)
    EditText etTestCu;
    @BindView(R.id.et_test_ct)
    EditText etTestCt;


    private MyTitle titleMsg;
    private EditText etMsgContent; //短信内容
    private TextView tvNum;
    private String ageF;
    private String ageB;
    private String sex = "";
    private String interestIds = "";
    private String cityCode = "";
    private String throwAddress = "";
    private int type = 1;
    private String taskName;
    private String rangeRadius;


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

    private Map<String, Object> mMapInfoBeans;
    private String mProvinceCode; //省Code
    private String content;
    private String enddate = "";
    private String startdate;
    private String industryMark;
    private String industryNameStr;
    private double minMoney;
    private String signStr;
    private String contentAll;
    private MessageTaskCacheBean messageTaskCacheBean;
    private boolean isReCreate;

    private int numCount;//输入的字数
    private String allPerson; //推送的总人数
    private String pushPersonNum = "";
    private int balance;
    private int smsPriceouter;


    @Override
    public void onBackPressed() {
        //签名
        //短信内容
        //开始时间.结束时间
        //任务预算
        obtainData();

        final ConfirmSubmitDialog dialog = new ConfirmSubmitDialog(this, "是否保存当前内容");
        dialog.setClickListenerInterface(new ConfirmSubmitDialog.ClickListenerInterface() {
            @Override
            public void doCancel() {
                dialog.dismiss();
                SharedPreferencesTool.getInstance().remove(SharedPreferencesTool.msgContent);
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

    }




    @OnClick({R.id.tv_push_time, R.id.btn_submit,R.id.iv_ques})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_push_time:
                if (getCurrentFocus() != null) {
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                }
                showDateFromDialog(tvPushTime);
                break;
            case R.id.btn_submit:
                obtainData();

                if (TextUtils.isEmpty(pushPersonNum)) {
                    ToastUtils.showShort("请输入推送人数");
                    isSubmitting = false;
                }

                if (Integer.parseInt(pushPersonNum)<1000 || Integer.parseInt(pushPersonNum) > Integer.parseInt(allPerson)) {
                    ToastUtils.showShort("推送人数需要大于1000小于"+allPerson);
                    isSubmitting = false;
                }


                if (TextUtils.isEmpty(signStr)) {
                    ToastUtils.showShort("请输入企业签名");
                    isSubmitting = false;
                    return;
                }
                if (signStr.length() > 2) {
                    String substring = signStr.substring(1, signStr.length() - 1);
                    Logger.d("substring:" + substring);
                    int length = etEnterpriseSignature.getText().length();

                    if (!RegexUtils.isZh(substring) || length < 4 || length > 6) {
                        ToastUtils.showShort("企业签名只能输入2-4位汉字");
                        isSubmitting = false;
                        return;
                    }
                }
                if (contentAll.length() > 70) {
                    ToastUtils.showShort("您输入的短信内容超出字数限制");
                    return;
                }

                if (TextUtils.isEmpty(content)) {
                    ToastUtils.showShort("请输入短信内容");
                    isSubmitting = false;
                    return;
                }

                content = signStr + etMsgContent.getText().toString().trim() + tvUnsubscribe.getText().toString();


                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, day);
                startDateText = DateTool.parseCalendar2Str(calendar, "yyyy-MM-dd");
                Logger.d("当前的开始时间：" + startDateText);
                if (!DateTool.isBefore(startDateText, startdate)) {
                    ToastUtils.showShort("日期失效，请重新选择日期");
                    isSubmitting = false;
                    return;
                }

                boolean toogle = toggleBold.getToogle();

                if (toogle) {
                    testCmPhone = "";
                    testCuPhone = "";
                    testCtPhone = "";
                }else{
                    testCmPhone = etTestCm.getText().toString();
                    testCuPhone = etTestCu.getText().toString();
                    testCtPhone = etTestCt.getText().toString();

                    if (!TextUtils.isEmpty(testCmPhone)) {
                        if (!CommonUtils.isPhoneNum(testCmPhone)) {
                            ToastUtils.showShort("请填写正确的移动号码。");
                            return;
                        }
                    }

                    if (!TextUtils.isEmpty(testCuPhone)) {
                        if (!CommonUtils.isPhoneNum(testCuPhone)) {
                            ToastUtils.showShort("请填写正确的联通号码。");
                            return;
                        }
                    }
                    if (!TextUtils.isEmpty(testCtPhone)) {
                        if (!CommonUtils.isPhoneNum(testCtPhone)) {
                            ToastUtils.showShort("请填写正确的电信号码。");
                            return;
                        }
                    }
                }


                if (TextUtils.isEmpty(taskName)) {
                    ToastUtils.showShort("请输入任务名称");
                    isSubmitting = false;
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
                        if (response.getResult() == 0) {
                            ToastUtils.showShort(response.getErr().getMsg());
                            return;
                        } else {
                            // TODO: 2018/6/26 跳的订单确认的页面
                            Intent intent = new Intent(MessageTaskNewActivity.this, OrderConfirmActivity.class);

                            HashMap map = new HashMap();
                            map.put("userId", UserDataManeger.getInstance().getUserId());
                            map.put("ageF", ageF);
                            map.put("ageB", ageB);
                            map.put("sex", sex);
                            map.put("interestIds", interestIds);
                            map.put("cityCode", cityCode);
                            map.put("throwAddress", throwAddress);
                            map.put("type", type + "");
                            map.put("taskName", taskName);
                            map.put("rangeRadius", rangeRadius);
                            map.put("budget",  + (Integer.parseInt(pushPersonNum) * 1.0)+"");
                            map.put("longitude", longitude + "");
                            map.put("dimension", dimension + "");
                            map.put("testCmPhone", testCmPhone);
                            map.put("testCuPhone", testCuPhone);
                            map.put("testCtPhone", testCtPhone);
                            map.put("startdate", startdate);
                            map.put("enddate", enddate);
                            map.put("content", content);
                            map.put("industryMark", industryMark); //是否自定义行业
                            map.put("industry", industryNameStr); //行业名称


                            intent.putExtra("map", map);

                            startdate = tvPushTime.getText().toString().trim();
                            // TODO: 2018/4/9 加上退订
                            content = signStr + etMsgContent.getText().toString().trim() + tvUnsubscribe.getText().toString();
                            if (TextUtils.isEmpty(etEnterpriseSignature.getText().toString())) {
                                ToastUtils.showShort("企业签名只能输入2-4位汉字");
                                return;
                            }

                            TaskOut taskOut = new TaskOut();
                            OrientationSettingsOut orientationSettingsOut = new OrientationSettingsOut();
                            orientationSettingsOut.setAgeF(ageF);
                            orientationSettingsOut.setAgeB(ageB);
                            orientationSettingsOut.setSex(sex);
                            List<String> strings = Arrays.asList(interestIds.split(","));
                            orientationSettingsOut.setXingqu(strings);
                            taskOut.setIndustryName(industryNameStr);
                            taskOut.setCustomFlag(Integer.parseInt(industryMark));
                            taskOut.setRangeRadius(rangeRadius);
                            taskOut.setThrowAddress(throwAddress);
                            taskOut.setTaskName(taskName);
                            taskOut.setContent(content);
                            taskOut.setThrowStartdate(startdate);

                            taskOut.setEstimatePeoplerno(Integer.parseInt(pushPersonNum));
                            taskOut.setTestCmPhone(testCmPhone);
                            taskOut.setTestCtPhone(testCtPhone);
                            taskOut.setTestCuPhone(testCuPhone);
                            taskOut.setOrientationSettingsOut(orientationSettingsOut);
                            intent.putExtra("taskOut", taskOut);

                            startActivity(intent);


//                            submit();
                        }
                    }
                }, map);

                break;
            case R.id.iv_ques:
                showDialog();
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_task_new);
        ButterKnife.bind(this);
        this.mContext = this;
        initIntentData();
        initView();
        initTitle();
        //请求接口
        initData();
    }


    private void initData() {
        // TODO: 2018/6/25 单价，余额接口。签名接口
        mPresent.getSignAndTd(mProvinceCode);
        mPresent.getCreateTaskBasicInfo(mProvinceCode);
    }

    @Override
    public void getSignAndTd(MyInfoBean response) {
        double id = (double) response.getResult().get("id");
        String provincecode = (String) response.getResult().get("provincecode");
        double signstate = (double) response.getResult().get("signstate");
        String tdcontent = (String) response.getResult().get("tdcontent");

        tvUnsubscribe.setText(tdcontent); //设置回复退订
        int length = tdcontent.length();
        tvNum.setText("还可以输入"+(70 - length)+"字符");

        // TODO: 2018/5/3 设置缓存信息
        if ((int) signstate == 0) { //自定义
            if (messageTaskCacheBean != null) { //有缓存信息
                Logger.d(messageTaskCacheBean.toString());
                if (!TextUtils.isEmpty(messageTaskCacheBean.getSignStr())) {
                    etEnterpriseSignature.setText(messageTaskCacheBean.getSignStr());
                }
                if (!TextUtils.isEmpty(messageTaskCacheBean.getContent())) {
                    etMsgContent.setText(messageTaskCacheBean.getContent());
                }
            }
        } else { //固定签名
            String sign = (String) response.getResult().get("sign");
            etEnterpriseSignature.setText(sign);
            setEnable();
        }
        Logger.d("-----" + provincecode + signstate + tdcontent);
    }


    @Override
    public void getCreateTaskBasicInfo(TaskBasicInfo taskBasicInfo) {
        balance = taskBasicInfo.getBalance();
        tvBalanceAccount.setText("余额："+taskBasicInfo.getBalance()+"元");
        smsPriceouter = taskBasicInfo.getSmsPriceouter();
        tvMessagePrice.setText("短信（"+smsPriceouter+"元/条）");
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
            sex = intent.getStringExtra("sex");
            interestIds = intent.getStringExtra("interestIds");
            cityCode = intent.getStringExtra("cityCode");
            throwAddress = intent.getStringExtra("throwAddress");
//            taskName = intent.getStringExtra("taskName");
            rangeRadius = intent.getStringExtra("rangeRadius");
            longitude = intent.getStringExtra("longitude");
            smsPrice = intent.getStringExtra("smsPrice");
            dimension = intent.getStringExtra("dimension");
            day = intent.getIntExtra("day", 3);
            mCityName = intent.getStringExtra("cityName");
            mProvinceCode = intent.getStringExtra(Constants.mProvinceCode);

            industryMark = intent.getStringExtra("industryMark");
            industryNameStr = intent.getStringExtra("industryNameStr");


            isReCreate = intent.getBooleanExtra("isReCreate", false);
            minMoney = intent.getDoubleExtra("minMoney", 1000);
            // TODO: 2018/4/23 测试号也得传过来
            Logger.d("省Code:---" + mProvinceCode);
            // TODO: 2018/4/7 这个地方先去掉单价

            allPerson = intent.getStringExtra(Constants.all_person);

            tvPerson.setText(allPerson + "人");
            tvProgressMaxNum.setText(allPerson);
            etPushPerson.setText(allPerson);

            if(!TextUtils.isEmpty(allPerson)){
                progressBar.setMax(Integer.parseInt(allPerson));
                progressBar.setProgress(Integer.parseInt(allPerson));
            }

            progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        etPushPerson.setText(1000 + progress + "");
                    }else{

                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });


            Calendar calendar1 = Calendar.getInstance();
            calendar1.add(Calendar.DAY_OF_MONTH, day);
            startDateText = DateTool.parseCalendar2Str(calendar1, "yyyy-MM-dd");

            //推送时间
            tvPushTime.setText(startDateText);
            Calendar calendar = Calendar.getInstance();
            String chinaDateTime = DateTool.getChinaDateTime();
            chinaDateTime = chinaDateTime.replace("-", "");
            chinaDateTime = chinaDateTime.replace(":", "");
            chinaDateTime = chinaDateTime.replace(" ", "_");
            etTaskName.setText(chinaDateTime+"_"+throwAddress);

//            etTaskName.setText(calendar.get(Calendar.YEAR)+""+(calendar.get(Calendar.MONTH)+1)+""+calendar.get(Calendar.DATE)
//            +"_" + calendar.get(Calendar.HOUR_OF_DAY) + "" + calendar.get(Calendar.MINUTE) +"" + calendar.get(Calendar.SECOND) + "_"+throwAddress);
        }
    }


    private void initView() {
        titleMsg = (MyTitle) findViewById(R.id.titleMsg);
        etMsgContent = findViewById(R.id.etMsgContent);
        tvNum = (TextView) findViewById(R.id.tvNum);

        String msgContent = SharedPreferencesTool.getInstance().getStringValue(SharedPreferencesTool.msgContent, "");
        etMsgContent.setText(msgContent);

        toggleBold.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    llTestPhone.setVisibility(View.VISIBLE);
                }else{
                    llTestPhone.setVisibility(View.GONE);
                }
            }
        });

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
                        etMsgContent.setText(s1);
                        numCount = s1.length() + tvUnsubscribe.getText().length();
                    }else { //短信没内容
                        numCount = s.length() + tvUnsubscribe.getText().length();
                    }
                }

                Logger.d("字数为：" + numCount);
                etMsgContent.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
                etEnterpriseSignature.addTextChangedListener(this);


                if (s.toString() != null) {
                    numCount = etMsgContent.getText().toString().length() + tvUnsubscribe.getText().length()+etEnterpriseSignature.getText().toString().length();
                }

                if (numCount > 70){
                    ToastUtils.showLong("您输入的短信内容超出"+ (numCount -70) + "个字");
                }else{
                    tvNum.setText("还可以输入"+(70-numCount)+"字符");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





        //输入短信内容时
        etMsgContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s/*之前的文字内容*/, int start/*添加文字的位置(从0开始)*/, int count, int after/*添加的文字总数*/) {

            }
            @Override
            public void onTextChanged(CharSequence s/*之后的文字内容 */, int start/*添加文字的位置(从0开始)*/, int before/*之前的文字总数*/, int count) {
                if (s.toString() != null) {
                    numCount = s.length() + tvUnsubscribe.getText().length()+etEnterpriseSignature.getText().toString().length();
                }
            }

            @Override
            public void afterTextChanged(Editable s/*之后的文字内容*/) {
                if (numCount>70){
                    ToastUtils.showShort("您输入的短信内容超出字数限制");
                }else{
                    tvNum.setText("还可以输入"+(70-numCount)+"字符");
                }
            }
        });

        //输入推送人数时：
        etPushPerson.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString())) {
                    int etPushpersonNum  = Integer.parseInt(s.toString());
                    if(etPushpersonNum > 1000||etPushpersonNum < Integer.parseInt(allPerson)){
                        progressBar.setProgress(etPushpersonNum-1000);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        etPushPerson.setOnFocusChangeListener(new View.
//                OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    // 此处为得到焦点时的处理内容
//                    Logger.d("获取焦点时：");
//                    etMsgContent.setEnabled(true);
//                } else {
//                    Logger.d("失去焦点时焦点时：");
//                    // 此处为失去焦点时的处理内容
//                    String s = etPushPerson.getText().toString();
//                    int etPushpersonNum  = Integer.parseInt(s.toString());
//                    if (etPushpersonNum < 1000 ) {
//                        ToastUtils.showShort("推送人数不能小于1000");
//                        etPushPerson.setText(allPerson);
//                    }else if(etPushpersonNum > Integer.parseInt(allPerson)){
//                        etPushPerson.setText(allPerson);
//                        ToastUtils.showShort("推送人数不能大于"+Integer.parseInt(allPerson));
//                    }
//                }
//            }
//        });

    }


    private void initTitle() {
        titleMsg.setTitleName("寻客设置");
        titleMsg.setLeftImage(R.mipmap.ic_navigate_before, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        titleMsg.setRightText("保存");

        titleMsg.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = etMsgContent.getText().toString();
                SharedPreferencesTool.getInstance().putStringValue(SharedPreferencesTool.msgContent,content);
            }
        });
    }




    private void obtainData() {
        pushPersonNum = etPushPerson.getText().toString();
        taskName = etTaskName.getText().toString();
        startdate = tvPushTime.getText().toString();
        signStr = etEnterpriseSignature.getText().toString().trim(); //签名
        content = etMsgContent.getText().toString().trim(); //短信内容
        contentAll = content + signStr + tvUnsubscribe.getText().toString(); //所有内容
    }

    

    private String mNowDateText;
    private String startDateText; //开始时间的文本
    private String endDateText;
    /**
     * 开始日期
     *
     * @param v
     */
    private void showDateFromDialog(View v) {
        DateSelectPopupWindow myPopupwindow = new DateSelectPopupWindow(this, mNowDateText, startDateText, endDateText, DEFAULT_DATA);
        myPopupwindow.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        myPopupwindow.setOnDateSelectListener(new DateSelectPopupWindow.OnDateSelectListener() {
            @Override
            public void onDateSelect(int year, int monthOfYear, int dayOfMonth) {
                if (year == 0 && monthOfYear == 0 && dayOfMonth == 0) {
                    if (mNowDateText.trim().equals("") || mNowDateText.trim().equals(DEFAULT_STR)) {
                        mNowDateText = startDateText;
                    }
                } else {
                    //设置成当前日期
                    mNowDateText = DateTool.getChinaDateFromCalendar(year, monthOfYear, dayOfMonth);
                }
                tvPushTime.setText(mNowDateText);
            }
        });
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
    protected TaskPresentNew<ITaskViewNew> createPresent() {
        return new TaskPresentNew<>(this);
    }

    @Override
    public void createCustomer(SettingOut settingOuts) {

    }


    public static final String DEFAULT_DATA = "1970-01-01";
    public static final String DEFAULT_STR = "开始日期";
    public static final String DEFAULT_END = "结束日期";



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

    private void showDialog() {
        final ConfirmDialog dialog = new ConfirmDialog(this, getString(R.string.report_quess),"我已了解");
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



    private void savaCache() {
        // TODO: 2018/5/3 存储到缓存文件中
        SharedPreferencesTool.getInstance().putStringValue(SharedPreferencesTool.msgContent,content);
//        MessageTaskCacheBean messageTaskCacheBean = (MessageTaskCacheBean) SharedPreferencesTool.getInstance().getObjectFromShare(SharedPreferencesTool.MessageTaskCacheBean);
//        if (messageTaskCacheBean == null) {
//            messageTaskCacheBean = new MessageTaskCacheBean();
//        }
//        messageTaskCacheBean.setSignStr(signStr);
//        messageTaskCacheBean.setStartdate(startdate);
//        messageTaskCacheBean.setEnddate(enddate);
////        messageTaskCacheBean.setBudget(budget);
//        messageTaskCacheBean.setContent(content);
//        SharedPreferencesTool.getInstance().setObject(messageTaskCacheBean, SharedPreferencesTool.MessageTaskCacheBean);
    }
}


