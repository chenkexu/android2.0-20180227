package com.orientdata.lookforcustomers.model;

import com.orientdata.lookforcustomers.bean.ErrBean;
import com.orientdata.lookforcustomers.bean.PicListBean;
import com.orientdata.lookforcustomers.bean.TradeSelfout;
import com.orientdata.lookforcustomers.bean.UploadPicBean;

import java.util.List;

/**
 * Created by wy on 2017/11/18.
 * 创建寻客
 */

public interface IImgModel {
    void uploadImg(Complete complete, int type,String picPath);//上传图片
    void getModelList(Complete complete);//获取模版列表
    void getPictureList(PictureComplete complete);//图片列表
    void deletePicture(PictureDeleteComplete complete,String deletePicture);//删除图片

    interface Complete {
        void onSuccess(UploadPicBean uploadPicBean);
        void onGetModelListSuc(List<TradeSelfout> modelList);
        void onError(int code, String message);
    }
    interface PictureComplete{
        void onSuccess(PicListBean picListBean);
        void onError(int code, String message);
    }

    interface PictureDeleteComplete{
        void onSuccess(ErrBean errBean);
        void onError(int code, String message);
    }
}
