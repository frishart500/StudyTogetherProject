package com.example.kvantoriumproject.MainClasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MicrophoneInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kvantoriumproject.R;
import com.example.kvantoriumproject.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChooseGenderActivity extends AppCompatActivity {
private ImageView back, boy, girl;
private FirebaseAuth mAuth;
private String email, name, phone, points, data, describtion, subject, imgUri, average, countOfHowMuchTasksCreated, howMuchNotifications, howMuchTasksDone, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_gender);
        back = findViewById(R.id.back);
        boy = findViewById(R.id.mail);
        girl = findViewById(R.id.femail);
        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();

        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        name = getIntent().getStringExtra("name");
        describtion = getIntent().getStringExtra("describtion");
        points = getIntent().getStringExtra("points");
        phone = getIntent().getStringExtra("phone");
        data = getIntent().getStringExtra("data");
        subject = getIntent().getStringExtra("subject");
        imgUri = getIntent().getStringExtra("imgUri");
        average = getIntent().getStringExtra("average");
        countOfHowMuchTasksCreated = getIntent().getStringExtra("countOfHowMuchTasksCreated");
        howMuchNotifications = getIntent().getStringExtra("howMuchNotifications");
        howMuchTasksDone = getIntent().getStringExtra("howMuchTasksDone");
        View.OnClickListener images = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.back:
                        startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
                        break;
                    case R.id.mail:
                        try {
                            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        User user = new User(email, name, data, describtion, phone, subject,
                                                "boy1", "500", "0", "5.0", "0", "0","mail", "online");
                                        user.setId(mAuth.getCurrentUser().getUid());
                                        FirebaseDatabase.getInstance().getReference("User").child(mAuth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        } catch (Exception ex) {}
                        break;
                    case R.id.femail:
                        try {
                            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        User user = new User(email, name, data, describtion, phone, subject,
                                                "girl1", "500", "0", "5.0", "0", "0","femail", "online");
                                        user.setId(mAuth.getCurrentUser().getUid());
                                        FirebaseDatabase.getInstance().getReference("User").child(mAuth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        } catch (Exception ex) {}
                        break;
                }
            }
        };
        boy.setOnClickListener(images);
        girl.setOnClickListener(images);
        back.setOnClickListener(images);
    }
}