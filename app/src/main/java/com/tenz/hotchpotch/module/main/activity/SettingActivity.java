package com.tenz.hotchpotch.module.main.activity;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
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
import com.tenz.hotchpotch.service.AppUpdateService;
import com.tenz.hotchpotch.util.AppUtil;
import com.tenz.hotchpotch.util.FileUtil;
import com.tenz.hotchpotch.util.LogUtil;
import com.tenz.hotchpotch.util.ResourceUtil;
import com.tenz.hotchpotch.util.SpUtil;
import com.tenz.hotchpotch.util.ToastUtil;
import com.tenz.hotchpotch.widget.dialog.ConfirmDialog;

import java.io.File;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.ShareParams;
import cn.jiguang.share.qqmodel.QQ;

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

    private BaseNiceDialog appUpdateProgressDialog;
    private ProgressBar pb_update;
    private TextView tv_progress;

    private int versionCode;
    private String versionName;
    private String apkUrl = "http://pro-app-qn.fir.im/46b10550719bd83157235014fec0144b8291e5d8.apk?" +
            "attname=hotchpotch.apk_1.6.2.apk&e=1519633005&token=LOvmia8oXF4xnLh0IdH05XMYpH6ENHNpARlmPc-T:bpNO0snU5oIkIFXithxANx3Rt5A=";

    private AppUpdateService.MyBinder myBinder;
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (AppUpdateService.MyBinder) service;
            myBinder.getService().setmProgressListener(new AppUpdateService.OnProgressListener() {
                @Override
                public void onProgress(int progress) {
                    //更新对话框进度条
                    pb_update.setProgress(progress);
                    tv_progress.setText(String.valueOf(progress)+"%");
                }

                @Override
                public void onSuccess(boolean isSuccess) {
                    appUpdateProgressDialog.dismiss();
                    //失败提示
                    if (!isSuccess) {
                        showToast("下载失败");
                    }else{
                        showToast("下载成功");
                    }
                }

            });
        }
    };

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
        // 绑定Service
        Intent intent = new Intent(this,AppUpdateService.class);
        bindService(intent,connection, Context.BIND_AUTO_CREATE);

    }

    @OnClick({R.id.rl_modify_information,R.id.rl_modify_password,R.id.rl_version,R.id.btn_logout})
    public void onCLick(View view){
        switch (view.getId()){
            case R.id.rl_modify_information:

                break;
            case R.id.rl_modify_password:

                break;
            case R.id.rl_version:
                checkUpdateVersion();
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
     * App检查版本更新
     */
    private void checkUpdateVersion() {
        //判断版本号
        if(AppUtil.getVersionCode(mContext)>0){
            //App版本更新对话框
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
                                    //apk下载更新
                                    appUpdate();
                                }
                            });
                        }
                    })
                    .setMargin(30)     //dialog左右两边到屏幕边缘的距离（单位：dp），默认0dp
                    .show(getSupportFragmentManager());     //显示dialog
        }else{
            ToastUtil.showToast("当前已是最新版本");
        }
    }

    /**
     * App版本更新
     */
    private void appUpdate() {
        //App版本更新下载进度对话框
        appUpdateProgressDialog = NiceDialog.init()
                .setLayoutId(R.layout.layout_dialog_app_update_progress)     //设置dialog布局文件
                .setConvertListener(new ViewConvertListener() {     //进行相关View操作的回调
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        pb_update = holder.getView(R.id.pb_update);
                        tv_progress = holder.getView(R.id.tv_progress);
                        //初始化为0
                        pb_update.setProgress(0);
                        tv_progress.setText("0%");
                        holder.setOnClickListener(R.id.tv_update_cancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                //取消更新
                                myBinder.calcelDowmload();
                            }
                        });
                        holder.setOnClickListener(R.id.tv_update_background, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                //后台更新
                            }
                        });
                    }
                })
                .setMargin(30)     //dialog左右两边到屏幕边缘的距离（单位：dp），默认0dp
        .show(getSupportFragmentManager());//显示dialog
        myBinder.dwonLoadApk(apkUrl,"hotchpotch.apk");
    }

    @Override
    protected void onDestroy() {
        unbindService(connection);
        super.onDestroy();
    }

}
