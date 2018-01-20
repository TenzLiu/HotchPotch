package com.tenz.hotchpotch.module.photo.fragment;

import android.os.Bundle;

import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.base.BaseFragment;

/**
 * Author: TenzLiu
 * Date: 2018-01-20 16:51
 * Description: PhotoFragment
 */

public class PhotoFragment extends BaseFragment {

    public static PhotoFragment getInstancec(){
        PhotoFragment photoFragment = new PhotoFragment();
        Bundle bundle = new Bundle();
        photoFragment.setArguments(bundle);
        return photoFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_photo;
    }

}
