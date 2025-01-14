package com.example.videoadslib;

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
import androidx.fragment.app.Fragment;
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

public class AdFragment extends Fragment {
    private static final int CLOSE_BUTTON_DELAY = 5000; // 5 seconds

    private VideoApiManager videoApiManager;
    private VideoPlayer videoPlayer;
    private VideoAd currentAd;

    private PlayerView video_container;
    private ImageButton close_button;
    private MaterialButton redirect_button;
    private ShapeableImageView logo_image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ad_fragment, container, false);

        findViews(view);
        fetchAdFromServer();
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        if (videoPlayer != null && videoPlayer.getPlayer() != null) {
            // Check if the player is ready and should be playing
            if (videoPlayer.getPlayer().getPlaybackState() == Player.STATE_READY) {
                // Resume playback
                videoPlayer.getPlayer().play();
                Log.d("AdFragment", "Video resumed in onResume");
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (videoPlayer != null && videoPlayer.getPlayer() != null) {
            videoPlayer.getPlayer().pause(); // Pause the video
            Log.d("AdFragment", "Video paused in onPause");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (videoPlayer != null) {
            videoPlayer.releasePlayer(); // Release player resources
            Log.d("AdFragment", "Video player released in onDestroyView");
        }
    }

    private void fetchAdFromServer() {
        videoApiManager = new VideoApiManager();
        videoApiManager.fetchAd(new VideoApiManager.AdFetchedCallback() {
            @Override
            public void onSuccessFetching(VideoAd ad) {
                currentAd = ad;
                Log.d("AdFragment", "Fetched ad: " + ad.toString());
                playAdVideo();
            }

            @Override
            public void onFailureFetching(String errorMessage) {
                // Handle error
                Log.d("AdFragment", "Error fetching ad: " + errorMessage);
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

            // optional: mute the video
            //videoPlayer.mute();

            // Set up video click listener to redirect to advertiser link
            video_container.setOnClickListener(v -> {
                if (currentAd.getAdvertiserLink() != null){
                    openAdvertiserLink(currentAd.getAdvertiserLink());
                }
            });

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
                                sutUpRedirectButton();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                                // When image is loaded, show the button
                                logo_image.setVisibility(View.VISIBLE);
                                sutUpRedirectButton();
                                return false; // Return false to allow Glide to handle the resourc
                            }
                        })
                        .into(logo_image);
            }

            // If no logo image is available, show the redirect button
            else {
                sutUpRedirectButton();
            }
        }
    }

    private void sutUpRedirectButton(){
        redirect_button.setVisibility(View.VISIBLE);
        redirect_button.setOnClickListener(v -> {
            if (currentAd.getAdvertiserLink() != null){
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
        new Handler().postDelayed(() -> close_button.setVisibility(View.VISIBLE), CLOSE_BUTTON_DELAY);

        close_button.setOnClickListener(v -> {
            Log.d("AdFragment", "Close button clicked");
            if (videoPlayer != null) {
                videoPlayer.releasePlayer();
                Log.d("AdFragment", "Video player released");
            }
            requireActivity().getSupportFragmentManager().beginTransaction().remove(AdFragment.this).commit();

        });
    }

    private void findViews(View view) {
        video_container = view.findViewById(R.id.video_container);
        close_button = view.findViewById(R.id.close_button);
        redirect_button = view.findViewById(R.id.redirect_button);
        logo_image = view.findViewById(R.id.logo_image);
    }
}
