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
    private Button enterBtn;
    private EditText email;
    FirebaseAuth mAuth;
    private ImageView back;
    private ProgressDialog mLoadingBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        enterBtn = findViewById(R.id.enterBtn);
        email = findViewById(R.id.email);
        back = findViewById(R.id.back);

        setStatusBarColor();
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

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
    private void resetPassword(){

        mLoadingBar = new ProgressDialog(ForgetPasswordActivity.this);

        mLoadingBar.setTitle("Отправка сообщения на почту");
        mLoadingBar.setMessage("Пожалуйста подождите, пока мы проверяем ваши полномочия");
        mLoadingBar.setCanceledOnTouchOutside(false);
        mLoadingBar.show();

        String emailString = email.getText().toString().trim();
        if(emailString.isEmpty()){
            showError(email, "Введите email ввашей почты");
            //email.requestFocus();
            //return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()){
            showError(email, "Поожалуйста убедитесь в правильносте email");
            //email.requestFocus();
            //return;
        }

        mAuth.sendPasswordResetEmail(emailString).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    mLoadingBar.dismiss();
                    Toast.makeText(getApplicationContext(), "Вы успешно получили письмо на почту!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }else
                    Toast.makeText(ForgetPasswordActivity.this, "Попробуйте снова!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showError(EditText input, String s){
        input.setError(s);
        input.requestFocus();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarColor(){
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
    }
}