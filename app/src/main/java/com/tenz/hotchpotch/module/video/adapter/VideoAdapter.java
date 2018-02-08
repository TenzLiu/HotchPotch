package com.tenz.hotchpotch.module.video.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.module.video.entity.GetVideos;
import com.tenz.hotchpotch.util.GlideUtil;

import java.util.List;

/**
 * Author: TenzLiu
 * Date: 2018-02-06 18:07
 * Description: VideoAdapter
 */

public class VideoAdapter extends BaseQuickAdapter<GetVideos.Video,BaseViewHolder> {

    public VideoAdapter(int layoutResId, @Nullable List<GetVideos.Video> data) {
        super(layoutResId, data);
    }

    public VideoAdapter(@Nullable List<GetVideos.Video> data) {
        super(data);
    }

    public VideoAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetVideos.Video item) {
        GlideUtil.loadImage(mContext,item.getCover(),
                (ImageView) helper.getView(R.id.siv_image));
        ((TextView)helper.getView(R.id.tv_name)).setText(item.getTitle());
    }

}
