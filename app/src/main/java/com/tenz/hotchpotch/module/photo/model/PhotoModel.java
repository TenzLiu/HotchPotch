package com.tenz.hotchpotch.module.photo.model;


import com.tenz.hotchpotch.api.PhotoApi;
import com.tenz.hotchpotch.http.RetrofitFactory;
import com.tenz.hotchpotch.module.photo.contract.PhotoContract;
import com.tenz.hotchpotch.module.photo.entity.GetPhotos;

import io.reactivex.Observable;

/**
 * Author: TenzLiu
 * Date: 2018-01-29 16:44
 * Description: PhotoModel
 */

public class PhotoModel implements PhotoContract.IPhotoModel {

    public static PhotoModel newInstance(){
        return new PhotoModel();
    }

    @Override
    public Observable<GetPhotos> getPhotos(int page) {
        return RetrofitFactory.getInstance().createApi(PhotoApi.HOST,PhotoApi.class).getPhotos(page);
    }

}
