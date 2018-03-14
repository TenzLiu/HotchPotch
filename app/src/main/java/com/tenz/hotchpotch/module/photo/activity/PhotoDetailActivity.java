package com.tenz.hotchpotch.module.photo.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.base.BaseActivity;
import com.tenz.hotchpotch.rx.RxScheduler;
import com.tenz.hotchpotch.util.FileUtil;
import com.tenz.hotchpotch.util.GlideUtil;
import com.tenz.hotchpotch.util.ResourceUtil;
import com.tenz.hotchpotch.util.StatusBarUtil;
import com.tenz.hotchpotch.util.StringUtil;
import com.tenz.hotchpotch.util.ToastUtil;

import java.io.File;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Author: TenzLiu
 * Date: 2018-01-29 17:49
 * Description: PhotoDetailActivity
 */

public class PhotoDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.pv_image)
    PhotoView pv_image;
    @BindView(R.id.fab_save)
    FloatingActionButton fab_save;

    private String pic_url;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        StatusBarUtil.setTransparent(this);
        initTitleBar(mToolbar,"",ResourceUtil.getColor(R.color.colorTransParent));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4以上
            int statusBarHeight = StatusBarUtil.getStatusBarHeight(mContext);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mToolbar.getLayoutParams();
            layoutParams.setMargins(0,statusBarHeight,0,0);
            mToolbar.setLayoutParams(layoutParams);
        }
        pv_image.enable();
        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtil.isEmpty(pic_url)){
                    ToastUtil.showToast("图片地址获取失败");
                    return;
                }
                savaImageToLocal(pic_url,""+System.currentTimeMillis());
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            pic_url = bundle.getString("pic_url");
        }
        RequestOptions requestOptions = new RequestOptions()
                .centerInside()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.default_icon);
        GlideUtil.loadImage(mContext,pic_url,requestOptions,pv_image);
    }

    /**
     * savaImageToLocal
     * @param picUrl
     * @param fileName
     */
    private void savaImageToLocal(final String picUrl, final String fileName) {
        Observable.create(new ObservableOnSubscribe<File>() {

            @Override
            public void subscribe(ObservableEmitter<File> e) throws Exception {
                e.onNext(Glide.with(mContext)
                        .load(picUrl)
                        .downloadOnly(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                        .get());
                e.onComplete();
            }
        }).compose(RxScheduler.<File>rxSchedulerTransform())
                .compose(this.<File>bindToLifecycle())
                .subscribe(new Observer<File>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(File file) {
                        saveImage(file,fileName);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(pv_image,"保存失败",Snackbar.LENGTH_SHORT)
                                .setActionTextColor(ResourceUtil.getColor(R.color.colorWhite))
                                .show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * saveImage
     * @param file
     * @param fileName
     */
    private void saveImage(File file, String fileName) {
        FileUtil.saveImage(mContext, fileName, file, new FileUtil.SaveResultCallback() {
            @Override
            public void onSavedSuccess() {
                Snackbar.make(pv_image,"保存成功",Snackbar.LENGTH_SHORT)
                        .setActionTextColor(ResourceUtil.getColor(R.color.colorWhite))
                        .show();
            }

            @Override
            public void onSavedFailed() {
                Snackbar.make(pv_image,"保存失败",Snackbar.LENGTH_SHORT)
                        .setActionTextColor(ResourceUtil.getColor(R.color.colorWhite))
                        .show();
            }
        });
    }

}
