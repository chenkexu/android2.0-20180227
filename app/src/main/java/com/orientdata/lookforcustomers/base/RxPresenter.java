package com.orientdata.lookforcustomers.base;


import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * 基于Rx的Presenter封装,控制订阅的生命周期
 */
public class RxPresenter<T extends BaseContract.BaseView> implements BaseContract.BasePresenter<T> {

   protected T mView;
    protected CompositeDisposable mCompositeDisposable;

    public RxPresenter(T  view) {
        attachView(view);
    }

    protected void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    protected void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }


    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
        unSubscribe();
    }

}
