package study_com.study_exmp.studytogetherproject.MainClasses;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

import study_com.study_exmp.studytogetherproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    //переменные
    private Button enterBtn;
    private EditText email;
    private FirebaseAuth mAuth;
    private ImageView back;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
                Intent intent = new Intent(getApplicationContext(), LoginOrSignUpActivity.class);
                startActivity(intent);
            }
        });

        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.main));

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
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar snackbar = Snackbar.make(parentLayout, getResources().getString(R.string.try_again), Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(0XFFffffff);
                    snackbar.setTextColor(0XFF601C80);
                    snackbar.show();
                    startActivity(new Intent(getApplicationContext(), LoginOrSignUpActivity.class));
                } else{
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar snackbar = Snackbar.make(parentLayout, getResources().getString(R.string.try_again), Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(0XFF601C80);
                    snackbar.setTextColor(0XFFffffff);
                    snackbar.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(0);
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}