package com.tenz.hotchpotch.module.video.entity;

import java.util.List;

/**
 * Author: TenzLiu
 * Date: 2018-01-30 18:07
 * Description: GetVideos
 */

public class GetVideos {

    private List<Video> videoList;

    public List<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
    }

    public static class Video{

        private String videoUrl;
        private String title;

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

}
