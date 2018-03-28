package com.tenz.hotchpotch.app;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;

import com.tenz.hotchpotch.service.ApplicationService;


/**
 * User: Tenz Liu
 * Date: 2017-08-21
 * Time: 11-44
 * Description: AppApplication
 */

public class AppApplication extends Application {

    private static AppApplication sApplication;
    protected static Context sContext;
    protected static Handler sHandler;
    protected static int sMainThreadId;

    @Override
    public void onCreate() {
        super.onCreate();
        //解决android N（>=24）系统以上分享 路径为file://时的 android.os.FileUriExposedException异常
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        sApplication = this;
        sContext = getApplicationContext();
        sHandler = new Handler();
        sMainThreadId = android.os.Process.myTid();
        CrashHandler.getInstance().init(sContext);
        ApplicationService.startService(this);
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
