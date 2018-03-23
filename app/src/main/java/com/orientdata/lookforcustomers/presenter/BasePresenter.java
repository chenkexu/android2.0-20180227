package com.orientdata.lookforcustomers.presenter;

import java.lang.ref.WeakReference;

/**
 * Created by wy on 2017/9/11.
 * 对应activity的UI抽象接口
 */

public abstract class BasePresenter<T> {
    /**
     * 持有UI接口的弱引用
     */
    protected WeakReference<T> mViewRef;

    /**
     * 拿取数据
     */
    public abstract void fecth();
    /**
     * 绑定View
     *
     * @param view
     */
    public void attachView(T view) {
        mViewRef = new WeakReference<T>(view);
    }

    /**
     * 解绑View，断开与Activity的联系
     */
    public void detach() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }


}
