package com.tenz.hotchpotch.module.video.presenter;

import com.tenz.hotchpotch.module.video.contract.VideoContract;
import com.tenz.hotchpotch.module.video.entity.GetVideos;
import com.tenz.hotchpotch.module.video.model.VideoModel;
import com.tenz.hotchpotch.rx.RxScheduler;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Author: TenzLiu
 * Date: 2018-02-06 15:51
 * Description: VideoPresenter
 */

public class VideoPresenter extends VideoContract.VideoPresenter {

    private int page = 1;

    public static VideoPresenter newInstance(){
        return new VideoPresenter();
    }

    @Override
    public void getVideos(boolean isRefresh) {
        if(mIModel != null && mIView != null){
            if(isRefresh)
                page = 1;
            mIModel.getVideos(page)
                    .compose(mIView.<GetVideos>bindToLife())
                    .compose(RxScheduler.<GetVideos>rxSchedulerTransform())
                    .subscribe(new Observer<GetVideos>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mIView.showLoadingDialog();
                        }

                        @Override
                        public void onNext(GetVideos value) {
                            mIView.showVideos(isRefresh,value.getV9LG4B3A0().size()<15, value.getV9LG4B3A0());
                            page ++;
                        }

                        @Override
                        public void onError(Throwable e) {
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
    public VideoContract.IVideoModel getModel() {
        return VideoModel.newInstance();
    }

}
