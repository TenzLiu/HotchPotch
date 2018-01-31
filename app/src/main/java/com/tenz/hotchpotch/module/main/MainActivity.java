package com.tenz.hotchpotch.module.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.app.AppManager;
import com.tenz.hotchpotch.app.Constant;
import com.tenz.hotchpotch.base.BaseActivity;
import com.tenz.hotchpotch.http.RetrofitFactory;
import com.tenz.hotchpotch.module.home.adapter.HomeAdapter;
import com.tenz.hotchpotch.module.home.fragment.HomeFragment;
import com.tenz.hotchpotch.module.news.fragment.NewsFragment;
import com.tenz.hotchpotch.module.photo.fragment.PhotoFragment;
import com.tenz.hotchpotch.module.video.fragment.VideoFragment;
import com.tenz.hotchpotch.util.BottomNavigationViewHelperUtil;
import com.tenz.hotchpotch.util.ResourceUtil;
import com.tenz.hotchpotch.util.StatusBarUtil;
import com.tenz.hotchpotch.widget.image.MovingImageView;
import com.tenz.hotchpotch.widget.image.MovingViewAnimator;
import com.tenz.hotchpotch.widget.image.ShapeImageView;

import butterknife.BindView;

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
        BottomNavigationViewHelperUtil.disableShiftMode(bnv_content);
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

                        break;
                    case R.id.item_qrcode:

                        break;
                    case R.id.item_share:

                        break;
                    case R.id.item_about:

                        break;
                    case R.id.item_setting:
                        startActivity(SettingActivity.class);
                        break;
                }
                closeMenuDrawerLayout();
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
                                StatusBarUtil.setBarColor(MainActivity.this, ResourceUtil.getColor(R.color.colorApp));
                                switchToFragment(1);
                                break;
                            case R.id.item_video:
                                StatusBarUtil.setBarColor(MainActivity.this, ResourceUtil.getColor(R.color.colorApp));
                                switchToFragment(2);
                                break;
                            case R.id.item_photo:
                                StatusBarUtil.setBarColor(MainActivity.this, ResourceUtil.getColor(R.color.colorApp));
                                switchToFragment(3);
                                break;
                        }
                        return true;
                    }
                });
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
            AppManager.getInstance().exitApp(mContext,false);
        }
    }

    @Override
    public void onBackPressed() {
        back();
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

}
