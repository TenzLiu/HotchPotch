package com.tenz.hotchpotch.module.video.model;

import com.tenz.hotchpotch.api.VideoApi;
import com.tenz.hotchpotch.http.RetrofitFactory;
import com.tenz.hotchpotch.module.video.contract.VideoContract;
import com.tenz.hotchpotch.module.video.entity.GetVideos;

import java.util.List;

import io.reactivex.Observable;

/**
 * Author: TenzLiu
 * Date: 2018-02-06 15:46
 * Description: VideoModel
 */

public class VideoModel implements VideoContract.IVideoModel {

    public static VideoModel newInstance(){
        return new VideoModel();
    }

    @Override
    public Observable<GetVideos> getVideos(int page) {
        return RetrofitFactory.getInstance().createApi(VideoApi.HOST,VideoApi.class)
                .getVideos(page);
    }

}
