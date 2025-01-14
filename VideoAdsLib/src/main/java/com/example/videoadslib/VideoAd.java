package com.example.videoadslib;

import com.google.gson.annotations.SerializedName;

public class VideoAd {

    @SerializedName("video_link")
    private String videoLink; // URL to the video

    @SerializedName("advertiser_link")
    private String advertiserLink; // URL to the advertiser website

    @SerializedName("advertiser_icon")
    private String advertiserIcon; // URL to the advertiser icon

    public VideoAd(){

    }

    public VideoAd(String videoLink, String advertiserLink, String advertiserIcon) {
        this.videoLink = videoLink;
        this.advertiserLink = advertiserLink;
        this.advertiserIcon = advertiserIcon;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public VideoAd setVideoLink(String videoLink) {
        this.videoLink = videoLink;
        return this;
    }

    public String getAdvertiserLink() {
        return advertiserLink;
    }

    public VideoAd setAdvertiserLink(String advertiserLink) {
        this.advertiserLink = advertiserLink;
        return this;
    }

    public String getAdvertiserIcon() {
        return advertiserIcon;
    }

    public VideoAd setAdvertiserIcon(String advertiserIcon) {
        this.advertiserIcon = advertiserIcon;
        return this;
    }

    @Override
    public String toString() {
        return "VideoAd{" +
                "videoLink='" + videoLink + '\'' +
                ", advertiserLink='" + advertiserLink + '\'' +
                ", advertiserIcon='" + advertiserIcon + '\'' +
                '}';
    }
}
