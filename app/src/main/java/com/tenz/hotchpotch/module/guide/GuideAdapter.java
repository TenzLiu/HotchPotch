package com.tenz.hotchpotch.module.guide;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Author: TenzLiu
 * Date: 2018-01-19 14:43
 * Description: GuideAdapter
 */

public class GuideAdapter extends PagerAdapter {

    private Context mContext;
    private List<ImageView> mImageViewList;

    public GuideAdapter(Context context, List<ImageView> imageViewList) {
        mContext = context;
        mImageViewList = imageViewList;
    }

    @Override
    public int getCount() {
        return mImageViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mImageViewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mImageViewList.get(position);
        container.addView(view);
        return view;
    }

}
