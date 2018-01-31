package com.tenz.hotchpotch.module.home.entity;

import com.tenz.hotchpotch.module.news.entity.GetNews;
import com.tenz.hotchpotch.module.photo.entity.GetPhotos;
import com.tenz.hotchpotch.module.video.entity.GetVideos;

import java.util.List;

/**
 * Author: TenzLiu
 * Date: 2018-01-31 11:54
 * Description: 首页列表封装数据
 */

public class HomeData {

    private int type;//类型
    private List<GetBanner.Banner> mBannerList;
    private List<Module> mModuleList;
    private GetVideos.Video mVideo;
    private GetNews.News mNews;
    private GetPhotos.Photo mPhoto;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<GetBanner.Banner> getBannerList() {
        return mBannerList;
    }

    public void setBannerList(List<GetBanner.Banner> bannerList) {
        mBannerList = bannerList;
    }

    public List<Module> getModuleList() {
        return mModuleList;
    }

    public void setModuleList(List<Module> moduleList) {
        mModuleList = moduleList;
    }

    public GetVideos.Video getVideo() {
        return mVideo;
    }

    public void setVideo(GetVideos.Video video) {
        mVideo = video;
    }

    public GetNews.News getNews() {
        return mNews;
    }

    public void setNews(GetNews.News news) {
        mNews = news;
    }

    public GetPhotos.Photo getPhoto() {
        return mPhoto;
    }

    public void setPhoto(GetPhotos.Photo photo) {
        mPhoto = photo;
    }
}
