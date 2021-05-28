package com.example.studytogetherproject.MainClasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.studytogetherproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    //переменные
    private EditText email, password;
    private FirebaseAuth mAuth;
    private Button btnEnter;
    private TextView regBtn, forgetPassword, textView2, textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //базовые настройки
        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //методы
        init();
        setOnClickEnter();
        setOnClick();
        checkInternetConection();

    }


    //проверка налиия интернета
    private void checkInternetConection() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
            //диалоговое окно
            Dialog dialog;
            dialog = new Dialog(LoginActivity.this);//создание объекта
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//без Наиминования
            dialog.setContentView(R.layout.internet_conection);//меняем layout
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);//не закрываться сразу
            //переменные в диалоговом окне
            TextView exit = dialog.findViewById(R.id.exit);
            TextView settings = dialog.findViewById(R.id.settings);
            //нажатия
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishAffinity();
                    System.exit(0);//выход из приложения
                }
            });

            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //переход в настройки
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                }
            });

            dialog.show();
        }
    }

    private void init() {
        //создание объектов
        email = findViewById(R.id.email);
        textView2 = findViewById(R.id.textView2);
        password = findViewById(R.id.password);
        btnEnter = findViewById(R.id.enterBtn);
        regBtn = findViewById(R.id.registration);
        textView = findViewById(R.id.textView);
        forgetPassword = findViewById(R.id.forgetPassword);
        mAuth = FirebaseAuth.getInstance();
        //подчеркивание текста
        regBtn.setPaintFlags(regBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        forgetPassword.setPaintFlags(regBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private void setOnClickEnter() {
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //вход по нажатию
                Snackbar snackbar = Snackbar.make(v, "Вход...", Snackbar.LENGTH_LONG);
                snackbar.setBackgroundTint(0XFFffffff);
                snackbar.setTextColor(0XFF601C80);
                snackbar.show();
                signIn(email.getText().toString().trim(), password.getText().toString().trim());
            }
        });
    }

    private void setOnClick() {
        View.OnClickListener BTNs = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.registration:
                        //переход в экран регистрации
                        startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
                        break;
                    case R.id.forgetPassword:
                        //переход в экран "забыли пароль"
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
            if (!emailS.trim().isEmpty() && !passwordS.isEmpty()) {//проверка был ли введен текст
                //вход по логину и паролю
                mAuth.signInWithEmailAndPassword(emailS, passwordS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            View parentLayout = findViewById(android.R.id.content);
                            Snackbar snackbar = Snackbar.make(parentLayout, "Вход провален.", Snackbar.LENGTH_LONG);
                            snackbar.setBackgroundTint(0XFF601C80);
                            snackbar.setTextColor(0XFFffffff);
                            snackbar.show();
                        }
                    }
                });

            } else if (emailS.isEmpty()) {//если невведен текст
                showError(email, "Введите email");
            } else if (passwordS.isEmpty() | passwordS.length() < 6) {
                showError(password, "Введите пароль");
            }

        } catch (Exception ex) {
        }
    }

    private void showError(EditText ed, String errorText) {
        ed.setError(errorText);
        ed.requestFocus();
    }

    @Override
    public void onBackPressed() {
        //диалоговое окно
        Dialog dialog;
        dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.exit_from_app_dialog);
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
                finishAffinity();
                System.exit(0);
            }
        });

        dialog.show();

    }

}