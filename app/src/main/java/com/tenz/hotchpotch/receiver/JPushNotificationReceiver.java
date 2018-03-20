package com.tenz.hotchpotch.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.app.Constant;
import com.tenz.hotchpotch.event.JPushEvent;
import com.tenz.hotchpotch.module.welcome.WelcomeActivity;
import com.tenz.hotchpotch.util.AppUtil;
import com.tenz.hotchpotch.util.LogUtil;
import com.tenz.hotchpotch.util.StringUtil;
import com.ttsea.jrxbus2.RxBus2;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * Author: TenzLiu
 * Date: 2018-03-20 10:06
 * Description: 极光通知接收
 */

public class JPushNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            LogUtil.d("[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                LogUtil.d("[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                LogUtil.d("[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                processCustomMessage(context, bundle);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                LogUtil.d("[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                LogUtil.d("[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
                String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                String message = bundle.getString(JPushInterface.EXTRA_ALERT);
                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
                JPushEvent jPushEvent = new JPushEvent();
                LogUtil.d("jPushEvent:-----------------------"+jPushEvent.toString());

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                LogUtil.d("[MyReceiver] 用户点击打开了通知");
                if (AppUtil.isAppRunningForegroud(context)) {
                    String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                    String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                    String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
                    JPushEvent jPushEvent = new JPushEvent();
                    LogUtil.d("jPushEvent:-----------------------"+jPushEvent.toString());
                    //RxBus发送通知
                    //RxBus2.getInstance().post(Constant.CODE_JPUSH_RECEIVER,jPushEvent);
                }else{
                    //打开自定义的Activity
                    Intent i = new Intent(context, WelcomeActivity.class);
                    i.putExtras(bundle);
                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    context.startActivity(i);
                }

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                LogUtil.d("[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                LogUtil.d("[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
            } else {
                LogUtil.d("[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e){

        }

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (StringUtil.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    LogUtil.d("This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it =  json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " +json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    LogUtil.d("Get message extra JSON error!");
                }
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to Activity
    private void processCustomMessage(Context context, Bundle bundle) {
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        //获取NotificationManager实例
        NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //获取PendingIntent
        Intent mainIntent = new Intent(context,WelcomeActivity.class);
        mainIntent.putExtras(bundle);
        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        PendingIntent mainPendingIntent = PendingIntent.getActivity(context,
                0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //创建 Notification.Builder 对象
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.logo)
                //点击通知后自动清除
                .setAutoCancel(true)
                .setContentTitle("HotchPotch")
                .setContentText(extras)
                .setContentIntent(mainPendingIntent);
        //发送通知
        notifyManager.notify(1, builder.build());
    }

}
