package com.tenz.hotchpotch.util;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Author: TenzLiu
 * Date: 2018-01-18 16:11
 * Description: resource资源工具类
 */

public class ResourceUtil {

    /**
     * 获取strings.xml资源文件字符串
     * @param id 资源文件id
     * @return 资源文件对应字符串
     */
    public static String getString(int id){
        return AppUtil.getContext().getResources().getString(id);
    }

    /**
     * 获取strings.xml资源文件字符串数组
     * @param id 资源文件id
     * @return 资源文件对应字符串数组
     */
    public static String[] getStringArray(int id){
        return AppUtil.getContext().getResources().getStringArray(id);
    }

    /**
     * 获取drawable资源文件图片
     * @param id 资源文件id
     * @return 资源文件对应图片
     */
    public static Drawable getDrawable(int id){
        return AppUtil.getContext().getResources().getDrawable(id);
    }

    /**
     * 获取colors.xml资源文件颜色
     * @param id 资源文件id
     * @return 资源文件对应颜色值
     */
    public static int getColor(int id){
        return AppUtil.getContext().getResources().getColor(id);
    }

    /**
     * 获取dimens资源文件中具体像素值
     * @param id 资源文件id
     * @return 资源文件对应像素值
     */
    public static int getDemen(int id){
        return AppUtil.getContext().getResources().getDimensionPixelSize(id);
    }

    /**
     * 加载布局文件
     * @param id 布局文件id
     * @return 布局view
     */
    public static View inflate(int id){
        return View.inflate(AppUtil.getContext(),id,null);
    }

}
