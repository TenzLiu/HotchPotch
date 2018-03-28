package com.tenz.hotchpotch.module.home.activity;

import android.os.Bundle;

import com.lzy.widget.PullZoomView;
import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.base.BaseActivity;
import com.tenz.hotchpotch.util.StatusBarUtil;

import butterknife.BindView;

/**
 * Author: TenzLiu
 * Date: 2018-03-28 15:13
 * Description: 下拉头部放大的布局效果
 */

public class PullZoomViewActivity extends BaseActivity {

    @BindView(R.id.pzv_scrollview)
    PullZoomView pzv_scrollview;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pullzoomview;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        StatusBarUtil.setTransparent(PullZoomViewActivity.this);
        pzv_scrollview.setIsParallax(true);
        pzv_scrollview.setIsZoomEnable(true);
        pzv_scrollview.setSensitive(1.5f);
        pzv_scrollview.setZoomTime(500);
        pzv_scrollview.setOnScrollListener(new PullZoomView.OnScrollListener() {
            @Override
            public void onScroll(int l, int t, int oldl, int oldt) {
                System.out.println("onScroll   t:" + t + "  oldt:" + oldt);
            }

            @Override
            public void onHeaderScroll(int currentY, int maxY) {
                System.out.println("onHeaderScroll   currentY:" + currentY + "  maxY:" + maxY);
            }

            @Override
            public void onContentScroll(int l, int t, int oldl, int oldt) {
                System.out.println("onContentScroll   t:" + t + "  oldt:" + oldt);
            }
        });
        pzv_scrollview.setOnPullZoomListener(new PullZoomView.OnPullZoomListener() {
            @Override
            public void onPullZoom(int originHeight, int currentHeight) {
                System.out.println("onPullZoom  originHeight:" + originHeight + "  currentHeight:" + currentHeight);
            }

            @Override
            public void onZoomFinish() {
                System.out.println("onZoomFinish");
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();

    }

}
