package com.orientdata.lookforcustomers.presenter;

import com.orientdata.lookforcustomers.bean.CertificationOut;
import com.orientdata.lookforcustomers.bean.SearchListBean;
import com.orientdata.lookforcustomers.bean.TradeSelfout;
import com.orientdata.lookforcustomers.bean.UploadPicBean;
import com.orientdata.lookforcustomers.event.SearchListEvent;
import com.orientdata.lookforcustomers.event.UploadImgEvent;
import com.orientdata.lookforcustomers.model.ICertificateModel;
import com.orientdata.lookforcustomers.model.IImgModel;
import com.orientdata.lookforcustomers.model.imple.CertificateModelImple;
import com.orientdata.lookforcustomers.model.imple.ImgModelImple;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.home.IHomeView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by wy on 2017/10/25.
 * 首页
 */

public class HomePresent<T> extends BasePresenter<IHomeView> {
    private IHomeView mHomeView;
    private ICertificateModel mCertificateModel = new CertificateModelImple();
    private IImgModel mImgModel = new ImgModelImple();

    public HomePresent(IHomeView mHomeView) {
        this.mHomeView = mHomeView;
    }

    @Override
    public void fecth() {

    }

    /**
     * @param isCertificate 获取认证信息
     */
    public void getCertificateMsg(final boolean isCertificate) {
        if (mCertificateModel != null) {
            mHomeView.showLoading();
            mCertificateModel.getCertificateMsg(new ICertificateModel.GetMsgComplete() {
                @Override
                public void onSuccess(CertificationOut certificationOut) {
                    mHomeView.hideLoading();
                    mHomeView.getCertificateMsg(certificationOut, isCertificate);
                }

                @Override
                public void onError(int code, String message) {
                    mHomeView.hideLoading();
                    ToastUtils.showShort(message);
                }
            });
        }

    }



    public void uploadImg(int type, String picPath) {
        if (mImgModel != null) {
            mHomeView.showLoading();
            mImgModel.uploadImg(new IImgModel.Complete() {
                @Override
                public void onSuccess(UploadPicBean uploadPicBean) {
                    mHomeView.hideLoading();
                    UploadImgEvent uploadImgEvent = new UploadImgEvent();
                    uploadImgEvent.uploadPicBean = uploadPicBean;
                    EventBus.getDefault().post(uploadImgEvent);
                }

                @Override
                public void onGetModelListSuc(List<TradeSelfout> modelList) {

                }

                @Override
                public void onError(int code, String message) {
                    mHomeView.hideLoading();
                    ToastUtils.showShort(message);
                }
            }, type, picPath);
        }
    }



    public void getSearchList(int type, int status, int page, int size) {
        if (mCertificateModel != null) {
            mHomeView.showLoading();
            mCertificateModel.getSearchList(new ICertificateModel.GetSearchListComplete() {
                @Override
                public void onSuccess(SearchListBean searchListBean) {
                    mHomeView.hideLoading();
                    SearchListEvent searchListEvent = new SearchListEvent();
                    searchListEvent.searchListBean = searchListBean;
                    EventBus.getDefault().post(searchListEvent);
                }

                @Override
                public void onError(int code, String message) {
                    mHomeView.hideLoading();
                    ToastUtils.showShort(message);
                }
            }, type, status, page, size);
        }
    }







    public void getSearchList() {
        //-1代表不传 0全部
        getSearchList(0, 0, -1, -1);
    }
}
