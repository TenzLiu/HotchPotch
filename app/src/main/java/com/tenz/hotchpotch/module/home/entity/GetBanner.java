package com.tenz.hotchpotch.module.home.entity;

import java.util.List;

/**
 * Author: TenzLiu
 * Date: 2018-01-30 17:17
 * Description: GetBanner
 */

public class GetBanner {

    private List<Banner> mBannerList;

    public List<Banner> getBannerList() {
        return mBannerList;
    }

    public void setBannerList(List<Banner> bannerList) {
        mBannerList = bannerList;
    }

    public static class Banner{

        private String pic_url;
        private String title;

        public String getPic_url() {
            return pic_url;
        }

        public void setPic_url(String pic_url) {
            this.pic_url = pic_url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

}
