package com.tenz.hotchpotch.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Author: TenzLiu
 * Date: 2018-03-22 11:45
 * Description: 系統原生分享工具类
 * public static String WeiBoPackageName = "com.sina.weibo";
   public static String WeiBoClassName = "com.sina.weibo.composerinde.ComposerDispatchActivity";
   public static String WeChatPackageName = "com.tencent.mm";
   public static String WeChatCircleClassName = "com.tencent.mm.ui.tools.ShareToTimeLineUI";
   public static String WeChatSingleClassName = "com.tencent.mm.ui.tools.ShareImgUI";
   public static String QQPackageName = "com.tencent.mobileqq";
   public static String QQClassName = "com.tencent.mobileqq.activity.JumpActivity";
   public static String QQZonePackageName = "com.qzone";
   public static String QQZoneClassName = "com.qzonex.module.operation.ui.QZonePublishMoodActivity";
 */

public class SystemShareUtil {

    public static String IMAGE_NAME = "iv_share_";
    public static int IMAGE_INDEX = 0;
    public static String WeiBoPackageName = "com.sina.weibo";
    public static String WeiBoClassName = "com.sina.weibo.composerinde.ComposerDispatchActivity";
    public static String WeChatPackageName = "com.tencent.mm";
    public static String WeChatCircleClassName = "com.tencent.mm.ui.tools.ShareToTimeLineUI";
    public static String WeChatSingleClassName = "com.tencent.mm.ui.tools.ShareImgUI";
    public static String QQPackageName = "com.tencent.mobileqq";
    public static String QQClassName = "com.tencent.mobileqq.activity.JumpActivity";
    public static String QQZonePackageName = "com.qzone";
    public static String QQZoneClassName = "com.qzonex.module.operation.ui.QZonePublishMoodActivity";

    /**
     * 分享纯文本
     * @param context
     * @param shareText
     * @param packageName
     * @param className
     */
    public static void shareText(Context context, String shareText, String packageName, String className){
        if(QQPackageName.equals(packageName)){
            if(!isInstallQQ(context)){
                ToastUtil.showToast("请安装QQ");
                return;
            }
        }else if(QQZonePackageName.equals(packageName)){
            if(!isInstallQQZone(context)){
                ToastUtil.showToast("请安装QQ空间");
                return;
            }
        }else if(WeChatPackageName.equals(packageName)){
            if(!isInstallWechat(context)){
                ToastUtil.showToast("请安装微信");
                return;
            }
        }else if(WeiBoPackageName.equals(packageName)){
            if(!isInstallSina(context)){
                ToastUtil.showToast("请安装新浪微博");
                return;
            }
        }
        Intent shareIntent = new Intent();
        if(!StringUtil.isEmpty(packageName) && !StringUtil.isEmpty(className)){
            ComponentName comp = new ComponentName(packageName, className);
            shareIntent .setComponent(comp);
        }
        shareIntent .setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT,shareText);//分享文本格式内容
        shareIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(shareIntent ,"分享"));
    }

    /**
     * 分享纯文本
     * @param context
     * @param imageUrl
     * @param packageName
     * @param className
     */
    public static void shareImage(Context context, String imageUrl, String packageName, String className){
        if(QQPackageName.equals(packageName)){
            if(!isInstallQQ(context)){
                ToastUtil.showToast("请安装QQ");
                return;
            }
        }else if(QQZonePackageName.equals(packageName)){
            if(!isInstallQQZone(context)){
                ToastUtil.showToast("请安装QQ空间");
                return;
            }
        }else if(WeChatPackageName.equals(packageName)){
            if(!isInstallWechat(context)){
                ToastUtil.showToast("请安装微信");
                return;
            }
        }else if(WeiBoPackageName.equals(packageName)){
            if(!isInstallSina(context)){
                ToastUtil.showToast("请安装新浪微博");
                return;
            }
        }
        ToastUtil.showToast("启动分享，请稍后...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                File imageFile = saveImageToSdCard(context, imageUrl);
                Intent shareIntent = new Intent();
                if(!StringUtil.isEmpty(packageName) && !StringUtil.isEmpty(className)){
                    ComponentName comp = new ComponentName(packageName, className);
                    shareIntent .setComponent(comp);
                }
                shareIntent .setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(imageFile));//分享图片格式内容
                shareIntent.setType("image/*");
                context.startActivity(Intent.createChooser(shareIntent ,"分享"));
            }
        }).start();
    }

    /**
     * 分享纯文本
     * @param context
     * @param imageUrls
     * @param shareText
     * @param packageName
     * @param className
     */
    public static void shareImages(Context context, List<String> imageUrls, String shareText, String packageName, String className){
        if(QQPackageName.equals(packageName)){
            if(!isInstallQQ(context)){
                ToastUtil.showToast("请安装QQ");
                return;
            }
        }else if(QQZonePackageName.equals(packageName)){
            if(!isInstallQQZone(context)){
                ToastUtil.showToast("请安装QQ空间");
                return;
            }
        }else if(WeChatPackageName.equals(packageName)){
            if(!isInstallWechat(context)){
                ToastUtil.showToast("请安装微信");
                return;
            }
        }else if(WeiBoPackageName.equals(packageName)){
            if(!isInstallSina(context)){
                ToastUtil.showToast("请安装新浪微博");
                return;
            }
        }
        ToastUtil.showToast("启动分享，请稍后...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<File> files = new ArrayList();
                ArrayList<Uri> imageUris = new ArrayList();
                for(int i = 0; i < imageUrls.size(); ++i) {
                    File file = saveImageToSdCard(context, imageUrls.get(i));
                    files.add(file);
                }
                Iterator iterator = files.iterator();
                while(iterator.hasNext()) {
                    File f = (File)iterator.next();
                    imageUris.add(Uri.fromFile(f));
                }
                Intent shareIntent = new Intent();
                if(!StringUtil.isEmpty(packageName) && !StringUtil.isEmpty(className)){
                    ComponentName comp = new ComponentName(packageName, className);
                    shareIntent .setComponent(comp);
                }
                shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                shareIntent.putParcelableArrayListExtra("android.intent.extra.STREAM", imageUris);
                shareIntent.setType("image/*");
                shareIntent.putExtra("Kdescription", shareText);
                context.startActivity(shareIntent);
            }
        }).start();
    }

    /**
     * 图片保存本地
     * @param context
     * @param image
     * @return
     */
    public static final File saveImageToSdCard(Context context, String image) {
        boolean success = false;
        File file = null;
        try {
            file = createStableImageFile(context);
            Bitmap bitmap = null;
            URL url = new URL(image);
            HttpURLConnection conn = null;
            conn = (HttpURLConnection)url.openConnection();
            InputStream is = null;
            is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            FileOutputStream outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
            success = true;
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        return success?file:null;
    }

    /**
     * 创建文件file
     * @param context
     * @return
     * @throws IOException
     */
    public static File createStableImageFile(Context context) throws IOException {
        ++IMAGE_INDEX;
        String imageFileName = IMAGE_NAME + IMAGE_INDEX + ".jpg";
        File storageDir = context.getExternalCacheDir();
        File image = new File(storageDir, imageFileName);
        return image;
    }

    /**
     * 是否安装微信
     * @param context
     * @return
     */
    public static boolean isInstallWechat(Context context){
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if(pinfo != null) {
            for(int i = 0; i < pinfo.size(); ++i) {
                String pn = pinfo.get(i).packageName;
                if(pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否安装QQ
     * @param context
     * @return
     */
    public static boolean isInstallQQ(Context context){
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否安装QQ空間
     * @param context
     * @return
     */
    public static boolean isInstallQQZone(Context context){
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.qzone")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否安装新浪微博
     * @param context
     * @return
     */
    public static boolean isInstallSina(Context context){
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.sina.weibo")) {
                    return true;
                }
            }
        }
        return false;
    }

}
