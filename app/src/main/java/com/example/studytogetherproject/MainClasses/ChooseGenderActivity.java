package com.example.studytogetherproject.MainClasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.studytogetherproject.R;
import com.example.studytogetherproject.Moduls.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ChooseGenderActivity extends AppCompatActivity {
    //переменные
    private ImageView back, boy, girl;
    private FirebaseAuth mAuth;
    private String email, name, phone, points, data, describtion, subject,
            imgUri, average, countOfHowMuchTasksCreated, howMuchNotifications, howMuchTasksDone, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_gender);
        //базовая настрйка
        getSupportActionBar().hide();
        //иницилизация
        init();
        //получение с помощью intent
        getExtras();

        View.OnClickListener images = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(v, "Загрузка...", Snackbar.LENGTH_LONG);
                switch (v.getId()) {
                    case R.id.back:
                        startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
                        break;
                    case R.id.mail:
                        registrationMail();
                        snackbar.setBackgroundTint(0XFFffffff);
                        snackbar.setTextColor(0XFF601C80);
                        snackbar.show();
                        break;
                    case R.id.femail:
                        registrationFemail();
                        snackbar.setBackgroundTint(0XFFffffff);
                        snackbar.setTextColor(0XFF601C80);
                        snackbar.show();
                        break;
                }
            }
        };
        //set listeners
        boy.setOnClickListener(images);
        girl.setOnClickListener(images);
        back.setOnClickListener(images);
    }


    private void registrationMail(){
        try {
            //регистрация для мальчика
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //объект User
                        Users users = new Users(email, name, data, describtion, phone, subject,
                                "boy1", "500", "0", "5.0", "0", "0", "mail", "online");
                        users.setId(mAuth.getCurrentUser().getUid());
                        FirebaseDatabase.getInstance().getReference("User").child(mAuth.getCurrentUser().getUid()).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {//если успешно
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }
                            }
                        });
                    }
                }
            });
        } catch (Exception ex) {
        }

    }


    private void registrationFemail(){
        //регистрация для девушки
        try {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Users users = new Users(email, name, data, describtion, phone, subject,
                                "girl1", "500", "0", "5.0", "0", "0", "femail", "online");
                        users.setId(mAuth.getCurrentUser().getUid());
                        FirebaseDatabase.getInstance().getReference("User").child(mAuth.getCurrentUser().getUid()).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        } catch (Exception ex) {
        }
    }


    private void init(){
        back = findViewById(R.id.back);
        boy = findViewById(R.id.mail);
        girl = findViewById(R.id.femail);
        mAuth = FirebaseAuth.getInstance();
    }

    private void getExtras(){
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
    }

}