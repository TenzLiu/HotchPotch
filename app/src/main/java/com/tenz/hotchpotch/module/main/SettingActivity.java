package com.tenz.hotchpotch.module.main;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.app.AppManager;
import com.tenz.hotchpotch.app.Constant;
import com.tenz.hotchpotch.base.BaseActivity;
import com.tenz.hotchpotch.module.login.activity.LoginActivity;
import com.tenz.hotchpotch.util.ResourceUtil;
import com.tenz.hotchpotch.util.SpUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: TenzLiu
 * Date: 2018-01-21 13:53
 * Description: SettingActivity
 */

public class SettingActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initTitleBar(mToolbar, ResourceUtil.getString(R.string.setting));
    }

    @OnClick({R.id.btn_logout})
    public void onCLick(View view){
        switch (view.getId()){
            case R.id.btn_logout:
                startActivity(LoginActivity.class);
                SpUtil.putBoolean(mContext, Constant.KEY_IS_LOGIN,false);
                AppManager.getInstance().finishAllActivityExcept(LoginActivity.class);
                break;
        }
    }

}
