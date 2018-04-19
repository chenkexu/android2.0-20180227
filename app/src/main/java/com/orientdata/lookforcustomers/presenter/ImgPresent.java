package com.orientdata.lookforcustomers.presenter;

import com.orientdata.lookforcustomers.bean.ErrBean;
import com.orientdata.lookforcustomers.bean.PicListBean;
import com.orientdata.lookforcustomers.bean.TradeSelfout;
import com.orientdata.lookforcustomers.bean.UploadPicBean;
import com.orientdata.lookforcustomers.event.ImgClipResultEvent;
import com.orientdata.lookforcustomers.event.PicDeleteResultEvent;
import com.orientdata.lookforcustomers.event.PicListResultEvent;
import com.orientdata.lookforcustomers.model.IImgModel;
import com.orientdata.lookforcustomers.model.imple.ImgModelImple;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.findcustomer.IImgView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by wy on 2017/11/18.
 * 图片库
 */

public class ImgPresent<T> extends BasePresenter<IImgView> {
    private IImgView mImgView;
    private IImgModel mImgModel = new ImgModelImple();

    public ImgPresent(IImgView mImgView) {
        this.mImgView = mImgView;
    }

    public void uploadImg(int type, String picPath){
        uploadImg(type,picPath,true);
    }
    public void uploadImg(int type, String picPath, final boolean isShow){
        if(mImgModel!=null){
            if(isShow){
                mImgView.showLoading();
            }
            mImgModel.uploadImg(new IImgModel.Complete() {
                @Override
                public void onSuccess(UploadPicBean uploadPicBean) {
                    mImgView.hideLoading();
                    mImgView.uploadPicSuc(uploadPicBean);
                }

                @Override
                public void onGetModelListSuc(List<TradeSelfout> modelList) {
                    if(isShow){
                        mImgView.hideLoading();
                    }
                }

                @Override
                public void onError(int code, String message) {
                    if(isShow){
                        mImgView.hideLoading();
                    }
                    ToastUtils.showShort(message);
                }
            },type,picPath);
        }
    }

    /**
     * 模版制作列表
     */
    public void getModelList(){
        if(mImgModel!=null){
            mImgView.showLoading();
            mImgModel.getModelList(new IImgModel.Complete() {
                @Override
                public void onSuccess(UploadPicBean uploadPicBean) {
                    mImgView.hideLoading();
                }

                @Override
                public void onGetModelListSuc(List<TradeSelfout> modelList) {
                    mImgView.hideLoading();
//                    mImgView.modelList(modelList);
                    ImgClipResultEvent imgClipResultEvent = new ImgClipResultEvent();
                    imgClipResultEvent.modelList =modelList;
                    EventBus.getDefault().post(imgClipResultEvent);
                }

                @Override
                public void onError(int code, String message) {
                    mImgView.hideLoading();
                    ToastUtils.showShort(message);
                }
            });
        }
    }
    /**
     * 图库列表
     */
    public void getPictureList(){
        if(mImgModel!=null){
            mImgView.showLoading();
            mImgModel.getPictureList(new IImgModel.PictureComplete() {

                @Override
                public void onSuccess(PicListBean picListBean) {
                    mImgView.hideLoading();
                    PicListResultEvent picListResultEvent = new PicListResultEvent();
                    picListResultEvent.picList = picListBean.getResult();
                    EventBus.getDefault().post(picListResultEvent);
                }

                @Override
                public void onError(int code, String message) {
                    mImgView.hideLoading();
                    ToastUtils.showShort(message);
                }
            });
        }
    }
    /**
     * 图库列表
     */
    public void deletePicture(String deletePicture){
        if(mImgModel!=null){
            mImgView.showLoading();
            mImgModel.deletePicture(new IImgModel.PictureDeleteComplete() {

                @Override
                public void onSuccess(ErrBean errBean) {
                    mImgView.hideLoading();
                    PicDeleteResultEvent picDeleteResultEvent = new PicDeleteResultEvent();
                    picDeleteResultEvent.errBean = errBean;
                    EventBus.getDefault().post(picDeleteResultEvent);
                }

                @Override
                public void onError(int code, String message) {
                    mImgView.hideLoading();
                    ToastUtils.showShort(message);
                }
            },deletePicture);
        }
    }


    @Override
    public void fecth() {

    }
}
