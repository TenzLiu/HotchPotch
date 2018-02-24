package com.tenz.hotchpotch.module.main.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.base.BaseActivity;
import com.tenz.hotchpotch.util.AppUtil;
import com.tenz.hotchpotch.util.ResourceUtil;

import butterknife.BindView;

/**
 * Author: TenzLiu
 * Date: 2018-02-08 14:49
 * Description: AboutActivity
 */

public class AboutActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_version_name)
    TextView tv_version_name;
    @BindView(R.id.tv_production_introduction)
    TextView tv_production_introduction;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initTitleBar(mToolbar,"关于");
        tv_version_name.setText(AppUtil.getAppVersionName(mContext));
        tv_production_introduction.setText(ResourceUtil.getString(R.string.about_production));
    }
}
