package com.tenz.hotchpotch.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.util.AppUtil;
import com.tenz.hotchpotch.util.LogUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.File;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Author: TenzLiu
 * Date: 2017-11-20 11:55
 * Description: App更新服务
 */

public class AppUpdateService extends Service {

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private OnProgressListener mProgressListener;
    private String downLoadApkUrl;
    private String downApkPath;// 保存apk的文件夹
    private String downLoadApkName; //保存的apk名称
    private int currentProgress;//当前下载进度
    private boolean isCancleDownload;
    private RequestCall downLoadRequestCall;

    public interface OnProgressListener {
        void onProgress(int progress);

        void onSuccess(boolean isSuccess);
    }

    public void setmProgressListener(OnProgressListener progressListener) {
        mProgressListener = progressListener;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        downApkPath = AppUtil.getSDCardPath()+"/hotchpotch/";
        File downApkPathFile = new File(downApkPath);
        if(!downApkPathFile.exists()){
            downApkPathFile.mkdir();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 开始下载
     */
    private void startDownload() {
        initNotificationView();
        mNotificationManager.notify(0,mBuilder.build());
        downLoadRequestCall = OkHttpUtils//
                .get()//
                .url(downLoadApkUrl)//
                .build();//
        downLoadRequestCall.execute(new FileCallBack(downApkPath, downLoadApkName)//
                {

                    @Override
                    public void onBefore(Request request, int id){
                    }

                    @Override
                    public void inProgress(float progress, long total, int id){
                        LogUtil.d("inProgress :" + (int) (100 * progress));
                        if(currentProgress!=(int) (100 * progress)){
                            currentProgress = (int) (100 * progress);
                            if(!isCancleDownload && currentProgress%5==0){
                                mProgressListener.onProgress(currentProgress);
                                mBuilder.setContentText("正在下载("+currentProgress+"%)").setProgress(100,currentProgress,false);
                                mNotificationManager.notify(0,mBuilder.build());
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id){
                        LogUtil.d("onError :" + e.getMessage());
                        if(isCancleDownload){

                        }else{
                            mProgressListener.onSuccess(false);
                            mNotificationManager.cancel(0);
                        }
                    }

                    @Override
                    public void onResponse(File file, int id){
                        LogUtil.d("onResponse :" + file.getAbsolutePath());
                        if(isCancleDownload){

                        }else if(!isCancleDownload&&currentProgress==100&&null!=file){
                            mProgressListener.onSuccess(true);
                            mNotificationManager.cancel(0);
                            installApk(file);
                        }else{
                            mProgressListener.onSuccess(false);
                            mNotificationManager.cancel(0);
                        }
                    }
                });
    }

    /**
     * 安卓apk
     * @param file
     */
    private void installApk(File file) {
        Uri uri = null;
        if (!file.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        LogUtil.d(file.getAbsolutePath());
        if(Build.VERSION.SDK_INT>=24){
            uri = FileProvider.getUriForFile(this,getApplicationContext().getPackageName() + ".provider", file);
        }else{
            uri = Uri.fromFile(file);
        }
        i.setDataAndType(uri,"application/vnd.android.package-archive");// File.toString()会返回路径信息
        //关键点：
        //安装完成后执行打开
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }

    /**
     * 初始化状态栏进度条
     */
    private void initNotificationView() {
        try {
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(this);
            Intent intent = new Intent(this, RemoteViews.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
            mBuilder.setSmallIcon(R.mipmap.logo)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.logo))
                    .setContentTitle("版本更新")
                    .setContentText("正在下载(0%)")
                    .setAutoCancel(true)
                    .setProgress(100,0,false)
                    .setContentIntent(pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public class MyBinder extends Binder {

        /**
         * 获取当前Service的实例
         * @return
         */
        public AppUpdateService getService(){
            return AppUpdateService.this;
        }

        /**
         * download
         * @param url
         * @param fileName
         */
        public void dwonLoadApk(String url, String fileName){
            downLoadApkUrl = url;
            downLoadApkName = fileName;
            isCancleDownload = false;
            startDownload();//启动下载
        }

        /**
         * 取消下载
         */
        public void calcelDowmload(){
            isCancleDownload = true;
            downLoadRequestCall.cancel();
            mNotificationManager.cancel(0);
        }

    }

}
