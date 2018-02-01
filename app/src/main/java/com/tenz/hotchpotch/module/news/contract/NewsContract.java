package com.tenz.hotchpotch.module.news.contract;

import com.tenz.hotchpotch.base.BasePresenter;
import com.tenz.hotchpotch.base.IBaseModel;
import com.tenz.hotchpotch.base.IBaseView;
import com.tenz.hotchpotch.module.news.entity.GetNews;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.Observable;


/**
 * Author: TenzLiu
 * Date: 2018-01-26 12:47
 * Description: NewsContract
 */

public interface NewsContract {

    abstract class NewsPresenter extends BasePresenter<INewsModel,INewsView>{

        public abstract void getNrews(boolean isRefresh);

    }

    interface INewsModel extends IBaseModel {

        Observable<GetNews> getNews(String category, int count, long min_behot_time);

    }

    interface INewsView extends IBaseView{

        void showNews(GetNews getNews);

    }

}
