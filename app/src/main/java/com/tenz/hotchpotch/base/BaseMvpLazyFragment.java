package com.tenz.hotchpotch.base;

import android.support.annotation.NonNull;

/**
 * Author: TenzLiu
 * Date: 2018-01-18 23:21
 * Description: BaseMvpFragment,并处理Fragment懒加载
 */

public abstract class BaseMvpLazyFragment<P extends BasePresenter, M extends IBaseModel>
        extends BaseLazyFragment {

    /**
     * presenter 具体的presenter由子类确定
     */
    protected P mPresenter;
    /**
     * model 具体的model由子类确定
     */
    protected M mModel;

    @Override
    protected void initData() {
        super.initData();
        mPresenter = initPresenter();
        if(mPresenter != null){
            mModel = (M) mPresenter.getModel();
            if(mModel != null){
                mPresenter.attachMV(mModel,this);
            }
        }
    }

    /**
     * 初始化presenter
     * @return
     */
    @NonNull
    protected abstract P initPresenter();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPresenter != null){
            mPresenter.detachMV();
        }
    }

}
