package com.tenz.hotchpotch.module.news.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.module.news.entity.GetNews;
import com.tenz.hotchpotch.util.DateUtil;
import com.tenz.hotchpotch.util.GlideUtil;
import com.tenz.hotchpotch.util.JsonUtil;

import java.util.List;

/**
 * Author: TenzLiu
 * Date: 2018-01-26 17:11
 * Description: NewsAdapter
 */

public class NewsAdapter extends BaseQuickAdapter<GetNews.DataBean,BaseViewHolder> {

    public NewsAdapter(int layoutResId, @Nullable List data) {
        super(layoutResId, data);
    }

    public NewsAdapter(@Nullable List data) {
        super(data);
    }

    public NewsAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetNews.DataBean dataBean) {
        GetNews.News news = JsonUtil.fromJsonToObject(dataBean.getContent(), GetNews.News.class);
        helper.setText(R.id.tv_title,news.getTitle());
        helper.setText(R.id.tv_time, DateUtil.getDay(news.getPublish_time()));
        helper.setText(R.id.tv_author,news.getUser_info()!=null?news.getUser_info().getName():"");
        GlideUtil.loadImage(mContext,news.getUser_info()!=null?news.getUser_info().getAvatar_url():"",
                (ImageView) helper.getView(R.id.iv_image));
    }

}
