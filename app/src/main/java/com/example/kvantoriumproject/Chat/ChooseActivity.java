package com.example.kvantoriumproject.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.example.kvantoriumproject.Moduls.ChooseInf;
import com.example.kvantoriumproject.MainClasses.MainActivity;
import com.example.kvantoriumproject.R;
import com.example.kvantoriumproject.Moduls.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChooseActivity extends AppCompatActivity {
    private EditText comment;
    private RatingBar defaultRatingBar;
    private Button btn_send;
    private String getExtraEmail, getExtraMyEmail;
    private ImageView back;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

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

    private void status(String status){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        Users users = new Users();
        users.setStatus(status);
        ref.child("status").setValue(status);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

    private void sendInformation(){
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
                                if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(getExtraEmail)){
                                    choose.setEmail(getIntent().getStringExtra("myEmail"));
                                }else{
                                    choose.setEmail(getExtraEmail);
                                }

                                choose.setComment(comment.getText().toString());

                                choose.setName(name);
                                choose.setMyEmail(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                choose.getRatingMiddle();
                                FirebaseDatabase.getInstance().getReference("Raiting").push().setValue(choose);
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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