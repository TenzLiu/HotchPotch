package com.tenz.hotchpotch.module.main.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.app.Constant;
import com.tenz.hotchpotch.base.BaseActivity;
import com.tenz.hotchpotch.util.ResourceUtil;
import com.tenz.hotchpotch.util.ToastUtil;
import com.tenz.hotchpotch.widget.popuwindow.CommonPopupWindow;
import com.ttsea.jrxbus2.RxBus2;
import com.ttsea.jrxbus2.Subscribe;

import java.lang.ref.WeakReference;

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
    @BindView(R.id.ll_container_qrcode_type)
    LinearLayout ll_container_qrcode_type;
    @BindView(R.id.tv_qrcode_type)
    TextView tv_qrcode_type;

    /**
     * 1:生成中文二维码
     * 2:生成中文带logo二维码
     * 3:生成英文二维码
     * 4:生成英文带logo二维码
     */
    private int createQrCodeType = 1;
    private CommonPopupWindow creaeteQrCodeTypePopupWindow;

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

    @OnClick({R.id.ll_qrcode_type,R.id.btn_create_qr_code,R.id.btn_scan})
    public void onCLick(View view){
        String codeResuylt = et_content.getText().toString().trim();
        switch (view.getId()){
            case R.id.ll_qrcode_type:
                hideKeyBord();
                //生成二维码类型
                creaeteQrCodeTypePopupWindow = new CommonPopupWindow.Builder(mContext)
                        //设置PopupWindow布局
                        .setView(R.layout.layout_popu_qrcode_type)
                        //设置宽高
                        .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT)
                        //设置动画
                        .setAnimationStyle(R.style.AnimDown)
                        //设置背景颜色，取值范围0.0f-1.0f 值越小越暗 1.0f为透明
                        .setBackGroundLevel(0.5f)
                        //设置PopupWindow里的子View及点击事件
                        .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                            @Override
                            public void getChildView(View view, int layoutResId) {
                                TextView tv_create_ch_qr_code = view.findViewById(R.id.tv_create_ch_qr_code);
                                TextView tv_create_ch_with_logo_qr_code = view.findViewById(R.id.tv_create_ch_with_logo_qr_code);
                                TextView tv_create_en_qr_code = view.findViewById(R.id.tv_create_en_qr_code);
                                TextView tv_create_en_with_logo_qr_code = view.findViewById(R.id.tv_create_en_with_logo_qr_code);
                                tv_create_ch_qr_code.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        createQrCodeType = 1;
                                        tv_qrcode_type.setText("生成中文二维码");
                                        creaeteQrCodeTypePopupWindow.dismiss();
                                    }
                                });
                                tv_create_ch_with_logo_qr_code.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        createQrCodeType = 2;
                                        tv_qrcode_type.setText("生成中文带logo二维码");
                                        creaeteQrCodeTypePopupWindow.dismiss();
                                    }
                                });
                                tv_create_en_qr_code.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        createQrCodeType = 3;
                                        tv_qrcode_type.setText("生成英文二维码");
                                        creaeteQrCodeTypePopupWindow.dismiss();
                                    }
                                });
                                tv_create_en_with_logo_qr_code.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        createQrCodeType = 4;
                                        tv_qrcode_type.setText("生成英文带logo二维码");
                                        creaeteQrCodeTypePopupWindow.dismiss();
                                    }
                                });
                            }
                        })
                        //设置外部是否可点击 默认是true
                        .setOutsideTouchable(true)
                        //开始构建
                        .create();
                //弹出PopupWindow
                creaeteQrCodeTypePopupWindow.showAsDropDown(ll_container_qrcode_type);
                break;
            case R.id.btn_create_qr_code:
                //生成二维码
                if("".equals(codeResuylt)){
                    showToast("请输入要生成二维码的内容");
                   return;
                }
                new CreateQRCodeAsyncTask(ZXingActivity.this,codeResuylt,createQrCodeType).execute();
                tv_result.setText(codeResuylt);
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
        new CreateQRCodeAsyncTask(ZXingActivity.this,msg,createQrCodeType).execute();
        tv_result.setText(msg);
    }

    /**
     * 生成二维码异步操作
     */
    private static class CreateQRCodeAsyncTask extends AsyncTask<Void, Void, Bitmap>{

        private WeakReference<ZXingActivity> mZXingActivityWeakReference;
        private String content;
        private int createQrCodeType;

        public CreateQRCodeAsyncTask(ZXingActivity zXingActivity, String content, int createQrCodeType) {
            mZXingActivityWeakReference = new WeakReference<>(zXingActivity);
            this.content = content;
            this.createQrCodeType = createQrCodeType;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            ZXingActivity zXingActivity = mZXingActivityWeakReference.get();
            if(zXingActivity != null){
                if(createQrCodeType == 1){
                    //生成中文二维码
                    return QRCodeEncoder.syncEncodeQRCode(content,
                            BGAQRCodeUtil.dp2px(zXingActivity.mContext, 160));
                }else if(createQrCodeType == 2){
                    //生成中文带logo二维码
                    Bitmap logoBitmap = BitmapFactory.decodeResource(zXingActivity.mContext.getResources(),
                            R.mipmap.ic_launcher);
                    return QRCodeEncoder.syncEncodeQRCode(content,
                            BGAQRCodeUtil.dp2px(zXingActivity.mContext, 160),
                            Color.parseColor("#000000"), logoBitmap);
                }else if(createQrCodeType == 3){
                    //生成英文二维码
                    return QRCodeEncoder.syncEncodeQRCode(content,
                            BGAQRCodeUtil.dp2px(zXingActivity.mContext, 160),
                            Color.parseColor("#000000"));
                }else{
                    //生成英文带logo二维码
                    Bitmap logoBitmap = BitmapFactory.decodeResource(zXingActivity.mContext.getResources(),
                            R.mipmap.ic_launcher);
                    return QRCodeEncoder.syncEncodeQRCode(content,
                            BGAQRCodeUtil.dp2px(zXingActivity.mContext, 160),
                            Color.BLACK, Color.WHITE, logoBitmap);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                ZXingActivity zXingActivity = mZXingActivityWeakReference.get();
                if(zXingActivity != null)
                    zXingActivity.iv_qr_code.setImageBitmap(bitmap);
            } else {
                ToastUtil.showToast("生成二维码失败");
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus2.getInstance().unRegister(this);
    }

}
