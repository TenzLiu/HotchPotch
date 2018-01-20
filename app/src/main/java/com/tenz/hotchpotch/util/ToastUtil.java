package com.tenz.hotchpotch.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Author: TenzLiu
 * Date: 2018-01-18 15:24
 * Description: Toast工具类
 */

public class ToastUtil {

    private static Toast sToast = null;

    /**
     * 显示Toast
     * @param resourceId
     */
    public static void showToast(int resourceId){
        showToast(ResourceUtil.getString(resourceId));
    }

    /**
     * 显示Toast
     * @param message
     */
    public static void showToast(String message){
        showToast(message,Toast.LENGTH_SHORT);
    }

    /**
     * 显示Toast
     * @param message
     * @param duration
     */
    public static void showToast(String message, int duration){
        showToast(AppUtil.getContext(),message,duration);
    }

    /**
     * 显示Toast,保证在主线程显示
     * @param context
     * @param message
     * @param duration
     */
    public static void showToast(final Context context, final String message, final int duration){
        AppUtil.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if(context == null || StringUtil.isEmpty(message)){
                    return;
                }
                if(sToast == null){
                    sToast = Toast.makeText(context, message, duration);
                }else{
                    sToast.setText(message);
                    sToast.setDuration(duration);
                }
                sToast.show();
            }
        });

    }

}
