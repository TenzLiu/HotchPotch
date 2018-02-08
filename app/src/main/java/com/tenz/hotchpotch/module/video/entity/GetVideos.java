package com.tenz.hotchpotch.module.video.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Author: TenzLiu
 * Date: 2018-01-30 18:07
 * Description: GetVideos
 */

public class GetVideos {

    private List<Video> V9LG4B3A0;

    public List<Video> getV9LG4B3A0() {
        return V9LG4B3A0;
    }

    public void setV9LG4B3A0(List<Video> v9LG4B3A0) {
        V9LG4B3A0 = v9LG4B3A0;
    }

    public static class Video implements Serializable {

        private String title;
        private String mp4_url;
        private String cover;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMp4_url() {
            return mp4_url;
        }

        public void setMp4_url(String mp4_url) {
            this.mp4_url = mp4_url;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

    }

}
