package com.tenz.hotchpotch.module.main.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.app.AppManager;
import com.tenz.hotchpotch.app.Constant;
import com.tenz.hotchpotch.base.BaseActivity;
import com.tenz.hotchpotch.module.login.activity.LoginActivity;
import com.tenz.hotchpotch.util.ResourceUtil;
import com.tenz.hotchpotch.util.ToastUtil;
import com.ttsea.jrxbus2.RxBus2;
import com.ttsea.jrxbus2.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * Author: TenzLiu
 * Date: 2018-02-23 17:29
 * Description: ZXingActivity
 */

public class ZXingActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.iv_qr_code)
    ImageView iv_qr_code;
    @BindView(R.id.tv_result)
    TextView tv_result;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_zxing;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus2.getInstance().register(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initTitleBar(mToolbar, ResourceUtil.getString(R.string.qr_code));
    }

    @Override
    protected void initData() {
        super.initData();

    }

    @OnClick({R.id.btn_create_ch_qr_code,R.id.btn_create_ch_with_logo_qr_code,
            R.id.btn_create_en_qr_code,R.id.btn_create_en_with_logo_qr_code,
            R.id.btn_scan})
    public void onCLick(View view){
        String codeResuylt = et_content.getText().toString().trim();
        switch (view.getId()){
            case R.id.btn_create_ch_qr_code:
                //生成中文二维码
                if(!"".equals(codeResuylt)){
                    createChineseQRCode(codeResuylt);
                    tv_result.setText(codeResuylt);
                }
                break;
            case R.id.btn_create_ch_with_logo_qr_code:
                //生成中文带logo二维码
                if(!"".equals(codeResuylt)){
                    createChineseQRCodeWithLogo(codeResuylt);
                    tv_result.setText(codeResuylt);
                }
                break;
            case R.id.btn_create_en_qr_code:
                //生成英文二维码
                if(!"".equals(codeResuylt)){
                    createEnglishQRCode(codeResuylt);
                    tv_result.setText(codeResuylt);
                }
                break;
            case R.id.btn_create_en_with_logo_qr_code:
                //生成英文带logo二维码
                if(!"".equals(codeResuylt)){
                    createEnglishQRCodeWithLogo(codeResuylt);
                    tv_result.setText(codeResuylt);
                }
                break;
            case R.id.btn_scan:
                //扫描
                startActivity(ZXingScanActivity.class);
                break;
        }
    }

    /**
     * RxBus 二维码扫描结果接收通知
     * @param msg
     */
    @Subscribe(code = Constant.CODE_QRCODE_RESULT)
    public void onCodeEvent(String msg){
        et_content.setText("");
        createChineseQRCode(msg);
        tv_result.setText(msg);
    }

    /**
     * 生成中文二维码
     */
    private void createChineseQRCode(final String content) {
        /*
        这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
        请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考
         https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
         */
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return QRCodeEncoder.syncEncodeQRCode(content,
                        BGAQRCodeUtil.dp2px(ZXingActivity.this, 160));
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    iv_qr_code.setImageBitmap(bitmap);
                } else {
                    ToastUtil.showToast("生成中文二维码失败");
                }
            }
        }.execute();
    }

    /**
     * 生成英文二维码
     */
    private void createEnglishQRCode(final String content) {
        /*
        这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
        请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考
        https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
         */
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return QRCodeEncoder.syncEncodeQRCode(content,
                        BGAQRCodeUtil.dp2px(ZXingActivity.this, 160),
                        Color.parseColor("#000000"));
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    iv_qr_code.setImageBitmap(bitmap);
                } else {
                    ToastUtil.showToast("生成英文二维码失败");
                }
            }
        }.execute();
    }

    /**
     * 生成中文带logo二维码
     */
    private void createChineseQRCodeWithLogo(final String content) {
        /*
        这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
        请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考
        https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
         */
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap logoBitmap = BitmapFactory.decodeResource(ZXingActivity.this.getResources(),
                        R.mipmap.ic_launcher);
                return QRCodeEncoder.syncEncodeQRCode(content,
                        BGAQRCodeUtil.dp2px(ZXingActivity.this, 160),
                        Color.parseColor("#000000"), logoBitmap);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    iv_qr_code.setImageBitmap(bitmap);
                } else {
                    ToastUtil.showToast("生成带logo的中文二维码失败");
                }
            }
        }.execute();
    }

    /**
     * 生成英文带logo二维码
     */
    private void createEnglishQRCodeWithLogo(final String content) {
        /*
        这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
        请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考
        https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
         */
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap logoBitmap = BitmapFactory.decodeResource(ZXingActivity.this.getResources(),
                        R.mipmap.ic_launcher);
                return QRCodeEncoder.syncEncodeQRCode(content,
                        BGAQRCodeUtil.dp2px(ZXingActivity.this, 160),
                        Color.BLACK, Color.WHITE, logoBitmap);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    iv_qr_code.setImageBitmap(bitmap);
                } else {
                    ToastUtil.showToast("生成带logo的英文二维码失败");
                }
            }
        }.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus2.getInstance().unRegister(this);
    }
}
