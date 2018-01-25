package com.tenz.hotchpotch.base;

import android.support.annotation.NonNull;

import com.tenz.hotchpotch.rx.RxManager;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Author: TenzLiu
 * Date: 2018-01-18 23:06
 * Description: BasePresenter
 */

public abstract class BasePresenter<M, V> {

    protected M mIModel;
    protected V mIView;
    protected RxManager mRxManager = new RxManager();
    private LifecycleProvider<ActivityEvent> provider;

    /**
     * 构造方法
     * @param provider RxLifecycle管理生命周期
     */
    public BasePresenter(LifecycleProvider<ActivityEvent> provider) {
        this.provider = provider;
    }

    /**
     * 获取LifecycleProvider
     * @return
     */
    public LifecycleProvider<ActivityEvent> getLifecycleProvider() {
        return provider;
    }

    /**
     * IView和IModel绑定完成立即执行
     * 实现类实现绑定完成后的逻辑,例如数据初始化等,界面初始化, 更新等
     */
    public abstract void onStart();

    /**
     * 绑定IModel和IView的引用
     * @param mIModel
     * @param mIView
     */
    public void attachMV(@NonNull M mIModel, @NonNull V mIView){
        this.mIModel = mIModel;
        this.mIView = mIView;
        this.onStart();
    }

    /**
     * 返回presenter想持有的Model引用
     * @return
     */
    public abstract M getModel();

    /**
     * 解绑IModel和IView
     */
    public void detachMV(){
        mIModel = null;
        mIView = null;
        mRxManager = null;
    }

}
