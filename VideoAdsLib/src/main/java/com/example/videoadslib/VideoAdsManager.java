package com.example.videoadslib;

import androidx.fragment.app.FragmentManager;

public class VideoAdsManager {
    private AdDialogFragment adDialogFragment;
    private VideoApiManager videoApiManager;
    private String packageName;

    public VideoAdsManager(String packageName){
        adDialogFragment = new AdDialogFragment();
        videoApiManager = new VideoApiManager();
        this.packageName = packageName;
        createPackage(packageName);
    }

    private void createPackage(String packageName) {
        if(packageName == null){
            throw new IllegalArgumentException("Package name cannot be null");
        }
        videoApiManager.createPackage(packageName);
    }

    public void startVideoAd(FragmentManager fragmentManager){
        adDialogFragment.setCancelable(false);
        adDialogFragment.show(fragmentManager, "AdDialogFragment");
    }

    public void setCloseButtonDelay(int delayMillis) {
        adDialogFragment.setCloseButtonDelay(delayMillis);
    }

    public void setAdClosedCallback(AdClosedCallback adClosedCallback) {
        adDialogFragment.setAdClosedCallback(adClosedCallback);
    }
}
