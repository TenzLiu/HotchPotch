package com.tenz.hotchpotch.rx;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: TenzLiu
 * Date: 2018-01-24 17:32
 * Description: Rxjava线程调度
 */

public class RxScheduler {

    /**
     * 统一线程处理
     * 发布事件io线程，解绑io线程，接收事件主线程
     * compose处理线程
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> rxSchedulerTransform(){
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

}
