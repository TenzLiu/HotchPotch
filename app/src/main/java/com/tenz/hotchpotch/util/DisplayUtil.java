package com.tenz.hotchpotch.util;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.tenz.hotchpotch.app.AppApplication;

/**
 * User: Tenz Liu
 * Date: 2017-08-23
 * Time: 20-36
 * Description: 显示器工具类
 */

public class DisplayUtil {

    public static Context mContext = AppApplication.getInstance();

    /**
     * px转dp
     * @param value
     * @return
     */
    public static int px2dp(int value){
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (value / scale + 0.5f);
    }

    /**
     * dp转px
     * @param value
     * @return
     */
    public static int dp2px(int value){
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f );
    }

    /**
     * px转sp
     * @param value
     * @return
     */
    public static int px2sp(int value){
        float scale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (value / scale + 0.5f);
    }

    /**
     * sp转px
     * @param value
     * @return
     */
    private static int sp2px(int value){
        float scale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (value * scale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     * @return
     */
    public static int getWindowWidth(){
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        return widthPixels;
    }

    /**
     * 获取屏幕高度
     * @return
     */
    public static int getWindowHeight(){
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int heightPixels = displayMetrics.heightPixels;
        return heightPixels;
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity
     * @return bp
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        if (bmp == null) {
            return null;
        }
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Bitmap bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, bmp.getWidth(), bmp.getHeight()
                - statusBarHeight);
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(false);

        return bp;
    }

    /**
     * 获取actionbar的像素高度，默认使用android官方兼容包做actionbar兼容
     *
     * @return
     */
    public static int getActionBarHeight(Context context) {
        int actionBarHeight = 0;
        if (context instanceof AppCompatActivity && ((AppCompatActivity) context)
                .getSupportActionBar() != null) {
            Log.d("isAppCompatActivity", "==AppCompatActivity");
            actionBarHeight = ((AppCompatActivity) context).getSupportActionBar().getHeight();
        } else if (context instanceof Activity && ((Activity) context).getActionBar() != null) {
            Log.d("isActivity", "==Activity");
            actionBarHeight = ((Activity) context).getActionBar().getHeight();
        } else if (context instanceof ActivityGroup) {
            Log.d("ActivityGroup", "==ActivityGroup");
            if (((ActivityGroup) context).getCurrentActivity() instanceof AppCompatActivity && (
                    (AppCompatActivity) ((ActivityGroup) context).getCurrentActivity())
                    .getSupportActionBar() != null) {
                actionBarHeight = ((AppCompatActivity) ((ActivityGroup) context)
                        .getCurrentActivity()).getSupportActionBar().getHeight();
            } else if (((ActivityGroup) context).getCurrentActivity() instanceof Activity && (
                    (Activity) ((ActivityGroup) context).getCurrentActivity()).getActionBar() !=
                    null) {
                actionBarHeight = ((Activity) ((ActivityGroup) context).getCurrentActivity())
                        .getActionBar().getHeight();
            }
        }
        if (actionBarHeight != 0)
            return actionBarHeight;
        final TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.support.v7.appcompat.R.attr
                .actionBarSize, tv, true)) {
            if (context.getTheme().resolveAttribute(android.support.v7.appcompat.R.attr
                    .actionBarSize, tv, true))
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context
                        .getResources().getDisplayMetrics());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context
                        .getResources().getDisplayMetrics());
        } else {
            if (context.getTheme().resolveAttribute(android.support.v7.appcompat.R.attr
                    .actionBarSize, tv, true))
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context
                        .getResources().getDisplayMetrics());
        }
        Log.d("actionBarHeight", "====" + actionBarHeight);
        return actionBarHeight;
    }


    /**
     * 设置view margin
     *
     * @param v
     * @param l
     * @param t
     * @param r
     * @param b
     */
    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

}
