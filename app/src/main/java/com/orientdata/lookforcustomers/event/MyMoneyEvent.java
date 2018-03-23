package com.orientdata.lookforcustomers.event;


/**
 * 个人信息 是否需要更新 （充值 提现 佣金转入余额）
 */
public class MyMoneyEvent {
    public boolean isRefresh;
    public double commission;//佣金
    public double balance;//余额
    public double frozenAmount;//余额
    public String subCount;//每日可提现次数
}
