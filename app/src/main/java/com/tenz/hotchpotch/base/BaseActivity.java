package com.tenz.hotchpotch.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.app.AppManager;
import com.tenz.hotchpotch.util.LogUtil;
import com.tenz.hotchpotch.util.ResourceUtil;
import com.tenz.hotchpotch.util.StatusBarUtil;
import com.tenz.hotchpotch.util.ToastUtil;
import com.tenz.hotchpotch.widget.dialog.LoadingDialog;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author: TenzLiu
 * Date: 2018-01-18 16:52
 * Description: BaseActivity
 */

public abstract class BaseActivity extends RxAppCompatActivity implements IBaseView,
        TakePhoto.TakeResultListener,InvokeListener {

    /**
     * 上下文对象
     */
    protected Context mContext;
    /**
     * ButterKnife
     */
    private Unbinder mUnbinder;
    /**
     * 加载框
     */
    private LoadingDialog mLoadingDialog;
    /**
     * 拍照
     */
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    /**
     * 是否显示toolbar的右上角按钮
     */
    private boolean isShowRight;
    /**
     * toolbar的右上角按钮类型
     * 1:分享
     */
    private int rightType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type=PermissionManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
        PermissionManager.handlePermissionsResult(this,type,invokeParam,this);
    }

    /**
     *  获取TakePhoto实例
     * @return
     */
    public TakePhoto getTakePhoto(){
        if (takePhoto==null){
            takePhoto= (TakePhoto) TakePhotoInvocationHandler.of(this)
                    .bind(new TakePhotoImpl(this,this));
        }
        return takePhoto;
    }

    @Override
    public void takeSuccess(TResult result) {
        //LogUtil.d("takeSuccess：" + result.getImage().getCompressPath());
    }

    @Override
    public void takeFail(TResult result,String msg) {
        //LogUtil.d("takeFail:" + msg);
        ToastUtil.showToast(msg);
    }

    @Override
    public void takeCancel() {
        LogUtil.d( getResources().getString(R.string.msg_operation_canceled));
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type=PermissionManager.checkPermission(TContextWrap.of(this),invokeParam.getMethod());
        if(PermissionManager.TPermissionType.WAIT.equals(type)){
            this.invokeParam=invokeParam;
        }
        return type;
    }

    /**
     *
     * @param savedInstanceState
     */
    private void init(Bundle savedInstanceState){
        //加载布局
        setContentView(getLayoutId());
        //注册butterknife
        mUnbinder = ButterKnife.bind(this);
        //设置状态栏透明
        StatusBarUtil.setBarColor(this, ResourceUtil.getColor(R.color.colorApp));
        //设置强制竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = this;
        //设置拍照
        getTakePhoto().onCreate(savedInstanceState);
        initView(savedInstanceState);
        initData();
        AppManager.getInstance().addActivity(this);
        LogUtil.d(getComponentName().getClassName());
    }

    /**
     * 获取当前layouty的布局ID,用于设置当前布局
     * 交由子类实现
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化view
     * 子类实现 控件绑定、视图初始化等内容
     * @param savedInstanceState
     */
    protected void initView(Bundle savedInstanceState) {
    }

    /**
     * 初始化数据
     * 子类可以复写此方法初始化子类数据
     */
    protected void initData() {
    }

    /**
     * 设置toolbar
     * @param toolbar
     * @param title
     */
    protected void initTitleBar(Toolbar toolbar, String title){
        initTitleBar(toolbar, title, true, false, 0);
    }

    /**
     * 设置toolbar
     * @param toolbar
     * @param title
     */
    protected void initTitleBar(Toolbar toolbar, String title, boolean showHomeAsUp){
        initTitleBar(toolbar, title, showHomeAsUp, false, 0);
    }

    /**
     * 设置toolbar
     * @param toolbar
     * @param title
     */
    protected void initTitleBar(Toolbar toolbar, String title, boolean isShowRight, int rightType){
        initTitleBar(toolbar, title, true, isShowRight, rightType);
    }

    /**
     * 设置toolbar
     * @param toolbar
     * @param title
     */
    protected void initTitleBar(Toolbar toolbar, String title,
                                boolean showHomeAsUp, boolean isShowRight, int rightType){
        this.isShowRight = isShowRight;
        this.rightType = rightType;
        toolbar.setTitle(title);
        //将Toolbar显示到界面
        setSupportActionBar(toolbar);
        if(showHomeAsUp){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);//给左上角图标的左边加上一个返回的图标 。对应ActionBar.DISPLAY_HOME_AS_UP
            getSupportActionBar().setDisplayShowHomeEnabled(true);//决定左上角的图标是否可以点击
            toolbar.setNavigationIcon(ResourceUtil.getDrawable(R.mipmap.back_icon));//左上角的图标
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem;
        if (isShowRight) {
            getMenuInflater().inflate(R.menu.menu_toolbar_right, menu);
            menuItem = menu.findItem(R.id.action_icon);
            switch (rightType) {
                case 1:
                    //分享
                    menuItem.setTitle("分享");
                    break;
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_icon:
                onSubTitleClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * toolbar右上角按钮点击事件
     */
    protected void onSubTitleClick() {

    }

    @Override
    public void showToast(String message) {
        ToastUtil.showToast(message);
    }

    @Override
    public void showLoadingDialog() {
        showLoadingDialog(ResourceUtil.getString(R.string.loading));
    }

    @Override
    public void showLoadingDialog(String message) {
        if(mLoadingDialog == null){
            mLoadingDialog = new LoadingDialog(mContext);
        }else if(mLoadingDialog.isShowing()){
            return;
        }
        mLoadingDialog.setMessage(message);
        mLoadingDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        if(mLoadingDialog != null){
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void hideKeyBord() {
        //点击空白位置 隐藏软键盘
        InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService
                (INPUT_METHOD_SERVICE);
        boolean hideSoftInputFromWindow = mInputMethodManager.hideSoftInputFromWindow(this
                .getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void back() {

    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.<T>bindToLifecycle();
    }

    /**
     * 跳转页面
     * @param cls
     */
    public void startActivity(Class cls){
        startActivity(cls,null);
    }

    /**
     * 跳转页面,带参数
     * @param cls
     */
    public void startActivity(Class cls, Bundle bundle){
        Intent intent = new Intent(this,cls);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 跳转页面,有返回
     * @param cls
     * @param requestCode
     */
    public void startActivityForResult(Class cls,int requestCode){
        startActivityForResult(cls,null,requestCode);
    }

    /**
     * 跳转页面,带参数,有返回
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class cls, Bundle bundle, int requestCode){
        Intent intent = new Intent(this,cls);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        startActivityForResult(intent,requestCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        AppManager.getInstance().finishActivity(this);
    }

}
