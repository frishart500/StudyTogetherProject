package com.example.studytogetherproject.MainClasses;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.studytogetherproject.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginFragment extends Fragment {

    //переменные
    private EditText email, password;
    private FirebaseAuth mAuth;
    private Button btnEnter;
    private TextView forgetPassword;
    private CardView loginCard, passwordCard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //методы
        init(view);
        setOnClickEnter();
        setOnClick();
        checkInternetConection();


        return view;
    }



    //проверка налиия интернета
    private void checkInternetConection() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) ((Activity) getContext()).getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
            //диалоговое окно
            Dialog dialog;
            dialog = new Dialog(getContext());//создание объекта
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
                    ((Activity) getContext()).finishAffinity();
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

    private void init(View view) {
        //создание объектов
        loginCard = view.findViewById(R.id.cardView1);
        passwordCard = view.findViewById(R.id.cardView2);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        btnEnter = view.findViewById(R.id.enterBtn);
        forgetPassword = view.findViewById(R.id.forgetPassword);
        forgetPassword.setPaintFlags(forgetPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mAuth = FirebaseAuth.getInstance();
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
                signIn(email.getText().toString().trim(), password.getText().toString().trim(), v);
            }
        });
    }

    private void setOnClick() {
        View.OnClickListener BTNs = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.forgetPassword:
                        //переход в экран "забыли пароль
                        startActivity(new Intent(getContext(), ForgetPasswordActivity.class));
                        break;
                }
            }
        };
        forgetPassword.setOnClickListener(BTNs);
    }

    private void signIn(String emailS, String passwordS, View v) {
        try {
            if (!emailS.trim().isEmpty() && !passwordS.isEmpty()) {//проверка был ли введен текст
                //вход по логину и паролю
                mAuth.signInWithEmailAndPassword(emailS, passwordS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(getContext(), MainActivity.class));
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


}