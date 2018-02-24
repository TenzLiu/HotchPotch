package com.tenz.hotchpotch.module.photo.presenter;

import com.tenz.hotchpotch.module.photo.contract.PhotoContract;
import com.tenz.hotchpotch.module.photo.entity.GetPhotos;
import com.tenz.hotchpotch.module.photo.model.PhotoModel;
import com.tenz.hotchpotch.rx.RxScheduler;
import com.tenz.hotchpotch.util.ToastUtil;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Author: TenzLiu
 * Date: 2018-01-29 16:45
 * Description: PhotoPresenter
 */

public class PhotoPresenter extends PhotoContract.PhotoPresenter {

    private int page;

    public static PhotoPresenter newInstance(){
        return new PhotoPresenter();
    }

    @Override
    public void getPhotos(boolean isRefresh) {
        if(mIModel != null && mIView != null){
            if(isRefresh)
                page = 0;
            mIModel.getPhotos(page)
                    .compose(RxScheduler.<GetPhotos>rxSchedulerTransform())
                    .compose(mIView.<GetPhotos>bindToLife())
                    .subscribe(new Observer<GetPhotos>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mIView.showLoadingDialog();
                        }

                        @Override
                        public void onNext(GetPhotos getPhotos) {
                            page ++;
                            mIView.showPhotos(isRefresh, getPhotos.getResults().size()<20, getPhotos.getResults());
                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtil.showToast(""+e.toString());
                            mIView.dismissLoadingDialog();
                        }

                        @Override
                        public void onComplete() {
                            mIView.dismissLoadingDialog();
                        }
                    });
        }
    }

    @Override
    public void onStart() {

    }

    @Override
    public PhotoContract.IPhotoModel getModel() {
        return PhotoModel.newInstance();
    }

}
