package com.example.videoadslib;

import androidx.fragment.app.FragmentManager;

/**
 * The VideoAdsManager class provides an easy-to-use API for integrating video ads into Android applications.
 * It handles the configuration, display, and callback management of video ads.
 */
public class VideoAdsManager {
    private AdDialogFragment adDialogFragment;
    private VideoApiManager videoApiManager;
    private String packageName;

    /**
     * Constructs a VideoAdsManager instance with the given package name.
     *
     * @param packageName The package name of the application integrating the video ads.
     * @throws IllegalArgumentException if the package name is null.
     */
    public VideoAdsManager(String packageName){
        adDialogFragment = new AdDialogFragment();
        videoApiManager = new VideoApiManager();
        this.packageName = packageName;
        createPackage(packageName);
    }

    /**
     * Initializes the video ad package using the provided package name.
     *
     * @param packageName The package name to register for video ads.
     */
    private void createPackage(String packageName) {
        if(packageName == null){
            throw new IllegalArgumentException("Package name cannot be null");
        }
        videoApiManager.createPackage(packageName);
    }

    /**
     * Displays a video ad.
     *
     * @param fragmentManager The FragmentManager to manage the AdDialogFragment.
     * @param muteAd          Whether the video ad should be muted.
     */
    public void startVideoAd(FragmentManager fragmentManager,   boolean muteAd){
        adDialogFragment.setMuteAd(muteAd);
        adDialogFragment.setCancelable(false);
        adDialogFragment.show(fragmentManager, "AdDialogFragment");
    }

    /**
     * Sets a delay for when the close button becomes available.
     *
     * @param delayMillis The delay in milliseconds before the close button is enabled.
     */
    public void setCloseButtonDelay(int delayMillis) {
        adDialogFragment.setCloseButtonDelay(delayMillis);
    }

    /**
     * Sets a callback to be invoked when the video ad is closed.
     *
     * @param adClosedCallback The callback to be invoked when the video ad is closed.
     */
    public void setAdClosedCallback(AdClosedCallback adClosedCallback) {
        adDialogFragment.setAdClosedCallback(adClosedCallback);
    }


}
