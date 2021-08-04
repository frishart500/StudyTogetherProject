package com.example.studytogetherproject.ui.home;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.studytogetherproject.MainClasses.MainActivity;
import com.example.studytogetherproject.R;
import com.example.studytogetherproject.Moduls.Users;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChangesActivity extends AppCompatActivity {
    private EditText name, data, phone, describtion;
    private ImageView back;
    private CircleImageView userImg;
    private ImageView dataImg;
    private Button change;
    private TextView subject;
    private InterstitialAd mInterstitialAd;
    private final String TAG = "---AdMob";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatabaseReference uidRef = null;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changes);
        init();
        getDataPicker();
        createListOfTheSubjects();
        getSupportActionBar().hide();

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.main));

        getUserImage();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                createAd();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(v, "Профиль корректно изменен!", Snackbar.LENGTH_LONG);
                snackbar.setBackgroundTint(0XFFffffff);
                snackbar.setTextColor(0XFF601C80);
                snackbar.show();
                changes();

                if (mInterstitialAd != null) {
                    mInterstitialAd.show(ChangesActivity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        });

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

    private void createListOfTheSubjects(){
        subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ChangesActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.list_of_subject);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);

                ListView list = dialog.findViewById(R.id.list);
                ImageView close = dialog.findViewById(R.id.close);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                //////////

                String[] subjectsArray = getResources().getStringArray(R.array.subjects);

                ///////////

                ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_for_list, R.id.textSubject, subjectsArray);
                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.dismiss();
                        subject.setText(subjectsArray[position]);
                    }
                });

                dialog.show();
            }
        });
    }

    private void init() {
        uidRef = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        back = findViewById(R.id.back);
        name = findViewById(R.id.name);
        data = findViewById(R.id.editData);
        subject = findViewById(R.id.subject);
        phone = findViewById(R.id.phone);
        describtion = findViewById(R.id.describe);
        dataImg = findViewById(R.id.dataImg);
        change = findViewById(R.id.change);
        userImg = findViewById(R.id.userImg);
    }

    private void changes() {
        String nameS = name.getText().toString();
        String dataS = data.getText().toString();
        String subjectS = subject.getText().toString();
        String phoneS = phone.getText().toString();
        String describtionS = describtion.getText().toString();

        ValueEventListener vChangeListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = new Users();
                if (!nameS.isEmpty()) {
                    users.setName(nameS);
                    uidRef.child("name").setValue(users.getName());
                    FirebaseDatabase.getInstance().getReference("FriendsInChats").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String s = "";
                            for(DataSnapshot ds : snapshot.getChildren()){
                                String name = ds.child("name").getValue(String.class);
                                s = ds.getKey();
                                if(!name.equals(nameS)){
                                    FirebaseDatabase.getInstance().getReference("FriendsInChats").child(s).child("name").setValue(nameS);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                if (!subjectS.equals("")) {
                    users.setSubject(subjectS);
                    uidRef.child("subject").setValue(users.getSubject());
                }
                if (!describtionS.isEmpty()) {
                    users.setDescribtion(describtionS);
                    uidRef.child("describtion").setValue(users.getDescribtion());
                }
                if (!phoneS.isEmpty()) {
                    users.setPhone(phoneS);
                    uidRef.child("phone").setValue(users.getPhone());
                }
                if (!dataS.isEmpty()) {
                    users.setData(dataS);
                    uidRef.child("data").setValue(users.getData());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        uidRef.addListenerForSingleValueEvent(vChangeListener);


    }

    private void getDataPicker() {
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "." + month + "." + year;
                String m = String.valueOf(month);
                String d = String.valueOf(day);
                if (String.valueOf(day).length() == 1) {
                    d = ("0" + day);
                }

                if (String.valueOf(month).length() == 1) {
                    m = ("0" + month);
                }

                date = d + "." + m + "." + year;


                data.setText(date);
            }
        };

        dataImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(ChangesActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
    }

    private void getUserImage(){
        FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String img = snapshot.child("imgUri").getValue(String.class);
                Picasso.get().load(img).into(userImg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}