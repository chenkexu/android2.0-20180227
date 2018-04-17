package com.orientdata.lookforcustomers.presenter;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.bean.BalanceDetailBean;
import com.orientdata.lookforcustomers.bean.CommissionListBean;
import com.orientdata.lookforcustomers.bean.ErrBean;
import com.orientdata.lookforcustomers.bean.MyInfoBean;
import com.orientdata.lookforcustomers.event.BalanceDetailEvent;
import com.orientdata.lookforcustomers.event.BalanceListEvent;
import com.orientdata.lookforcustomers.event.CommissionListEvent;
import com.orientdata.lookforcustomers.event.CommissionVertificateEvent;
import com.orientdata.lookforcustomers.event.MyMoneyEvent;
import com.orientdata.lookforcustomers.event.UpdateSmsStateEvent;
import com.orientdata.lookforcustomers.event.UpdateSmsTimeEvent;
import com.orientdata.lookforcustomers.event.UpdateToBalanceEvent;
import com.orientdata.lookforcustomers.model.ICommissionModel;
import com.orientdata.lookforcustomers.model.ISendSmsModel;
import com.orientdata.lookforcustomers.model.imple.CommissionModelImple;
import com.orientdata.lookforcustomers.model.imple.SendSmsModelImple;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;
import com.orientdata.lookforcustomers.util.CommonUtils;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.mine.ICommissionView;
import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.Map;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * Created by wy on 2017/11/18.
 * 佣金
 */

public class CommissionPresent<T> extends BasePresenter<ICommissionView> {
    private ICommissionView mCommissionView;
    private ICommissionModel mCommissionModel = new CommissionModelImple();
    private ISendSmsModel mSendSmsModel = new SendSmsModelImple();


    public CommissionPresent(ICommissionView mCommissionView) {
        this.mCommissionView = mCommissionView;
    }
    public void updateToBalance(){
        if(mCommissionModel!=null){
            mCommissionView.showLoading();
            mCommissionModel.updateToBalance(new ICommissionModel.Complete() {
                @Override
                public void onSuccess(ErrBean errBean) {
                    mCommissionView.hideLoading();
                    UpdateToBalanceEvent updateToBalanceEvent = new UpdateToBalanceEvent();
                    updateToBalanceEvent.err = errBean;
                    EventBus.getDefault().post(updateToBalanceEvent);
                }

                @Override
                public void onError(int code, String message) {
                    mCommissionView.hideLoading();
                    ToastUtils.showShort(message);
                }
            });
        }
    }
    public void updateUserCommission(BigDecimal commission, String zhiFu, String code, String codeId){
        if(mCommissionModel!=null){
            mCommissionView.showLoading();
            mCommissionModel.updateUserCommission(new ICommissionModel.Complete() {
                @Override
                public void onSuccess(ErrBean errBean) {
                    mCommissionView.hideLoading();
                    UpdateToBalanceEvent updateToBalanceEvent = new UpdateToBalanceEvent();
                    updateToBalanceEvent.err = errBean;
                    EventBus.getDefault().post(updateToBalanceEvent);
                }

                @Override
                public void onError(int code, String message) {
                    mCommissionView.hideLoading();
                    ToastUtils.showShort(message);
                }
            },commission,zhiFu,code,codeId);
        }
    }
    public void commissionVertificate(BigDecimal commission, String zhiFu){
        if(mCommissionModel!=null){
            mCommissionView.showLoading();
            mCommissionModel.commissionVertificate(new ICommissionModel.Complete() {
                @Override
                public void onSuccess(ErrBean errBean) {
                    mCommissionView.hideLoading();
                    CommissionVertificateEvent commissionVertificateEvent = new CommissionVertificateEvent();
                    commissionVertificateEvent.err = errBean;
                    EventBus.getDefault().post(commissionVertificateEvent);
                }

                @Override
                public void onError(int code, String message) {
                    mCommissionView.hideLoading();
                    ToastUtils.showShort(message);
                }
            },commission,zhiFu);
        }
    }
    public void commissionList(String searchDate,int page,int size){
        if(mCommissionModel!=null){
            mCommissionView.showLoading();
            mCommissionModel.commissionList(new ICommissionModel.CommissionListComplete() {

                @Override
                public void onSuccess(CommissionListBean commissionListBean) {
                    mCommissionView.hideLoading();
                    CommissionListEvent commissionListEvent = new CommissionListEvent();
                    commissionListEvent.commissionListBean = commissionListBean;
                    EventBus.getDefault().post(commissionListEvent);
                }

                @Override
                public void onError(int code, String message) {
                    mCommissionView.hideLoading();
                    ToastUtils.showShort(message);
                }
            },searchDate,page,size);
        }
    }
    public void balanceList(String searchDate,int page,int size){
        if(mCommissionModel!=null){
            mCommissionView.showLoading();
            mCommissionModel.balanceList(new ICommissionModel.CommissionListComplete() {

                @Override
                public void onSuccess(CommissionListBean commissionListBean) {
                    mCommissionView.hideLoading();
                    BalanceListEvent balanceListEvent = new BalanceListEvent();
                    balanceListEvent.balanceListBean = commissionListBean;
                    EventBus.getDefault().post(balanceListEvent);
                }

                @Override
                public void onError(int code, String message) {
                    mCommissionView.hideLoading();
                    ToastUtils.showShort(message);
                }
            },searchDate,page,size);
        }
    }
    public void balanceDetail(int balanceHistoryId){
        if(mCommissionModel!=null){
            mCommissionView.showLoading();
            mCommissionModel.balanceDetail(new ICommissionModel.BalanceDetailComplete() {

                @Override
                public void onSuccess(BalanceDetailBean balanceDetailBean) {
                    mCommissionView.hideLoading();
                    BalanceDetailEvent balanceDetailEvent = new BalanceDetailEvent();
                    balanceDetailEvent.balanceDetailBean = balanceDetailBean;
                    EventBus.getDefault().post(balanceDetailEvent);
                }

                @Override
                public void onError(int code, String message) {
                    mCommissionView.hideLoading();
                    ToastUtils.showShort(message);
                }
            },balanceHistoryId);
        }
    }
    /**
     * 发送短信
     *
     * @param phoneNo
     */
    private String codeId = "";
    public void sendSms(String phoneNo) {
        if (TextUtils.isEmpty(phoneNo)) {
            ToastUtils.showShort("请输入手机号");
            return;
        }
        if (!CommonUtils.isPhoneNum(phoneNo)) {
            ToastUtils.showShort("请输入正确的手机号");
            return;
        }
        if (mCommissionView != null && mSendSmsModel != null) {
            mCommissionView.showLoading();
            mSendSmsModel.sendSms(phoneNo, codeId, new ISendSmsModel.SendSmsComplete() {
                @Override
                public void success(String id) {
                    mCommissionView.hideLoading();
                    codeId = id;
                    countDown();
                }

                @Override
                public void onError(int code, String message) {
                    mCommissionView.hideLoading();
                    ToastUtils.showShort(message);
                }
            });
        }
    }
    //倒计时开始
    private long totalTime = 60000;
    private long progress = 0;
    private CountDownTimer timer;

    private void countDown() {
        progress = totalTime;
        timer = new CountDownTimer(totalTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //不可再次获取验证码
                progress -= 1000;
                String currentTime = progress / 1000 + "s";
                UpdateSmsTimeEvent updateSmsTimeEvent = new UpdateSmsTimeEvent();
                updateSmsTimeEvent.isCall = false;
                updateSmsTimeEvent.surplusTime = currentTime;
                updateSmsTimeEvent.codeId = codeId;
                EventBus.getDefault().post(updateSmsTimeEvent);
            }

            @Override
            public void onFinish() {
                //可以再次获取验证码
                progress = totalTime;
                UpdateSmsTimeEvent updateSmsTimeEvent = new UpdateSmsTimeEvent();
                updateSmsTimeEvent.isCall = true;
                updateSmsTimeEvent.surplusTime = "0s";
                updateSmsTimeEvent.codeId = codeId;
                EventBus.getDefault().post(updateSmsTimeEvent);
            }
        };
        timer.start();
    }

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
    public void fecth() {

    }
}
