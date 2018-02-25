package com.tenz.hotchpotch.module.main.activity;

import android.Manifest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jph.takephoto.model.TResult;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tbruyelle.rxpermissions2.RxPermissionsFragment;
import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.app.Constant;
import com.tenz.hotchpotch.base.BaseActivity;
import com.tenz.hotchpotch.helper.TakePhotoHelper;
import com.tenz.hotchpotch.util.StringUtil;
import com.tenz.hotchpotch.util.ToastUtil;
import com.ttsea.jrxbus2.RxBus2;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import io.reactivex.functions.Consumer;

/**
 * Author: TenzLiu
 * Date: 2018-02-23 17:29
 * Description: ZXingScanActivity
 */

public class ZXingScanActivity extends BaseActivity implements QRCodeView.Delegate {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.zv_qrcode)
    ZXingView zv_qrcode;
    @BindView(R.id.iv_album)
    ImageView iv_album;
    @BindView(R.id.tv_album)
    TextView tv_album;
    @BindView(R.id.iv_flash_light)
    ImageView iv_flash_light;
    @BindView(R.id.tv_flash_light)
    TextView tv_flash_light;

    private boolean isOpenFlashLight;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_zxing_scan;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initTitleBar(mToolbar,"二维码扫描");
        zv_qrcode.setDelegate(this);
        RxPermissions rxPermissions = new RxPermissions(ZXingScanActivity.this);
        rxPermissions.request(Manifest.permission.CAMERA,Manifest.permission.VIBRATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if(aBoolean){
                            zv_qrcode.startCamera();
                            zv_qrcode.showScanRect();
                            zv_qrcode.startSpot();
                        }
                    }
                });
    }

    @Override
    protected void initData() {
        super.initData();

    }

    @OnClick({R.id.ll_album,R.id.ll_flash})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_album:
                new TakePhotoHelper().takePhotoAlbum(ZXingScanActivity.this,getTakePhoto(),
                        false,true,1);
                break;
            case R.id.ll_flash:
                if(!isOpenFlashLight){
                    zv_qrcode.openFlashlight();
                    iv_flash_light.setImageResource(R.mipmap.flash_s);
                }else{
                    zv_qrcode.closeFlashlight();
                    iv_flash_light.setImageResource(R.mipmap.flash_n);
                }
                isOpenFlashLight = ! isOpenFlashLight;
                break;
        }
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        //扫描成功回调
        vibrate();//震动
        //zv_qrcode.startSpot();//继续扫码
        RxBus2.getInstance().post(Constant.CODE_QRCODE_RESULT,result);//通知扫描结果
        finish();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        //扫描打开相机出错
        ToastUtil.showToast("打开相机出错");
    }

    /**
     * 初始化设置振动器
     */
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        final String imagePath = result.getImages().get(0).getOriginalPath();
        /*
        这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
            请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考
            https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
             */
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return QRCodeDecoder.syncDecodeQRCode(imagePath);
            }

            @Override
            protected void onPostExecute(String result) {
                if (StringUtil.isEmpty(result)) {
                    ToastUtil.showToast("未发现二维码");
                } else {
                    RxBus2.getInstance().post(Constant.CODE_QRCODE_RESULT,result);//通知扫描结果
                    finish();
                }
            }
        }.execute();
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
    protected void onStart() {
        super.onStart();
        zv_qrcode.startCamera();
        zv_qrcode.showScanRect();
        zv_qrcode.startSpot();
    }

    @Override
    protected void onStop() {
        zv_qrcode.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if(isOpenFlashLight){
            zv_qrcode.closeFlashlight();
        }
        zv_qrcode.onDestroy();
        super.onDestroy();
    }

}
