package com.example.videoadsproject;

import android.os.Bundle;
import android.util.Log;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.videoadslib.AdDialogFragment;
import com.example.videoadslib.VideoAdsManager;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private MaterialButton load_ad_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




        VideoAdsManager videoAdsManager = new VideoAdsManager(getPackageName());
        videoAdsManager.setCloseButtonDelay(9000); // 9 seconds
        videoAdsManager.setAdClosedCallback(() -> {
            // your code here
            Log.d("MainActivity", "Ad closed");
        });
        load_ad_button = findViewById(R.id.load_ad_button);
        load_ad_button.setOnClickListener(v -> videoAdsManager.startVideoAd(getSupportFragmentManager()));
    }



    @NonNull
    @Override
    public OnBackInvokedDispatcher getOnBackInvokedDispatcher() {
        return super.getOnBackInvokedDispatcher();
    }


}