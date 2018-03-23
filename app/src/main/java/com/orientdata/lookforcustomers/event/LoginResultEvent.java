package com.orientdata.lookforcustomers.event;

/**
 * Created by wy on 2017/10/27.
 * 登录
 */

public class LoginResultEvent {
    //是否登录成功
    public boolean isLogin;
    //是否是新用户
    public boolean isNewUser;

    public int userId;
}
