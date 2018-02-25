package com.tenz.hotchpotch.module.main.activity;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.app.AppManager;
import com.tenz.hotchpotch.app.Constant;
import com.tenz.hotchpotch.base.BaseActivity;
import com.tenz.hotchpotch.module.login.activity.LoginActivity;
import com.tenz.hotchpotch.util.AppUtil;
import com.tenz.hotchpotch.util.ResourceUtil;
import com.tenz.hotchpotch.util.SpUtil;
import com.tenz.hotchpotch.util.ToastUtil;
import com.tenz.hotchpotch.widget.dialog.ConfirmDialog;

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
    @BindView(R.id.tv_version_name)
    TextView tv_version_name;

    private int versionCode;
    private String versionName;
    private String apkUrl = "http://pro-app-tc.fir.im/f1f7e92b1bb735baea5b85f162a560ef704b490a.apk?sign=5b5d2430ce1d6ca45ac9c475a319de9d&t=5a7c218f";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initTitleBar(mToolbar, ResourceUtil.getString(R.string.setting));
        tv_version_name.setText(AppUtil.getAppVersionName(mContext));
    }

    @Override
    protected void initData() {
        super.initData();
        versionCode = AppUtil.getVersionCode(mContext);
        versionName = AppUtil.getAppVersionName(mContext);
    }

    @OnClick({R.id.rl_modify_information,R.id.rl_modify_password,R.id.rl_version,R.id.btn_logout})
    public void onCLick(View view){
        switch (view.getId()){
            case R.id.rl_modify_information:

                break;
            case R.id.rl_modify_password:

                break;
            case R.id.rl_version:
                updateVersion();
                break;
            case R.id.btn_logout:
                ConfirmDialog.show(getSupportFragmentManager(), "提示", "确定退出登录",new ConfirmDialog.ConfirmDialogListener(){

                    @Override
                    public void onNegative(BaseNiceDialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onPositive(BaseNiceDialog dialog) {
                        dialog.dismiss();
                        startActivity(LoginActivity.class);
                        SpUtil.putBoolean(mContext, Constant.KEY_IS_LOGIN,false);
                        AppManager.getInstance().finishAllActivityExcept(LoginActivity.class);
                    }
                });
                break;
        }
    }

    /**
     * 版本更新
     */
    private void updateVersion() {
        //判断版本号
        if(AppUtil.getVersionCode(mContext)>0){
            NiceDialog.init()
                    .setLayoutId(R.layout.layout_dialog_app_update)     //设置dialog布局文件
                    .setConvertListener(new ViewConvertListener() {     //进行相关View操作的回调
                        @Override
                        public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                            holder.setOnClickListener(R.id.btn_later, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            holder.setOnClickListener(R.id.btn_update, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    ToastUtil.showToast("update...");
                                }
                            });
                        }
                    })
                    //.setDimAmount(0.3f)     //调节灰色背景透明度[0-1]，默认0.5f
                    //.setShowBottom(true)     //是否在底部显示dialog，默认flase
                    .setMargin(30)     //dialog左右两边到屏幕边缘的距离（单位：dp），默认0dp
                    //.setWidth()     //dialog宽度（单位：dp），默认为屏幕宽度，-1代表WRAP_CONTENT
                    //.setHeight()     //dialog高度（单位：dp），默认为WRAP_CONTENT
                    //.setOutCancel(false)     //点击dialog外是否可取消，默认true
                    //.setAnimStyle(R.style.EnterExitAnimation)     //设置dialog进入、退出的动画style(底部显示的dialog有默认动画)
                    .show(getSupportFragmentManager());     //显示dialog
        }
    }

}
