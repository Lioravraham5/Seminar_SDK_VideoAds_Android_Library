package com.example.videoadslib;

import android.content.Context;
import android.net.Uri;

import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;


public class VideoPlayer {
    private ExoPlayer player;

    public void initializePlayer(Context context, Uri videoUri) {
        player = new ExoPlayer.Builder(context).build();
        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        player.setMediaItem(mediaItem);
        player.prepare();
    }

    public ExoPlayer getPlayer() {
        return player;
    }

    public void play() {
        if (player != null) {
            player.play();
        }
    }

    public void stop() {
        if (player != null) {
            player.stop();
        }
    }

    public void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    public void mute() {
        if (player != null) {
            player.setVolume(0f);
        }
    }

    public void unMute() {
        if (player != null) {
            player.setVolume(1f);
        }
    }
}
