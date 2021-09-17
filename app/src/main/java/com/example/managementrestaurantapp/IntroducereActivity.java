package com.example.managementrestaurantapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class IntroducereActivity extends AppCompatActivity {

    ImageView logo,burgerImg;
    TextView appName;
    LottieAnimationView lottieAnimationView;
    public static int SPLASH_SCREEN = 3200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logo = findViewById(R.id.logo);
        appName = findViewById(R.id.app_name);
        burgerImg = findViewById(R.id.img);
        lottieAnimationView = findViewById(R.id.burger);

        burgerImg.animate().translationY(-2500).setDuration(1000).setStartDelay(2000);
        logo.animate().translationY(-1400).setDuration(1000).setStartDelay(2000);
        appName.animate().translationY(-1400).setDuration(1000).setStartDelay(2000);
        lottieAnimationView.animate().translationY(1400).setDuration(1000).setStartDelay(2000);

        new Handler().postDelayed(() -> {

            Intent intent = new Intent(IntroducereActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        },SPLASH_SCREEN );
    }
}