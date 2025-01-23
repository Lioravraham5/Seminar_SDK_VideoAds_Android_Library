package com.example.videoadsproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.videoadslib.VideoAdsManager;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private MaterialButton open_activity_button;

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

        findViews();
        open_activity_button.setOnClickListener(v -> openNewActivity());
    }

    private void openNewActivity() {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    private void findViews() {
        open_activity_button = findViewById(R.id.open_activity_button);
    }


    @NonNull
    @Override
    public OnBackInvokedDispatcher getOnBackInvokedDispatcher() {
        return super.getOnBackInvokedDispatcher();
    }


}