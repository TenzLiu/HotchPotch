package com.tenz.hotchpotch.module.home.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.base.BaseFragment;
import com.tenz.hotchpotch.util.LogUtil;
import com.tenz.hotchpotch.util.ToastUtil;
import com.tenz.hotchpotch.widget.banner.BannerImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Author: TenzLiu
 * Date: 2018-01-20 16:51
 * Description: HomeFragment
 */

public class HomeFragment extends BaseFragment {

    @BindView(R.id.banner_home)
    Banner banner_home;

    private List<String> bannerImageList;
    private List<String> bannerTitleList;

    public static HomeFragment getInstancec(){
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);

    }

    @Override
    protected void initData() {
        super.initData();
        initBannerData();
        banner_home.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器
        banner_home.setImageLoader(new BannerImageLoader());
        //设置图片集合
        banner_home.setImages(bannerImageList);
        banner_home.setBannerTitles(bannerTitleList);
        //banner设置方法全部调用完毕时最后调用
        banner_home.start();
    }

    /**
     * 初始化banner数据
     */
    private void initBannerData() {
        bannerImageList = new ArrayList<>();
        bannerTitleList = new ArrayList<>();
        bannerImageList.add("http://img.zcool.cn/community/01b72057a7e0790000018c1bf4fce0.png");
        bannerImageList.add("http://img.zcool.cn/community/01fca557a7f5f90000012e7e9feea8.jpg");
        bannerImageList.add("http://img.zcool.cn/community/01996b57a7f6020000018c1bedef97.jpg");
        bannerImageList.add("http://img.zcool.cn/community/01700557a7f42f0000018c1bd6eb23.jpg");
        bannerTitleList.add("初秋特惠大放价");
        bannerTitleList.add("坚果早餐生活");
        bannerTitleList.add("夏日特惠4折起");
        bannerTitleList.add("女性专场返利");
    }

}
