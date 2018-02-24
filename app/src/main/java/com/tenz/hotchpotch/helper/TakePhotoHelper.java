package com.tenz.hotchpotch.helper;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;


import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TakePhotoOptions;

import java.io.File;

/**
 * Author: TenzLiu
 * Date: 2017-12-11 11:32
 * Description: 拍照工具类
 * //拍照（裁剪）
 * new TakePhotoHelper().takePhoto(MainActivity.this,getTakePhoto(),true,true);
 * //拍照（不裁剪）
 * new TakePhotoHelper().takePhoto(MainActivity.this,getTakePhoto(),false,true);
 * //相册单张（裁剪）
 * new TakePhotoHelper().takePhotoAlbum(MainActivity.this,getTakePhoto(),true,true,1);
 * //相册单张（不裁剪）
 * new TakePhotoHelper().takePhotoAlbum(MainActivity.this,getTakePhoto(),false,true,1);
 * //相册多张（裁剪）
 *  new TakePhotoHelper().takePhotoAlbum(MainActivity.this,getTakePhoto(),true,true,3);
 * //相册多张（不裁剪）
 * new TakePhotoHelper().takePhotoAlbum(MainActivity.this,getTakePhoto(),false,true,3);
 */

public class TakePhotoHelper {

    private Context mContext;
    private File file;
    private Uri imageUri;
    private TakePhoto takePhoto;
    private boolean isCrop;
    private boolean isCompress;

    /**
     * 构造方法初始化
     */
    public TakePhotoHelper() {

    }

    /**
     * 拍照
     */
    public void takePhoto(Context context, TakePhoto takePhoto, boolean isCrop, boolean isCompress){
        this.mContext = context;
        this.takePhoto = takePhoto;
        this.isCrop = isCrop;
        this.isCompress = isCompress;
        file = new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        // 判断是否是7.0
        if(Build.VERSION.SDK_INT >= 24){
            // 适配android7.0 ，不能直接访问原路径
            imageUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
        }
        else{
            imageUri = Uri.fromFile(file);
        }
        configCompress();
        configTakePhotoOption();
        if(isCrop){
            takePhoto.onPickFromCaptureWithCrop(imageUri,getCropOptions());
        }else {
            takePhoto.onPickFromCapture(imageUri);
        }
    }

    /**
     * 相册选择图片
     * @param isCrop
     */
    public void takePhotoAlbum(Context context, TakePhoto takePhoto, boolean isCrop, boolean isCompress, int limit){
        this.mContext = context;
        this.takePhoto = takePhoto;
        this.isCrop = isCrop;
        this.isCompress = isCompress;
        file = new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        // 判断是否是7.0
        if(Build.VERSION.SDK_INT >= 24){
            // 适配android7.0 ，不能直接访问原路径
            imageUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
        }
        else{
            imageUri = Uri.fromFile(file);
        }
        configCompress();
        configTakePhotoOption();
        if(limit>1){
            if(isCrop){
                takePhoto.onPickMultipleWithCrop(limit,getCropOptions());
            }else {
                takePhoto.onPickMultiple(limit);
            }
            return;
        }
        if(true){//从相册选择（默认）
            if(isCrop){
                takePhoto.onPickFromDocumentsWithCrop(imageUri,getCropOptions());
            }else {
                takePhoto.onPickFromDocuments();
            }
            return;
        }else {//从文件选择
            if(isCrop){
                takePhoto.onPickFromGalleryWithCrop(imageUri,getCropOptions());
            }else {
                takePhoto.onPickFromGallery();
            }
        }
    }

    /**
     * 初始化是否使用自带相册与是否纠正拍照的照片旋转角度
     */
    private void configTakePhotoOption(){
        TakePhotoOptions.Builder builder=new TakePhotoOptions.Builder();
        if(true){//使用TakePhoto自带相册(默认)
            builder.setWithOwnGallery(true);
        }
        if(true){//纠正拍照的照片旋转角度（默认）
            builder.setCorrectImage(true);
        }
        takePhoto.setTakePhotoOptions(builder.create());

    }

    /**
     * 初始化压缩
     */
    private void configCompress(){
        if(!isCompress){//如果不压缩，直接返回
            takePhoto.onEnableCompress(null,false);
            return ;
        }
        int maxSize = 102400;//默认大小
        int width = 800;//默认宽
        int height = 800;//默认高
        boolean showProgressBar = false;//是否显示压缩进度条
        boolean enableRawFile = true;//拍照压缩后是否保存原图
        CompressConfig config;
        if(true){//用自带的压缩工具
            config = new CompressConfig.Builder()
                    .setMaxSize(maxSize)
                    .setMaxPixel(width>=height? width:height)
                    .enableReserveRaw(enableRawFile)
                    .create();
        }else {//用Luban压缩工具
            LubanOptions option = new LubanOptions.Builder()
                    .setMaxHeight(height)
                    .setMaxWidth(width)
                    .setMaxSize(maxSize)
                    .create();
            config= CompressConfig.ofLuban(option);
            config.enableReserveRaw(enableRawFile);
        }
        takePhoto.onEnableCompress(config,showProgressBar);
    }

    /**
     * 初始化裁剪
     * @return
     */
    private CropOptions getCropOptions(){
        if(!isCrop){//如果不裁剪直接返回
            return null;
        }
        int height = 800;//默认高
        int width = 800;//默认宽
        boolean withWonCrop = false;//裁剪工具是否用自带(默认)，也可第三方
        CropOptions.Builder builder=new CropOptions.Builder();
        if(true){//尺寸/比例：宽x高(默认)
            builder.setAspectX(width).setAspectY(height);
        }else {//尺寸/比例：宽/高
            builder.setOutputX(width).setOutputY(height);
        }
        builder.setWithOwnCrop(withWonCrop);
        return builder.create();
    }

}
