package com.tenz.hotchpotch.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.tenz.hotchpotch.util.LogUtil;



/**
 * User: Tenz Liu
 * Date: 2017-08-21
 * Time: 11-44
 * Description: AppApplication
 */

public class AppApplication extends Application {

    public static final boolean DEBUG = true;//是否debug模式
    private static AppApplication sApplication;
    protected static Context sContext;
    protected static Handler sHandler;
    protected static int sMainThreadId;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        sContext = getApplicationContext();
        sHandler = new Handler();
        sMainThreadId = android.os.Process.myTid();
        LogUtil.init(DEBUG);
    }

    /**
     * 获取当前对象
     * @return
     */
    public static synchronized AppApplication getInstance(){
        return sApplication;
    }

    /**
     * 获取上下文对象
     * @return
     */
    public static Context getAppContext(){
        return sContext;
    }

    /**
     * 获取全局handler
     * @return
     */
    public static Handler getHandler(){
        return sHandler;
    }

    /**
     * 获取主线程id
     * @return
     */
    public static int getMainThreadId(){
        return sMainThreadId;
    }

}
