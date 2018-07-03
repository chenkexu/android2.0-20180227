package com.orientdata.lookforcustomers.presenter;

import com.orientdata.lookforcustomers.bean.CertificationOut;
import com.orientdata.lookforcustomers.bean.MyInfoBean;
import com.orientdata.lookforcustomers.bean.SearchListBean;
import com.orientdata.lookforcustomers.bean.TaskCountBean;
import com.orientdata.lookforcustomers.bean.TradeSelfout;
import com.orientdata.lookforcustomers.bean.UploadPicBean;
import com.orientdata.lookforcustomers.bean.UserInfoBean;
import com.orientdata.lookforcustomers.bean.WrResponse;
import com.orientdata.lookforcustomers.event.MyMoneyEvent;
import com.orientdata.lookforcustomers.event.SearchListEvent;
import com.orientdata.lookforcustomers.event.UploadImgEvent;
import com.orientdata.lookforcustomers.model.ICertificateModel;
import com.orientdata.lookforcustomers.model.IImgModel;
import com.orientdata.lookforcustomers.model.imple.CertificateModelImple;
import com.orientdata.lookforcustomers.model.imple.ImgModelImple;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;
import com.orientdata.lookforcustomers.network.api.ApiManager;
import com.orientdata.lookforcustomers.network.api.BaseObserver;
import com.orientdata.lookforcustomers.network.api.ParamsUtil;
import com.orientdata.lookforcustomers.network.util.RxUtil;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.home.imple.IMeView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * Created by wy on 2017/10/25.
 * 首页
 */

public class MePresent<T> extends BasePresenter<IMeView> {

    private IMeView mHomeView;
    private ICertificateModel mCertificateModel = new CertificateModelImple();
    private IImgModel mImgModel = new ImgModelImple();

    public MePresent(IMeView mHomeView) {
        this.mHomeView = mHomeView;
    }


    //显示小红点的接口
    public void showRedPoint(){
        HashMap<String, Object> map = ParamsUtil.getMap();
        ApiManager.getInstence().getApiService().getUnReadMsgAndUnReadAnnouncement(ParamsUtil.getParams(map))
                .compose(RxUtil.<WrResponse<TaskCountBean>>rxSchedulerHelper())
                .subscribe(new BaseObserver<TaskCountBean>() {
                    @Override
                    protected void onSuccees(WrResponse<TaskCountBean> t) {
                        if (t.getResult()!=null){
                            mHomeView.showRedPoint(t.getResult());
                        }
                    }

                    @Override
                    protected void onFailure(String errorInfo, boolean isNetWorkError) {
                        ToastUtils.showShort(errorInfo);
                    }
                });
    }


    public void getTaskCount(){
        HashMap<String, Object> map = ParamsUtil.getMap();
        ApiManager.getInstence().getApiService().getTaskCount(ParamsUtil.getParams(map))
                .compose(RxUtil.<WrResponse<TaskCountBean>>rxSchedulerHelper())
                .subscribe(new BaseObserver<TaskCountBean>() {
                    @Override
                    protected void onSuccees(WrResponse<TaskCountBean> t) {
                        mHomeView.getTaskCount(t.getResult());
                    }

                    @Override
                    protected void onFailure(String errorInfo, boolean isNetWorkError) {
                    }
                });
    }

    /**
     * 获取账号 佣金 和 余额
     */
    public void getCommission(){
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());
        OkHttpClientManager.postAsyn(HttpConstant.SELECT_SHOW_MY_INFO, new OkHttpClientManager.ResultCallback<MyInfoBean>() {

            private String moreMoney;
            private String phone = "";

            @Override
            public void onError(Exception e) {
                ToastUtils.showShort(e.getMessage());
            }

            @Override
            public void onResponse(MyInfoBean response) {
                if (response.getCode() == 0) {
                    if (response.getResult() == null || response.getResult().size() <= 0) {
                        return;
                    }
                    //TODO
                    Map<String,Object> mMapInfoBeans = response.getResult();

                    double commission = 0;
                    double balance = 0;
                    double frozenAmount = 0;
                    String subCount = "";
                    String name = "";

                           /* commission
                    是 bigdecimal 佣金*/
                    if (mMapInfoBeans.containsKey("commission")) {
                        commission = (Double) mMapInfoBeans.get("commission");
                    }

                    /*        balance
                    是 bigdecimal 余额*/
                    if (mMapInfoBeans.containsKey("balance")) {
                        balance = (Double) mMapInfoBeans.get("balance");
                    }
                    /*        subCount
                    是 String 每日可提现次数*/
                    if (mMapInfoBeans.containsKey("subCount")) {
                        subCount = (String) mMapInfoBeans.get("subCount");
                    }

                    /*        frozenAmount
                    是 bigdecimal 冻结金额*/
                    if (mMapInfoBeans.containsKey("frozenAmount")) {
                        frozenAmount = (Double) mMapInfoBeans.get("frozenAmount");
                    }

                    //是 String 名称
                    if (mMapInfoBeans.containsKey("name")) {
                        name = (String) mMapInfoBeans.get("name");
                    }

                    /*        userHead
                    是 String 头像路径*/
                    String userHead = "";
                    if (mMapInfoBeans.containsKey("userHead")) {
                        userHead = (String) mMapInfoBeans.get("userHead");
                    }
                    /*        phone
                    是 String 手机号*/
                    if (mMapInfoBeans.containsKey("phone")) {
                        phone = (String) mMapInfoBeans.get("phone");
                    }
                           /* commission
                    是 bigdecimal 佣金*/
                    if (mMapInfoBeans.containsKey("commission")) {
                        commission = (Double) mMapInfoBeans.get("commission");
                    }

                    /*        moreMoney
                    是 String 最低提现金额*/
                    if (mMapInfoBeans.containsKey("moreMoney")) {
                        moreMoney = (String) mMapInfoBeans.get("moreMoney");
                    }
                    /*        subCount
                    是 String 每日可提现次数*/
                    if (mMapInfoBeans.containsKey("subCount")) {
                        subCount = (String) mMapInfoBeans.get("subCount");
                    }

                    /* balance
                    是 bigdecimal 余额*/
                    if (mMapInfoBeans.containsKey("balance")) {
                        balance = (Double) mMapInfoBeans.get("balance");
                    }

                    /* frozenAmount
                    是 bigdecimal 冻结金额*/
                    if (mMapInfoBeans.containsKey("frozenAmount")) {
                        frozenAmount = (Double) mMapInfoBeans.get("frozenAmount");
                    }

                    if (mMapInfoBeans.containsKey("upMoney")) {
                        String upMoney = (String) mMapInfoBeans.get("upMoney");
                    }

                    MyMoneyEvent moneyEvent = new MyMoneyEvent();
                    moneyEvent.commission = commission;
                    moneyEvent.balance = balance;
                    moneyEvent.subCount = subCount;
                    moneyEvent.frozenAmount = frozenAmount;
                    moneyEvent.userHead = userHead;
                    moneyEvent.phone = phone;
                    moneyEvent.moreMoney = moreMoney;
                    moneyEvent.name = name;

                    mHomeView.getMyMoney(moneyEvent);
                }
            }
        }, map);
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





    public void getUserData() {
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());
        OkHttpClientManager.postAsyn(HttpConstant.SELECT_USER_INFO, new OkHttpClientManager.ResultCallback<UserInfoBean>() {
            @Override
            public void onError(Exception e) {
                ToastUtils.showShort(e.getMessage());
            }

            @Override
            public void onResponse(UserInfoBean response) {
                if (response.getCode() == 0) {
                    if (response.getResult() == null) {
                        return;
                    }
                }
                mHomeView.getUserData(response);
            }
        }, map);
    }








    public void getSearchList() {
        //-1代表不传 0全部
        getSearchList(0, 0, -1, -1);
    }
}
