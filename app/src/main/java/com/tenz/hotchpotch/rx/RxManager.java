package com.tenz.hotchpotch.rx;

import com.tenz.hotchpotch.util.LogUtil;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Author: TenzLiu
 * Date: 2018-01-24 18:11
 * Description: RxManager 用于管理Rxjava 注册订阅和取消订阅,以及RxBus的管理
 */

public class RxManager {

    /**
     * 将所有正在处理的Subscription都添加到CompositeSubscription中。统一退出的时候注销观察
     * 目前已用RxLifecycle代替
     */
    private CompositeDisposable mCompositeDisposable;

    /**
     * 将Disposable注册到CompositeDisposable中统一管理,避免内存泄漏
     *
     * @param disposable
     */
    public void subscribe(Disposable disposable) {
        //csb 如果解绑了的话添加 sb 需要新的实例否则绑定时无效的
        if (mCompositeDisposable == null || mCompositeDisposable.isDisposed()) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);//将所有 Disposable 放入集中处理
    }

    /**
     * 解绑正在执行的RxJava任务,避免内存泄漏
     */
    public void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();//保证 Activity 结束时取消所有正在执行的订阅
        }
    }

}
