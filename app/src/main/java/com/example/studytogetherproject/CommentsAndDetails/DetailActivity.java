package com.example.studytogetherproject.CommentsAndDetails;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.studytogetherproject.MainClasses.MainActivity;
import com.example.studytogetherproject.R;
import com.example.studytogetherproject.Moduls.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailActivity extends AppCompatActivity {
    private TextView name, describe, subject;
    private TextView goToComments;
    private ImageView back, userImg, stars;
    private String email_string, id_string;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        id_string = getIntent().getStringExtra("id");
        init();
        getIntentInDetail();
        getSupportActionBar().hide();

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.mainLight));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailActivity.this, MainActivity.class));
            }
        });
        goToComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, CommentActivity.class);
                email_string = getIntent().getStringExtra("email");
                intent.putExtra("email", id_string);
                startActivity(intent);
            }
        });
        getRatingOfUser();
    }

    private void init() {
        back = findViewById(R.id.back);
        userImg = findViewById(R.id.userImg);
        goToComments = findViewById(R.id.goToComments);
        goToComments.setPaintFlags(goToComments.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        name = findViewById(R.id.name);
        describe = findViewById(R.id.describeInDetail);
        subject = findViewById(R.id.subject);
        stars = findViewById(R.id.stars);
    }

    private void getIntentInDetail() {
        String nameIntent = getIntent().getStringExtra("title");
        String subject_string = getIntent().getStringExtra("subjectToDetail");
        String describe_string = getIntent().getStringExtra("describe");


        ValueEventListener valUserImage = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String imgUri = snapshot.child("imgUri").getValue(String.class);
                if (imgUri.equals("boy1")) {
                    userImg.setImageResource(R.drawable.boy1);
                } else if (imgUri.equals("boy2")) {
                    userImg.setImageResource(R.drawable.boy2);
                } else if (imgUri.equals("boy3")) {
                    userImg.setImageResource(R.drawable.boy3);
                } else if (imgUri.equals("girl1")) {
                    userImg.setImageResource(R.drawable.girl1);
                } else if (imgUri.equals("girl2")) {
                    userImg.setImageResource(R.drawable.girl2);
                } else if (imgUri.equals("girl3")) {
                    userImg.setImageResource(R.drawable.girl3);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseDatabase.getInstance().getReference("User").child(id_string).addListenerForSingleValueEvent(valUserImage);

        name.setText(nameIntent);
        describe.setText(describe_string);
        subject.setText(subject_string);
    }

    private void getRatingOfUser() {
        ValueEventListener valRating = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                id_string = getIntent().getStringExtra("id");
                Users users = new Users();
                double total = 0.0;
                double count = 0.0;
                double average = 0.0;
                double ratingInDoubleVar;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String email = ds.child("email").getValue(String.class);

                    email_string = getIntent().getStringExtra("email");

                    if (email.equals(id_string)) {
                        String raiting = ds.child("raiting").getValue(String.class);
                        ratingInDoubleVar = Double.parseDouble(raiting);
                        total += ratingInDoubleVar;
                        count += 1;
                    }
                }
                average = total / count;
                double roundOff = (double) Math.round(average * 100) / 100;
                users.setAverage(String.valueOf(roundOff));
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
                FirebaseDatabase.getInstance().getReference("User").child(id_string).child("average").setValue(users.getAverage());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseDatabase.getInstance().getReference("Raiting").addListenerForSingleValueEvent(valRating);
    }
}