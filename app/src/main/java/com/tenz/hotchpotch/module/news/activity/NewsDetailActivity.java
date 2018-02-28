package com.tenz.hotchpotch.module.news.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.base.BaseActivity;
import com.tenz.hotchpotch.util.JShareUtil;
import com.tenz.hotchpotch.util.LogUtil;
import com.tenz.hotchpotch.util.StringUtil;

import butterknife.BindView;
import cn.jiguang.share.qqmodel.QQ;
import cn.jiguang.share.wechat.Wechat;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * Author: TenzLiu
 * Date: 2018-01-29 11:50
 * Description: NewsDetailActivity
 */

public class NewsDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.pb_news_detail)
    ProgressBar pb_news_detail;
    @BindView(R.id.wv_news_detail)
    WebView wv_news_detail;

    private String url;
    private String htmlData;
    private String title, content, imageUrl;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initWebSetting(wv_news_detail.getSettings());
    }

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            url = bundle.getString("url");
            htmlData = bundle.getString("htmlData");
            title = bundle.getString("title");
            content = bundle.getString("content");
            imageUrl = bundle.getString("imageUrl");
        }
        if(!StringUtil.isEmpty(url)){
            wv_news_detail.loadUrl(url);
        }else{
            wv_news_detail.loadData(htmlData, "text/html; charset=UTF-8", null);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebSetting(WebSettings webSettings){
        webSettings.setJavaScriptEnabled(true);//支持js

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true);  //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(false); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局
        webSettings.setAppCacheEnabled(true); //启用应用缓存
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//优先使用缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setDomStorageEnabled(true);//启用或禁用DOM缓存
        webSettings.setBlockNetworkImage(false);
        webSettings.setSupportMultipleWindows(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            //sdk>5.0
            //https协议在使用的时候需要申请一个安全证书
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //辅助 WebView 处理 Javascript 的对话框,网站图标,网站标题等等
        wv_news_detail.setWebChromeClient(new WebChromeClient());
        //处理各种通知 & 请求事件
        wv_news_detail.setWebViewClient(new WebViewClient());

    }

    /**
     * WebChromeClient
     */
    public class WebChromeClient extends android.webkit.WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if(pb_news_detail == null)
                return;
            //进度条
            if(newProgress < 100){
                pb_news_detail.setProgress(newProgress);
            }else{
                pb_news_detail.setVisibility(View.GONE);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            //标题
            initTitleBar(mToolbar, title, true, 1);
        }
    }

    /**
     * WebViewClient
     */
    public class WebViewClient extends android.webkit.WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器，而是在本WebView中显示
            wv_news_detail.loadUrl(url);
            return true;
        }
    }

    @Override
    protected void onSubTitleClick() {
        super.onSubTitleClick();
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
                                JShareUtil.share(QQ.Name,title,content,url,imageUrl,"");
                            }
                        });
                        holder.setOnClickListener(R.id.ll_wechat, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                //微信分享
                                JShareUtil.share(Wechat.Name,title,content,url,imageUrl,"");
                            }
                        });
                    }
                })
                .setShowBottom(true)
                //.setMargin(30)     //dialog左右两边到屏幕边缘的距离（单位：dp），默认0dp
                .show(getSupportFragmentManager());     //显示dialog
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(wv_news_detail != null){
            wv_news_detail.removeAllViews();
            wv_news_detail.destroy();
            wv_news_detail = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KEYCODE_BACK && wv_news_detail.canGoBack()){
            wv_news_detail.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
