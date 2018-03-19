package com.tenz.hotchpotch.util;

import android.os.Handler;

import java.lang.ref.WeakReference;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Author: TenzLiu
 * Date: 2018-03-19 11:48
 * Description: JPushUtil
 */

public class JPushUtil {

    private static final int MSG_SET_ALIAS = 1001;
    private JPushHandler mJPushHandler = new JPushHandler(JPushUtil.this);

    public JPushUtil() {

    }

    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    public void setAlias(String alias) {
        if (StringUtil.isEmpty(alias)) {
            ToastUtil.showToast("别名不能为空");
            return;
        }
        // 调用 Handler 来异步设置别名
        mJPushHandler.sendMessage(mJPushHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            switch (code) {
                case 0:
                    LogUtil.d("Set tag and alias success");
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    LogUtil.d("Failed to set alias and tags due to timeout. Try again after 60s.");
                    // 延迟 60 秒来调用 Handler 设置别名
                    mJPushHandler.sendMessageDelayed(mJPushHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    LogUtil.d("Failed with errorCode = " + code);
            }
        }
    };

    /**
     *
     */
    private static class JPushHandler extends Handler{

        private WeakReference<JPushUtil> mJPushHandlerWeakReference;

        public JPushHandler(JPushUtil jPushUtil) {
            mJPushHandlerWeakReference = new WeakReference<>(jPushUtil);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            JPushUtil jPushUtil = mJPushHandlerWeakReference.get();
            if(jPushUtil == null){
                return;
            }
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    LogUtil.d("Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(AppUtil.getContext(),
                            (String) msg.obj,
                            null,
                            jPushUtil.mAliasCallback);
                    break;
                default:
                    LogUtil.d("Unhandled msg - " + msg.what);
            }
        }

    }

}
