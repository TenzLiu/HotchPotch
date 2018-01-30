package com.tenz.hotchpotch.module.welcome;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.app.Constant;
import com.tenz.hotchpotch.base.BaseActivity;
import com.tenz.hotchpotch.module.guide.GuideActivity;
import com.tenz.hotchpotch.module.login.activity.LoginActivity;
import com.tenz.hotchpotch.module.main.MainActivity;
import com.tenz.hotchpotch.rx.RxScheduler;
import com.tenz.hotchpotch.util.DisplayUtil;
import com.tenz.hotchpotch.util.SpUtil;
import com.tenz.hotchpotch.util.StatusBarUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Author: TenzLiu
 * Date: 2018-01-19 09:55
 * Description: 欢迎页
 */

public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.ll_skip)
    LinearLayout ll_skip;
    @BindView(R.id.tv_skip_time)
    TextView tv_skip_time;
    @BindView(R.id.iv_welcome)
    ImageView iv_welcome;

    private int mTime = 3;
    private boolean mIsCancel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        StatusBarUtil.setTransparent(this);
        requestPermissions();
        //去除状态栏高度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            DisplayUtil.setMargins(ll_skip,0, StatusBarUtil.getStatusBarHeight(mContext)
                    +DisplayUtil.dp2px(20),DisplayUtil.dp2px(20),0);
        }
        startAnima();
    }

    /**
     * 申请权限
     */
    private void requestPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if(!granted){
                            showToast("App未能获取全部相关权限");
                        }
                        innitCountDown();
                    }
                });
    }

    /**
     * 初始化倒计时
     */
    private void innitCountDown() {
        Observable.interval(0,1, TimeUnit.SECONDS)
                .take(3)//3s自动结束
                .map(new Function<Long, Long>() {//转换
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return mTime - aLong;
                    }
                }).compose(RxScheduler.<Long>rxSchedulerTransform())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long value) {
                        if(tv_skip_time!=null)
                            tv_skip_time.setText(String.valueOf(value));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if(!mIsCancel){
                            toMain();
                        }
                    }
                });
    }

    /**
     * 欢迎页渐出动画
     */
    private void startAnima() {
        AlphaAnimation animation = new AlphaAnimation(0.5f,1.0f);
        animation.setDuration(2000);
        iv_welcome.setAnimation(animation);
    }

    @OnClick({R.id.ll_skip})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_skip:
                mIsCancel = true;
                toMain();
                break;
        }
    }

    /**
     * 跳转
     */
    private void toMain(){
        if(SpUtil.getBoolean(mContext, Constant.KEY_IS_GUIDE,false)){
            if(SpUtil.getBoolean(mContext,Constant.KEY_IS_LOGIN,false)){
                startActivity(MainActivity.class);
            }else{
                startActivity(LoginActivity.class);
            }
        }else{
            startActivity(GuideActivity.class);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        //禁止返回
    }

}
