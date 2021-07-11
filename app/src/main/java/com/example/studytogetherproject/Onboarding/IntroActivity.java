package com.example.studytogetherproject.Onboarding;

import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;
import com.codemybrainsout.onboarder.views.FlowingGradientClass;
import com.example.studytogetherproject.MainClasses.LoginOrSignUpActivity;
import com.example.studytogetherproject.R;


import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AhoyOnboarderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AhoyOnboarderCard ahoyOnboarderCard1 = new AhoyOnboarderCard("Добро пожаловать!", "Давай я расскажу поподробней о нашем приложении.", R.drawable.shef1);
        ahoyOnboarderCard1.setTitleColor(R.color.main);
        ahoyOnboarderCard1.setBackgroundColor(R.color.white);
        ahoyOnboarderCard1.setDescriptionColor(R.color.mainLight);
        ahoyOnboarderCard1.setTitleTextSize(dpToPixels(15, this));
        ahoyOnboarderCard1.setDescriptionTextSize(dpToPixels(8, this));
        ahoyOnboarderCard1.setIconLayoutParams(450, 450, 300, 20, 20, 10);

        AhoyOnboarderCard ahoyOnboarderCard2 = new AhoyOnboarderCard("В приложении есть баллы и рейтинг", "По окончании туториала вам дадут 500 баллов \uD83D\uDC8E " +
                "Чтобы получать баллы нужно выполнять задания \uD83D\uDCC8 " +
                "Чтобы выложить задание нужно их тратить \uD83D\uDCC9 " +
                "За выполнение заданий эксперту ставится оценка, которая влияет на рейтинг \uD83D\uDD25", R.drawable.znak);
        ahoyOnboarderCard2.setBackgroundColor(R.color.white);
        ahoyOnboarderCard2.setTitleColor(R.color.main);
        ahoyOnboarderCard2.setDescriptionColor(R.color.mainLight);
        ahoyOnboarderCard2.setTitleTextSize(dpToPixels(15, this));
        ahoyOnboarderCard2.setDescriptionTextSize(dpToPixels(8, this));
        ahoyOnboarderCard2.setIconLayoutParams(290, 290, 170, 20, 20, 20);

        AhoyOnboarderCard ahoyOnboarderCard3 = new AhoyOnboarderCard("Как работает приложение?", "Публикуете задание \uD83D\uDCDD " +
                "У вас списывают баллы \uD83D\uDCB8 " +
                "Эксперты кидают заявки \uD83D\uDC68\u200D\uD83C\uDF93 " +
                "Выбираете эксперта \uD83D\uDD0D " +
                "Эксперт помогает вам\uD83E\uDD1D " +
                "Эксперт получает баллы \uD83D\uDC8E " +
                "Пишите отзыв о работе \uD83D\uDCC3", R.drawable.fire);

        ahoyOnboarderCard3.setBackgroundColor(R.color.white);
        ahoyOnboarderCard3.setTitleColor(R.color.main);
        ahoyOnboarderCard3.setDescriptionColor(R.color.mainLight);
        ahoyOnboarderCard3.setTitleTextSize(dpToPixels(15, this));
        ahoyOnboarderCard3.setDescriptionTextSize(dpToPixels(8, this));
        ahoyOnboarderCard3.setIconLayoutParams(290, 290, 170, 20, 20, 20);

        AhoyOnboarderCard ahoyOnboarderCard4 = new AhoyOnboarderCard("Завершение задания", "Чтобы закончить задание нажмите кнопку 'Закончит задание'. Затем подождите, когда второй человек нажмёт эту кнопку и задание будет полностью завершено \uD83E\uDDE0", R.drawable.chat_img_);
        ahoyOnboarderCard4.setBackgroundColor(R.color.white);
        ahoyOnboarderCard4.setTitleColor(R.color.main);
        ahoyOnboarderCard4.setDescriptionColor(R.color.mainLight);
        ahoyOnboarderCard4.setTitleTextSize(dpToPixels(15, this));
        ahoyOnboarderCard4.setDescriptionTextSize(dpToPixels(8, this));
        ahoyOnboarderCard4.setIconLayoutParams(290, 290, 170, 20, 20, 20);

        AhoyOnboarderCard ahoyOnboarderCard5 = new AhoyOnboarderCard("Мои поздравления теперь мы точно сможем учиться вместе \uD83E\uDD73\uD83D\uDE0E", "", R.drawable.shef2);
        ahoyOnboarderCard5.setBackgroundColor(R.color.white);
        ahoyOnboarderCard5.setTitleColor(R.color.main);
        ahoyOnboarderCard5.setDescriptionColor(R.color.mainLight);
        ahoyOnboarderCard5.setTitleTextSize(dpToPixels(12, this));
        ahoyOnboarderCard5.setDescriptionTextSize(dpToPixels(8, this));
        ahoyOnboarderCard5.setIconLayoutParams(450, 450, 300, 20, 20, 10);

        List<AhoyOnboarderCard> pages = new ArrayList<>();
        pages.add(ahoyOnboarderCard1);
        pages.add(ahoyOnboarderCard2);
        pages.add(ahoyOnboarderCard3);
        pages.add(ahoyOnboarderCard4);
        pages.add(ahoyOnboarderCard5);

        setFinishButtonDrawableStyle(ContextCompat.getDrawable(this, R.drawable.main_color));
        setFinishButtonTitle("          Начать          ");

        setColorBackground(R.color.main);
        setOnboardPages(pages);
    }

    @Override
    public void onFinishButtonPressed() {

        startActivity(new Intent(getApplicationContext(), LoginOrSignUpActivity.class));

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(0);
    }
}