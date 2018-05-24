package com.orientdata.lookforcustomers.network.util;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class RxUtil {
	public static <T> ObservableTransformer<T,T> rxSchedulerHelper(){
		return new ObservableTransformer<T,T>() {
			@Override
			public ObservableSource<T> apply(Observable<T>  upstream) {
				return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
			}
		};
	}

	public static <T> ObservableTransformer<T, T> compose() {
		return new ObservableTransformer<T, T>() {
			@Override
			public ObservableSource<T> apply(Observable<T> observable) {
				return observable
						.subscribeOn(Schedulers.io())
						.doOnSubscribe(new Consumer<Disposable>() {
							@Override
							public void accept(Disposable disposable) throws Exception {

							}
						})
						.observeOn(AndroidSchedulers.mainThread());
			}
		};
	}


}
