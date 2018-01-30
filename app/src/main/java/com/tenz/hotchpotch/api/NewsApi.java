package com.tenz.hotchpotch.api;


import com.tenz.hotchpotch.module.news.entity.GetNews;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Author: TenzLiu
 * Date: 2018-01-25 17:34
 * Description: NewsApi
 */

public interface NewsApi {

    String HOST = "http://is.snssdk.com/api/";

    /**
     * 今日头条
     * @param category 标签
     * @param count 返回数量
     * @param min_behot_time 上次请求时间的时间戳
     * @return
     */
    @GET("news/feed/v51/")
    Observable<GetNews> getNews(@Query("category") String category, @Query("count") int count,
                                @Query("min_behot_time") Long min_behot_time);


}
