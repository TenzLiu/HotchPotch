package com.tenz.hotchpotch.module.home.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.base.BaseActivity;
import com.tenz.hotchpotch.util.SystemShareUtil;
import com.tenz.hotchpotch.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: TenzLiu
 * Date: 2018-03-22 10:35
 * Description: 系统原生分享
 */

public class SystemShareActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private List<String> shareImage = new ArrayList<>();
    private List<String> shareImages = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_system_share;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initTitleBar(mToolbar,"原生分享");
    }

    @Override
    protected void initData() {
        super.initData();
        shareImage.add("https://gd2.alicdn.com/imgextra/i1/2259324182/TB2sdjGm0BopuFjSZPcXXc9EpXa_!!2259324182.jpg");
        shareImages.add("https://gd2.alicdn.com/imgextra/i1/2259324182/TB2sdjGm0BopuFjSZPcXXc9EpXa_!!2259324182.jpg");
        shareImages.add("http://img4.tbcdn.cn/tfscom/i1/2259324182/TB2ISF_hKtTMeFjSZFOXXaTiVXa_!!2259324182.jpg");
    }

    @OnClick({R.id.btn_share_text,R.id.btn_share_image,R.id.btn_share_images})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_share_text:
                share("shareText",null);
                break;
            case R.id.btn_share_image:
                share("shareText",shareImage);
                break;
            case R.id.btn_share_images:
                share("shareText",shareImages);
                break;
        }
    }

    /**
     * 分享
     * @param shareText
     * @param shareImages
     */
    private void share(String shareText, List<String> shareImages) {
        //分享
        NiceDialog.init()
                .setLayoutId(R.layout.layout_dialog_system_share)     //设置dialog布局文件
                .setConvertListener(new ViewConvertListener() {     //进行相关View操作的回调
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        holder.setOnClickListener(R.id.ll_qq, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                //QQ分享
                                if(shareImages == null){
                                    SystemShareUtil.shareText(mContext,shareText,SystemShareUtil.QQPackageName,SystemShareUtil.QQClassName);
                                }else if(shareImages.size() == 1){
                                    SystemShareUtil.shareImage(mContext,shareImages.get(0),SystemShareUtil.QQPackageName,SystemShareUtil.QQClassName);
                                }else{
                                    SystemShareUtil.shareImages(mContext,shareImages,shareText,SystemShareUtil.QQPackageName,SystemShareUtil.QQClassName);
                                }
                            }
                        });
                        holder.setOnClickListener(R.id.ll_qzone, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                //QQ空间分享
                                if(shareImages == null){
                                    SystemShareUtil.shareText(mContext,shareText,SystemShareUtil.QQZonePackageName,SystemShareUtil.QQZoneClassName);
                                }else if(shareImages.size() == 1){
                                    SystemShareUtil.shareImage(mContext,shareImages.get(0),SystemShareUtil.QQZonePackageName,SystemShareUtil.QQZoneClassName);
                                }else{
                                    SystemShareUtil.shareImages(mContext,shareImages,shareText,SystemShareUtil.QQZonePackageName,SystemShareUtil.QQZoneClassName);
                                }
                            }
                        });
                        holder.setOnClickListener(R.id.ll_wechat, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                //微信分享
                                if(shareImages == null){
                                    SystemShareUtil.shareText(mContext,shareText,SystemShareUtil.WeChatPackageName,SystemShareUtil.WeChatSingleClassName);
                                }else if(shareImages.size() == 1){
                                    SystemShareUtil.shareImage(mContext,shareImages.get(0),SystemShareUtil.WeChatPackageName,SystemShareUtil.WeChatSingleClassName);
                                }else{
                                    SystemShareUtil.shareImages(mContext,shareImages,shareText,SystemShareUtil.WeChatPackageName,SystemShareUtil.WeChatSingleClassName);
                                }
                            }
                        });
                        holder.setOnClickListener(R.id.ll_wechat_circle, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                //微信朋友圈分享
                                if(shareImages == null){
                                    ToastUtil.showToast("仅支持分享图片至朋友圈");
                                    return;
                                    //SystemShareUtil.shareText(mContext,shareText,SystemShareUtil.WeChatPackageName,SystemShareUtil.WeChatCircleClassName);
                                }else if(shareImages.size() == 1){
                                    SystemShareUtil.shareImage(mContext,shareImages.get(0),SystemShareUtil.WeChatPackageName,SystemShareUtil.WeChatCircleClassName);
                                }else{
                                    SystemShareUtil.shareImages(mContext,shareImages,shareText,SystemShareUtil.WeChatPackageName,SystemShareUtil.WeChatCircleClassName);
                                }
                            }
                        });
                        holder.setOnClickListener(R.id.ll_sina, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                //新浪微博分享
                                if(shareImages == null){
                                    SystemShareUtil.shareText(mContext,shareText,SystemShareUtil.WeiBoPackageName,SystemShareUtil.WeiBoClassName);
                                }else if(shareImages.size() == 1){
                                    SystemShareUtil.shareImage(mContext,shareImages.get(0),SystemShareUtil.WeiBoPackageName,SystemShareUtil.WeiBoClassName);
                                }else{
                                    SystemShareUtil.shareImages(mContext,shareImages,shareText,SystemShareUtil.WeiBoPackageName,SystemShareUtil.WeiBoClassName);
                                }
                            }
                        });
                        holder.setOnClickListener(R.id.ll_system, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                //系统分享
                                if(shareImages == null){
                                    SystemShareUtil.shareText(mContext,shareText,"","");
                                }else if(shareImages.size() == 1){
                                    SystemShareUtil.shareImage(mContext,shareImages.get(0),"","");
                                }else{
                                    SystemShareUtil.shareImages(mContext,shareImages,shareText,"","");
                                }
                            }
                        });
                    }
                })
                .setShowBottom(true)
                //.setMargin(30)     //dialog左右两边到屏幕边缘的距离（单位：dp），默认0dp
                .show(getSupportFragmentManager());     //显示dialog
    }

}
