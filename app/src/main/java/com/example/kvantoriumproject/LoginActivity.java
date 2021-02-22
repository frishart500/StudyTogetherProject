package com.example.kvantoriumproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private FirebaseAuth mAuth;
    private Button btnEnter;
    private TextView regBtn, forgetPassword, textView2, textView;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        setOnClickEnter();
        setOnClick();

        getSupportActionBar().hide();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
        }

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // ночная тема не активна, используется светлая тема
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // ночная тема активна, и она используется
                regBtn.setTextColor(Color.parseColor("#FFFFFF"));
                forgetPassword.setTextColor(Color.parseColor("#FFFFFF"));
                textView2.setTextColor(Color.parseColor("#FFFFFF"));
                textView.setTextColor(Color.parseColor("#FFFFFF"));
                break;
        }

    }

    private void init() {
        email = findViewById(R.id.email);
        textView2 = findViewById(R.id.textView2);
        password = findViewById(R.id.password);
        btnEnter = findViewById(R.id.enterBtn);
        regBtn = findViewById(R.id.registration);
        textView = findViewById(R.id.textView);
        forgetPassword = findViewById(R.id.forgetPassword);
        mAuth = FirebaseAuth.getInstance();
    }

    private void setOnClickEnter(){
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(email.getText().toString().trim(), password.getText().toString().trim());
            }
        });
    }

    private void setOnClick(){
        View.OnClickListener BTNs = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.registration:
                        startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
                        break;
                    case R.id.forgetPassword:
                        startActivity(new Intent(getApplicationContext(), ForgetPasswordActivity.class));
                        break;
                }
            }
        };
        regBtn.setOnClickListener(BTNs);
        forgetPassword.setOnClickListener(BTNs);
    }

    private void signIn(String emailS, String passwordS) {
        try {
            if (!emailS.trim().isEmpty() && !passwordS.isEmpty()) {
                mAuth.signInWithEmailAndPassword(emailS, passwordS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Вход успешен", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else
                            Toast.makeText(LoginActivity.this, "Взод провален", Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (emailS.isEmpty()) {
                showError(email, "Введите email");
            } else if (passwordS.isEmpty() | passwordS.length() < 6) {
                showError(password, "Введите пароль");
            }

        }catch (Exception ex){}
    }

    private void showError(EditText ed, String errorText) {
        ed.setError(errorText);
        ed.requestFocus();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Выход")
                .setMessage("Вы действительно хотите выйти из приложения?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("Нет", null).show();
    }

}