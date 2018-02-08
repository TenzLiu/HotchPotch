package com.tenz.hotchpotch.api;


import com.tenz.hotchpotch.module.video.entity.GetVideos;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

import static com.tenz.hotchpotch.http.RetrofitFactory.AVOID_HTTP403_FORBIDDEN;

/**
 * Author: TenzLiu
 * Date: 2018-01-29 16:18
 * Description: VideoApi
 */

public interface VideoApi {

    public static final String HOST = "http://c.3g.163.com";

    @Headers(AVOID_HTTP403_FORBIDDEN)
    @GET("/nc/video/list/V9LG4B3A0/n/{page}-10.html")
    Observable<GetVideos> getVideos(@Path("page") int page);

}
