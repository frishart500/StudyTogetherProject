package com.example.kvantoriumproject.MainClasses;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kvantoriumproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class RegistrationActivity extends AppCompatActivity {
    //переменные
    private FirebaseAuth mAuth;
    private ImageView dataImg, back;
    private TextView regText, textOfImage;
    private Button reg;
    private EditText email, password, name, phone, data, describtion, subject;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatabaseReference mDataBase;
    private Uri uploadUri = null;
    private StorageReference mStorageRef;
    private boolean isReached = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getSupportActionBar().hide();

        init();
        setOnClickReg();
        getDataPicker();


    }

    private void getDataPicker() {
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "." + month + "." + year;
                String m = String.valueOf(month);
                String d = String.valueOf(day);
                System.out.println("MONTH " + month);
                if (String.valueOf(day).length() == 1) {
                    d = ("0" + day);
                }

                if (String.valueOf(month).length() == 1) {
                    m = ("0" + month);
                }

                date = d + "." + m + "." + year;


                data.setText(date);
            }
        };

        dataImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RegistrationActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("ImageDB");

        regText = findViewById(R.id.textView2);
        back = findViewById(R.id.back);
        reg = findViewById(R.id.registrBtn);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        describtion = findViewById(R.id.describe);
        phone = findViewById(R.id.phone);
        data = findViewById(R.id.editData);
        subject = findViewById(R.id.subject);
        dataImg = findViewById(R.id.dataImg);
    }

    private void setOnClickReg() {
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration(email.getText().toString().trim(), password.getText().toString().trim());
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    private void registration(String emailS, String passwordS) {

        if (!emailS.isEmpty() && !data.getText().toString().isEmpty() && !passwordS.isEmpty() && !name.getText().toString().isEmpty() && !describtion.getText().toString().isEmpty() && !subject.getText().toString().trim().isEmpty() && !phone.getText().toString().isEmpty()) {

            Intent intent = new Intent(getApplicationContext(), ChooseGenderActivity.class);
            intent.putExtra("email", email.getText().toString());
            intent.putExtra("password", passwordS);
            putIntent(intent);
            startActivity(intent);

        } else if (emailS.isEmpty()) {
            showError(email, "Введите email");
        } else if (passwordS.isEmpty() && passwordS.length() < 6) {
            showError(password, "Введите пароль, который должен иметь не менее 6 символов");
        } else if (data.getText().toString().isEmpty()) {
            showError(data, "Введите дату рождения");
        } else if (describtion.getText().toString().isEmpty() && describtion.getText().toString().length() <= 60) {
            showError(describtion, "Введите описание, которое должно содержать менее 60 символов");
        } else if (phone.getText().toString().isEmpty()) {
            showError(phone, "Введите номер телефона");
        } else if (subject.getText().toString().isEmpty()) {
            showError(subject, "Введите предмет");
        }
    }

    private void putIntent(Intent intent){
        intent.putExtra("name", name.getText().toString());
        intent.putExtra("data", data.getText().toString());
        intent.putExtra("describtion", describtion.getText().toString());
        intent.putExtra("phone", phone.getText().toString());
        intent.putExtra("subject", subject.getText().toString());
        intent.putExtra("imgUri", "");
        intent.putExtra("points", "500");
        intent.putExtra("imgUri", "0");
        intent.putExtra("average", "5.0");
        intent.putExtra("howMuchTaskDone", "0");
        intent.putExtra("howMuchNotifications", "0");
    }

    private void showError(EditText ed, String error) {
        ed.setError(error);
        ed.requestFocus();
    }
}