package com.tenz.hotchpotch.module.main.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.app.AppManager;
import com.tenz.hotchpotch.app.Constant;
import com.tenz.hotchpotch.base.BaseActivity;
import com.tenz.hotchpotch.helper.BottomNavigationViewHelperHelp;
import com.tenz.hotchpotch.helper.TakePhotoHelper;
import com.tenz.hotchpotch.module.home.adapter.HomeAdapter;
import com.tenz.hotchpotch.module.home.fragment.HomeFragment;
import com.tenz.hotchpotch.module.login.activity.LoginActivity;
import com.tenz.hotchpotch.module.news.fragment.NewsFragment;
import com.tenz.hotchpotch.module.photo.fragment.PhotoFragment;
import com.tenz.hotchpotch.module.video.fragment.VideoFragment;
import com.tenz.hotchpotch.util.FileUtil;
import com.tenz.hotchpotch.util.GlideUtil;
import com.tenz.hotchpotch.util.JShareUtil;
import com.tenz.hotchpotch.util.LogUtil;
import com.tenz.hotchpotch.util.ResourceUtil;
import com.tenz.hotchpotch.util.SpUtil;
import com.tenz.hotchpotch.util.StatusBarUtil;
import com.tenz.hotchpotch.util.StringUtil;
import com.tenz.hotchpotch.util.ToastUtil;
import com.tenz.hotchpotch.widget.image.MovingImageView;
import com.tenz.hotchpotch.widget.image.MovingViewAnimator;
import com.tenz.hotchpotch.widget.image.ShapeImageView;
import com.ttsea.jrxbus2.RxBus2;
import com.ttsea.jrxbus2.Subscribe;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import cn.jiguang.share.qqmodel.QQ;
import cn.jiguang.share.wechat.Wechat;

public class MainActivity extends BaseActivity implements HomeAdapter.Option {

    @BindView(R.id.dl_main)
    DrawerLayout dl_main;
    @BindView(R.id.nv_main)
    NavigationView nv_main;

    //drawerlayout head
    MovingImageView miv_drawer_head_bg;
    ShapeImageView siv_head_icon;
    TextView tv_describe;
    TextView tv_welcome;
    TextView tv_user_name;

    @BindView(R.id.fl_container)
    FrameLayout fl_container;
    @BindView(R.id.bnv_content)
    BottomNavigationView bnv_content;

    private long mExitTime;//点击退出的时间
    private Fragment homeFragment;
    private Fragment newsFragment;
    private Fragment videoFragment;
    private Fragment photoFragment;
    private int currentFragmentPosition;//当前fragment位置

    public String imageLogoPath;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus2.getInstance().register(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //保存当前Fragment位置
        outState.putInt(Constant.KEY_MAIN_CURRENT_FRAGMENT_POSITION,currentFragmentPosition);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        StatusBarUtil.setTransparent(this);
        //初始化Butterknife不能注解的控件
        miv_drawer_head_bg = nv_main.getHeaderView(0).findViewById(R.id.miv_drawer_head_bg);
        siv_head_icon = nv_main.getHeaderView(0).findViewById(R.id.siv_head_icon);
        tv_describe = nv_main.getHeaderView(0).findViewById(R.id.tv_describe);
        tv_welcome = nv_main.getHeaderView(0).findViewById(R.id.tv_welcome);
        tv_user_name = nv_main.getHeaderView(0).findViewById(R.id.tv_user_name);
        //去掉BottomNavigationView效果
        BottomNavigationViewHelperHelp.disableShiftMode(bnv_content);
        initListener();
        initFragment(savedInstanceState);
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        //drawerlayour监听
        dl_main.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //抽屉布局滑动中暂停动画
                miv_drawer_head_bg.pauseMoving();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if(miv_drawer_head_bg.getMovingState() == MovingViewAnimator.MovingState.pause){
                    miv_drawer_head_bg.resumeMoving();
                }else if(miv_drawer_head_bg.getMovingState() == MovingViewAnimator.MovingState.stop){
                    miv_drawer_head_bg.startMoving();
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                //抽屉布局关闭停止动画
                miv_drawer_head_bg.stopMoving();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if(miv_drawer_head_bg.getMovingState() == MovingViewAnimator.MovingState.pause){
                    miv_drawer_head_bg.resumeMoving();
                }else if(miv_drawer_head_bg.getMovingState() == MovingViewAnimator.MovingState.stop){
                    miv_drawer_head_bg.startMoving();
                }
            }
        });
        //drawer NavigationView item点击监听
        nv_main.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_github_address:
                        Bundle bundle = new Bundle();
                        bundle.putString("url","https://github.com/TenzLiu/HotchPotch");
                        bundle.putString("htmlData","");
                        startActivity(WebActivity.class,bundle);
                        break;
                    case R.id.item_qrcode:
                        startActivity(ZXingActivity.class);
                        break;
                    case R.id.item_share:
                        shareApp("HotchPotch","采用mvp+retrofix+rxjava框架的一款集结多功能的个人项目",
                                "https://github.com/TenzLiu/HotchPotch",imageLogoPath);
                        break;
                    case R.id.item_about:
                        startActivity(AboutActivity.class);
                        break;
                    case R.id.item_setting:
                        startActivity(SettingActivity.class);
                        break;
                }
                //closeMenuDrawerLayout();
                return true;
            }
        });
        //bottomlayout item点击监听
        bnv_content.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.item_home:
                                StatusBarUtil.setTransparent(MainActivity.this);
                                switchToFragment(0);
                                break;
                            case R.id.item_news:
                                StatusBarUtil.setBarColor(MainActivity.this,
                                        ResourceUtil.getColor(R.color.colorApp));
                                switchToFragment(1);
                                break;
                            case R.id.item_video:
                                StatusBarUtil.setBarColor(MainActivity.this,
                                        ResourceUtil.getColor(R.color.colorApp));
                                switchToFragment(2);
                                break;
                            case R.id.item_photo:
                                StatusBarUtil.setBarColor(MainActivity.this,
                                        ResourceUtil.getColor(R.color.colorApp));
                                switchToFragment(3);
                                break;
                        }
                        return true;
                    }
                });
        //头像
        siv_head_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NiceDialog.init()
                        .setLayoutId(R.layout.layout_dialog_take_photo)     //设置dialog布局文件
                        .setConvertListener(new ViewConvertListener() {     //进行相关View操作的回调
                            @Override
                            public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                                holder.setOnClickListener(R.id.tv_take_photo, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        //拍照
                                        new TakePhotoHelper().takePhoto(MainActivity.this,
                                                getTakePhoto(),true,true);
                                    }
                                });
                                holder.setOnClickListener(R.id.tv_album, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        //相册
                                        new TakePhotoHelper().takePhotoAlbum(MainActivity.this,
                                                getTakePhoto(),true,true,1);
                                    }
                                });
                                holder.setOnClickListener(R.id.tv_cancel, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                            }
                        })
                        //.setDimAmount(0.3f)     //调节灰色背景透明度[0-1]，默认0.5f
                        .setShowBottom(true)     //是否在底部显示dialog，默认flase
                        .setMargin(10)     //dialog左右两边到屏幕边缘的距离（单位：dp），默认0dp
                        //.setWidth()     //dialog宽度（单位：dp），默认为屏幕宽度，-1代表WRAP_CONTENT
                        //.setHeight()     //dialog高度（单位：dp），默认为WRAP_CONTENT
                        //.setOutCancel(false)     //点击dialog外是否可取消，默认true
                        //.setAnimStyle(R.style.EnterExitAnimation)     //设置dialog进入、退出的动画style(底部显示的dialog有默认动画)
                        .show(getSupportFragmentManager());     //显示dialog
            }
        });
    }

    /**
     * 分享App
     */
    private void shareApp(String title, String content,String url,String imagePath) {
        //分享
        NiceDialog.init()
                .setLayoutId(R.layout.layout_dialog_share)     //设置dialog布局文件
                .setConvertListener(new ViewConvertListener() {     //进行相关View操作的回调
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        holder.setOnClickListener(R.id.ll_qq, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                //QQ分享
                                JShareUtil.share(QQ.Name,title,content,url,"",imagePath);
                            }
                        });
                        holder.setOnClickListener(R.id.ll_wechat, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                //微信分享
                                JShareUtil.share(Wechat.Name,title,content,url,"",imagePath);
                            }
                        });
                    }
                })
                .setShowBottom(true)
                //.setMargin(30)     //dialog左右两边到屏幕边缘的距离（单位：dp），默认0dp
                .show(getSupportFragmentManager());     //显示dialog
    }

    /**
     * 初始化fragment
     * @param savedInstanceState
     */
    private void initFragment(Bundle savedInstanceState) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(savedInstanceState != null){
            homeFragment = getSupportFragmentManager().findFragmentByTag(Constant.TAG_MAIN_FRAGMENT_HOME);
            newsFragment = getSupportFragmentManager().findFragmentByTag(Constant.TAG_MAIN_FRAGMENT_NEWS);
            videoFragment = getSupportFragmentManager().findFragmentByTag(Constant.TAG_MAIN_FRAGMENT_VIDEO);
            photoFragment = getSupportFragmentManager().findFragmentByTag(Constant.TAG_MAIN_FRAGMENT_PHOTO);
            //获取保存当前fragment位置
            currentFragmentPosition = savedInstanceState.getInt(Constant.KEY_MAIN_CURRENT_FRAGMENT_POSITION);
        }else{
            homeFragment = HomeFragment.getInstancec();
            newsFragment = NewsFragment.getInstancec();
            videoFragment = VideoFragment.getInstancec();
            photoFragment = PhotoFragment.getInstancec();
            fragmentTransaction.add(R.id.fl_container,homeFragment, Constant.TAG_MAIN_FRAGMENT_HOME);
            fragmentTransaction.add(R.id.fl_container,newsFragment, Constant.TAG_MAIN_FRAGMENT_NEWS);
            fragmentTransaction.add(R.id.fl_container,videoFragment, Constant.TAG_MAIN_FRAGMENT_VIDEO);
            fragmentTransaction.add(R.id.fl_container,photoFragment, Constant.TAG_MAIN_FRAGMENT_PHOTO);
        }
        fragmentTransaction.commitAllowingStateLoss();
        switchToFragment(currentFragmentPosition);
    }

    /**
     * 切换fragment
     * @param currentFragmentPosition
     */
    private void switchToFragment(int currentFragmentPosition) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (currentFragmentPosition){
            case 0:
                fragmentTransaction.hide(newsFragment);
                fragmentTransaction.hide(videoFragment);
                fragmentTransaction.hide(photoFragment);
                fragmentTransaction.show(homeFragment);
                break;
            case 1:
                fragmentTransaction.hide(homeFragment);
                fragmentTransaction.hide(videoFragment);
                fragmentTransaction.hide(photoFragment);
                fragmentTransaction.show(newsFragment);
                break;
            case 2:
                fragmentTransaction.hide(homeFragment);
                fragmentTransaction.hide(newsFragment);
                fragmentTransaction.hide(photoFragment);
                fragmentTransaction.show(videoFragment);
                break;
            case 3:
                fragmentTransaction.hide(homeFragment);
                fragmentTransaction.hide(newsFragment);
                fragmentTransaction.hide(videoFragment);
                fragmentTransaction.show(photoFragment);
                break;
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void initData() {
        super.initData();
        //设置用户头像
        if(!StringUtil.isEmpty(SpUtil.getString(mContext,Constant.KEY_USER_HEAD,""))){
            GlideUtil.loadImage(mContext,new File(SpUtil.getString(mContext,Constant.KEY_USER_HEAD,"")),siv_head_icon);
        }
        //获取logo路径
        new Thread(){
            @Override
            public void run() {
                File imageFile = FileUtil.copyResurces(mContext, "logo.png", "logo.png", 0);
                if(imageFile != null){
                    imageLogoPath = imageFile.getAbsolutePath();
                }
                super.run();
            }
        }.start();
    }

    /**
     * 打开抽屉布局
     */
    private void openMenuDrawerLayout(){
        if(!isMenuDrawerLayoutOpen()){
            dl_main.openDrawer(GravityCompat.START);
        }
    }

    /**
     * 关闭抽屉布局
     */
    private void closeMenuDrawerLayout(){
        if(isMenuDrawerLayoutOpen()){
            dl_main.closeDrawer(GravityCompat.START);
        }
    }

    /**
     * 判断抽屉布局是否打开
     * @return
     */
    private boolean isMenuDrawerLayoutOpen(){
        return dl_main.isDrawerOpen(GravityCompat.START);
    }

    @Override
    public void toNews() {
        bnv_content.setSelectedItemId(R.id.item_news);
    }

    @Override
    public void toVideo() {
        bnv_content.setSelectedItemId(R.id.item_video);
    }

    @Override
    public void toPhoto() {
        bnv_content.setSelectedItemId(R.id.item_photo);
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        SpUtil.putString(mContext,Constant.KEY_USER_HEAD,result.getImage().getCompressPath());
        GlideUtil.loadImage(mContext,new File(result.getImage().getCompressPath()),siv_head_icon);
    }

    /**
     * RxBus token过期接收通知
     * @param msg
     */
    @Subscribe(code = Constant.CODE_CODEOVERDUE)
    public void onCodeEvent(String msg){
        ToastUtil.showToast(msg);
        startActivity(LoginActivity.class);
        AppManager.getInstance().finishAllActivityExcept(LoginActivity.class);
    }

    @Override
    public void back() {
        super.back();
        if(isMenuDrawerLayoutOpen()){
            closeMenuDrawerLayout();
            return;
        }
        if(System.currentTimeMillis() - mExitTime > 2000){
            showToast("再按一次退出应用");
            mExitTime = System.currentTimeMillis();
        }else{
            AppManager.getInstance().finishActivity();
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus2.getInstance().unRegister(this);
    }

}
