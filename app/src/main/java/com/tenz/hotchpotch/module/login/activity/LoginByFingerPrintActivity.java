package com.tenz.hotchpotch.module.login.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.app.Constant;
import com.tenz.hotchpotch.base.BaseActivity;
import com.tenz.hotchpotch.helper.CryptoObjectHelper;
import com.tenz.hotchpotch.module.main.MainActivity;
import com.tenz.hotchpotch.rx.RxScheduler;
import com.tenz.hotchpotch.util.ResourceUtil;
import com.tenz.hotchpotch.util.SpUtil;
import com.tenz.hotchpotch.util.ToastUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Author: TenzLiu
 * Date: 2018-01-21 15:34
 * Description: LoginByFingerPrintActivity 指纹登录
 */

public class LoginByFingerPrintActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_finger_print)
    ImageView iv_finger_print;
    @BindView(R.id.tv_finger_print)
    TextView tv_finger_print;

    private FingerprintManagerCompat fingerprintManager;
    private CryptoObjectHelper cryptoObjectHelper;
    private android.support.v4.os.CancellationSignal cancellationSignal;
    private FingerprintManagerCompat.AuthenticationCallback authenticationCallback;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_by_finger_print;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initTitleBar(mToolbar, ResourceUtil.getString(R.string.login_by_finger_print));
    }

    @Override
    protected void initData() {
        super.initData();
        fingerprintManager = FingerprintManagerCompat.from(this);
        cancellationSignal = new android.support.v4.os.CancellationSignal();
        authenticationCallback = new FingerprintManagerCompat.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                super.onAuthenticationError(errMsgId, errString);
                //但多次指纹密码验证错误后，进入此方法；并且，不能短时间内调用指纹验证
                tv_finger_print.setText(errString);
            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                super.onAuthenticationHelp(helpMsgId, helpString);
                tv_finger_print.setText(helpString);
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                tv_finger_print.setText("指纹识别登录成功,3s后自动登录");
                //倒计时3秒登录
                final int count = 3;
                Observable.interval(0,1, TimeUnit.SECONDS)
                        .take(count)
                        .map(new Function<Long, Long>() {
                            @Override
                            public Long apply(Long aLong) throws Exception {
                                return count - aLong;
                            }
                        }).compose(RxScheduler.<Long>rxSchedulerTransform())
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Long value) {
                                tv_finger_print.setText("指纹识别登录成功,"+value+"s后自动登录");
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                SpUtil.putBoolean(mContext, Constant.KEY_IS_LOGIN,true);
                                startActivity(MainActivity.class);
                            }
                        });
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                tv_finger_print.setText("指纹识别失败，请重新识别");
            }
        };
        try {
            cryptoObjectHelper = new CryptoObjectHelper();
            //是否有指纹识别的硬件
            if(!fingerprintManager.isHardwareDetected()){
                ToastUtil.showToast("该手机不支持指纹识别");
            }else{
                //是否录入指纹
                if(!fingerprintManager.hasEnrolledFingerprints()){
                    //google没有开放让普通app启动指纹注册界面的权限
                    ToastUtil.showToast("该手机还未录入指纹，请设置");
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(intent);
                }else{
                    RxPermissions rxPermissions = new RxPermissions(this);
                    rxPermissions.request(new String[]{Manifest.permission.USE_FINGERPRINT})
                            .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if(aBoolean){
                                //进行指纹识别
                                fingerprintManager.authenticate(cryptoObjectHelper.buildCryptoObject(),
                                        0,cancellationSignal,authenticationCallback,null);
                            }else{
                                ToastUtil.showToast("没有指纹识别权限，请设置");
                                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
