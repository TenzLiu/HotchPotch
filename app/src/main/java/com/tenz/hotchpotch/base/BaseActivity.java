package com.tenz.hotchpotch.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.app.AppManager;
import com.tenz.hotchpotch.base.IBaseView;
import com.tenz.hotchpotch.util.LogUtil;
import com.tenz.hotchpotch.util.ResourceUtil;
import com.tenz.hotchpotch.util.StatusBarUtil;
import com.tenz.hotchpotch.util.ToastUtil;
import com.tenz.hotchpotch.widget.dialog.LoadingDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author: TenzLiu
 * Date: 2018-01-18 16:52
 * Description: BaseActivity
 */

public abstract class BaseActivity extends AppCompatActivity implements IBaseView {

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState){
        //加载布局
        setContentView(getLayoutId());
        //注册butterknife
        mUnbinder = ButterKnife.bind(this);
        //设置状态栏透明
        StatusBarUtil.setTransparent(this);
        //设置强制竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = this;
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
            mLoadingDialog = new LoadingDialog(this);
        }else if(mLoadingDialog.isShowing()){
            return;
        }
        mLoadingDialog.setMessage(message);
        mLoadingDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        if(mLoadingDialog != null && mLoadingDialog.isShowing()){
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
    }
}
