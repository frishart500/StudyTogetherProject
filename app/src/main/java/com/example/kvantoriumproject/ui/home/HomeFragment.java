package com.example.kvantoriumproject.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.security.FileIntegrityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.kvantoriumproject.ChooseInf;
import com.example.kvantoriumproject.MainClasses.LoginActivity;
import com.example.kvantoriumproject.MainClasses.MainActivity;
import com.example.kvantoriumproject.R;
import com.example.kvantoriumproject.User;
import com.example.kvantoriumproject.ui.dashboard.Adapter;
import com.example.kvantoriumproject.ui.dashboard.CommentActivity;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;


public class HomeFragment extends Fragment {
    private ImageView userImg, notification, stars;
    private TextView email, phone, points, data, name, subject, exit, goToComments, describeInProfile, textHowMuchNotifications;
    private FirebaseAuth mAuth;
    private Button changeBtn;
    private double average = 0.0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initView(root);
        readUser();
        getRatingAndSetAverage();


        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NotificationActivity.class));
            }
        });

        goToComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CommentActivity.class);
                //intent.putExtra("email", mAuth.getCurrentUser().getUid());
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
                        mAuth.signOut();
                        startActivity(new Intent(getContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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

    private void initView(View root){
        exit = root.findViewById(R.id.exit);
        textHowMuchNotifications = root.findViewById(R.id.text_how_much);
        notification = root.findViewById(R.id.notification);
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
                String pointsString = dataSnapshot.child("points").getValue(String.class);
                email.setText(emailString);
                name.setText(nameS);
                String expertSubjectString = dataSnapshot.child("subject").getValue(String.class);
                subject.setText(expertSubjectString);
                String phoneString = dataSnapshot.child("phone").getValue(String.class);
                phone.setText(phoneString);
                String dataString = dataSnapshot.child("data").getValue(String.class);
                String howMuchTasksDone = dataSnapshot.child("howMuchTasksDone").getValue(String.class);
                String howMuchNotifications = dataSnapshot.child("howMuchNotifications").getValue(String.class);
                String gender = dataSnapshot.child("gender").getValue(String.class);

                getGender(gender, howMuchTasksDone);


                int counterOfNotifications = Integer.parseInt(howMuchNotifications);
                if(counterOfNotifications == 0){
                    textHowMuchNotifications.setVisibility(View.GONE);
                    notification.setVisibility(View.VISIBLE);
                }else{
                    textHowMuchNotifications.setVisibility(View.VISIBLE);
                    textHowMuchNotifications.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getContext(), NotificationActivity.class));
                        }
                    });
                    notification.setVisibility(View.GONE);
                }

                String describeProfile = dataSnapshot.child("describtion").getValue(String.class);
                describeInProfile.setText(describeProfile);

                textHowMuchNotifications.setText("");
                data.setText(dataString);
                points.setText(pointsString);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        uidRef.addListenerForSingleValueEvent(eventListener);
    }

    private void getGender(String gender, String howMuchTasksDone){
        if(gender.equals("mail")){

            userImg.setImageResource(R.drawable.boy1);

            int counterHowMuchTasksDone = Integer.parseInt(howMuchTasksDone);
            if(counterHowMuchTasksDone == 20){
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
                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("howMuchTasksDone").setValue(counterHowMuchTasksDone);
                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imgUri").setValue("boy2");
                userImg.setImageResource(R.drawable.boy2);
            }else if(counterHowMuchTasksDone == 50){
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
                userImg.setImageResource(R.drawable.boy3);
                counterHowMuchTasksDone += 1;
                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imgUri").setValue("boy3");
                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("howMuchTasksDone").setValue(counterHowMuchTasksDone);
            }

        }else{
            userImg.setImageResource(R.drawable.girl1);

            int counterHowMuchTasksDone = Integer.parseInt(howMuchTasksDone);
            if(counterHowMuchTasksDone == 20){
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
                userImg.setImageResource(R.drawable.girl2);
                counterHowMuchTasksDone += 1;
                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imgUri").setValue("girl2");
                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("howMuchTasksDone").setValue(counterHowMuchTasksDone);
            }else if(counterHowMuchTasksDone == 50){
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
                userImg.setImageResource(R.drawable.girl3);
                counterHowMuchTasksDone += 1;
                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imgUri").setValue("girl3");
                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("howMuchTasksDone").setValue(counterHowMuchTasksDone);
            }
        }
    }

    private void getRatingAndSetAverage() {
        ValueEventListener valRating = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = new User();
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
                    user.setAverage(roundOff + "");
                    if(roundOff < 1 && roundOff > 0){
                        stars.setImageResource(R.drawable.zero_five);
                    }else if(roundOff <= 1.5 && roundOff > 0 && roundOff > 1){
                        stars.setImageResource(R.drawable.one_five);
                    }else if(roundOff > 0.5 && roundOff > 0 && roundOff < 1.5){
                        stars.setImageResource(R.drawable.one);
                    }else if(roundOff > 1.5 && roundOff > 0 && roundOff < 2.5){
                        stars.setImageResource(R.drawable.two);
                    }else if(roundOff >= 2 && roundOff > 0 && roundOff <= 2.5){
                        stars.setImageResource(R.drawable.two_five);
                    }else if(roundOff > 2.5 && roundOff > 0 && roundOff <= 3){
                        stars.setImageResource(R.drawable.three);
                    }else if(roundOff > 3 && roundOff > 0 && roundOff <= 3.5){
                        stars.setImageResource(R.drawable.three_five);
                    }else if(roundOff > 3.5 && roundOff > 0 && roundOff <= 4){
                        stars.setImageResource(R.drawable.four);
                    }else if(roundOff > 4 && roundOff > 0 && roundOff <= 4.5){
                        stars.setImageResource(R.drawable.four_five);
                    }else if(roundOff > 4.5 && roundOff > 0 && roundOff <= 5){
                        stars.setImageResource(R.drawable.five);
                    }else if(roundOff == 0){
                        stars.setImageResource(R.drawable.zero);
                    }

                    FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("average").setValue(user.getAverage());


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseDatabase.getInstance().getReference("Raiting").addListenerForSingleValueEvent(valRating);

    }


}