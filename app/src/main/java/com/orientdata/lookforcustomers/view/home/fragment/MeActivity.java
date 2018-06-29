package com.orientdata.lookforcustomers.view.home.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
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
import com.orientdata.lookforcustomers.widget.dialog.RemindDialog;
import com.qiniu.android.common.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.badgeview.BGABadgeFrameLayout;
import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

import static com.orientdata.lookforcustomers.R.id.tv_company_name;


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
    @BindView(tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.iv_back)
    ImageView ivBack;

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
    private  String btText = "";
    private int authStatus = -1;
    private int throwingTaskCount;
    private int examineTaskCount;
    private int waitThrowTaskCount;
    private Adapter taskAdapter;


    @OnClick({R.id.tv_more_task, R.id.iv_message, R.id.iv_back,R.id.linear_company,
            R.id.ll_account_balance, R.id.ll_account_froze, R.id.ll_account_commission})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_more_task:
                intent = new Intent(MeActivity.this, TaskListActivity.class);
                startActivity(intent);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {
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


    private void showAuthenticationStatus(){
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
        aCache = ACache.get(this);
        LoginResultBean.UserBean user = (LoginResultBean.UserBean) SharedPreferencesTool.getInstance().getObjectFromShare(SharedPreferencesTool.user);
        tvCompanyName.setText("ID: "+user.getUserNo()+"");
        showDefaultLoading();
        //获取账号 佣金 和 余额
        mPresent.getCommission();
        mPresent.getCertificateMsg(true);
        mPresent.getTaskCount();
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
        hideDefaultLoading();
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
            L.e("头像地址是："+userHead);
            GlideUtil.getInstance().loadHeadImage(this,ivHeadPortrait,userHead,true);
        } else {
            Glide.with(this).load(R.mipmap.head_default).into(ivHeadPortrait);
        }
        tvPhone.setText("手机号:"+myMoneyEvent.phone);





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

                    if (examineTaskCount!=0) {
                        mBadgeView.showTextBadge(examineTaskCount+"");
                    }

                    break;
                case "待投放":
                    if (waitThrowTaskCount!=0) {
                        mBadgeView.showTextBadge(waitThrowTaskCount+"");
                    }
                    break;
                case "投放中":
                    if (throwingTaskCount!=0) {
                        mBadgeView.showTextBadge(throwingTaskCount+"");
                    }
                    break;
                case "投放结束":

                    break;
                case "审核失败":
                    break;
            }
        }
    }
}
