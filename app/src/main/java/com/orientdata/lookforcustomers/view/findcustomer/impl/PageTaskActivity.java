package com.orientdata.lookforcustomers.view.findcustomer.impl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.AdPage;
import com.orientdata.lookforcustomers.bean.AdPagesBean;
import com.orientdata.lookforcustomers.bean.ErrBean;
import com.orientdata.lookforcustomers.bean.SettingOut;
import com.orientdata.lookforcustomers.bean.UploadPicBean;
import com.orientdata.lookforcustomers.event.AdEvent;
import com.orientdata.lookforcustomers.event.ImgClipResultEvent;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;
import com.orientdata.lookforcustomers.presenter.TaskPresent;
import com.orientdata.lookforcustomers.util.DateTool;
import com.orientdata.lookforcustomers.util.GlideUtil;
import com.orientdata.lookforcustomers.util.SharedPreferencesTool;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.util.XEditText;
import com.orientdata.lookforcustomers.view.certification.fragment.ACache;
import com.orientdata.lookforcustomers.view.findcustomer.CreateAdActivity;
import com.orientdata.lookforcustomers.view.findcustomer.CreateFindCustomerActivity;
import com.orientdata.lookforcustomers.view.findcustomer.ITaskView;
import com.orientdata.lookforcustomers.view.findcustomer.TestPhoneSettingActivity;
import com.orientdata.lookforcustomers.view.mine.CleanMessageUtil;
import com.orientdata.lookforcustomers.widget.DateSelectPopupWindow;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.orientdata.lookforcustomers.widget.dialog.ConfirmSubmitDialog;
import com.orientdata.lookforcustomers.widget.dialog.SettingStringDialog;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

import static com.orientdata.lookforcustomers.R.id.et_budget;

/**
 * Created by wy on 2017/11/27.
 * 创建页面任务
 */

public class PageTaskActivity extends BaseActivity<ITaskView, TaskPresent<ITaskView>> implements ITaskView, View.OnClickListener {
    private MyTitle titlePage;
    private TextView tvCreate;
    private RelativeLayout date_from;
    private RelativeLayout date_to;
    private TextView tvDateFrom;
    private TextView tvDateTo;
    private ImageView addAd;
    private TextView textView3;
    private ImageView imageView4;
    private TextView tv_create_ad_page;
    private RelativeLayout input_img_url;
    private String testCmPhone = "";//移动测试号
    private String testCuPhone = "";//联通测试号
    private String testCtPhone = "";//电信测试号
    private String adImgId = "";//广告ID
    private String adImgPath = "";


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
    private String mapPath;
    private String address;
    private int day;
    private TextView tvCoverage;
    private String pagePrice = "";

    private AdPagesBean mAdPageBeans;
    private List<AdPage> mAdPages;
    private List<String> mPageNameLists;
    private ImageView iv_close;
    private TextView tv_http;
    private XEditText et_http;
    private int pageTaskFromPosition = -1;
    private String mCityName;
    //private int mCurrentTaskPagePosition = -1;
    private Context mContext;
    private boolean isSubmitting = false;
    @BindView(et_budget)
    EditText etBudget;
    private String industryMark ="";
    private String industryNameStr="";
    private String adLink;
    private String enddate;
    private String startdate;
    private double minMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_task);
        ButterKnife.bind(this);
        this.mContext = this;
        getData();
        initIntentData();
        initView();
        initTitle();
        initDate();
        updateView();
        mPresent.createCustomer(cityCode);
    }

//    @Override
//    protected void onStart() {
//
//        super.onStart();
//    }

    private void updateView() {
        //预算／单价
        // TODO: 2018/4/4 设置最大覆盖人数 
//        tvCoverage.setText("预计最大可覆盖人数(人)：" + (int) (Integer.parseInt(budget) / Double.parseDouble(pagePrice)));
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
            type = intent.getIntExtra("type", 2);
            taskName = intent.getStringExtra("taskName");
            rangeRadius = intent.getStringExtra("rangeRadius");

//            budget = intent.getStringExtra("budget");
            longitude = intent.getStringExtra("longitude");
            dimension = intent.getStringExtra("dimension");
            mapPath = intent.getStringExtra("mapPath");
            address = intent.getStringExtra("address");
            pagePrice = intent.getStringExtra("pagePrice");
            day = intent.getIntExtra("day", 0);
            mCityName = intent.getStringExtra("cityName");

            industryMark = intent.getStringExtra("industryMark");
            industryNameStr = intent.getStringExtra("industryNameStr");

            minMoney = intent.getDoubleExtra("minMoney", 1000);

        }
    }

    private void initView() {
        titlePage = (MyTitle) findViewById(R.id.titlePage);
        tvCreate = (TextView) findViewById(R.id.tvCreate);
        date_from = (RelativeLayout) findViewById(R.id.date_from);
        date_to = (RelativeLayout) findViewById(R.id.date_to);
        addAd = (ImageView) findViewById(R.id.addAd);
        tvCoverage = (TextView) findViewById(R.id.tvCoverage);

        etBudget.setHint("请输入任务预算"+minMoney+"元起");

        tvDateFrom = date_from.findViewById(R.id.tvLeftText);
        input_img_url = findViewById(R.id.input_img_url);
        tvDateTo = date_to.findViewById(R.id.tvLeftText);
        textView3 = input_img_url.findViewById(R.id.textView3);//选择落地页
        imageView4 = input_img_url.findViewById(R.id.imageView4);
        tv_http = input_img_url.findViewById(R.id.tv_http);//http
        iv_close = input_img_url.findViewById(R.id.iv_close);//关闭按钮
        iv_close.setOnClickListener(this);
        et_http = input_img_url.findViewById(R.id.et_http);
        tv_create_ad_page = findViewById(R.id.tv_create_ad_page);
        tv_create_ad_page.setOnClickListener(this);
        date_from.setOnClickListener(this);
        date_to.setOnClickListener(this);
        tvCreate.setOnClickListener(this);
        addAd.setOnClickListener(this);
        textView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);


        et_http.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())) {
                    iv_close.setVisibility(View.GONE);
                }else {
                    iv_close.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initTitle() {
        titlePage.setTitleName(getString(R.string.page_task_setting));
        titlePage.setImageBack(this);
        titlePage.setRightText(R.string.test);
        titlePage.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), TestPhoneSettingActivity.class);
                intent.putExtra("testCmPhone", testCmPhone);
                intent.putExtra("testCuPhone", testCuPhone);
                intent.putExtra("testCtPhone", testCtPhone);
                intent.putExtra("cityName", mCityName);
                intent.putExtra("type", type);
                intent.putExtra("cityCode", cityCode);
                startActivityForResult(intent, 1);
            }
        });

        //输入任务预算时：
        etBudget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                budget = s.toString().trim();
                if (!TextUtils.isEmpty(budget)) {
                    tvCoverage.setText("预计最大可覆盖人数(人)：" + (int) (Integer.parseInt(budget) / Double.parseDouble(pagePrice)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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

            if (!TextUtils.isEmpty(adLink)) {
                map.put("adLink", adLink);
            }
            if (pageTaskFromPosition != -1) {
                map.put("templateId", mAdPages.get(pageTaskFromPosition).getPageTemplateId() + "");
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
            map.put("testCuPhone", testCuPhone);
            map.put("testCtPhone", testCtPhone);

            map.put("industryMark",industryMark ); //是否自定义行业
            map.put("industry", industryNameStr); //行业名称


            /*map.put("adImgid", "");
            map.put("adLink", "");
            map.put("templateId", "");*/

            map.put("startdate", startdate);
            map.put("enddate", enddate);
            // map.put("content", content);
            File[] submitfiles = new File[1];
            String[] submitFileKeys = new String[1];
            submitfiles[0] = new File(adImgPath);
            submitFileKeys[0] = 1 + "";
            List<File> fileList = new ArrayList<>();
            fileList.add(new File(adImgPath));

            try {
                showDefaultLoading();
                OkHttpClientManager.postAsyn(HttpConstant.INSERT_CREATE_TASK2, new OkHttpClientManager.ResultCallback<ErrBean>() {
                    @Override
                    public void onError(Exception e) {
                        hideDefaultLoading();
                        ToastUtils.showShort(e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
                        isSubmitting = false;
                    }

                    @Override
                    public void onResponse(ErrBean response) {
                        hideDefaultLoading();
                        isSubmitting = false;
                        ToastUtils.showShort("创建成功");
                        ACache.get(mContext).remove(SharedPreferencesTool.DIRECTION_HISTORY);
                        CleanMessageUtil.deleteDir(new File(Environment.getExternalStorageDirectory() + File.separator +"tempAd.jpg"));//添加图片
                        CleanMessageUtil.deleteDir(new File(Environment.getExternalStorageDirectory() + File.separator + "imageWithText.jpg"));//模板制作
                        //showSubmitFeedbackDialog(response.getCode());
//                    finish();
                        closeActivity(CreateFindCustomerActivity.class, PageTaskActivity.class);
                    }
                }, submitfiles, "file", map);
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            ToastUtils.showShort("请勿重复提交");
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCreate://创建任务
                startdate = tvDateFrom.getText().toString().trim();
                enddate = tvDateTo.getText().toString().trim();
                adLink = et_http.getText().toString().trim();
                budget = etBudget.getText().toString().trim();

                // String content = etMsgContent.getText().toString().trim();
                if (TextUtils.isEmpty(adImgPath)) {
                    ToastUtils.showShort("请选择广告位图片！");
                    isSubmitting = false;
                    return;
                }

                if (TextUtils.isEmpty(adLink) && pageTaskFromPosition == -1) {
                    ToastUtils.showShort("请选择落地页或者输入图片链接！");
                    isSubmitting = false;
                    return;
                }

//                if (!RegexUtils.isURL(adLink)){
//                    ToastUtils.showShort("请输入有效的图片链接！");
//                    return;
//                }

                if (TextUtils.isEmpty(startdate)
                        || startdate.equals("开始日期")
                        || TextUtils.isEmpty(enddate)
                        || enddate.equals("结束日期")) {
                    ToastUtils.showShort("请选择开始/结束日期！");
                    isSubmitting = false;
                    return;
                }

                if (TextUtils.isEmpty(budget)) {
                    ToastUtils.showShort("请输入任务预算");
                    return;
                }
                if (Double.parseDouble(budget)<minMoney) {
                    ToastUtils.showShort("任务预算需"+minMoney+"起");
                    isSubmitting = false;
                    return;
                }
                showRemindDialog();
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
            case R.id.addAd://添加广告位
                startActivity(new Intent(this, AddAdvertiseImgActivity.class));
                break;
            case R.id.imageView4:
            case R.id.textView3://选择落地页
                //ToastUtils.showShort("选择落地页");
                showPageTaskFromDialog(mPageNameLists, tv_http);

                break;
            case R.id.iv_close:
                tv_http.setText("http://");
                et_http.setText("");
                et_http.setFocusableInTouchMode(true);
                et_http.setFocusable(true);
                et_http.requestFocus();
                iv_close.setVisibility(View.GONE);
                pageTaskFromPosition = -1;
                break;
            case R.id.tv_create_ad_page:
               /* baiduMapPath = intent.getStringExtra("mapPath");
                longitude = intent.getStringExtra("longitude");
                dimension = intent.getStringExtra("dimension");
                address = intent.getStringExtra("address");*/

                Intent intent = new Intent(this, CreateAdActivity.class);
                //intent.putExtra("adImagePath", adImgPath);
                intent.putExtra("mapPath", mapPath);
                intent.putExtra("longitude", longitude);
                intent.putExtra("dimension", dimension);
                intent.putExtra("address", address);
                startActivity(intent);
                break;
        }
    }

    @Subscribe
    public void adEventResult(AdEvent adEvent) {
        if (adEvent != null && adEvent.complete) {
            getData();
        }
    }

    /**
     * 选择落地页
     * @param listString
     * @param view
     */
    private void showPageTaskFromDialog(List<String> listString, final TextView view) {

        if (listString != null && listString.size() > 0) {
            final SettingStringDialog dialog = new SettingStringDialog(this,"请选择落地页", R.style.Theme_Light_Dialog);
            dialog.setOnchangeListener(new SettingStringDialog.ChangeListener() {
                @Override
                public void onChangeListener(String data, int position) {
                    view.setText(data);
                    dialog.dismiss();
                    pageTaskFromPosition = position;
                    //设置et_http不可编辑
                    et_http.setText("");
                    et_http.setFocusable(false);
                    et_http.setFocusableInTouchMode(false);
                    iv_close.setVisibility(View.VISIBLE);
                }
            });
            dialog.setUpData(listString);
            dialog.setSelect(pageTaskFromPosition);
            dialog.show();
        }else{
            ToastUtils.showShort("没有落地页可选择，请先创建");
        }
    }

    /**
     * 获取落地页列表
     */
    public void getData() {
        showDefaultLoading();
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());
        OkHttpClientManager.postAsyn(HttpConstant.SELECT_AD_PAGES, new OkHttpClientManager.ResultCallback<AdPagesBean>() {
            @Override
            public void onError(Exception e) {
                hideDefaultLoading();
                ToastUtils.showShort(e.getMessage());
                           /* startActivity(data);
                            finish();*/
            }

            @Override
            public void onResponse(AdPagesBean response) {
                hideDefaultLoading();
                if (response.getCode() == 0) {
                    mAdPageBeans = response;
                    mAdPages = mAdPageBeans.getResult();
                    if (mAdPages == null) {
                        hideDefaultLoading();
                        return;
                    }
                    if (mPageNameLists == null) {
                        mPageNameLists = new ArrayList<>();
                    } else {
                        mPageNameLists.clear();
                    }
                    for (AdPage adPage : mAdPages) {
                        mPageNameLists.add(adPage.getName()+"_"+DateTool.parseDate2Str(adPage.getCreateDate(),DateTool.FORMAT_DATE_TIME)+"");
                    }
                    /*  if (mAdapter == null) {
                        mAdPageLists = mAdPageBeans.getResult();
                        mAdapter = new ImageWarehouseFragment.MyImageWarehouseViewAdapter(getActivity(), mAdPageLists);
                        listView.setAdapter(mAdapter);
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }*/

                }

            }
        }, map);

    }

    @Subscribe
    public void imgClipResult(ImgClipResultEvent imgClipResultEvent) {
//        Glide.with(this).load(imgClipResultEvent.path).into(addAd);
        if (imgClipResultEvent.bitmap != null) {
            addAd.setImageBitmap(imgClipResultEvent.bitmap);

        }
        if (!TextUtils.isEmpty(imgClipResultEvent.path)) {
            adImgPath = imgClipResultEvent.path;
        }

        if (!TextUtils.isEmpty(imgClipResultEvent.library_url)) {
            showDefaultLoading();
//          Glide.with(this).load(imgClipResultEvent.library_url).into(addAd);

            GlideUtil.getInstance().loadAdImage(this,addAd,imgClipResultEvent.library_url,true);

            adImgPath = imgClipResultEvent.path;
            hideDefaultLoading();
        }
        /*if (!TextUtils.isEmpty(imgClipResultEvent.adImgid)) {
            adImgId = imgClipResultEvent.adImgid;
            addAd.setImageBitmap(imgClipResultEvent.bitmap);
            Glide.with(this).load(adImgId).into(addAd);
        }*/
//        mPresent.uploadImg(3, LocalUploadFragment.IMAGE_FILE_LOCATION);
    }

    Bitmap bm = null;

    //确认，生成图片
    public void confirm(View view) {
        bm = loadBitmapFromView(view);
        adImgPath = Environment.getExternalStorageDirectory() + "/ClipPhoto/cache/";// + System.currentTimeMillis() + ".png";
        File dir = new File(adImgPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        adImgPath = adImgPath + System.currentTimeMillis() + ".png";
        try {
            bm.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(adImgPath));
//            Toast.makeText(this, "图片已保存至：SD卡根目录/imageWithText.jpg", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //mPresent.uploadImg(3, filePath);

    }

    //以图片形式获取View显示的内容（类似于截图）
    public static Bitmap loadBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private String mNowDateText;
    private String startDateText;
    private String endDateText;
    public static final String DEFAULT_DATA = "1970-01-01";
    public static final String DEFAULT_STR = "开始日期";
    public static final String DEFAULT_END = "结束日期";

    /**
     * 开始时间
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
     * 结束时间
     * @param v
     */
    private void showDateToDialog(View v) {
        if (tvDateFrom.getText().equals("开始日期")) {
            ToastUtils.showShort("请先选择开始时间。");
            return;
        }
//        Calendar calendar1 = Calendar.getInstance();
//        calendar1.add(Calendar.DAY_OF_MONTH, day);
//        startDateText1 = DateTool.parseCalendar2Str(calendar1, "yyyy-MM-dd");

        startDateText1 = mNowDateText;

        Calendar calendar = DateTool.parseDate2Calendar(DateTool.parseStr2Date(mNowDateText, "yyyy-MM-dd"));
//        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
//        calendar.add(Calendar.DAY_OF_YEAR, day);
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
     * 提示dialog
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
        if (uploadPicBean != null) {
            ToastUtils.showShort("上传至图片库");
        }
    }

    /**
     * @param nowDataText
     */
    public void setupDateText(String nowDataText) {
        setupDateText(nowDataText, null, null);
        setupDateText1(nowDataText, null, null);
    }

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

}
