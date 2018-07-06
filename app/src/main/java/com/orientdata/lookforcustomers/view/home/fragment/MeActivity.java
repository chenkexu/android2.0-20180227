package com.orientdata.lookforcustomers.view.home.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.CertificationOut;
import com.orientdata.lookforcustomers.bean.HomeBean;
import com.orientdata.lookforcustomers.bean.LoginResultBean;
import com.orientdata.lookforcustomers.bean.TaskCountBean;
import com.orientdata.lookforcustomers.bean.URLBean;
import com.orientdata.lookforcustomers.bean.UserInfoBean;
import com.orientdata.lookforcustomers.event.MyMoneyEvent;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;
import com.orientdata.lookforcustomers.presenter.MePresent;
import com.orientdata.lookforcustomers.runtimepermissions.PermissionsManager;
import com.orientdata.lookforcustomers.util.CommonUtils;
import com.orientdata.lookforcustomers.util.GlideUtil;
import com.orientdata.lookforcustomers.util.L;
import com.orientdata.lookforcustomers.util.SharedPreferencesTool;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.certification.fragment.ACache;
import com.orientdata.lookforcustomers.view.certification.impl.CertificationActivity;
import com.orientdata.lookforcustomers.view.home.InvoiceActivity;
import com.orientdata.lookforcustomers.view.home.RechargeActivity;
import com.orientdata.lookforcustomers.view.home.ReportActivity;
import com.orientdata.lookforcustomers.view.home.imple.IMeView;
import com.orientdata.lookforcustomers.view.mine.MessageAndNoticeActivity;
import com.orientdata.lookforcustomers.view.mine.ShareToGetCommissionActivity;
import com.orientdata.lookforcustomers.view.mine.TaskListActivity;
import com.orientdata.lookforcustomers.view.mine.imple.AccountBalanceActivity;
import com.orientdata.lookforcustomers.view.mine.imple.CommissionWithDrawActivity;
import com.orientdata.lookforcustomers.view.mine.imple.SettingActivity;
import com.orientdata.lookforcustomers.widget.PullZoomView;
import com.orientdata.lookforcustomers.widget.dialog.RemindDialog;
import com.qiniu.android.common.Constants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.badgeview.BGABadgeFrameLayout;
import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;


/**
 * Created by ckx on 2018/6/11.
 */

public class MeActivity extends BaseActivity<IMeView, MePresent<IMeView>> implements IMeView {
    @BindView(R.id.margin)
    View margin;

    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.iv_head_portrait)
    ImageView ivHeadPortrait;


    @BindView(R.id.linear_company)
    RelativeLayout linearCompany;
    @BindView(R.id.tv_account_balance)
    TextView tvAccountBalance;
    @BindView(R.id.tv_account_froze)
    TextView tvAccountFroze;
    @BindView(R.id.tv_account_commission)
    TextView tvAccountCommission;
    @BindView(R.id.tv_more_task)
    TextView tvMoreTask;
    @BindView(R.id.rv_task)
    RecyclerView rvTask;
    @BindView(R.id.rv_mine)
    RecyclerView rvMine;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.iv_message)
    ImageView ivMessage;
    @BindView(R.id.ll_account_balance)
    LinearLayout llAccountBalance;
    @BindView(R.id.ll_account_froze)
    LinearLayout llAccountFroze;
    @BindView(R.id.textView23)
    TextView textView23;
    @BindView(R.id.ll_account_commission)
    LinearLayout llAccountCommission;
    @BindView(R.id.textView24)
    TextView textView24;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.pzv)
    PullZoomView pzv;

    private MePresent mePresent;
    private ACache aCache = null;//数据缓存

    private String[] taskStatusStr = {"审核中", "待投放", "投放中", "投放结束", "审核失败"};

    private int[] taskStatusPics = {R.mipmap.me_task_exmine_ing, R.mipmap.me_task_pre,
            R.mipmap.me_task_ing, R.mipmap.me_task_over,
            R.mipmap.me_task_error
    };

    private String[] myStr = {"账户充值", "账户认证", "报表统计", "发票管理", "佣金提现",
            "分享佣金", "在线客服", "邀请码", "设置"};


    private int[] myStrPics = {R.mipmap.iv_balance_account, R.mipmap.iv_renzheng,
            R.mipmap.iv_report, R.mipmap.iv_invoice,
            R.mipmap.iv_my_commission, R.mipmap.iv_share, R.mipmap.iv_service,
            R.mipmap.iv_share_code, R.mipmap.iv_setting
    };


    private List<HomeBean> imagesAndTitles = new ArrayList<>();
    private List<HomeBean> imagesAndTitles2 = new ArrayList<>();
    private String subCount;
    private double frozenAmount;
    private double commission;
    private double balance;
    private String userHead;
    private String upMoney;
    private String moreMoney;
    private Intent intent;

    private String cerStatus = "";
    private String remindString = "";
    private int imgResId = 0;
    private String btText = "";
    private int authStatus = -1;
    private int throwingTaskCount;
    private int examineTaskCount;
    private int waitThrowTaskCount;
    private Adapter taskAdapter;

    private static final int CROP_SMALL_PICTURE = 103;
    public static final String IMAGE_FILE_LOCATION = "file:///sdcard/tempHeadPortrait.jpg";//剪切完的图片所存地址
    public static final String IMAGE_FILE_LOCATION_Path = Environment.getExternalStorageDirectory() + "/tempHeadPortrait.jpg";//剪切完的图片所存地址
    Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);

    @OnClick({R.id.tv_more_task, R.id.iv_message, R.id.iv_back, R.id.linear_company, R.id.iv_head_portrait,
            R.id.ll_account_balance, R.id.ll_account_froze, R.id.ll_account_commission})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_more_task:
                intent = new Intent(MeActivity.this, TaskListActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_head_portrait:
                choosePhoto();
                break;
            case R.id.linear_company:
                showAuthenticationStatus();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_message:
                intent = new Intent(MeActivity.this, MessageAndNoticeActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_account_froze:
            case R.id.ll_account_balance:
                intent = new Intent(MeActivity.this, AccountBalanceActivity.class);
                intent.putExtra("balance", balance);
                intent.putExtra("frozenAmount", frozenAmount);
                startActivity(intent);
                break;
            case R.id.ll_account_commission:
                intent = new Intent(MeActivity.this, CommissionWithDrawActivity.class);
                intent.putExtra("moreMoney", moreMoney);
                intent.putExtra("commission", commission);
                intent.putExtra("subCount", subCount);
                intent.putExtra("upMoney", upMoney);
                startActivity(intent);
                break;
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        mImmersionBar.fitsSystemWindows(true).statusBarDarkFont(false).statusBarColor(R.color.colorPrimary).init();
        ButterKnife.bind(this);
        mePresent = new MePresent(this);
        initView();
        pzv.setOnPullZoomListener(new PullZoomView.OnPullZoomListener() {
            @Override
            public void onPullZoom(int originHeight, int currentHeight) {
            }

            @Override
            public void onZoomFinish() {
                initData();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {
        pzv.setIsParallax(true);
        pzv.setIsZoomEnable(true);
        pzv.setSensitive(1.5f);
        pzv.setZoomTime(500);

        for (int i = 0; i < taskStatusPics.length; i++) {
            HomeBean homeBean = new HomeBean(taskStatusStr[i], taskStatusPics[i]);
            imagesAndTitles.add(homeBean);
        }
        for (int i = 0; i < myStr.length; i++) {
            HomeBean homeBean = new HomeBean(myStr[i], myStrPics[i]);
            imagesAndTitles2.add(homeBean);
        }
        rvMine.setLayoutManager(new GridLayoutManager(this, 5));
        rvTask.setLayoutManager(new GridLayoutManager(this, 5));
        taskAdapter = new Adapter(imagesAndTitles);
        Adapter meAdapter = new Adapter(imagesAndTitles2);
        rvTask.setAdapter(taskAdapter);

        taskAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MeActivity.this, TaskListActivity.class);
                String taskType = taskStatusStr[position];
                intent.putExtra(Constants.taskType, taskType);
                startActivity(intent);
            }
        });

        meAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {

            private Intent intent;

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position) {
                    case 0: //账户充值
                        intent = new Intent(MeActivity.this, RechargeActivity.class);
                        startActivity(intent);
                        break;
                    case 1: //账户认证
                        showAuthenticationStatus();
                        break;
                    case 2: //报表统计
                        intent = new Intent(MeActivity.this, ReportActivity.class);
                        startActivity(intent);
                        break;
                    case 3: //发票管理
                        intent = new Intent(MeActivity.this, InvoiceActivity.class);
                        startActivity(intent);
                        break;
                    case 4: //佣金提现
                        intent = new Intent(MeActivity.this, CommissionWithDrawActivity.class);
                        intent.putExtra("moreMoney", moreMoney);
                        intent.putExtra("commission", commission);
                        intent.putExtra("subCount", subCount);
                        intent.putExtra("upMoney", upMoney);
                        startActivity(intent);
                        break;
                    case 5: //分享佣金
                        //赚取佣金 获取Url
                        showDefaultLoading();
                        MDBasicRequestMap map = new MDBasicRequestMap();
                        map.put("userId", UserDataManeger.getInstance().getUserId());
                        OkHttpClientManager.postAsyn(HttpConstant.CREATE_URL, new OkHttpClientManager.ResultCallback<URLBean>() {
                            @Override
                            public void onError(Exception e) {
                                ToastUtils.showShort(e.getMessage());
                                hideDefaultLoading();
                            }

                            @Override
                            public void onResponse(URLBean re) {
                                if (re.getCode() == 0) {
                                    String url = re.getResult();
                                    if (!TextUtils.isEmpty(url)) {
                                        Intent intent1 = new Intent(MeActivity.this, ShareToGetCommissionActivity.class);
                                        intent1.putExtra("url", url);
                                        startActivity(intent1);
                                    } else {
                                        ToastUtils.showShort("获取分享链接失败");
                                    }
                                }
                                hideDefaultLoading();
                            }
                        }, map);
                        break;
                    case 6://在线客服
                        final String url = "mqqwpa://im/chat?chat_type=wpa&uin=2280249239";
                        if (CommonUtils.checkApkExist(MeActivity.this, "com.tencent.mobileqq")) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        } else {
                            ToastUtils.showShort("本机未安装QQ应用,请下载安装。");
                        }
                        break;
                    case 7: //邀请码
                        ToastUtils.showShort("程序员小哥正在开发中......");
                        break;
                    case 8: //设置
                        intent = new Intent(MeActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        rvMine.setAdapter(meAdapter);
    }


    private void showAuthenticationStatus() {
        if (authStatus != 2) {
            final RemindDialog dialog = new RemindDialog(MeActivity.this, "", remindString, imgResId, btText);
            dialog.setClickListenerInterface(new RemindDialog.ClickListenerInterface() {
                @Override
                public void doCertificate() {
                    dialog.dismiss();
                    startActivity(new Intent(MeActivity.this, CertificationActivity.class));
                }
            });
            dialog.setCancelable(true);
            dialog.show();
        } else {
            //审核通过
            startActivity(new Intent(MeActivity.this, CertificationActivity.class));
        }
    }

    private void initData() {
        showDefaultLoading();
        aCache = ACache.get(this);
        LoginResultBean.UserBean user = (LoginResultBean.UserBean) SharedPreferencesTool.getInstance().getObjectFromShare(SharedPreferencesTool.user);
        if (user!=null) {
            tvCompanyName.setText("ID: " + user.getUserNo() + "");
        }
        //获取账号 佣金 和 余额
        mPresent.getCommission();
        mPresent.getCertificateMsg(true);
        mPresent.getTaskCount();
        mPresent.showRedPoint();
    }

    @Override
    public void showRedPoint(TaskCountBean redCountBean) {
        hideDefaultLoading();
        if (redCountBean.getUnReadAnnouncementNum() == 0 && redCountBean.getUnReadMsgNum() == 0) {
            ivMessage.setImageResource(R.mipmap.icon_me_meesage_no);
        } else {
            ivMessage.setImageResource(R.mipmap.icon_me_meesage);
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


    @Override
    public void getCertificateMsg(CertificationOut certificationOut, boolean isCertificate) {

        if (certificationOut == null) {
            //未认证
            cerStatus = getString(R.string.no_cer);
            remindString = getString(R.string.no_certified);
            imgResId = R.mipmap.go_pass;
            btText = getString(R.string.go_cer);
//          账户未认证，去认证
        } else {
            //认证状态 1审核中 2审核通过 3审核拒绝
            authStatus = certificationOut.getAuthStatus();
            if (authStatus == 1 || authStatus == 4) {
                //审核中
                cerStatus = getString(R.string.cer_ing);
                remindString = getString(R.string.cer_waiting);
                imgResId = R.mipmap.pass_ing;
                btText = getString(R.string.go_watch);
            } else if (authStatus == 3) {
                //审核拒绝
                cerStatus = getString(R.string.no_pass);
                remindString = getString(R.string.not_pass);
                imgResId = R.mipmap.no_pass;
                btText = getString(R.string.re_go_cer);
            }
        }
        tvStatus.setText(cerStatus);

    }


    @Override
    public void getUserData(UserInfoBean userInfoBean) {
        Map<Object, Object> map = userInfoBean.getResult();
        double frozenAmount = (Double) map.get("frozenAmount"); //是 bigdecimal 冻结金额
        double balance = (Double) map.get("balance");//是 bigdecimal 账户余额
        int authStatus = ((Double) map.get("authStatus")).intValue();// 是 int 认证状态 1未认证 2审核中3已认证4审核未通过
        Double userStatus = (Double) map.get("userStatus");//是 int 1 正常 2 黑名单 3 黑名单审核中

        String userNo = (String) map.get("userNo");

        if (authStatus != 3) { //未认证

        } else if (userStatus == 1.0) {  //正常

        } else if (userStatus == 2.0) {  //账户异常

        } else if (userStatus == 3.0) { //黑名单审核中

        } else if (balance <= 0) {  //余额不足

        }
        //缓存此状态
        aCache.put(SharedPreferencesTool.CERTIFICATE_STATUS, authStatus + "");
        aCache.put(SharedPreferencesTool.USER_STATUS, userStatus + "");
    }


    @Override
    public void getMyMoney(MyMoneyEvent myMoneyEvent) {
        balance = myMoneyEvent.balance;
        commission = myMoneyEvent.commission;
        frozenAmount = myMoneyEvent.frozenAmount;
        subCount = myMoneyEvent.subCount;
        userHead = myMoneyEvent.userHead;
        upMoney = myMoneyEvent.upMoney;
        moreMoney = myMoneyEvent.moreMoney;


        tvAccountFroze.setText(CommonUtils.formatFloatNumber(frozenAmount));
        tvAccountBalance.setText(CommonUtils.formatFloatNumber(balance));
        tvAccountCommission.setText(commission + "");


        if (!TextUtils.isEmpty(userHead)) {
            L.e("头像地址是：" + userHead);
            GlideUtil.getInstance().loadHeadImage(this, ivHeadPortrait, userHead, true);
        } else {
            Glide.with(this).load(R.mipmap.head_default).into(ivHeadPortrait);
        }
        tvPhone.setText("手机号:" + myMoneyEvent.phone);


    }

    @Override
    public void getTaskCount(TaskCountBean taskCountBean) {
        throwingTaskCount = taskCountBean.getThrowingTaskCount();
        examineTaskCount = taskCountBean.getExamineTaskCount();
        waitThrowTaskCount = taskCountBean.getWaitThrowTaskCount();
        taskAdapter.notifyDataSetChanged();
    }


    @Override
    protected MePresent<IMeView> createPresent() {
        return new MePresent<>(this);
    }


    class Adapter extends BaseQuickAdapter<HomeBean, BaseViewHolder> {

        public Adapter(@Nullable List<HomeBean> data) {
            super(R.layout.item_gv_me, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, HomeBean item) {
            helper.setImageResource(R.id.iv_logo, item.getPic());
            helper.setText(R.id.tv_name, item.getName());

            BGABadgeFrameLayout mBadgeView = helper.getView(R.id.badge_view);

            switch (item.getName()) {
                case "审核中":

                    if (examineTaskCount != 0) {
                        mBadgeView.showTextBadge(examineTaskCount + "");
                    }

                    break;
                case "待投放":
                    if (waitThrowTaskCount != 0) {
                        mBadgeView.showTextBadge(waitThrowTaskCount + "");
                    }
                    break;
                case "投放中":
                    if (throwingTaskCount != 0) {
                        mBadgeView.showTextBadge(throwingTaskCount + "");
                    }
                    break;
                case "投放结束":

                    break;
                case "审核失败":
                    break;
            }
        }
    }


    public static final int PHOTOZOOM = 0; // 相册/拍照

    //选择照片
    private void choosePhoto() {
        if (CommonUtils.haveSDCard()) {
            if (hasPermisson()) {
                Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);   //创建打开相册的意图对象,选择照片
                openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");//传入数据(图片uri,图片类型)
                startActivityForResult(openAlbumIntent, PHOTOZOOM);   //开启
            } else {
                requestPermission();
            }
        } else {
            ToastUtils.showShort("没有SD卡");
        }
    }

    /**
     * 请求拍照的权限
     */
    @TargetApi(23)
    public void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission_group.CONTACTS,
                        Manifest.permission_group.MICROPHONE,
                        Manifest.permission_group.SMS},
                0);
    }

    /**
     * 是否有拍照的权限
     */
    @TargetApi(23)
    public boolean hasPermisson() {
        boolean b1 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean b2 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        boolean b3 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission.CAMERA);
        boolean b4 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission_group.CONTACTS);
        boolean b5 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission_group.MICROPHONE);
        boolean b6 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission_group.SMS);
        return b1 && b2 && b3 && b4 && b5 && b6;
    }

    Bitmap bitmap1 = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = null;
        switch (requestCode) {
            case PHOTOZOOM://相册
                if (resultCode != RESULT_OK) {
                    return;
                }
                if (data == null) {
                    return;
                }
                cropImageUri(data.getData(), 720, 720, CROP_SMALL_PICTURE);
                break;

            case CROP_SMALL_PICTURE:
                //这里是调用图片库裁剪后的返回
                //可以参照裁剪方法，里面已经指定了uri，所以在这里，直接可以从里面取uri，然后获取bitmap，并且设置到imageview
                try {
                    bitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    // TODO: 2018/4/11 压缩图片
//                    compressImg(bitmap1);
                    //上传图片
                    if (bitmap1 != null) {
                        File file = new File(IMAGE_FILE_LOCATION_Path);
                        if (file.exists()) {
                            mPresent.uploadImg(2, IMAGE_FILE_LOCATION_Path);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //是否裁剪
        intent.putExtra("crop", "true");
//        //设置xy的裁剪比例
        //设置输出的宽高
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        //是否缩放
        intent.putExtra("scale", false);
        //输入图片的Uri，指定以后，可以在这个uri获得图片
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //是否返回图片数据，可以不用，直接用uri就可以了
        intent.putExtra("return-data", false);
        //设置输入图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //是否关闭面部识别
        intent.putExtra("noFaceDetection", true); // no face detection
        //启动
        startActivityForResult(intent, requestCode);
    }
}
