package com.orientdata.lookforcustomers.model;

import com.orientdata.lookforcustomers.bean.ErrBean;
import com.orientdata.lookforcustomers.bean.SettingOut;
import com.orientdata.lookforcustomers.bean.TaskInfoBean;

/**
 * Created by wy on 2017/11/18.
 * 创建寻客
 */

public interface ITaskModel {
    void createCustomer(Complete complete, String code);//获取设置
    void getTaskDetail(TaskInfoComplete taskInfoComplete, int taskId);//寻客详情
    void deletTask(DeleteTaskComplete complete, int taskId);//删除寻客

    interface Complete {
        void onSuccess(SettingOut selectSettingBeans);
        void onError(int code, String message);
    }
    interface TaskInfoComplete{
        void onSuccess(TaskInfoBean taskInfoBean);
        void onError(int code, String message);
    }
    interface DeleteTaskComplete{
        void onSuccess(ErrBean errBean);
        void onError(int code, String message);
    }
}
