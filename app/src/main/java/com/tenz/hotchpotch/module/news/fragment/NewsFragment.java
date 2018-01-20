package com.tenz.hotchpotch.module.news.fragment;

import android.os.Bundle;

import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.base.BaseFragment;

/**
 * Author: TenzLiu
 * Date: 2018-01-20 16:51
 * Description: NewsFragment
 */

public class NewsFragment extends BaseFragment {

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

}
