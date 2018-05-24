package com.orientdata.lookforcustomers.base;



public  interface BaseContract {
	
	

    interface BasePresenter<T> {
        void attachView(T view);
        void detachView();
    }

    
    
    interface BaseView {
         void showToast(String str);
        /**
         * 显示加载图
         */
         void showLoading();
        /**
         * 隐藏加载图
         */
         void hideLoading();
    }


    
    
  
    
    
}
