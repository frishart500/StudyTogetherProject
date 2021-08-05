package com.example.studytogetherproject.ui.Notifications;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.studytogetherproject.Chat.ChatActivity;
import com.example.studytogetherproject.Moduls.FinishTask;
import com.example.studytogetherproject.Moduls.Friends;
import com.example.studytogetherproject.R;
import com.example.studytogetherproject.Moduls.Users;
import com.example.studytogetherproject.CommentsAndDetails.DetailActivity;
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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.ViewHolder> {

    private ArrayList<Friends> arrayList = new ArrayList<>();
    private Context context;
    private InterstitialAd mInterstitialAd;
    private final String TAG = "---AdMob";

    public AdapterNotification(Context context, ArrayList<Friends> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new AdapterNotification.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Friends item = arrayList.get(position);
        holder.nameOfTask.setText(item.getNameOfTask());
        holder.name.setText(item.getName());
        Glide.with(context).load(item.getImgUri2()).into(holder.userImg);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, nameOfTask;
        ImageView userImg;
        Button yes, no;
        ImageView imageView;

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            userImg = itemView.findViewById(R.id.imgItem);
            nameOfTask = itemView.findViewById(R.id.nameOfTask);
            yes = itemView.findViewById(R.id.yes);
            no = itemView.findViewById(R.id.no);
            imageView = itemView.findViewById(R.id.imgItem);



            MobileAds.initialize(context, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                    createAd();
                }
            });

            name.setPaintFlags(name.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Friends item = arrayList.get(position);

                    FirebaseDatabase.getInstance().getReference("Friends").child(item.getId()).removeValue();

                    arrayList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), arrayList.size());

                    Users users = new Users();
                    ValueEventListener valUser = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String howMuchNotifications = snapshot.child("howMuchNotifications").getValue(String.class);
                            int parserCounter = Integer.parseInt(howMuchNotifications);
                            if (parserCounter > 0) {
                                parserCounter = parserCounter - 1;
                                users.setHowMuchNotifications(parserCounter + "");
                                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("howMuchNotifications").setValue(users.getHowMuchNotifications());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };
                    FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(valUser);

                    Snackbar snackbar = Snackbar.make(v, "Вы отклонили заявку!", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(0XFFffffff);
                    snackbar.setTextColor(0XFF601C80);
                    snackbar.show();
                }
            });



            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Friends item = arrayList.get(position);
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("title", item.getName());
                    intent.putExtra("email", item.getMyEmail());
                    intent.putExtra("id", item.getAnotherId());
                    intent.putExtra("describe", item.getDescribtionOfUser());
                    intent.putExtra("subjectToDetail", item.getSubjectOfUser());

                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            (Activity) context, imageView, ViewCompat.getTransitionName(imageView));

                    context.startActivity(intent, options.toBundle());
                }
            });


            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mInterstitialAd != null) {
                        mInterstitialAd.show((Activity) context);
                    } else {
                        Log.d("TAG", "The interstitial ad wasn't ready yet.");
                        funkGoChat();
                    }
                }
            });

        }

        private void funkGoChat() {

            Friends item = arrayList.get(getAdapterPosition());
            String name = item.getName();
            String nameOfTask = item.getNameOfTask();
            String price = item.getPoints();
            String phone = item.getPhone();
            String classText = item.getClassText();
            String dateToFinish = item.getDateToFinish();
            String describe = item.getDescribe();
            String email = item.getEmail();
            String myEmail = item.getMyEmail();
            String userId = item.getUserId();
            String anotherId = item.getAnotherId();
            String justId = item.getIdOfTask();

            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("nameOfTask", nameOfTask);
            intent.putExtra("name", name);
            intent.putExtra("price", price);
            intent.putExtra("dateToFinish", dateToFinish);
            intent.putExtra("describeTask", describe);
            intent.putExtra("phone", phone);
            intent.putExtra("classText", classText);
            intent.putExtra("email", email);
            intent.putExtra("myEmail", myEmail);
            intent.putExtra("userId", userId);
            intent.putExtra("justId", justId);
            intent.putExtra("anotherId", anotherId);

            FinishTask finishTask = new FinishTask();
            finishTask.setId1("false");
            finishTask.setId2("false");
            finishTask.setUID1("");
            finishTask.setUID2("");
            finishTask.setIdOfTask(justId);
            FirebaseDatabase.getInstance().getReference("FinishTask").push().setValue(finishTask);

            Users users = new Users();
            ValueEventListener valUser = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String howMuchNotifications = snapshot.child("howMuchNotifications").getValue(String.class);
                    int parserCounter = Integer.parseInt(howMuchNotifications);
                    if (parserCounter > 0) {
                        parserCounter = parserCounter - 1;
                        users.setHowMuchNotifications(parserCounter + "");
                        FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("howMuchNotifications").setValue(users.getHowMuchNotifications());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(valUser);

            DatabaseReference dr = FirebaseDatabase.getInstance().getReference("FriendsInChats");

            Friends friends = new Friends();
            friends.setName("name");
            friends.setImgUri1("imgUri1");
            friends.setImgUri2("imgUri2");
            friends.setNameAnotherUser(item.getName());
            friends.setSubjectOfUser("subject");
            friends.setDescribtionOfUser("describtionOfUser");
            friends.setEmail(item.getEmail());
            friends.setMyEmail(item.getMyEmail());
            friends.setNameOfTask(item.getNameOfTask());
            friends.setPoints(item.getPoints());
            friends.setSubject(item.getSubject());
            friends.setPhone(item.getPhone());
            friends.setClassText(item.getClassText());
            friends.setDescribe(item.getDescribe());
            friends.setDateToFinish(item.getDateToFinish());
            friends.setUserId(item.getUserId());
            friends.setIdOfTask(item.getIdOfTask());
            friends.setAnotherId(item.getAnotherId());

            ValueEventListener val = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String s = "";
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        s = ds.getKey();
                        System.out.println(s);
                        friends.setId(s);
                    }
                    dr.child(s).child("id").setValue(friends.getId());
                    intent.putExtra("id", friends.getId());
                    String finalS = s;
                    ValueEventListener valUsers = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String sUser = "";
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                sUser = ds.getKey();
                                friends.setUserId(sUser);

                                ValueEventListener val = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String name = snapshot.child("name").getValue(String.class);
                                        String imgUri = snapshot.child("imgUri").getValue(String.class);
                                        String subject = snapshot.child("subject").getValue(String.class);
                                        String describtionOfUser = snapshot.child("describtion").getValue(String.class);
                                        friends.setName(name);
                                        friends.setImgUri1(imgUri);
                                        friends.setSubjectOfUser(subject);
                                        friends.setDescribtionOfUser(describtionOfUser);
                                        dr.child(finalS).child("name").setValue(friends.getName());
                                        dr.child(finalS).child("imgUri1").setValue(friends.getImgUri1());
                                        dr.child(finalS).child("describtionOfUser").setValue(friends.getDescribtionOfUser());
                                        dr.child(finalS).child("subjectOfUser").setValue(friends.getSubjectOfUser());
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                };
                                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(val);

                            }


                            ValueEventListener valFriends = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        String userId = ds.child("userId").getValue(String.class);
                                        String anotherId = ds.child("anotherId").getValue(String.class);
                                        String imgUri2 = ds.child("imgUri2").getValue(String.class);
                                        String imgUri1 = ds.child("imgUri1").getValue(String.class);
                                        friends.setUserId(userId);
                                        friends.setImgUri2(imgUri2);
                                        friends.setImgUri1(imgUri1);
                                        friends.setAnotherId(anotherId);
                                        dr.child(finalS).child("userId").setValue(userId);
                                        dr.child(finalS).child("imgUri2").setValue(friends.getImgUri2());
                                        dr.child(finalS).child("imgUri1").setValue(imgUri1);
                                        dr.child(finalS).child("anotherId").setValue(anotherId);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            };
                            FirebaseDatabase.getInstance().getReference("Friends").addListenerForSingleValueEvent(valFriends);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };
                    FirebaseDatabase.getInstance().getReference("User").addListenerForSingleValueEvent(valUsers);

                    dr.child(finalS).child("imgUri2").setValue(friends.getImgUri2());
                    dr.child(finalS).child("name").setValue(friends.getName());
                    dr.child(finalS).child("userId").setValue(friends.getUserId());
                    dr.child(finalS).child("anotherId").setValue(friends.getAnotherId());
                    dr.child(finalS).child("nameAnotherUser").setValue(friends.getNameAnotherUser());
                    dr.child(finalS).child("email").setValue(friends.getEmail());
                    dr.child(finalS).child("nameOfTask").setValue(friends.getNameOfTask());
                    dr.child(finalS).child("points").setValue(friends.getPoints());
                    dr.child(finalS).child("phone").setValue(friends.getPhone());
                    dr.child(finalS).child("classText").setValue(friends.getClassText());
                    dr.child(finalS).child("describe").setValue(friends.getDescribe());
                    dr.child(finalS).child("img").setValue(friends.getImg());
                    dr.child(finalS).child("dateToFinish").setValue(friends.getDateToFinish());
                    dr.child(finalS).child("myEmail").setValue(friends.getMyEmail());
                    dr.child(finalS).child("justId").setValue(friends.getIdOfTask());


                    ValueEventListener getImgUri2 = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String imgUri2 = snapshot.child("imgUri").getValue(String.class);
                            friends.setImgUri2(imgUri2);
                            dr.child(finalS).child("imgUri2").setValue(friends.getImgUri2());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };
                    FirebaseDatabase.getInstance().getReference("User").child(friends.getAnotherId()).addListenerForSingleValueEvent(getImgUri2);
                    dr.child(finalS).child("imgUri2").setValue(friends.getImgUri2());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            dr.push().setValue(friends);

            FirebaseDatabase.getInstance().getReference("FriendsInChats").addListenerForSingleValueEvent(val);

            FirebaseDatabase.getInstance().getReference("Friends").child(item.getId()).removeValue();

            arrayList.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
            notifyItemRangeChanged(getAdapterPosition(), arrayList.size());

            FirebaseDatabase.getInstance().getReference("Task").child(item.getIdOfTask()).removeValue();

            context.startActivity(intent);
        }

        private void createAd() {
            AdRequest adRequest = new AdRequest.Builder().build();
            createIntertitialAd(adRequest);
        }

        private void createIntertitialAd(AdRequest adRequest) {
            InterstitialAd.load(context, "ca-app-pub-1029213395711583/9528832398", adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    mInterstitialAd = interstitialAd;
                    Log.d(TAG, "onAdLoaded");

                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            Log.d("TAG", "The ad was dismissed.");

                            funkGoChat();
                            //context.startActivity(new Intent(context, MainActivity.class));
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
    }
}
