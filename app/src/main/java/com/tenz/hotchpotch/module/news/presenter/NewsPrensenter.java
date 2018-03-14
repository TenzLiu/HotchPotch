package com.tenz.hotchpotch.module.news.presenter;

import com.tenz.hotchpotch.module.news.contract.NewsContract;
import com.tenz.hotchpotch.module.news.entity.GetNews;
import com.tenz.hotchpotch.module.news.model.NewsModel;
import com.tenz.hotchpotch.rx.RxScheduler;
import com.tenz.hotchpotch.util.JsonUtil;
import com.tenz.hotchpotch.util.ToastUtil;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Author: TenzLiu
 * Date: 2018-01-26 14:44
 * Description: NewsPrensenter
 */

public class NewsPrensenter extends NewsContract.NewsPresenter {

    private String category = "news_tech";
    private int count = 20;
    private long min_behot_time;

    public static NewsPrensenter newInstance(){
        return new NewsPrensenter();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void getNrews(boolean isRefresh) {
        if(mIModel != null && mIView != null){
            if(isRefresh)
                min_behot_time = System.currentTimeMillis();
                mIModel.getNews(category,count,min_behot_time)
                    .compose(RxScheduler.<GetNews>rxSchedulerTransform())
                    .compose(mIView.<GetNews>bindToLife())
                    .subscribe(new Observer<GetNews>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mIView.showLoadingDialog();
                        }

                        @Override
                        public void onNext(GetNews getNews) {
                            min_behot_time = getNews.getData().size()>0?
                                    JsonUtil.fromJsonToObject(getNews.getData().get(0).getContent(), GetNews.News.class).getBehot_time()
                                    :System.currentTimeMillis();
                            mIView.showNews(isRefresh, getNews.getData().size()<count, getNews);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mIView.dismissLoadingDialog();
                            ToastUtil.showToast(""+e.toString());
                        }

                        @Override
                        public void onComplete() {
                            mIView.dismissLoadingDialog();
                        }
                    });
        }
    }

    @Override
    public NewsContract.INewsModel getModel() {
        return NewsModel.newInstance();
    }

}
