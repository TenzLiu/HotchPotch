package com.tenz.hotchpotch.module.photo.contract;


import com.tenz.hotchpotch.base.BasePresenter;
import com.tenz.hotchpotch.base.IBaseModel;
import com.tenz.hotchpotch.base.IBaseView;
import com.tenz.hotchpotch.module.photo.entity.GetPhotos;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.List;

import io.reactivex.Observable;

/**
 * Author: TenzLiu
 * Date: 2018-01-29 16:39
 * Description: PhotoContract
 */

public interface PhotoContract {

    public abstract class PhotoPresenter extends BasePresenter<IPhotoModel,IPhotoView>{

        public abstract void getPhotos(boolean isRefresh);

    }

    public interface IPhotoModel extends IBaseModel{

        Observable<GetPhotos> getPhotos(int page);

    }

    public interface IPhotoView extends IBaseView{

        void showPhotos(List<GetPhotos.Photo> photoList);

    }

}
