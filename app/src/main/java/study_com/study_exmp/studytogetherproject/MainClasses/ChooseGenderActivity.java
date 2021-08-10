package study_com.study_exmp.studytogetherproject.MainClasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import study_com.study_exmp.studytogetherproject.R;
import study_com.study_exmp.studytogetherproject.Moduls.Users;
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
        conditionForWindowFlag();

        View.OnClickListener images = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(v, getResources().getString(R.string.loading_gender), Snackbar.LENGTH_LONG);
                switch (v.getId()) {
                    case R.id.back:
                        startActivity(new Intent(getApplicationContext(), LoginOrSignUpActivity.class));
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


    private void conditionForWindowFlag(){
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
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
                                "https://firebasestorage.googleapis.com/v0/b/kvantoriumproject-552ef.appspot.com/o/avatars%2Fboy1.jpg?alt=media&token=a8b8df51-8875-4992-9cc9-7272c7a88e53", "500", "0", "5.0", "0", "0", "mail", "online");
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
                                "https://firebasestorage.googleapis.com/v0/b/kvantoriumproject-552ef.appspot.com/o/avatars%2Fgirl1.jpg?alt=media&token=cc2d9a8f-da89-464f-96ac-9d4974a1116c", "500", "0", "5.0", "0", "0", "femail", "online");
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
    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(0);
    }
}