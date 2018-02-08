package com.tenz.hotchpotch.module.video.contract;

import com.tenz.hotchpotch.base.BasePresenter;
import com.tenz.hotchpotch.base.IBaseModel;
import com.tenz.hotchpotch.base.IBaseView;
import com.tenz.hotchpotch.module.video.entity.GetVideos;

import java.util.List;

import io.reactivex.Observable;

/**
 * Author: TenzLiu
 * Date: 2018-02-06 15:09
 * Description: VideoContract
 */

public interface VideoContract {

    public abstract class VideoPresenter extends BasePresenter<IVideoModel,IVideoView>{

        public abstract void getVideos(boolean isRefresh);

    }

    public interface IVideoModel extends IBaseModel{

        Observable<GetVideos> getVideos(int page);

    }

    public interface IVideoView extends IBaseView{

        void showVideos(List<GetVideos.Video> videoList);

    }

}
