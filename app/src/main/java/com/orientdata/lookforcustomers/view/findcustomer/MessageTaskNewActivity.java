package com.orientdata.lookforcustomers.view.findcustomer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.ErrBean;
import com.orientdata.lookforcustomers.bean.MessageTaskCacheBean;
import com.orientdata.lookforcustomers.bean.MyInfoBean;
import com.orientdata.lookforcustomers.bean.SelectWordBean;
import com.orientdata.lookforcustomers.bean.SettingOut;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;
import com.orientdata.lookforcustomers.presenter.TaskPresentNew;
import com.orientdata.lookforcustomers.util.DateTool;
import com.orientdata.lookforcustomers.util.RegexUtils;
import com.orientdata.lookforcustomers.util.SharedPreferencesTool;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.certification.fragment.ACache;
import com.orientdata.lookforcustomers.view.findcustomer.impl.ITaskViewNew;
import com.orientdata.lookforcustomers.view.home.imple.HomeActivity;
import com.orientdata.lookforcustomers.widget.DateSelectPopupWindow;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.orientdata.lookforcustomers.widget.dialog.ConfirmDialog;
import com.orientdata.lookforcustomers.widget.dialog.ConfirmSubmitDialog;
import com.orientdata.lookforcustomers.widget.toggleButton.zcw.togglebutton.ToggleButton;
import com.qiniu.android.common.Constants;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
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

public class MessageTaskNewActivity extends BaseActivity<ITaskViewNew, TaskPresentNew<ITaskViewNew>> implements ITaskViewNew, View.OnClickListener {


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
    TextView tvPushTime;
    @BindView(R.id.toggleBold)
    ToggleButton toggleBold;
    @BindView(R.id.rt_task_name)
    EditText rtTaskName;
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
    private TextView tvCreate;
    private TextView tvNum;
    private RelativeLayout date_from;
    private RelativeLayout date_to;
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

    private int numCount;//输入的字数



    @Override
    public void onBackPressed() {
        //签名
        //短信内容
        //开始时间.结束时间
        //任务预算
        obtainData();
        String remindString = "";
        if (!TextUtils.isEmpty(testCmPhone) || !TextUtils.isEmpty(testCuPhone) || !TextUtils.isEmpty(testCtPhone)) {
            remindString = "测试号码无法保存，再次创建任务需重新输入测试号码";
        } else {
            remindString = "";
        }

        final ConfirmSubmitDialog dialog = new ConfirmSubmitDialog(this, "是否保存当前内容", remindString);
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
            type = intent.getIntExtra("type", 1);
            taskName = intent.getStringExtra("taskName");
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

            String allPerson = intent.getStringExtra(Constants.all_person);

            tvPerson.setText(allPerson + "人");
            tvProgressMaxNum.setText(allPerson);
            etPushPerson.setText(allPerson);
            progressBar.setMax(Integer.parseInt(allPerson));
            progressBar.setProgress(Integer.parseInt(allPerson));

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, day);
            startDateText = DateTool.parseCalendar2Str(calendar, "yyyy-MM-dd");

            //推送时间
            tvPushTime.setText(startDateText);

            rtTaskName.setText(calendar.get(Calendar.YEAR)+calendar.get(Calendar.MONTH)+calendar.get(Calendar.DATE)
            +"_"+Calendar.HOUR_OF_DAY+Calendar.MINUTE+Calendar.SECOND+throwAddress);
        }
    }


    private void initView() {
        titleMsg = (MyTitle) findViewById(R.id.titleMsg);
        etMsgContent = findViewById(R.id.etMsgContent);
        tvNum = (TextView) findViewById(R.id.tvNum);
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

                if (numCount>70){
                    ToastUtils.showShort("您输入的短信内容超出字数限制");
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

    }


    private void initTitle() {
        titleMsg.setTitleName("寻客设置");
        titleMsg.setLeftImage(R.mipmap.ic_navigate_before, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                onBackPressed();
            }
        });
        titleMsg.setRightText("保存");
    }


    /**
     * 新增寻客
     */
    private void submit() {
        if (!isSubmitting) {
            isSubmitting = true;
            MDBasicRequestMap map = new MDBasicRequestMap();
            startdate = tvPushTime.getText().toString().trim();
            enddate = tvDateTo.getText().toString().trim();
            // TODO: 2018/4/9 加上退订 
            content = etMsgContent.getText().toString().trim() + tvUnsubscribe.getText().toString();

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
            map.put("sex", sex);
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
            map.put("testCuPhone", testCuPhone);
            map.put("testCtPhone", testCtPhone);
            map.put("startdate", startdate);
            map.put("enddate", enddate);
            map.put("content", content);
            map.put("industryMark", industryMark); //是否自定义行业
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
            Logger.d("submitfiles:" + submitfiles);


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

                    Intent intent = new Intent(MessageTaskNewActivity.this, HomeActivity.class);
                    intent.putExtra("task", "task");
                    startActivity(intent);
//                      finish();
                    closeActivity(CreateFindCustomerActivity.class, MessageTaskNewActivity.class);
                }
            }, map);


        } else {
            ToastUtils.showShort("请勿重复提交");
        }
    }


    private void obtainData() {
        enddate = tvDateTo.getText().toString().trim();
        content = etMsgContent.getText().toString().trim();
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
                if (Double.parseDouble(budget) < minMoney) {
                    ToastUtils.showShort("任务预算需" + minMoney + "起");
                    isSubmitting = false;
                    return;
                }
                content = etMsgContent.getText().toString().trim() + tvUnsubscribe.getText().toString();

                // TODO: 2018/5/9 比较一下开始时间是否在有效期内
                if (!DateTool.isBefore(startdate, enddate)) {
                    ToastUtils.showShort("结束日期不能小于开始日期");
                    return;
                }
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, day);
                startDateText = DateTool.parseCalendar2Str(calendar, "yyyy-MM-dd");
                Logger.d("当前的开始时间：" + startDateText);
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
                        if (response.getResult() == 0) {
                            ToastUtils.showShort(response.getErr().getMsg());
                            return;
                        } else {
                            showRemindDialog();
                        }
                    }
                }, map);

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

                break;
        }
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


    private String mNowDateText1; //当前时间
    private String startDateText1; //开始时间
    private String endDateText1; //结束时间


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
        MessageTaskCacheBean messageTaskCacheBean = (MessageTaskCacheBean) SharedPreferencesTool.getInstance().getObjectFromShare(SharedPreferencesTool.MessageTaskCacheBean);
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
}


