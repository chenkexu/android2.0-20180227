package com.orientdata.lookforcustomers.view.home.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.WangrunBaseFragment;
import com.orientdata.lookforcustomers.bean.AnOutBean;
import com.orientdata.lookforcustomers.bean.AnnouncementBean;
import com.orientdata.lookforcustomers.bean.BannerBean;
import com.orientdata.lookforcustomers.bean.BannerBeans;
import com.orientdata.lookforcustomers.bean.CertificationOut;
import com.orientdata.lookforcustomers.bean.URLBean;
import com.orientdata.lookforcustomers.bean.UserInfoBean;
import com.orientdata.lookforcustomers.event.MyMoneyEvent;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;
import com.orientdata.lookforcustomers.presenter.HomePresent;
import com.orientdata.lookforcustomers.runtimepermissions.PermissionsManager;
import com.orientdata.lookforcustomers.util.CommonUtils;
import com.orientdata.lookforcustomers.util.DateTool;
import com.orientdata.lookforcustomers.util.SharedPreferencesTool;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.banner.HomeViewPagerAdapterDefult;
import com.orientdata.lookforcustomers.view.banner.HomeViewPagerAdapterSucessful;
import com.orientdata.lookforcustomers.view.banner.RoolViewPager;
import com.orientdata.lookforcustomers.view.certification.fragment.ACache;
import com.orientdata.lookforcustomers.view.certification.impl.CertificationActivity;
import com.orientdata.lookforcustomers.view.findcustomer.CreateFindCustomerActivity;
import com.orientdata.lookforcustomers.view.home.AnnouncementDetailActivity;
import com.orientdata.lookforcustomers.view.home.AnnouncementListActivity;
import com.orientdata.lookforcustomers.view.home.IHomeView;
import com.orientdata.lookforcustomers.view.home.RechargeActivity;
import com.orientdata.lookforcustomers.view.mine.ShareToGetCommissionActivity;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.orientdata.lookforcustomers.widget.dialog.RemindDialog;
import com.viewpagerindicator.CirclePageIndicator;

import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * Created by wy on 2017/10/30.
 * 首页Fragment
 */

public class HomeFragment extends WangrunBaseFragment<IHomeView, HomePresent<IHomeView>>
        implements IHomeView, View.OnClickListener {
    private RelativeLayout rlCreate;
    private LinearLayout ll_fm_home_accountCertification;
    private MyTitle title;
    private TextView tv_user_status;
    private TextView tv_fm_home_accountBalance;
    private TextView tv_fm_home_blockBalance;
    private TextView tv_fm_home_PageTodayConsumeCoins;
    private TextView tv_fm_home_PageTodayExposureVolume;
    private TextView tv_fm_home_PageTodayClickVolume;
    private TextView tv_fm_home_SMSTodayConsumeCoins;
    private TextView tv_fm_home_SMSTodaySendVolume;
    private RoolViewPager mViewPager;
    private CirclePageIndicator mIndicator;
    private TextView tv_fm_home_LastestAnnouncementTopic;
    private TextView tv_fm_home_LastestAnnouncementTime;
    private RelativeLayout rl_announcement_lists;
    private LinearLayout ll_fm_home_consumerConsults;
    private LinearLayout btn_recharge;
    private LinearLayout ll_fm_home_accountRecharge;

    private List<String> imagerUrls = new ArrayList<String>();
    private List<String> imageClickUrls = new ArrayList<String>();
    HomeViewPagerAdapterSucessful s_adapter;
    private Handler handler;
    private FragmentActivity mActivity;
    private HomeViewPagerAdapterDefult myAdapter;
    private AnnouncementBean mTopAnnouncement;
    private ImageView iv_fm_home_LastestAnnouncementMore;

    //赚取佣金
    private LinearLayout ll_fm_home_secureCommissions;
    private boolean isReLogin = false;//是否是重新登录
    //    private boolean isRefresh = false;//充值和佣金提现等操作
    private ACache aCache = null;//数据缓存
    int authStatus = -1;//认证
    double userStatus = -1;//用户状态
    private boolean isCertificateNext = false;
    private TextView tv_useId; //标题栏中的id
    private SwipeRefreshLayout refreshLayout;
    private LinearLayout titleView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home2, container, false);
        initView(view);
        mActivity = getActivity();
        addDefultUrl();
        initTitle();
        getUserData();
        //添加下拉刷新
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                getUserData();
            }
        });
        return view;
    }



    /*  @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }*/

/*    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getUserData();
        }
    }*/

    /*@Override
    public void onStart() {
        super.onStart();
        getUserData();
    }*/

//    @Subscribe
//    public void reLoginResult(ReLoginEvent reLoginEvent) {
//        if (reLoginEvent != null && reLoginEvent.reLogin) {
//            getUserData();
//        }
//    }


//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.e("==","resume   home页 获取数据");
//        getUserData();
//    }

    @Subscribe
    public void refreshResult(MyMoneyEvent moneyEvent) {
        if (moneyEvent != null) {
//            isRefresh = moneyEvent.isRefresh;
            tv_fm_home_accountBalance.setText(getNum(moneyEvent.balance));
        }
    }

    /**
     * 对double数字进行处理 如果是整数 显示整数 如果是小数 保留两位
     *
     * @param num
     * @return
     */
    public String getNum(double num) {
        if (num % 1 == 0) {
            return (int) num + "";
        } else {
            BigDecimal b = new BigDecimal(num);
            return (b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "";
        }
    }

    public void getUserData() {
        showDefaultLoading();
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());

        OkHttpClientManager.postAsyn(HttpConstant.SELECT_USER_INFO, new OkHttpClientManager.ResultCallback<UserInfoBean>() {
            @Override
            public void onError(Exception e) {
                ToastUtils.showShort(e.getMessage());
                hideDefaultLoading();
            }

            @Override
            public void onResponse(UserInfoBean response) {
                hideDefaultLoading();
                if (response.getCode() == 0) {
                    if (response.getResult() == null) {
                        return;
                    }
                    Map<Object, Object> map = response.getResult();
                    double frozenAmount = (Double) map.get("frozenAmount"); //是 bigdecimal 冻结金额
                    tv_fm_home_blockBalance.setText(CommonUtils.formatFloatNumber(frozenAmount));
                   /* if (frozenAmount % 1 == 0) {
                        tv_fm_home_blockBalance.setText((int) frozenAmount + "");
                    } else {
                        tv_fm_home_blockBalance.setText(frozenAmount + "");
                    }*/
                    double balance = (Double) map.get("balance");//是 bigdecimal 账户余额
                    tv_fm_home_accountBalance.setText(CommonUtils.formatFloatNumber(balance));
                    /*if (balance % 1 == 0) {
                        //整数
                        tv_fm_home_accountBalance.setText((int) balance + "");
                    } else {
                        tv_fm_home_accountBalance.setText(balance + "");
                    }*/
                    userStatus = (Double) map.get("userStatus");//是 int 1 正常 2 黑名单 3 黑名单审核中

                    String userNo = (String) map.get("userNo"); //是 String 用户编号
//                    title.setTitleName("ID:" + userNo);

                    tv_useId.setText(userNo);
                    BigDecimal todaySmsMoney = BigDecimal.valueOf((Double) map.get("todaySmsMoney")); //是 bigdecimal 今日短信消费
                    tv_fm_home_SMSTodayConsumeCoins.setText(todaySmsMoney.intValue() + "");
                    double successCount = (Double) map.get("successCount");//是 int 今日下发量
                    tv_fm_home_SMSTodaySendVolume.setText((int) successCount + "");
                    BigDecimal todayPageMoney = BigDecimal.valueOf((Double) map.get("todayPageMoney"));//是 bigdecimal 今日页面消费
                    tv_fm_home_PageTodayConsumeCoins.setText(todayPageMoney.intValue() + "");
                    double clickCount = (Double) map.get("clickCount"); //是 int 今日点击量
                    tv_fm_home_PageTodayClickVolume.setText((int) clickCount + "");
                    double showCount = (Double) map.get("showCount");// 是 int 今日曝光量
                    tv_fm_home_PageTodayExposureVolume.setText((int) showCount + "");
                    authStatus = ((Double) map.get("authStatus")).intValue();// 是 int 认证状态 1未认证 2审核中3已认证4审核未通过
                    if (authStatus != 3) {
                        tv_user_status.setVisibility(View.VISIBLE);
                        tv_user_status.setText("未认证");
                    } else if (userStatus == 1.0) {
                        tv_user_status.setVisibility(View.GONE);
                        tv_user_status.setText("正常");
                    } else if (userStatus == 2.0) {
                        tv_user_status.setVisibility(View.VISIBLE);
                        tv_user_status.setText("账户异常");
                    } else if (userStatus == 3.0) {
                        tv_user_status.setVisibility(View.GONE);
                        tv_user_status.setText("账户异常");
                    }else if (balance <= 0){
                        tv_user_status.setVisibility(View.VISIBLE);
                        tv_user_status.setText("余额不足");
                    }
                    //缓存此状态
                    aCache.put(SharedPreferencesTool.CERTIFICATE_STATUS, authStatus + "");
                    aCache.put(SharedPreferencesTool.USER_STATUS, userStatus + "");
                }
            }
        }, map);
        showDefaultLoading();
        //获取banner信息
        map = new MDBasicRequestMap();
        //map.put("userId", UserDataManeger.getInstance().getUserId());
        OkHttpClientManager.postAsyn(HttpConstant.SELECT_BANNER_INFO, new OkHttpClientManager.ResultCallback<BannerBeans>() {
            @Override
            public void onError(Exception e) {
                ToastUtils.showShort(e.getMessage());
                hideDefaultLoading();
            }

            @Override
            public void onResponse(BannerBeans response) {
                hideDefaultLoading();
                if (response.getCode() == 0) {
                    if (response.getResult() == null) {
                        return;
                    }
                    List<BannerBean> list = response.getResult();
                    addSucessful(list);
                } else {
                    addDefultUrl();
                }
            }
        }, map);
        showDefaultLoading();
        //获取置顶公告
        map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());
        OkHttpClientManager.postAsyn(HttpConstant.SELECT_TOP_ANNOUNCEMENT, new OkHttpClientManager.ResultCallback<AnOutBean>() {
            @Override
            public void onError(Exception e) {
                ToastUtils.showShort(e.getMessage());
                hideDefaultLoading();
            }

            @Override
            public void onResponse(AnOutBean re) {
                hideDefaultLoading();
                if (re.getCode() == 0) {
                    mTopAnnouncement = re.getResult();
                    if (mTopAnnouncement != null && !TextUtils.isEmpty(mTopAnnouncement.getTitle())) {
                        tv_fm_home_LastestAnnouncementTopic.setText(mTopAnnouncement.getTitle());
                    }
                    if (mTopAnnouncement != null && mTopAnnouncement.getCreateDate() != null) {
                        tv_fm_home_LastestAnnouncementTime.setText(DateTool.parseDate2Str(mTopAnnouncement.getCreateDate(), "yyyy-MM-dd"));
                    }
                }

            }
        }, map);
    }


    private void addSucessful(List<BannerBean> data) {
        if (data == null || data.size() <= 0) {
            return;
        }
        imagerUrls.clear();
        for (int i = 0; i < data.size(); i++) {
            imagerUrls.add(data.get(i).getImageId());
            imageClickUrls.add(data.get(i).getImageUrl());
        }
        // 展示数据
        if (s_adapter == null) {
            s_adapter = new HomeViewPagerAdapterSucessful(mActivity, imagerUrls, imageClickUrls, handler);
            mViewPager.setAdapter(s_adapter);
        } else {
            s_adapter.notifyDataSetChanged();
        }
    }

    private void addDefultUrl() {
        // TODO: 2017/6/28
        initviewpager();

    }

    //初始化ViewPager轮播图页面
    private void initviewpager() {
        myAdapter = new HomeViewPagerAdapterDefult(mActivity);
        mViewPager.setAdapter(myAdapter);
        mIndicator.setViewPager(mViewPager);    //点作为指示器使用,肯定是与viewPager进行绑定
        mIndicator.setSnap(true);// 快照，使用快照的方式显示点

        //设置控件小点和轮播图图片默认最初显示处下标为0
        mIndicator.onPageSelected(0);
        mViewPager.setCurrentItem(0);

        if (handler == null) {
            // viewpager切换下一个界面的操作

            handler = new Handler() {
                public void handleMessage(android.os.Message msg) {
                    //判断viewPager是否为空,不为空就进行切换的实现,为空了就清除消息
                    if (mViewPager != null) {
                        int currentItem = mViewPager.getCurrentItem();// 获取当前显示ViewPager界面的索引
                        //如果当前下表索引为图片的最后一张,就将下标置为0
                        if (currentItem == imagerUrls.size() - 1) {
                            currentItem = 0;
                        } else {
                            currentItem++;
                        }
                        // 设置viewpager显示下一个界面
                        mViewPager.setCurrentItem(currentItem);
                        // 切换一次完成，还要紧接着切换第二次
                        handler.sendEmptyMessageDelayed(0, 3000);
                    } else {
                        handler.removeMessages(0);
                        handler = null;
                    }
                }

                ;
            };
            handler.sendEmptyMessageDelayed(0, 3000);// 只有执行此方法，才会发送延迟消息，不执行就不发送
        }


        mViewPager.addOnPageChangeListener(new CirclePageIndicator(getContext()) {
            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initTitle() {
        tv_useId.setText("---");
//        title.setTitleName("ID:" + "---");
//        title.setImageBack(this);
//        title.setRightText(R.string.test);
//        title.setRightTextClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivityForResult(new Intent(getBaseContext(), TestPhoneSettingActivity.class), 1);
//            }
//        });
    }

    private void initView(View view) {
        aCache = ACache.get(getContext());

        tv_user_status = view.findViewById(R.id.tv_user_status);
        tv_useId = view.findViewById(R.id.tv_useId);

        tv_fm_home_accountBalance = view.findViewById(R.id.tv_fm_home_accountBalance);
        titleView = view.findViewById(R.id.title);
        tv_fm_home_blockBalance = view.findViewById(R.id.tv_fm_home_blockBalance);
        tv_fm_home_PageTodayConsumeCoins = view.findViewById(R.id.tv_fm_home_PageTodayConsumeCoins);
        tv_fm_home_PageTodayExposureVolume = view.findViewById(R.id.tv_fm_home_PageTodayExposureVolume);
        tv_fm_home_PageTodayClickVolume = view.findViewById(R.id.tv_fm_home_PageTodayClickVolume);
        tv_fm_home_SMSTodayConsumeCoins = view.findViewById(R.id.tv_fm_home_SMSTodayConsumeCoins);
        tv_fm_home_SMSTodaySendVolume = view.findViewById(R.id.tv_fm_home_SMSTodaySendVolume);
        refreshLayout = view.findViewById(R.id.refreshLayout);
//        title = view.findViewById(R.id.fm_home_title_id);
        rlCreate = view.findViewById(R.id.rlCreate);
        ll_fm_home_accountCertification = view.findViewById(R.id.ll_fm_home_accountCertification);
        mViewPager = view.findViewById(R.id.home_Viewpager);
        mIndicator = view.findViewById(R.id.home_Indicator);
        tv_fm_home_LastestAnnouncementTopic = view.findViewById(R.id.tv_fm_home_LastestAnnouncementTopic);
        tv_fm_home_LastestAnnouncementTopic.setOnClickListener(this);
        tv_fm_home_LastestAnnouncementTime = view.findViewById(R.id.tv_fm_home_LastestAnnouncementTime);
        ll_fm_home_accountCertification.setOnClickListener(this);
        rl_announcement_lists = view.findViewById(R.id.rl_fm_home_announcementlists);
        rl_announcement_lists.setOnClickListener(this);
        ll_fm_home_consumerConsults = view.findViewById(R.id.ll_fm_home_consumerConsults);
        ll_fm_home_consumerConsults.setOnClickListener(this);
        rlCreate.setOnClickListener(this);
        btn_recharge = view.findViewById(R.id.btn_recharge);
        btn_recharge.setOnClickListener(this);
        ll_fm_home_accountRecharge = view.findViewById(R.id.ll_fm_home_accountRecharge);
        ll_fm_home_accountRecharge.setOnClickListener(this);
        ll_fm_home_secureCommissions = view.findViewById(R.id.ll_fm_home_secureCommissions);
        ll_fm_home_secureCommissions.setOnClickListener(this);
        iv_fm_home_LastestAnnouncementMore = view.findViewById(R.id.iv_fm_home_LastestAnnouncementMore);
        iv_fm_home_LastestAnnouncementMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.ll_fm_home_accountCertification:
                mPresent.getCertificateMsg(true);
                break;
            case R.id.rlCreate:
                if (userStatus == 2.0) {
                    //黑名单
                    ToastUtils.showShort("账户异常，请联系客服");
                } else {
                    ACache.get(getContext()).remove(SharedPreferencesTool.DIRECTION_HISTORY);
//                    startActivity(new Intent(getContext(), MessageTaskActivity.class));
                    startActivity(new Intent(getContext(), CreateFindCustomerActivity.class));
                }
//                mPresent.getCertificateMsg(false);
                break;
            case R.id.tv_fm_home_LastestAnnouncementTopic://公告
                intent = new Intent(getContext(), AnnouncementDetailActivity.class);
                if (mTopAnnouncement == null) {
                    //ToastUtils.showShort("操作非法");
                }
                intent.putExtra("mTopAnnouncement", mTopAnnouncement);
                startActivity(intent);
                break;
            case R.id.rl_fm_home_announcementlists://公告
                intent = new Intent(getContext(), AnnouncementDetailActivity.class);
                if (mTopAnnouncement == null) {
                    //ToastUtils.showShort("操作非法");
                }
                intent.putExtra("mTopAnnouncement", mTopAnnouncement);
                startActivity(intent);
                break;
            case R.id.iv_fm_home_LastestAnnouncementMore://公告列表
                intent = new Intent(getContext(), AnnouncementListActivity.class);
                //intent.putExtra("mTopAnnouncement", mTopAnnouncement);
                startActivity(intent);
                break;
            case R.id.ll_fm_home_consumerConsults:
                final String url = "mqqwpa://im/chat?chat_type=wpa&uin=2280249239";

                if (checkApkExist(getActivity(), "com.tencent.mobileqq")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } else {
                    ToastUtils.showShort("本机未安装QQ应用,请下载安装。");
                }
                break;
            case R.id.btn_recharge:
                intent = new Intent(getActivity(), RechargeActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_fm_home_accountRecharge:

//                startActivity(new Intent(getActivity(), TestActivity.class));
                // TODO: 2018/4/24 进入测试页面
                intent = new Intent(getActivity(), RechargeActivity.class);
                startActivity(intent);

                break;
            case R.id.ll_fm_home_secureCommissions:
                if (hasSharePermisson()) {
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
                                    Intent intent1 = new Intent(getActivity(), ShareToGetCommissionActivity.class);
                                    intent1.putExtra("url", url);
                                    startActivity(intent1);
                                } else {
                                    ToastUtils.showShort("获取分享链接失败");
                                }
                            }
                            hideDefaultLoading();
                        }
                    }, map);
                } else {
                    requestSharePermission();
                }
                break;
        }

    }

    /**
     * 是否有拍照的权限
     */
    @TargetApi(23)
    public boolean hasSharePermisson() {
        boolean b1 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission.ACCESS_NETWORK_STATE);
        boolean b2 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission.ACCESS_WIFI_STATE);
        boolean b3 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission.READ_PHONE_STATE);
        boolean b4 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean b5 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        boolean b6 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission.INTERNET);
        boolean b7 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        boolean b8 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);
        boolean b9 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
        return b1 && b2 && b3 && b4 && b5 && b6 && b7 && b8 && b9;
    }

    /**
     * 请求拍照的权限
     */
    @TargetApi(23)
    public void requestSharePermission() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS},
                0);
    }

    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
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
        String cerStatus = "";
        String remindString = "";
        int imgResId = 0;
        String btText = "";
        int authStatus = -1;
        if (certificationOut == null) {
            //未认证
            cerStatus = getString(R.string.no_cer);
            remindString = getString(R.string.no_certified);
            imgResId = R.mipmap.go_pass;
            btText = getString(R.string.go_cer);
        } else {
            //认证状态 1审核中 2审核通过 3审核拒绝
            authStatus = certificationOut.getAuthStatus();
            Logger.d("authStatus:"+authStatus);
            // TODO: 2018/4/17 测试弹窗
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
        if (authStatus != 2) {
            final RemindDialog dialog = new RemindDialog(getContext(), "", remindString, imgResId, btText);
            dialog.setClickListenerInterface(new RemindDialog.ClickListenerInterface() {
                @Override
                public void doCertificate() {
                    dialog.dismiss();
                    startActivity(new Intent(getContext(), CertificationActivity.class));
                }
            });
            dialog.setCancelable(true);
            dialog.show();
        } else {
            //审核通过
            if (isCertificate) {
                startActivity(new Intent(getContext(), CertificationActivity.class));
            } else {
                ACache.get(getContext()).remove(SharedPreferencesTool.DIRECTION_HISTORY);
                startActivity(new Intent(getContext(), CreateFindCustomerActivity.class));

            }
        }
    }

    @Override
    protected HomePresent<IHomeView> createPresent() {
        return new HomePresent<>(this);
    }
}
