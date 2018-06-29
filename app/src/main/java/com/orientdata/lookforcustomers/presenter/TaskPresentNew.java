package com.orientdata.lookforcustomers.presenter;

import com.orientdata.lookforcustomers.bean.ErrBean;
import com.orientdata.lookforcustomers.bean.MyInfoBean;
import com.orientdata.lookforcustomers.bean.SettingOut;
import com.orientdata.lookforcustomers.bean.TaskBasicInfo;
import com.orientdata.lookforcustomers.bean.TaskInfoBean;
import com.orientdata.lookforcustomers.bean.WrResponse;
import com.orientdata.lookforcustomers.event.DeleteTaskEvent;
import com.orientdata.lookforcustomers.event.TaskInfoEvent;
import com.orientdata.lookforcustomers.model.IImgModel;
import com.orientdata.lookforcustomers.model.ITaskModel;
import com.orientdata.lookforcustomers.model.imple.ImgModelImple;
import com.orientdata.lookforcustomers.model.imple.TaskModelImple;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;
import com.orientdata.lookforcustomers.network.api.ApiManager;
import com.orientdata.lookforcustomers.network.api.BaseObserver;
import com.orientdata.lookforcustomers.network.api.ParamsUtil;
import com.orientdata.lookforcustomers.network.util.RxUtil;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.findcustomer.impl.ITaskViewNew;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * Created by wy on 2017/11/18.
 * 寻客
 */

public class TaskPresentNew<T> extends BasePresenter<ITaskViewNew> {

    private ITaskViewNew mPageTaskView;
    private ITaskModel mSelectSettingModel = new TaskModelImple();
    private IImgModel mImgModel = new ImgModelImple();

    public TaskPresentNew(ITaskViewNew mPageTaskView) {
        this.mPageTaskView = mPageTaskView;
    }







    public void createCustomer(String code){
        if(mSelectSettingModel!=null){
            mPageTaskView.showLoading();
            mSelectSettingModel.createCustomer(new ITaskModel.Complete() {
                @Override
                public void onSuccess(SettingOut selectSettingBeans) {
                    mPageTaskView.hideLoading();
                    mPageTaskView.createCustomer(selectSettingBeans);
                }

                @Override
                public void onError(int code, String message) {
                    mPageTaskView.hideLoading();
                    ToastUtils.showShort(message);
                }
            },code);
        }
    }

    //获取签名信息
    public void getSignAndTd(String mProvinceCode){
        mPageTaskView.showLoading();
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());
        map.put("provincecode", mProvinceCode);

        OkHttpClientManager.postAsyn(HttpConstant.GET_SIGN_AND_TD, new OkHttpClientManager.ResultCallback<MyInfoBean>() {
            @Override
            public void onError(Exception e) {
                mPageTaskView.hideLoading();
                ToastUtils.showShort(e.getMessage());
                mPageTaskView.hideLoading();
            }
            @Override
            public void onResponse(MyInfoBean response) {
                mPageTaskView.hideLoading();
                if (response.getCode() == 0) {
                    if (response.getResult() == null || response.getResult().size() <= 0) {
                        mPageTaskView.hideLoading();
                        return;
                    }
                    mPageTaskView.getSignAndTd(response);
                }
            }
        }, map);
        }



    public void getCreateTaskBasicInfo(String mProvinceCode){
        mPageTaskView.showLoading();
        final HashMap<String, Object> map = ParamsUtil.getMap();
        map.put("provinceCode",mProvinceCode);

        ApiManager.getInstence().getApiService().getCreateTaskBasicInfo(ParamsUtil.getParams(map))
                .compose(RxUtil.<WrResponse<TaskBasicInfo>>rxSchedulerHelper())
                .subscribe(new BaseObserver<TaskBasicInfo>() {
                    @Override
                    protected void onSuccees(WrResponse<TaskBasicInfo> t) {
                            mPageTaskView.hideLoading();
                            mPageTaskView.getCreateTaskBasicInfo(t.getResult());
                    }

                    @Override
                    protected void onFailure(String errorInfo, boolean isNetWorkError) {
                        mPageTaskView.hideLoading();
                        ToastUtils.showShort(errorInfo);
                    }
                });
    }



    public void getTaskDetail(int taskId){
        if(mSelectSettingModel!=null){
            mPageTaskView.showLoading();
            mSelectSettingModel.getTaskDetail(new ITaskModel.TaskInfoComplete() {

                @Override
                public void onSuccess(TaskInfoBean taskInfoBean) {
                    mPageTaskView.hideLoading();
                    TaskInfoEvent taskInfoEvent = new TaskInfoEvent();
                    taskInfoEvent.taskInfoBean = taskInfoBean;
                    EventBus.getDefault().post(taskInfoEvent);
                }

                @Override
                public void onError(int code, String message) {
                    mPageTaskView.hideLoading();
                    ToastUtils.showShort(message);
                }
            },taskId);
        }
    }

    public void deletTask(int taskId){
        if(mImgModel!=null){
            mPageTaskView.showLoading();
            mSelectSettingModel.deletTask(new ITaskModel.DeleteTaskComplete(){

                @Override
                public void onSuccess(ErrBean errBean) {
                    mPageTaskView.hideLoading();
                    DeleteTaskEvent deleteTaskEvent = new DeleteTaskEvent();
                    deleteTaskEvent.errBean = errBean;
                    EventBus.getDefault().post(deleteTaskEvent);
                }

                @Override
                public void onError(int code, String message) {
                    mPageTaskView.hideLoading();
                    ToastUtils.showShort(message);
                }
            },taskId);
        }
    }


    @Override
    public void fecth() {

    }
}
