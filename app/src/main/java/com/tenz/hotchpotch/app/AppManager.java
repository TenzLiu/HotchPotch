package com.tenz.hotchpotch.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.tenz.hotchpotch.util.LogUtil;

import java.util.Stack;

/**
 * Author: TenzLiu
 * Date: 2018-01-18 16:58
 * Description: AppManager管理工具类
 */

public class AppManager {

    private static Stack<Activity> sActivityStack;
    private static AppManager sAppManager;

    /**
     * 私有化
     */
    private AppManager() {
    }

    /**
     * 单例
     * @return
     */
    public static AppManager getInstance(){
        if(sAppManager == null){
            synchronized (AppManager.class){
                if(sAppManager == null){
                    sAppManager = new AppManager();
                }
            }
        }
        return sAppManager;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity){
        if(sActivityStack == null){
            sActivityStack = new Stack<>();
        }
        sActivityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity getCurrentActivity(){
        return sActivityStack.lastElement();
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity){
        if(activity != null && sActivityStack !=null){
            sActivityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity(){
        finishActivity(sActivityStack.lastElement());
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls){
        for(Activity activity : sActivityStack){
            if(activity.getClass().equals(cls)){
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity除了指定的activity
     */
    public void finishAllActivityExcept(Class<?> cls){
        for(int i=0; i<sActivityStack.size(); i++){
            if(null != sActivityStack.get(i)){
                if(!sActivityStack.get(i).getClass().equals(cls)){
                    sActivityStack.get(i).finish();
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity(){
        for(int i=0; i<sActivityStack.size(); i++){
            if(null != sActivityStack.get(i)){
                sActivityStack.get(i).finish();
            }
        }
        sActivityStack.clear();
    }

    /**
     * 返回到指定的activity
     *
     * @param cls
     */
    public void returnToActivity(Class<?> cls) {
        while (sActivityStack.size() != 0)
            if (sActivityStack.peek().getClass() == cls) {
                break;
            } else {
                finishActivity(sActivityStack.peek());
            }
    }


    /**
     * 是否已经打开指定的activity
     * @param cls
     * @return
     */
    public boolean isOpenActivity(Class<?> cls) {
        if (sActivityStack!=null){
            for (int i = 0, size = sActivityStack.size(); i < size; i++) {
                if (cls == sActivityStack.peek().getClass()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 退出应用程序
     * @param context      上下文
     * @param isBackground 是否有后台运行
     */
    public void exitApp(Context context, boolean isBackground){
        finishAllActivity();
        // 注意，如果您有后台程序运行，请不要支持此句子
        if (!isBackground) {
            try{
                ActivityManager activityManager = (ActivityManager) context
                        .getSystemService(Context.ACTIVITY_SERVICE);
                activityManager.killBackgroundProcesses(context.getPackageName());
                System.exit(0);
            }catch (Exception e){
                LogUtil.e(e);
            }finally {

            }
        }
    }

}
