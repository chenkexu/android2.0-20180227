package com.orientdata.lookforcustomers.presenter;

import com.orientdata.lookforcustomers.model.ICommissionModel;
import com.orientdata.lookforcustomers.model.ISendSmsModel;
import com.orientdata.lookforcustomers.model.imple.CommissionModelImple;
import com.orientdata.lookforcustomers.model.imple.SendSmsModelImple;
import com.orientdata.lookforcustomers.view.mine.ISetView;

/**
 * Created by wy on 2017/11/18.
 */

public class SettingPresent<T> extends BasePresenter<ISetView> {
    private ISetView mSetView;
    private ICommissionModel mCommissionModel = new CommissionModelImple();
    private ISendSmsModel mSendSmsModel = new SendSmsModelImple();


    public SettingPresent(ISetView mSetView) {
        this.mSetView = mSetView;
    }
//    public void updateToBalance(){
//        if(mCommissionModel!=null){
//            mCommissionView.showLoading();
//            mCommissionModel.updateToBalance(new ICommissionModel.Complete() {
//                @Override
//                public void onSuccess(ErrBean errBean) {
//                    mCommissionView.hideLoading();
//                    UpdateToBalanceEvent updateToBalanceEvent = new UpdateToBalanceEvent();
//                    updateToBalanceEvent.err = errBean;
//                    EventBus.getDefault().post(updateToBalanceEvent);
//                }
//
//                @Override
//                public void onError(int code, String message) {
//                    mCommissionView.hideLoading();
//                    ToastUtils.showShort(message);
//                }
//            });
//        }
//    }
//    public void updateUserCommission(BigDecimal commission, String zhiFu, String code, String codeId){
//        if(mCommissionModel!=null){
//            mCommissionView.showLoading();
//            mCommissionModel.updateUserCommission(new ICommissionModel.Complete() {
//                @Override
//                public void onSuccess(ErrBean errBean) {
//                    mCommissionView.hideLoading();
//                    UpdateToBalanceEvent updateToBalanceEvent = new UpdateToBalanceEvent();
//                    updateToBalanceEvent.err = errBean;
//                    EventBus.getDefault().post(updateToBalanceEvent);
//                }
//
//                @Override
//                public void onError(int code, String message) {
//                    mCommissionView.hideLoading();
//                    ToastUtils.showShort(message);
//                }
//            },commission,zhiFu,code,codeId);
//        }
//    }
//    public void commissionVertificate(BigDecimal commission, String zhiFu){
//        if(mCommissionModel!=null){
//            mCommissionView.showLoading();
//            mCommissionModel.commissionVertificate(new ICommissionModel.Complete() {
//                @Override
//                public void onSuccess(ErrBean errBean) {
//                    mCommissionView.hideLoading();
//                    CommissionVertificateEvent commissionVertificateEvent = new CommissionVertificateEvent();
//                    commissionVertificateEvent.err = errBean;
//                    EventBus.getDefault().post(commissionVertificateEvent);
//                }
//
//                @Override
//                public void onError(int code, String message) {
//                    mCommissionView.hideLoading();
//                    ToastUtils.showShort(message);
//                }
//            },commission,zhiFu);
//        }
//    }
//    public void commissionList(String searchDate,int page,int size){
//        if(mCommissionModel!=null){
//            mCommissionView.showLoading();
//            mCommissionModel.commissionList(new ICommissionModel.CommissionListComplete() {
//
//                @Override
//                public void onSuccess(CommissionListBean commissionListBean) {
//                    mCommissionView.hideLoading();
//                    CommissionListEvent commissionListEvent = new CommissionListEvent();
//                    commissionListEvent.commissionListBean = commissionListBean;
//                    EventBus.getDefault().post(commissionListEvent);
//                }
//
//                @Override
//                public void onError(int code, String message) {
//                    mCommissionView.hideLoading();
//                    ToastUtils.showShort(message);
//                }
//            },searchDate,page,size);
//        }
//    }
//    public void balanceList(String searchDate,int page,int size){
//        if(mCommissionModel!=null){
//            mCommissionView.showLoading();
//            mCommissionModel.balanceList(new ICommissionModel.CommissionListComplete() {
//
//                @Override
//                public void onSuccess(CommissionListBean commissionListBean) {
//                    mCommissionView.hideLoading();
//                    BalanceListEvent balanceListEvent = new BalanceListEvent();
//                    balanceListEvent.balanceListBean = commissionListBean;
//                    EventBus.getDefault().post(balanceListEvent);
//                }
//
//                @Override
//                public void onError(int code, String message) {
//                    mCommissionView.hideLoading();
//                    ToastUtils.showShort(message);
//                }
//            },searchDate,page,size);
//        }
//    }
//    public void balanceDetail(int balanceHistoryId){
//        if(mCommissionModel!=null){
//            mCommissionView.showLoading();
//            mCommissionModel.balanceDetail(new ICommissionModel.BalanceDetailComplete() {
//
//                @Override
//                public void onSuccess(BalanceDetailBean balanceDetailBean) {
//                    mCommissionView.hideLoading();
//                    BalanceDetailEvent balanceDetailEvent = new BalanceDetailEvent();
//                    balanceDetailEvent.balanceDetailBean = balanceDetailBean;
//                    EventBus.getDefault().post(balanceDetailEvent);
//                }
//
//                @Override
//                public void onError(int code, String message) {
//                    mCommissionView.hideLoading();
//                    ToastUtils.showShort(message);
//                }
//            },balanceHistoryId);
//        }
//    }
//    /**
//     * 发送短信
//     *
//     * @param phoneNo
//     */
//    private String codeId = "";
//    public void sendSms(String phoneNo) {
//        if (TextUtils.isEmpty(phoneNo)) {
//            ToastUtils.showShort("请输入手机号");
//            return;
//        }
//        if (!CommonUtils.isPhoneNum(phoneNo)) {
//            ToastUtils.showShort("请输入正确的手机号");
//            return;
//        }
//        if (mCommissionView != null && mSendSmsModel != null) {
//            mCommissionView.showLoading();
//            mSendSmsModel.sendSms(phoneNo, codeId, new ISendSmsModel.SendSmsComplete() {
//                @Override
//                public void success(String id) {
//                    mCommissionView.hideLoading();
//                    codeId = id;
//                    countDown();
//                }
//
//                @Override
//                public void onError(int code, String message) {
//                    mCommissionView.hideLoading();
//                    ToastUtils.showShort(message);
//                }
//            });
//        }
//    }
//    //倒计时开始
//    private long totalTime = 60000;
//    private long progress = 0;
//    private CountDownTimer timer;
//
//    private void countDown() {
//        progress = totalTime;
//        timer = new CountDownTimer(totalTime, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                //不可再次获取验证码
//                progress -= 1000;
//                String currentTime = progress / 1000 + "s";
//                UpdateSmsTimeEvent updateSmsTimeEvent = new UpdateSmsTimeEvent();
//                updateSmsTimeEvent.isCall = false;
//                updateSmsTimeEvent.surplusTime = currentTime;
//                updateSmsTimeEvent.codeId = codeId;
//                EventBus.getDefault().post(updateSmsTimeEvent);
//            }
//
//            @Override
//            public void onFinish() {
//                //可以再次获取验证码
//                progress = totalTime;
//                UpdateSmsTimeEvent updateSmsTimeEvent = new UpdateSmsTimeEvent();
//                updateSmsTimeEvent.isCall = true;
//                updateSmsTimeEvent.surplusTime = "0s";
//                updateSmsTimeEvent.codeId = codeId;
//                EventBus.getDefault().post(updateSmsTimeEvent);
//            }
//        };
//        timer.start();
//    }

    @Override
    public void fecth() {

    }
}
