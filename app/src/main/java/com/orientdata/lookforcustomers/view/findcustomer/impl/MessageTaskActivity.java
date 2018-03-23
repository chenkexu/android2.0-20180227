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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.ErrBean;
import com.orientdata.lookforcustomers.bean.SettingOut;
import com.orientdata.lookforcustomers.bean.UploadPicBean;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.presenter.TaskPresent;
import com.orientdata.lookforcustomers.runtimepermissions.PermissionsManager;
import com.orientdata.lookforcustomers.util.CommonUtils;
import com.orientdata.lookforcustomers.util.DateTool;
import com.orientdata.lookforcustomers.util.SharedPreferencesTool;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.certification.fragment.ACache;
import com.orientdata.lookforcustomers.view.findcustomer.CreateFindCustomerActivity;
import com.orientdata.lookforcustomers.view.findcustomer.ITaskView;
import com.orientdata.lookforcustomers.view.findcustomer.TestPhoneSettingActivity;
import com.orientdata.lookforcustomers.widget.DateSelectPopupWindow;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.orientdata.lookforcustomers.widget.dialog.ConfirmSubmitDialog;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.OkHttpClientManager;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * Created by wy on 2017/11/27.
 * 短信任务
 */

public class MessageTaskActivity extends BaseActivity<ITaskView, TaskPresent<ITaskView>> implements ITaskView, View.OnClickListener {
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
    private String educationLevelF;
    private String educationLevelB;
    private String sex;
    private String consumptionCapacityF;
    private String consumptionCapacityB;
    private String ascription;
    private String phoneModelIds;
    private String interestIds;
    private String cityCode;
    private String throwAddress;
    private int type;
    private String taskName;
    private String rangeRadius;
    private String budget;
    private String longitude;
    private String dimension;
    private String testCmPhone = "";//移动测试号
    private String testCuPhone = "";//联通测试号
    private String testCtPhone = "";//电信测试号
    private int day;
    private String mCityName;
    private String smsPrice = "";//短信单价
    private TextView tvCoverage;
    private Context mContext;
    private boolean isSubmitting = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_task);
        this.mContext = this;
        initIntentData();
        initView();
        initTitle();
        initDate();
        updateView();
    }

    private void initIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            ageF = intent.getStringExtra("ageF");
            ageB = intent.getStringExtra("ageB");
            educationLevelF = intent.getStringExtra("educationLevelF");
            educationLevelB = intent.getStringExtra("educationLevelB");
            sex = intent.getStringExtra("sex");
            consumptionCapacityF = intent.getStringExtra("consumptionCapacityF");
            consumptionCapacityB = intent.getStringExtra("consumptionCapacityB");
            ascription = intent.getStringExtra("ascription");
            phoneModelIds = intent.getStringExtra("phoneModelIds");
            interestIds = intent.getStringExtra("interestIds");
            cityCode = intent.getStringExtra("cityCode");
            throwAddress = intent.getStringExtra("throwAddress");
            type = intent.getIntExtra("type", 1);
            taskName = intent.getStringExtra("taskName");
            rangeRadius = intent.getStringExtra("rangeRadius");
            budget = intent.getStringExtra("budget");
            longitude = intent.getStringExtra("longitude");
            smsPrice = intent.getStringExtra("smsPrice");
            dimension = intent.getStringExtra("dimension");
            day = intent.getIntExtra("day", 0);
            mCityName = intent.getStringExtra("cityName");
        }
    }

    private void updateView() {
        //预算／单价
        tvCoverage.setText("预计最大可覆盖人数(人)：" + (int) (Integer.parseInt(budget) / Double.parseDouble(smsPrice)));
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
        etMsgContent.setFilters(new InputFilter[]{new EmojiExcludeFilter(), new InputFilter.LengthFilter(64)});
        etMsgContent.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence s/*之前的文字内容*/, int start/*添加文字的位置(从0开始)*/, int count, int after/*添加的文字总数*/) {

            }

            @Override
            public void onTextChanged(CharSequence s/*之后的文字内容 */, int start/*添加文字的位置(从0开始)*/, int before/*之前的文字总数*/, int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s/*之后的文字内容*/) {
                tvNum.setText("" + temp.length());
            }
        });

    }

    private void initTitle() {
        titleMsg.setTitleName(getString(R.string.msg_task_setting));
        titleMsg.setImageBack(this);
        titleMsg.setRightText(R.string.test);
        titleMsg.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), TestPhoneSettingActivity.class);
                intent.putExtra("testCmPhone", testCmPhone);
                intent.putExtra("testCuPhone", testCuPhone);
                intent.putExtra("testCtPhone", testCtPhone);
                intent.putExtra("cityName", mCityName);
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
            String startdate = tvDateFrom.getText().toString().trim();
            String enddate = tvDateTo.getText().toString().trim();
            String content = etMsgContent.getText().toString().trim();
            if (TextUtils.isEmpty(startdate)
                    || startdate.equals("开始日期")
                    || TextUtils.isEmpty(enddate)
                    || enddate.equals("结束日期")
                    || TextUtils.isEmpty(content)
                    ) {
                ToastUtils.showShort("信息填写不完善");
                isSubmitting = false;
                return;
            }
            map.put("userId", UserDataManeger.getInstance().getUserId());
            map.put("ageF", ageF);
            map.put("ageB", ageB);
            map.put("educationLevelF", educationLevelF);
            map.put("educationLevelB", educationLevelB);
            map.put("sex", sex);
            map.put("consumptionCapacityF", consumptionCapacityF);
            map.put("consumptionCapacityB", consumptionCapacityB);
            map.put("ascription", ascription);
            map.put("phoneModelIds", phoneModelIds);
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
            try {
                showDefaultLoading();
                OkHttpClientManager.postAsyn(HttpConstant.INSERT_CREATE_TASK, new OkHttpClientManager.ResultCallback<ErrBean>() {
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
//                    finish();
                        closeActivity(CreateFindCustomerActivity.class, MessageTaskActivity.class);
                        //showSubmitFeedbackDialog(response.getCode());
                    }
                }, submitfiles, "file", map);
            } catch (IOException e) {
                Log.e("MessageTaskActivity", e.getMessage());
            }

        } else {
            ToastUtils.showShort("请勿重复提交");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCreate:
                if (CommonUtils.haveSDCard()) {
                    if (hasPermisson()) {
                        showRemindDialog();
                    } else {
                        requestPermission();
                    }
                } else {
                    Toast.makeText(this, "没有SD卡!", Toast.LENGTH_SHORT).show();
                }

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
    private String startDateText;
    private String endDateText;

    /**
     * 开始日期
     *
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
                // SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                if (year == 0 && monthOfYear == 0 && dayOfMonth == 0) {
                    if (mNowDateText.trim().equals("") || mNowDateText.trim().equals(DEFAULT_STR)) {
                        mNowDateText = startDateText;
                    }
                } else {
                    mNowDateText = DateTool.getChinaDateFromCalendar(year, monthOfYear, dayOfMonth);
                }
                tvDateFrom.setText(mNowDateText);
            }
        });
    }

    private String mNowDateText1;
    private String startDateText1;
    private String endDateText1;

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
     * dialog
     */
    private void showRemindDialog() {
        final ConfirmSubmitDialog dialog = new ConfirmSubmitDialog(this, "确认提交？", "注：任务提交后不得再次修改，且审核通过后不得删除！");
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
        tvDateFrom.setText(mNowDateText);
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
