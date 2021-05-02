package com.example.kvantoriumproject.MainClasses;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kvantoriumproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    //переменные
    private Button enterBtn;
    private EditText email;
    private FirebaseAuth mAuth;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        //иницилизация
        init();
        //activity без actionBar (базавая настройка)
        getSupportActionBar().hide();
        //нажатия
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        enterBtn = findViewById(R.id.enterBtn);
        email = findViewById(R.id.email);
        back = findViewById(R.id.back);
    }

    private void resetPassword() {
        String emailString = email.getText().toString().trim();
        if (emailString.isEmpty()) {
            showError(email, "Введите email ввашей почты");
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            showError(email, "Поожалуйста убедитесь в правильносте email");
        }
        mAuth.sendPasswordResetEmail(emailString).addOnCompleteListener(new OnCompleteListener<Void>() {
            //отправка письма на почту со сменой email
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Вы успешно получили письмо на почту!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                } else
                    Toast.makeText(ForgetPasswordActivity.this, "Попробуйте снова!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}