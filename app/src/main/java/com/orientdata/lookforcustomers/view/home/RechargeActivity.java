package com.orientdata.lookforcustomers.view.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.WangrunBaseActivity;
import com.orientdata.lookforcustomers.bean.ChargeMinMoneyBean;
import com.orientdata.lookforcustomers.bean.MessageTypeBean;
import com.orientdata.lookforcustomers.bean.MyInfoBean;
import com.orientdata.lookforcustomers.bean.PayBean;
import com.orientdata.lookforcustomers.bean.SelectWordBean;
import com.orientdata.lookforcustomers.bean.WrResponse;
import com.orientdata.lookforcustomers.event.MyMoneyEvent;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;
import com.orientdata.lookforcustomers.network.callback.WrCallback;
import com.orientdata.lookforcustomers.network.util.NetWorkUtils;
import com.orientdata.lookforcustomers.util.NoDoubleClickUtils;
import com.orientdata.lookforcustomers.util.RxUtils;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.orientdata.lookforcustomers.widget.dialog.RechargeDialog;
import com.orientdata.lookforcustomers.wxapi.WXPayEntryActivity;
import com.squareup.okhttp.OkHttpClient;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * 充值页面
 */
public class RechargeActivity extends WangrunBaseActivity implements View.OnClickListener {
    private MyTitle title;
    private EditText fet_recharge;
    private RelativeLayout rl_weixin;
    private RelativeLayout rl_zhifubao;
    private RadioButton rb_weixin;
    private RadioButton rb_zhifubao;
    private TextView tv_recharge_btn;
    private TextView tv_recharge200;
    private boolean is200Checked = true;
    private TextView tv_recharge500;
    private boolean is500Checked = false;
    private TextView tv_recharge1000;
    private boolean is1000Checked = false;
    private TextView tv_recharge3000;
    private boolean is3000Checked = false;
    private double cost = 1000;
    private static final int ZHIFUBAO = 1;
    private TextView tv_offline_recharge_btn;
    private Double mRechargeMinMoney = 0.0;
    //创建Hanler对象,处理支付宝支付回调
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {                 //判断属性为支付宝
                case ZHIFUBAO:
                    //创建支付结果对象,传入参数为handler在子线程通过sendMessage方法传递回来的obj结果属性
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();                      //支付结果信息

                    String resultStatus = payResult.getResultStatus();              //支付结果状态

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {  //支付成功
                        final RechargeDialog rechargeDialog = new RechargeDialog(RechargeActivity.this, "充值成功", "确定", R.mipmap.icon_dialog_success);
                        rechargeDialog.setClickListenerInterface(new RechargeDialog.ClickListenerInterface() {
                            @Override
                            public void doCertificate() {
                                rechargeDialog.dismiss();
                            }
                        });
                        rechargeDialog.show();
                        //更新个人信息
                        getCommission();
                        Toast.makeText(RechargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(RechargeActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else if(TextUtils.equals(resultStatus, "6001")) {		//支付取消
                           ToastUtils.showShort("取消支付");
                        } else {
                             //弹出充值失败的dialog
                            final RechargeDialog rechargeDialog = new RechargeDialog(RechargeActivity.this, "充值失败", "返回", R.mipmap.icon_dialog_error);
                            rechargeDialog.setClickListenerInterface(new RechargeDialog.ClickListenerInterface() {
                                @Override
                                public void doCertificate() {
                                    rechargeDialog.dismiss();
                                }
                            });
                            rechargeDialog.show();
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(RechargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;

            }
        }
    };
    private RechargeDialog rechargeDialog;

    /**
     * 获取账号 佣金 和 余额
     */
    public void getCommission(){
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());
//        mCommissionView.showLoading();
        OkHttpClientManager.postAsyn(HttpConstant.SELECT_SHOW_MY_INFO, new OkHttpClientManager.ResultCallback<MyInfoBean>() {
            @Override
            public void onError(Exception e) {
                ToastUtils.showShort(e.getMessage());
//                mCommissionView.hideLoading();
            }

            @Override
            public void onResponse(MyInfoBean response) {
                if (response.getCode() == 0) {
                    if (response.getResult() == null || response.getResult().size() <= 0) {
//                        mCommissionView.hideLoading();
                        return;
                    }
                    //TODO
                    Map<String,Object> mMapInfoBeans = response.getResult();

                    double commission = 0;
                    double balance = 0;
                    double frozenAmount = 0;
                    String subCount = "";
                           /* commission
                    是 bigdecimal 佣金*/
                    if (mMapInfoBeans.containsKey("commission")) {
                        commission = (Double) mMapInfoBeans.get("commission");
                    }

                    /*        balance
                    是 bigdecimal 余额*/
                    if (mMapInfoBeans.containsKey("balance")) {
                        balance = (Double) mMapInfoBeans.get("balance");
                    }
                    /*        subCount
                    是 String 每日可提现次数*/
                    if (mMapInfoBeans.containsKey("subCount")) {
                        subCount = (String) mMapInfoBeans.get("subCount");
                    }

                    /*        frozenAmount
                    是 bigdecimal 冻结金额*/

                    if (mMapInfoBeans.containsKey("frozenAmount")) {
                        frozenAmount = (Double) mMapInfoBeans.get("frozenAmount");
                    }

                    MyMoneyEvent moneyEvent = new MyMoneyEvent();
                    moneyEvent.commission = commission;
                    moneyEvent.balance = balance;
                    moneyEvent.subCount = subCount;
                    moneyEvent.frozenAmount = frozenAmount;
                    EventBus.getDefault().post(moneyEvent);
                }
//                mCommissionView.hideLoading();
            }
        }, map);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        initView();
        initTitle();
        getData();
        RxUtils.clickView(tv_recharge_btn)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toCharge();
                    }
                });

    }






    private void getData() {
        showDefaultLoading();
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId() + "");


        OkHttpClientManager.postAsyn(HttpConstant.CHARGE_MIN_MONEY, new OkHttpClientManager.ResultCallback<ChargeMinMoneyBean>() {
            @Override
            public void onError(Exception e) {
                hideDefaultLoading();
                ToastUtils.showShort(e.getMessage());
            }

            @Override
            public void onResponse(ChargeMinMoneyBean response) {
                hideDefaultLoading();
                if (response == null) {
                    return;
                }
                if (response.getCode() == 0) {
                    if (TextUtils.isEmpty(response.getResult())) {
                        return;
                    }
                    mRechargeMinMoney = Double.valueOf(response.getResult());
                    fet_recharge.setHint("单笔最小" + response.getResult());
                }
            }
        }, map);


    }

    private void initView() {
        tv_offline_recharge_btn = findViewById(R.id.tv_offline_recharge_btn);
        tv_offline_recharge_btn.setOnClickListener(this);
        title = findViewById(R.id.mt_title);
        fet_recharge = findViewById(R.id.fet_recharge);
        //fet_recharge.setFixedText("自定义金额(元):");
        fet_recharge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    is3000Checked = false;
                    is1000Checked = false;
                    is500Checked = false;
                    is200Checked = false;
                    tv_recharge200.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_recharge200.setBackground(getResources().getDrawable(R.drawable.selector_recharge_tv_unchecked));
                    tv_recharge500.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_recharge500.setBackground(getResources().getDrawable(R.drawable.selector_recharge_tv_unchecked));
                    tv_recharge3000.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_recharge3000.setBackground(getResources().getDrawable(R.drawable.selector_recharge_tv_unchecked));
                    tv_recharge1000.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_recharge1000.setBackground(getResources().getDrawable(R.drawable.selector_recharge_tv_unchecked));
                }
            }
        });
        rl_weixin = findViewById(R.id.rl_weixin);
        rl_weixin.setOnClickListener(this);
        rl_zhifubao = findViewById(R.id.rl_zhifubao);
        rl_zhifubao.setOnClickListener(this);
        rb_weixin = findViewById(R.id.rb_weixin);
        rb_weixin.setOnClickListener(this);
        rb_zhifubao = findViewById(R.id.rb_zhifubao);
        rb_zhifubao.setOnClickListener(this);
        tv_recharge_btn = findViewById(R.id.tv_recharge_btn);
        tv_recharge_btn.setOnClickListener(this);
        tv_recharge200 = findViewById(R.id.tv_recharge200);
        tv_recharge200.setOnClickListener(this);
        tv_recharge500 = findViewById(R.id.tv_recharge500);
        tv_recharge500.setOnClickListener(this);
        tv_recharge1000 = findViewById(R.id.tv_recharge1000);
        tv_recharge1000.setOnClickListener(this);
        tv_recharge3000 = findViewById(R.id.tv_recharge3000);
        tv_recharge3000.setOnClickListener(this);

    }

    private void initTitle() {
        title.setTitleName("充值");
        title.setImageBack(this);
       /* title.setRightText(R.string.test);
        title.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getBaseContext(), TestPhoneSettingActivity.class), 1);
            }
        });*/
    }

    private  void toCharge(){

        //验证数据
        String fet_value = fet_recharge.getText().toString().trim();
        Double value = null;
        if (TextUtils.isEmpty(fet_value)) {
            //输入数字为空
            if (!is200Checked && !is500Checked && !is3000Checked && !is1000Checked) {
                ToastUtils.showShort("请选择或者输入金额");
                return;
            }
            value = cost;
        } else {
            value = Double.valueOf(fet_value);
            if (value < mRechargeMinMoney) {
                ToastUtils.showShort("请输入正确金额,大于" + mRechargeMinMoney);
                return;
            }
        }
        if (!rb_zhifubao.isChecked() && !rb_weixin.isChecked()) {
            ToastUtils.showShort("请选择支付方式！");
            return;
        }
        //String  cost = fet_recharge.get
        if (rb_zhifubao.isChecked()) {
            ToastUtils.showShort("支付宝支付:" + value);
            //支付宝支付
            MDBasicRequestMap map = new MDBasicRequestMap();
            map.put("userId", UserDataManeger.getInstance().getUserId());
            map.put("cost", value + "");
            map.put("payType", 2 + "");

            OkHttpClientManager.postAsyn(HttpConstant.PAY_URL, new OkHttpClientManager.ResultCallback<PayBean>() {
                @Override
                public void onError(Exception e) {
                    ToastUtils.showShort(e.getMessage());
                }

                @Override
                public void onResponse(PayBean response) {
                    if (response.getCode() == 0) {
                        Map<Object, Object> result = response.getResult();
                        Map<String, String> order = (Map<String, String>) result.get("order");
                        Map<String, String> zfb = (Map<String, String>) result.get("zfb");
                        final String payInfo = zfb.get("signedString");
                        Runnable payRunnable = new Runnable() {

                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(RechargeActivity.this);
                                String result = alipay.pay(payInfo, true);

                                //创建message对象,设置msg属性
                                Message msg = new Message();
                                msg.what = ZHIFUBAO;             //属性:支付宝
                                msg.obj = result;                //结果
                                mHandler.sendMessage(msg);
                            }
                        };
                        // 必须异步调用
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();

                    }
                }
            }, map);
        }
        if (rb_weixin.isChecked()) {
            //微信支付
            ToastUtils.showShort("微信支付:" + value);

            MDBasicRequestMap map = new MDBasicRequestMap();
            map.put("userId", UserDataManeger.getInstance().getUserId());
            map.put("cost", value + "");
            map.put("payType", 1 + "");


            OkHttpClientManager.postAsyn(HttpConstant.PAY_URL, new OkHttpClientManager.ResultCallback<PayBean>() {
                @Override
                public void onError(Exception e) {
                    ToastUtils.showShort(e.getMessage());
                }

                @Override
                public void onResponse(PayBean response) {
                    if (response.getCode() == 0) {
                        Map<Object, Object> result = response.getResult();
                        Map<String, String> order = (Map<String, String>) result.get("order");
                        Map<String, String> wx = (Map<String, String>) result.get("wx");
                        // 将该app注册到微信
                        final IWXAPI msgApi = WXAPIFactory.createWXAPI(getApplication(), null);
                        boolean isRegister = msgApi.registerApp("wx94a5977e9ef0661b");
                        //调起支付
                        //IWXAPI api;
                        PayReq request = new PayReq();
                        request.appId = wx.get("appid").toString().trim();
                        request.partnerId = wx.get("partnerId");
                        request.prepayId = wx.get("prepayId");//"1101000000140415649af9fc314aa427";
                        request.packageValue = wx.get("_package");// "Sign=WXPay";
                        request.nonceStr = wx.get("nonceStr");// "1101000000140429eb40476f8896f4c9";
                        request.timeStamp = wx.get("timeStamp"); // "1398746574";
                        request.sign = wx.get("sign");//"7FFECB600D7157C5AA49810D2D8F28BC2811827B";
                        msgApi.sendReq(request);
                    }
                }
            }, map);

        }
    }
















    //微信支付的回调
   //在ui线程执行
    @Subscribe
    public void onEventMainThread(WXPayEntryActivity.wxPayResult payResultResult) {
//        Log.d("", payResultResult + "");
        Logger.d("收到了微信支付的回调"+payResultResult);
        if (this == null)
            return;

        switch (payResultResult) {
            case success:
                Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show();
                rechargeDialog = new RechargeDialog(RechargeActivity.this, "充值成功", "确定", R.mipmap.icon_dialog_success);
                rechargeDialog.setClickListenerInterface(new RechargeDialog.ClickListenerInterface() {
                    @Override
                    public void doCertificate() {
                        rechargeDialog.dismiss();
                    }
                });
                rechargeDialog.show();
                break;
            case cancle:
                Toast.makeText(this, "取消支付", Toast.LENGTH_SHORT).show();
                break;
            case fail:
                //弹出充值失败的dialog
                rechargeDialog = new RechargeDialog(RechargeActivity.this, "充值失败", "返回", R.mipmap.icon_dialog_error);
                rechargeDialog.setClickListenerInterface(new RechargeDialog.ClickListenerInterface() {
                    @Override
                    public void doCertificate() {
                        rechargeDialog.dismiss();
                    }
                });
                rechargeDialog.show();
                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                Toast.makeText(RechargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                break;
            case error:
                Toast.makeText(this, "未知", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_offline_recharge_btn:
                Intent offlineIntent = new Intent(this, OfflineRechargeActivity.class);
                offlineIntent.putExtra("mRechargeMinMoney", mRechargeMinMoney);
                startActivity(offlineIntent);
                break;
            case R.id.rl_weixin:
                if (rb_zhifubao.isChecked()) {
                    rb_zhifubao.setChecked(false);
                }
//                rb_weixin.setSelected(true);
                rb_weixin.setChecked(true);
                break;
            case R.id.rl_zhifubao:
                if (rb_weixin.isChecked()) {
                    rb_weixin.setChecked(false);
                }
                rb_zhifubao.setChecked(true);
                break;
            case R.id.rb_weixin:
                if (rb_zhifubao.isChecked()) {
                    rb_zhifubao.setChecked(false);
                }
//                rb_weixin.setSelected(true);
                rb_weixin.setChecked(true);
                break;
            case R.id.rb_zhifubao:
                if (rb_weixin.isChecked()) {
                    rb_weixin.setChecked(false);
                }
                rb_zhifubao.setChecked(true);
                break;
            case R.id.tv_recharge_btn://充值



                break;
            case R.id.tv_recharge200: //充1000元
                cost = 1000;
                is200Checked = !is200Checked;
                is500Checked = !is200Checked;
                is1000Checked = !is200Checked;
                is3000Checked = !is200Checked;
                if (is200Checked) {//选中
                    fet_recharge.setText("");
                    tv_recharge200.setTextColor(Color.parseColor("#ffffff"));
                    tv_recharge200.setBackground(getResources().getDrawable(R.drawable.selector_recharge_tv_checked));
                    tv_recharge500.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_recharge500.setBackground(getResources().getDrawable(R.drawable.selector_recharge_tv_unchecked));
                    tv_recharge1000.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_recharge1000.setBackground(getResources().getDrawable(R.drawable.selector_recharge_tv_unchecked));
                    tv_recharge3000.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_recharge3000.setBackground(getResources().getDrawable(R.drawable.selector_recharge_tv_unchecked));
                } else {
                    tv_recharge200.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_recharge200.setBackground(getResources().getDrawable(R.drawable.selector_recharge_tv_unchecked));
                }
                break;
            case R.id.tv_recharge500://充2000元
                cost = 2000;
                is500Checked = !is500Checked;
                is200Checked = !is500Checked;
                is1000Checked = !is500Checked;
                is3000Checked = !is500Checked;
                if (is500Checked) {//选中
                    fet_recharge.setText("");
                    tv_recharge500.setTextColor(Color.parseColor("#ffffff"));
                    tv_recharge500.setBackground(getResources().getDrawable(R.drawable.selector_recharge_tv_checked));
                    tv_recharge200.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_recharge200.setBackground(getResources().getDrawable(R.drawable.selector_recharge_tv_unchecked));
                    tv_recharge1000.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_recharge1000.setBackground(getResources().getDrawable(R.drawable.selector_recharge_tv_unchecked));
                    tv_recharge3000.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_recharge3000.setBackground(getResources().getDrawable(R.drawable.selector_recharge_tv_unchecked));
                } else {
                    tv_recharge500.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_recharge500.setBackground(getResources().getDrawable(R.drawable.selector_recharge_tv_unchecked));
                }
                break;
            case R.id.tv_recharge1000: //充3000元
                cost = 3000;
                is1000Checked = !is1000Checked;
                is500Checked = !is1000Checked;
                is200Checked = !is1000Checked;
                is3000Checked = !is1000Checked;
                if (is1000Checked) {//选中
                    fet_recharge.setText("");
                    tv_recharge1000.setTextColor(Color.parseColor("#ffffff"));
                    tv_recharge1000.setBackground(getResources().getDrawable(R.drawable.selector_recharge_tv_checked));

                    tv_recharge200.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_recharge200.setBackground(getResources().getDrawable(R.drawable.selector_recharge_tv_unchecked));
                    tv_recharge500.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_recharge500.setBackground(getResources().getDrawable(R.drawable.selector_recharge_tv_unchecked));
                    tv_recharge3000.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_recharge3000.setBackground(getResources().getDrawable(R.drawable.selector_recharge_tv_unchecked));
                } else {
                    tv_recharge1000.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_recharge1000.setBackground(getResources().getDrawable(R.drawable.selector_recharge_tv_unchecked));
                }
                break;
            case R.id.tv_recharge3000: //5000元
                cost = 5000;
                is3000Checked = !is3000Checked;
                is1000Checked = !is3000Checked;
                is500Checked = !is3000Checked;
                is200Checked = !is3000Checked;
                if (is3000Checked) {//选中
                    fet_recharge.setText("");
                    tv_recharge3000.setTextColor(Color.parseColor("#ffffff"));
                    tv_recharge3000.setBackground(getResources().getDrawable(R.drawable.selector_recharge_tv_checked));

                    tv_recharge200.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_recharge200.setBackground(getResources().getDrawable(R.drawable.selector_recharge_tv_unchecked));
                    tv_recharge500.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_recharge500.setBackground(getResources().getDrawable(R.drawable.selector_recharge_tv_unchecked));
                    tv_recharge1000.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_recharge1000.setBackground(getResources().getDrawable(R.drawable.selector_recharge_tv_unchecked));
                } else {
                    tv_recharge3000.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_recharge3000.setBackground(getResources().getDrawable(R.drawable.selector_recharge_tv_unchecked));
                }
                break;
        }
    }
}
