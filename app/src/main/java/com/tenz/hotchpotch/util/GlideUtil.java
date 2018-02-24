package com.tenz.hotchpotch.util;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.tenz.hotchpotch.R;

/**
 * Author: TenzLiu
 * Date: 2018-02-01 09:54
 * Description: GlideUtil
 */

public class GlideUtil {

    /**
     * 默认RequestOptions
     */
    private static RequestOptions mRequestOptions = new RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.mipmap.default_icon)
            .error(R.mipmap.default_icon);
    private static DrawableTransitionOptions mDrawableTransitionOptions = new DrawableTransitionOptions()
            .crossFade(800);

    /**
     * 默认加载图片
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImage(Context context, Object url, ImageView imageView){
        Glide.with(context)
                .load(url)
                .apply(mRequestOptions)
                .into(imageView);
    }

    /**
     * 默认加载图片
     * @param fragment
     * @param url
     * @param imageView
     */
    public static void loadImage(Fragment fragment, Object url, ImageView imageView){
        Glide.with(fragment)
                .load(url)
                .apply(mRequestOptions)
                .into(imageView);
    }

    /**
     * 自定义默认图片
     * @param context
     * @param url
     * @param imageView
     * @param resId
     */
    public static void loadImage(Context context, Object url, int resId, ImageView imageView){
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(resId))
                .into(imageView);
    }

    /**
     * 自定义默认图片
     * @param fragment
     * @param url
     * @param imageView
     * @param resId
     */
    public static void loadImage(Fragment fragment, Object url, int resId, ImageView imageView){
        Glide.with(fragment)
                .load(url)
                .apply(new RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(resId))
                .into(imageView);
    }

    /**
     * 自定义RequestOptions
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImage(Context context, Object url, RequestOptions requestOptions, ImageView imageView){
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(imageView);
    }

    /**
     * 自定义RequestOptions
     * @param fragment
     * @param url
     * @param imageView
     */
    public static void loadImage(Fragment fragment, Object url, RequestOptions requestOptions, ImageView imageView){
        Glide.with(fragment)
                .load(url)
                .apply(requestOptions)
                .into(imageView);
    }

    /**
     * 加载图片回调
     * @param context
     * @param url
     * @param imageViewTarget
     */
    public static void loadImage(Context context, Object url, ImageViewTarget imageViewTarget){
        Glide.with(context)
                .load(url)
                .apply(mRequestOptions)
                .into(imageViewTarget);
    }

    /**
     * 加载图片回调
     * @param fragment
     * @param url
     * @param imageViewTarget
     */
    public static void loadImage(Fragment fragment, Object url, ImageViewTarget imageViewTarget){
        Glide.with(fragment)
                .load(url)
                .apply(mRequestOptions)
                .into(imageViewTarget);
    }

    /**
     * 加载图片回调
     * @param context
     * @param url
     * @param requestListener
     * @param imageView
     */
    public static void loadImage(Context context, Object url, RequestListener requestListener, ImageView imageView){
        Glide.with(context)
                .load(url)
                .apply(mRequestOptions)
                .listener(requestListener)
                .into(imageView);
    }

    /**
     * 加载图片回调
     * @param fragment
     * @param url
     * @param requestListener
     * @param imageView
     */
    public static void loadImage(Fragment fragment, Object url, RequestListener requestListener, ImageView imageView){
        Glide.with(fragment)
                .load(url)
                .apply(mRequestOptions)
                .listener(requestListener)
                .into(imageView);
    }

}
