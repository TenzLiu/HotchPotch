package com.tenz.hotchpotch.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.tenz.hotchpotch.util.LogUtil;

import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatformConfig;
import cn.jpush.android.api.JPushInterface;

/**
 * Author: TenzLiu
 * Date: 2018-03-05 10:18
 * Description: ApplicationService App冷启动缓慢出现白屏方案
 */

public class ApplicationService extends IntentService {


    public static final boolean DEBUG = true;//是否debug模式
    private static final String ACTION_INIT_WHEN_APP_CREATE = "com.tenz.hotchpotch.service.action.INIT";

    public ApplicationService() {
        super("ApplicationService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ApplicationService(String name) {
        super(name);
    }

    /**
     * 启动
     * @param context
     */
    public static void startService(Context context){
        Intent intent = new Intent(context,ApplicationService.class);
        intent.setAction(ACTION_INIT_WHEN_APP_CREATE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            String intentAction = intent.getAction();
            if(ACTION_INIT_WHEN_APP_CREATE.equals(intentAction)){
                //将application好事操作放在此处
                LogUtil.init(DEBUG);
                //极光初始化
                JPushInterface.setDebugMode(true);
                JPushInterface.init(this);
                JShareInterface.setDebugMode(true);
                PlatformConfig platformConfig = new PlatformConfig()
                        .setWechat("wxc40e16f3ba6ebabc", "dcad950cd0633a27e353477c4ec12e7a")
                        .setQQ("1106671627", "IGabM3RBSNz4bb4N");
                JShareInterface.init(this,platformConfig);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d("onCreate..........");
    }

    @Override
    public void onDestroy() {
        LogUtil.d("onDestroy..........");
        super.onDestroy();
    }
}
