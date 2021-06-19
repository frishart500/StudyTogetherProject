package com.example.studytogetherproject.ui.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.studytogetherproject.MainClasses.LoginOrSignUpActivity;
import com.example.studytogetherproject.MainClasses.MainActivity;
import com.example.studytogetherproject.R;
import com.example.studytogetherproject.Moduls.Users;
import com.example.studytogetherproject.CommentsAndDetails.CommentActivity;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment {
    private ImageView stars;
    private CircleImageView userImg;
    private TextView email, exit, goToComments, addPoints, points, phone, data, name, subject, describeInProfile;
    private FirebaseAuth mAuth;
    private Button changeBtn;
    private RewardedAd mRewardedAd;
    private final String TAG = "--->AdMob";
    private double average = 0.0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initView(root);
        ReadUsersThread readUsersThread = new ReadUsersThread();
        readUsersThread.start();
        getRatingAndSetAverage();
        ShowAdThread thread = new ShowAdThread();
        thread.initilisationAd();
        thread.start();

        Window window = ((Activity)getContext()).getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.mainLight));

        goToComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CommentActivity.class);
                startActivity(intent);
            }
        });
        ((MainActivity) getActivity()).getSupportActionBar().hide();

        exit.setPaintFlags(exit.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        goToComments.setPaintFlags(goToComments.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mAuth = FirebaseAuth.getInstance();
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog;
                dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.exit_or_not);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                TextView textCancel = dialog.findViewById(R.id.no);
                TextView yes = dialog.findViewById(R.id.yes);
                textCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        Users users = new Users();
                        users.setStatus("offline");
                        ref.child("status").setValue(users.getStatus());
                        mAuth.signOut();
                        startActivity(new Intent(getContext(), LoginOrSignUpActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
                });

                dialog.show();
            }
        });
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChangesActivity.class));
            }
        });

        return root;
    }

    private void initView(View root) {
        exit = root.findViewById(R.id.exit);
        addPoints = root.findViewById(R.id.addPoints);
        changeBtn = root.findViewById(R.id.changeBtn);
        stars = root.findViewById(R.id.stars);
        userImg = root.findViewById(R.id.userImg);
        email = root.findViewById(R.id.email);
        name = root.findViewById(R.id.name);
        data = root.findViewById(R.id.data);
        phone = root.findViewById(R.id.phone);
        subject = root.findViewById(R.id.subject);
        points = root.findViewById(R.id.points);
        goToComments = root.findViewById(R.id.goToComments);
        describeInProfile = root.findViewById(R.id.describeInProfile);
    }
    private void readUser() {

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference uidRef = rootRef.child("User").child(uid);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nameS = dataSnapshot.child("name").getValue(String.class);
                String emailString = dataSnapshot.child("email").getValue(String.class);
                String dataString = dataSnapshot.child("data").getValue(String.class);
                String howMuchTasksDone = dataSnapshot.child("howMuchTasksDone").getValue(String.class);
                String howMuchNotifications = dataSnapshot.child("howMuchNotifications").getValue(String.class);
                String gender = dataSnapshot.child("gender").getValue(String.class);
                String pointsString = dataSnapshot.child("points").getValue(String.class);
                String phoneString = dataSnapshot.child("phone").getValue(String.class);
                String expertSubjectString = dataSnapshot.child("subject").getValue(String.class);
                String imgUri = dataSnapshot.child("imgUri").getValue(String.class);

                email.setText(emailString);
                name.setText(nameS);
                subject.setText(expertSubjectString);
                phone.setText(phoneString);

                if(imgUri.equals("boy1")){
                    userImg.setImageResource(R.drawable.boy1);
                }else if(imgUri.equals("boy2")){
                    userImg.setImageResource(R.drawable.boy2);
                }else if(imgUri.equals("boy3")){
                    userImg.setImageResource(R.drawable.boy3);
                }

                if(imgUri.equals("girl1")){
                    userImg.setImageResource(R.drawable.girl1);
                }else if(imgUri.equals("girl2")){
                    userImg.setImageResource(R.drawable.girl2);
                }else if(imgUri.equals("girl3")){
                    userImg.setImageResource(R.drawable.girl3);
                }

                getGender(gender, howMuchTasksDone);


                int counterOfNotifications = Integer.parseInt(howMuchNotifications);
                if (counterOfNotifications == 0) {
                }
                String describeProfile = dataSnapshot.child("describtion").getValue(String.class);
                describeInProfile.setText(describeProfile);
                data.setText(dataString);
                points.setText(pointsString);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        uidRef.addListenerForSingleValueEvent(eventListener);
    }

    private void getGender(String gender, String howMuchTasksDone) {
        if (gender.equals("mail")) {

            int counterHowMuchTasksDone = Integer.parseInt(howMuchTasksDone);
            if (counterHowMuchTasksDone == 20) {
                Dialog dialog;
                dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.how_much_was_done);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);

                TextView text = dialog.findViewById(R.id.text_how_much_tasks_youve_done);
                String textCounter = text.getText().toString() + " " + 20;
                text.setText(textCounter + "");
                Button ok = dialog.findViewById(R.id.ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                counterHowMuchTasksDone += 1;
                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("howMuchTasksDone").setValue(counterHowMuchTasksDone + "");
                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("points").setValue("500");
                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imgUri").setValue("boy2");
            } else if (counterHowMuchTasksDone == 50) {
                Dialog dialog;
                dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.how_much_was_done);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);

                TextView text = dialog.findViewById(R.id.text_how_much_tasks_youve_done);
                String textCounter = text.getText().toString() + " " + 50;
                text.setText(textCounter);
                Button ok = dialog.findViewById(R.id.ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                counterHowMuchTasksDone += 1;
                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("points").setValue("500");
                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imgUri").setValue("boy3");
                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("howMuchTasksDone").setValue(counterHowMuchTasksDone + "");
            }

        } else if(gender.equals("femail")) {
            int counterHowMuchTasksDone = Integer.parseInt(howMuchTasksDone);
            if (counterHowMuchTasksDone == 20) {
                Dialog dialog;
                dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.how_much_was_done);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);

                TextView text = dialog.findViewById(R.id.text_how_much_tasks_youve_done);
                String textCounter = text.getText().toString() + " " + 20;
                text.setText(textCounter);
                Button ok = dialog.findViewById(R.id.ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                counterHowMuchTasksDone += 1;
                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("points").setValue("500");
                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imgUri").setValue("girl2");
                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("howMuchTasksDone").setValue(counterHowMuchTasksDone + "");
            } else if (counterHowMuchTasksDone == 50) {
                Dialog dialog;
                dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.how_much_was_done);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);

                TextView text = dialog.findViewById(R.id.text_how_much_tasks_youve_done);
                String textCounter = text.getText().toString() + " " + 50;
                text.setText(textCounter);
                Button ok = dialog.findViewById(R.id.ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                counterHowMuchTasksDone += 1;
                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("points").setValue("500");
                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imgUri").setValue("girl3");
                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("howMuchTasksDone").setValue(counterHowMuchTasksDone + "");
            }
        }
    }

    private void getRatingAndSetAverage() {
        ValueEventListener valRating = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = new Users();
                double total = 0.0;
                double count = 0.0;

                double ratingInDoubleVar;
                if (snapshot != null) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String email = ds.child("email").getValue(String.class);
                        if (email.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            String raiting = ds.child("raiting").getValue(String.class);
                            ratingInDoubleVar = Double.parseDouble(raiting);
                            total += ratingInDoubleVar;
                            count += 1;
                        }
                    }
                    average = total / count;
                    double roundOff = (double) Math.round(average * 100) / 100;
                    users.setAverage(roundOff + "");
                    if (roundOff < 1 && roundOff > 0) {
                        stars.setImageResource(R.drawable.zero_five);
                    } else if (roundOff <= 1.5 && roundOff > 0 && roundOff > 1) {
                        stars.setImageResource(R.drawable.one_five);
                    } else if (roundOff > 0.5 && roundOff > 0 && roundOff < 1.5) {
                        stars.setImageResource(R.drawable.one);
                    } else if (roundOff > 1.5 && roundOff > 0 && roundOff < 2.5) {
                        stars.setImageResource(R.drawable.two);
                    } else if (roundOff >= 2 && roundOff > 0 && roundOff <= 2.5) {
                        stars.setImageResource(R.drawable.two_five);
                    } else if (roundOff > 2.5 && roundOff > 0 && roundOff <= 3) {
                        stars.setImageResource(R.drawable.three);
                    } else if (roundOff > 3 && roundOff > 0 && roundOff <= 3.5) {
                        stars.setImageResource(R.drawable.three_five);
                    } else if (roundOff > 3.5 && roundOff > 0 && roundOff <= 4) {
                        stars.setImageResource(R.drawable.four);
                    } else if (roundOff > 4 && roundOff > 0 && roundOff <= 4.5) {
                        stars.setImageResource(R.drawable.four_five);
                    } else if (roundOff > 4.5 && roundOff > 0 && roundOff <= 5) {
                        stars.setImageResource(R.drawable.five);
                    } else if (roundOff == 0) {
                        stars.setImageResource(R.drawable.zero);
                    }

                    FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("average").setValue(users.getAverage());


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseDatabase.getInstance().getReference("Raiting").addListenerForSingleValueEvent(valRating);

    }

    class ShowAdThread extends Thread{

        public void initilisationAd(){
            MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                    loadRewardedAd();
                }
            });
        }

        private void loadRewardedAd() {
            AdRequest adRequest = new AdRequest.Builder().build();

            RewardedAd.load(getContext(), "ca-app-pub-1029213395711583/5657035996",
                    adRequest, new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            Log.d(TAG, loadAdError.getMessage());
                            mRewardedAd = null;
                            Log.d(TAG, "Ad was falilure.");
                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                            mRewardedAd = rewardedAd;
                            Log.d(TAG, "Ad was loaded.");

                            mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdShowedFullScreenContent() {
                                    Log.d(TAG, "Ad was shown.");
                                    mRewardedAd = null;
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(AdError adError) {
                                    Log.d(TAG, "Ad failed to show.");
                                }

                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    Log.d(TAG, "Ad was dismissed.");
                                    loadRewardedAd();
                                }
                            });

                        }
                    });
        }


        private void showRewardAd(View v) {
            if (mRewardedAd != null) {
                Activity activityContext = getActivity();
                mRewardedAd.show((Activity) getContext(), new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        // Handle the reward.
                        Log.d(TAG, "The user earned the reward.");
                        int rewardAmount = rewardItem.getAmount();
                        String rewardType = rewardItem.getType();

                        FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String points_getting = snapshot.child("points").getValue(String.class);
                                int pointsInteger = Integer.parseInt(points_getting);
                                int result = pointsInteger + 10;
                                points.setText( result + "");
                                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("points").setValue(result + "");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });
            } else {
                Snackbar snackbar = Snackbar.make(v, "Реклама не загружена. Попробуйте снова.", Snackbar.LENGTH_LONG);
                snackbar.setBackgroundTint(0XFF601C80);
                snackbar.setTextColor(0XFFffffff);
                snackbar.show();
            }
        }

        @Override
        public void run() {
            super.run();

            points.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showRewardAd(v);
                    Snackbar snackbar = Snackbar.make(v, "Дождитесь пока загрузиться реклама...", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(0XFFffffff);
                    snackbar.setTextColor(0XFF601C80);
                    snackbar.show();
                }
            });

            addPoints.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showRewardAd(v);
                    Snackbar snackbar = Snackbar.make(v, "Дождитесь пока загрузиться реклама...", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(0XFFffffff);
                    snackbar.setTextColor(0XFF601C80);
                    snackbar.show();
                }
            });
        }
    }

    class ReadUsersThread extends Thread{
        @Override
        public void run() {
            super.run();
            readUser();
        }
    }

}
