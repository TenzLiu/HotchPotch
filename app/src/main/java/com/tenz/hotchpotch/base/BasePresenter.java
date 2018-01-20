package com.tenz.hotchpotch.base;

import android.support.annotation.NonNull;

import com.tenz.hotchpotch.rx.RxManager;

/**
 * Author: TenzLiu
 * Date: 2018-01-18 23:06
 * Description: BasePresenter
 */

public abstract class BasePresenter<M, V> {

    public M mIModel;
    public V mIView;
    protected RxManager mRxManager = new RxManager();

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
        mRxManager.unSubsribe();
        mIModel = null;
        mIView = null;
    }

    /**
     * IView和IModel绑定完成立即执行
     * 实现类实现绑定完成后的逻辑,例如数据初始化等,界面初始化, 更新等
     */
    public abstract void onStart();


}
