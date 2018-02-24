package com.tenz.hotchpotch.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.tenz.hotchpotch.util.LogUtil;
import com.tenz.hotchpotch.util.ResourceUtil;
import com.tenz.hotchpotch.util.ToastUtil;
import com.tenz.hotchpotch.widget.dialog.LoadingDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Author: TenzLiu
 * Date: 2018-01-18 22:26
 * Description: BaseFragment,并处理Fragment懒加载
 */

public abstract class BaseLazyFragment extends Fragment implements IBaseView,
        TakePhoto.TakeResultListener,InvokeListener {

    /**
     * Fragment的View加载完毕的标记(懒加载)
     */
    private boolean isViewCreate;
    /**
     * Fragment对用户可见的标记（懒加载）
     */
    private boolean isUIVisible;
    /**
     * 上下文对象
     */
    protected Context mContext;
    /**
     * 所属activity
     */
    protected Activity mActivity;
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //改变isUIVisible标记,isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
        if(isVisibleToUser){
            isUIVisible = true;
            lazyLoadData();
        }else{
            isUIVisible = false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //加载布局
        View view = inflater.inflate(getLayoutId(),container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //注册butterknife
        mUnbinder = ButterKnife.bind(this,view);
        mContext = getContext();
        mActivity = (Activity) getContext();
        //设置强制竖屏
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //设置拍照
        getTakePhoto().onCreate(savedInstanceState);
        initView(view,savedInstanceState);
        isViewCreate = true;
        lazyLoadData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type=PermissionManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
        PermissionManager.handlePermissionsResult(getActivity(),type,invokeParam,this);
    }

    /**
     *  获取TakePhoto实例
     * @return
     */
    public TakePhoto getTakePhoto(){
        if (takePhoto==null){
            takePhoto= (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this,this));
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
        PermissionManager.TPermissionType type=PermissionManager.checkPermission(
                TContextWrap.of(this),invokeParam.getMethod());
        if(PermissionManager.TPermissionType.WAIT.equals(type)){
            this.invokeParam=invokeParam;
        }
        return type;
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
    protected void initView(View view, Bundle savedInstanceState) {
    }

    /**
     * 懒加载方法
     */
    private void lazyLoadData() {
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,
        // 必须确保onCreateView加载完毕且页面可见,才加载数据
        if(isUIVisible && isViewCreate){
            initData();
            //数据加载完毕,恢复标记,防止重复加载
            isUIVisible = false;
            isViewCreate = false;
        }
    }

    /**
     * 初始化数据
     * 子类可以复写此方法初始化子类数据
     */
    protected void initData() {
        LogUtil.d("initData***********************");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
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
        if(mLoadingDialog != null && mLoadingDialog.isShowing()){
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void hideKeyBord() {
        //点击空白位置 隐藏软键盘
        InputMethodManager mInputMethodManager = (InputMethodManager) mContext.getSystemService
                (INPUT_METHOD_SERVICE);
        boolean hideSoftInputFromWindow = mInputMethodManager.hideSoftInputFromWindow(mActivity
                .getCurrentFocus().getWindowToken(), 0);
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
        Intent intent = new Intent(mContext,cls);
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
        Intent intent = new Intent(mContext,cls);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        startActivityForResult(intent,requestCode);
    }

    @Override
    public void back() {

    }

}
