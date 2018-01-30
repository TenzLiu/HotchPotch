package com.tenz.hotchpotch.api;


import com.tenz.hotchpotch.module.photo.entity.GetPhotos;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Author: TenzLiu
 * Date: 2018-01-29 16:18
 * Description: PhotoApi
 */

public interface PhotoApi {

    public static final String HOST = "http://gank.io";

    @GET("/api/data/福利/20/{page}")
    Observable<GetPhotos> getPhotos(@Path("page") int page);

}
