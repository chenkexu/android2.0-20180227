package com.orientdata.lookforcustomers.model.imple;

import android.text.TextUtils;

import com.orientdata.lookforcustomers.bean.BalanceDetailBean;
import com.orientdata.lookforcustomers.bean.CommissionListBean;
import com.orientdata.lookforcustomers.bean.ErrBean;
import com.orientdata.lookforcustomers.model.ICommissionModel;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;

import java.math.BigDecimal;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * Created by wy on 2017/11/18.
 * 我的佣金
 */

public class CommissionModelImple implements ICommissionModel {
    @Override
    public void updateToBalance(final Complete complete) {
        String url = HttpConstant.UPDATE_TO_BALANCE;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<ErrBean>() {
            @Override
            public void onError(Exception e) {
                complete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(ErrBean err) {
                if (err.getCode() == 0) {
                    complete.onSuccess(err);
                }
            }
        }, map);
    }

    @Override
    public void commissionVertificate(final Complete complete, BigDecimal commission, String zhiFu) {
        String url = HttpConstant.UPDATE_USER_COMMISSION_VERTIFICATE;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());
        map.put("commission", commission+"");
        map.put("zhiFu", zhiFu);
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<ErrBean>() {
            @Override
            public void onError(Exception e) {
                complete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(ErrBean err) {
                if (err.getCode() == 0) {
                    complete.onSuccess(err);
                }
            }
        }, map);
    }

    @Override
    public void updateUserCommission(final Complete complete, BigDecimal commission,String zhiFu,String code,String codeId) {
        String url = HttpConstant.UPDATE_USER_COMMISSION;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());
        map.put("commission", commission+"");
        map.put("zhiFu", zhiFu);
        map.put("code", code);
        map.put("codeId", codeId);
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<ErrBean>() {
            @Override
            public void onError(Exception e) {
                complete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(ErrBean err) {
                if (err.getCode() == 0) {
                    complete.onSuccess(err);
                }
            }
        }, map);
    }
    @Override
    public void commissionList(final CommissionListComplete complete, String searchDate, int page, int size) {
        String url = HttpConstant.COMMISSION_LIST;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());
        if(!TextUtils.isEmpty(searchDate)){
            map.put("searchDate", searchDate);
        }
        map.put("page", page+"");
        map.put("size", size+"");
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<CommissionListBean>() {
            @Override
            public void onError(Exception e) {
                complete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(CommissionListBean commissionListBean) {
                if (commissionListBean.getCode() == 0) {
                    complete.onSuccess(commissionListBean);
                }
            }
        }, map);
    }

    @Override
    public void balanceList(final CommissionListComplete complete, String searchDate, int page, int size) {
        String url = HttpConstant.BALANCE_LIST;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());
        if(!TextUtils.isEmpty(searchDate)){
            map.put("searchDate", searchDate);
        }
        map.put("page", page+"");
        map.put("size", size+"");
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<CommissionListBean>() {
            @Override
            public void onError(Exception e) {
                complete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(CommissionListBean commissionListBean) {
                if (commissionListBean.getCode() == 0) {
                    complete.onSuccess(commissionListBean);
                }
            }
        }, map);
    }

    @Override
    public void balanceDetail(final BalanceDetailComplete complete, int balanceHistoryId) {
        String url = HttpConstant.BALANCE_DETAIL;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());
        map.put("balanceHistoryId", balanceHistoryId+"");

        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<BalanceDetailBean>() {
            @Override
            public void onError(Exception e) {
                complete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(BalanceDetailBean balanceDetailBean) {
                if (balanceDetailBean.getCode() == 0) {
                    complete.onSuccess(balanceDetailBean);
                }
            }
        }, map);
    }

}
