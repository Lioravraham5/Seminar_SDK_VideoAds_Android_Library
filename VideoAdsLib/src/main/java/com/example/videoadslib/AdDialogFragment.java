package com.example.videoadslib;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.media3.common.Player;
import androidx.media3.ui.PlayerView;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

public class AdDialogFragment extends DialogFragment {
    private VideoApiManager videoApiManager;
    private VideoPlayer videoPlayer;
    private VideoAd currentAd;
    private boolean isClicked = false; // Flag to check if the video has been clicked

    private PlayerView video_container;
    private ImageButton close_button;
    private MaterialButton redirect_button;
    private ShapeableImageView logo_image;

    // callback for the ad closed event for the host app:
    private AdClosedCallback adClosedCallback;
    private int closeButtonDelay = 5000; // default 5 seconds
    private boolean muteAd = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ad_fragment, container, false);

        findViews(view);

        // Initialize the VideoApiManager
        videoApiManager = new VideoApiManager();

        fetchAdFromServer();
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        // Get the current dialog window
        if (getDialog() != null && getDialog().getWindow() != null) {
            // Set the dialog window to match the parent (activity) size
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (videoPlayer != null && videoPlayer.getPlayer() != null) {
            // Check if the player is ready and should be playing
            if (videoPlayer.getPlayer().getPlaybackState() == Player.STATE_READY) {
                // Resume playback
                videoPlayer.getPlayer().play();
                Log.d("AdDialogFragment", "Video resumed in onResume");
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (videoPlayer != null && videoPlayer.getPlayer() != null) {
            videoPlayer.getPlayer().pause(); // Pause the video
            Log.d("AdDialogFragment", "Video paused in onPause");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (videoPlayer != null) {
            videoPlayer.releasePlayer(); // Release player resources
            Log.d("AdDialogFragment", "Video player released in onDestroyView");
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (adClosedCallback != null) {
            adClosedCallback.onAdClosed();
        }
    }

    private void fetchAdFromServer() {
        videoApiManager.fetchAd(new VideoApiManager.AdFetchedCallback() {
            @Override
            public void onSuccessFetching(VideoAd ad) {
                currentAd = ad;
                Log.d("AdDialogFragment", "Fetched ad: " + ad.toString());
                playAdVideo();
            }

            @Override
            public void onFailureFetching(String errorMessage) {
                // Handle error
                Log.d("AdDialogFragment", "Error fetching ad: " + errorMessage);
            }
        });

    }

    private void playAdVideo() {
        if (currentAd != null && currentAd.getVideoLink() != null){
            Uri videoUri = Uri.parse(currentAd.getVideoLink());

            //initialize videoPlayer:
            videoPlayer = new VideoPlayer();
            videoPlayer.initializePlayer(getContext(),videoUri);

            // Attach the player to the PlayerView
            video_container.setPlayer(videoPlayer.getPlayer());
            videoPlayer.play();

            // Set up video click listener to redirect to advertiser link
            video_container.setOnClickListener(v -> {
                if (currentAd.getAdvertiserLink() != null){
                    isClicked = true;
                    openAdvertiserLink(currentAd.getAdvertiserLink());
                }
            });

            // Mute the video if required
            if (muteAd) {
                muteVideo();
            }


            // Show the close button after a delay
            showCloseButtonWithDelay();

            // Listen for video completion
            setupOnCompletionListener();
        }

    }

    private void setupOnCompletionListener() {
        if (videoPlayer != null){
            videoPlayer.getPlayer().addListener(new Player.Listener() {
                @Override
                public void onPlaybackStateChanged(int playbackState) {
                    if (playbackState == Player.STATE_ENDED) {
                        showPostVideoUI();
                    }
                }
            });
            }
    }

    private void showPostVideoUI() {
        if (videoPlayer != null){
            videoPlayer.releasePlayer();
            video_container.setVisibility(View.GONE);

            // Set the background color to white
            FrameLayout rootLayout = (FrameLayout) requireView();
            rootLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.white));

            // Show the logo image if available
            if (currentAd.getAdvertiserIcon() != null){
                Glide.with(this)
                        .load(currentAd.getAdvertiserIcon())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                                setUpRedirectButton();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                                // When image is loaded, show the button
                                logo_image.setVisibility(View.VISIBLE);
                                setUpRedirectButton();
                                return false; // Return false to allow Glide to handle the resourc
                            }
                        })
                        .into(logo_image);
            }

            // If no logo image is available, show the redirect button
            else {
                setUpRedirectButton();
            }
        }
    }

    private void addAdEventToServer() {
        if(currentAd != null){
            AdEvent adEvent = new AdEvent(currentAd.getId(), isClicked);
            String packageName = requireContext().getPackageName();
            Log.d("AdDialogFragment", "Package name: " + packageName);
            videoApiManager.addAdEvent(packageName, adEvent);
            Log.d("AdDialogFragment", "AdEvent sent: " + adEvent.toString());
        }
        else {
            Log.d("AdDialogFragment", "AdEvent not sent: Ad is null.");
        }
    }

    private void setUpRedirectButton(){
        redirect_button.setVisibility(View.VISIBLE);
        redirect_button.setOnClickListener(v -> {
            if (currentAd.getAdvertiserLink() != null){
                isClicked = true;
                openAdvertiserLink(currentAd.getAdvertiserLink());
            }
        });
    }

    private void openAdvertiserLink(String advertiserLink) {
        // Pause the video before navigating to the link
        if (videoPlayer != null && videoPlayer.getPlayer() != null) {
            videoPlayer.getPlayer().pause(); // Pause the video
        }

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentAd.getAdvertiserLink()));
        startActivity(browserIntent);
    }

    private void showCloseButtonWithDelay() {
        // Show close button with delay
        new Handler().postDelayed(() -> close_button.setVisibility(View.VISIBLE), closeButtonDelay);

        close_button.setOnClickListener(v -> {
            Log.d("AdDialogFragment", "Close button clicked");
            if (videoPlayer != null) {
                videoPlayer.releasePlayer();
                Log.d("AdDialogFragment", "Video player released");
            }

            // Send ad event to server
            addAdEventToServer();

            dismiss();
        });
    }

    private void findViews(View view) {
        video_container = view.findViewById(R.id.video_container);
        close_button = view.findViewById(R.id.close_button);
        redirect_button = view.findViewById(R.id.redirect_button);
        logo_image = view.findViewById(R.id.logo_image);
    }

    public void setCloseButtonDelay(int delayMillis) {
        this.closeButtonDelay = delayMillis;
    }

    public void setAdClosedCallback(AdClosedCallback adClosedCallback) {
        this.adClosedCallback = adClosedCallback;
    }

    public AdDialogFragment setMuteAd(boolean muteAd) {
        this.muteAd = muteAd;
        return this;
    }

    public void muteVideo() {
        if (videoPlayer != null) {
            videoPlayer.mute();
            Log.d("AdDialogFragment", "Video muted");
        }
        else {
            Log.d("AdDialogFragment", "muteVideo() called but videoPlayer is null");
        }
    }

    public void unMuteVideo() {
        if (videoPlayer != null) {
            videoPlayer.unMute();
            Log.d("AdDialogFragment", "Video unmuted");
        }
    }

    public void setVolume(float volumeLevel) {
        if (videoPlayer != null && videoPlayer.getPlayer() != null) {
            videoPlayer.getPlayer().setVolume(volumeLevel);
            Log.d("AdDialogFragment", "Volume set to " + volumeLevel);
        }
    }
}
