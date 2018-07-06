package com.orientdata.lookforcustomers.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.bean.PayBean;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;
import com.orientdata.lookforcustomers.util.MyOpenActivityUtils;
import com.orientdata.lookforcustomers.util.RxUtils;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.wxapi.WXPayEntryActivity;
import com.orientdata.lookforcustomers.zfb.Alipay;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * Created by ckx on 2018/6/27.
 */

public class ChargePopWindow extends Dialog implements View.OnClickListener{


    private static final int ZHIFUBAO = 1;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tv_zhifubao)
    TextView tvZhifubao;
    @BindView(R.id.tv_wechat)
    TextView tvWechat;
    @BindView(R.id.tv_charge_xieyi)
    TextView tvChargeXieyi;
    @BindView(R.id.tv_charge_money)
    TextView tvChargeMoney;
    private Context context;
    private Double value;




    public ChargePopWindow(final Context context,double value) {
        super(context, R.style.RemindDialog);// 必须调用父类的构造函数
        this.context = context;
        this.value = value;
        View contentView = LayoutInflater.from(context).inflate(R.layout.layout_charge_popwindow, null);
        setContentView(contentView);

        ButterKnife.bind(this,contentView);

        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay();
        //获取屏幕宽
        wlp.width = (int) (d.getWidth());
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.mypopwindow_anim_style);
        window.setAttributes(wlp);

        tvChargeMoney.setText("￥"+value);

        RxUtils.setOnClickListeners(new RxUtils.Action1<View>() {
            @Override
            public void onClick(View view) {
                dismiss();
                MyOpenActivityUtils.openCommonWebView(context, "充值协议", "http://www.orientdata.cn/activity.html");
            }
        },tvChargeXieyi);

    }



    private RechargeDialog rechargeDialog;

    //微信支付的回调
    //在ui线程执行
    @Subscribe
    public void onEventMainThread(WXPayEntryActivity.wxPayResult payResultResult) {
        Logger.d("收到了微信支付的回调"+payResultResult);
        if (this == null)
            return;
        switch (payResultResult) {
            case success:
                Toast.makeText(context, "成功", Toast.LENGTH_SHORT).show();
                break;
            case cancle:
                Toast.makeText(context, "取消支付", Toast.LENGTH_SHORT).show();
                break;
            case fail:
                //弹出充值失败的dialog
                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();
                break;
            case error:
                Toast.makeText(context, "未知", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }





    @OnClick({R.id.iv_close, R.id.tv_zhifubao, R.id.tv_wechat, R.id.tv_charge_xieyi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.tv_zhifubao:
                dismiss();
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
                            Map<String, String> zfb = (Map<String, String>) result.get("zfb");
                            final String payInfo = zfb.get("signedString");

                            new Alipay(context, payInfo, new Alipay.AlipayResultCallBack() {
                                @Override
                                public void onSuccess() {
                                    PayBean payBean = new PayBean();
                                    EventBus.getDefault().post(payBean);
                                    Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onDealing() {
                                    Toast.makeText(context, "支付处理中...", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(int error_code) {
                                    Toast.makeText(context, "支付错误", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancel() {
                                    Toast.makeText(context, "支付取消", Toast.LENGTH_SHORT).show();
                                }
                            }).doPay();
                        }
                    }
                }, map);
                break;
            case R.id.tv_wechat:
                dismiss();
                //微信支付
                ToastUtils.showShort("微信支付:" + value);

                MDBasicRequestMap map2 = new MDBasicRequestMap();
                map2.put("userId", UserDataManeger.getInstance().getUserId());
                map2.put("cost", value + "");
                map2.put("payType", 1 + "");

                OkHttpClientManager.postAsyn(HttpConstant.PAY_URL, new OkHttpClientManager.ResultCallback<PayBean>() {
                    @Override
                    public void onError(Exception e) {
                        ToastUtils.showShort(e.getMessage());
                    }

                    @Override
                    public void onResponse(PayBean response) {
                        if (response.getCode() == 0) {
                            Map<Object, Object> result = response.getResult();
                            Map<String, String> wx = (Map<String, String>) result.get("wx");
                            // 将该app注册到微信
                            final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
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
                }, map2);
                break;
            case R.id.tv_charge_xieyi:

                break;
        }
    }

    @Override
    public void onClick(View v) {

    }
}
