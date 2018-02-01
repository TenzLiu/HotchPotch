package com.tenz.hotchpotch.module.photo.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.module.photo.entity.GetPhotos;
import com.tenz.hotchpotch.util.GlideUtil;
import com.tenz.hotchpotch.util.ResourceUtil;

import java.util.List;

/**
 * Author: TenzLiu
 * Date: 2018-01-29 17:01
 * Description: PhotoAdapter
 */

public class PhotoAdapter extends BaseQuickAdapter<GetPhotos.Photo,BaseViewHolder> {

    public PhotoAdapter(int layoutResId, @Nullable List<GetPhotos.Photo> data) {
        super(layoutResId, data);
    }

    public PhotoAdapter(@Nullable List<GetPhotos.Photo> data) {
        super(data);
    }

    public PhotoAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetPhotos.Photo photo) {
        GlideUtil.loadImage(mContext,photo.getUrl(),(ImageView) helper.getView(R.id.iv_image));
    }

}
