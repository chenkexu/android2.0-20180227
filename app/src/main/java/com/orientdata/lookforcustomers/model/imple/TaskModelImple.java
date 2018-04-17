package com.orientdata.lookforcustomers.model.imple;

import com.orientdata.lookforcustomers.bean.ErrBean;
import com.orientdata.lookforcustomers.bean.SelectSettingBean;
import com.orientdata.lookforcustomers.bean.TaskInfoBean;
import com.orientdata.lookforcustomers.model.ITaskModel;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * Created by wy on 2017/11/18.
 * 寻客
 */

public class TaskModelImple implements ITaskModel {

    @Override
    public void createCustomer(final Complete complete, String code) {
        String url = HttpConstant.SELECT_SETTING2;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("code",code);
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<SelectSettingBean>() {
            @Override
            public void onError(Exception e) {
                complete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(SelectSettingBean response) {
                if (response.getCode() == 0) {
                    complete.onSuccess(response.getResult());
                }
            }
        }, map);
    }
    @Override
    public void getTaskDetail(final TaskInfoComplete complete, int taskId) {
        String url = HttpConstant.USER_TASK_INFO2;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());
        map.put("taskId",taskId+"");
        OkHttpClientManager.getAsyn(url, new OkHttpClientManager.ResultCallback<TaskInfoBean>() {

            @Override
            public void onError(Exception e) {
                complete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(TaskInfoBean response) {
                if (response.getCode() == 0) {
                    complete.onSuccess(response);
                }
            }
        },map);
    }

    @Override
    public void deletTask(final DeleteTaskComplete complete, int taskId) {
        String url = HttpConstant.DELETE_TASK;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());
        map.put("taskId",taskId+"");
        OkHttpClientManager.getAsyn(url, new OkHttpClientManager.ResultCallback<ErrBean>() {

            @Override
            public void onError(Exception e) {
                complete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(ErrBean response) {
                if (response.getCode() == 0) {
                    complete.onSuccess(response);
                }
            }
        },map);
    }
}
