package com.tenz.hotchpotch.module.home.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lzy.widget.PullZoomView;
import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.base.BaseActivity;
import com.tenz.hotchpotch.module.main.activity.MainActivity;
import com.tenz.hotchpotch.util.DisplayUtil;
import com.tenz.hotchpotch.util.LogUtil;
import com.tenz.hotchpotch.util.StatusBarUtil;
import com.tenz.hotchpotch.widget.scrollview.GradationScrollView;

import butterknife.BindView;

/**
 * Author: TenzLiu
 * Date: 2018-03-27 23:14
 * Description: scrollview滚动顶部toolbar渐变
 */

public class ScrollViewToolbarTransitionActivity extends BaseActivity implements GradationScrollView.ScrollViewListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.sv_scrollview_toolbar)
    GradationScrollView sv_scrollview_toolbar;
    @BindView(R.id.iv_head)
    ImageView iv_head;

    private int statusBarHeight;
    private int toolBarHeight;
    private int imageHeadHeight;
    private boolean isCanBack;//是否能返回,全透明状态不可返回

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scrollview_toolbar_transition;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        StatusBarUtil.setTransparent(ScrollViewToolbarTransitionActivity.this);
        initTitleBar(mToolbar,"toolbar渐变");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4以上
            statusBarHeight = StatusBarUtil.getStatusBarHeight(mContext);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mToolbar.getLayoutParams();
            layoutParams.setMargins(0,statusBarHeight,0,0);
            mToolbar.setLayoutParams(layoutParams);
        }
        //设置透明
        StatusBarUtil.setBarColor(ScrollViewToolbarTransitionActivity.this,Color.argb(0,99,184,255));
        mToolbar.setAlpha(0);
        mToolbar.setEnabled(false);
        sv_scrollview_toolbar.setScrollViewListener(this);
        sv_scrollview_toolbar.setScrollViewListener(this);
        DisplayUtil.measureWidthAndHeight(iv_head);
        imageHeadHeight = iv_head.getMeasuredHeight();
    }

    @Override
    protected void initData() {
        super.initData();
        toolBarHeight = DisplayUtil.dp2px(50);
    }

    @Override
    public void toolBarBack() {
        if(isCanBack){
            finish();
        }
    }

    @Override
    public void onScrollChanged(GradationScrollView scrollView, int x, int y, int oldx, int oldy) {
        if(y <= 0){
            //设置背景全透明
            StatusBarUtil.setBarColor(ScrollViewToolbarTransitionActivity.this,Color.argb(0,99,184,255));
            mToolbar.setAlpha(0);
            isCanBack = false;
        }else if(y > 0 && y <= (imageHeadHeight-toolBarHeight)){
            ///滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / (imageHeadHeight-toolBarHeight);
            float alpha = (255 * scale);
            StatusBarUtil.setBarColor(ScrollViewToolbarTransitionActivity.this,Color.argb((int) alpha,99,184,255));
            mToolbar.setAlpha(scale);
            isCanBack = true;
        }else{
            //设置背景不透明
            StatusBarUtil.setBarColor(ScrollViewToolbarTransitionActivity.this,Color.argb(255,99,184,255));
            mToolbar.setAlpha(1);
            isCanBack = true;
        }
    }

}
