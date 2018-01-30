package com.tenz.hotchpotch.module.news.model;

import com.tenz.hotchpotch.api.NewsApi;
import com.tenz.hotchpotch.http.RetrofitFactory;
import com.tenz.hotchpotch.module.news.contract.NewsContract;
import com.tenz.hotchpotch.module.news.entity.GetNews;

import io.reactivex.Observable;

/**
 * Author: TenzLiu
 * Date: 2018-01-26 14:43
 * Description: NewsModel
 */

public class NewsModel implements NewsContract.INewsModel {

    public static NewsModel newInstance(){
        return new NewsModel();
    }

    @Override
    public Observable<GetNews> getNews(String category, int count, long min_behot_time) {
        return RetrofitFactory.getInstance().createApi(NewsApi.HOST,NewsApi.class)
                .getNews(category,count,min_behot_time);
    }

}
