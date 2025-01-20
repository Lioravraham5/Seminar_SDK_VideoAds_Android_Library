package com.example.videoadslib;

import com.google.gson.annotations.SerializedName;

public class AdEvent {
    @SerializedName("ad_id")
    private String adId;

    @SerializedName("is_clicked")
    private boolean isClicked;

    public AdEvent(String adId, boolean isClicked) {
        this.adId = adId;
        this.isClicked = isClicked;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }
}
