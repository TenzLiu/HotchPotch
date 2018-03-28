package com.tenz.hotchpotch.module.home.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.othershe.nicedialog.BaseNiceDialog;
import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.base.BaseActivity;
import com.tenz.hotchpotch.util.LogUtil;
import com.tenz.hotchpotch.widget.dialog.ConfirmDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: TenzLiu
 * Date: 2018-03-27 01:21
 * Description: Android与webView的交互
 */

public class JSWebViewActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.wv_jswebview)
    WebView wv_jswebview;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_js_webview;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initTitleBar(mToolbar,"Android与webview交互");
        WebSettings settings = wv_jswebview.getSettings();
        //允许js
        settings.setJavaScriptEnabled(true);
        //允许js弹框
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        wv_jswebview.setWebChromeClient(mWebChromeClient);
        wv_jswebview.addJavascriptInterface(new AndroidtoJs(),"test");



    }

    @Override
    protected void initData() {
        super.initData();
        ///载入JS代码
        // 格式规定为:file:///android_asset/文件名.html
        wv_jswebview.loadUrl("file:///android_asset/jswebview.html");
    }

    @OnClick({R.id.btn_call_webview})
    public void onCLick(View view){
        switch (view.getId()){
            //Android调用JS方法
            case R.id.btn_call_webview:
                // Android版本变量
                int version = Build.VERSION.SDK_INT;
                // 因为该方法在 Android 4.4 版本才可使用，所以使用时需进行版本判断
                if (version < 18) {
                    wv_jswebview.post(new Runnable() {
                        @Override
                        public void run() {
                            // 注意调用的JS方法名要对应上
                            // 调用javascript的callJS()方法
                            wv_jswebview.loadUrl("javascript:callJS()");
                        }
                    });
                } else {
                    wv_jswebview.evaluateJavascript("javascript:callJS()", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            //返回值
                            LogUtil.d("value:"+value);
                        }
                    });
                }
                break;
        }
    }

    // 由于设置了弹窗检验调用结果,所以需要支持js对话框
    // webview只是载体，内容的渲染需要使用webviewChromClient类去实现
    // 通过设置WebChromeClient对象处理JavaScript的对话框
    //设置响应js 的Alert()函数
    WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            ConfirmDialog.show(getSupportFragmentManager(), "提示", message,"",
                    "确定",new ConfirmDialog.ConfirmDialogListener(){

                        @Override
                        public void onNegative(BaseNiceDialog dialog) {
                            dialog.dismiss();
                        }

                        @Override
                        public void onPositive(BaseNiceDialog dialog) {
                            dialog.dismiss();
                            result.confirm();
                        }
                    });
            return true;
        }
    };

    // 继承自Object类
    public class AndroidtoJs extends Object{

        // 定义JS需要调用的方法
        // 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        public void callAndroid(String message){
            showToast(message);
        }

    }

}
