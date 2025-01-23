package com.example.videoadsproject;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.videoadslib.VideoAdsManager;

public class SecondActivity extends AppCompatActivity {

    private VideoAdsManager videoAdsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        displayVideoAd();
    }

    private void displayVideoAd() {
        // Create a VideoAdsManager instance
        videoAdsManager = new VideoAdsManager(getPackageName());

        // Set the close button delay to 6 seconds
        videoAdsManager.setCloseButtonDelay(6000);

        // Set the ad closed callback
        videoAdsManager.setAdClosedCallback(() -> {
            // your code here
            Log.d("SecondActivity", "Ad closed");
        });

        // Display the video ad
        videoAdsManager.startVideoAd(getSupportFragmentManager(), false);
    }
}