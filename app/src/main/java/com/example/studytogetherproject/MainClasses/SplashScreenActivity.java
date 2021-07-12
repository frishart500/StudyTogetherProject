package com.example.studytogetherproject.MainClasses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.example.studytogetherproject.Onboarding.IntroActivity;
import com.example.studytogetherproject.Onboarding.IntroActivityOnboarding;
import com.example.studytogetherproject.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity {
    //переменные
    private FirebaseAuth mAuth;
    private ImageView image;

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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if(mAuth.getCurrentUser() != null){
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    intent = new Intent(getApplicationContext(), IntroActivityOnboarding.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3500);
    }


}