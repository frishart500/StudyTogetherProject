package com.example.studytogetherproject.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.example.studytogetherproject.Moduls.ChooseInf;
import com.example.studytogetherproject.MainClasses.MainActivity;
import com.example.studytogetherproject.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChooseActivity extends AppCompatActivity {
    private EditText comment;
    private RatingBar defaultRatingBar;
    private Button btn_send;
    private String getExtraEmail;
    private ImageView back;
    private InterstitialAd mInterstitialAd;
    private final String TAG = "---AdMob";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                createAd();
            }
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseActivity.this, MainActivity.class));
            }
        });

        sendInformation();
        getSupportActionBar().hide();
    }

    private void createAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        createIntertitialAd(adRequest);
    }

    private void createIntertitialAd(AdRequest adRequest){
        InterstitialAd.load(this,"ca-app-pub-1029213395711583/9528832398", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                Log.d(TAG, "onAdLoaded");

                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Log.d("TAG", "The ad was dismissed.");
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        Log.d("TAG", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        mInterstitialAd = null;
                        Log.d("TAG", "The ad was shown.");
                    }
                });

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.d(TAG, loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });
    }

    private void sendInformation() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                getExtraEmail = getIntent().getStringExtra("email");
                comment = findViewById(R.id.comment);
                defaultRatingBar = findViewById(R.id.ratingBar_default);
                btn_send = findViewById(R.id.btn_send);
                ChooseInf choose = new ChooseInf();
                defaultRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        choose.setRaiting(String.valueOf(rating));
                        btn_send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                choose.setEmail(getExtraEmail);
                                if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(getExtraEmail)) {
                                    choose.setEmail(getIntent().getStringExtra("myEmail"));
                                } else {
                                    choose.setEmail(getExtraEmail);
                                }

                                choose.setComment(comment.getText().toString());

                                choose.setName(name);
                                choose.setMyEmail(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                choose.getRatingMiddle();
                                FirebaseDatabase.getInstance().getReference("Raiting").push().setValue(choose);

                                if (mInterstitialAd != null) {
                                    mInterstitialAd.show(ChooseActivity.this);
                                } else {
                                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }
                            }
                        });

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(valueEventListener);
    }
}