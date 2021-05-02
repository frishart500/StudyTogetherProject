package com.example.kvantoriumproject.MainClasses;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.kvantoriumproject.slides.MainSlideFragment;
import com.example.kvantoriumproject.R;
import com.example.kvantoriumproject.slides.Slide1Fragment;
import com.example.kvantoriumproject.slides.Slide2Fragment;
import com.example.kvantoriumproject.slides.Slide3Fragment;
import com.example.kvantoriumproject.slides.Slide4Fragment;
import com.example.kvantoriumproject.slides.Slide5Fragment;
import com.example.kvantoriumproject.slides.Slide6Fragment;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;


public class IntroActivity extends AppIntro {
    private Slide1Fragment intro1;
    private MainSlideFragment intro;
    private Slide2Fragment intro2;
    private Slide3Fragment intro3;
    private Slide4Fragment intro4;
    private Slide5Fragment intro5;
    private Slide6Fragment intro6;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        intro1 = new Slide1Fragment();
        intro = new MainSlideFragment();
        intro2 = new Slide2Fragment();
        intro3 = new Slide3Fragment();
        intro4 = new Slide4Fragment();
        intro5 = new Slide5Fragment();
        intro6 = new Slide6Fragment();

        adding();
        settings();
    }

    private void adding(){
        addSlide(intro);
        addSlide(intro1);
        addSlide(intro2);
        addSlide(intro3);
        addSlide(intro4);
        addSlide(intro5);
        addSlide(intro6);
    }

    private void settings(){
        setTitleColor(Color.parseColor("#601C80"));
        setNextArrowColor(Color.parseColor("#601C80"));
        setColorDoneText(Color.parseColor("#601C80"));
        setProgressButtonEnabled(true);
        showSkipButton(false);
        setSeparatorColor(Color.parseColor("#FFFFFF"));
        setBarColor(Color.parseColor("#FFFFFF"));
        setIndicatorColor(Color.parseColor("#601C80"), Color.parseColor("#D091EC"));
        setDoneText("Вперед!");
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }

    @Override
    public void onNextPressed() {
    }
}