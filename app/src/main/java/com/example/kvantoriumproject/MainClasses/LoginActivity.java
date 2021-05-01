package com.example.kvantoriumproject.MainClasses;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kvantoriumproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private FirebaseAuth mAuth;
    private Button btnEnter;
    private TextView regBtn, forgetPassword, textView2, textView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        setOnClickEnter();
        setOnClick();
        checkInternetConection();

        getSupportActionBar().hide();
        //        exit.setPaintFlags(exit.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        /*if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
        }*/
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


    }

    private void checkInternetConection(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }
        else{
            connected = false;
            Dialog dialog;
            dialog = new Dialog(LoginActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.internet_conection);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            TextView exit = dialog.findViewById(R.id.exit);
            TextView settings = dialog.findViewById(R.id.settings);
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishAffinity();
                    System.exit(0);
                }
            });

            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                }
            });

            dialog.show();
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

        regBtn.setPaintFlags(regBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        forgetPassword.setPaintFlags(regBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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