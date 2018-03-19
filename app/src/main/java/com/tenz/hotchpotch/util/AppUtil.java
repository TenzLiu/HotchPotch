package com.tenz.hotchpotch.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.tenz.hotchpotch.app.AppApplication;

import java.io.File;
import java.util.List;

/**
 * Author: TenzLiu
 * Date: 2018-01-18 14:40
 * Description: App工具类
 */

public class AppUtil {

    /**
     * 获取全局handler
     * @return
     */
    public static Handler getMainHandler(){
        return AppApplication.getHandler();
    }

    /**
     * 获取UI主线程id
     * @return
     */
    public static int getMainThreadId(){
        return AppApplication.getMainThreadId();
    }

    /**
     * 是否运行在UI主线程
     * @return
     */
    public static boolean isRunOnUIThread(){
        int myTid = android.os.Process.myTid();
        if(myTid == getMainThreadId()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 运行在UI主线程
     * @param runnable
     */
    public static void runOnUIThread(Runnable runnable){
        if(isRunOnUIThread()){
            // 已经是主线程, 直接运行
            runnable.run();
        }else{
            // 如果是子线程, 借助handler让其运行在主线程
            runOnUIThread(runnable);
        }
    }

    /**
     * 获取上下文对象
     * @return
     */
    public static Context getContext(){
        return AppApplication.getAppContext();
    }

    /**
     * 判断App是否在前台运行
     * @param context
     * @return
     */
    public static boolean isAppRunningForegroud(Context context){
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager
                .getRunningAppProcesses();
        if(runningAppProcesses == null){
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo runningAppProcess:runningAppProcesses) {
            if(runningAppProcess.processName.equals(context.getPackageName()) &&
                    runningAppProcess.importance == ActivityManager
                            .RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                return true;
            }
        }
        return false;
    }

    /**
     * 获取版本名称
     * @return
     */
    public static String getAppVersionName(Context context){
        String versionName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager
                    .getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
            if(versionName == null || versionName.length() <= 0){
                return "";
            }
        }catch (Exception e){
            LogUtil.d("Exception:---"+e);
        }
        return versionName;
    }

    /**
     * 获取版本号
     * @param context
     * @return
     */
    public static int getVersionCode(Context context){
        int versionCode = -1;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager
                    .getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        }catch (Exception e){
            LogUtil.d("Exception:---"+e);
        }
        return versionCode;
    }

    /**
     * 获取手机IMEI号
     * @param context
     * @return
     */
    public static String getIMEI(Context context){
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getImei();
    }

    /**
     * 获取手机IMSI号
     * @param context
     * @return
     */
    public static String getIMSI(Context context){
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getSubscriberId();
    }

    /**
     * 判断手机是否安装SDCard
     * @return
     */
    public static boolean isHasSDCard(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取手机SDCard路径
     * @return
     */
    public static String getSDCardPath(){
        File sdDir = null;
        if(isHasSDCard()){
            sdDir = Environment.getExternalStorageDirectory().getAbsoluteFile();
        }
        if(sdDir != null){
            return sdDir.getPath();
        }
        return "";
    }

    /**
     * 显示软键盘
     * @param editText
     */
    public static void showSoftInput(EditText editText){
        InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText,InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏软键盘
     * @param editText
     */
    public static void hideSoftInput(EditText editText){
        InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken()
                ,InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     *
     * @param context
     * @param phone
     * @param immediate  true:直接拨打  false:拨号界面
     */
    public static void callPhone(Context context, String phone, boolean immediate){
        Intent intent;
        if(immediate){
            intent = new Intent(Intent.ACTION_CALL);
        }else{
            intent = new Intent(Intent.ACTION_DIAL);
        }
        Uri data = Uri.parse("tel:"+phone);
        intent.setData(data);
        context.startActivity(intent);
    }

}
