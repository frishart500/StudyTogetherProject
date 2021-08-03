package com.example.studytogetherproject.MainClasses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.example.studytogetherproject.Onboarding.IntroActivityOnboarding;
import com.example.studytogetherproject.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity {
    //переменные
    private FirebaseAuth mAuth;
    private ImageView image;
    private Intent intent;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //базовые настройки
        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //иницилизация
        image = findViewById(R.id.image);
        mAuth = FirebaseAuth.getInstance();

        //анимация
        ObjectAnimator animation = ObjectAnimator.ofFloat(image, "rotationY", 0.0f, 360f);
        animation.setDuration(2000);
        animation.setRepeatCount(ObjectAnimator.INFINITE);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();

        //поток
        runnable = new Runnable() {
            @Override
            public void run() {
                if (mAuth.getCurrentUser() != null)
                    intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                else
                    intent = new Intent(SplashScreenActivity.this, IntroActivityOnboarding.class);
                SplashScreenActivity.this.startActivity(intent);
            }
        };

        handler = new Handler();
        handler.postDelayed(runnable, 3500);

    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }
}