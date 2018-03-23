package vr.md.com.mdlibrary;

import android.app.Activity;
import android.content.SharedPreferences;

import vr.md.com.mdlibrary.utils.MySharedpreferences;

/**
 * Created by Mr.Z on 16/4/19.
 */
public class UserDataManeger {
    private static final String TOKEN = "usertoken";
    private static final String USERINFO = "userInfo";

    private static UserDataManeger manager;

    private UserDataManeger() {

    }

    public static UserDataManeger getInstance() {
        if (manager == null) {
            synchronized (UserDataManeger.class) {
                if (manager == null) {
                    manager = new UserDataManeger();
                }
            }
        }
        return manager;
    }

    /**
     * 保存用户token
     */
    public void saveUserToken(String token) {
        MySharedpreferences.putString("TOKEN", token);
    }

    /**
     * 保存用户token
     */
    public String getUserToken() {
        return MySharedpreferences.getString("TOKEN");
    }

    public void setUserId(String userId) {
        MySharedpreferences.putString("userId", userId);
    }

    public String getUserId() {
        return MySharedpreferences.getString("userId");
    }

    public void setAccount(String phone) {
        MySharedpreferences.putString("phone", phone);
    }

    public String getAccount() {
        return MySharedpreferences.getString("phone");
    }

    public void setPassword(String password) {
        MySharedpreferences.putString("password", password);
    }

    public String getPassword() {
        return MySharedpreferences.getString("password");
    }
    public void clearUserData(){
        MySharedpreferences.putString("TOKEN","");
        MySharedpreferences.putString("userId","");
        MySharedpreferences.putString("phone","");
        MySharedpreferences.putString("password","");
    }

}
