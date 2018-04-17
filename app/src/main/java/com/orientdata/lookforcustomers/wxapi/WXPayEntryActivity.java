package com.orientdata.lookforcustomers.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

//import com.app.EastOffice.event.WXPayEvent;
//import com.app.EastOffice.network.HttpConstant;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.bean.MyInfoBean;
import com.orientdata.lookforcustomers.event.MyMoneyEvent;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.home.RechargeActivity;
import com.orientdata.lookforcustomers.widget.dialog.RechargeDialog;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, HttpConstant.WX_APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case 0:
                getCommission();
                setPayResult(wxPayResult.success);
                break;
            case -1:
                setPayResult(wxPayResult.fail);
                break;
            case -2:
                setPayResult(wxPayResult.cancle);
                break;
            default:
                break;
        }


//        if (baseResp.errCode == 0) {
//            getCommission();
////            rechargeDialog.show();
//            Toast.makeText(getApplicationContext(), "支付成功！", Toast.LENGTH_LONG).show();
//            //EventBus.getDefault().post(new WXPayEvent(true));
//            finish();
//        } else {
////            rechargeDialog.show();
//            Toast.makeText(getApplicationContext(), "支付失败！", Toast.LENGTH_LONG).show();
//            //EventBus.getDefault().post(new WXPayEvent(false));
//            finish();
//        }
    }

    public enum wxPayResult {success, fail, cancle, error}

    private void setPayResult(wxPayResult payResultResult) {
        EventBus.getDefault().post(payResultResult);
        finish();
    }


    /**
     * 获取账号 佣金 和 余额
     */
    public void getCommission() {
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
                    Map<String, Object> mMapInfoBeans = response.getResult();

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
}