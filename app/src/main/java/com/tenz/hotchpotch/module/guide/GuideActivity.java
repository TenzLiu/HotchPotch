package com.tenz.hotchpotch.module.guide;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.app.Constant;
import com.tenz.hotchpotch.base.BaseActivity;
import com.tenz.hotchpotch.module.login.activity.LoginActivity;
import com.tenz.hotchpotch.module.main.MainActivity;
import com.tenz.hotchpotch.util.ResourceUtil;
import com.tenz.hotchpotch.util.SpUtil;
import com.tenz.hotchpotch.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: TenzLiu
 * Date: 2018-01-19 14:24
 * Description: 引导页
 */

public class GuideActivity extends BaseActivity {

    @BindView(R.id.vp_guide)
    ViewPager vp_guide;
    @BindView(R.id.tv_experience)
    TextView tv_experience;

    private int[] mGuideImages = new int[]{R.mipmap.guide_01,R.mipmap.guide_02,R.mipmap.guide_03};
    private List<ImageView> mImageViewList;
    private GuideAdapter mGuideAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        StatusBarUtil.setTransparent(this);
        mImageViewList = new ArrayList<>();
        initGuideImages();
        mGuideAdapter = new GuideAdapter(mContext,mImageViewList);
        vp_guide.setAdapter(mGuideAdapter);
        vp_guide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 2){
                    tv_experience.setVisibility(View.VISIBLE);//最后显示立即体验按钮
                }else{
                    tv_experience.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 初始化图片
     */
    private void initGuideImages() {
        for(int i=0; i<3; i++){
            ImageView imageViewGuide = new ImageView(this);
            imageViewGuide.setImageDrawable(ResourceUtil.getDrawable(mGuideImages[i]));
            imageViewGuide.setScaleType(ImageView.ScaleType.FIT_XY);
            mImageViewList.add(imageViewGuide);
        }
    }

    @OnClick({R.id.tv_experience})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_experience:
                SpUtil.putBoolean(mContext, Constant.KEY_IS_GUIDE,true);
                if(SpUtil.getBoolean(mContext,Constant.KEY_IS_LOGIN,false)){
                    startActivity(MainActivity.class);
                }else{
                    startActivity(LoginActivity.class);
                }
                finish();
                break;
        }
    }

}
