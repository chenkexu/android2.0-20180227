package com.orientdata.lookforcustomers.model.imple;

import android.util.Log;

import com.orientdata.lookforcustomers.bean.ErrBean;
import com.orientdata.lookforcustomers.bean.ModelListBean;
import com.orientdata.lookforcustomers.bean.PicListBean;
import com.orientdata.lookforcustomers.bean.SelectSettingBean;
import com.orientdata.lookforcustomers.bean.TaskInfoBean;
import com.orientdata.lookforcustomers.bean.TradeSelfout;
import com.orientdata.lookforcustomers.bean.UploadPicBean;
import com.orientdata.lookforcustomers.model.IImgModel;
import com.orientdata.lookforcustomers.model.ITaskModel;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.util.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.OkHttpClientManager;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * Created by wy on 2017/11/18.
 * 图片
 */

public class ImgModelImple implements IImgModel {

    @Override
    public void uploadImg(final Complete complete, int type,String path) {
        String url = HttpConstant.UPLOAD_PIC;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("type",type+"");
        map.put("userId", UserDataManeger.getInstance().getUserId());
        File[] submitfiles = new File[1];
        submitfiles[0] = new File(path);


        try {
            OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<UploadPicBean>() {
                @Override
                public void onError(Exception e) {
                    complete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
                }

                @Override
                public void onResponse(UploadPicBean response) {
                    if (response.getCode() == 0) {
                        complete.onSuccess(response);
                    }
                }
            },submitfiles,"file",map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getModelList(final Complete complete) {
        String url = HttpConstant.SELECT_USER_MODEL_LIST;
        MDBasicRequestMap map = new MDBasicRequestMap();
        OkHttpClientManager.getAsyn(url, new OkHttpClientManager.ResultCallback<ModelListBean>() {
            @Override
            public void onError(Exception e) {
                complete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(ModelListBean modelList) {
                if (modelList.getCode() == 0) {
                    complete.onGetModelListSuc(modelList.getResult());
                }
            }
        },map);
    }

    @Override
    public void deletePicture(final PictureDeleteComplete complete,String userPicStoreIds) {
        String url = HttpConstant.DELETE_USER_PIC;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId",UserDataManeger.getInstance().getUserId());
        map.put("userPicStoreIds",userPicStoreIds);
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


    @Override
    public void getPictureList(final PictureComplete complete) {
        String url = HttpConstant.USER_PIC_LIST;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId",UserDataManeger.getInstance().getUserId());
        OkHttpClientManager.getAsyn(url, new OkHttpClientManager.ResultCallback<PicListBean>() {

            @Override
            public void onError(Exception e) {
                complete.onError(500, e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(PicListBean response) {
                if (response.getCode() == 0) {
                    complete.onSuccess(response);
                }
            }
        },map);
    }
}
