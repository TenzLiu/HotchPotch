package com.tenz.hotchpotch.module.news.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.base.BaseMvpFragment;
import com.tenz.hotchpotch.module.news.activity.NewsDetailActivity;
import com.tenz.hotchpotch.module.news.adapter.NewsAdapter;
import com.tenz.hotchpotch.module.news.contract.NewsContract;
import com.tenz.hotchpotch.module.news.entity.GetNews;
import com.tenz.hotchpotch.module.news.model.NewsModel;
import com.tenz.hotchpotch.module.news.presenter.NewsPrensenter;
import com.tenz.hotchpotch.util.JsonUtil;
import com.tenz.hotchpotch.util.LogUtil;
import com.tenz.hotchpotch.util.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Author: TenzLiu
 * Date: 2018-01-20 16:51
 * Description: NewsFragment
 */

public class NewsFragment extends BaseMvpFragment<NewsPrensenter,NewsModel>
        implements NewsContract.INewsView, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.abl_appbar)
    AppBarLayout abl_appbar;
    @BindView(R.id.srl_container)
    SwipeRefreshLayout srl_container;
    @BindView(R.id.rv_news)
    RecyclerView rv_news;
    @BindView(R.id.fab_to_top)
    FloatingActionButton fab_to_top;

    private NewsAdapter newsAdapter;

    public static NewsFragment getInstancec(){
        NewsFragment newsFragment = new NewsFragment();
        Bundle bundle = new Bundle();
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        srl_container.setColorSchemeColors(ResourceUtil.getColor(R.color.colorApp));
        rv_news.setLayoutManager(new LinearLayoutManager(mContext));
        srl_container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl_container.setRefreshing(true);
                mPresenter.getNrews(true);
            }
        });
        abl_appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(verticalOffset == 0){
                    fab_to_top.hide();
                }else{
                    fab_to_top.show();
                }
            }
        });
        fab_to_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rv_news.smoothScrollToPosition(0);
            }
        });
        initAdapter(new ArrayList<GetNews.DataBean>());
    }

    @Override
    protected void initData() {
        super.initData();
        srl_container.setRefreshing(true);
        mPresenter.getNrews(true);
    }

    @NonNull
    @Override
    protected NewsPrensenter initPresenter() {
        return NewsPrensenter.newInstance();
    }

    @Override
    public void showNews(boolean isRefresh, boolean isNoMoreData, GetNews getNews) {
        srl_container.setRefreshing(false);
        if(isRefresh){
            if(getNews.getData().size()==0){
                newsAdapter.setEmptyView(R.layout.layout_view_empty);
            }else{
                newsAdapter.setNewData(getNews.getData());
            }
        }else{
            newsAdapter.addData(getNews.getData());
        }
        if(isNoMoreData){
            newsAdapter.loadMoreEnd();
        }else{
            newsAdapter.loadMoreComplete();
        }
    }

    /**
     * 初始化adapter
     * @param newsList
     */
    private void initAdapter(List<GetNews.DataBean> newsList){
        newsAdapter = new NewsAdapter(R.layout.item_news,newsList);
        newsAdapter.setOnLoadMoreListener(this,rv_news);
        newsAdapter.openLoadAnimation();
        rv_news.setAdapter(newsAdapter);
        newsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GetNews.News news = JsonUtil.fromJsonToObject(newsAdapter.getItem(position).getContent(), GetNews.News.class);
                Bundle bundle = new Bundle();
                bundle.putString("url",news.getUrl());
                bundle.putString("htmlData","");
                bundle.putString("title",news.getUser_info()!=null?news.getUser_info().getName():"");
                bundle.putString("content",news.getTitle());
                bundle.putString("imageUrl",news.getUser_info()!=null?news.getUser_info().getAvatar_url():"");
                startActivity(NewsDetailActivity.class,bundle);
            }
        });
        newsAdapter.setEmptyView(R.layout.layout_view_empty);
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.getNrews(false);
    }

}
