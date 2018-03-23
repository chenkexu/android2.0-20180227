package com.orientdata.lookforcustomers.model;

import com.orientdata.lookforcustomers.bean.Area;
import com.orientdata.lookforcustomers.bean.AreaOut;
import com.orientdata.lookforcustomers.bean.BalanceDetailBean;
import com.orientdata.lookforcustomers.bean.CommissionListBean;
import com.orientdata.lookforcustomers.bean.ErrBean;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by wy on 2017/11/18.
 * 佣金
 */

public interface ICommissionModel {
    void updateToBalance(Complete complete);//转入佣金
    void commissionVertificate(Complete complete,BigDecimal commission, String zhiFu);//佣金提现验证
    void updateUserCommission(Complete complete, BigDecimal commission, String zhiFu, String code, String codeId);//佣金提现
    void commissionList(CommissionListComplete complete,String searchDate,int page,int size);//佣金明细列表
    void balanceList(CommissionListComplete complete,String searchDate,int page,int size);//余额列表
    void balanceDetail(BalanceDetailComplete complete,int balanceHistoryId);//余额详情

    interface Complete {
        void onSuccess(ErrBean errBean);

        void onError(int code, String message);
    }
    interface CommissionListComplete {
        void onSuccess(CommissionListBean errBean);

        void onError(int code, String message);
    }
    interface BalanceDetailComplete {
        void onSuccess(BalanceDetailBean balanceDetailBean);

        void onError(int code, String message);
    }
    
}
