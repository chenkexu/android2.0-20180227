package com.orientdata.lookforcustomers.presenter;

import com.orientdata.lookforcustomers.bean.ErrBean;
import com.orientdata.lookforcustomers.bean.SettingOut;
import com.orientdata.lookforcustomers.bean.TaskInfoBean;
import com.orientdata.lookforcustomers.bean.TradeSelfout;
import com.orientdata.lookforcustomers.bean.UploadPicBean;
import com.orientdata.lookforcustomers.event.DeleteTaskEvent;
import com.orientdata.lookforcustomers.event.TaskInfoEvent;
import com.orientdata.lookforcustomers.model.IImgModel;
import com.orientdata.lookforcustomers.model.ITaskModel;
import com.orientdata.lookforcustomers.model.imple.ImgModelImple;
import com.orientdata.lookforcustomers.model.imple.TaskModelImple;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.findcustomer.ITaskView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by wy on 2017/11/18.
 * 寻客
 */

public class TaskPresent<T> extends BasePresenter<ITaskView> {
    private ITaskView mPageTaskView;
    private ITaskModel mSelectSettingModel = new TaskModelImple();
    private IImgModel mImgModel = new ImgModelImple();

    public TaskPresent(ITaskView mPageTaskView) {
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

    public void uploadImg(int type,String picPath){
        if(mImgModel!=null){
            mPageTaskView.showLoading();
            mImgModel.uploadImg(new IImgModel.Complete() {

                @Override
                public void onSuccess(UploadPicBean uploadPicBean) {
                    mPageTaskView.hideLoading();
                    mPageTaskView.uploadPicSuc(uploadPicBean);
                }

                @Override
                public void onGetModelListSuc(List<TradeSelfout> modelList) {

                }

                @Override
                public void onError(int code, String message) {
                    mPageTaskView.hideLoading();
                    ToastUtils.showShort(message);
                }
            },type,picPath);
        }
    }


    public void getTaskDetail(int taskId){
        mPageTaskView.showLoading();
        if(mSelectSettingModel!=null){

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
