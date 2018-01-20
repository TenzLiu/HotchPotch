package com.tenz.hotchpotch.module.video.fragment;

import android.os.Bundle;

import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.base.BaseFragment;

/**
 * Author: TenzLiu
 * Date: 2018-01-20 16:51
 * Description: VideoFragment
 */

public class VideoFragment extends BaseFragment {

    public static VideoFragment getInstancec(){
        VideoFragment videoFragment = new VideoFragment();
        Bundle bundle = new Bundle();
        videoFragment.setArguments(bundle);
        return videoFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

}
