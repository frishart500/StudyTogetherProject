package study_com.studytogetherproject.Onboarding;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import study_com.studytogetherproject.MainClasses.LoginOrSignUpActivity;
import study_com.studytogetherproject.Onboarding.slides.SlideFragment1;
import study_com.studytogetherproject.Onboarding.slides.SlideFragment2;
import study_com.studytogetherproject.Onboarding.slides.SlideFragment3;
import study_com.studytogetherproject.Onboarding.slides.SlideFragment4;
import study_com.studytogetherproject.Onboarding.slides.SlideFragment5;
import study_com.study_exmp.studytogetherproject.R;

import java.util.ArrayList;
import java.util.List;

public class IntroActivityOnboarding extends AppCompatActivity {
    private ViewPager viewPager;
    private PagerAdapter adapter;
    private List<Fragment> list;
    private ImageView balls;
    private Button btn, btnStart;
    private TextView skipBtn;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_onboarding);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.main));

        getSupportActionBar().hide();

        viewPager = findViewById(R.id.view_pager);
        btn = findViewById(R.id.materialButton);
        btnStart = findViewById(R.id.materialButtonStart);
        skipBtn = findViewById(R.id.skipBtn);
        balls = findViewById(R.id.balls);

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginOrSignUpActivity.class));
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(getItemofviewpager(+1), true);
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        btn.setVisibility(View.VISIBLE);
                        btnStart.setVisibility(View.GONE);
                        skipBtn.setVisibility(View.VISIBLE);
                        balls.setImageResource(R.drawable.first_ball);
                        break;
                    case 1:
                        btn.setVisibility(View.VISIBLE);
                        btnStart.setVisibility(View.GONE);
                        skipBtn.setVisibility(View.VISIBLE);
                        balls.setImageResource(R.drawable.second_ball);
                        break;
                    case 2:
                        btn.setVisibility(View.VISIBLE);
                        btnStart.setVisibility(View.GONE);
                        skipBtn.setVisibility(View.VISIBLE);
                        balls.setImageResource(R.drawable.third_ball);
                        break;
                    case 3:
                        btn.setVisibility(View.VISIBLE);
                        btnStart.setVisibility(View.GONE);
                        skipBtn.setVisibility(View.VISIBLE);
                        balls.setImageResource(R.drawable.fourth_ball);
                        break;
                    case 4:
                        btnStart.setVisibility(View.VISIBLE);
                        btn.setVisibility(View.GONE);
                        skipBtn.setVisibility(View.GONE);
                        balls.setImageResource(R.drawable.fifth_ball);
                        btnStart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(getApplicationContext(), LoginOrSignUpActivity.class));
                            }
                        });
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
        list = new ArrayList<>();
        list.add(new SlideFragment1());
        list.add(new SlideFragment2());
        list.add(new SlideFragment3());
        list.add(new SlideFragment4());
        list.add(new SlideFragment5());
        adapter = new SlidePagerAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(adapter);

    }

    private int getItemofviewpager(int i) {
        return viewPager.getCurrentItem() + i;
    }


}