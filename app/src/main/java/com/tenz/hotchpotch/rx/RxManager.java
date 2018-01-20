package com.tenz.hotchpotch.rx;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Author: TenzLiu
 * Date: 2018-01-18 22:57
 * Description: RxManager
 * 用于管理Rxjava 注册订阅和取消订阅
 */

public class RxManager {

    /**
     *  管理订阅者
     */
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    /**
     * 订阅
     * @param disposable
     */
    public void register(Disposable disposable){
        mCompositeDisposable.add(disposable);
    }

    /**
     * 取消订阅
     */
    public void unSubsribe(){
        mCompositeDisposable.dispose();
    }

}
