package com.tenz.hotchpotch.util;

import java.util.HashMap;

import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.ShareParams;

/**
 * Author: TenzLiu
 * Date: 2018-02-28 14:20
 * Description: JShareUtil
 */

public class JShareUtil {

    /**
     *
     * @param sharePlatform
     * @param title
     * @param content
     * @param url
     * @param imageUrl
     */
    public static void share(String sharePlatform, String title, String content, String url, String imageUrl, String imagePath){
        //这里以分享链接为例
        ShareParams shareParams = new ShareParams();
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        shareParams.setTitle(title);
        shareParams.setText(content);
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        shareParams.setUrl(url);
        if(!StringUtil.isEmpty(imageUrl)){
            shareParams.setImageUrl(imageUrl);
        }else{
            shareParams.setImagePath(imagePath);
        }
        JShareInterface.share(sharePlatform, shareParams, new PlatActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

            }

            @Override
            public void onError(Platform platform, int i, int i1, Throwable throwable) {
                ToastUtil.showToast(throwable.getMessage());
            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
    }

}
